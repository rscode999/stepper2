import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Class to test the setKeyBlockPositions method. Uncomment the "setKeyBlockPositions_Testing" method in StepperFunctions to run the tests.
 */
public class SetKeyBlockPositionsTest {

    StepperFunctions fcns = new StepperFunctions();
    final private int NUMBER_OF_BLOCKS = fcns.BLOCK_COUNT;
    final private int BLOCK_LENGTH = fcns.BLOCK_LENGTH;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TESTS

    @DisplayName("When the text length is less than the block length, setKeyBlockPositions should return a byte array of all 0's")
    @Test
    void testSubNumberOfBlocks() {

        //Start at index 0
        byte[] expected = setKeyBlockPositions_Reference(0);
        byte[] result = fcns.setKeyBlockPositions_Testing(0);
        printAssert(expected, result);

        //Start at index 1
        expected = setKeyBlockPositions_Reference(1);
        result = fcns.setKeyBlockPositions_Testing(1);
        printAssert(expected, result);

        //Start at index BLOCK_LENGTH-1
        expected = setKeyBlockPositions_Reference(BLOCK_LENGTH-1);
        result = fcns.setKeyBlockPositions_Testing(BLOCK_LENGTH-1);
        printAssert(expected, result);
    }

    @DisplayName("When the text length is between the block length and the block length squared, only the first index should not be zero")
    @Test
    void testFirstIndexNoChange() {
        //Start at index BLOCK_LENGTH
        byte[] expected = setKeyBlockPositions_Reference(BLOCK_LENGTH);
        byte[] result = fcns.setKeyBlockPositions_Testing(BLOCK_LENGTH);
        printAssert(expected, result);

        //Start at index 2*BLOCK_LENGTH-1
        expected = setKeyBlockPositions_Reference(2*BLOCK_LENGTH-1);
        result = fcns.setKeyBlockPositions_Testing(2*BLOCK_LENGTH-1);
        printAssert(expected, result);

        //Start at index 2*BLOCK_LENGTH
        expected = setKeyBlockPositions_Reference(2*BLOCK_LENGTH);
        result = fcns.setKeyBlockPositions_Testing(2*BLOCK_LENGTH);
        printAssert(expected, result);

        //Start at index 2*BLOCK_LENGTH+1
        expected = setKeyBlockPositions_Reference(2*BLOCK_LENGTH+1);
        result = fcns.setKeyBlockPositions_Testing(2*BLOCK_LENGTH+1);
        printAssert(expected, result);

        //Start at index BLOCK_LENGTH squared, minus 1
        expected = setKeyBlockPositions_Reference(BLOCK_LENGTH*BLOCK_LENGTH-1);
        result = fcns.setKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH-1);
        printAssert(expected, result);
    }

    @DisplayName("When the text length is at least the block length squared, the first index should set to 0 " +
            "and the second index should increment. This process should occur each time the first index overflows")
    @Test
    void testFirstIndexChange() {
        //Start at index BLOCK_LENGTH squared
        byte[] expected = setKeyBlockPositions_Reference(BLOCK_LENGTH*BLOCK_LENGTH);
        byte[] result = fcns.setKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH);
        printAssert(expected, result);

        //Start at index BLOCK_LENGTH squared, plus one
        expected = setKeyBlockPositions_Reference(BLOCK_LENGTH*BLOCK_LENGTH+1);
        result = fcns.setKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH+1);
        printAssert(expected, result);

        //Start at index BLOCK_LENGTH squared, plus BLOCK_LENGTH
        expected = setKeyBlockPositions_Reference(BLOCK_LENGTH*BLOCK_LENGTH+BLOCK_LENGTH);
        result = fcns.setKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH+BLOCK_LENGTH);
        printAssert(expected, result);

        //Start at index BLOCK_LENGTH squared, plus 3*BLOCK_LENGTH
        expected = setKeyBlockPositions_Reference(BLOCK_LENGTH*BLOCK_LENGTH+3*BLOCK_LENGTH);
        result = fcns.setKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH+3*BLOCK_LENGTH);
        printAssert(expected, result);

