/**
 * Contains variable fields of a StepperApp not related to the GUI. Constants are stored in a StepperFunctions object.<br><br>
 *
 * Allows Boss and Worker threads to access required functions and keep non-GUI functions in an easy-to-find place outside the main app.<br>
 * The field/method separation also minimizes possible rep exposure when methods and fields are part of the same class, where these classes
 * are passed into multiple objects that can modify fields.
 */
public class StepperAppFields {

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
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //GETTERS, SETTERS, UTILITIES

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
     * Returns the thread count minus 1. If the count is 1 or less, returns the unmodified thread count<br><br>
     * Necessary because the StringParserBoss is a worker thread and must be treated as such
     *
     * @return adjusted thread count
     */
    public int threadCount() {
        if(threadCount <= 1) {
            return threadCount;
        }

        return threadCount-1;
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
        if(newThreadCount<0 || newThreadCount>StepperFunctions.MAX_THREADS) {
            throw new IllegalArgumentException("Input out of range");
        }

        threadCount = newThreadCount;
    }

}
