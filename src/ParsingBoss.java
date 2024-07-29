import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;


/**
 * Created by a ParsingDispatcher to process the Dispatcher's input.<br><br>
 *
 * Dispatchers and Workers share exception messages through the App's key field, accessed with {app}.fields().key() and set
 * with {app}.fields().setKey(). Exception messages always start with "~~~".
 */
public class ParsingBoss extends SwingWorker<String,String> {

    /**
     * The worker threads that this Boss employs. The array's length may vary depending on the number of threads used
     */
    private SwingWorker<String,String>[] workerThreads;


    /**
     * The parent app that the Boss works for.
     * This reference is needed so the Boss can change the app's screen and its variable fields. Can't be null
     */
    final private StepperApp app;


    /**
     * True if the boss is encrypting, false otherwise
     */
    final private boolean encrypting;

    /**
     * The absolute path to the input file. If `filepath` is the empty string, the Boss will take its input from
     * its parent App's top text input.
     */
    final private String filepath;


    /**
     * Allowed values: 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only
     */
    final private byte punctMode;



    /**
     * Creates a new ParsingBoss and initializes its fields. Should be created only from a ParsingDispatcher.
     * @param app reference to the parent app. Can't be null
     * @param encrypting true if the Boss will encrypt its input, false if decrypting
     * @param punctMode 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only. Any other value is not allowed
     * @param filepath absolute path to the input file. Empty (i.e. length=0) if taking input from a text field. Can't be null
     */
    public ParsingBoss(StepperApp app, boolean encrypting, byte punctMode, String filepath) {
        if(app==null) {
            throw new AssertionError("App reference cannot be null");
        }
        if(!(punctMode>=0 && punctMode<=2)) {
            throw new AssertionError("Punctuation mode out of valid range");
        }
        if(filepath==null) {
            throw new AssertionError("Filepath cannot be null");
        }

        this.app=app;
        this.encrypting=encrypting;
        this.punctMode=punctMode;
        this.filepath=filepath;
    }

    /**
     * WARNING! USE ONLY WHEN TESTING METHODS! Creates a new ParsingBoss, but initializes fields against operation preconditions.
     */
    public ParsingBoss() {
        this.app=null;
        this.encrypting=true;
        this.punctMode=127;
        this.filepath=null;
    }



    /**
     * Returns a String containing representations of the Boss's fields.<br><br>
     *
     * Mimics the 'repr' method in a Python class.<br>
     * Mostly a debugging tool.
     *
     * @return String representation of fields
     */
    @Override
    public String toString() {
        String workers = "";
        if(workerThreads==null) {
            workers += "no";
        }
        else {
            workers += workerThreads.length;
        }

        return "Boss with " + workers + " workers, input=\"" + app.fields().text() + "\", key=\"" + app.fields().key() +
                "\", encrypting=" + encrypting + ", punctuation=" + punctMode;
    }


