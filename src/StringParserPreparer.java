import javax.swing.*;


/**
 * Created by a StringParserBoss to format its input string.
 */
public class StringParserPreparer extends SwingWorker<String,String> {

    /**
     * Input text that the Preparer will format. Can't be null
     */
    final private String input;

    /**
     * 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only
     */
    final private byte punctMode;



    /**
     * Creates a ParserPreparer and initializes its fields
     * @param input input string. Can't be null
     * @param punctMode 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only. Any other value is not allowed
     */
    public StringParserPreparer(String input, byte punctMode) {
        assert input != null;
        assert punctMode>=0 && punctMode<=2;

        this.input=input;
        this.punctMode=punctMode;
    }


    /**
     * Returns a String containing representations of the Preparer's fields.<br><br>
     *
     * Mimics the 'repr' method in a Python class.<br>
     * Mostly a debugging tool.
     *
     * @return representation of fields
     */
    @Override
    public String toString() {
        return "Preparer, input=\"" + input + "\", punctuation=" + punctMode;
    }


    /**
     * Returns the result of the Preparer's processing.<br><br>
     *
     * WARNING: Any exceptions thrown in this method are SILENT. They will not stop the program and produce no error messages.
     *
     * @return the Preparer's results. This method should NEVER return null.
     */
    @Override
    protected String doInBackground() throws Exception {
        String output = StepperFunctions.removeDiacritics(input);

        //Include all punctuation
        if (punctMode == 0) {
            //Do nothing
        }
        //Remove spaces
        else if (punctMode == 1) {
            output = StepperFunctions.removeSpaces(input);
        }
        //Remove non-alphanumeric characters
        else {
            output = StepperFunctions.removeNonAlnums(input);
        }

//        System.out.println("\"" + output + "\"");
        return output;
    }
}
