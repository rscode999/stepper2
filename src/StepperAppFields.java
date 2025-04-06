/**
 * Contains constants and fields of a StepperApp not related to the GUI.<br><br>
 *
 * Allows Boss and Worker threads to access required functions and keep non-GUI functions in an easy-to-find place outside the main app.<br>
 * The field/method separation also minimizes possible rep exposure when methods and fields are part of the same class, where these classes
 * are passed into multiple objects that can modify fields.
 */
public class StepperAppFields {

    //CONSTANTS

    /**
     * Number of blocks to use in processes. Must be on the interval [1, 127].<br><br>
     *
     * Highly recommended to be a prime number.
     */
    final public static byte BLOCK_COUNT = 5;

    /**
     * Length of each block to use in processes. Must be on the interval [1, 127].<br><br>
     *
     * Highly recommended to be a prime number, or at least relatively prime with BLOCK_COUNT.
     */
    final public static byte BLOCK_LENGTH = 22;


    /**
     * Name of the default text input file. Must end in ".txt".
     */
    final public static String DEFAULT_INPUT_FILENAME = "input.txt";


    /**
     * Amount to rotate each block. Length must equal BLOCK_COUNT. Must be private. Indices are accessed through a getter method.
     */
    final private static byte[] KEY_BLOCK_INCREMENTS = {2,3,5,7,11};


    /**
     * Maximum number of threads that can be used for a process. Must be at least 1
     */
    final public static int MAX_THREADS = 999;


    /**
     * Standard title for any message dialog popup boxes. Most message dialogs should use this as a title.
     * Cannot be null or the empty string
     */
    final public static String MESSAGE_DIALOG_TITLE = "Error";


    /**
     * When given to a ParsingBoss as a filename, this load signal tells the Boss to take its input from a text field
     * instead of from a file.<br><br>
     * Cannot be the empty string because empty strings trigger loading from the default input file.
     */
    final public static String TEXT_LOAD_SIGNAL = "~~~TEXT";


    /**
     * Array of valid passwords, used in the login method. Can't be null. None of its indices can be null
     */
    final private static String[] VALID_PASSWORDS = new String[]{"11111111", "test", "12345", "111111", "123123", "42069",
            "123456", "1234567", "12345678", "123456789", "11111", "111111111", "null",
            "pasword", "asdfghjkl", "qwerty", "qwertyuiop", "1234567890", "123467890", "abc123",
            "123567890", "314159265", "69420", "test1", "iloveyou",
            "asdfghjkl", "Password1", "12345", "123456", "1234567890", "123467890", "123567890",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "abcd1234", "987654321",
            "password", "", "asdf", "letmein", "0"};


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //VARIABLES


    /**
     * Holds the result from the login function
     */
    private byte loginCredentials;

    /**
     * Holds the number of threads to do operations with. Must be on the interval [0, MAX_THREAD_COUNT].<br><br>
     *
     * Warning: If threadCount is 0, the parent StepperApp will enter undefined behavior and not process its input.
     */
    private int threadCount;




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //CONSTRUCTOR



    /**
     * Creates a new instance of StepperAppFields
     */
    public StepperAppFields() {
        loginCredentials = 127;
        threadCount = 1;

        assertConstantInvars();
        assertVariableInvars();
    }


