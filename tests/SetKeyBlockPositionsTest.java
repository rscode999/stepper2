import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;


/**
 * Class to test the method `setKeyBlockPositions` in a `ParsingOperationsWorker`.
 */
public class SetKeyBlockPositionsTest {

    //UTILITIES

    /**
     * Equal to `StepperAppFields.BLOCK_LENGTH`. Shortened for extra readability. (Static imports didn't work)
     */
    long BLOCK_LEN = StepperAppFields.BLOCK_LENGTH;


    /**
     * Returns the key block positions for the given text length for unenhanced (v1) operations.<br><br>
     *
     * This is a reference implementation that is much slower than the actual method.
     * Should be used for testing only.
     *
     * @param textLength the text length to test
     * @return key block positions for the given text length
     */
    byte[] setKeyBlockPositions_Reference(long textLength) {
        if(textLength < 0) throw new AssertionError("Text length cannot be negative");

        //Set the output array, assign all empty space to 0
        byte[] result = new byte[StepperAppFields.BLOCK_COUNT];

        //Move through the characters. Rotate at the correct time
        for(int i=1; i<=textLength; i++) {
            if(i % StepperAppFields.BLOCK_LENGTH == 0) {
                result[0]++;
                for (int m = 0; m < result.length - 1; m++) {
                    if (result[m] >= StepperAppFields.BLOCK_LENGTH) {
                        for (int r = 0; r <= m; r++) {
                            result[r] = 0;
                        }
                        result[m + 1]++;
                    }
                }
                if (result[result.length - 1] >= StepperAppFields.BLOCK_LENGTH) {
                    Arrays.fill(result, (byte) 0);
                }
            }
        }
        return result;
    }

    // ////////////////////////////////////////////

    /**
     * Returns true if the two arrays are both not null, have the same length,
     * and all corresponding elements are equal.
     * Returns false otherwise.
     *
     * @param arr1 the first array to compare
     * @param arr2 the first array to compare
     * @return whether the arrays are equal
     */
     boolean arraysEqual(byte[] arr1, byte[] arr2) {

        if(arr1==null || arr2==null || arr1.length!=arr2.length) {
            return false;
        }

        for(int i=0; i<arr1.length; i++) {
            if(arr1[i] != arr2[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if `expected` and `result` are equal according to `arraysEqual`. If not, the method prints the
     * expected and result, then throws an AssertionError.
     *
     * @param expected expected output of the test
     * @param result actual output of the test
     */
    void printAssert(byte[] expected, byte[] result) {
        //Check if the tests will be valid
        if(BLOCK_LEN<5 || BLOCK_LEN>127) {
            throw new AssertionError("WARNING- block length is not on the interval [5, 127]. Tests are invalid");
        }

        //Do the comparison, abort upon failure
         if(!arraysEqual(expected, result)) {
             System.err.println("Expected: " + Arrays.toString(expected));
             System.err.println("Result:   " + Arrays.toString(result));
             throw new AssertionError("Test failed- expected and result not equal");
         }
    }



    // //////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////////////////////////////////////////////////////////////
    // //////////////////////////////////////////////////////////////////////////////////////////
    //TESTS


    @DisplayName("The output should be all zeros when the given text length is less than BLOCK_LENGTH")
    @Test
    void testNoRotation() {
        ParsingOperationsWorker w = new ParsingOperationsWorker();
        long input;
        byte[] expected;
        byte[] result;

        input = 0;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = 3;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = BLOCK_LEN - 1;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);
    }


    @DisplayName("The output should be all zeros, except for the first index, which should be 1, " +
            "when the length is between BLOCK_LENGTH and 2*BLOCK_LENGTH-1")
    @Test
    void testSingleRotation() {
        ParsingOperationsWorker w = new ParsingOperationsWorker();
        long input;
        byte[] expected;
        byte[] result;

        input = BLOCK_LEN;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = BLOCK_LEN+1;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = BLOCK_LEN*2-1;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);
    }


    @DisplayName("The output should be all zeros, except for the first index, when the length is between " +
            "BLOCK_LENGTH and BLOCK_LENGTH^2")
    @Test
    void testFirstIndexRotation() {
        ParsingOperationsWorker w = new ParsingOperationsWorker();
        long input;
        byte[] expected;
        byte[] result;

        input = BLOCK_LEN*2;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = BLOCK_LEN*3 - 1;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = BLOCK_LEN*3;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = BLOCK_LEN*5 - 1;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = BLOCK_LEN*5;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = BLOCK_LEN * BLOCK_LEN - 1;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);
    }


    @DisplayName("The second index should not be zero if the given length is at least BLOCK_LENGTH squared")
    @Test
    void testSecondIndexRotation() {
        ParsingOperationsWorker w = new ParsingOperationsWorker();
        long input;
        byte[] expected;
        byte[] result;

        input = BLOCK_LEN * BLOCK_LEN;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = (BLOCK_LEN * BLOCK_LEN) + BLOCK_LEN-1;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = (BLOCK_LEN * BLOCK_LEN) + BLOCK_LEN;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);
    }


    @DisplayName("Other indices should not be zero if the given length is at least BLOCK_LENGTH squared")
    @Test
    void testOtherIndexRotations() {
        ParsingOperationsWorker w = new ParsingOperationsWorker();
        long input;
        byte[] expected;
        byte[] result;

        input = (BLOCK_LEN * BLOCK_LEN  * BLOCK_LEN);
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = (BLOCK_LEN * BLOCK_LEN  * BLOCK_LEN) + BLOCK_LEN*2 - 1;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = (BLOCK_LEN * BLOCK_LEN  * BLOCK_LEN) + BLOCK_LEN*2;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = (long)Math.pow(BLOCK_LEN,5) + BLOCK_LEN*5 - 1;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        input = (long)Math.pow(BLOCK_LEN,5) + BLOCK_LEN*5;
        expected = setKeyBlockPositions_Reference(input);
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);
    }


    @DisplayName("The output should reset to all zeros if overflowed")
    @Test
    void testOverflow() {
        ParsingOperationsWorker w = new ParsingOperationsWorker();
        long input;
        byte[] expected;
        byte[] result;

        //Barely overflows, should be all zeros
        input = (long)Math.pow(BLOCK_LEN, StepperAppFields.BLOCK_COUNT);
        expected = setKeyBlockPositions_Reference(input - (long)Math.pow(BLOCK_LEN, StepperAppFields.BLOCK_COUNT));
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        //Overflow + 1, should be all zeros
        input = (long)Math.pow(BLOCK_LEN, StepperAppFields.BLOCK_COUNT) + 1;
        expected = setKeyBlockPositions_Reference(input - (long)Math.pow(BLOCK_LEN, StepperAppFields.BLOCK_COUNT));
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);

        //Overflow with some rotation, equivalent to a length of (BLOCK_LEN * BLOCK_LEN) + BLOCK_LEN*2 - 1
        input = (long)Math.pow(BLOCK_LEN, StepperAppFields.BLOCK_COUNT) + (BLOCK_LEN * BLOCK_LEN) + BLOCK_LEN*2 - 1;
        expected = setKeyBlockPositions_Reference(input - (long)Math.pow(BLOCK_LEN, StepperAppFields.BLOCK_COUNT));
        result = w.setKeyBlockPositions_Testing(input);
        printAssert(expected, result);
    }
}
