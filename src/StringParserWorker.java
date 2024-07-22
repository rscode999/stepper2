import javax.swing.*;
import java.util.concurrent.ExecutionException;

/**
 * Does a small portion of a StringParserBoss's work. Cannot have a field that can hold a StepperApp.
 */
public class StringParserWorker extends SwingWorker<String,String> {

    /**
     * The String to process. Can't be null
     */
    final private String input;

    /**
     * The key to process the input with. Can't be null
     */
    final private byte[][] key;

    /**
     * True if this worker is encrypting its text, false otherwise
     */
    final private boolean encrypting;


    /**
     * The name of the Worker (mainly for debugging purposes). Can't be null
     */
    final private String name;


    /**
     * The index of the first character in `input` in the Boss's input string. Can't be negative
     */
    final private int startIndex;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a StringParserWorker and loads its fields
     * @param input the substring it should process. Can't be null
     * @param key the key to process the substring with. Can't be null
     * @param encrypting true if this Worker should encrypt its text, false otherwise
     * @param name the name of the Worker
     * @param startIndex where to start processing the input at. Must be at least 0
     */
    public StringParserWorker(String input, byte[][] key, boolean encrypting,
                              String name, int startIndex) {
        assert input!=null;
        assert key!=null;
        assert name!=null;
        assert startIndex>=0;

        this.input=input;
        this.key=key;
        this.encrypting=encrypting;
        this.name=name;
        this.startIndex=startIndex;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a String containing representations of the Worker's fields.<br><br>
     *
     * Mimics the 'repr' method in a Python class.<br>
     * Mostly a debugging tool.
     *
     * @return representation of fields
     */
    @Override
    public String toString() {
        return "Worker \"" + name + "\", input=\"" + input + " " + (input.length()) + "\", start=" + startIndex;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the result of the Worker's processing.<br><br>
     *
     * WARNING: Any exceptions thrown in this method are SILENT. They will not stop the program and produce no error messages.
     *
     * @return the Worker's results. This method should NEVER return null.
     */
    @Override
    protected String doInBackground() {

        //Do process
        String output="";
        if (encrypting) {
            output = StepperFunctions.encrypt(input, key.clone(), startIndex);
        }
        else {
            output = StepperFunctions.decrypt(input, key.clone(), startIndex);
        }

//        System.out.println(output);
        return output;
    }

}
