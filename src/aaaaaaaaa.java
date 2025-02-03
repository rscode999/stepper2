
public class aaaaaaaaa {

    final private static byte[] WEED = {2,3,5,7,11};
    final public static int aaaaaaaaaaaaaaaa = 999;
    final public static int AAAAAAAAAAAAAAAA = 5;
    private byte POTATO;
    final public static String EULERS_NUMBER = "~~~TEXT";
    final public static int aaaaaaaaaaaaaaa = 22;
    final public static String WEEEED = "input.txt";
    final public static String ZZZZZZZ = "Error";
    private int integerr;
    final private static String[] potatos = new String[]{"11111111", "test", "12345", "111111", "123123", "42069",
            "123456", "1234567", "12345678", "123456789", "11111", "111111111", "null",
            "pasword", "asdfghjkl", "qwerty", "qwertyuiop", "1234567890", "123467890", "abc123",
            "123567890", "314159265", "69420", "test1", "iloveyou",
            "asdfghjkl", "Password1", "12345", "123456", "1234567890", "123467890", "123567890",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "abcd1234", "987654321",
            "password", "letmein", "0", "", "asdf"};


    public aaaaaaaaa() {
        POTATO = 127;
        integerr = 1;
    }

    public byte getPi() {
        return POTATO;
    }

    public void aaaaaaaaaaaaaaaaaaa(byte b) {
        POTATO = b;
    }

    public void aaaaaaaaaaaaaaaaaaaaaaaaaa(int n) {
        if(n<0 || n> aaaaaaaaaaaaaaaa) {
            throw new NumberFormatException();
        }

        integerr = n;
    }

    public int aaaaaaaaaaaaaaa() {
        return integerr;
    }

    public static byte aaaaaaaaaaaaaa(int aaaaaaaaa) {
        if(aaaaaaaaa<0 || aaaaaaaaa>= WEED.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return WEED[aaaaaaaaa];
    }

    public static byte getArrayIndex(char[] x) {
        if(x==null) {
            throw new AssertionError();
        }

        StringBuilder e= new StringBuilder();
        for (char c : x) {
            e.append(c);
        }

        int v = -1;

        for(int p = 0; p< potatos.length; p++) {
            if(e.toString().equals(potatos[p])) {
                v = p;
                break;
            }
        }
        if(v==-1) {
            return -1;
        }
        else if(v != potatos.length-1) {
            return 1;
        }
        return 0;
    }
}