//        byte[] expected;
//        byte[] result;

        //Start at index BLOCK_LENGTH cubed, minus 1
        expected = setKeyBlockPositions_Reference(BLOCK_LENGTH*BLOCK_LENGTH*BLOCK_LENGTH-1);
        result = fcns.setKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH*BLOCK_LENGTH-1);
        printAssert(expected, result);
    }

    @DisplayName("When the text length reaches the block length to n, an integer greater than 1, index n-1 should set to 0 " +
            "and index n should increment")
    @Test
    void testOtherIndexChanges() {
        //Start at index BLOCK_LENGTH cubed
        byte[] expected = setKeyBlockPositions_Reference(BLOCK_LENGTH*BLOCK_LENGTH*BLOCK_LENGTH);
        byte[] result = fcns.setKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH*BLOCK_LENGTH);
        printAssert(expected, result);

        //Start at BLOCK_LENGTH cubed, plus BLOCK_LENGTH-1
        expected = setKeyBlockPositions_Reference((int)Math.pow(BLOCK_LENGTH,3)+BLOCK_LENGTH-1);
        result = fcns.setKeyBlockPositions_Testing((int)Math.pow(BLOCK_LENGTH,3)+BLOCK_LENGTH-1);
        printAssert(expected, result);

        //Start at BLOCK_LENGTH cubed, plus BLOCK_LENGTH
        expected = setKeyBlockPositions_Reference((int)Math.pow(BLOCK_LENGTH,3)+BLOCK_LENGTH);
        result = fcns.setKeyBlockPositions_Testing((int)Math.pow(BLOCK_LENGTH,3)+BLOCK_LENGTH);
        printAssert(expected, result);

        //Start at index BLOCK_LENGTH^4-1
        expected = setKeyBlockPositions_Reference((int)Math.pow(BLOCK_LENGTH,4)-1);
        result = fcns.setKeyBlockPositions_Testing((int)Math.pow(BLOCK_LENGTH,4)-1);
        printAssert(expected, result);

        //Start at index BLOCK_LENGTH^4. If NUMBER_OF_BLOCKS is 4 or less, remove this test
        expected = setKeyBlockPositions_Reference((int)Math.pow(BLOCK_LENGTH,4));
        result = fcns.setKeyBlockPositions_Testing((int)Math.pow(BLOCK_LENGTH,4));
        printAssert(expected, result);

