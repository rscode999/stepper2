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
 * If a problem occurs during processing that must be displayed on the main App, an error message is placed
 * into the `errorMessage` field and used by the Dispatcher.<br><br>
 *
 * All private helper methods must continuously check if the Boss is cancelled. If so, the method should return
 * the empty string, unless the specification states otherwise. Methods that run quickly and in O(1) time, i.e. where its runtime
 * doesn't depend on an arbitrary input length, do not need to check if the Boss is cancelled.
 */
public class ParsingBoss extends SwingWorker<Void,Void> {

    /**
     * The worker threads that this Boss employs. The array's length may vary depending on the number of threads used
     */
    private SwingWorker<String,Void>[] workerThreads;


    /**
     * The parent app that the Boss works for.
     * This reference is needed so the Boss can change the app's screen and variable fields. Can't be null
     */
    final private StepperApp app;


    /**
     * True if the boss is encrypting, false otherwise
     */
    final private boolean encrypting;

    /**
     * Holds any error message that the Boss should display on the main App, via its Dispatcher.<br>
     * If no errors occur, this field will be the empty string.<br><br>
     *
     * Cannot be null
     */
    String errorMessage;

    /**
     * The absolute path to the input file.<br><br>
     *
     * -If equal to `StepperAppFields.TEXT_LOAD_SIGNAL`, the Boss will take its input from
     * its parent App's top text field.<br>
     * -If empty, the Boss will take input from `StepperAppFields.DEFAULT_INPUT_FILENAME`.<br><br>
     *
     * Cannot be null
     */
    final private String filepath;


    /**
     * Allowed values: 0 if including punctuation, 1 if excluding spaces, 2 if alphabetic characters only
     */
    final private byte punctMode;


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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
        this.filepath=filepath;
        this.punctMode=punctMode;

