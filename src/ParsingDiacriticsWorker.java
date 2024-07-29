import javax.swing.*;

/**
 * Worker thread that lowercases and removes diacritics from an input
 */
public class ParsingDiacriticsWorker extends SwingWorker<String,String> {

    /**
     * The text to remove diacritics from
     */
    String text;

    /**
     * The name of the Worker, mostly for debugging purposes.
     */
    String name;


    /**
     * Creates a ParsingDiacriticsWorker and loads it with `text`
     * @param text String to remove diacritics from, non-null
     * @param name custom name for this Worker, non-null
     */
    public ParsingDiacriticsWorker(String text, String name) {
        assert text != null;
        assert name != null;

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


    /**
     * Removes diacritics from the input text and returns it<br><br>
     *
     * WARNING: Any exceptions thrown in this method are SILENT. They will not stop the program and produce no error messages.
     *
     * @return version of `text` without diacritics
     */
    @Override
    protected String doInBackground() {
        return StepperFunctions.removeDiacritics(text);
    }
}
