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
    private StringParserWorker[] workerThreads;


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

        //Format text
        text = StepperFunctions.removeDiacritics(text);

        //Format the key
        byte[][] key = StepperFunctions.createKeyBlocks(inputKey, StepperFunctions.BLOCK_COUNT, StepperFunctions.BLOCK_LENGTH);
        //Load formatted key into app
        app.fields().setKey(StepperFunctions.arrToString(key));



        //Create loads
        String[] textPieces = StepperFunctions.setWorkerLoads(text,
                app.fields().threadCount(),
                StepperFunctions.BLOCK_LENGTH);

        //Make the worker threads: one index for each piece of the text
        workerThreads = new StringParserWorker[textPieces.length];
        int startingBlock = 0;
        int numberCount = 0;
        for (int i = 0; i < workerThreads.length; i++) {
            workerThreads[i] = new StringParserWorker(textPieces[i], key, encrypting, punctMode, startingBlock, numberCount, Integer.toString(i));

            int[] charCounts = StepperFunctions.countAlphaAndNumericChars(textPieces[i]);
            startingBlock += charCounts[0] / StepperFunctions.BLOCK_LENGTH;
            numberCount += charCounts[1];
        }

        for(StringParserWorker w : workerThreads) {
            System.out.println(w);
        }

        System.gc();
        app.setLoadingStatusText("Executing...");

        //Start each worker thread
        for (int i = 0; i < workerThreads.length; i++) {
            workerThreads[i].execute();
        }

        //Make array to hold the result. Put the results from each thread into the result
        String[] resultPieces = new String[textPieces.length];
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

            System.out.println("Do in Background interrupted- " + e.getCause());
            return "";
        }
//        for (String s : result) {
//            System.out.println("\"" + s + "\"");
//        }

        System.gc();
        app.setLoadingStatusText("Finalizing...");

        //Create the output
        String output = "";
        for (int i = 0; i < workerThreads.length; i++) {
            output += resultPieces[i];
        }


        //Remove unneeded memory
        workerThreads = null;
        resultPieces = null;

        //Screen changing occurs in the StringParserDispatcher that created this Boss
        return output;
    }



}