//        //Start at index BLOCK_LENGTH^7-1. If NUMBER_OF_BLOCKS is 7 or less, remove this test
//        expected = setKeyBlockPositions_Reference((int)Math.pow(BLOCK_LENGTH,7)-1);
//        result = fcns.setKeyBlockPositions_Testing((int)Math.pow(BLOCK_LENGTH,7)-1);
//        printAssert(expected, result);
//
//        //Start at index BLOCK_LENGTH^7+1. If NUMBER_OF_BLOCKS is 7 or less, remove this test
//        expected = setKeyBlockPositions_Reference((int)Math.pow(BLOCK_LENGTH,7)+1);
//        result = fcns.setKeyBlockPositions_Testing((int)Math.pow(BLOCK_LENGTH,7)+1);
//        printAssert(expected, result);
    }

    @DisplayName("When the text length is greater than BLOCK_LENGTH to the power of NUMBER_OF_BLOCKS, setKeyBlockPositions " +
            "should ignore the first BLOCK_LENGTH^NUMBER_OF_BLOCKS characters")
    @Test
    void testOverflow() {
        byte[] expected = new byte[NUMBER_OF_BLOCKS];
        byte[] result = fcns.setKeyBlockPositions_Testing((long)Math.pow(BLOCK_LENGTH, NUMBER_OF_BLOCKS));
        printAssert(expected, result);

        /*
        //Result overflows the 64-bit integer limit
        expected = fcns.setKeyBlockPositions_Testing(BLOCK_LENGTH);
        result = fcns.setKeyBlockPositions_Testing((long)Math.pow(BLOCK_LENGTH, NUMBER_OF_BLOCKS) + BLOCK_LENGTH);
        printAssert(expected, result);
         */
    }





    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //UTILITY METHODS

    /**
     * Returns the key block positions for a given text length.<br><br>
     *
     * Much slower implementation than setKeyBlockPositions.
     * Designed for testing the setKeyBlockPositions function.
     *
     * @param textLength text length to test
     * @return same value as setKeyBlockPositions, if the test passes
     */
    private byte[] setKeyBlockPositions_Reference(int textLength) {
        assert textLength >= 0;
        byte[] keyBlockPositions=new byte[NUMBER_OF_BLOCKS];
        for(int i=BLOCK_LENGTH; i<=textLength; i+=BLOCK_LENGTH) {
            //Rotate the leftmost key block forward
            keyBlockPositions[0]++;
            //Check for any key blocks, except for the last one, that overflowed
            for(int m=0; m<keyBlockPositions.length-1; m++) {
                //If overflow found
                if(keyBlockPositions[m] >= BLOCK_LENGTH) {
                    //Reset all key blocks up to the overflowed block
                    for(int r=0; r<=m; r++) {
                        keyBlockPositions[r]=0;
                    }
                    //Increment the key block after it
                    keyBlockPositions[m+1]++;
                }
            }
            //Check if the last key block overflowed. If so, reset all key blocks
            if(keyBlockPositions[keyBlockPositions.length-1] >= BLOCK_LENGTH) {
                for(int r=0; r<keyBlockPositions.length; r++) {
                    keyBlockPositions[r]=0;
                }
            }
        }
        return keyBlockPositions;
    }

    /**
     * Returns true if `result` is not null, the lengths of `expected` and `result` are equal,
     * and all corresponding indices in `expected` and `result` are equal. Returns false otherwise
     *
     * @param expected expected value. Can't be null
     * @param result value that comes from the tested function output
     * @return true if `expected` and `result` are equal, false otherwise
     */
    private boolean arraysEqual(byte[] expected, byte[] result) {
        if(expected==null) {
            throw new IllegalArgumentException("Expected can't be null");
        }

        if(result==null) {
            return false;
        }

        if(expected.length != result.length) {
            return false;
        }

        for(int i=0; i<expected.length; i++) {
            if(expected[i] != result[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares `expected` and `result`. If the two are not equal, as determined by arraysEqual, prints the expected and result
     * to System.err and throws an AssertionError.
     * @param expected expected value, can't be null
     * @param result value that comes from the tested function output
     */
    private void printAssert(byte[] expected, byte[] result) {
        if(!arraysEqual(expected, result)) {
            //Print expected
            System.err.print("Expected: {");
            for(int i=0; i<expected.length-1; i++) {
                System.err.print(expected[i] + ",");
            }
            System.err.println(expected[expected.length-1] + "}");

            //Print result
            System.err.print("Result:   {");
            for(int i=0; i<result.length-1; i++) {
                System.err.print(result[i] + ",");
            }
            System.err.println(result[result.length-1] + "}");

            throw new AssertionError("Test failed");
        }
        else if(false) {
            //Print expected
            System.out.print("Expected: {");
            for(int i=0; i<expected.length-1; i++) {
                System.out.print(expected[i] + ",");
            }
            System.out.println(expected[expected.length-1] + "}");

            //Print result
            System.out.print("Result:   {");
            for(int i=0; i<result.length-1; i++) {
                System.out.print(result[i] + ",");
            }
            System.out.println(result[result.length-1] + "}");
            System.out.println();
        }
    }


}
