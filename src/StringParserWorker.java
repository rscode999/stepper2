import javax.swing.*;
import java.util.Arrays;

/**
 * Does a small portion of a StringParserBoss's work. Cannot have a field that can hold a StepperApp.<br><br>
 *
 * The worker takes the position of the next block and amount of numbers processed so far to determine which piece of the
 * Boss's work to do.
 */
public class StringParserWorker extends SwingWorker<String,String> {

    /**
     * The String to process. Can't be null
     */
    private String input;

    /**
     * The key to process the input with. Can't be null
     */
    private final byte[][] key;

    /**
     * True if this worker is encrypting its text, false otherwise
     */
    final private boolean encrypting;

    /**
     * Allowed values: 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only
     */
    final private byte punctMode;

    /**
     * The block number in the Boss's input string. Can't be negative
     */
    final private int startBlock;

    /**
     * The amount of numbers processed so far in the Boss's input string. Can't be negative
     */
    final private int numbersPreviouslyEncrypted;

    /**
     * The name of the Worker (mainly for debugging purposes). Can't be null
     */
    final private String name;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a StringParserWorker and loads its fields
     * @param givenInput the substring it should process. Can't be null
     * @param givenKey the key to process the substring with. Can't be null. No indices can be null
     * @param encrypting true if this Worker should encrypt its text, false otherwise
     * @param punctMode 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only
     * @param startBlock where to start processing the input at. Must be at least 0
     * @param numbersPreviouslyEncrypted amount of numbers encrypted so far. Cannot be negative
     * @param name the name of the Worker

     */
    public StringParserWorker(String givenInput, byte[][] givenKey, boolean encrypting,
                              byte punctMode, int startBlock, int numbersPreviouslyEncrypted, String name) {
        assert givenInput!=null;
        assert givenKey!=null;
        assert punctMode>=0 && punctMode<=2;
        assert startBlock>=0;
        assert numbersPreviouslyEncrypted>=0;

        //Make deep copy of the input
        this.input=givenInput;

        //Make a deep copy of the key
        this.key = new byte[givenKey.length][givenKey[0].length];
        for(int a=0; a<givenKey.length; a++) {
            assert givenKey[a] != null;
            for(int i=0; i<givenKey[0].length; i++) {
                this.key[a][i] = givenKey[a][i];
            }
        }

        this.encrypting=encrypting;
        this.punctMode=punctMode;
        this.name=name;
        this.numbersPreviouslyEncrypted=numbersPreviouslyEncrypted;
        this.startBlock=startBlock;
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
        return "Worker \"" + name + "\", input(" + input.length() + ")=\"" + input + "\", startblock=" + startBlock + ", numberstart="
                + numbersPreviouslyEncrypted;
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

        //Remove non-alphabetic characters
        if(encrypting && punctMode==1) {
            input = StepperFunctions.removeSpaces(input);
        }

        char[] nonAlphas = StepperFunctions.findNonAlphaPositions(input);
        input = StepperFunctions.removeNonAlphas(input);

        //Do process
        String output="";
        if (encrypting) {
            output = StepperFunctions.encrypt(input, key, startBlock);
        }
        else {
            output = StepperFunctions.decrypt(input, key, startBlock);
        }

        //Reinsert punctuation
        output = StepperFunctions.recombineNonAlphas(output, nonAlphas, punctMode<=1);

        //Do the numbers
        if(encrypting) {
            output = StepperFunctions.encryptNumbers(output, key, numbersPreviouslyEncrypted);
        }
        else {
            output = StepperFunctions.decryptNumbers(output, key, numbersPreviouslyEncrypted);
        }

        return output;
    }

}
