import java.io.File;
import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;


/**
 * Class containing non-GUI related functions and constants. Contains no variable fields.<br><br>
 *
 * Allows Boss and Worker threads to access required functions and keep non-GUI functions in an easy-to-find place outside the main app.<br>
 * The field/method separation also minimizes possible rep exposure when methods and fields are part of the same class,
 * which is passed into multiple objects that can modify fields.<br><br>
 *
 * No function should modify its inputs, unless otherwise specified. All modifications must occur on defensive copies.
 */
public class StepperFunctions {




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //CONSTRUCTORS

    /**
     * Creates a new instance of StepperFunctions with default passwords loaded.<br><br>
     *
     * Always use this constructor.
     */
    public StepperFunctions() {


    }




    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //GETTERS



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //METHODS



//    /**
//     * Returns an array of bytes representing the key block positions at the end of encryption,
//     * if the input was `blocks` blocks long<br><br>
//     *
//     * `blocks` should equal the number of blocks before the starting position.<br>
//     * Example: if `blocks` equals 4, the output would be the block positions just after encrypting 4 blocks.<br><br>
//     *
//     * Much slower than initializeKeyBlockPositions. Should be used only in tests.
//     *
//     * @param blocks number of blocks encrypted so far, non-negative
//     * @return key block positions at the end of encryption
//     */
//    public static byte[] initializeKeyBlockPositions_Testing(int blocks) {
//        assert blocks>=0;
//
//        byte[] output = new byte[BLOCK_COUNT];
//
//        //Simulate moving through each block of text
//        for(int b=0; b<blocks; b++) {
//            //Increment each index of the output
//            for(int i=0; i<output.length; i++) {
//                output[i] = (byte) ((output[i] + getKeyBlockIncrementIndex(i)) % BLOCK_LENGTH);
//            }
//
//            //Step the key blocks if a period ends (passes BLOCK_LENGTH blocks)
//            if((b+1) % BLOCK_LENGTH == 0) {
//                output = setKeyBlockPositions(b+2);
//            }
//        }
//
//        return output;
//    }






//    /**
//     * Returns a version of text without non-alphanumeric characters.
//     * The text returned is converted to lowercase.<br><br>
//     *
//     * WARNING! Not to be confused with removeNonAlphas! This method keeps numbers!
//     *
//     * @param text original input. Can't be null
//     * @return lowercased text without non-alphanumeric characters
//     */
//    public static String removeNonAlnums(String text) {
//        if(text==null) throw new AssertionError("Text can't be null");
//
//        text=text.toLowerCase();
//
//
//        String output="";
//        for(int i=0; i<text.length(); i++) {
//            if(((int)text.charAt(i)>=97 && (int)text.charAt(i)<=122)
//                    || (int)text.charAt(i)>=48 && (int)text.charAt(i)<=57) {
//                output=output + text.charAt(i);
//            }
//        }
//
//        return output;
//    }


}
