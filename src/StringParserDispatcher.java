import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * A SwingWorker that processes a given input string with a StringParserBoss.<br><br>
 *
 * StringParserDispatchers and StringParserBosses take references to the main StepperModel because they are responsible for screen changes.<br>
 * Each StepperApp should contain exactly one StringParserDispatcher.<br>
 *
 * Most of the work is done by a StringParserBoss created in the doInBackground method. This setup allows the Dispatcher to
 * cancel all the Boss's processing instantly.<br><br>
 *
 * A StringParserDispatcher should be reconstructed before every use. Its inaccessible private fields are changed when
 * it processes its input.
 */
public class StringParserDispatcher extends SwingWorker<String,String> {

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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new StringParserBoss and initializes its fields.
     *
     * @param app the app that uses this Boss. Can't be null
     * @param encrypting true if the Boss is encrypting, false if decrypting
     * @param punctMode 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only
     */
    public StringParserDispatcher(StepperApp app, boolean encrypting, byte punctMode) {
        assert app!=null;
        assert punctMode>=0 && punctMode<=2;

        this.app=app;
        this.encrypting=encrypting;
        this.punctMode=punctMode;
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
            StringParserBoss thread = new StringParserBoss(app,encrypting,punctMode);
            thread.execute();
            output = thread.get();
        }
        catch (ExecutionException | InterruptedException e) {
            return "";
        }

        app.setOutput(output, app.fields().key());
        return output;
    }



    /**
     * Used to change the screen through the Dispatcher's `app` reference after doInBackground finishes.<br><br>
     *
     * May also print messages to System.out for debugging purposes.
     */
    @Override
    protected void done() {
        if(isCancelled()) {
            System.out.println("Main execution thread cancelled");
            app.setScreen("INPUT");
        }
        else {
            System.out.println("Main execution thread finished");
            app.setScreen("RESULTS");
        }

        //The Boss loads the App's output text areas with the result
    }
}
