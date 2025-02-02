import javax.swing.*;

/**
 * A SwingWorker that processes a given input string with a ParsingBoss.<br><br>
 *
 * -Most of a Dispatcher's work is to pass its input to a ParsingBoss, execute the Boss, and take the Boss's output.
 * This setup allows the Dispatcher to cancel all the Boss's processing instantly.<br>
 *
 * -ParsingDispatchers and ParsingBosses take references to the main StepperApp because they are responsible for
 * screen changes and app attribute updates.<br>
 *
 * -If a problem occurs during processing that must be displayed on the main App, an error message is placed
 * into the `errorMessage` field and used by the Dispatcher.<br><br>
 */
public class ParsingDispatcher extends SwingWorker<Void,Void> {

    /**
     * The parent app that the Boss works for.
     * This reference is needed to update the app. Can't be null
     */
    final private StepperApp app;

    /**
     * The thread that will do the Dispatcher's work.
     */
    private ParsingBoss bossThread;


    /**
     * True if the boss is encrypting, false otherwise
     */
    final private boolean encrypting;


    /**
     * The absolute path to the input file for the Boss to read from.<br><br>
     *
     * If empty, the filepath to read from is StepperAppFields.DEFAULT_INPUT_FILENAME.<br>
     * If its value is equal to StepperAppFields.TEXT_LOAD_SIGNAL, the Boss will take its input from
     * the App's top text field.<br><br>
     *
     * Cannot be null
     */
    final private String filepath;


    /**
     * Allowed values: 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only
     */
    final private byte punctMode;



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new ParsingBoss and initializes its fields.
     *
     * @param app the app that uses this Boss. Can't be null
     * @param encrypting true if the Boss is encrypting, false if decrypting
     * @param punctMode 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only. No other values allowed
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
        return "Dispatcher, encrypting=" + encrypting + ", punctuation=" + punctMode;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Puts the result of the Dispatcher's processing, or an empty string if cancelled, into the parent App.<br><br>
     *
     * WARNING: Any exceptions thrown in this method are SILENT. They will not stop the program and produce no error messages.
     */
    @Override
    protected Void doInBackground() {

        //Wait for the Boss's output
        try {
            bossThread = new ParsingBoss(app,encrypting,punctMode,filepath);
            bossThread.execute();
            bossThread.get();
        }
        catch (Throwable t) {
            System.err.println("Exception thrown in Dispatcher thread- " + t);

            //Important: must cancel the boss thread
            bossThread.cancel(true);
            return null;
        }

        //The Boss should have updated the App's result fields with the formatted results
        return null;
    }



    /**
     * Changes the screen through the Dispatcher's `app` reference after doInBackground finishes.<br><br>
     *
     * If the Boss reported an error, the Boss' error is fetched with the `errorMessage()` method,
     * then displayed on the main app in a message dialog.
     */
    @Override
    protected void done() {
        //Reset processing text (no longer needed)
        app.setProcessingProgressText("");

        //If something went wrong, display an error dialog on the main App reference
        if(!bossThread.errorMessage().isEmpty()) {
            app.setScreen("INPUT");
            JOptionPane.showMessageDialog(this.app, bossThread.errorMessage(),
                    StepperAppFields.MESSAGE_DIALOG_TITLE, JOptionPane.ERROR_MESSAGE);

            return; //Very important to return now so normal operations are stopped
        }

        //If cancelled, go back to the input screen and don't go to the results
        if(isCancelled()) {
            app.setScreen("INPUT");
            System.out.println("Main execution thread cancelled");
        }
        //if not, go to the result screen
        else {
            app.setScreen("RESULTS");
            System.out.println("Main execution thread finished");
        }

        //Just do this for the lol
        bossThread = null;
        return;
    }
}
