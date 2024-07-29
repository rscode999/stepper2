import javax.swing.*;

/**
 * Does a small portion of a ParsingBoss's work. Cannot have a field that can hold a StepperApp.<br><br>
 *
 * The worker takes the position of the next block and amount of numbers processed so far to determine which piece of the
 * Boss's work to do.
 */
public class ParsingOperationsWorker extends SwingWorker<String,String> {

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
     * Creates a ParsingOperationsWorker and loads its fields
     * @param input the substring it should process. Can't be null
     * @param key the key to process the substring with. Can't be null. No subarrays can be null. All indices must be on [0,25]
     * @param encrypting true if this Worker should encrypt its text, false otherwise
     * @param punctMode 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only
     * @param startBlock where to start processing the input at. Must be at least 0
     * @param numbersPreviouslyEncrypted amount of numbers encrypted so far. Cannot be negative
     * @param name the name of the Worker
     */
    public ParsingOperationsWorker(String input, byte[][] key, boolean encrypting,
                                   byte punctMode, int startBlock, int numbersPreviouslyEncrypted, String name) {
        assert input!=null;
        assert key!=null;
        assert punctMode>=0 && punctMode<=2;
        assert startBlock>=0;
        assert numbersPreviouslyEncrypted>=0;

        this.input=input;

        //Make a deep copy of the key
        this.key = new byte[key.length][key[0].length];
        for(int a=0; a<key.length; a++) {
            assert key[a] != null;
            for(int i=0; i<key[0].length; i++) {
                assert key[a][i]>=0 && key[a][i]<=25;
                this.key[a][i] = key[a][i];
            }
        }

        this.encrypting=encrypting;
        this.punctMode=punctMode;
        this.startBlock=startBlock;
        this.numbersPreviouslyEncrypted=numbersPreviouslyEncrypted;
        this.name=name;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a String containing representations of the Worker's fields.
     * @return representation of fields
     */
    @Override
    public String toString() {
        return "Operations Worker \"" + name + "\", startblock=" + startBlock + ", numberstart="
                + numbersPreviouslyEncrypted + ", input=\"" + input + "\"";
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
