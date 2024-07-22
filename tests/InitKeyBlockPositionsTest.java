import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Class to test the initializeKeyBlockPositions method.<br><br>
 *
 * WARNING: In the StepperFunctions class, `BLOCK_COUNT` must equal 7 and `BLOCK_LENGTH` must equal 19.
 * KEY_BLOCK_INCREMENTS must equal {1,2,3,5,7,11,13}
 */
public class InitKeyBlockPositionsTest {

    /**
     * Constant defined in the testing class. Necessary to prevent tests from failing upon changing StepperFunctions.BLOCK_LENGTH
     */
    final private int BLOCK_LENGTH = 19;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //UTILITIES

    /**
     * Compares `expected` and `result`. If `result` is null or does not equal `expected`,
     * throws an AssertionError with an error message that describes the point of failure.<br><br>
     *
     * The arrays are equal if one of the following conditions is true:<br>
     * -both are null<br>
     * -they have the same length and all corresponding indices are equal
     *
     * @param expected the value that the tested function should return. Can't be null
     * @param result the actual result from the tested function
     */
    private void assertArraysEqual(byte[] expected, byte[] result) {

        //Null checks
        if(expected==null && result==null) {
            return;
        }
        else if(expected==null) {
            throw new AssertionError("Test failed- expected is null and result is not null");
        }
        else if(result==null) {
            throw new AssertionError("Test failed- result should not be null");
        }

        //Length check
        if(expected.length != result.length) {
            System.err.println("Expected length: " + expected.length);
            System.err.println("Result length: " + result.length);
            throw new AssertionError("Test failed- lengths are not equal");
        }

        //Index check
        for(int c=0; c<expected.length; c++) {
            if(expected[c] != result[c]) {
                //Print arrays
                System.err.print("Expected: {");
                for(int i=0; i<expected.length-1; i++) {
                    System.err.print(expected[i] + ",");
                }
                System.err.println(expected[expected.length-1] + "}");
                System.err.print("Result:   {");
                for(int i=0; i<result.length-1; i++) {
                    System.err.print(result[i] + ",");
                }
                System.err.println(result[result.length-1] + "}");
                throw new AssertionError("Test failed- corresponding indices are not equal");
            }
        }
    }

    /**
     * Asserts that the test configuration is valid.<br><br>
     *
     * Valid configuration:<br>
     * -In the StepperFunctions class, `BLOCK_COUNT` must equal 7 and `BLOCK_LENGTH` must equal 19<br>
     * -`StepperFunctions.KEY_BLOCK_INCREMENTS` must equal {1,2,3,5,7,11,13}<br>
     * -The constant `BLOCK_LENGTH` defined in this class must equal `StepperFunctions.BLOCK_LENGTH`.
     */
    void assertTestValid() {
        byte[] testingIncrements = {1,2,3,5,7,11,13};

        if(!( StepperFunctions.BLOCK_LENGTH==19
        && BLOCK_LENGTH == StepperFunctions.BLOCK_LENGTH
        && StepperFunctions.BLOCK_COUNT==7
        && testingIncrements.length == StepperFunctions.BLOCK_COUNT) ) {
            throw new AssertionError("Test has invalid configuration");
        }

        for(int i=0; i<testingIncrements.length; i++) {
            if(testingIncrements[i] != StepperFunctions.keyBlockIncrementIndex(i)) {
                throw new AssertionError("Test has invalid configuration");
            }
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TESTS


    @DisplayName("When the text has BLOCK_LENGTH blocks or less, the value in each returned index should equal the " +
            "value of the corresponding shift times the number of blocks")
    @Test
    void testRotations() {
        assertTestValid();

        //Text length is zero
        byte[] expected = new byte[] {0,0,0,0,0,0,0};
        byte[] result = StepperFunctions.initializeKeyBlockPositions_Testing(0);
        assertArraysEqual(expected,result);

        //At the end of the first block
        expected = new byte[] {0,0,0,0,0,0,0};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH);
        assertArraysEqual(expected,result);

        //One rotation
        expected = new byte[] {1, 2, 3, 5, 7, 11, 13};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH+1);
        assertArraysEqual(expected,result);

        //4 rotations
        expected = new byte[] {3, 6, 9, 15, 2, 14, 1};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*4);
        assertArraysEqual(expected,result);

