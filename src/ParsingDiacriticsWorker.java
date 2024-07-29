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
        return removeDiacritics(text);
    }


    /**
     * Returns a lowercased version of `input` without diacritics or accent marks.<br><br>
     *
     * Any character that is not transformed by the private helper method `removeDiacritics(char)` is not changed.
     *
     * @param input text to remove diacritics from
     * @return copy of input without diacritics in lowercase letters
     */
    private String removeDiacritics(String input) {
        String output="";
        for(int i=0; i<input.length(); i++) {
            if(this.isCancelled()) {
                return "";
            }

            output += removeDiacritics(input.charAt(i));
        }
        return output;
    }


    /**
     * Returns a lowercase version of the input without accent marks or letter variants.<br><br>
     * Helper to createBlocks
     * @param input letter to remove diacritics from
     * @return copy of input without diacritics
     */
    public static char removeDiacritics(char input) {

        String a="" + input;
        a=a.toLowerCase();
        input=a.charAt(0);
        a=null;

        //Not like the 'final' declaration will save the array indices from tampering,
        //but I hope that it increases speed a little.
        final String[] outChars={"àáâãäå", "ç", "ð", "èéëêœæ", "ìíîï", "òóôõöø", "ǹńñň",
                "ß", "ùúûü", "ýÿ", "⁰₀", "¹₁", "²₂", "³₃", "⁴₄", "⁵₅", "⁶₆", "⁷₇", "⁸₈", "⁹₉", "—"};
        final char[] inChars={'a', 'c', 'd', 'e', 'i', 'o', 'n', 's', 'u', 'y', '0', '1',
                '2', '3', '4', '5', '6', '7', '8', '9', '-'};
        char charReplacement='#';

        //to protect me from my own stoopidity
        if(outChars.length != inChars.length) throw new AssertionError("warning: check lengths");


        //loop through outChar strings
        for(int os=0; os<outChars.length; os++) {
            //loop through individual letters in the outChars string.
            //if match, set replacement char to ASCII replacement

            for(int oc=0; oc<outChars[os].length(); oc++) {
                if(outChars[os].charAt(oc)==input) {
                    charReplacement=inChars[os];
                }
            }
        }

        if(charReplacement=='#') {
            return input;
        }

        return charReplacement;

    }

}
