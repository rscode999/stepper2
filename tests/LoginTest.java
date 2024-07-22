import org.junit.jupiter.api.DisplayName;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Class for testing the custom password constructor and the login method in StepperFunctions
 */
public class LoginTest {


    //CONSTRUCTOR TESTS


    @DisplayName("A StepperFunctions instance should throw an exception if " +
            "any index in its valid passwords input array is null")
    @Test
    public void testConstructorException() {
        //Several nulls
        String[] passwords1 = new String[] {null, "ab", ")93jdF0", null, "hello world", "AleDefgE", null};
        assertThrows(IllegalArgumentException.class, () -> { StepperFunctions fcns = new StepperFunctions(passwords1); });

        //Null at the start
        String[] passwords2 = new String[] {null, "ab", "aBc", "abcde", "hello world", ",.1*234f\\[de"};
        assertThrows(IllegalArgumentException.class, () -> { StepperFunctions fcns = new StepperFunctions(passwords2); });

        //Null in the middle
        String[] passwords3 = new String[] {"a", "ab", "aBc", "abcde", null, ",.1*234f\\[de"};
        assertThrows(IllegalArgumentException.class, () -> { StepperFunctions fcns = new StepperFunctions(passwords3); });

        //Null at the end
        String[] passwords4 = new String[] {"a", "ab", "aBc", "abcde", "hello world", null};
        assertThrows(IllegalArgumentException.class, () -> { StepperFunctions fcns = new StepperFunctions(passwords4); });
    }


    @DisplayName("A StepperFunctions instance should not throw an exception if " +
            "all indices in its valid passwords input array are not null")
    @Test
    public void testConstructorNoException() {
        //Normal string, with some backslashes
        String[] passwords1 = new String[] {"a", "ab", ")93jdF0", "[]\\", "hello world", "AleDefgE"};
        StepperFunctions fcns = new StepperFunctions(passwords1);

        //Contains empty string
        String[] passwords2 = new String[] {"", "ab", ")93jdF0", "[]\\", "hello world", "AleDefgE"};
        fcns = new StepperFunctions(passwords2);

        //Contains a string with the text "null"
        String[] passwords3 = new String[] {"", "ab", ")93jdF0", "[]\\", "null", "AleDefgE"};
        fcns = new StepperFunctions(passwords3);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //LOGIN TESTS


    @DisplayName("When the entered password (as a char array) is equal to the final index in the passwords array, " +
            "login should return 0")
    @Test
    public void testCorrectPassword() {

        //Password with capital letters
        String[] validPasswords = new String[] {"hello world", ",.1*234f\\[de", "a", "ab", "Abcde", "abc", "AbCde"};
        StepperFunctions fcns = new StepperFunctions(validPasswords);
        assertEquals(0, fcns.login("AbCde".toCharArray()));

        //Single letter
        validPasswords = new String[] {"hello world", ",.1*234f\\[de", "a", "ab", "abc", "e"};
        fcns = new StepperFunctions(validPasswords);
        assertEquals(0, fcns.login("e".toCharArray()));

        //Includes spaces
        validPasswords = new String[] {",.1*234f\\[de", "a", "ab", "abc", "e", "hello world"};
        fcns = new StepperFunctions(validPasswords);
        assertEquals(0, fcns.login("hello world".toCharArray()));

        //Empty string
        validPasswords = new String[] {",.1*234f\\[de", "a", "ab", "abc", "e", ""};
        fcns = new StepperFunctions(validPasswords);
        assertEquals(0, fcns.login("".toCharArray()));

        //Password with backslashes and non-letters
        validPasswords = new String[] {"a", "ab", "aBc", "abcde", "hello world", ",.1*234f\\[de"};
        fcns = new StepperFunctions(validPasswords);
        assertEquals(0, fcns.login(",.1*234f\\[de".toCharArray()));
    }


    @DisplayName("When the entered password (as a char array) is not equal to the final index in the passwords array, " +
            "but still in the passwords array, login should return 1")
    @Test
    public void testIncorrectContainedPassword() {
        String[] validPasswords = new String[] {"", "a", "ab", "abc", "abCde", "hello world", ",.1*234f\\[de"};
        StepperFunctions fcns = new StepperFunctions(validPasswords);

        //First index
        assertEquals(1, fcns.login("".toCharArray()));

        //Middle index
        assertEquals(1, fcns.login("abCde".toCharArray()));

        //Second to last index
        assertEquals(1, fcns.login("hello world".toCharArray()));

        //Includes spaces and non-letters
        validPasswords = new String[] {"hello world", ",.1*234f\\  [de", "a", "ab", "abc", "abcde"};
        fcns = new StepperFunctions(validPasswords);
        assertEquals(1, fcns.login(",.1*234f\\  [de".toCharArray()));
    }


    @DisplayName("When the entered password (as a char array) is not equal to any index in the passwords array, " +
            "login should return -1")
    @Test
    public void testIncorrectUncontainedPassword() {
        String[] validPasswords = new String[] {"", "a", "ab", "abc", "abCde", "hello world", ",.1*234f\\[de"};
        StepperFunctions fcns = new StepperFunctions(validPasswords);

        //Single letter
        assertEquals(-1, fcns.login("b".toCharArray()));

        //Password contained in the array, but with letters switched
        assertEquals(-1, fcns.login("ba".toCharArray()));

        //Password contained in the array, but with non-letters switched
        assertEquals(-1, fcns.login(",.1*234\\f[de".toCharArray()));

        //Password contained in the array, but with letters capitalized
        assertEquals(-1, fcns.login("abcde".toCharArray()));
    }


    @DisplayName("When the entered password (as a char array) is equal to more than one password in the passwords array, " +
            "login should ignore all duplicates except for the one with the smallest index number")
    @Test
    public void testDuplicatePasswords() {
        //Single duplicate pair, no duplicates are in the last index, duplicate matches input
        String[] validPasswords = new String[] {"p", "s", "o", "d", "a", "w", "d", "e", "z", "a", "t"};
        StepperFunctions fcns = new StepperFunctions(validPasswords);
        assertEquals(1, fcns.login("d".toCharArray()));

        //Duplicates in other indices, but final password is unique and matches input
        validPasswords = new String[] {"a", "b", "a", "c", "a", "a", "d", "a", "a", "e", "x"};
        fcns = new StepperFunctions(validPasswords);
        assertEquals(0, fcns.login("x".toCharArray()));

        //Duplicates with final password throughout array
        validPasswords = new String[] {"a", "b", "a", "c", "a", "a", "d", "a", "a", "e", "a"};
        fcns = new StepperFunctions(validPasswords);
        assertEquals(1, fcns.login("a".toCharArray()));

        //Duplicates with final password in first index
        validPasswords = new String[] {"a", "s", "a", "c", "a", "w", "d", "d", "z", "e", "a"};
        fcns = new StepperFunctions(validPasswords);
        assertEquals(1, fcns.login("a".toCharArray()));

        //Duplicates with final password in second to last index
        validPasswords = new String[] {"p", "s", "o", "c", "q", "w", "d", "d", "z", "a", "a"};
        fcns = new StepperFunctions(validPasswords);
        assertEquals(1, fcns.login("a".toCharArray()));
    }
}