        //Beginning of 16th rotation
        expected = new byte[] {15, 11, 7, 18, 10, 13, 5};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*16 - BLOCK_LENGTH + 1);
        assertArraysEqual(expected,result);

        //End of 16th rotation
        expected = new byte[] {15, 11, 7, 18, 10, 13, 5};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*16);
        assertArraysEqual(expected,result);

        //BLOCK_LENGTH^2 rotations
        expected = new byte[] {18, 17, 16, 14, 12, 8, 6};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH);
        assertArraysEqual(expected,result);
    }


    @DisplayName("After the first period of BLOCK_LENGTH blocks, the lowest index in the output should increase by 1 " +
            "before continuing")
    @Test
    void testFirstIndexStepRotations() {
        assertTestValid();

        //Text length is BLOCK_LENGTH^2 + 1. First step should occur
        byte[] expected = new byte[] {1,0,0,0,0,0,0};
        byte[] result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH + 1);
        assertArraysEqual(expected,result);

        //Text length is BLOCK_LENGTH^2 + BLOCK_LENGTH. First step should occur. Should not rotate
        expected = new byte[] {1,0,0,0,0,0,0};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH + BLOCK_LENGTH);
        assertArraysEqual(expected,result);

        //Text length is BLOCK_LENGTH^2 + BLOCK_LENGTH + 1. First rotation after step should occur
        expected = new byte[] {2, 2, 3, 5, 7, 11, 13};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH + BLOCK_LENGTH + 1);
        assertArraysEqual(expected,result);

        //3 rotations after step
        expected = new byte[] {4, 6, 9, 15, 2, 14, 1};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH + 4*BLOCK_LENGTH);
        assertArraysEqual(expected,result);

        //Beginning of 4th rotation after step
        expected = new byte[] {5, 8, 12, 1, 9, 6, 14};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH + 4*BLOCK_LENGTH + 1);
        assertArraysEqual(expected,result);

        //End of 4th rotation after step
        expected = new byte[] {5, 8, 12, 1, 9, 6, 14};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH + 5*BLOCK_LENGTH);
        assertArraysEqual(expected,result);

        //BLOCK_LENGTH rotations after step
        expected = new byte[] {0, 17, 16, 14, 12, 8, 6};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(2 * BLOCK_LENGTH*BLOCK_LENGTH);
        assertArraysEqual(expected,result);
    }


    @DisplayName("After every period of BLOCK_LENGTH blocks, index 0 in the output should be one higher than " +
            "the index's new value at the previous period")
    @Test
    void testFirstIndexStep() {
        //2 * BLOCK_LENGTH^2 + 1 rotations after the first step. Second step should occur
        byte[] expected = new byte[] {2,0,0,0,0,0,0};
        byte[] result = StepperFunctions.initializeKeyBlockPositions_Testing(2 * BLOCK_LENGTH*BLOCK_LENGTH + 1);
        assertArraysEqual(expected,result);

        //2 * BLOCK_LENGTH^2 + BLOCK_LENGTH rotations after the first step. Second step should occur. Rotation should not occur
        expected = new byte[] {2,0,0,0,0,0,0};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(2 * BLOCK_LENGTH*BLOCK_LENGTH + BLOCK_LENGTH);
        assertArraysEqual(expected,result);

        //2 * BLOCK_LENGTH^2 + BLOCK_LENGTH+1 rotations after the first step. Rotation should occur
        expected = new byte[] {3, 2, 3, 5, 7, 11, 13};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(2 * BLOCK_LENGTH*BLOCK_LENGTH + BLOCK_LENGTH+1);
        assertArraysEqual(expected,result);

        //Before second step
        expected = new byte[] {0, 17, 16, 14, 12, 8, 6};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH * 2);
        assertArraysEqual(expected,result);

        //After second step
        expected = new byte[] {2,0,0,0,0,0,0};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH * 2 + 1);
        assertArraysEqual(expected,result);

        //After third step
        expected = new byte[] {3,0,0,0,0,0,0};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH * 3 + 1);
        assertArraysEqual(expected,result);

        //After BLOCK_LENGTH-1 steps
        expected = new byte[] {18,0,0,0,0,0,0};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH * (BLOCK_LENGTH-1) + 1);
        assertArraysEqual(expected,result);

        //Just before the second index has its first step
        expected = new byte[] {17, 17, 16, 14, 12, 8, 6};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH*BLOCK_LENGTH);
        assertArraysEqual(expected,result);
    }


    @DisplayName("Each time an index reaches BLOCK_LENGTH, the index should reset to 0 and the index to the right should increase")
    @Test
    void testAdditionalIndexSteps() {
        //Second index's first step
        byte[] expected = new byte[] {0,1,0,0,0,0,0};
        byte[] result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH*BLOCK_LENGTH + 1);
        assertArraysEqual(expected,result);

        //After second index's first step
        expected = new byte[] {3, 7, 9, 15, 2, 14, 1};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH*BLOCK_LENGTH + BLOCK_LENGTH*3 + 1);
        assertArraysEqual(expected,result);

        //Second index's second step
        expected = new byte[] {1,1,0,0,0,0,0};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH*BLOCK_LENGTH + BLOCK_LENGTH*BLOCK_LENGTH + 1);
        assertArraysEqual(expected,result);

        //Second index's 4th step
        expected = new byte[] {4,1,0,0,0,0,0};
        result = StepperFunctions.initializeKeyBlockPositions_Testing(BLOCK_LENGTH*BLOCK_LENGTH*BLOCK_LENGTH + 4*BLOCK_LENGTH*BLOCK_LENGTH + 1);
        assertArraysEqual(expected,result);

        //Before third index's first step
        expected = new byte[] {17, 16, 16, 14, 12, 8, 6};
        result = StepperFunctions.initializeKeyBlockPositions_Testing((int)Math.pow(BLOCK_LENGTH,4));
        assertArraysEqual(expected,result);

        //Third index's first step
        expected = new byte[] {0,0,1,0,0,0,0};
        result = StepperFunctions.initializeKeyBlockPositions_Testing((int)Math.pow(BLOCK_LENGTH,4) + 1);
        assertArraysEqual(expected,result);

        //5th index's first step
        expected = new byte[] {0,0,0,0,1,0,0};
        result = StepperFunctions.initializeKeyBlockPositions_Testing((int)Math.pow(BLOCK_LENGTH,6) + 1);
        assertArraysEqual(expected,result);
    }
}
