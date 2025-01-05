import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Class to test the method `removeDiacritics` of a `ParsingBoss`.
 */
public class RemoveDiacriticsCharTest {

    @DisplayName("removeDiacritics should leave ASCII characters unchanged")
    @Test
    void testNoRemoves() {
        ParsingBoss boss = new ParsingBoss();
        char bossResult;
        char input;

        //Test a letter
        input = 'a';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('a', bossResult);

        //Test another letter
        input = 'z';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('z', bossResult);

        //Test a number
        input = '0';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('0', bossResult);

        //Test another number
        input = '9';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('9', bossResult);

        //Test a symbol
        input = '+';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('+', bossResult);

        //Test a space
        input = ' ';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals(' ', bossResult);
    }


    @DisplayName("removeDiacritics should eliminate accents from characters in its internal arrays")
    @Test
    void testAccentRemoval() {
        ParsingBoss boss = new ParsingBoss();
        char bossResult;
        char input;

        //Test the first letter in the 'a' replacements
        input = 'à';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('a', bossResult);

        //Test the last letter in the 'a' replacements
        input = 'å';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('a', bossResult);

        //Test a letter that's alone in its replacements
        input = 'ð';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('d', bossResult);

        //This character renders weird, so test it
        input = 'ǹ';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('n', bossResult);

        //One more for good measure
        input = 'ÿ';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('y', bossResult);
    }


    @DisplayName("removeDiacritics should reformat superscript and subscript numbers to ASCII equivalents")
    @Test
    void testNumbers() {
        ParsingBoss boss = new ParsingBoss();
        char bossResult;
        char input;

        //Superscript 0
        input = '⁰';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('0', bossResult);

        //Superscript 5
        input = '⁵';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('5', bossResult);

        //Superscript 9
        input = '⁹';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('9', bossResult);

        //Subscript 0
        input = '₀';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('0', bossResult);

        //Subscript 1
        input = '₁';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('1', bossResult);

        //Subscript 9
        input = '₉';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('9', bossResult);
    }


    @DisplayName("removeDiacritics should reformat selected symbols to ASCII equivalents")
    @Test
    void testSymbols() {
        ParsingBoss boss = new ParsingBoss();
        char bossResult;
        char input;

        //This is the only weird symbol
        input = '—';
        bossResult = boss.removeDiacritics_Testing(input);
        Assertions.assertEquals('-', bossResult);
    }

}
