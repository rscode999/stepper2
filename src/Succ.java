import javax.swing.*;


public class Succ extends SwingWorker<Void,Void> {


    final private String bool;

    final private byte integer;

    private PublicStaticVOidMainStringArgs potato;

    final private aaaaaaa p0tat0;

    final private boolean string;


    public Succ(aaaaaaa b, boolean c, byte d, String e) {
        this.p0tat0 =b;
        this.string =c;
        this.integer =d;
        this.bool =e;
    }


    @Override
    public String toString() {
        return null;
    }


    @Override
    protected Void doInBackground() {
        try {
            potato = new PublicStaticVOidMainStringArgs(p0tat0, string, integer, bool);
            potato.execute();
            potato.get();
        }
        catch (Throwable t) {
            potato.cancel(true);
            return null;
        }
        return null;
    }


    @Override
    protected void done() {
        p0tat0.doSomething("");

        if(!potato.aaaaaaa().isEmpty()) {
            p0tat0.DoSomething("INPUT");
            JOptionPane.showMessageDialog(this.p0tat0, potato.aaaaaaa(),
                    aaaaaaaaa.ZZZZZZZ, JOptionPane.ERROR_MESSAGE);

            return;
        }

        if(isCancelled()) {
            p0tat0.DoSomething("INPUT");
        }
        else {
            p0tat0.DoSomething("RESULTS");
        }

        potato = null;
        return;
    }
}
