import javax.swing.*;


public class PublicStaticVoiddMainStringArgs extends SwingWorker<String,Void> {


    private String aaaaaaaaaaaaaaa;

    final private boolean aaaaaaaaaaaaaaaaaaaa;

    final private String aaaaaaaaaaaaaa;

    private final byte[][] aaaaaaaaaa;

    final private byte aaaaaa;

    final private int aaaaaaaaaaaaaaaaaaaaaaaa;

    final private int SEVENTEEN;



    public PublicStaticVoiddMainStringArgs(String g, byte[][] p, boolean v,
                                           byte z, int c, int d, String e) {

        if(p==null) {
            throw new AssertionError();
        }

        this.aaaaaaaaaa = new byte[p.length][p[0].length];
        for(int a=0; a<p.length; a++) {
            if(p[a]==null) {
                throw new AssertionError();
            }
            for(int o=0; o<p[0].length; o++) {
                this.aaaaaaaaaa[a][o] = p[a][o];
            }
        }

        this.aaaaaaaaaaaaaaa =g;
        this.aaaaaa =z;
        this.SEVENTEEN =d;
        this.aaaaaaaaaaaaaa =e;
        this.aaaaaaaaaaaaaaaaaaaaaaaa =c;
        this.aaaaaaaaaaaaaaaaaaaa =v;
    }

    @Override
    public String toString() {
        return null;
    }


    @Override
    protected String doInBackground() {

        if(aaaaaaaaaaaaaaaaaaaa && aaaaaa==1) {
            aaaaaaaaaaaaaaa = aaaaaaaaaaaaaa(aaaaaaaaaaaaaaa);
        }
        char[] aaaaaaaaaaaaaaaaaaaa = aaaaaaaaaaaaa(aaaaaaaaaaaaaaa);
        aaaaaaaaaaaaaaa = aaaaaaaaaaaaaaaaaaaaaaaaaaaa(aaaaaaaaaaaaaaa);

        if (this.aaaaaaaaaaaaaaaaaaaa) {
            aaaaaaaaaaaaaaa = aaaaaaaaaaa(aaaaaaaaaaaaaaa, aaaaaaaaaa, aaaaaaaaaaaaaaaaaaaaaaaa);
        }
        else {
            aaaaaaaaaaaaaaa = aaaaaaaaaaaa(aaaaaaaaaaaaaaa, aaaaaaaaaa, aaaaaaaaaaaaaaaaaaaaaaaa);
        }

        aaaaaaaaaaaaaaa = aaaaaaaaaaa(aaaaaaaaaaaaaaa, aaaaaaaaaaaaaaaaaaaa, aaaaaa<=1);

        if(this.aaaaaaaaaaaaaaaaaaaa) {
            aaaaaaaaaaaaaaa = aaaaaaaaaaaaaaaaaaaaaaaaa(aaaaaaaaaaaaaaa, aaaaaaaaaa, SEVENTEEN);
        }
        else {
            aaaaaaaaaaaaaaa = aaaaaaaaa(aaaaaaaaaaaaaaa, aaaaaaaaaa, SEVENTEEN);
        }
        return aaaaaaaaaaaaaaa;
    }



