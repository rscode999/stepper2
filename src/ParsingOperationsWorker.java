import javax.swing.*;

/**
 * Does a small portion of a ParsingBoss's work. Cannot have a field that can hold a StepperApp.<br><br>
 *
 * The worker takes the position of the next block and amount of numbers processed so far to determine which piece of the
 * Boss's work to do.<br>
 *
 * All private helper methods must continuously check if the Worker is cancelled. If so, the method should return
 * the empty string, unless the specification states otherwise. Methods that run quickly and in O(1) time, i.e. where its runtime
 * doesn't depend on an arbitrary length, do not need to check if the Worker is cancelled.
 */
public class ParsingOperationsWorker extends SwingWorker<String,String> {

    /**
     * The String to process. Can't be null
     */
    private String input;

    /**
     * The key to process the input with. Can't be null. Dimensions must be `StepperAppFields.BLOCK_COUNT` by `StepperAppFields.BLOCK_LENGTH`.
     */
    private final byte[][] key;


    /**
     * True if this worker is encrypting its text, false otherwise
     */
    final private boolean encrypting;

    /**
     * The name of the Worker (mainly for debugging purposes). Can't be null or equal the string "null"
     */
    final private String name;

    /**
     * The amount of numbers processed so far in the Boss's input string. Can't be negative
     */
    final private int numberStartIndex;

    /**
     * Allowed values: 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only
     */
    final private byte punctMode;

    /**
     * The text block number in the Boss's input string. Can't be negative
     */
    final private int startBlock;



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Creates a ParsingOperationsWorker and loads its fields.
     * @param input the substring it should process. Can't be null
     * @param key the key to process the substring with. Can't be null, no subarrays can be null.
     *            All indices must be on [0,25]. Dimensions must be `StepperAppFields.BLOCK_COUNT` by `StepperAppFields.BLOCK_LENGTH`
     * @param encrypting true if this Worker should encrypt its text, false otherwise
     * @param punctMode 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only. ALl other value are not allowed
     * @param startBlock the block in the original input to start processing at. Must be at least 0
     * @param numbersPreviouslyProcessed amount of numbers encrypted so far. Cannot be negative
     * @param name custom name for this Worker, non-null and cannot equal the string "null"
     */
    public ParsingOperationsWorker(String input, byte[][] key, boolean encrypting,
                                   byte punctMode, int startBlock, int numbersPreviouslyProcessed, String name) {

        //Check some preconditions
        if(input==null) throw new AssertionError("Input cannot be null");
        if(key[0]==null) throw new AssertionError("All indices in the key cannot be null");
        if(key.length != StepperAppFields.BLOCK_COUNT || key[0].length != StepperAppFields.BLOCK_LENGTH)
            throw new AssertionError("Key dimensions must be `StepperAppFields.BLOCK_COUNT` by `StepperAppFields.BLOCK_LENGTH`");

        //Make a deep copy of the key
        this.key = new byte[key.length][key[0].length];
        for(int a=0; a<key.length; a++) {
            if(key[a]==null) throw new AssertionError("All indices in the key cannot be null");

            for(int i=0; i<key[0].length; i++) {
                if(key[a][i]<0 || key[a][i]>25) throw new AssertionError("All indices in the key must be on the interval [0,25]");
                this.key[a][i] = key[a][i];
            }
        }

        this.input=input;
        this.encrypting=encrypting;
        this.punctMode=punctMode;
        this.startBlock=startBlock;
        this.numberStartIndex=numbersPreviouslyProcessed;
        this.name=name;

        assertPreconditions();
    }

    /**
     * FOR METHOD UNIT TESTING ONLY!!! Creates a new OperationsWorker and initializes its fields with garbage values.
     * BREAKS OPERATION PRECONDITIONS!
     */
    public ParsingOperationsWorker() {
        input=null;
        key=null;
        encrypting=false;
        name=null;
        numberStartIndex=-420;
        punctMode=-69;
        startBlock=-69;
    }


