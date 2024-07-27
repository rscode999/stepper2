import javax.swing.*;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


/**
 * Created by a StringParserDispatcher to process the Dispatcher's input.<br><br>
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

        //Take the input
        StepperAppFields fields = app.fields();
        String text = fields.text();
        String inputKey = fields.key();


        //Remove diacritics from the text. Multithreaded because this is the most computationally intensive task

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
        for(int i=0; i<workerThreads.length; i++) {
            textPieces[i] = workerThreads[i].get();
        }

        //Reload the text with the results
        text = "";
        for(int i=0; i<workerThreads.length; i++) {
            text += textPieces[i];
        }


        /////////////////////////////////////////////////////
        //OPERATION

        app.setLoadingStatusText("Executing...");

        //Format the key
        byte[][] key = StepperFunctions.createKeyBlocks(inputKey, StepperFunctions.BLOCK_COUNT, StepperFunctions.BLOCK_LENGTH);
        //Load formatted key into app
        app.fields().setKey(StepperFunctions.arrToString(key));


        //Create loads
        textPieces = StepperFunctions.setWorkerLoads(text,
                app.fields().threadCount(),
                StepperFunctions.BLOCK_LENGTH);

        //Make the worker threads: one index for each piece of the text
        workerThreads = new StringOperationsWorker[textPieces.length];
        int startingBlock = 0;
        int numberCount = 0;
        for (int i = 0; i < workerThreads.length; i++) {
            workerThreads[i] = new StringOperationsWorker(textPieces[i], key,
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
        try {
            for (int i = 0; i < workerThreads.length; i++) {
                textPieces[i] = workerThreads[i].get();
            }
        }
        catch (InterruptedException | ExecutionException e) {
            for (int i = 0; i < workerThreads.length; i++) {
                workerThreads[i].cancel(true);
            }

            System.out.println("Do in Background interrupted- " + e.getCause());
            return "";
        }
        for (String s : textPieces) {
            System.out.println("\"" + s + "\"");
        }

        workerThreads = null;
        System.gc();

        //Create the output
        text = "";
        for (int i = 0; i < textPieces.length; i++) {
            text += textPieces[i];
        }


        textPieces = null;

        //Screen changing occurs in the StringParserDispatcher that created this Boss
        return text;
    }



}
