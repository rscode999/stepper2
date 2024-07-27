import java.io.File;
import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Class containing non-GUI related functions and constants. Contains no variable fields.<br><br>
 *
 * Allows Boss and Worker threads to access required functions and keep non-GUI functions in an easy-to-find place outside the main app.<br>
 * The field/method separation also minimizes possible rep exposure when methods and fields are part of the same class,
 * which is passed into multiple objects that can modify fields.<br><br>
 *
 * No function should modify its inputs, unless otherwise specified. All modifications must occur on defensive copies.
 */
public class StepperFunctions {

    /**
     * Number of blocks to use in processes. Must be positive. Highly recommended to be a prime number.
     */
    final public static int BLOCK_COUNT = 7;

    /**
     * Length of each block to use in processes. Must be positive. Highly recommended to be a prime number, or at least relatively prime with BLOCK_COUNT.
     */
    final public static int BLOCK_LENGTH = 19;


    /**
     * Name of the default text input file. Must end in ".txt".
     */
    final public static String DEFAULT_INPUT_FILE = "input.txt";


    /**
     * Amount to rotate each block. Length must equal BLOCK_COUNT. Must be private. Indices are accessed through a getter method.
     */
    final private static byte[] KEY_BLOCK_INCREMENTS = {1,2,3,5,7,11,13};


    /**
     * Maximum number of threads that can be used for a process. Must be at least 1
     */
    final public static int MAX_THREADS = 999;


    /**
     * Array of valid passwords, used in the login method. Can't be null. None of its indices can be null<br><br>
     *
     * Must be separate from the login method to facilitate testing.<br><br>
     */
    final private String[] VALID_PASSWORDS;


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //CONSTRUCTORS

    /**
     * Creates a new instance of StepperFunctions with default passwords loaded.<br><br>
     *
     * Always use this constructor.
     */
    public StepperFunctions() {
        VALID_PASSWORDS = new String[]{"11111111", "test", "12345", "111111", "123123", "42069",
                "123456", "1234567", "12345678", "123456789", "11111", "111111111", "null",
                "pasword", "asdfghjkl", "qwerty", "qwertyuiop", "1234567890", "123467890", "abc123",
                "123567890", "314159265", "69420", "test1", "iloveyou",
                "asdfghjkl", "Password1", "12345", "123456", "1234567890", "123467890", "123567890",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "abcd1234", "987654321",
                "asdf", "password", "0", "letmein", ""};

        if(constantInvarsBroken()) {
            throw new AssertionError("An invariant of a constant is not true");
        }
    }

    /**
     * Creates a new instance of StepperFunctions, but with VALID_PASSWORDS custom initialized.<br><br>
     *
     * No index in `passwords` may be null. If any index is null, throws IllegalArgumentException.<br><br>
     *
     * This constructor should only be used in testing the login method.
     */
    public StepperFunctions(String[] passwords) {
        if(passwords == null) {
            throw new AssertionError("Password array cannot be null");
        }
        for(String s : passwords) {
            if(s==null) {
                throw new AssertionError("No index in `passwords` may be null");
            }
        }

        VALID_PASSWORDS = passwords;

        if(constantInvarsBroken()) {
            throw new AssertionError("An invariant of a constant is not true");
        }
    }

