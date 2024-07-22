import javax.swing.*;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


/**
 * Created by a StringParserDispatcher to process the Dispatcher's input.<br><br>
 *
 */
public class StringParserBoss extends SwingWorker<String,String> {

    /**
     * The worker threads that this Boss employs. The array's length may vary depending on the number of threads used
     */
    private SwingWorker<String,String>[] workerThreads;


    /**
     * The parent app that the Boss works for.
     * This reference is needed so the Boss can change the app's screen when it finishes. Can't be null
     */
    final private StepperApp app;


    /**
     * True if the boss is encrypting, false otherwise
     */
    final private boolean encrypting;


    /**
     * Allowed values: 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only
     */
    final private byte punctMode;


    /**
     * Creates a new StringParserBoss and initializes its fields. Should be created only from a StringParserDispatcher.
     * @param app reference to the parent app. Can't be null
     * @param encrypting true if the Boss will encrypt its input, false if decrypting
     * @param punctMode 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only. Any other value is not allowed
     */
    public StringParserBoss(StepperApp app, boolean encrypting, byte punctMode) {
        assert app!=null;
        assert punctMode>=0 && punctMode<=2;

        this.app=app;
        this.encrypting=encrypting;
        this.punctMode=punctMode;
    }


    /**
     * Returns a String containing representations of the Boss's fields.<br><br>
     *
     * Mimics the 'repr' method in a Python class.<br>
     * Mostly a debugging tool.
     *
     * @return String representation of fields
     */
    @Override
    public String toString() {
        String workers = "";
        if(workerThreads==null) {
            workers += "no";
        }
        else {
            workers += workerThreads.length;
        }

        return "Boss with " + workers + " workers, input=\"" + app.fields().text() + "\", key=\"" + app.fields().key() +
                "\", encrypting=" + encrypting + ", punctuation=" + punctMode;
    }


    /**
     * Returns the result of the Boss's processing.
     * Also sets the App's output when finished and periodically updates the App's progress bar.<br><br>
     *
     * WARNING: Any exceptions thrown in this method are SILENT. They will not stop the program and produce no error messages.
     *
     * @return the Boss's results. This method should NEVER return null.
     */
    @Override
    protected String doInBackground() throws Exception {
        //Switch the screen
        app.setScreen("PROCESSING");


        //Take the input
        StepperAppFields fields = app.fields();
        String text = fields.text();
        String inputKey = fields.key();


        /////////////////////////////////////////////////////////////////////////////////
        //Format the input

        //Do punctuation and diacritic transforms on the text, depending on punct mode selected
        String[] textPieces = StepperFunctions.setWorkerLoads(text, fields.threadCount(), StepperFunctions.BLOCK_LENGTH);
        String[] resultPieces = new String[textPieces.length];
        String output="";

        workerThreads = new StringParserPreparer[textPieces.length];

        for (int i = 0; i < workerThreads.length; i++) {
            workerThreads[i] = new StringParserPreparer(textPieces[i], punctMode);
            workerThreads[i].execute();
        }


        try {
            for (int i = 0; i < workerThreads.length; i++) {
                resultPieces[i] = (String) workerThreads[i].get();
            }
        } catch (InterruptedException | ExecutionException e) {
            return "";
        }

        for (int i = 0; i < workerThreads.length; i++) {
            output += resultPieces[i];
        }


        //Put the formatted result back into the input text
        text = output;


        //Remove unnecessary memory space
        output = null;
        resultPieces = null;
        workerThreads = null;
        if(text == null) {
            System.err.println("WARNING: the text is null");
        }
        System.gc();
        app.setProgress(25);
        System.out.println("done");


        //Record positions of non-alphabetic characters
        char[] nonAlphas = StepperFunctions.findNonAlphaPositions(text);
        //        for(int i=0; i<nonAlphas.length; i++) {
//            System.out.print((int)nonAlphas[i] + " ");
//        }

        //Remove non-alphabetic characters from the text
        text = StepperFunctions.removeNonAlphas(text);



        //Format the key
        byte[][] key = StepperFunctions.createKeyBlocks(inputKey, StepperFunctions.BLOCK_COUNT, StepperFunctions.BLOCK_LENGTH);
        //Put the key into the fields
        app.fields().setKey(StepperFunctions.keyToString(key));

        //Remove unneeded memory
        System.gc();
        app.setProgress(50);
        System.out.println("done");


        /////////////////////////////////////////////////////////////////////////////////
        //Process the input. text should contain only alphanumeric characters at this point


        //Split the text into even blocks
        textPieces = StepperFunctions.setWorkerLoads(text, fields.threadCount(), StepperFunctions.BLOCK_LENGTH);
//        for (String s : textPieces) {
//            System.out.println("\"" + s + "\" " + s.length());
//        }

        //Make the worker threads: one index for each piece of the text
        workerThreads = new StringParserWorker[textPieces.length];
        int startingBlock = 0;
        for (int i = 0; i < workerThreads.length; i++) {
            workerThreads[i] = new StringParserWorker(textPieces[i], key, encrypting, Integer.toString(i), startingBlock);

            startingBlock += (textPieces[i].length() / StepperFunctions.BLOCK_LENGTH);
            System.out.println(textPieces[i].length());
        }

        for(SwingWorker w : workerThreads) {
            System.out.println((StringParserWorker)w);
        }


        //Start each worker thread
        for (int i = 0; i < workerThreads.length; i++) {
            workerThreads[i].execute();
        }

        //Make array to hold the result. Put the results from each thread into the result
        resultPieces = new String[textPieces.length];
        Arrays.fill(resultPieces, "");
        try {
            for (int i = 0; i < workerThreads.length; i++) {
                resultPieces[i] = workerThreads[i].get();
            }
        }
        catch (InterruptedException | ExecutionException e) {
            for (int i = 0; i < workerThreads.length; i++) {
                workerThreads[i].cancel(true);
            }

            System.out.println("Do in Background interrupted");
            return "";
        }

//        for (String s : result) {
//            System.out.println("\"" + s + "\"");
//        }

        //Create the output
        output = "";
        for (int i = 0; i < workerThreads.length; i++) {
            output += resultPieces[i];
        }

        /////////////////////////////////////////////////////////////////////////////////
        //Undo formatting from the output


        //Put non-alphabetic characters into the output
        output = StepperFunctions.recombineNonAlphas(output, nonAlphas, punctMode <= 1);


        //Remove unneeded memory
        workerThreads = null;
        resultPieces = null;
        System.gc();
        app.setProgress(75);
        System.out.println("done");


        //Do the numbers
        if(encrypting) {
            output = StepperFunctions.encryptNumbers(output, key);
        }
        else {
            output = StepperFunctions.decryptNumbers(output, key);
        }


        //Screen changing occurs in the StringParserDispatcher that created this Boss
        return output;
    }

}