        this.errorMessage = "";
    }

    /**
     * WARNING! USE ONLY IN METHOD UNIT TESTS!!! Creates a new ParsingBoss, but initializes fields against operation preconditions.
     */
    public ParsingBoss() {
        this.app=null;
        this.encrypting=true;
        this.punctMode=127;
        this.filepath=null;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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

        return "Boss with " + workers + " workers, encrypting=" + encrypting + ", filepath=\"" + filepath + "\"," +
                " punctuation=" + punctMode;
    }

    /**
     * Returns the error message field of the Boss. The error message is the empty string if there are no errors.
     * @return the Boss' error message
     */
    public String errorMessage() {
        return this.errorMessage;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Loads the App's fields with the result and periodically updates the App's progress text.<br><br>
     *
     * By the end of the method, the parent App's output fields (text and key) should be loaded with the results.<br>
     *
     * If this method throws an exception, excluding exceptions thrown when interrupted, the method will
     * load the Boss's input error message field with a descriptive error message. The message will be displayed by a
     * Dispatcher.<br><br>
     *
     * WARNING: Any exceptions thrown in this method are SILENT. They will not stop the program and produce no error messages.
     */
    @Override
    protected Void doInBackground() {
        //Idiot check, in case the test constructor was used
        if(app==null
        || !(punctMode>=0 && punctMode<=2)
        || filepath==null) {
            System.err.println("WARNING: OPERATION PRECONDITIONS ARE NOT MET. TEST CONSTRUCTOR WAS USED");
            throw new AssertionError("Operation preconditions are not met");
        }

        //Switch the screen. At this point, the text on the Processing screen says "Loading input..."
        app.setScreen("PROCESSING");


        /////////////////////////////////////////////////////
        //TAKE THE INPUT


        //Get the input, depending on if loading from text or file
        String rawText;
        //Load from the text inputs if the text load signal is given as a filepath
        if (StepperAppFields.TEXT_LOAD_SIGNAL.equals(filepath)) {
            rawText = app.topTextInputValue();
        }
        //Otherwise, load from file
        else {
            try {
                rawText = getTextFromFile(filepath); //getTextFromFile gives a descriptive error message
            }
            //If file loading goes wrong, load the error message with the error message
            catch(FileNotFoundException e) {
                errorMessage = e.getMessage();
                return null;
            }
            //If anything else goes wrong, stop
            catch (Throwable t) {
                System.err.println("Exception thrown in text loading- " + t.toString());
                return null;
            }
        }


        //Convert the input into a StringBuilder for faster loading
        StringBuilder text = new StringBuilder(rawText);

        //Remove text to save memory
        rawText = null;


        System.gc();
        app.setProcessingStepText((app.fields().threadCount()<=1) ?
                "Formatting 1 thread, " + text.length() + " characters..." :
                "Formatting " + app.fields().threadCount() + " threads, " + text.length() + " characters...");


        /////////////////////////////////////////////////////
        //REMOVE DIACRITICS USING ALL THREADS


        //Create text pieces
        String[] textPieces = setWorkerLoads(text.toString(),
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
            for(SwingWorker<String,Void> workerThread : workerThreads) {
                workerThread.execute();
            }
            for(int i = 0; i < workerThreads.length; i++) {
                textPieces[i] = workerThreads[i].get();
            }
        }
        catch (InterruptedException | ExecutionException e) {
            for(SwingWorker<String,Void> workerThread : workerThreads) {
                workerThread.cancel(true);
            }
            return null;
        }
        catch (Exception e) {
            System.err.println("Exception in Boss during diacritics removal- " + e);
            return null;
        }

        //Reload the text with the results
        text = new StringBuilder();
        for(int i=0; i<workerThreads.length; i++) {
            text.append(textPieces[i]);
        }


        /////////////////////////////////////////////////////
        //OPERATION


        System.gc();
        app.setProcessingStepText((app.fields().threadCount()<=1) ?
                "Loading 1 thread, " + text.length() + " characters..." :
                "Loading " + app.fields().threadCount() + " threads, " + text.length() + " characters...");

        //Format the key
        byte[][] operationsKey = createKeyBlocks(
        app.bottomTextInputValue(), StepperAppFields.BLOCK_COUNT, StepperAppFields.BLOCK_LENGTH
        );

        //Assign workloads to threads
        textPieces = setWorkerLoads(
        text.toString(),
        app.fields().threadCount(),
        StepperAppFields.BLOCK_LENGTH
        );

        //Make the worker threads: one index for each piece of the text
        workerThreads = new ParsingOperationsWorker[textPieces.length];
        int startingBlock = 0;
        int numberCount = 0;
        for (int i = 0; i < workerThreads.length; i++) {
            workerThreads[i] = new ParsingOperationsWorker(
            textPieces[i], operationsKey, encrypting, punctMode, startingBlock, numberCount, Integer.toString(i)
            );

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
            for(SwingWorker<String,Void> workerThread : workerThreads) {
                workerThread.execute();
            }

            for (int i = 0; i < workerThreads.length; i++) {
                textPieces[i] = workerThreads[i].get();
            }
        }
        //If interrupted, stop all the workers
        catch (InterruptedException | ExecutionException e) {
            for(SwingWorker<String,Void> workerThread : workerThreads) {
                workerThread.cancel(true);
            }
            return null;
        }
        catch (Exception e) {
            System.err.println("Boss thread: Error during execution- " + e);
            return null;
        }


        workerThreads = null;
        System.gc();


        app.setProcessingStepText("Executing...");

        //Load the output into the parent App (this is a thread-safe operation) in chunks
        final int LOAD_SIZE = 10000;
        int charsLoaded = 0;

        //Load from each thread
        for(int t=0; t<textPieces.length; t++) {

            //Load the next LOAD_SIZE characters from the current thread
            for(int c=0; c<textPieces[t].length(); c+=LOAD_SIZE) {

                //If less than LOAD_SIZE characters left, load all remaining characters
                if(c+LOAD_SIZE >= textPieces[t].length()) {
                    app.setOutputTextArea(textPieces[t].substring(c), true);
                    charsLoaded += textPieces[t].substring(c).length();
                }
                //Otherwise, load LOAD_SIZE characters
                else {
                    app.setOutputTextArea(textPieces[t].substring(c, c + LOAD_SIZE), true);
                    charsLoaded += textPieces[t].substring(c, c+LOAD_SIZE).length();
                }

                //Update the progress
                app.setProcessingProgressText(String.valueOf(charsLoaded) + " characters processed" );

                if(isCancelled()) {
                    return null;
                }
            }
        }

        textPieces = null;


        //Load the key into the parent App (this is a thread-safe operation)
        app.setOutputKeyArea(arrToString(operationsKey), false);

        System.gc();
        //Screen changing occurs in the ParsingDispatcher that created this Boss
        return null;
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //FUNCTIONS




    /**
     * Returns `input`, but converted to a String<br><br>
     *
     * `input` should contain numerical values representing English letters. a=0, b=1, c=2... z=25<br>
     * The output should contain the characters represented by the numerical values in order.<br>
     *
     * Order: In order of increasing subarray index first, then by order of array index. input[0][1] should come
     * before input[1][0].<br>
     *
     * Note: the output string should entirely consist of lowercase English ASCII characters.<br><br>
     *
     * @param input array to convert to a String. Can't be null, no subarrays can be null. All indices must be on [0,25]
     * @return String containing letters represented by the input's numerical values, in order
     */
    private String arrToString(byte[][] input) {
        if(input==null) {
            throw new AssertionError("Input cannot be null");
        }

        StringBuilder output= new StringBuilder();

        //Loop through each character in the input. Append it to the string output
        for(byte[] block : input) {
            if(block == null) {
                throw new AssertionError("No subarray of the input can be null");
            }

            for(byte index : block) {
                if (index < 0 || index > 25) {
                    throw new AssertionError("All input indices must be on the interval [0,25]");
                }

                output.append((char) (index + 97));
            }
        }

        return output.toString();
    }


    /**
     * Returns the amount of lowercase English ASCII characters in index 0 and the amount of numeric characters in index 1.
     * If cancelled, returns {0,0}.<br><br>
     *
     * Returns an array to prevent looping over the same string twice.
     *
     * @param input String to count alphabetic and numeric characters in
     * @return {number of alphabetic chars, number of numeric chars}, or {0,0} if the Boss is cancelled
     */
    private int[] countAlphaAndNumericChars(String input) {
        int[] output = new int[] {0,0};

        for(int i=0; i<input.length(); i++) {
            //alphabetic character: +index 0
            if((int)input.charAt(i)>=97 && (int)input.charAt(i)<=122) {
                output[0]++;
            }

            //numeric character: +index 1
            if((int)input.charAt(i)>=48 && (int)input.charAt(i)<=57) {
                output[1]++;
            }

            //cancel check
            if(isCancelled()) {
                return new int[] {0,0};
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
     * @param input the input text. Can't be null
     * @param blocks number of indices in the output array. Must be positive
     * @param charsPerBlock number of indices in each of the output's subarrays. Must be positive
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
        StringBuilder formattedKey = new StringBuilder();


        //Create the formatted key. Fill until every input character is loaded
        for(int i=0; i<input.length(); i++) {
            char currentChar = Character.toLowerCase(input.charAt(i));
            currentChar = removeDiacritics(currentChar);

            if(currentChar>=97 && currentChar<=122) {
                formattedKey.append(currentChar);
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
            formattedKey.append((char) currentRandChar);
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
     * any character past index `blocks`*`charsPerBlock` in the input is ignored.<br><br>
     *
     * FOR UNIT TESTING ONLY!!!
     *
     * @param input the input text. Can't be null
     * @param blocks number of indices in the output array. Must be positive
     * @param charsPerBlock number of indices in each of the output's subarrays. Must be positive
     * @return `blocks` by `charsPerBlock` byte[][] array loaded with text from `input`
     */
    public byte[][] createKeyBlocks_Testing(String input, int blocks, int charsPerBlock) {
        return createKeyBlocks(input, blocks, charsPerBlock);
    }


    /**
     * Returns all the text from a file whose name is `filepath`. If `filepath` is the empty string, loads from
     * StepperFunctions.DEFAULT_INPUT_FILE.<br><br>
     *
     * The input filepath must end with the ".txt" extension.<br>
     * If the input filepath is empty, does not end in ".txt", or the file could not be read,
     * throws a FileNotFoundException.<br>
     *
     * @param filepath name of the input file. Can't be null
     * @return contents from the given input filename, or the empty string if the Boss is cancelled
     * @throws FileNotFoundException if the file can't be read or the filename lacks the ".txt" extension.
     * Displays a descriptive error message, which is used in the main App, if thrown.
     */
    private String getTextFromFile(String filepath) throws FileNotFoundException {
        if(filepath==null) {
            throw new AssertionError("Filename cannot be null");
        }

        File inputFile;

        //Create file from default or the top text input
        if(filepath.isEmpty()) {
            inputFile = new File(StepperAppFields.DEFAULT_INPUT_FILENAME);
        }
        else {
            inputFile = new File(filepath);
        }

        StringBuilder output = new StringBuilder();

        //Check if the input file ends in .txt
        if(inputFile.getName().length()<=3 || !inputFile.getName().endsWith(".txt")) {
            throw new FileNotFoundException("The input file must have a .txt extension");
        }
        else {
            //Read the file and load it into the fields
            try {
                Scanner fileReader = new Scanner(inputFile);

                //Load all the lines
                while (fileReader.hasNextLine()) {
                    if(isCancelled()) {
                        return "";
                    }

                    output.append(fileReader.nextLine());
                    output.append("\n");
                }
            }
            //If error, create a nicer error message and throw an exception with it
            catch (FileNotFoundException e) {
                String fileErrorMsg = "The input file \"";

                fileErrorMsg += (inputFile.getName().length() < 20) ?
                        inputFile.getName() :
                        inputFile.getName().substring(0, 16) + "... .txt";

                fileErrorMsg += "\" does not exist\n";

                if(filepath.contains("\\") || filepath.contains("/")) {
                    fileErrorMsg += "at the given absolute path";
                }
                else {
                    fileErrorMsg += "in the folder containing the app";
                }

                throw new FileNotFoundException(fileErrorMsg);
            }
        }

        return output.toString();
    }


    /**
     * Returns a lowercase version of the input without accent marks or letter variants.<br><br>
     *
     * Helper to createBlocks
     *
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
        final char[] inChars={'a', 'c', 'd', 'e', 'i', 'o', 'n', 's', 'u', 'y',  '0', '1',
                '2', '3', '4', '5', '6', '7', '8', '9', '-'};
        char charReplacement='#';


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
     * Returns a lowercase version of the input without accent marks or letter variants.<br><br>
     *
     * FOR UNIT TESTING ONLY!
     *
     * @param input letter to remove diacritics from
     * @return copy of input without diacritics
     */
    public char removeDiacritics_Testing(char input) {
        return removeDiacritics(input);
    }


    /**
     * Returns an array containing `text` split evenly into `threads` pieces.
     * The number of alphabetic characters of each piece must be a multiple of `blockLength`, except for the last piece.<br><br>
     *
     * -Alphabetic characters are lowercase English ASCII characters.<br>
     *
     * -All indices except for the last one should have `blockLength` alphabetic characters or a multiple thereof.<br>
     *
     * -Any unused threads should be assigned the empty string, not null. Empty strings may occur at the beginning of the output array.<br>
     *
     * -Note: The final character of each output index (excluding the last index) should end in an alphabetic character.
     *
     * @param text the text to split. Non-null
     * @param threads how many pieces `text` should be split into. If zero, or the Boss is cancelled, returns {""}. Cannot be negative
     * @param blockLength number of characters, or a multiple thereof, to put in each piece. Must be positive
     * @return array of Strings. There are `threads` total Strings whose alphabetic characters are
     * evenly split among the output's indices
     */
    private String[] setWorkerLoads(String text, int threads, int blockLength) {

        //Assert preconditions
        if (text==null || threads<0 || blockLength<=0) {
            throw new AssertionError("No argument can be null or zero");
        }

        //Return the empty string if threads is 0
        if(threads==0) {
            return new String[] {""};
        }

        //Find effective length of the text to create the blocks without wasting memory
        int alphaChars = 0;
        for(int i=0; i<text.length(); i++) {
            if(text.charAt(i)>=97 && text.charAt(i)<=122) {
                alphaChars++;
            }

            if(isCancelled()) {
                return new String[] {""};
            }
        }


        //CALCULATE NUMBER OF BLOCKS PER THREAD

        //Number of blocks equals the number of alphabetic characters divided by the block length
        //If there is a remainder, there is an extra block
        int nBlocks = alphaChars / blockLength;
        if (alphaChars % blockLength != 0) nBlocks++;
        //Note: one block is a piece of length `blockLength` or shorter

        //Create the number of characters in each thread. Temporarily holds the number of blocks
        int[] charsPerThread = new int[threads];

        //The minimum number of blocks per piece is the number of blocks divided by the number of threads
        Arrays.fill(charsPerThread, nBlocks / threads);
        //The number of remaining blocks equals the remainder of the number of blocks divided by the number of threads
        for (int i = charsPerThread.length - 1; i >= charsPerThread.length - nBlocks % threads; i--) {
            charsPerThread[i]++;
        }


        //CALCULATE NUMBER OF CHARACTERS PER THREAD

        //Create a StringBuilder array to hold the result and load it. Also properly calculate number of characters per thread
        StringBuilder[] threadLoads = new StringBuilder[threads];
        for(int t=0; t<threadLoads.length; t++) {
            threadLoads[t] = new StringBuilder();
            charsPerThread[t] *= blockLength; //now, charsPerThread holds the number of characters per thread

            //Check if cancelled, abort if so
            if(isCancelled()) {
                return new String[] {""};
            }
        }


        //LOAD THE THREADS

        //Move through each thread, except for the last one, and load it
        int currentThread = 0;
        int currentTextIndex = 0;
        while(currentThread < threadLoads.length) {

            //If there are no more characters to load, move to the next thread
            if(charsPerThread[currentThread] == 0) {
                currentThread++;
            }
            //If not, load the character
            else {
                threadLoads[currentThread].append(text.charAt(currentTextIndex));

                //Update number of alphabetic characters if a letter was loaded
                if(text.charAt(currentTextIndex)>=97 && text.charAt(currentTextIndex)<=122) {
                    charsPerThread[currentThread]--;
                }

                //Move to the next index
                currentTextIndex++;
            }

            //Exit the loop if it will overrun the input text
            if(currentTextIndex >= text.length()) {
                break;
            }

            //Check if cancelled, abort if so
            if(isCancelled()) {
                return new String[] {""};
            }
        }

        //Load any non-alphabetic character that was not yet loaded into the last thread
        while(currentTextIndex < text.length()) {
            threadLoads[threadLoads.length-1].append(text.charAt(currentTextIndex));
            currentTextIndex++;

            //Check if cancelled, abort if so
            if(isCancelled()) {
                return new String[] {""};
            }
        }

        //Convert StringBuilders to proper strings
        String[] output = new String[threads];
        for(int l=0; l<threadLoads.length; l++) {
            output[l] = threadLoads[l].toString();

            //Check if cancelled, abort if so
            if(isCancelled()) {
                return new String[] {""};
            }
        }

        return output;
    }


    /**
     * FOR UNIT TESTING ONLY!<br><br>
     *
     * Returns an array containing `text` split evenly into `threads` pieces.
     * The number of alphabetic characters of each piece must be a multiple of `blockLength`, except for the last piece.<br><br>
     *
     * -Alphabetic characters are lowercase English ASCII characters.<br>
     *
     * -All indices except for the last one should have `blockLength` alphabetic characters or a multiple thereof.<br>
     *
     * -Any unused threads should be assigned the empty string, not null. Empty strings may occur at the beginning of the output array.<br>
     *
     * -Note: The final character of each output index (excluding the last index) should end in an alphabetic character.<br><br>
     *
     * The test cases may fail. If so, manually check if the thread loads are even in each failed test.
     * An even distribution of work and a piece length being a multiple of `threads` are the most important aspects of the output.
     *
     * @param text the text to split. Non-null
     * @param threads how many pieces `text` should be split into. If zero, or the Boss is cancelled, returns {""}. Cannot be negative
     * @param blockLength number of characters, or a multiple thereof, to put in each piece. Must be positive
     * @return array of Strings. There are `threads` total Strings evenly split among the output's indices
     */
    public String[] setWorkerLoads_Testing(String text, int threads, int blockLength) {
        return setWorkerLoads(text, threads, blockLength);
    }
}