    /**
     * Returns true if the invariants of a constant are broken, false otherwise<br><br>
     *
     * Helper to the class constructors
     *
     * @return true if invariants broken, false otherwise
     */
    private boolean constantInvarsBroken() {
        if(!(BLOCK_LENGTH>0 && BLOCK_COUNT>0 &&
             DEFAULT_INPUT_FILE != null && DEFAULT_INPUT_FILE.length()>3 &&
             DEFAULT_INPUT_FILE.endsWith(".txt") &&
             KEY_BLOCK_INCREMENTS.length==BLOCK_COUNT &&
             MAX_THREADS>=1 &&
             VALID_PASSWORDS != null)
         ) {
            return true;
        }

        for(String s : VALID_PASSWORDS) {
            if(s==null) {
                return true;
            }
        }
        for(byte b : KEY_BLOCK_INCREMENTS) {
            if(b < 0) return true;
        }

        return false;
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //GETTERS

    /**
     * Returns index `index` of KEY_BLOCK_INCREMENTS. If `index` is not a valid index, throws an IndexOBException.<br><br>
     *
     * Needed to prevent other methods from modifying a constant array.
     * @param index index of KEY_BLOCK_INCREMENTS to access
     * @return value at given index
     * @throws IndexOutOfBoundsException if `index` is not valid
     */
    public static byte getKeyBlockIncrementIndex(int index) {
        if(index<0 || index>=KEY_BLOCK_INCREMENTS.length) {
            throw new IndexOutOfBoundsException("Index must be on the interval [0," + (KEY_BLOCK_INCREMENTS.length-1) + "]");
        }
        return KEY_BLOCK_INCREMENTS[index];
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //METHODS


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
    public static String arrToString(byte[][] input) {
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
    public static int[] countAlphaAndNumericChars(String input) {
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
    public static byte[][] createKeyBlocks(String input, int blocks, int charsPerBlock) {
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
     * Returns the decrypted version of text using the given key beginning after `startBlock` blocks have been decrypted.<br><br>
     *
     * Algorithm first implemented on February 26-29, 2024. Enhanced encryption finished on July 18, 2024. By Chris P Bacon
     *
     * @param text text to decrypt. Must contain all lowercase English ASCII characters. Can't be null
     * @param key key to decrypt with. Can't be null. All indices must be on [0,25]
     * @param startBlock index to start decrypting from. Must be non-negative
     * @return decrypted version of text
     */
    public static String decrypt(String text, byte[][] key, int startBlock) {
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
        for(int a=0; a<key.length; a++) {
            for(int i=0; i<key[a].length; i++) {
                if(key[a][i]<0 || key[a][i]>25) {
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
        byte[] keyBlockBasePositions= StepperFunctions.initializeKeyBlockPositions(startBlock + text.length()/BLOCK_LENGTH);
//        System.out.println(text);
        String output="";

        int currentChar=0;
        int currentBlock = (startBlock + text.length()/BLOCK_LENGTH);

        byte[] keyBlockReadPositions=new byte[BLOCK_COUNT];
        for(int s=0; s<keyBlockReadPositions.length; s++) {
            keyBlockReadPositions[s]=keyBlockBasePositions[s];
        }

        for(int m=0; m<(text.length() % BLOCK_LENGTH); m++) {
            for(int a=0; a<keyBlockReadPositions.length; a++) {
                keyBlockReadPositions[a]++;
                if(keyBlockReadPositions[a] >= BLOCK_LENGTH) {
                    keyBlockReadPositions[a]=0;
                }
            }
        }

        for(int t=text.length()-1; t>=text.length()-(text.length() % BLOCK_LENGTH); t--) {


            for(int d=0; d<keyBlockReadPositions.length; d++) {
                keyBlockReadPositions[d] -= 1;
                if(keyBlockReadPositions[d] < 0) {
                    keyBlockReadPositions[d] = BLOCK_LENGTH-1;
                }
            }

            currentChar=text.charAt(t) - 97;
            for(int k=keyBlockReadPositions.length-1; k>=0; k--) {
                currentChar = (currentChar - key[k][keyBlockReadPositions[k]]) % 26;
                if(currentChar < 0) {
                    currentChar += 26;
                }
            }

            output=output + (char)(currentChar+97);

        }


        for(int b=text.length()-(text.length() % BLOCK_LENGTH)-1; b>=0; b-=BLOCK_LENGTH) {

            currentBlock--;
            if((currentBlock+1)%BLOCK_LENGTH==0) {
                keyBlockBasePositions = setKeyBlockPositions(currentBlock);
            }


            for(int m=0; m<keyBlockBasePositions.length; m++) {
                keyBlockBasePositions[m] -= KEY_BLOCK_INCREMENTS[m];

                if(keyBlockBasePositions[m]<0) {
                    keyBlockBasePositions[m] += BLOCK_LENGTH;
                }
            }

//            System.out.println(Arrays.toString(keyBlockPositions));

            for(int s=0; s<keyBlockReadPositions.length; s++) {
                keyBlockReadPositions[s]=keyBlockBasePositions[s];
            }

            for(int t=b; t>b-BLOCK_LENGTH; t--) {

                for(int d=0; d<keyBlockReadPositions.length; d++) {
                    keyBlockReadPositions[d]--;
                    if(keyBlockReadPositions[d] < 0) {
                        keyBlockReadPositions[d]=BLOCK_LENGTH-1;
                    }
                }

                currentChar=(int)text.charAt(t) - 97;

                for(int k=keyBlockReadPositions.length-1; k>=0; k--) {

                    currentChar = (currentChar - key[k][keyBlockReadPositions[k]]) % 26;
                    if(currentChar < 0) {
                        currentChar += 26;
                    }

                }

                output=output + (char)(currentChar+97);

            }
        }

        //Reverse the output (the decryption process will make the text turn out backwards)
        String reversedOutput="";
        for(int rev=0; rev<output.length(); rev++) {
            reversedOutput=reversedOutput + output.charAt( output.length()-1-rev );
        }
        output=null;

        return reversedOutput;
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
    public static String decryptNumbers(String input, byte[][] key, int numbersPreviouslyDecrypted) {
        if(input==null || key==null) {
            throw new AssertionError("Input and key cannot be null");
        }
        if(numbersPreviouslyDecrypted < 0) {
            throw new AssertionError("Numbers decrypted cannot be negative");
        }

        String flattenedKey = "";
        for(int a=0; a<key.length; a++) {
            if(key[a]==null) {
                throw new AssertionError("Key indices cannot be null");
            }
            for(int i=0; i<key[a].length; i++) {
                if(key[a][i]<0 || key[a][i]>25) {
                    throw new AssertionError("Key indices must be on the interval [0,25]");
                }
                flattenedKey += (char)key[a][i];
            }
        }

        String output="";

        int currentInputChar;
        int keyIndex = numbersPreviouslyDecrypted % flattenedKey.length();

        for(int i=0; i<input.length(); i++) {
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

                //Move to the next index in the key, or go back to the beginning if it overflowed
                keyIndex++;
                if(keyIndex >= flattenedKey.length()) {
                    keyIndex = 0;
                }
            }

            output += (char)currentInputChar;
        }

        return output;
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
    public static String encrypt(String text, byte[][] key, int startBlocks) {
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
        for(int a=0; a<key.length; a++) {
            for(int i=0; i<key[a].length; i++) {
                if(key[a][i]<0 || key[a][i]>25) {
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

        byte[] keyBlockBasePositions = StepperFunctions.initializeKeyBlockPositions(startBlocks);
        byte[] keyBlockReadPositions = new byte[BLOCK_COUNT];
        for(int s=0; s<keyBlockReadPositions.length; s++) {
            keyBlockReadPositions[s] = keyBlockBasePositions[s];
        }

//        System.out.println(Arrays.toString(keyBlockBasePositions) + " " + text);

        String output="";
        int currentChar=0;
        int blocksEncrypted = startBlocks;

        for(int b=0; b<=text.length()-BLOCK_LENGTH; b+=BLOCK_LENGTH) {

            for(int pos=0; pos<keyBlockReadPositions.length; pos++) {
                keyBlockReadPositions[pos] = keyBlockBasePositions[pos];
            }

            for(int t=b; t<(b+BLOCK_LENGTH); t++) {

                currentChar=(int)text.charAt(t) - 97;

                for(int k=0; k<keyBlockReadPositions.length; k++) {
                    currentChar = (currentChar + key[k][keyBlockReadPositions[k]]) % 26;
                }

                output=output + (char)(currentChar+97);

                for(int a=0; a<keyBlockReadPositions.length; a++) {
                    keyBlockReadPositions[a]++;
                    if(keyBlockReadPositions[a] >= BLOCK_LENGTH) {
                        keyBlockReadPositions[a]=0;
                    }
                }
            }

//            System.out.println(Arrays.toString(keyBlockBasePositions));

            for(int r=0; r<keyBlockBasePositions.length; r++) {
                keyBlockBasePositions[r] = (byte) ((keyBlockBasePositions[r] + getKeyBlockIncrementIndex(r)) % BLOCK_LENGTH);
            }

            if((blocksEncrypted+1) % BLOCK_LENGTH == 0) {
                keyBlockBasePositions = setKeyBlockPositions(blocksEncrypted+2);
            }

            blocksEncrypted++;
        }

        for(int pos=0; pos<keyBlockReadPositions.length; pos++) {
            keyBlockReadPositions[pos] = keyBlockBasePositions[pos];
        }

        for(int t=text.length()-(text.length() % BLOCK_LENGTH); t<text.length(); t++) {

            currentChar=(int)text.charAt(t) - 97;

            for(int k=0; k<keyBlockReadPositions.length; k++) {
                currentChar = (currentChar + key[k][keyBlockReadPositions[k]]) % 26;
            }

            output=output + (char)(currentChar+97);

            for(int a=0; a<keyBlockReadPositions.length; a++) {
                keyBlockReadPositions[a]++;
                if(keyBlockReadPositions[a] >= BLOCK_LENGTH) {
                    keyBlockReadPositions[a]=0;
                }
            }
        }
        return output;
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
    public static String encryptNumbers(String input, byte[][] key, int numbersPreviouslyEncrypted) {
        if(input==null || key==null) {
            throw new AssertionError("Neither input can be null");
        }

        String flattenedKey = "";
        for(int a=0; a<key.length; a++) {
            if(key[a]==null) {
                throw new AssertionError("Key indices cannot be null");
            }
            for(int i=0; i<key[a].length; i++) {
                if(key[a][i]<0 || key[a][i]>25) {
                    throw new AssertionError("Key indices must be on the interval [0,25]");
                }
                flattenedKey += (char)key[a][i];
            }
        }

        String output="";

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

            output += (char)currentChar;
        }

        return output;
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
     * @return char array containing locations of non-alphabetic characters
     */
    public static char[] findNonAlphaPositions(String text) {
        if(text==null) {
            throw new AssertionError("Text cannot be null");
        }

        char[] nonAlphas = new char[text.length()];

        for(int i=0; i<text.length(); i++) {
            if((int)text.charAt(i)<65
                    || ((int)text.charAt(i)>90 && (int)text.charAt(i)<97)
                    || (int)text.charAt(i)>122) {

                nonAlphas[i] = text.charAt(i);
            }
            else {
                nonAlphas[i]=(char)0;
            }
        }
      /*
      for(int i=0; i<nonAlphas.length; i++) {
        System.out.print((int)nonAlphas[i] + " ");
      }
      System.out.println();
      */
        return nonAlphas;
    }


    /**
     * Returns all the text from a file whose name is `filename`.<br><br>
     *
     * The input filename must end with the ".txt" extension.<br>
     *
     * If the input filename does not end in ".txt", the method throws an IllegalArgumentException<br>
     * If the file could not be read, throws a FileNotFoundException.<br>
     *
     * @param filename name of the input file. Can't be null
     * @return contents from the given input filename
     * @throws FileNotFoundException if the file can't be read. Displays a descriptive error message if thrown.
     * @throws IllegalArgumentException if input doesn't end in ".txt". Displays a descriptive error message if thrown.
     */
    public static String getTextFromFile(String filename) throws FileNotFoundException {
        if(filename==null) {
            throw new AssertionError("Filename cannot be null");
        }

        File inputFile;

        //Create file from default or the top text input
        if(filename.length()<=0) {
            inputFile = new File(DEFAULT_INPUT_FILE);
        }
        else {
            inputFile = new File(filename);
        }

        String output = "";

        //Check if the input file ends in .txt
        if(inputFile.getName().length()<=3 || !inputFile.getName().substring((int) (inputFile.getName().length()-4)).equals(".txt")) {
            throw new IllegalArgumentException("The input file must have a .txt extension");
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
                if(filename.length()<=0) {
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
     * Returns an array of bytes representing the key block positions at the end of encryption,
     * if the input was `blocks` blocks long<br><br>
     *
     * `blocks` should equal the number of blocks before the starting position.<br>
     * Example: if `blocks` equals 4, the output would be the block positions just after encrypting 4 blocks.
     *
     * @param blocks number of blocks encrypted so far, non-negative
     * @return key block positions after encrypting `blocks` blocks
     */
    public static byte[] initializeKeyBlockPositions(long blocks) {
        assert blocks >= 0;

        byte[] output = setKeyBlockPositions(blocks);

        //Simulate moving through the remainder of the blocks
        for(int b=0; b<blocks%BLOCK_LENGTH; b++) {
            //Increment each index of the output
            for(int i=0; i<output.length; i++) {
                output[i] = (byte) ((output[i] + getKeyBlockIncrementIndex(i)) % BLOCK_LENGTH);
            }
        }

        return output;
    }


    /**
     * Returns an array of bytes representing the key block positions at the end of encryption,
     * if the input was `blocks` blocks long<br><br>
     *
     * `blocks` should equal the number of blocks before the starting position.<br>
     * Example: if `blocks` equals 4, the output would be the block positions just after encrypting 4 blocks.<br><br>
     *
     * Much slower than initializeKeyBlockPositions. Should be used only in tests.
     *
     * @param blocks number of blocks encrypted so far, non-negative
     * @return key block positions at the end of encryption
     */
    public static byte[] initializeKeyBlockPositions_Testing(int blocks) {
        assert blocks>=0;

        byte[] output = new byte[BLOCK_COUNT];

        //Simulate moving through each block of text
        for(int b=0; b<blocks; b++) {
            //Increment each index of the output
            for(int i=0; i<output.length; i++) {
                output[i] = (byte) ((output[i] + getKeyBlockIncrementIndex(i)) % BLOCK_LENGTH);
            }

            //Step the key blocks if a period ends (passes BLOCK_LENGTH blocks)
            if((b+1) % BLOCK_LENGTH == 0) {
                output = setKeyBlockPositions(b+2);
            }
        }

        return output;
    }


    /**
     * Returns value corresponding to the user's entered password, as a String. Input should never be null.<br><br>
     *
     * Return value is: 0 if entered password equals the last index in passwords, 1 if entered password is in the passwords array
     * but not the last index, -1 if entered password is not in the passwords array<br>
     * In case of duplicate passwords, all duplicates except the one with the lowest index number are ignored.<br>
     * Designed to take a password directly from a JPasswordField, so it uses a char array instead of a String<br><br>
     *
     * This method cannot be static because it uses input from the constructor
     *
     * @param enteredPasswordRaw the user's entered password, non-null
     * @return value corresponding to password correctness
     */
    public byte login(char[] enteredPasswordRaw) {
        if(enteredPasswordRaw==null) {
            throw new AssertionError("Entered password cannot be null");
        }

        //Convert entered password to a string
        String enteredPassword="";
        for(int i=0; i<enteredPasswordRaw.length; i++) {
            enteredPassword += enteredPasswordRaw[i];
        }

        int correctPwIndex = -1;


        //Loop through VALID_PASSWORDS, checking if the entered password equals the current password
        for(int p=0; p<VALID_PASSWORDS.length; p++) {

            //If equality found, record the index and stop
            if(enteredPassword.equals(VALID_PASSWORDS[p])) {
                correctPwIndex = p;
                break;
            }
        }

        //Return value corresponding to the highest possible password index found
        if(correctPwIndex==-1) {
            return -1;
        }
        else if(correctPwIndex != VALID_PASSWORDS.length-1) {
            return 1;
        }
        return 0;
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
    public static String recombineNonAlphas(String text, char[] nonAlphasIn, boolean reinsertingPunctuation) {
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
            nonAlphas[t]=nonAlphasIn[t];
        }

        String output="";
        int textIndex=0;
        int nonAlphasIndex=0;
        int outputLen=text.length();

        if(text.length() > nonAlphas.length) {
            System.err.println("WARNING: does 'symbols' have blank spaces accounted for?");
        }


        //all characters from [0..nonAlphasIndex) in text should be already processed
        //outputLen should equal the input's length, plus the number of symbols added to the output
        while(nonAlphasIndex < outputLen) {

            //If there's a symbol in the current index
            if(nonAlphas[nonAlphasIndex] > 0) {
                //If not an apostrophe (ignore the compiler warning)
                if(!(nonAlphas[nonAlphasIndex]==(char)39 || nonAlphas[nonAlphasIndex]==(char)96 || nonAlphas[nonAlphasIndex]=='’' || nonAlphas[nonAlphasIndex]=='`')) {

                    if( (reinsertingPunctuation) ||
                            (nonAlphas[nonAlphasIndex]>=48 && nonAlphas[nonAlphasIndex]<=57) ) {
                        //add to output
                        output=output + (char)nonAlphas[nonAlphasIndex];
                    }

                    //empty the symbol
                    nonAlphas[nonAlphasIndex]=0;
                }

                outputLen++;
            }

            //If there's no symbol
            else {
                output=output + text.charAt(textIndex);
                textIndex++;
            }

            nonAlphasIndex++;
        }


        //Add the rest of the symbols

        //all characters from [0..nonAlphasIndex) in text should be already processed
        while(nonAlphasIndex < nonAlphas.length) {
            if((nonAlphas[nonAlphasIndex]>0 && reinsertingPunctuation)
                    || (nonAlphas[nonAlphasIndex])>=48 && nonAlphas[nonAlphasIndex]<=57) {
                output=output + nonAlphas[nonAlphasIndex];
            }
            nonAlphasIndex++;
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


    /**
     * Returns a lowercased version of `input` without diacritics or accent marks.<br><br>
     *
     * Any character that is not transformed by the private helper method `removeDiacritics(char)` is not changed.
     *
     * @param input text to remove diacritics from
     * @return copy of input without diacritics in lowercase letters
     */
    public static String removeDiacritics(String input) {
        String output="";
        for(int i=0; i<input.length(); i++) {
            output += removeDiacritics(input.charAt(i));
        }
        return output;
    }


    /**
     * Returns a version of text without non-alphanumeric characters.
     * The text returned is converted to lowercase.<br><br>
     *
     * WARNING! Not to be confused with removeNonAlphas! This method keeps numbers!
     *
     * @param text original input. Can't be null
     * @return lowercased text without non-alphanumeric characters
     */
    public static String removeNonAlnums(String text) {
        if(text==null) throw new AssertionError("Text can't be null");

        text=text.toLowerCase();


        String output="";
        for(int i=0; i<text.length(); i++) {
            if(((int)text.charAt(i)>=97 && (int)text.charAt(i)<=122)
                    || (int)text.charAt(i)>=48 && (int)text.charAt(i)<=57) {
                output=output + text.charAt(i);
            }
        }

        return output;
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
    public static String removeNonAlphas(String text) {
        if(text==null) throw new AssertionError("Text can't be null");

        text=text.toLowerCase();

        String output="";
        for(int i=0; i<text.length(); i++) {
            if((int)text.charAt(i)>=97 && (int)text.charAt(i)<=122) {
                output=output + text.charAt(i);
            }
        }

        return output;
    }


    /**
     * Returns a copy of input, but without spaces.<br><br>
     *
     * -Exception: add a space after every punctuation mark, as defined in the 'punct' array
     * unless the punctuation mark is the last character.<br>
     *
     * -If there are many punctuation marks in a row, add a space only after the last mark.<br>
     *
     * Examples:<br>
     * "" >>> ""<br>
     * "abc abc" >>> "abcabc"<br>
     * "abc 123 abc" >>> "abc123abc"<br>
     * "abc? Abc,abc!" >>> "abc? Abc, abc!"<br>
     * "abc!!!abc?!." >>> "abc!!! abc?!."<br>
     *
     * -DO NOT separate with a space if the punctuation mark is surrounded by digits.<br>
     * Example: "3.14159 pi" >>> "3.14159pi"<br>
     * Example: "100, asdf" >>> "100, asdf"<br>
     * @param input text to remove spaces from
     * @return copy of input without spaces
     */
    public static String removeSpaces(String input) {
        if(input==null) throw new AssertionError("Input can't be null");

        char[] punct = new char[] {'.', ',', '!', '?', '-', ':', ';'};
        String output="";
        boolean currentCharInPunct=false;
        boolean previousCharInPunct=false;


        //If first character is not a space, add it
        if(input.length()>0 && input.charAt(0)!=' ') {
            output=output + input.charAt(0);
        }


        //Do everything after the first character. indices [0..i) are already processed
        for(int i=1; i<input.length(); i++) {

            //Try to find the current input character in punct
            for(int f=0; f<punct.length; f++) {
                if(punct[f]==input.charAt(i)) {
                    currentCharInPunct=true;
                }
                if(punct[f]==input.charAt(i-1)) {
                    previousCharInPunct=true;
                }
            }

            //If a punctuation mark was passed, add a space
            if(i>0 && !currentCharInPunct && previousCharInPunct) {
                //Add only if not inside of a number
                if(i<input.length()-1 && (input.charAt(i+1)<48 || input.charAt(i+1)>57)) {
                    output=output + " ";
                }
            }

            //If current character is not a space, add it
            if(input.charAt(i) != ' ') {
                output=output + input.charAt(i);
            }

            currentCharInPunct=false;
            previousCharInPunct=false;
        }

        return output;
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
    private static int[] setBlocksPerIndex(int totalBlocks, int threads) {
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
     * Returns the key block positions for the given text length.<br><br>
     *
     * Important note: this method uses text length, not the number of blocks that are in the text.<br><br>
     *
     * Helper to initializeKeyBlockPositions.
     *
     * @param textLength length of text (must be at least 0)
     * @return key block positions
     */
    private static byte[] setKeyBlockPositions(long textLength) {
        //Check text length: must be non-negative
        if(textLength < 0) throw new AssertionError("Text length cannot be negative");

        //Set the output array, assign all empty space to 0
        byte[] result = new byte[BLOCK_COUNT];
        for(int s=0; s<result.length; s++) {
            result[s]=0;
        }

        long quotient=textLength;
        double decimalPortion=0;

        //Eliminate block spill-overs.
        quotient = quotient % ((long)Math.pow(BLOCK_LENGTH, BLOCK_COUNT));

        //Divide quotient and take only the portion to the right of the decimal point
        decimalPortion = (double) quotient / BLOCK_LENGTH - quotient / BLOCK_LENGTH;
        //Divide quotient and take only the portion to the left of the decimal point
        quotient = quotient / BLOCK_LENGTH;


        //Much like converting a base-10 number to a base-BLOCK_LENGTH number
        //The lowest value digits end up on the right side.
        for(int i=result.length-1; i>=0; i--) {

            //Divide quotient and take only the portion to the right of the decimal point
            decimalPortion = (double)quotient / BLOCK_LENGTH - quotient / BLOCK_LENGTH;
            //Divide quotient and keep only the portion to the left of the decimal point
            quotient = quotient / (long)BLOCK_LENGTH;


            //Convert the decimal portion to a digit and add to the result
            result[i] = (byte)(Math.round(decimalPortion*BLOCK_LENGTH));

            if(quotient <= 0) {
                break;
            }

        }

        //Reverse the output
        byte[] output = new byte[result.length];
        for(int i=0; i<result.length; i++) {
            output[i] = result[result.length - i - 1];
        }

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
     * @param text the text to split
     * @param threads how many pieces `text` should be split into
     * @param blockLength number (or a divisor) of alphabetic characters per piece
     * @return array of Strings. There are `threads` total Strings evenly split among the output's indices
     */
    public static String[] setWorkerLoads(String text, int threads, int blockLength) {

        if (text == null || threads <= 0 || blockLength<=0) {
            throw new AssertionError("No argument can be null or zero");
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
