import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


/**
 * Created by a StringParserDispatcher to process the Dispatcher's input.<br><br>
 *
 * Dispatchers and Workers share error messages through the App's key field, accessed with {app}.fields().key() and set
 * with {app}.fields().setKey(). Error messages always start with "~~~".
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
     * The absolute path to the input file. If `filepath` is the empty string, the Boss will take its input from
     * its parent App's top text input.
     */
    final private String filepath;

    /**
     * Creates a new StringParserBoss and initializes its fields. Should be created only from a StringParserDispatcher.
     * @param app reference to the parent app. Can't be null
     * @param encrypting true if the Boss will encrypt its input, false if decrypting
     * @param punctMode 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only. Any other value is not allowed
     * @param filepath absolute path to the input file. Empty (i.e. length=0) if taking input from a text field. Can't be null
     */
    public StringParserBoss(StepperApp app, boolean encrypting, byte punctMode, String filepath) {
        assert app!=null;
        assert punctMode>=0 && punctMode<=2;
        assert filepath != null;

        this.app=app;
        this.encrypting=encrypting;
        this.punctMode=punctMode;
        this.filepath=filepath;
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
     * By the end of this method, the App's fields should be updated with the results.<br>
     * If this method throws an exception, the method will load the App's key field with an error message, which will
     * be interpreted by the Dispatcher that uses this Boss. Error messages always start with "~~~".<br>
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
        try {
            if (filepath.equals(StepperFunctions.TEXT_LOAD_SIGNAL)) {
                app.fields().setText(app.topInputValue());
            } else {
                app.fields().setText(StepperFunctions.getTextFromFile(filepath));
            }
        }
        catch (FileNotFoundException e) {
            app.fields().setKey("~~~" + e.getMessage());
            return "";
        }


        //Set local copies of the inputs
        String text = app.fields().text();
        String inputKey = app.fields().key();


        /////////////////////////////////////////////////////
        //REMOVE DIACRITICS USING ALL THREADS

        //Create text pieces
        String[] textPieces = StepperFunctions.setWorkerLoads(text,
                app.fields().threadCount(),
                StepperFunctions.BLOCK_LENGTH);

        //Create worker threads and assign them a workload
        workerThreads = new StringDiacriticsWorker[textPieces.length];
        for(int i=0; i<workerThreads.length; i++) {
            workerThreads[i] = new StringDiacriticsWorker(textPieces[i], Integer.toString(i));
        }

        //Execute workers
        for(int i=0; i<workerThreads.length; i++) {
            workerThreads[i].execute();
        }

        //Take output from each worker thread
        textPieces = new String[workerThreads.length];
        for (int i = 0; i < workerThreads.length; i++) {
            textPieces[i] = workerThreads[i].get();
        }

        //Reload the text with the results
        text = "";
        for(int i=0; i<workerThreads.length; i++) {
            text += textPieces[i];
        }


        /////////////////////////////////////////////////////
        //OPERATION
        //Doesn't use a function to save memory, processing power, and the organizational scheme

        app.setLoadingStatusText("Executing...");

        //Format the key
        byte[][] operationsKey = StepperFunctions.createKeyBlocks(inputKey, StepperFunctions.BLOCK_COUNT, StepperFunctions.BLOCK_LENGTH);
        //Load formatted key into app
        app.fields().setKey(StepperFunctions.arrToString(operationsKey));


        //Create loads
        textPieces = StepperFunctions.setWorkerLoads(text,
                app.fields().threadCount(),
                StepperFunctions.BLOCK_LENGTH);

        //Make the worker threads: one index for each piece of the text
        workerThreads = new StringOperationsWorker[textPieces.length];
        int startingBlock = 0;
        int numberCount = 0;
        for (int i = 0; i < workerThreads.length; i++) {
            workerThreads[i] = new StringOperationsWorker(textPieces[i], operationsKey,
                    encrypting, punctMode, startingBlock, numberCount, Integer.toString(i));

            int[] charCounts = StepperFunctions.countAlphaAndNumericChars(textPieces[i]);
            startingBlock += charCounts[0] / StepperFunctions.BLOCK_LENGTH;
            numberCount += charCounts[1];
        }
        textPieces = null;

//        for(SwingWorker w : workerThreads) {
//            System.out.println(w);
//        }

        System.gc();

        //Start each worker thread
        for (int i = 0; i < workerThreads.length; i++) {
            workerThreads[i].execute();
        }

        //Make array to hold the result. Put the results from each thread into the result
        textPieces = new String[workerThreads.length];
        Arrays.fill(textPieces, "");
        for (int i = 0; i < workerThreads.length; i++) {
            textPieces[i] = workerThreads[i].get();
        }



        workerThreads = null;
        System.gc();

        //Create the output
        text = "";
        for (int i = 0; i < textPieces.length; i++) {
            text += textPieces[i];
        }


        textPieces = null;
        app.fields().setText(text);

        //Screen changing occurs in the StringParserDispatcher that created this Boss
        return text;
    }



}
