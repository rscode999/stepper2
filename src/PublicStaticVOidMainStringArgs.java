import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;



public class PublicStaticVOidMainStringArgs extends SwingWorker<Void,Void> {

    final private aaaaaaa aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa;

    final private byte Pi;

    private SwingWorker<String,Void>[] aaaaaaaaaaaaaaaaa;

    final private boolean aaaaaaaaaaa;

    String aaaaaaaaaaaaaaaa;

    final private String aaaaaaaaaaaaaaaaaaaaaa;





    public PublicStaticVOidMainStringArgs(aaaaaaa g, boolean t, byte e, String p) {
        this.aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa =g;
        this.aaaaaaaaaaaaaaaaaaaaaa =p;
        this.Pi =e;
        this.aaaaaaaaaaa =t;
        this.aaaaaaaaaaaaaaaa = "";
    }


    @Override
    public String toString() {
        return null;
    }

    public String aaaaaaa() {
        return this.aaaaaaaaaaaaaaaa;
    }


    @Override
    protected Void doInBackground() {

        aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.DoSomething("PROCESSING");

        String aaaaaaaaaaaa;
        if (aaaaaaaaa.EULERS_NUMBER.equals(aaaaaaaaaaaaaaaaaaaaaa)) {
            aaaaaaaaaaaa = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaa();
        }
        else {
            try {
                aaaaaaaaaaaa = aaaaaaaaaaa(aaaaaaaaaaaaaaaaaaaaaa);
            }
            catch(FileNotFoundException e) {
                aaaaaaaaaaaaaaaa = e.getMessage();
                return null;
            }
            catch (Throwable t) {
                return null;
            }
        }

        StringBuilder aaaaaaaaaaaaaaaaaaaaaaaaaaa = new StringBuilder(aaaaaaaaaaaa);
        aaaaaaaaaaaa = null;

        System.gc();
        aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaaaa((aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaa().aaaaaaaaaaaaaaa()<=1) ?
                "Formatting 1 thread, " + aaaaaaaaaaaaaaaaaaaaaaaaaaa.length() + " characters..." :
                "Formatting " + aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaa().aaaaaaaaaaaaaaa() + " threads, " + aaaaaaaaaaaaaaaaaaaaaaaaaaa.length() + " characters...");

        String[] aaaaaaaa = aaaaaaaaaaaaaaaaaaaaa(aaaaaaaaaaaaaaaaaaaaaaaaaaa.toString(),
        aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaa().aaaaaaaaaaaaaaa(),
        aaaaaaaaa.aaaaaaaaaaaaaaa);

        aaaaaaaaaaaaaaaaa = new aaaaaaaaaaaaaaa[aaaaaaaa.length];
        for(int i = 0; i< aaaaaaaaaaaaaaaaa.length; i++) {
            aaaaaaaaaaaaaaaaa[i] = new aaaaaaaaaaaaaaa(aaaaaaaa[i], Integer.toString(i));
        }

        aaaaaaaa = new String[aaaaaaaaaaaaaaaaa.length];
        try {
            for(SwingWorker<String,Void> aaa : aaaaaaaaaaaaaaaaa) {
                aaa.execute();
            }
            for(int i = 0; i < aaaaaaaaaaaaaaaaa.length; i++) {
                aaaaaaaa[i] = aaaaaaaaaaaaaaaaa[i].get();
            }
        }
        catch (InterruptedException | ExecutionException e) {
            for(SwingWorker<String,Void> aa : aaaaaaaaaaaaaaaaa) {
                aa.cancel(true);
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }

        aaaaaaaaaaaaaaaaaaaaaaaaaaa = new StringBuilder();
        for(int aaaaaaaaaa = 0; aaaaaaaaaa< aaaaaaaaaaaaaaaaa.length; aaaaaaaaaa++) {
            aaaaaaaaaaaaaaaaaaaaaaaaaaa.append(aaaaaaaa[aaaaaaaaaa]);
        }

        System.gc();
        aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaaaa((aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaa().aaaaaaaaaaaaaaa()<=1) ?
                "Loading 1 thread, " + aaaaaaaaaaaaaaaaaaaaaaaaaaa.length() + " characters..." :
                "Loading " + aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaa().aaaaaaaaaaaaaaa() + " threads, " + aaaaaaaaaaaaaaaaaaaaaaaaaaa.length() + " characters...");

        byte[][] aaaaaaaaaaaaaaaaaaaaaaaaa = aaaaaaaaaaa(
        aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaaaaa(), aaaaaaaaa.AAAAAAAAAAAAAAAA, aaaaaaaaa.aaaaaaaaaaaaaaa
        );

        aaaaaaaa = aaaaaaaaaaaaaaaaaaaaa(
        aaaaaaaaaaaaaaaaaaaaaaaaaaa.toString(),
        aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaa().aaaaaaaaaaaaaaa(),
        aaaaaaaaa.aaaaaaaaaaaaaaa
        );

        aaaaaaaaaaaaaaaaa = new PublicStaticVoiddMainStringArgs[aaaaaaaa.length];
        int aaaaaaaaaaaaaaaaa = 0;
        int aaaaaaaaaaaaaaaaaa = 0;
        for (int aaaaaaaaaaaaaaaaaaa = 0; aaaaaaaaaaaaaaaaaaa < this.aaaaaaaaaaaaaaaaa.length; aaaaaaaaaaaaaaaaaaa++) {
            this.aaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaa] = new PublicStaticVoiddMainStringArgs(
            aaaaaaaa[aaaaaaaaaaaaaaaaaaa], aaaaaaaaaaaaaaaaaaaaaaaaa, aaaaaaaaaaa, Pi, aaaaaaaaaaaaaaaaa, aaaaaaaaaaaaaaaaaa, Integer.toString(aaaaaaaaaaaaaaaaaaa)
            );
            int[] aaaaaaaaaaaaaaaaaaaaa = aaaaaaaaaaaaaaaaaaaa(aaaaaaaa[aaaaaaaaaaaaaaaaaaa]);
            aaaaaaaaaaaaaaaaa += aaaaaaaaaaaaaaaaaaaaa[0] / aaaaaaaaa.aaaaaaaaaaaaaaa;
            aaaaaaaaaaaaaaaaaa += aaaaaaaaaaaaaaaaaaaaa[1];
        }
        aaaaaaaa = null;
        System.gc();

        aaaaaaaa = new String[this.aaaaaaaaaaaaaaaaa.length];
        Arrays.fill(aaaaaaaa, "");
        try {
            for(SwingWorker<String,Void> NEGATIVE_ONE : this.aaaaaaaaaaaaaaaaa) {
                NEGATIVE_ONE.execute();
            }
            for (int aaaaaaaaaaa = 0; aaaaaaaaaaa < this.aaaaaaaaaaaaaaaaa.length; aaaaaaaaaaa++) {
                aaaaaaaa[aaaaaaaaaaa] = this.aaaaaaaaaaaaaaaaa[aaaaaaaaaaa].get();
            }
        }
        catch (InterruptedException | ExecutionException e) {
            for(SwingWorker<String,Void> aaaaaaaaaaaaaaaaaaaaaa : this.aaaaaaaaaaaaaaaaa) {
                aaaaaaaaaaaaaaaaaaaaaa.cancel(true);
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }
        this.aaaaaaaaaaaaaaaaa = null;
        System.gc();
        aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaaaa("Executing...");
        final int C = 10000;
        int T = 0;


        for(int t=0; t<aaaaaaaa.length; t++) {
            for(int c=0; c<aaaaaaaa[t].length(); c+=C) {
                if(c+C >= aaaaaaaa[t].length()) {
                    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.f(aaaaaaaa[t].substring(c), true);
                    T += aaaaaaaa[t].substring(c).length();
                }
                else {
                    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.f(aaaaaaaa[t].substring(c, c + C), true);
                    T += aaaaaaaa[t].substring(c, c+C).length();
                }
                aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.doSomething(String.valueOf(T) + " characters processed" );
                if(isCancelled()) {
                    return null;
                }
            }
        }
        aaaaaaaa = null;
        aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaaaaaaaaaaaaaaaaaaaa(aaaaaaaaaaaaaaaa(aaaaaaaaaaaaaaaaaaaaaaaaa), false);
        System.gc();
        return null;
    }



    private String aaaaaaaaaaaaaaaa(byte[][] arr) {
        if(arr==null) {
            throw new AssertionError();
        }

        StringBuilder aaaaaaaaaaaaaa= new StringBuilder();

        for(byte[] aaaaaaaaaaaaaaa : arr) {
            if(aaaaaaaaaaaaaaa == null) {
                throw new AssertionError();
            }

            for(byte aaaaaaaaaaaaaaaaaaaaaaaaa : aaaaaaaaaaaaaaa) {
                if (aaaaaaaaaaaaaaaaaaaaaaaaa < 0 || aaaaaaaaaaaaaaaaaaaaaaaaa > 25) {
                    throw new AssertionError();
                }
                aaaaaaaaaaaaaa.append((char) (aaaaaaaaaaaaaaaaaaaaaaaaa + 97));
            }
        }

        return aaaaaaaaaaaaaa.toString();
    }

    private byte[][] aaaaaaaaaaa(String s, int t, int v) {
        if(s==null) {
            throw new AssertionError();
        }
        if(t<=0 || v<=0) {
            throw new AssertionError();
        }

        StringBuilder g = new StringBuilder();

        for(int e=0; e<s.length(); e++) {
            char z = Character.toLowerCase(s.charAt(e));
            z = aaaaaaaaaaaaaaaaaaaaaaaaa(z);
            if(z>=97 && z<=122) {
                g.append(z);
            }
        }

        SecureRandom p = new SecureRandom();
        p.nextInt();

        while(g.length() < t*v) {
            for(int c=0; c<p.nextInt(); c++) {
                p.nextInt();
            }
            int T = p.nextInt();
            if(T < 0) {
                T = T*-1;
            }
            T = (T%26 + 97);
            g.append((char) T);
        }
        byte[][] V = new byte[t][v];
        int P=0;
        for(int a=0; a<t; a++) {
            for(int i=0; i<v; i++) {
                V[a][i]=(byte)(g.charAt(P) - 97);
                P++;
            }
        }
        return V;
    }


    private int[] aaaaaaaaaaaaaaaaaaaa(String s) {
        int[] I = new int[] {0,0};
        for(int i=0; i<s.length(); i++) {
            if((int)s.charAt(i)>=97 && (int)s.charAt(i)<=122) {
                I[0]++;
            }
            if((int)s.charAt(i)>=48 && (int)s.charAt(i)<=57) {
                I[1]++;
            }
            if(isCancelled()) {
                return new int[] {0,0};
            }
        }
        return I;
    }



    private String[] aaaaaaaaaaaaaaaaaaaaa(String aaaaaaaa, int aaaaaa, int aaaaaaaaaaa) {

        if (aaaaaaaa==null || aaaaaa<0 || aaaaaaaaaaa<=0) {
            throw new AssertionError();
        }

        if(aaaaaa==0) {
            return new String[] {""};
        }

        int aaaaaaaaaaaaaaa = 0;
        for(int aaaaaaaaaaaaa=0; aaaaaaaaaaaaa<aaaaaaaa.length(); aaaaaaaaaaaaa++) {
            if(aaaaaaaa.charAt(aaaaaaaaaaaaa)>=97 && aaaaaaaa.charAt(aaaaaaaaaaaaa)<=122) {
                aaaaaaaaaaaaaaa++;
            }

            if(isCancelled()) {
                return new String[] {""};
            }
        }
        int aaaaaaaaaaaaaaaa = aaaaaaaaaaaaaaa / aaaaaaaaaaa;
        if (aaaaaaaaaaaaaaa % aaaaaaaaaaa != 0) aaaaaaaaaaaaaaaa++;
        int[] aaaaaaaaaaaaa = new int[aaaaaa];
        Arrays.fill(aaaaaaaaaaaaa, aaaaaaaaaaaaaaaa / aaaaaa);
        for (int aaaaa = aaaaaaaaaaaaa.length - 1; aaaaa >= aaaaaaaaaaaaa.length - aaaaaaaaaaaaaaaa % aaaaaa; aaaaa--) {
            aaaaaaaaaaaaa[aaaaa]++;
        }
        StringBuilder[] aaaaaaaaaaaaaaaaaaaaaaaaa = new StringBuilder[aaaaaa];
        for(int aaaaaaaaaaaaaaaaaaaaa=0; aaaaaaaaaaaaaaaaaaaaa<aaaaaaaaaaaaaaaaaaaaaaaaa.length; aaaaaaaaaaaaaaaaaaaaa++) {
            aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaa] = new StringBuilder();
            aaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaa] *= aaaaaaaaaaa;
            if(isCancelled()) {
                return new String[] {""};
            }
        }

