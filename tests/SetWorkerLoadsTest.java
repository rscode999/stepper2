import org.junit.jupiter.api.*;
import java.util.Arrays;
import static org.junit.Assert.assertThrows;

/**
 * Class to test the setWorkerLoads method in the StepperFunctions class
 */
public class SetWorkerLoadsTest {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //UTILITY METHODS

    /**
     * Returns true if `a1` has the same length as `a2` and all corresponding elements of the two arrays are equal.
     * i.e. a1[0].equals(a2[0]), a1[1].equals(a2[1]) ... a1[l].equals(a2[l]), l=length of a1 assuming a1.length==a2.length
     * Returns false otherwise. <br>
     * Necessary because arrays have no overridden equals method.
     *
     * @param a1 first array to compare
     * @param a2 second array to compare
     * @return true if arrays equal, false otherwise
     */
    private boolean arrayEquals(String[] a1, String[] a2) {
        if (a1.length != a2.length) {
            return false;
        }

        for (int i = 0; i < a1.length; i++) {
            if (!a1[i].equals(a2[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Compares `expected` and `result`. If the two inputs are not equal, as determined by arrayEquals, prints the
     * expected and result to System.err and throws an AssertionError.
     *
     * @param expected expected result
     * @param result output from test
     */
    public void printAssert(String[] expected, String[] result) {
        if (arrayEquals(expected, result) == false) {
            System.err.println("Expected: " + Arrays.toString(expected));
            System.err.println("Result: " + Arrays.toString(result));
            throw new AssertionError("Test failed- Expected and result are not equal");
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //TESTS

    /*
    @DisplayName("When setWorkerLoads is called where its text is null, or block count or minimum block size is not positive, " +
            "the function should throw an IllegalArgumentException")
    @Test
    void testExceptions() {
        StepperFunctions model = new StepperFunctions();

        //Null text
        assertThrows(IllegalArgumentException.class, () -> model.setWorkerLoads(null, 1, 2));

        //Block count = 0
        assertThrows(IllegalArgumentException.class, () -> model.setWorkerLoads("hello world", 0, 1));

        //Block count < 0
        assertThrows(IllegalArgumentException.class, () -> model.setWorkerLoads("hello world", -1, 2));

        //Minimum block size = 0
        assertThrows(IllegalArgumentException.class, () -> model.setWorkerLoads("hello world", 1, 0));

        //Minimum block size < 0
        assertThrows(IllegalArgumentException.class, () -> model.setWorkerLoads("hello world", 1, -1));

        //Text is the string containing "null"
        model.setWorkerLoads("null", 1, 2);
    }
     */

    @DisplayName("When setWorkerLoads is called and blockCount is the text's length divided by minBlockSize, the output should " +
            "hold text.length()/minBlockSize strings of length blockSize")
    @Test
    void testBlockSizeEvenSplit() {
        StepperFunctions model = new StepperFunctions();

        //3 blocks of 8
        String s = "123456789012345678901234";
        String[] result = model.setWorkerLoads(s, 3, 8);
        String[] expected = new String[]{"12345678", "90123456", "78901234"};
        printAssert(expected, result);

        //5 blocks of 8
        s = "1234567890123456789012345678901234567890";
        expected = new String[]{"12345678", "90123456", "78901234", "56789012", "34567890"};
        result = model.setWorkerLoads(s, 5, 8);
        printAssert(expected, result);

        //1 block of 8
        s = "12345678";
        expected = new String[]{"12345678"};
        result = model.setWorkerLoads(s, 1, 8);
        printAssert(expected, result);

        //6 blocks of 4
        s = "123456789012345678901234";
        expected = new String[]{"1234", "5678", "9012", "3456", "7890", "1234"};
        result = model.setWorkerLoads(s, 6, 4);
        printAssert(expected, result);

        //2 blocks of 6
        s = "123456789012";
        expected = new String[]{"123456", "789012"};
        result = model.setWorkerLoads(s, 2, 6);
        printAssert(expected, result);

        //4 blocks of 3
        s = "123456789012";
        expected = new String[]{"123", "456", "789", "012"};
        result = model.setWorkerLoads(s, 4, 3);
        printAssert(expected, result);
    }

    @DisplayName("When setWorkerLoads is called and blockCount is the text's length divided by 'm', a multiple of minBlockSize, " +
            "the output should hold text.length()/m strings of length m.")
    @Test
    void testEvenSplit() {
        StepperFunctions model = new StepperFunctions();

        //Block length is 8*2 (m=16)
        String s = "12345678901234567890123456789012";
        String[] result = model.setWorkerLoads(s, 2, 8);
        String[] expected = new String[]{"1234567890123456", "7890123456789012"};
        printAssert(expected, result);

        //Block length is 8*2 (m=16)
        s = "1234567812345678123456781234567812345678123456781234567812345678";
        expected = new String[]{"1234567812345678", "1234567812345678", "1234567812345678", "1234567812345678"};
        result = model.setWorkerLoads(s, 4,8);
        printAssert(expected, result);

        //Block length is 5*3 (m=15)
        s = "123451234512345123451234512345123451234512345";
        expected = new String[]{"123451234512345", "123451234512345", "123451234512345"};
        result = model.setWorkerLoads(s, 3, 5);

        //Block length is 6*2 (m=12)
        s = "121212121212121212121212121212121212121212121212";
        expected = new String[]{"121212121212", "121212121212", "121212121212", "121212121212"};
        result = model.setWorkerLoads(s, 4, 2);
    }

    @DisplayName("When blockCount is 1, setWorkerLoads should return a String array with the" +
            " entire contents of its String input in its first index, regardless of the minimum block size")
    @Test
    void testBlockCount1() {
        StepperFunctions model = new StepperFunctions();

        String s = "123456789012345678901234";
        String[] result = model.setWorkerLoads(s, 1, 8);
        String[] expected = new String[]{"123456789012345678901234"};
        printAssert(expected, result);

        s = "12345678";
        result = model.setWorkerLoads(s, 1, 2);
        expected = new String[]{"12345678"};
        printAssert(expected, result);

        s = "12345";
        result = model.setWorkerLoads(s, 1, 1);
        expected = new String[]{"12345"};
        printAssert(expected, result);

        s = "";
        result = model.setWorkerLoads(s, 1, 999);
        expected = new String[]{""};
        printAssert(expected, result);
    }


    @DisplayName("When blockCount is not the text length divided by minBlockSize and the text is split in half," +
            " the output's final index should take the remainder")
    @Test
    void testSingleUnevenSplit() {
        StepperFunctions model = new StepperFunctions();

        //First index has enough room for one string of length minBlockSize
        String s = "12345678901234567890";
        String[] result = model.setWorkerLoads(s, 2, 8);
        String[] expected = new String[]{"12345678", "901234567890"};
        printAssert(expected, result);

        //First index has enough room for many strings of length minBlockSize
        s = "1234567812345678123456781";
        result = model.setWorkerLoads(s, 2, 8);
        expected = new String[]{"1234567812345678", "123456781"};
        printAssert(expected, result);

        //First index has enough room for many strings of length minBlockSize, minBlockSize is not 8
        s = "1234561234561234561";
        result = model.setWorkerLoads(s, 2, 6);
        expected = new String[]{"123456123456", "1234561"};
        printAssert(expected, result);

        //First index has enough room for many strings of length minBlockSize and second index has room for many length-minBlockSize strings
        s = "123123123123123";
        result = model.setWorkerLoads(s, 2, 3);
        expected = new String[]{"123123", "123123123"};
        printAssert(expected, result); //Note: {"123123123", "123123"} also works for setWorkerLoads' intended purpose
    }

    @DisplayName("When blockCount is not the text length divided by minBlockSize and blockCount is more than 2," +
            " the output's final index should take the remainder")
    @Test
    void testUnevenSplit() {
        StepperFunctions model = new StepperFunctions();

        //Remainder block should be longer than others
        String s = "1234567890123456789012345678901234567890";
        String[] result = model.setWorkerLoads(s, 4, 8);
        String[] expected = new String[]{"12345678", "90123456", "78901234", "5678901234567890"};
        printAssert(expected, result);

        //Remainder block should be longer than others. Each block is longer than the minimum block size
        s = "12345678123456781234567812345678123456781234567812345";
        result = model.setWorkerLoads(s, 3, 8);
        expected = new String[]{"1234567812345678", "1234567812345678", "123456781234567812345"};
        printAssert(expected, result);

        //Remainder block should be shorter than others
        s = "1234512345123451234512345123451234512345123";
        result = model.setWorkerLoads(s, 3, 5);
        expected = new String[]{"123451234512345", "123451234512345", "1234512345123"};
        printAssert(expected, result);
    }

    @DisplayName("When blockCount divided by minBlockSize, rounded down to the nearest integer, equals 1 and " +
            "more than 2 blocks should be loaded into each output index, setWorkerLoads should load the appropriate" +
            "number of blocks (not 1 block) into each output index")
    @Test
    void testBugfix() {
        StepperFunctions model = new StepperFunctions();

        String s = "1234567890123456123456789012345612345678901234561234567890123456123456789012";
        String[] result = model.setWorkerLoads(s, 3, 16);
        String[] expected = new String[]{"12345678901234561234567890123456", "12345678901234561234567890123456", "123456789012"};
        printAssert(expected, result);

        s = "12341234123412341234123412341234123";
        result = model.setWorkerLoads(s, 5, 4);
        expected = new String[]{"12341234", "12341234", "12341234", "12341234", "123"};
        printAssert(expected, result);
    }

    @DisplayName("When blockSize is more than the text length divided by minBlockSize, all remaining strings should be empty (non-null) strings")
    @Test
    void testUnderflow() {
        StepperFunctions model = new StepperFunctions();

        String s = "123456781234567812345678";
        String[] result = model.setWorkerLoads(s, 5, 8);
        String[] expected = new String[]{"12345678", "12345678", "12345678", "", ""};
        printAssert(expected, result);

        s = "123456781";
        result = model.setWorkerLoads(s, 3, 8);
        expected = new String[]{"12345678", "1", ""};
        printAssert(expected, result);

        s = "12345678";
        result = model.setWorkerLoads(s, 3, 8);
        expected = new String[]{"12345678", "", ""};
        printAssert(expected, result);

        s = "1234567";
        result = model.setWorkerLoads(s, 2, 8);
        expected = new String[]{"1234567", ""};
        printAssert(expected, result);

        s = "123123123123123";
        result = model.setWorkerLoads(s, 6, 3);
        expected = new String[]{"123", "123", "123", "123", "123", ""};
        printAssert(expected, result);

        s = "123456789012345678901234567890";
        result = model.setWorkerLoads(s, 5, 30);
        expected = new String[]{"123456789012345678901234567890", "", "", "", ""};
        printAssert(expected, result);
    }
}
