import org.junit.jupiter.api.*;
import static org.junit.Assert.assertThrows;

/**
 * Class to test the createKeyBlocks method
 */
public class CreateKeyBlocksTest {

    //UTILITY METHODS

    /**
     * Returns true if `array1` and `array2` are both not null (no subarrays can be null), have the same dimensions, and values are
     * equal up to and including the value at array1[arrayIndex][subarrayIndex]. Returns false otherwise<br><br>
     *
     * Example: if array1 is [[1,2,3], [4,5,6]] and array2 is [[1,2,3], null], the method automatically returns false.<br>
     * Example: if array1 is [[1,2,3], [4,5,6]] and array2 is [[1,2,3], [4,5,6,7]], the method automatically returns false.<br>
     *
     * Example: if arrayIndex=2 and subarrayIndex=1 for a 4x3 array:<br>
     * [[*,*,*],<br>
     *  [*,*,*],<br>
     *  [*,*,2],<br>
     *  [0,1,2]]<br>
     * All indices with asterisks should be compared between the two arrays. All others should be ignored.<br>
     *
     * Note: both arrays should be tables. If any array is not a table, returns false.
     *
     * @param array1 first array to compare
     * @param array2 second array to compare
     * @param arrayIndex highest value index in the arrays to compare. Value must be on [0, array1.length)
     * @param subarrayIndex highest value in the subarrays to compare. Value must be on [0, array1[0].length)
     * @return true if array1 and array2 are equal, false otherwise
     */
    private boolean arraysEqual(byte[][] array1, byte[][] array2, int arrayIndex, int subarrayIndex) {

        //Check if null
        if(array1==null || array2==null) {
            return false;
        }

        //Check if any subarrays are null
        for(byte[] a : array1) {
            if(a==null) {
                return false;
            }
        }
        for(byte[] a : array2) {
            if(a==null) {
                return false;
            }
        }

        //Idiot check
        if(arrayIndex>=array1.length || subarrayIndex>=array1[0].length) {
            throw new IllegalArgumentException("READ THE SPEC BRO. HINT: ARRAY INDICES START FROM 0");
        }
        if(arrayIndex<0 || subarrayIndex<0) {
            throw new IllegalArgumentException("READ THE SPEC BRO. ARRAY INDICES CANNOT BE NEGATIVE");
        }

        //Compare the lengths
        if(array1.length != array2.length) {
            return false;
        }
        //Compare lengths of subarrays
        for(byte[] a : array1) {
            if(a.length != array1[0].length) {
                return false;
            }
        }
        for(byte[] a : array2) {
            if(a.length != array1[0].length) {
                return false;
            }
        }


        //Compare indices until the final specified index is reached, or a mismatch is found
        int indicesCompared = 0;

        //Should compare until `indicesToCompare` indices are compared
        int indicesToCompare = (arrayIndex+1)*(subarrayIndex+1);

        for(int i=0; i<array1.length; i++) {
            for(int s=0; s<array1[0].length; s++) {
                //Corresponding indices not equal: return false
                if(array1[i][s] != array2[i][s]) {
                    return false;
                }

                //Increase the number of indices compared. If more than the specified indices are read, exit the loop
                indicesCompared++;
                if(indicesCompared >= indicesToCompare) {
                    break;
                }
            }

            if(indicesCompared >= indicesToCompare) {
                break;
            }
        }

        return true;
    }


