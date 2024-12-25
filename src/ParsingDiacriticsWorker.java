import javax.swing.*;

/**
 * Worker thread that lowercases and removes diacritics from an input
 */
public class ParsingDiacriticsWorker extends SwingWorker<String,String> {

    /**
     * The text to remove diacritics from
     */
    final private String text;

    /**
     * The name of the Worker, mostly for debugging purposes. Cannot be null or the string "null".
     */
    final private String name;


    /**
     * Creates a ParsingDiacriticsWorker called `name` and loads it with `text`
     *
     * @param text String to remove diacritics from. Non-null
     * @param name custom name for this Worker. Non-null and cannot equal the string "null"
     */
    public ParsingDiacriticsWorker(String text, String name) {
        if(text==null) {
            throw new AssertionError("Text cannot be null");
        }
        if(name==null || name.equals("null")) {
            throw new AssertionError("Name cannot be null or equal the string \"null\"");
        }

        this.text = text;
        this.name = name;
    }


    /**
     * FOR UNIT TESTING ONLY! Creates a new ParsingDiacriticsWorker with garbage fields.
     * BREAKS OPERATION PRECONDITIONS!
     */
    public ParsingDiacriticsWorker() {
        this.text = null;
        this.name = null;
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
        //Idiot check
        if(text==null || name==null || name.equals("null")) {
            System.err.println("OPERATION PRECONDITIONS ARE NOT MET. WRONG CONSTRUCTOR USED");
            throw new AssertionError("Preconditions broken");
        }

        return removeDiacritics(text);
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    /**
     * Returns a lowercase version of the input character without accent marks or letter variants.
     * @param input letter to remove diacritics from
     * @return copy of input without diacritics
     */
    private char removeDiacritics(char input) {

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


        //loop through outChar strings
        for(int os=0; os<outChars.length; os++) {
            //loop through individual letters in the outChars string.
            //if match, set replacement char to ASCII replacement

            for(int oc=0; oc<outChars[os].length(); oc++) {
                if(outChars[os].charAt(oc) == input) {
                    charReplacement = inChars[os];
                    break;
                }
            }
        }

        if(charReplacement=='#') {
            return input;
        }

        return charReplacement;

    }


    /**
     * Returns a lowercased version of `input` without diacritics or accent marks.<br><br>
     *
     * Any character that is not transformed by the private helper method `removeDiacritics(char)` is not changed.
     *
     * @param input text to remove diacritics from. Cannot be null
     * @return lowercase version of input without diacritics
     */
    private String removeDiacritics(String input) {
        //Precondition is enforced in the method constructor

        StringBuilder output = new StringBuilder(input.length());
        for(int i=0; i<input.length(); i++) {
            if(this.isCancelled()) {
                return "";
            }

            output.append(removeDiacritics(input.charAt(i)));
        }
        return output.toString();
    }


    /**
     * Returns the value returned by `removeDiacritics` for the given input<br><br>
     *
     * FOR UNIT TESTS ONLY!
     *
     * @param input input to test
     * @return value from `removeDiacritics`
     */
    public String removeDiacritics_Testing(String input) {
        return removeDiacritics(input);
    }
}