    /**
     * Throws an AssertionError with a detailed message if an invariant of a constant is broken<br><br>
     *
     * Helper to the class constructor
     */
    private void assertConstantInvars() {
        if(BLOCK_COUNT<=0 || BLOCK_COUNT>127)
            throw new AssertionError("Block count must be on the interval [1, 127]");

        if(BLOCK_LENGTH<=0 || BLOCK_LENGTH>127)
            throw new AssertionError("Block length must be on the interval [1, 127]");


        if(DEFAULT_INPUT_FILENAME ==null
        || DEFAULT_INPUT_FILENAME.length()<4
        || DEFAULT_INPUT_FILENAME.endsWith(".txt")==false)
            throw new AssertionError("Default input filename must end in \".txt\"");

        if(KEY_BLOCK_INCREMENTS==null
        || KEY_BLOCK_INCREMENTS.length != BLOCK_COUNT)
            throw new AssertionError("The key block increment array's length (" + KEY_BLOCK_INCREMENTS.length +
                    ") must equal the block count (" + BLOCK_COUNT + ")");
        for(byte b : KEY_BLOCK_INCREMENTS) {
            if(b < 0) throw new AssertionError("All indices of the key block increment array must be positive");
        }

        if(MAX_THREADS <= 0) throw new AssertionError("Max thread count must be positive");

        if(MESSAGE_DIALOG_TITLE==null || MESSAGE_DIALOG_TITLE.equals(""))
            throw new AssertionError("Message dialog title cannot be null or the empty string");

        if(TEXT_LOAD_SIGNAL==null
        || TEXT_LOAD_SIGNAL.equals("")) throw new AssertionError("Text load signal cannot be null or the empty string");

        if(VALID_PASSWORDS==null) throw new AssertionError("Valid password array cannot be null");
        for(String s : VALID_PASSWORDS) {
            if(s==null) throw new AssertionError("No index in the valid password array can be null");
        }
    }

    /**
     * Throws an AssertionError with a detailed message if an invariant of a variable is broken<br><br>
     *
     * Helper to the class constructor
     */
    private void assertVariableInvars() {
        if(MAX_THREADS <= 0) throw new AssertionError("Max thread count must be positive");
        if(threadCount<0 || threadCount>MAX_THREADS) throw new AssertionError("Thread count must be on the interval [0, " + MAX_THREADS + "]");
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //GETTERS, SETTERS, UTILITIES



    /**
     * Returns index `index` of KEY_BLOCK_INCREMENTS. If `index` is not a valid index, throws an ArrayIndexOBException.<br><br>
     *
     * Needed to prevent other methods from modifying a constant array.
     * @param index index of KEY_BLOCK_INCREMENTS to access
     * @return value at given index
     * @throws ArrayIndexOutOfBoundsException if `index` is not on the interval [0, KEY_BLOCK_INCREMENTS.length)
     */
    public static byte getKeyBlockIncrementIndex(int index) {
        if(index<0 || index>=KEY_BLOCK_INCREMENTS.length) {
            throw new ArrayIndexOutOfBoundsException("The desired index (" + index + ") must be on the interval [0," + (KEY_BLOCK_INCREMENTS.length-1) + "]");
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
     * Sets the object's login credentials to `newValue`.
     * @param newValue the new login credentials
     */
    public void setLoginCredentials(byte newValue) {
        loginCredentials = newValue;
    }


    /**
     * Returns the thread count
     * @return thread count
     */
    public int threadCount() {
        return threadCount;
    }

    /**
     * Sets the object's threadCount to `newThreadCount`.
     * If the input is not on the interval [0, StepperAppFields.MAX_THREADS], throws a NumberFormatException.
     *
     * @param newThreadCount new number of threads. Must be on the interval [0, StepperAppFields.MAX_THREADS]
     * @throws NumberFormatException if `newThreadCount` is out of the valid range
     */
    public void setThreadCount(int newThreadCount) {
        if(newThreadCount<0 || newThreadCount>MAX_THREADS) {
            throw new NumberFormatException("Value must be an integer on [0, " + MAX_THREADS + "]");
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
     * @param enteredPasswordRaw the user's entered password, non-null
     * @return value corresponding to password correctness
     */
    public static byte login(char[] enteredPasswordRaw) {
        if(enteredPasswordRaw==null) {
            throw new AssertionError("Entered password cannot be null");
        }

        //Convert entered password to a string
        StringBuilder enteredPassword= new StringBuilder();
        for (char c : enteredPasswordRaw) {
            enteredPassword.append(c);
        }

        int correctPwIndex = -1;


        //Loop through VALID_PASSWORDS, checking if the entered password equals the current password
        for(int p=0; p<VALID_PASSWORDS.length; p++) {

            //If equality found, record the index and stop
            if(enteredPassword.toString().equals(VALID_PASSWORDS[p])) {
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
