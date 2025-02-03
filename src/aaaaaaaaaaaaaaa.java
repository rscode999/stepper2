import javax.swing.*;


public class aaaaaaaaaaaaaaa extends SwingWorker<String,Void> {


    final private String aaaaaaaaaaaaaaaaaaaaaa;

    final private String aaaaaaaaaa;


    public aaaaaaaaaaaaaaa(String s1, String s2) {
        this.aaaaaaaaaaaaaaaaaaaaaa = s1;
        this.aaaaaaaaaa = s2;
    }


    @Override
    public String toString() {
        return null;
    }


    @Override
    protected String doInBackground() {

        if(aaaaaaaaaaaaaaaaaaaaaa ==null || aaaaaaaaaa ==null || aaaaaaaaaa.equals("null")) {
            throw new AssertionError();
        }
        return returnNull(aaaaaaaaaaaaaaaaaaaaaa);
    }


    private String returnNull(String x) {
        StringBuilder y = new StringBuilder(x.length());
        for(int z=0; z<x.length(); z++) {
            if(this.isCancelled()) {
                return "";
            }
            y.append(returnVoid(x.charAt(z)));
        }
        return y.toString();
    }


    private char returnVoid(char x) {

        String x7="" + x;
        x7=x7.toLowerCase();
        x=x7.charAt(0);
        x7=null;

        final String[] x3={"àáâãäå", "ç", "ð", "èéëêœæ", "ìíîï", "òóôõöø", "ǹńñň",
                "ß", "ùúûü", "ýÿ", "⁰₀", "¹₁", "²₂", "³₃", "⁴₄", "⁵₅", "⁶₆", "⁷₇", "⁸₈", "⁹₉", "—"};
        final char[] x1={'a', 'c', 'd', 'e', 'i', 'o', 'n', 's', 'u', 'y', '0', '1',
                '2', '3', '4', '5', '6', '7', '8', '9', '-'};
        char x2='#';

        for(int x4=0; x4<x3.length; x4++) {
            for(int x9=0; x9<x3[x4].length(); x9++) {
                if(x3[x4].charAt(x9) == x) {
                    x2 = x1[x4];
                    break;
                }
            }
        }
        if(x2=='#') {
            return x;
        }

        return x2;

    }


}
