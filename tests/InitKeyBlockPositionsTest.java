//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//
//public class InitKeyBlockPositionsTest {
//    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    //UTILITY METHODS
//
//    /**
//     * Returns true if `a1` has the same length as `a2` and all corresponding elements of the two arrays are equal.
//     * i.e. a1[0].equals(a2[0]), a1[1].equals(a2[1]) ... a1[l].equals(a2[l]), l=length of a1 assuming a1.length==a2.length
//     * Returns false otherwise. <br>
//     * Necessary because arrays have no overridden equals method.
//     *
//     * @param a1 first array to compare
//     * @param a2 second array to compare
//     * @return true if arrays equal, false otherwise
//     */
//    private boolean arrayEquals(byte[] a1, byte[] a2) {
//        if (a1.length != a2.length) {
//            return false;
//        }
//
//        for (int i = 0; i < a1.length; i++) {
//            if (a1[i] != a2[i]) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * Compares `expected` and `result`. If the two inputs are not equal, as determined by arrayEquals, prints the
//     * expected and result to System.err and throws an AssertionError.
//     *
//     * @param expected expected result
//     * @param result output from test
//     */
//    public void printAssert(byte[] expected, byte[] result) {
//        if (arrayEquals(expected, result) == false) {
//            System.err.println("Expected: " + Arrays.toString(expected));
//            System.err.println("Result: " + Arrays.toString(result));
//            throw new AssertionError("Test failed- Expected and result are not equal");
//        }
//    }
//
//
//    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    //TESTS
//
//    @DisplayName("Test")
//    @Test
//    void test() {
//        byte[] expected = StepperFunctions.initializeKeyBlockPositions_Testing(0);
//        byte[] result = StepperFunctions.initializeKeyBlockPositions(0);
//        printAssert(expected, result);
//
//        expected = StepperFunctions.initializeKeyBlockPositions_Testing(18);
//        result = StepperFunctions.initializeKeyBlockPositions(18);
//        printAssert(expected, result);
//
//        expected = StepperFunctions.initializeKeyBlockPositions_Testing(19);
//        result = StepperFunctions.initializeKeyBlockPositions(19);
//        printAssert(expected, result);
//
//        expected = StepperFunctions.initializeKeyBlockPositions_Testing(19*2);
//        result = StepperFunctions.initializeKeyBlockPositions(19*2);
//        printAssert(expected, result);
//
//        expected = StepperFunctions.initializeKeyBlockPositions_Testing(19*2+1);
//        result = StepperFunctions.initializeKeyBlockPositions(19*2+1);
//        printAssert(expected, result);
//
//        expected = StepperFunctions.initializeKeyBlockPositions_Testing(19*3);
//        result = StepperFunctions.initializeKeyBlockPositions(19*3);
//        printAssert(expected, result);
//
//        expected = StepperFunctions.initializeKeyBlockPositions_Testing(19*3+1);
//        result = StepperFunctions.initializeKeyBlockPositions(19*3+1);
//        printAssert(expected, result);
//
//        expected = StepperFunctions.initializeKeyBlockPositions(19*19*19);
//        result = StepperFunctions.initializeKeyBlockPositions(19*19*19);
//        printAssert(expected, result);
//
//        expected = StepperFunctions.initializeKeyBlockPositions(19*19*19+1);
//        result = StepperFunctions.initializeKeyBlockPositions(19*19*19+1);
//        printAssert(expected, result);
//    }
//}