    /**
     * Checks operation preconditions.
     * If a precondition is broken, this method throws an AssertionError with a detailed error message.<br><br>
     *
     * Helper to the class constructor not used in method unit testing.
     */
    private void assertPreconditions() {
        if(input==null || key==null) throw new AssertionError("Input text and key cannot be null");
        if(punctMode<0 || punctMode>2) throw new AssertionError("Punctuation mode out of range");
        if(startBlock<0) throw new AssertionError("Start block cannot be negative");
        if(numberStartIndex<0) throw new AssertionError("Number start index cannot be negative");
        if(name==null || name.equals("null")) throw new AssertionError("Name cannot be null or equal the string \"null\"");

        if(key[0]==null) {
            throw new AssertionError("All indices in the key cannot be null");
        }
        if(key.length != StepperAppFields.BLOCK_COUNT || key[0].length != StepperAppFields.BLOCK_LENGTH) {
            throw new AssertionError("Key dimensions must be `StepperAppFields.BLOCK_COUNT` by `StepperAppFields.BLOCK_LENGTH`");
        }
        for(byte[] block : key) {
            if (block == null) {
                throw new AssertionError("All indices in the key cannot be null");
            }
            for(byte index : block) {
                if (index<0 || index>25) {
                    throw new AssertionError("All indices in the key must be on the interval [0,25]");
                }
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a String containing representations of the Worker's fields.
     * @return representation of fields
     */
    @Override
    public String toString() {
        return "Operations Worker \"" + name + "\", start block=" + startBlock + ", number start index="
                + numberStartIndex + ", input=\"" + input + "\"";
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
        //Idiot check
        try {
            assertPreconditions();
        }
        catch (Throwable t) {
            System.err.println("OPERATION PRECONDITIONS BROKEN. WRONG CONSTRUCTOR USED");
            throw t;
        }

        //Remove non-alphabetic characters
        if(encrypting && punctMode==1) {
            input = removeSpaces(input);
        }

        char[] nonAlphas = findNonAlphaPositions(input);
        input = removeNonAlphas(input);


        //Do process
        if (encrypting) {
            input = encrypt(input, key, startBlock);
        }
        else {
            input = decrypt(input, key, startBlock);
        }

        //Reinsert punctuation
        input = recombineNonAlphas(input, nonAlphas, punctMode<=1);


        //Do the numbers
        if(encrypting) {
            input = encryptNumbers(input, key, numberStartIndex);
        }
        else {
            input = decryptNumbers(input, key, numberStartIndex);
        }

        return input;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //FUNCTIONS



    /**
     * Returns the decrypted version of text using the given key.
     * Operations start after `startBlock` blocks have been decrypted.<br><br>
     *
     * Algorithm first implemented on February 26-29, 2024. Enhanced encryption finished on July 18, 2024. By Chris P Bacon
     *
     * @param text text to decrypt. Must contain all lowercase English ASCII characters. Can't be null
     * @param key key to decrypt with. Can't be null. All indices must be on [0,25]
     * @param startBlock index to start decrypting from. Must be non-negative
     * @return decrypted version of text
     */
    private String decrypt(String text, byte[][] key, int startBlock) {
        //Enforce preconditions

        //Check that both inputs are not null
        if(text == null || key == null) {
            throw new AssertionError("Text and key cannot be null");
        }
        for(byte[] k : key) {
            if(k==null) {
                throw new AssertionError("No index in the key can be null");
            }
        }

        //Check text contents: all alphabetic lowercase ASCII characters
        for(int v=0; v<text.length(); v++) {
            if(!(text.charAt(v)>=97 && text.charAt(v)<=122)) {
                throw new AssertionError("Text must contain all lowercase English ASCII characters");
            }
        }

        //Check key contents: all indices on [0,25]
        for(byte[] block : key) {
            for(byte index : block) {
                if (index < 0 || index > 25) {
                    throw new AssertionError("All key indices must be on the interval [0,25]");
                }
            }
        }

        //Check start index is non-negative
        if(startBlock < 0) {
            throw new AssertionError("Starting block number must be non-negative");
        }


        //////////////////////////

        //Configure positions
        byte[] keyBlockBasePositions=initializeKeyBlockPositions(startBlock + text.length()/ StepperAppFields.BLOCK_LENGTH);
//        System.out.println(text);
        StringBuilder output = new StringBuilder(text.length());

        int currentChar=0;
        int currentBlock = (startBlock + text.length()/ StepperAppFields.BLOCK_LENGTH);

        byte[] keyBlockReadPositions=new byte[StepperAppFields.BLOCK_COUNT];
        System.arraycopy(keyBlockBasePositions, 0, keyBlockReadPositions, 0, keyBlockReadPositions.length);

        for(int m = 0; m<(text.length() % StepperAppFields.BLOCK_LENGTH); m++) {
            for(int a=0; a<keyBlockReadPositions.length; a++) {
                keyBlockReadPositions[a]++;
                if(keyBlockReadPositions[a] >= StepperAppFields.BLOCK_LENGTH) {
                    keyBlockReadPositions[a]=0;
                }
            }
        }

        if(isCancelled()) {
            return "";
        }

        for(int t = text.length()-1; t>=text.length()-(text.length() % StepperAppFields.BLOCK_LENGTH); t--) {

            for(int d=0; d<keyBlockReadPositions.length; d++) {
                keyBlockReadPositions[d] -= 1;
                if(keyBlockReadPositions[d] < 0) {
                    keyBlockReadPositions[d] = StepperAppFields.BLOCK_LENGTH-1;
                }
            }

            currentChar=text.charAt(t) - 97;
            for(int k=keyBlockReadPositions.length-1; k>=0; k--) {
                currentChar = (currentChar - key[k][keyBlockReadPositions[k]]) % 26;
                if(currentChar < 0) {
                    currentChar += 26;
                }
            }

            output.append((char)(currentChar+97));

        }


        for(int b = text.length()-(text.length() % StepperAppFields.BLOCK_LENGTH)-1; b>=0; b-= StepperAppFields.BLOCK_LENGTH) {
            if(isCancelled()) {
                return "";
            }

            currentBlock--;
            if((currentBlock+1)% StepperAppFields.BLOCK_LENGTH==0) {
                keyBlockBasePositions = setKeyBlockPositions(currentBlock);
            }


            for(int m=0; m<keyBlockBasePositions.length; m++) {
                keyBlockBasePositions[m] -= StepperAppFields.getKeyBlockIncrementIndex(m);

                if(keyBlockBasePositions[m]<0) {
                    keyBlockBasePositions[m] += StepperAppFields.BLOCK_LENGTH;
                }
            }

            System.arraycopy(keyBlockBasePositions, 0, keyBlockReadPositions, 0, keyBlockReadPositions.length);

            for(int t = b; t>b- StepperAppFields.BLOCK_LENGTH; t--) {

                for(int d=0; d<keyBlockReadPositions.length; d++) {
                    keyBlockReadPositions[d]--;
                    if(keyBlockReadPositions[d] < 0) {
                        keyBlockReadPositions[d]= StepperAppFields.BLOCK_LENGTH-1;
                    }
                }

                currentChar=(int)text.charAt(t) - 97;

                for(int k=keyBlockReadPositions.length-1; k>=0; k--) {

                    currentChar = (currentChar - key[k][keyBlockReadPositions[k]]) % 26;
                    if(currentChar < 0) {
                        currentChar += 26;
                    }

                }

                output.append((char)(currentChar+97));

            }
        }

        output.reverse();

        return output.toString();
    }


    /**
     * Returns a copy of input, but with numbers decrypted using inputKey.<br><br>
     *
     * Any non-number is unchanged in the output.
     * @param input the input text segment
     * @param key key to decrypt with, all indices must be on the interval [0,25]
     * @param numbersPreviouslyDecrypted how many numbers were decrypted prior to this text segment
     * @return input, but with numbers decrypted
     */
    private String decryptNumbers(String input, byte[][] key, int numbersPreviouslyDecrypted) {
        if(input==null || key==null) {
            throw new AssertionError("Input and key cannot be null");
        }
        if(numbersPreviouslyDecrypted < 0) {
            throw new AssertionError("Numbers decrypted cannot be negative");
        }

        StringBuilder flattenedKey = new StringBuilder();
        for(byte[] block : key) {
            if (block == null) {
                throw new AssertionError("Key indices cannot be null");
            }
            for(byte index : block) {
                if (index < 0 || index > 25) {
                    throw new AssertionError("Key indices must be on the interval [0,25]");
                }
                flattenedKey.append((char) index);
            }
        }

        StringBuilder output = new StringBuilder(input.length());

        int currentInputChar;
        int keyIndex = numbersPreviouslyDecrypted % flattenedKey.length();

        for(int i=0; i<input.length(); i++) {
            if(isCancelled()) {
                return "";
            }

            currentInputChar=(int)input.charAt(i);

            //If current char is a number, decrypt it before adding it to the output
            if(currentInputChar>=48 && currentInputChar<=57) {
                currentInputChar -= 48;

                currentInputChar = (currentInputChar - (int)flattenedKey.charAt(keyIndex)) % 10;
                if(currentInputChar < 0) {
                    currentInputChar += 10;
                }
                currentInputChar = (currentInputChar - (int)flattenedKey.charAt(keyIndex)) % 10;
                if(currentInputChar < 0) {
                    currentInputChar += 10;
                }

                currentInputChar += 48;

                keyIndex++;
                if(keyIndex >= flattenedKey.length()) {
                    keyIndex = 0;
                }
            }

            output.append((char)currentInputChar);
        }

        return output.toString();
    }


    /**
     * Returns the encrypted version of text, using inputKey as the key. Encryption starts after `startBlocks` blocks. <br><br>
     *
     * The result should be as if the entire text was encrypted, then only the substring starting after `startBlocks` is
     * in the final result.<br><br>
     *
     * Algorithm first implemented on February 26-29, 2024. Enhanced encryption finished on July 18, 2024. By Chris P Bacon
     *
     * @param text text to encrypt. Must contain all lowercase English ASCII characters. Can't be null
     * @param key key to encrypt with. Can't be null. All indices must be on [0,25]
     * @param startBlocks index to start encrypting from. Must be non-negative
     * @return encrypted version of text
     */
    private String encrypt(String text, byte[][] key, int startBlocks) {
        //Enforce preconditions

        //Check that both inputs are not null
        if(text == null || key == null) {
            throw new AssertionError("Text and key cannot be null");
        }
        for(byte[] k : key) {
            if(k==null) {
                throw new AssertionError("No index in the key can be null");
            }
        }

        //Check text contents: all alphabetic lowercase ASCII characters
        for(int v=0; v<text.length(); v++) {
            if(!(text.charAt(v)>=97 && text.charAt(v)<=122)) {
                throw new AssertionError("Text must contain all lowercase English ASCII characters");
            }
        }

        //Check key contents: all indices on [0,25]
        for(byte[] block : key) {
            for(byte index : block) {
                if (index < 0 || index > 25) {
                    throw new AssertionError("All key indices must be on the interval [0,25]");
                }
            }
        }

        //Check start index is non-negative
        if(startBlocks < 0) {
            throw new AssertionError("Starting block number must be non-negative");
        }


        //////////////////////////
        //Start the process

        byte[] keyBlockBasePositions = initializeKeyBlockPositions(startBlocks);
        byte[] keyBlockReadPositions = new byte[StepperAppFields.BLOCK_COUNT];
        System.arraycopy(keyBlockBasePositions, 0, keyBlockReadPositions, 0, keyBlockReadPositions.length);


        StringBuilder output = new StringBuilder(text.length());
        int currentChar=0;
        int blocksEncrypted = startBlocks;

        for(int b = 0; b <= (text.length() - StepperAppFields.BLOCK_LENGTH); b += StepperAppFields.BLOCK_LENGTH) {
            if(isCancelled()) {
                return "";
            }

            System.arraycopy(keyBlockBasePositions, 0, keyBlockReadPositions, 0, keyBlockReadPositions.length);

            for(int t = b; t<(b + StepperAppFields.BLOCK_LENGTH); t++) {

                currentChar=(int)text.charAt(t) - 97;

                for(int k=0; k<keyBlockReadPositions.length; k++) {
                    currentChar = (currentChar + key[k][keyBlockReadPositions[k]]) % 26;
                }

                output.append((char)(currentChar+97));

                for(int a=0; a<keyBlockReadPositions.length; a++) {
                    keyBlockReadPositions[a]++;
                    if(keyBlockReadPositions[a] >= StepperAppFields.BLOCK_LENGTH) {
                        keyBlockReadPositions[a]=0;
                    }
                }
            }

            for(int r=0; r<keyBlockBasePositions.length; r++) {
                keyBlockBasePositions[r] = (byte) ((keyBlockBasePositions[r] + StepperAppFields.getKeyBlockIncrementIndex(r)) % StepperAppFields.BLOCK_LENGTH);
            }

            if((blocksEncrypted+1) % StepperAppFields.BLOCK_LENGTH == 0) {
                keyBlockBasePositions = setKeyBlockPositions(blocksEncrypted+2);
            }

            blocksEncrypted++;
        }

        System.arraycopy(keyBlockBasePositions, 0, keyBlockReadPositions, 0, keyBlockReadPositions.length);

        if(isCancelled()) {
            return "";
        }

        for(int t = text.length()-(text.length() % StepperAppFields.BLOCK_LENGTH); t<text.length(); t++) {

            currentChar=(int)text.charAt(t) - 97;

            for(int k=0; k<keyBlockReadPositions.length; k++) {
                currentChar = (currentChar + key[k][keyBlockReadPositions[k]]) % 26;
            }

            output.append((char)(currentChar+97));

            for(int a=0; a<keyBlockReadPositions.length; a++) {
                keyBlockReadPositions[a]++;
                if(keyBlockReadPositions[a] >= StepperAppFields.BLOCK_LENGTH) {
                    keyBlockReadPositions[a]=0;
                }
            }
        }

        return output.toString();
    }


    /**
     * Returns a copy of input, but with numbers encrypted using inputKey.<br><br>
     *
     * Any non-number is unchanged in the output.
     * @param input the input text segment
     * @param key key to encrypt with, all indices must be on the interval [0,25]
     * @param numbersPreviouslyEncrypted how many numbers were encrypted prior to this text segment
     * @return input, but with numbers encrypted
     */
    private String encryptNumbers(String input, byte[][] key, int numbersPreviouslyEncrypted) {
        if(input==null || key==null) {
            throw new AssertionError("Neither input can be null");
        }

        StringBuilder flattenedKey = new StringBuilder();
        for(byte[] block : key) {
            if (block == null) {
                throw new AssertionError("Key indices cannot be null");
            }
            for(byte index : block) {
                if (index < 0 || index > 25) {
                    throw new AssertionError("Key indices must be on the interval [0,25]");
                }
                flattenedKey.append((char) index);
            }
        }

        StringBuilder output = new StringBuilder(input.length());

        int currentChar;
        int keyIndex = numbersPreviouslyEncrypted % flattenedKey.length();

        for(int i=0; i<input.length(); i++) {
            currentChar=(int)input.charAt(i);

            //If current char is a number, encrypt it before adding it to the output
            if(currentChar>=48 && currentChar<=57) {
                currentChar -= 48;
                currentChar = (currentChar + (flattenedKey.charAt(keyIndex))) % 10;
                currentChar = (currentChar + (flattenedKey.charAt(keyIndex))) % 10;
                currentChar += 48;

                keyIndex++;
                if(keyIndex >= flattenedKey.length()) {
                    keyIndex=0;
                }
            }

            output.append((char)currentChar);
        }

        return output.toString();
    }


    /**
     * Returns an array containing the positions of all non-alphabetic characters in `text`.
     * If there's an alphabetic character, puts a 0 in the output index.<br><br>
     *
     * Alphabetic characters are ASCII characters that belong to the English alphabet.<br>
     *
     * Example: if the text is "a1b2c3", the output, expressed as ints, should be {0, 48, 0, 49, 0, 50}.
     * Since indices 0, 2, and 4 in the input are alphabetic characters, the corresponding indices in the output is 0.
     * Indices 1, 3, and 5 hold the corresponding ASCII value in the corresponding output indices
     *
     * @param text text to find non-alphabetic characters in
     * @return char array containing locations of non-alphabetic characters. Returns {'C'} if the Worker is cancelled.
     */
    private char[] findNonAlphaPositions(String text) {
        if(text==null) {
            throw new AssertionError("Text cannot be null");
        }

        char[] nonAlphas = new char[text.length()];

        for(int i=0; i<text.length(); i++) {
            if(isCancelled()) {
                return new char[] {'C'};
            }

            if((int)text.charAt(i)<65
                    || ((int)text.charAt(i)>90 && (int)text.charAt(i)<97)
                    || (int)text.charAt(i)>122) {

                nonAlphas[i] = text.charAt(i);
            }
            else {
                nonAlphas[i]=(char)0;
            }
        }

        return nonAlphas;
    }


    /**
     * Returns an array of bytes representing the key block positions at the end of encryption,
     * if the input was `blocks` blocks long<br><br>
     *
     * `blocks` should equal the number of blocks before the starting position.<br>
     * Example: if `blocks` equals 4, the output would be the block positions just after encrypting 4 blocks.<br><br>
     *
     * Helper to the operation functions.
     *
     * @param blocks number of blocks encrypted so far, non-negative
     * @return key block positions after encrypting `blocks` blocks
     */
    private byte[] initializeKeyBlockPositions(long blocks) {
        assert blocks >= 0;

        byte[] output = setKeyBlockPositions(blocks);

        //Simulate moving through the remainder of the blocks
        for(int b = 0; b < blocks%StepperAppFields.BLOCK_LENGTH; b++) {
            //Increment each index of the output
            for(int i=0; i<output.length; i++) {
                output[i] = (byte) ((output[i] + StepperAppFields.getKeyBlockIncrementIndex(i)) % StepperAppFields.BLOCK_LENGTH);
            }
        }

        return output;
    }


    /**
     * Returns text, with all characters from nonAlphas reinserted in their places<br><br>
     *
     * text represents an output without non-alphabetic characters.<br>
     *
     * nonAlphas contains characters at every index where there would normally be
     * a non-alphabetic character in text, and a non-positive number otherwise<br>
     *
     * If reinsertPunctuation is true, the text returned should contain all characters from nonAlphas
     * reinserted in their original places.
     * If not, the text returned should contain only alphanumeric characters.<br>
     *
     * Example: if the text to return is 'abc defg 123' and punct is 'y' or 's':<br>
     * text would contain [0, 1, 2, 3, 4, 5, 6], the numerical values for 'abcdefg'<br>
     * nonAlphas would contain [0,0,0,32,0,0,0,48,49,50]<br>
     * There is a space at the original text's index 3, so nonAlphas's index 3 is 32<br>
     * All other indices are 0 because the original text's indices there are letters<br>
     *
     * If reinsertPunctuation was false, the output would be "abcdefg123".<br><br>
     *
     * Undoes the separation of characters in 'removeNonAlphas'.
     *
     * @param text input text without non-alphabetic characters
     * @param nonAlphasIn array containing locations of non-alphabetic characters
     * @param reinsertingPunctuation whether to include punctuation in the output.
     *                            If false, the function reinserts numbers only
     * @return version of text with non-alphabetic characters in their places
     */
    private String recombineNonAlphas(String text, char[] nonAlphasIn, boolean reinsertingPunctuation) {
        if(text==null) {
            throw new AssertionError("Text cannot be null");
        }
        if(nonAlphasIn==null) {
            throw new AssertionError("Non-alphas cannot be null");
        }

        for(int v=0; v<text.length(); v++) {
            if(text.charAt(v)<97 || text.charAt(v)>122) {
                throw new AssertionError("All indices in the text must be English lowercase letters");
            }
        }

        //make defensive copy of nonAlphasIn
        char[] nonAlphas = new char[nonAlphasIn.length];
        for(int t=0; t<nonAlphas.length; t++) {
            if(isCancelled()) {
                return "";
            }

            nonAlphas[t]=nonAlphasIn[t];
        }

        StringBuilder output = new StringBuilder(text.length());
        int textIndex=0;
        int nonAlphasIndex=0;
        int outputLen=text.length();

        if(text.length() > nonAlphas.length) {
            System.err.println("WARNING: does 'symbols' have blank spaces accounted for?");
        }


        //all characters from [0..nonAlphasIndex) in text should be already processed
        //outputLen should equal the input's length, plus the number of symbols added to the output
        while(nonAlphasIndex < outputLen) {
            if(isCancelled()) {
                return "";
            }

            //If there's a symbol in the current index
            if(nonAlphas[nonAlphasIndex] > 0) {
                //If not an apostrophe (ignore the compiler warning)
                if(!(nonAlphas[nonAlphasIndex]==(char)39 || nonAlphas[nonAlphasIndex]==(char)96 || nonAlphas[nonAlphasIndex]=='’' || nonAlphas[nonAlphasIndex]=='`')) {

                    if( (reinsertingPunctuation) ||
                            (nonAlphas[nonAlphasIndex]>=48 && nonAlphas[nonAlphasIndex]<=57) ) {
                        //add to output
                        output.append((char)nonAlphas[nonAlphasIndex]);
                    }

                    //empty the symbol
                    nonAlphas[nonAlphasIndex]=0;
                }

                outputLen++;
            }

            //If there's no symbol
            else {
                output.append(text.charAt(textIndex));
                textIndex++;
            }

            nonAlphasIndex++;
        }


        //Add the rest of the symbols

        //all characters from [0..nonAlphasIndex) in text should be already processed
        while(nonAlphasIndex < nonAlphas.length) {
            if(isCancelled()) {
                return "";
            }

            if((nonAlphas[nonAlphasIndex]>0 && reinsertingPunctuation)
                    || (nonAlphas[nonAlphasIndex])>=48 && nonAlphas[nonAlphasIndex]<=57) {
                output.append(nonAlphas[nonAlphasIndex]);
            }
            nonAlphasIndex++;
        }


        return output.toString();
    }


    /**
     * Returns a version of text without non-alphabetic characters.
     * The text returned is converted to lowercase.<br><br>
     *
     * WARNING! Not to be confused with removeNonAlnums! This method removes all non-letters, including numbers!
     *
     * @param text original input. Can't be null
     * @return lowercased text without non-alphabetic characters
     */
    private String removeNonAlphas(String text) {
        if(text==null) throw new AssertionError("Text can't be null");

        if(isCancelled()) {
            return "";
        }

        text=text.toLowerCase();

        StringBuilder output = new StringBuilder(text.length());
        for(int i=0; i<text.length(); i++) {
            if(isCancelled()) {
                return "";
            }

            if((int)text.charAt(i)>=97 && (int)text.charAt(i)<=122) {
                output.append(text.charAt(i));
            }
        }

        return output.toString();
    }


    /**
     * Returns a copy of `input`, but with spaces removed.<br><br>
     *
     * Any space between two letters is to be removed. All other spaces are to remain in the output.
     *
     * @param input text to remove spaces from
     * @return copy of input without spaces
     */
    private String removeSpaces(String input) {
        StringBuilder output = new StringBuilder(input.length());
        output.append(input.charAt(0));

        for(int i=1; i<input.length()-1; i++) {

            if(! (input.charAt(i)==' '
                    && Character.isAlphabetic(input.charAt(i-1)) && Character.isAlphabetic(input.charAt(i+1)))) {
                output.append(input.charAt(i));
            }

            if(isCancelled()) {
                return "";
            }
        }

        output.append(input.charAt( input.length()-1 ));

        return output.toString();
    }


    /**
     * Returns the key block positions of unenhanced (v1) operations for the given text length.<br><br>
     *
     * Important note: this method uses text length, not the number of blocks that are in the text.<br><br>
     *
     * Helper to initializeKeyBlockPositions and the operation functions.
     *
     * @param textLength length of text. Must be at least 0
     * @return key block positions as a byte array
     */
    private byte[] setKeyBlockPositions(long textLength) {
        //Check text length: must be non-negative
        if(textLength < 0) throw new AssertionError("Text length cannot be negative");

        //Set the output array, assign all empty space to 0
        byte[] result = new byte[StepperAppFields.BLOCK_COUNT];

        long quotient=textLength;
        double decimalPortion=0;

        //Eliminate overflows
        quotient = quotient % ((long)Math.pow(StepperAppFields.BLOCK_LENGTH, StepperAppFields.BLOCK_COUNT));

        //Divide quotient and take only the portion to the left of the decimal point to eliminate the least significant digit
        quotient = quotient / StepperAppFields.BLOCK_LENGTH;


        //Much like converting a base-10 number to a base-BLOCK_LENGTH number, except without the least significant digit
        //The lowest value digits end up on the right side.
        for(int i=result.length-1; i>=0; i--) {

            //Divide quotient and take only the portion to the right of the decimal point
            decimalPortion = (double)quotient / StepperAppFields.BLOCK_LENGTH - quotient / StepperAppFields.BLOCK_LENGTH;
            //Divide quotient and keep only the portion to the left of the decimal point
            quotient = quotient / (long)StepperAppFields.BLOCK_LENGTH;

            //Convert the decimal portion to a digit and add to the result
            result[i] = (byte)(Math.round(decimalPortion*StepperAppFields.BLOCK_LENGTH));

            if(quotient <= 0) {
                break;
            }
        }

        //Reverse the result to get the output
        byte[] output = new byte[result.length];
        for(int i=0; i<result.length; i++) {
            output[i] = result[result.length - i - 1];
        }

        return output;
    }


    /**
     * Returns the output of `setKeyBlockPositions`.<br><br>
     *
     * setKeyBlockPositions gives the key block positions of unenhanced (v1)
     * operations for the given text length.<br><br>
     *
     * FOR TESTING PURPOSES ONLY!
     *
     * @param textLength length to test
     * @return output of setKeyBlockPositions
     */
    public byte[] setKeyBlockPositions_Testing(long textLength) {
        return setKeyBlockPositions(textLength);
    }

}
