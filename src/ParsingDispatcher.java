import javax.swing.*;

/**
 * A SwingWorker that processes a given input string with a ParsingBoss.<br><br>
 *
 * Most of a Dispatcher's work is to pass its input to a ParsingBoss, execute the Boss, and take the Boss's output.
 *
 * StringParserDispatchers and StringParserBosses take references to the main StepperModel because they are responsible for screen changes.<br>
 * Each StepperApp should contain exactly one ParsingDispatcher.<br>
 *
 * Most of the work is done by a ParsingBoss created in the doInBackground method. This setup allows the Dispatcher to
 * cancel all the Boss's processing instantly.<br>
 *
 * Dispatchers and Bosses share exception messages through the App's key field, accessed with {app}.fields().key() and set
 * with {app}.fields().setKey(). Exception messages always start with "~~~".
 */
public class ParsingDispatcher extends SwingWorker<String,String> {

    /**
     * The parent app that the Boss works for.
     * This reference is needed so the Boss can change the app's screen when it finishes. Can't be null
     */
    final private StepperApp app;

    /**
     * The thread that will do the Dispatcher's work
     */
    private ParsingBoss bossThread;


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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new ParsingBoss and initializes its fields.
     *
     * @param app the app that uses this Boss. Can't be null
     * @param encrypting true if the Boss is encrypting, false if decrypting
     * @param punctMode 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only
     * @param filepath absolute path to the input file. Empty (i.e. length=0) if taking input from a text field. Can't be null
     */
    public ParsingDispatcher(StepperApp app, boolean encrypting, byte punctMode, String filepath) {
        if(app==null) throw new AssertionError("Parent app reference cannot be null");
        if(!(punctMode>=0 && punctMode<=2)) throw new AssertionError("Punctuation mode out of valid range");
        if(filepath==null) throw new AssertionError("Input filepath cannot be null");

        this.app=app;
        this.encrypting=encrypting;
        this.punctMode=punctMode;
        this.filepath=filepath;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a String containing representations of the Worker's fields.<br><br>
     *
     * Mimics the 'repr' method in a Python class.<br>
     * Mostly a debugging tool.
     *
     * @return String representation of fields
     */
    @Override
    public String toString() {
        return "Dispatcher, input=\"" + app.fields().text() + "\", key=" + app.fields().key() + ", encrypting=" + encrypting +
                ", punctuation=" + punctMode;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the result of the Dispatcher's processing, or an empty string if cancelled.<br><br>
     *
     * If execution is unexpectedly stopped, the method returns the empty string (not null).<br><br>
     *
     * WARNING: Any exceptions thrown in this method are SILENT. They will not stop the program and produce no error messages.
     *
     * @return the result of the processing
     */
    @Override
    protected String doInBackground() {
        String output = "";
        try {
            bossThread = new ParsingBoss(app,encrypting,punctMode,filepath);
            bossThread.execute();
            output = bossThread.get();
        }
        catch (Throwable t) {
            System.out.println("Exception thrown in Dispatcher thread: " + t);

            //Important: must cancel the boss thread
            bossThread.cancel(true);
            return "";
        }

        app.setOutput(output, app.fields().key());
        return output;
    }



    /**
     * Used to change the screen through the Dispatcher's `app` reference after doInBackground finishes.<br><br>
     *
     * Before screen changes, the method checks for error messages in the parent App's key field. If so,
     * the Dispatcher resets the screen.
     */
    @Override
    protected void done() {
        if(isCancelled()) {
            System.out.println("Main execution thread cancelled");
            app.setScreen("INPUT");
        }
        else {
            //Check for exception messages in the load. If so, present an error dialog
            if(app.fields().key().startsWith( StepperFunctions.INPUT_ERROR_SIGNAL)) {
                System.out.println("Error detected");
                app.setScreen("INPUT");

                JOptionPane.showMessageDialog(app, app.fields().key().substring( StepperFunctions.INPUT_ERROR_SIGNAL.length() ),
                        "Load error", JOptionPane.ERROR_MESSAGE);
            }
            //Otherwise, show the results
            else {
                System.out.println("Main execution thread finished");
                app.setScreen("RESULTS");
            }
        }
    }
}