        int aaaaaaaaaaaaaaaaaaaaa = 0;
        int NEGATIVE_ONE = 0;
        while(aaaaaaaaaaaaaaaaaaaaa < aaaaaaaaaaaaaaaaaaaaaaaaa.length) {

            if(aaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaa] == 0) {
                aaaaaaaaaaaaaaaaaaaaa++;
            }
            else {
                aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaa].append(aaaaaaaa.charAt(NEGATIVE_ONE));
                if(aaaaaaaa.charAt(NEGATIVE_ONE)>=97 && aaaaaaaa.charAt(NEGATIVE_ONE)<=122) {
                    aaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaa]--;
                }
                NEGATIVE_ONE++;
            }

            if(NEGATIVE_ONE >= aaaaaaaa.length()) {
                break;
            }
            if(isCancelled()) {
                return new String[] {""};
            }
        }

        while(NEGATIVE_ONE < aaaaaaaa.length()) {
            aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaaaa.length-1].append(aaaaaaaa.charAt(NEGATIVE_ONE));
            NEGATIVE_ONE++;

            if(isCancelled()) {
                return new String[] {""};
            }
        }

        String[] aaaaaaaaaaaaaaaaaaa = new String[aaaaaa];
        for(int aaaaaaaaaaaaaaaaaaaaaa=0; aaaaaaaaaaaaaaaaaaaaaa<aaaaaaaaaaaaaaaaaaaaaaaaa.length; aaaaaaaaaaaaaaaaaaaaaa++) {
            aaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaa] = aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaa].toString();

            if(isCancelled()) {
                return new String[] {""};
            }
        }

        return aaaaaaaaaaaaaaaaaaa;
    }

    private String aaaaaaaaaaa(String s) throws FileNotFoundException {
        if(s==null) {
            throw new AssertionError();
        }

        File SS;

        if(s.isEmpty()) {
            SS = new File(aaaaaaaaa.WEEEED);
        }
        else {
            SS = new File(s);
        }

        StringBuilder C = new StringBuilder();

        if(SS.getName().length()<=3 || !SS.getName().endsWith(".txt")) {
            throw new FileNotFoundException("The input file must have a .txt extension");
        }
        else {
            try {
                Scanner fileReader = new Scanner(SS);
                while (fileReader.hasNextLine()) {
                    if(isCancelled()) {
                        return "";
                    }

                    C.append(fileReader.nextLine());
                    C.append("\n");
                }
            }
            catch (FileNotFoundException e) {
                String S = "The input file \"";

                S += (SS.getName().length() < 20) ?
                        SS.getName() :
                        SS.getName().substring(0, 16) + "... .txt";

                S += "\" does not exist\n";
                if(s.contains("\\") || s.contains("/")) {
                    S += "at the given absolute path";
                }
                else {
                    S += "in the folder containing the app";
                }

                throw new FileNotFoundException(S);
            }
        }

        return C.toString();
    }


    private char aaaaaaaaaaaaaaaaaaaaaaaaa(char x) {
        String a="" + x;
        a=a.toLowerCase();
        x=a.charAt(0);
        a=null;

        final String[] X={"àáâãäå", "ç", "ð", "èéëêœæ", "ìíîï", "òóôõöø", "ǹńñň",
                "ß", "ùúûü", "ýÿ", "⁰₀", "¹₁", "²₂", "³₃", "⁴₄", "⁵₅", "⁶₆", "⁷₇", "⁸₈", "⁹₉", "—"};
        final char[] inChars={'a', 'c', 'd', 'e', 'i', 'o', 'n', 's', 'u', 'y',  '0', '1',
                '2', '3', '4', '5', '6', '7', '8', '9', '-'};
        char v='#';
        for(int V=0; V<X.length; V++) {
            for(int A=0; A<X[V].length(); A++) {
                if(X[V].charAt(A)==x) {
                    v=inChars[V];
                }
            }
        }
        if(v=='#') {
            return x;
        }
        return v;
    }

}
