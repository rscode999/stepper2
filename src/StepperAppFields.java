/**
 * Contains variable fields of a StepperApp not related to the GUI. Constants are stored in a StepperFunctions object.<br><br>
 *
 * Allows Boss and Worker threads to access required functions and keep non-GUI functions in an easy-to-find place outside the main app.<br>
 * The field/method separation also minimizes possible rep exposure when methods and fields are part of the same class, where these classes
 * are passed into multiple objects that can modify fields.
 */
public class StepperAppFields {


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
     * Placed into an App's key field (through app.fields().setKey(...)), followed by an error message,
     * by a ParsingBoss if the Boss encounters an error caused by user input.<br><br>
     *
     * If detected in an App's key field, the parent ParsingDispatcher should stop its processing, return to the input screen, and
     * display an error message.
     */
    final public static String INPUT_ERROR_SIGNAL = "*~~*";

    /**
     * When given to a ParsingBoss as a filename, this load signal tells the Boss to take its input from a text field
     * instead of from a file.<br><br>
     * Cannot be the empty string because empty strings trigger loading from the default input file.
     */
    final public static String TEXT_LOAD_SIGNAL = "~~~";

    /**
     * Array of valid passwords, used in the login method. Can't be null. None of its indices can be null<br><br>
     *
     * Must be separate from the login method to facilitate testing.<br><br>
     */
    final private String[] VALID_PASSWORDS;


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Holds the result from the login function
     */
    private byte loginCredentials;

    /**
     * Holds the input text. Can't be null
     */
    private String text;

    /**
     * Holds the key. Can't be null
     */
    private String key;

    /**
     * Holds the number of threads to do operations with. Must be on the interval [0, 999].<br><br>
     *
     * Warning: If threadCount is 0, the parent StepperApp will enter undefined behavior and not process its input.
     */
    private int threadCount;

    /**
     * Creates a new instance of StepperAppFields
     */
    public StepperAppFields() {
        loginCredentials = 127;
        text="";
        key="";
        threadCount = 1;

        VALID_PASSWORDS = new String[]{"11111111", "test", "12345", "111111", "123123", "42069",
                "123456", "1234567", "12345678", "123456789", "11111", "111111111", "null",
                "pasword", "asdfghjkl", "qwerty", "qwertyuiop", "1234567890", "123467890", "abc123",
                "123567890", "314159265", "69420", "test1", "iloveyou",
                "asdfghjkl", "Password1", "12345", "123456", "1234567890", "123467890", "123567890",
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "abcd1234", "987654321",
                "asdf", "password", "", "0", "letmein"};
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
                true)
        ) {
            return true;
        }

//        for(String s : VALID_PASSWORDS) {
//            if(s==null) {
//                return true;
//            }
//        }
        for(byte b : KEY_BLOCK_INCREMENTS) {
            if(b < 0) return true;
        }

        return false;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //GETTERS, SETTERS, UTILITIES

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


    /**
     * Returns the login credentials
     * @return login credentials
     */
    public byte loginCredentials() {
        return loginCredentials;
    }


    /**
     * Sets login credentials to `newValue`.
     * @param newValue the new login credentials
     */
    public void setLoginCredentials(byte newValue) {
        loginCredentials = newValue;
    }


    /**
     * Returns the key
     * @return current contents of the key field
     */
    public String key() {
        return key;
    }


    /**
     * Sets text to `newKey`. newKey can't be null
     * @param newKey the new text
     */
    public void setKey(String newKey) {
        assert newKey != null;
        key = newKey;
    }


    /**
     * Returns the text
     * @return current contents of the text field
     */
    public String text() {
        return text;
    }


    /**
     * Sets text to `newText`. newText can't be null
     * @param newText the new text
     */
    public void setText(String newText) {
        assert newText != null;
        text = newText;
    }


    /**
     * Returns the thread count
     *
     * @return thread count
     */
    public int threadCount() {
        return threadCount;
    }


    /**
     * Sets threadCount to `newThreadCount`.<br><br>
     *
     * newThreadCount must be on the interval [0,MAX_THREADS]. If newThreadCount is not on the interval, throws IllegalArgumentException.<br>
     * Note: If `newThreadCount` is 0, the app cannot process its input string.
     *
     * @param newThreadCount new number of threads
     * @throws IllegalArgumentException if newThreadCount is not on the interval [0,MAX_THREADS]
     */
    public void setThreadCount(int newThreadCount) {
        if(newThreadCount<0 || newThreadCount>MAX_THREADS) {
            throw new IllegalArgumentException("Input out of range");
        }

        threadCount = newThreadCount;
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
}
