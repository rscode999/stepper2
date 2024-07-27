import javax.swing.*;

/**
 * Worker thread that lowercases and removes diacritics from an input
 */
public class StringDiacriticsWorker extends SwingWorker<String,String> {

    /**
     * The text to remove diacritics from
     */
    String text;

    /**
     * The name of the Worker, mostly for debugging purposes.
     */
    String name;


    /**
     * Creates a StringDiacriticsWorker and loads it with `text`
     * @param text String to remove diacritics from, non-null
     * @param name custom name for this Worker
     */
    public StringDiacriticsWorker(String text, String name) {
        assert text != null;

        this.text = text;
        this.name = name;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a String representation of the Worker.
     * @return String representation of the Worker's fields
     */
    @Override
    public String toString() {
        return "Diacritics Worker \"" + name + "\", text=\"" + text + "\"";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected String doInBackground() throws Exception {
        return StepperFunctions.removeDiacritics(text);
    }
}