    /**
     * Returns true if `array` is not null, all of `array`'s subarrays are not null, and all indices in `array`
     * are on [0,25]. Returns false otherwise
     * @param array array to check
     * @return true if all values on [0,25], false otherwise
     */
    private boolean valuesInRange(byte[][] array) {
        //Check if entire array is null
        if(array==null) {
            return false;
        }

        //Check each subarray
        for(int a=0; a<array.length; a++) {
            //Check if subarray is null
            if(array[a]==null) {
                return false;
            }

            //Check if each index is on [0,25]
            for(int i=0; i<array[a].length; i++) {
                if(array[a][i]<0 || array[a][i]>25) {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Compares the two inputs and checks for equality and postcondition violation.<br><br>
     *
     * If any input's index is not on [0,25], as determined by valuesInRange, prints an error message to System.err
     * and throws an AssertionError.
     * If the inputs are not equal, as determined by arraysEqual, prints the expected and result
     * to System.err in an easily readable format and throws an AssertionError.<br>
     *
     * Note: if arrayIndex=2 and subarrayIndex=1 for a 4x3 array:<br>
     * [[*,*,*],<br>
     *  [*,*,*],<br>
     *  [*,*,2],<br>
     *  [0,1,2]]<br>
     * All indices with asterisks should be compared between the two arrays. All others should be ignored.<br>
     *
     * If arrayIndex is -1, this method skips the index comparison step and only checks if all result indices are on [0,25]
     *
     * @param expected first array to compare
     * @param result second array to compare
     * @param arrayIndex highest value index in the arrays to compare. If -1, only checks if the result's indices are on [0,25]
     * @param subarrayIndex highest value index in the subarrays to compare
     */
    private void printAssert(byte[][] expected, byte[][] result, int arrayIndex, int subarrayIndex) {
        if(!valuesInRange(expected)) {
            //Print error message
            throw new AssertionError("Postcondition violation in expected result");
        }

        if(!valuesInRange(result)) {
            //Print output array
            if(result==null) {
                throw new AssertionError("Test failed- result is null");
            }
            else {
                System.err.println("Result:");
                for (int a = 0; a < result.length; a++) {
                    System.err.print("{");
                    if(result[a]==null) {
                        System.err.println("(null)}");
                    }
                    else {
                        for (int i = 0; i < result[a].length - 1; i++) {
                            System.err.print(result[a][i] + ",");
                        }
                        System.err.println(result[a][result[a].length - 1] + "}");
                    }
                }
                throw new AssertionError("Test failed- subarrays in the result are null, or the result contains values not on [0,25]");
            }
        }

        if(arrayIndex != -1  &&  !arraysEqual(expected, result, arrayIndex, subarrayIndex)) {

            //Print expected array
            System.err.println("Expected:");
            for (int a = 0; a < expected.length; a++) {
                System.err.print("{");
                for (int i = 0; i < expected[a].length - 1; i++) {
                    System.err.print(expected[a][i] + ",");
                }
                System.err.println(expected[a][expected[a].length - 1] + "}");
            }

            System.err.println();

            //Print result array
            System.err.println("Result:");
            if(result==null) {
                System.err.println("(null)");
            }
            else {
                for (int a = 0; a < result.length; a++) {
                    System.err.print("{");
                    for (int i = 0; i < result[a].length - 1; i++) {
                        System.err.print(result[a][i] + ",");
                    }
                    System.err.println(result[a][result[a].length - 1] + "}");
                }
            }

            throw new AssertionError("Test failed- expected and result are different");
        }
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TESTS

    /*
    @DisplayName("Calling createKeyBlocks with a null string input, or a non-positive blocks or charsPerBlock, " +
            "should result in an IllegalArgumentException")
    @Test
    void testAssertions() {
        StepperFunctions fcns = new StepperFunctions();
        assertThrows(IllegalArgumentException.class, () -> fcns.createKeyBlocks(null, 1, 8));
        assertThrows(IllegalArgumentException.class, () -> fcns.createKeyBlocks("abc", 0, 8));
        assertThrows(IllegalArgumentException.class, () -> fcns.createKeyBlocks("abc", -1, 8));
        assertThrows(IllegalArgumentException.class, () -> fcns.createKeyBlocks("abc", -59, 8));
        assertThrows(IllegalArgumentException.class, () -> fcns.createKeyBlocks("abc", 2, 0));
        assertThrows(IllegalArgumentException.class, () -> fcns.createKeyBlocks("abc", 2, -1));
        assertThrows(IllegalArgumentException.class, () -> fcns.createKeyBlocks("abc", 2, -265));
    }
     */

    @DisplayName("Calling createKeyBlocks on a string whose length equals blocks*charsPerBlock should create a byte[][] array " +
            "with all characters' numerical values loaded in sequential order")
    @Test
    void testNormalLoad() {
        StepperFunctions fcns = new StepperFunctions();

        String input = "abcdefgh";
        byte[][] expected = {{0,1,2,3}, {4,5,6,7}};
        byte[][] result = fcns.createKeyBlocks(input, 2, 4);
        printAssert(expected, result, 1, 3);

        input = "xyzxyzxyzxyz";
        expected = new byte[][] {{23,24,25}, {23,24,25}, {23,24,25}, {23,24,25}};
        result = fcns.createKeyBlocks(input, 4,3);
        printAssert(expected, result, 3, 2);

        input = "abababababab";
        expected = new byte[][] {{0,1}, {0,1}, {0,1}, {0,1}, {0,1}, {0,1}};
        result = fcns.createKeyBlocks(input, 6,2);
        printAssert(expected, result, 5,1);
    }

    @DisplayName("Calling createKeyBlocks with one block should create a nested byte[][] array with one index. " +
            "The one index should be a byte[] array containing all numerical values")
    @Test
    void testSingleBlockLoad() {
        StepperFunctions fcns = new StepperFunctions();

        String input = "abcdefgh";
        byte[][] expected = {{0,1,2,3,4,5,6,7}};
        byte[][] result = fcns.createKeyBlocks(input, 1, 8);
        printAssert(expected, result, 0, 7);

        input = "xyzxyzxyzxyz";
        expected = new byte[][] {{23,24,25, 23,24,25, 23,24,25, 23,24,25}};
        result = fcns.createKeyBlocks(input, 1,12);
        printAssert(expected, result, 0, 11);

        input = "x";
        expected = new byte[][] {{23}};
        result = fcns.createKeyBlocks(input, 1,1);
        printAssert(expected, result, 0, 0);
    }

    @DisplayName("Calling createKeyBlocks when the input string's length is more than blocks*charsPerBlock should make a " +
            "byte[][] array that holds only the first blocks*charsPerBlock characters in the input string")
    @Test
    void testOverflow() {
        StepperFunctions fcns = new StepperFunctions();

        //Overflow by one character
        String input = "abcdefg";
        byte[][] expected = {{0,1,2}, {3,4,5}};
        byte[][] result = fcns.createKeyBlocks(input, 2, 3);
        printAssert(expected, result, 1, 2);

        //Overflow by many characters
        input = "bcdefghijk";
        expected = new byte[][] {{1,2,3}, {4,5,6}, {7,8,9}};
        result = fcns.createKeyBlocks(input, 3,3);
        printAssert(expected, result, 2, 2);

        //Overflow with a single subarray
        input = "bcdefghijklmnopq";
        expected = new byte[][] {{1,2,3, 4,5,6, 7,8,9, 10}};
        result = fcns.createKeyBlocks(input, 1,10);
        printAssert(expected, result, 0, 9);
    }

    @DisplayName("Calling createKeyBlocks when the input string's length is less than blocks*charsPerBlock should make a " +
            "byte[][] array with blocks*charsPerBlock characters. Any characters that don't come from the input string " +
            "should be random numbers on [0,25]")
    @Test
    void testUnderflow() {
        StepperFunctions fcns = new StepperFunctions();

        //When extra characters fit evenly inside a new array index. Final 3 indices should be random
        String input = "abcdef";
        byte[][] expected = {{0,1,2}, {3,4,5}, {6,20,25}};
        byte[][] result = fcns.createKeyBlocks(input, 3, 3);
        printAssert(expected, result, 1, 2);

        //First 6 indices should be the same after another call to setWorkerLoads
        result = fcns.createKeyBlocks(input, 3, 3);
        printAssert(expected, result, 1, 2);


        //When extra characters don't fit evenly inside a new array index. Final 2 indices should be random
        input = "bcdefghijk";
        expected = new byte[][] {{1,2,3}, {4,5,6}, {7,8,9}, {10,25,0}};
        result = fcns.createKeyBlocks(input, 4,3);
        printAssert(expected, result, 3, 0);

        //First 10 indices should be the same after another call to setWorkerLoads
        result = fcns.createKeyBlocks(input, 4,3);
        printAssert(expected, result, 3, 0);


        //When the string has one character and output array has more than one index, all other indices except the first are random
        input = "a";
        expected = new byte[][] {{0,2,3,4,5}, {5,2,4,7,20}, {4,18,14,0,25}};
        result = fcns.createKeyBlocks(input, 3,5); //first index should be 0, all others should be random
        printAssert(expected, result, 0, 0); //first index should be 0, all others should be random

        //First index should be the same after another call to setWorkerLoads
        result = fcns.createKeyBlocks(input, 3,5);
        printAssert(expected, result, 0, 0);


        //All indices holding characters from the input should be the same. All randomized indices should be on [0,25] for each run, even on multiple runs
        input = "abcdefg";
        expected = new byte[][] {{0,1,2,3}, {4,5,6,25}, {3,15,19,24}};
        for(int i=1; i<=100; i++) {
            result = fcns.createKeyBlocks(input, 3, 4);
            printAssert(expected, result, 1, 2);
        }

        //All indices should be randomized if the input string is empty
        input = "";
        expected = new byte[][] {{25,1,22,8}, {10,5,11,25}, {3,15,19,24}};
        result = fcns.createKeyBlocks(input, 3, 4);
        printAssert(expected, result, -1, 0);
    }

    @DisplayName("Any characters that don't come from the input string should always be random numbers on [0,25]. " +
            "WARNING: this test takes a long time to run")
    @Test
    void testRandomCharsInRange() {
        StepperFunctions fcns = new StepperFunctions();

        //All randomized indices should be on [0,25] for each run
        String input = "abcdef";
        byte[][] expected = new byte[][] {{0,1,2,3,4,5,10,25}};
        byte[][] result;
        for(int i=1; i<=100000; i++) {
            result = fcns.createKeyBlocks(input, 1, 8);
            printAssert(expected, result, 0, 5);
        }

        //All randomized indices should be on [0,25] for each run, even with multiple array indices
        input = "abcdefg";
        expected = new byte[][] {{0,1,2,3}, {4,5,6,25}, {3,15,19,24}};
        for(int i=1; i<=100000; i++) {
            result = fcns.createKeyBlocks(input, 3, 4);
            printAssert(expected, result, 1, 2);
        }

        //All randomized indices should be on [0,25] for each run, even if all characters are random
        input = "";
        expected = new byte[][] {{0,1,2,3}, {4,5,6,25}, {3,15,19,24}};
        for(int i=1; i<=100000; i++) {
            result = fcns.createKeyBlocks(input, 3, 4);
            printAssert(expected, result, -1, 0);
        }
    }

    @DisplayName("createKeyBlocks should treat uppercase letters in the same way as lowercase letters")
    @Test
    void testUppercase() {
        StepperFunctions fcns = new StepperFunctions();

        //One uppercase letter at the beginning
        String input = "Abcdef";
        byte[][] expected = new byte[][] {{0,1,2,3,4,5}};
        byte[][] result = fcns.createKeyBlocks(input, 1, 6);
        printAssert(expected, result, 0, 5);

        //One uppercase letter at the end
        input = "abcdeF";
        expected = new byte[][] {{0,1,2,3,4,5}};
        result = fcns.createKeyBlocks(input, 1, 6);
        printAssert(expected, result, 0, 5);

        //Many uppercase letters
        input = "abCdEf";
        expected = new byte[][] {{0,1,2,3,4,5}};
        result = fcns.createKeyBlocks(input, 1, 6);
        printAssert(expected, result, 0, 5);

        //When the result needs multiple subarrays
        input = "AbCdwxYzfgHi";
        expected = new byte[][] {{0,1,2,3}, {22,23,24,25}, {5,6,7,8}};
        result = fcns.createKeyBlocks(input, 3, 4);
        printAssert(expected, result, 2, 3);

        //All uppercase letters
        input = "ABCDEFGHI";
        expected = new byte[][] {{0,1,2,3}, {4,5,6,7}, {8,16,25,21}};
        result = fcns.createKeyBlocks(input, 3, 4);
        printAssert(expected, result, 2, 0);
    }

    @DisplayName("createKeyBlocks should ignore any character that is not an English ASCII letter")
    @Test
    void testNonLetters() {
        StepperFunctions fcns = new StepperFunctions();

        //Non-letters at the beginning
        String input = "----abcdef";
        byte[][] expected = new byte[][] {{0,1,2,3,4,5}};
        byte[][] result = fcns.createKeyBlocks(input, 1, 6);
        printAssert(expected, result, 0, 5);

        //Non-letters at the end
        input = "abcdef.[';";
        expected = new byte[][] {{0,1,2}, {3,4,5}};
        result = fcns.createKeyBlocks(input, 2, 3);
        printAssert(expected, result, 1, 2);

        //Non-letters in the middle
        input = "abc    -+-;.\\def";
        expected = new byte[][] {{0,1,2}, {3,4,5}};
        result = fcns.createKeyBlocks(input, 2, 3);
        printAssert(expected, result, 1, 2);

        //Numbers (should ignore numbers)
        input = "0abc123def3";
        expected = new byte[][] {{0,1,2}, {3,4,5}};
        result = fcns.createKeyBlocks(input, 2, 3);
        printAssert(expected, result, 1, 2);

        //Letters that appear like ASCII letters but are not
        input = "аbcdefg"; //The first letter is a Cyrillic 'a'
        expected = new byte[][] {{1,2,3}, {4,5,6}};
        result = fcns.createKeyBlocks(input, 2, 3);
        printAssert(expected, result, 1, 2);
    }

    @DisplayName("createKeyBlocks should treat letters with diacritics as English ASCII letters with diacritics removed")
    @Test
    void testDiacritics() {
        StepperFunctions fcns = new StepperFunctions();

        String input = "açàáâãäå";
        byte[][] expected = new byte[][] {{0,2,0,0}, {0,0,0,0}};
        byte[][] result = fcns.createKeyBlocks(input, 2, 4);
        printAssert(expected, result, 1, 3);

        input = "ýßǹń ñňÿð ë";
        expected = new byte[][] {{24,18,13}, {13,13,13}, {24,3,4}};
        result = fcns.createKeyBlocks(input, 3, 3);
        printAssert(expected, result, 2, 2);
    }
}