    private char[] aaaaaaaaaaaaa(String aaaaaaaaaaaaaaaaa) {
        if(aaaaaaaaaaaaaaaaa==null) {
            throw new AssertionError();
        }

        char[] aaaaaaaaaaaaaaaaaaaaaaaaaaaa = new char[aaaaaaaaaaaaaaaaa.length()];

        for(int aaaaaaaaaaaaaaaaaaaaaaa=0; aaaaaaaaaaaaaaaaaaaaaaa<aaaaaaaaaaaaaaaaa.length(); aaaaaaaaaaaaaaaaaaaaaaa++) {
            if(isCancelled()) {
                return new char[] {'C'};
            }
            if((int)aaaaaaaaaaaaaaaaa.charAt(aaaaaaaaaaaaaaaaaaaaaaa)<65
                    || ((int)aaaaaaaaaaaaaaaaa.charAt(aaaaaaaaaaaaaaaaaaaaaaa)>90 && (int)aaaaaaaaaaaaaaaaa.charAt(aaaaaaaaaaaaaaaaaaaaaaa)<97)
                    || (int)aaaaaaaaaaaaaaaaa.charAt(aaaaaaaaaaaaaaaaaaaaaaa)>122) {

                aaaaaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa] = aaaaaaaaaaaaaaaaa.charAt(aaaaaaaaaaaaaaaaaaaaaaa);
            } else {
                aaaaaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa]=(char)0;
            }
        }
        return aaaaaaaaaaaaaaaaaaaaaaaaaaaa;
    }



    private String aaaaaaaaa(String aaaaaaaaaa, byte[][] AAAAAAAAAA, int aaaaaaaaaaa) {

        StringBuilder AAAAAAA = new StringBuilder();
        for(byte[] AAAAAAAAAAAA : AAAAAAAAAA) {
            if (AAAAAAAAAAAA == null) {
                throw new AssertionError();
            }
            for(byte AAAAAAAAAAAAA : AAAAAAAAAAAA) {
                if (AAAAAAAAAAAAA < 0 || AAAAAAAAAAAAA > 25) {
                    throw new AssertionError();
                }
                AAAAAAA.append((char) AAAAAAAAAAAAA);
            }
        }
        StringBuilder aaaaaaaaaaaa = new StringBuilder(aaaaaaaaaa.length());
        int aaaaaaaaaaaaaaa;
        int aaaaa = aaaaaaaaaaa % AAAAAAA.length();
        for(int aaaaaaaaaaaaaaaaaaaaaaaaa=0; aaaaaaaaaaaaaaaaaaaaaaaaa<aaaaaaaaaa.length(); aaaaaaaaaaaaaaaaaaaaaaaaa++) {
            if(isCancelled()) {
                return "";
            }


            aaaaaaaaaaaaaaa=(int)aaaaaaaaaa.charAt(aaaaaaaaaaaaaaaaaaaaaaaaa);

            if(aaaaaaaaaaaaaaa>=48 && aaaaaaaaaaaaaaa<=57) {
                aaaaaaaaaaaaaaa -= 48;

                aaaaaaaaaaaaaaa = (aaaaaaaaaaaaaaa - (int)AAAAAAA.charAt(aaaaa)) % 10;
                if(aaaaaaaaaaaaaaa < 0) {
                    aaaaaaaaaaaaaaa += 10;
                }
                aaaaaaaaaaaaaaa = (aaaaaaaaaaaaaaa - (int)AAAAAAA.charAt(aaaaa)) % 10;
                if(aaaaaaaaaaaaaaa < 0) {
                    aaaaaaaaaaaaaaa += 10;
                }

                aaaaaaaaaaaaaaa += 48;

                aaaaa++;
                if(aaaaa >= AAAAAAA.length()) {
                    aaaaa = 0;
                }
            }

            aaaaaaaaaaaa.append((char)aaaaaaaaaaaaaaa);
        }

        return aaaaaaaaaaaa.toString();
    }


    private String aaaaaaaaaaaaaa(String aaaaaaaaaaaaaaaaaaa) {
        StringBuilder aaaaaaa = new StringBuilder(aaaaaaaaaaaaaaaaaaa.length());
        aaaaaaa.append(aaaaaaaaaaaaaaaaaaa.charAt(0));

        for(int aaaaaaaaa=1; aaaaaaaaa<aaaaaaaaaaaaaaaaaaa.length()-1; aaaaaaaaa++) {

            if(! (aaaaaaaaaaaaaaaaaaa.charAt(aaaaaaaaa)==' '
                    && Character.isAlphabetic(aaaaaaaaaaaaaaaaaaa.charAt(aaaaaaaaa-1)) && Character.isAlphabetic(aaaaaaaaaaaaaaaaaaa.charAt(aaaaaaaaa+1)))) {
                aaaaaaa.append(aaaaaaaaaaaaaaaaaaa.charAt(aaaaaaaaa));
            }

            if(isCancelled()) {
                return "";
            }
        }

        aaaaaaa.append(aaaaaaaaaaaaaaaaaaa.charAt( aaaaaaaaaaaaaaaaaaa.length()-1 ));

        return aaaaaaa.toString();
    }




    private String aaaaaaaaaaaaaaaaaaaaaaaaa(String aaaaaaa, byte[][] aaaaaaaaaaaaaaa, int aaaaaaaaaaa) {
        StringBuilder aaaa = new StringBuilder();
        for(byte[] aaaaa : aaaaaaaaaaaaaaa) {
            if (aaaaa == null) {
                throw new AssertionError();
            }
            for(byte aaaaaaaaaaaaaaaaaaaa : aaaaa) {
                if (aaaaaaaaaaaaaaaaaaaa < 0 || aaaaaaaaaaaaaaaaaaaa > 25) {
                    throw new AssertionError();
                }
                aaaa.append((char) aaaaaaaaaaaaaaaaaaaa);
            }
        }

        StringBuilder aaaaaaaaaaaaaaaaaaaaa = new StringBuilder(aaaaaaa.length());

        int aaaaaaaaaaaaaaaaaaaa;
        int aaaaaaaaaaaaaaaaaaaaaaaaaaaaa = aaaaaaaaaaa % aaaa.length();

        for(int aaaaaaaaaaaaaaaaaaaaaa=0; aaaaaaaaaaaaaaaaaaaaaa<aaaaaaa.length(); aaaaaaaaaaaaaaaaaaaaaa++) {
            if(isCancelled()) {
                return "";
            }

            aaaaaaaaaaaaaaaaaaaa=(int)aaaaaaa.charAt(aaaaaaaaaaaaaaaaaaaaaa);
            if(aaaaaaaaaaaaaaaaaaaa>=48 && aaaaaaaaaaaaaaaaaaaa<=57) {
                aaaaaaaaaaaaaaaaaaaa -= 48;
                aaaaaaaaaaaaaaaaaaaa = (aaaaaaaaaaaaaaaaaaaa + (aaaa.charAt(aaaaaaaaaaaaaaaaaaaaaaaaaaaaa))) % 10;
                aaaaaaaaaaaaaaaaaaaa = (aaaaaaaaaaaaaaaaaaaa + (aaaa.charAt(aaaaaaaaaaaaaaaaaaaaaaaaaaaaa))) % 10;
                aaaaaaaaaaaaaaaaaaaa += 48;
                aaaaaaaaaaaaaaaaaaaaaaaaaaaaa++;
                if(aaaaaaaaaaaaaaaaaaaaaaaaaaaaa >= aaaa.length()) {
                    aaaaaaaaaaaaaaaaaaaaaaaaaaaaa=0;
                }
            }
            aaaaaaaaaaaaaaaaaaaaa.append((char)aaaaaaaaaaaaaaaaaaaa);
        }

        return aaaaaaaaaaaaaaaaaaaaa.toString();
    }



    private String aaaaaaaaaaaa(String aaaaaaaaaaaa, byte[][] aaaaaaaaaa, int aaaaaaaaaaaaaaaaaaaaaaaaaaaa) {

        byte[] aaaaaaaaaaaaaaaa= aaaaaaaaaaaaaaaaaaaaaaa(aaaaaaaaaaaaaaaaaaaaaaaaaaaa + aaaaaaaaaaaa.length()/ aaaaaaaaa.aaaaaaaaaaaaaaa);

        StringBuilder aaaaaaaaaaaaa = new StringBuilder(aaaaaaaaaaaa.length());

        int aaaaaaaaaaaaaaaaaaaaa=0;
        int aaaaaaaaaaaaaaaaaaaa = (aaaaaaaaaaaaaaaaaaaaaaaaaaaa + aaaaaaaaaaaa.length()/ aaaaaaaaa.aaaaaaaaaaaaaaa);

        byte[] aaaaaaaaaaaaaaaaaa=new byte[aaaaaaaaa.AAAAAAAAAAAAAAAA];
        System.arraycopy(aaaaaaaaaaaaaaaa, 0, aaaaaaaaaaaaaaaaaa, 0, aaaaaaaaaaaaaaaaaa.length);

        for(int aaaaaaaa = 0; aaaaaaaa<(aaaaaaaaaaaa.length() % aaaaaaaaa.aaaaaaaaaaaaaaa); aaaaaaaa++) {
            for(int aaaa=0; aaaa<aaaaaaaaaaaaaaaaaa.length; aaaa++) {
                aaaaaaaaaaaaaaaaaa[aaaa]++;
                if(aaaaaaaaaaaaaaaaaa[aaaa] >= aaaaaaaaa.aaaaaaaaaaaaaaa) {
                    aaaaaaaaaaaaaaaaaa[aaaa]=0;
                }
            }
        }

        if(isCancelled()) {
            return "";
        }

        for(int aaaaaaaaaaaaaaa = aaaaaaaaaaaa.length()-1; aaaaaaaaaaaaaaa>=aaaaaaaaaaaa.length()-(aaaaaaaaaaaa.length() % aaaaaaaaa.aaaaaaaaaaaaaaa); aaaaaaaaaaaaaaa--) {

            for(int aaaaaaaaaaaaaaaaaaaaaaaaaaaaa=0; aaaaaaaaaaaaaaaaaaaaaaaaaaaaa<aaaaaaaaaaaaaaaaaa.length; aaaaaaaaaaaaaaaaaaaaaaaaaaaaa++) {
                aaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaaaaaaaa] -= 1;
                if(aaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaaaaaaaa] < 0) {
                    aaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaaaaaaaa] = aaaaaaaaa.aaaaaaaaaaaaaaa -1;
                }
            }

            aaaaaaaaaaaaaaaaaaaaa=aaaaaaaaaaaa.charAt(aaaaaaaaaaaaaaa) - 97;
            for(int aaaaaaaaaaaaaaaaaaa=aaaaaaaaaaaaaaaaaa.length-1; aaaaaaaaaaaaaaaaaaa>=0; aaaaaaaaaaaaaaaaaaa--) {
                aaaaaaaaaaaaaaaaaaaaa = (aaaaaaaaaaaaaaaaaaaaa - aaaaaaaaaa[aaaaaaaaaaaaaaaaaaa][aaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaa]]) % 26;
                if(aaaaaaaaaaaaaaaaaaaaa < 0) {
                    aaaaaaaaaaaaaaaaaaaaa += 26;
                }
            }
            aaaaaaaaaaaaa.append((char)(aaaaaaaaaaaaaaaaaaaaa+97));
        }

        for(int aaaaaaaaaaaaaaaaaaaaaa = aaaaaaaaaaaa.length()-(aaaaaaaaaaaa.length() % aaaaaaaaa.aaaaaaaaaaaaaaa)-1; aaaaaaaaaaaaaaaaaaaaaa>=0; aaaaaaaaaaaaaaaaaaaaaa-= aaaaaaaaa.aaaaaaaaaaaaaaa) {
            if(isCancelled()) {
                return "";
            }

            aaaaaaaaaaaaaaaaaaaa--;
            if((aaaaaaaaaaaaaaaaaaaa+1)% aaaaaaaaa.aaaaaaaaaaaaaaa ==0) {
                aaaaaaaaaaaaaaaa = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa(aaaaaaaaaaaaaaaaaaaa);
            }


            for(int aaaaaaaa=0; aaaaaaaa<aaaaaaaaaaaaaaaa.length; aaaaaaaa++) {
                aaaaaaaaaaaaaaaa[aaaaaaaa] -= aaaaaaaaa.aaaaaaaaaaaaaa(aaaaaaaa);

                if(aaaaaaaaaaaaaaaa[aaaaaaaa]<0) {
                    aaaaaaaaaaaaaaaa[aaaaaaaa] += aaaaaaaaa.aaaaaaaaaaaaaaa;
                }
            }

            System.arraycopy(aaaaaaaaaaaaaaaa, 0, aaaaaaaaaaaaaaaaaa, 0, aaaaaaaaaaaaaaaaaa.length);

            for(int aaaaaaaaaaaaaaaaa = aaaaaaaaaaaaaaaaaaaaaa; aaaaaaaaaaaaaaaaa > aaaaaaaaaaaaaaaaaaaaaa - aaaaaaaaa.aaaaaaaaaaaaaaa; aaaaaaaaaaaaaaaaa--) {

                for(int aaaaaaaaaaa=0; aaaaaaaaaaa<aaaaaaaaaaaaaaaaaa.length; aaaaaaaaaaa++) {
                    aaaaaaaaaaaaaaaaaa[aaaaaaaaaaa]--;
                    if(aaaaaaaaaaaaaaaaaa[aaaaaaaaaaa] < 0) {
                        aaaaaaaaaaaaaaaaaa[aaaaaaaaaaa]= aaaaaaaaa.aaaaaaaaaaaaaaa -1;
                    }
                }

                aaaaaaaaaaaaaaaaaaaaa=(int)aaaaaaaaaaaa.charAt(aaaaaaaaaaaaaaaaa) - 97;

                for(int aa=aaaaaaaaaaaaaaaaaa.length-1; aa>=0; aa--) {

                    aaaaaaaaaaaaaaaaaaaaa = (aaaaaaaaaaaaaaaaaaaaa - aaaaaaaaaa[aa][aaaaaaaaaaaaaaaaaa[aa]]) % 26;
                    if(aaaaaaaaaaaaaaaaaaaaa < 0) {
                        aaaaaaaaaaaaaaaaaaaaa += 26;
                    }

                }

                aaaaaaaaaaaaa.append((char)(aaaaaaaaaaaaaaaaaaaaa+97));

            }
        }
        aaaaaaaaaaaaa.reverse();
        return aaaaaaaaaaaaa.toString();
    }

    private byte[] aaaaaaaaaaaaaaaaaaaaaaa(long aa) {
        assert aa >= 0;
        byte[] v = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa(aa);
        for(int z = 0; z < aa% aaaaaaaaa.aaaaaaaaaaaaaaa; z++) {
            for(int d=0; d<v.length; d++) {
                v[d] = (byte) ((v[d] + aaaaaaaaa.aaaaaaaaaaaaaa(d)) % aaaaaaaaa.aaaaaaaaaaaaaaa);
            }
        }
        return v;
    }

    private String aaaaaaaaaaa(String aaaaaaaaaaa, char[] aaaaaaaaa, boolean aaaaaaaaaaaa) {

        char[] aaaaaaaaaaaaaaaaaaaaaaaaa = new char[aaaaaaaaa.length];
        for(int aaaaaaaaaaaaaa=0; aaaaaaaaaaaaaa<aaaaaaaaaaaaaaaaaaaaaaaaa.length; aaaaaaaaaaaaaa++) {
            if(isCancelled()) {
                return "";
            }

            aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaa]=aaaaaaaaa[aaaaaaaaaaaaaa];
        }

        StringBuilder aaaaaaaa = new StringBuilder(aaaaaaaaaaa.length());
        int aaaaaaaaaaaaaaaaaaaaaaa=0;
        int aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa=aaaaaaaaaaa.length();
        int SIXTEEN=0;

        while(aaaaaaaaaaaaaaaaaaaaaaa < aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa) {
            if(isCancelled()) {
                return "";
            }

            if(aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa] > 0) {
                if(!(aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa]==(char)39 || aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa]==(char)96 || aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa]=='’' || aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa]=='`')) {

                    if( (aaaaaaaaaaaa) ||
                            (aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa]>=48 && aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa]<=57) ) {
                        aaaaaaaa.append((char)aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa]);
                    }
                    aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa]=0;
                }

                aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa++;
            }
            else {
                aaaaaaaa.append(aaaaaaaaaaa.charAt(SIXTEEN));
                SIXTEEN++;
            }

            aaaaaaaaaaaaaaaaaaaaaaa++;
        }

        while(aaaaaaaaaaaaaaaaaaaaaaa < aaaaaaaaaaaaaaaaaaaaaaaaa.length) {
            if(isCancelled()) {
                return "";
            }

            if((aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa]>0 && aaaaaaaaaaaa)
                    || (aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa])>=48 && aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa]<=57) {
                aaaaaaaa.append(aaaaaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaa]);
            }
            aaaaaaaaaaaaaaaaaaaaaaa++;
        }


        return aaaaaaaa.toString();
    }


    private String aaaaaaaaaaaaaaaaaaaaaaaaaaaa(String aaaaaaaaaaa) {
        if(aaaaaaaaaaa==null) throw new AssertionError();

        if(isCancelled()) {
            return "";
        }

        aaaaaaaaaaa=aaaaaaaaaaa.toLowerCase();

        StringBuilder aaaaaaaaaaaaaa = new StringBuilder(aaaaaaaaaaa.length());
        for(int aaaaaaaaaaaa=0; aaaaaaaaaaaa<aaaaaaaaaaa.length(); aaaaaaaaaaaa++) {
            if(isCancelled()) {
                return "";
            }

            if((int)aaaaaaaaaaa.charAt(aaaaaaaaaaaa)>=97 && (int)aaaaaaaaaaa.charAt(aaaaaaaaaaaa)<=122) {
                aaaaaaaaaaaaaa.append(aaaaaaaaaaa.charAt(aaaaaaaaaaaa));
            }
        }

        return aaaaaaaaaaaaaa.toString();
    }


    private byte[] aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa(long aaaaaaa) {
        if(aaaaaaa < 0) throw new AssertionError();

        byte[] aaaaaaaaaaaaaa = new byte[aaaaaaaaa.AAAAAAAAAAAAAAAA];

        long aaaaaaaaaaa=aaaaaaa;
        double aaaaaaaaaa=0;

        aaaaaaaaaaa = aaaaaaaaaaa % ((long)Math.pow(aaaaaaaaa.aaaaaaaaaaaaaaa, aaaaaaaaa.AAAAAAAAAAAAAAAA));

        aaaaaaaaaaa = aaaaaaaaaaa / aaaaaaaaa.aaaaaaaaaaaaaaa;

        for(int aaaaaaaaaaaaa=aaaaaaaaaaaaaa.length-1; aaaaaaaaaaaaa>=0; aaaaaaaaaaaaa--) {
            aaaaaaaaaa = (double)aaaaaaaaaaa / aaaaaaaaa.aaaaaaaaaaaaaaa - aaaaaaaaaaa / aaaaaaaaa.aaaaaaaaaaaaaaa;
            aaaaaaaaaaa = aaaaaaaaaaa / (long) aaaaaaaaa.aaaaaaaaaaaaaaa;
            aaaaaaaaaaaaaa[aaaaaaaaaaaaa] = (byte)(Math.round(aaaaaaaaaa* aaaaaaaaa.aaaaaaaaaaaaaaa));
            if(aaaaaaaaaaa <= 0) {
                break;
            }
        }

        byte[] aaaaaaaaaaaaaaaaaaaaaa = new byte[aaaaaaaaaaaaaa.length];
        for(int aaaaaaaaaaaa=0; aaaaaaaaaaaa<aaaaaaaaaaaaaa.length; aaaaaaaaaaaa++) {
            aaaaaaaaaaaaaaaaaaaaaa[aaaaaaaaaaaa] = aaaaaaaaaaaaaa[aaaaaaaaaaaaaa.length - aaaaaaaaaaaa - 1];
        }

        return aaaaaaaaaaaaaaaaaaaaaa;
    }


    private String aaaaaaaaaaa(String aaaaaaaaaaaaaa, byte[][] aaaaaaaaaa, int aaaaaaaaaaaaaaaaa) {


        byte[] aaaaaaaaaaaaaaaaaaaaa = aaaaaaaaaaaaaaaaaaaaaaa(aaaaaaaaaaaaaaaaa);
        byte[] aaaaaa = new byte[aaaaaaaaa.AAAAAAAAAAAAAAAA];
        System.arraycopy(aaaaaaaaaaaaaaaaaaaaa, 0, aaaaaa, 0, aaaaaa.length);


        StringBuilder aaaaaaaaaaaaaaaaaaaaaa = new StringBuilder(aaaaaaaaaaaaaa.length());
        int aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa=0;
        int aaaaaaaaaaa = aaaaaaaaaaaaaaaaa;

        for(int aaaaaaaaaaaaaaaaaaaa = 0; aaaaaaaaaaaaaaaaaaaa <= (aaaaaaaaaaaaaa.length() - aaaaaaaaa.aaaaaaaaaaaaaaa); aaaaaaaaaaaaaaaaaaaa += aaaaaaaaa.aaaaaaaaaaaaaaa) {
            if(isCancelled()) {
                return "";
            }

            System.arraycopy(aaaaaaaaaaaaaaaaaaaaa, 0, aaaaaa, 0, aaaaaa.length);

            for(int aaa = aaaaaaaaaaaaaaaaaaaa; aaa<(aaaaaaaaaaaaaaaaaaaa + aaaaaaaaa.aaaaaaaaaaaaaaa); aaa++) {

                aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa=(int)aaaaaaaaaaaaaa.charAt(aaa) - 97;

                for(int aaaaaaaaaaaaaaaaaaaaaaaaaaa=0; aaaaaaaaaaaaaaaaaaaaaaaaaaa<aaaaaa.length; aaaaaaaaaaaaaaaaaaaaaaaaaaa++) {
                    aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa = (aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa + aaaaaaaaaa[aaaaaaaaaaaaaaaaaaaaaaaaaaa][aaaaaa[aaaaaaaaaaaaaaaaaaaaaaaaaaa]]) % 26;
                }

                aaaaaaaaaaaaaaaaaaaaaa.append((char)(aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa+97));

                for(int aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa=0; aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa<aaaaaa.length; aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa++) {
                    aaaaaa[aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa]++;
                    if(aaaaaa[aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa] >= aaaaaaaaa.aaaaaaaaaaaaaaa) {
                        aaaaaa[aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa]=0;
                    }
                }
            }

            for(int aaaaa=0; aaaaa<aaaaaaaaaaaaaaaaaaaaa.length; aaaaa++) {
                aaaaaaaaaaaaaaaaaaaaa[aaaaa] = (byte) ((aaaaaaaaaaaaaaaaaaaaa[aaaaa] + aaaaaaaaa.aaaaaaaaaaaaaa(aaaaa)) % aaaaaaaaa.aaaaaaaaaaaaaaa);
            }

            if((aaaaaaaaaaa+1) % aaaaaaaaa.aaaaaaaaaaaaaaa == 0) {
                aaaaaaaaaaaaaaaaaaaaa = aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa(aaaaaaaaaaa+2);
            }

            aaaaaaaaaaa++;
        }

        System.arraycopy(aaaaaaaaaaaaaaaaaaaaa, 0, aaaaaa, 0, aaaaaa.length);

        if(isCancelled()) {
            return "";
        }

        for(int aaaaaaaaaaaaaaaaaaaaaaaaaa = aaaaaaaaaaaaaa.length()-(aaaaaaaaaaaaaa.length() % aaaaaaaaa.aaaaaaaaaaaaaaa); aaaaaaaaaaaaaaaaaaaaaaaaaa<aaaaaaaaaaaaaa.length(); aaaaaaaaaaaaaaaaaaaaaaaaaa++) {

            aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa=(int)aaaaaaaaaaaaaa.charAt(aaaaaaaaaaaaaaaaaaaaaaaaaa) - 97;

            for(int aaaaaaa=0; aaaaaaa<aaaaaa.length; aaaaaaa++) {
                aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa = (aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa + aaaaaaaaaa[aaaaaaa][aaaaaa[aaaaaaa]]) % 26;
            }

            aaaaaaaaaaaaaaaaaaaaaa.append((char)(aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa+97));

            for(int aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa=0; aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa<aaaaaa.length; aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa++) {
                aaaaaa[aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa]++;
                if(aaaaaa[aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa] >= aaaaaaaaa.aaaaaaaaaaaaaaa) {
                    aaaaaa[aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa]=0;
                }
            }
        }

        return aaaaaaaaaaaaaaaaaaaaaa.toString();
    }

}