    /**
     * Returns the result of the Boss's processing.
     * Also sets the App's output when finished and periodically updates the App's progress text.<br><br>
     *
     * By the end of this method, the parent App's fields (i.e. app.fields()) should be updated with the results.<br>
     * If this method throws an exception, excluding exceptions thrown when interrupted, the method will
     * load the App's key field with an error message, which will be interpreted by the Dispatcher that uses this Boss.
     * Error messages always start with "~~~".<br><br>
     *
     * WARNING: Any exceptions thrown in this method are SILENT. They will not stop the program and produce no error messages.
     *
     * @return the Boss's results. This method should NEVER return null.
     */
    @Override
    protected String doInBackground() throws Exception {
        //Idiot check
        if(app==null) {
            throw new AssertionError("App reference cannot be null");
        }
        if(!(punctMode>=0 && punctMode<=2)) {
            throw new AssertionError("Punctuation mode out of valid range");
        }
        if(filepath==null) {
            throw new AssertionError("Filepath cannot be null");
        }


        //Switch the screen
        app.setScreen("PROCESSING");

        //Take the input, depending on if loading from text or file
        try {
            if (filepath.equals(StepperAppFields.TEXT_LOAD_SIGNAL)) {
                app.fields().setText(app.topInputValue());
            } else {
                app.fields().setText(getTextFromFile(filepath));
            }
        }
        catch(FileNotFoundException e) {
            app.fields().setKey(StepperAppFields.INPUT_ERROR_SIGNAL + e.getMessage());
            return "";
        }
        catch(Throwable t) {
            app.fields().setKey(StepperAppFields.INPUT_ERROR_SIGNAL + "Exception at text loading- " + t);
            return "";
        }


        //Set local copies of the inputs
        String text = app.fields().text();
        String inputKey = app.fields().key();


        System.gc();
        app.setLoadingStatusText("Formatting...");

        /////////////////////////////////////////////////////
        //REMOVE DIACRITICS USING ALL THREADS

        //Create text pieces
        String[] textPieces = setWorkerLoads(text,
                app.fields().threadCount(),
                StepperAppFields.BLOCK_LENGTH);

        //Create worker threads and assign them a workload
        workerThreads = new ParsingDiacriticsWorker[textPieces.length];
        for(int i=0; i<workerThreads.length; i++) {
            workerThreads[i] = new ParsingDiacriticsWorker(textPieces[i], Integer.toString(i));
        }


        //Take output from each worker thread
        textPieces = new String[workerThreads.length];
        try {
            //Execute workers
            for(int i=0; i<workerThreads.length; i++) {
                workerThreads[i].execute();
            }
            for (int i = 0; i < workerThreads.length; i++) {
                textPieces[i] = workerThreads[i].get();
            }
        }
        catch (ExecutionException | InterruptedException e) {
            for (int i = 0; i < workerThreads.length; i++) {
                workerThreads[i].cancel(true);
            }
            return "";
        }

        //Reload the text with the results
        text = "";
        for(int i=0; i<workerThreads.length; i++) {
            text += textPieces[i];
        }


        /////////////////////////////////////////////////////
        //OPERATION
        //Doesn't use a function to save memory, processing power, and the organizational scheme

        System.gc();
        app.setLoadingStatusText("Executing...");

        //Format the key
        byte[][] operationsKey = createKeyBlocks(inputKey, StepperAppFields.BLOCK_COUNT, StepperAppFields.BLOCK_LENGTH);
        //Load formatted key into app
        app.fields().setKey(arrToString(operationsKey));


        //Create loads
        textPieces = setWorkerLoads(text,
                app.fields().threadCount(),
                StepperAppFields.BLOCK_LENGTH);

        //Make the worker threads: one index for each piece of the text
        workerThreads = new ParsingOperationsWorker[textPieces.length];
        int startingBlock = 0;
        int numberCount = 0;
        for (int i = 0; i < workerThreads.length; i++) {
            workerThreads[i] = new ParsingOperationsWorker(textPieces[i], operationsKey,
                    encrypting, punctMode, startingBlock, numberCount, Integer.toString(i));

            int[] charCounts = countAlphaAndNumericChars(textPieces[i]);
            startingBlock += charCounts[0] / StepperAppFields.BLOCK_LENGTH;
            numberCount += charCounts[1];
        }
        textPieces = null;

//        for(SwingWorker w : workerThreads) {
//            System.out.println(w);
//        }

        System.gc();


        //Make array to hold the result. Put the results from each thread into the result
        textPieces = new String[workerThreads.length];
        Arrays.fill(textPieces, "");
        try {
            //Start each worker thread
            for (int i = 0; i < workerThreads.length; i++) {
                workerThreads[i].execute();
            }

            for (int i = 0; i < workerThreads.length; i++) {
                textPieces[i] = workerThreads[i].get();
            }
        }
        catch (ExecutionException | InterruptedException e) {
            for (int i = 0; i < workerThreads.length; i++) {
                workerThreads[i].cancel(true);
            }
            return "";
        }


        workerThreads = null;
        System.gc();

        //Create the output
        text = "";
        for (int i = 0; i < textPieces.length; i++) {
            text += textPieces[i];
        }


        textPieces = null;
        app.fields().setText(text);

        //Screen changing occurs in the ParsingDispatcher that created this Boss
        return text;
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////




    /**
     * Returns input, but converted to a String<br><br>
     *
     * input should contain numerical values representing English letters. a=0, b=1, c=2... z=25<br>
     * The output should contain the characters represented by the numerical values in order.<br>
     *
     * Order: In order of increasing subarray index first, then by order of array index. input[0][1] should come
     * before input[1][0].<br>
     *
     * Note: the output string should entirely consist of lowercase English ASCII characters.<br><br>
     *
     * @param input array to convert to a String. Can't be null. All indices must be on [0,25]
     * @return String containing letters represented by the input's numerical values, in order
     */
    private String arrToString(byte[][] input) {
        if(input==null) {
            throw new AssertionError("Input cannot be null");
        }

        String output="";

        for(int a=0; a<input.length; a++) {
            for(int i=0; i<input[a].length; i++) {
                if(input[a][i]<0 || input[a][i]>25) {
                    throw new AssertionError("All input indices must be on the interval [0,25]");
                }

                output += (char)(input[a][i] + 97);
            }
        }

        return output;
    }


    /**
     * Returns the amount of lowercase English ASCII characters in index 0 and the amount of numeric characters in index 1.<br><br>
     *
     * Returns an array to prevent looping over the same string twice.
     *
     * @param input String to count alphabetic and numeric characters in
     * @return {number of alphabetic chars, number of numeric chars}
     */
    private int[] countAlphaAndNumericChars(String input) {
        int[] output = new int[] {0,0};

        for(int i=0; i<input.length(); i++) {
            if((int)input.charAt(i)>=97 && (int)input.charAt(i)<=122) {
                output[0]++;
            }

            if((int)input.charAt(i)>=48 && (int)input.charAt(i)<=57) {
                output[1]++;
            }
        }

        return output;
    }


    /**
     * Returns a byte[][] array with `blocks` indices, each with `charsPerBlock` characters,
     * containing the text from `input` as numerical values.<br><br>
     *
     * -Numerical values: a=0, b=1, c=2... z=25. A=0, B=1, C=2... Z=25. Note: uppercase letters are the same as lowercase letters<br>
     *
     * -Before the input can be processed, removeDiacritics must be called on each character of the input.<br>
     *
     * -All non-letters (any character that is not an English ASCII letter after removeDiacritics is called) are to be ignored.<br>
     *
     * -If `input` contains less than `blocks`*`charsPerBlock` English ASCII letters, any character not filled by `input`
     * becomes a random value on the interval [0,25]. If `input` contains more than `blocks`*`charsPerBlock` English ASCII letters,
     * any character past index `blocks`*`charsPerBlock` in the input is ignored.<br>
     *
     * @param input the input text, can't be null
     * @param blocks number of indices in the output array, must be positive
     * @param charsPerBlock number of indices in each of the output's subarrays, must be positive
     * @return `blocks` by `charsPerBlock` byte[][] array loaded with text from `input`
     */
    private byte[][] createKeyBlocks(String input, int blocks, int charsPerBlock) {
        if(input==null) {
            throw new AssertionError("Input string cannot be null");
        }
        if(blocks<=0 || charsPerBlock<=0) {
            throw new AssertionError("Blocks and characters per block must be positive");
        }

        //The formatted key will have all lowercase ASCII characters in it
        String formattedKey="";


        //Create the formatted key. Fill until every input character is loaded
        for(int i=0; i<input.length(); i++) {
            char currentChar = Character.toLowerCase(input.charAt(i));
            currentChar = removeDiacritics(currentChar);

            if(currentChar>=97 && currentChar<=122) {
                formattedKey += currentChar;
            }
        }


        //Create a sequence of random numbers
        SecureRandom rng = new SecureRandom();
        rng.nextInt();

        //If the output is not filled, load with random characters
        while(formattedKey.length() < blocks*charsPerBlock) {
            for(int r=0; r<rng.nextInt(); r++) {
                rng.nextInt();
            }
            int currentRandChar = rng.nextInt();

            //Convert the next random number to a non-negative number
            if(currentRandChar < 0) {
                currentRandChar = currentRandChar*-1;
            }

            //Convert the number to a lowercase character ASCII value
            currentRandChar = (currentRandChar%26 + 97);

            //Add random character to the formatted key
            formattedKey += (char)currentRandChar;
        }
//        System.out.println(formattedKey);

        //At this point, the formatted key should contain blocks*charsPerBlock characters.
        byte[][] output = new byte[blocks][charsPerBlock];
        int inputIndex=0;
        //Load the output with the input's indices
        for(int a=0; a<blocks; a++) {
            for(int i=0; i<charsPerBlock; i++) {
                output[a][i]=(byte)(formattedKey.charAt(inputIndex) - 97);
                inputIndex++;
            }
        }

        return output;
    }

    /**
     * Returns the value given by createKeyBlocks
     *
     * @param input the input text, can't be null
     * @param blocks number of indices in the output array, must be positive
     * @param charsPerBlock number of indices in each of the output's subarrays, must be positive
     * @return value from createKeyBlocks
     */
    public byte[][] createKeyBlocks_Testing(String input, int blocks, int charsPerBlock) {
        return createKeyBlocks(input, blocks, charsPerBlock);
    }


    /**
     * Returns all the text from a file whose name is `filepath`. If `filepath` is the empty string, loads from
     * StepperFunctions.DEFAULT_INPUT_FILE.<br><br>
     *
     * The input filepath must end with the ".txt" extension.<br>
     *
     * If the input filepath does not end in ".txt" or the file could not be read, throws a FileNotFoundException.<br>
     *
     * @param filepath name of the input file. Can't be null
     * @return contents from the given input filename
     * @throws FileNotFoundException if the file can't be read or the filename lacks the ".txt" extension.
     * Displays a descriptive error message if thrown.
     */
    private String getTextFromFile(String filepath) throws FileNotFoundException {
        if(filepath==null) {
            throw new AssertionError("Filename cannot be null");
        }

        File inputFile;

        //Create file from default or the top text input
        if(filepath.equals("")) {
            inputFile = new File(StepperAppFields.DEFAULT_INPUT_FILE);
        }
        else {
            inputFile = new File(filepath);
        }

        String output = "";

        //Check if the input file ends in .txt
        if(inputFile.getName().length()<=3 || !inputFile.getName().substring((int) (inputFile.getName().length()-4)).equals(".txt")) {
            throw new FileNotFoundException("The input file must have a .txt extension");
        }
        else {
            //Read the file and load it into the fields
            try {

                Scanner fileReader = new Scanner(inputFile);

                while (fileReader.hasNextLine()) {
                    output += fileReader.nextLine();
                    output += "\n";
                }
            }
            //If error, throw an exception
            catch (FileNotFoundException e) {
                String fileErrorMsg = "The input file \"" + inputFile.getName() + "\" does not exist\n";
                if(filepath.equals("")) {
                    fileErrorMsg += "in the folder containing the app";
                }
                else {
                    fileErrorMsg += "at the given absolute path";
                }

                throw new FileNotFoundException(fileErrorMsg);
            }
        }

        return output;
    }


    /**
     * Returns a lowercase version of the input without accent marks or letter variants.<br><br>
     * Helper to createBlocks
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


    /**
     * Returns the appropriate number of blocks to go into each output index<br><br>
     *
     * Helper to setWorkerLoads.
     *
     * @param totalBlocks total number of blocks in the input string. Must be positive
     * @param threads number of blocks in the output array. Must be positive
     * @return array of length `threads` containing appropriate number of blocks per output index
     */
    private int[] setBlocksPerIndex(int totalBlocks, int threads) {
        /*
        This is roughly equivalent to putting the first block of the input in index 0 of the output array, the second in index 1...
        Eventually, the end of the output array is reached. The block after goes into the first output array index...

        Suppose there are 8 blocks in the input and 3 indices in the output.
        After loading the first 3 blocks: {[1], [2], [3]}
        After loading the next 3 blocks: {[1,4], [2,5], [3,6]}
        After loading the final 2 blocks: {[1,4,7], [2,5,8], [3,6]}
        This arrangement succeeds in evenly splitting the blocks among the output indices.
         */
        if(totalBlocks<=0) throw new AssertionError("Block total must be positive");
        if(threads<=0) throw new AssertionError("Thread count must be positive");

        int[] output = new int[threads];
        int outputIndex = threads-1;

        for(int i=totalBlocks-1; i>=0; i--) {
            output[outputIndex]++;
            outputIndex--;

            if(outputIndex < 0) {
                outputIndex = threads-1;
            }
        }

//        System.out.println(Arrays.toString(output));
        return output;
    }


    /**
     * Splits `text` evenly into `threads` pieces.
     * The number of alphabetic characters of each piece must be a multiple of `blockLength`, except for the last piece.<br><br>
     *
     * -Alphabetic characters are lowercase English ASCII characters.<br>
     *
     * -All indices except for the last one should have `blockLength` alphabetic characters, or a multiple thereof.<br>
     *
     * -If `threads` is greater than ceil(text.length()/blockSize), any strings not used should be empty strings, not null.<br>
     *
     * -Note: The final character of each block (excluding the last block) should end in an alphabetic character.<br><br>
     *
     * The test cases may fail. If so, manually check if the thread loads are even in each failed test.
     * Even distribution and piece length being a multiple of `threads` are the most important aspects of the output.<br>
     *
     * @param text the text to split, non-null
     * @param threads how many pieces `text` should be split into, non-negative. If zero, returns {""}
     * @param blockLength number (or a divisor) of alphabetic characters per piece, non-negative
     * @return array of Strings. There are `threads` total Strings evenly split among the output's indices
     */
    private String[] setWorkerLoads(String text, int threads, int blockLength) {

        if (text == null || threads < 0 || blockLength<=0) {
            throw new AssertionError("No argument can be null or zero");
        }

        if(threads==0) {
            return new String[] {""};
        }

        //Find effective length of the text to create the blocks without wasting memory
        int alphaChars = 0;
        for(int i=0; i<text.length(); i++) {
            if(text.charAt(i)>=97 && text.charAt(i)<=122) {
                alphaChars++;
            }
        }

        //Create String array to hold the blocks
        String[] blocks = new String[(int) Math.ceil(alphaChars / (float)blockLength)];
        Arrays.fill(blocks, "");
        if(blocks.length==0) {
            return new String[] {""};
        }

        //Load each String with blockSize characters until the end is reached
        int currentBlock = 0;
        alphaChars = 0;
        for (int i = 0; i < text.length(); i ++) {
            blocks[currentBlock] += text.charAt(i);

            //Add to alphabetic char total
            if(text.charAt(i)>=97 && text.charAt(i)<=122) {
                alphaChars++;
            }

            //Move to new block when current block is filled with alpha chars
            if(alphaChars>=blockLength) {
                alphaChars=0;
                currentBlock++;
            }
            //Prevent array overruns
            if(currentBlock >= blocks.length) {
                currentBlock--;
            }
        }

//        for(int i=0; i<blocks.length; i++) {
//            System.out.println("\"" + blocks[i] + "\"");
//        }
//        System.out.println();

        //Create another String array to hold the result and load it
        String[] output = new String[threads];
        Arrays.fill(output, "");


        currentBlock = 0;
        int[] blocksPerIndex = setBlocksPerIndex(blocks.length, threads);

        //Move through the threads
        for(int t=0; t<threads; t++) {
            for(int w=0; w<blocksPerIndex[t]; w++) {
                output[t] += blocks[currentBlock];
                currentBlock++;
            }
        }

        return output;
    }
}
