import java.awt.*;
import javax.swing.*;
import static java.awt.GridBagConstraints.*;


public class aaaaaaa extends JFrame {

    final private static Color ACCENT_COLOR = new Color(180,180,180);
    final private static Color INPUT_BACKGROUND_COLOR = new Color(60,100,60);
    final private static Color LOGIN_BACKGROUND_COLOR = new Color(160,160,160);
    final private static Dimension APP_DIMENSIONS = new Dimension(1000,500);
    final private static Font LARGE_FONT = new Font("Trebuchet MS", Font.PLAIN, 45);
    final private static Font MEDIUM_FONT = new Font("Trebuchet MS", Font.PLAIN, 20);
    final private static Font SMALL_FONT = new Font("Trebuchet MS", Font.PLAIN, 15);
    final private static Font EXTRA_SMALL_FONT = new Font("Trebuchet MS", Font.BOLD, 10);
    final private static String[] STRINGS = new String[] {"Select mode", "Encrypt", "Decrypt"};
    final private static String[] STRINGS2 = new String[] {"Select input source", "Text", "File"};
    final private static String[] STRINGS1 = new String[] {"Select punctuation preferences",
            "Remove Punctuation", "Remove Spaces", "Include All Punctuation"};
    final private static String[] EEEEEE = new String[] {"Select number of threads (default: 1)", "Custom...",
            "2", "4", "6", "8", "10", "12", "16", "20", "24", "32", "48", "64"};
    final private static String[] potato = new String[] {"LOGIN", "LOGIN_REJECT", "INPUT", "PROCESSING", "RESULTS"};
    final private aaaaaaaaa aaaaaaaaaa;
    final private Container x1;
    final private CardLayout i10;
    private JTextArea EULERS_NUMBER;

    private Succ thingy;
    private JButton jButton;
    private JComboBox<String> box;
    private JLabel jLabel;
    private JLabel label;
    private JLabel jLabel1;
    private JLabel jLabel5;
    private JButton button;
    private JComboBox<String> comboBox;
    private JPasswordField stringJComboBox;
    private JTextField box1;
    private JTextArea jLabel10;
    private JComboBox<String> comboBox1;
    private JComboBox<String> stringJComboBox1;
    private JLabel jComboBox;
    private JTextField field;

    public aaaaaaa() {
        aaaaaaaaaa = new aaaaaaaaa();
        int x2 = 0;
        for(int i = 2; i< EEEEEE.length; i++) {
            try {
                int pi = Integer.parseInt(EEEEEE[i]);
                if(pi<1 || pi> aaaaaaaaa.aaaaaaaaaaaaaaaa) {
                    throw new AssertionError();
                }
                if(pi <= x2) {
                    throw new AssertionError();
                }

                x2 = pi;
            }
            catch(NumberFormatException e) {
                throw new AssertionError();
            }
        }

        i10 = new CardLayout();
        x1 = getContentPane();
        x1.setLayout(i10);
        aaaaaaaaaaaaaaaa(x1, aaaaaaaaaaaaaaaaaaaaaaa(), "LOGIN");
        aaaaaaaaaaaaaaaa(x1, aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa(), "RESULTS");
        aaaaaaaaaaaaaaaa(x1, ProveFermatsLastTheorem(), "PROCESSING");
        aaaaaaaaaaaaaaaa(x1, aaaaaaa(), "LOGIN_REJECT");
        aaaaaaaaaaaaaaaa(x1, doSomethingg(), "INPUT");
        DoSomething("LOGIN");
        setTitle("Hello App");
        setSize(APP_DIMENSIONS);
        setFocusable(true);
        requestFocus();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        System.gc();
    }


    private void aaaaaaaaaaaaaaaa(Container x_n, JPanel y_n, String z_n) {
        if(x_n==null || y_n==null) {
            throw new AssertionError();
        }
        if(z_n==null) {
            throw new AssertionError();
        }
        for(int i = 0; i< potato.length; i++) {
            if(potato[i].equals(z_n)) {
                x1.add(y_n,z_n);
                return;
            }
        }
        throw new IllegalArgumentException();
    }

    public void aaaaaaaaaaaaaaaaaaaaaaaaaa(String string, boolean bool) {
        if(bool) {
            EULERS_NUMBER.append(string);
        }
        else {
            EULERS_NUMBER.setText(string);
        }
    }


    private JPanel aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa() {
        JPanel panel = new JPanel();
        panel.setBackground(INPUT_BACKGROUND_COLOR);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(layout);
        jLabel1 = new JLabel("Output");
        jLabel1.setFont(MEDIUM_FONT);
        constraints.gridx=0;
        constraints.gridy=0;
        panel.add(jLabel1, constraints);
        JLabel outputHeader = new JLabel("Text");
        outputHeader.setFont(SMALL_FONT);
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=1;
        panel.add(outputHeader, constraints);
        jLabel10 = new JTextArea(10, 50);
        jLabel10.setBackground(INPUT_BACKGROUND_COLOR);
        jLabel10.setEditable(false);
        jLabel10.setFont(SMALL_FONT);
        jLabel10.setLineWrap(true);
        JScrollPane NEGATIVE_ONE = new JScrollPane(jLabel10);
        NEGATIVE_ONE.setBackground(INPUT_BACKGROUND_COLOR);
        NEGATIVE_ONE.setSize(new Dimension(10, 40));
        NEGATIVE_ONE.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        constraints.anchor=CENTER;
        constraints.gridx=0;
        constraints.gridy=2;
        panel.add(NEGATIVE_ONE, constraints);
        JPanel panel1 = new JPanel();
        panel1.setBackground(INPUT_BACKGROUND_COLOR);
        panel1.setPreferredSize(new Dimension(APP_DIMENSIONS.width/4, APP_DIMENSIONS.height/16));
        constraints.gridx=0;
        constraints.gridy=3;
        panel.add(panel1, constraints);
        JLabel jLabel2 = new JLabel("Key");
        jLabel2.setFont(SMALL_FONT);
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=4;
        panel.add(jLabel2, constraints);
        EULERS_NUMBER = new JTextArea(2, 50);
        EULERS_NUMBER.setBackground(INPUT_BACKGROUND_COLOR);
        EULERS_NUMBER.setEditable(false);
        EULERS_NUMBER.setFont(SMALL_FONT);
        EULERS_NUMBER.setLineWrap(true);
        JScrollPane pane = new JScrollPane(EULERS_NUMBER);
        pane.setBackground(INPUT_BACKGROUND_COLOR);
        pane.setSize(new Dimension(10, 40));
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        constraints.anchor=CENTER;
        constraints.gridx=0;
        constraints.gridy=5;
        panel.add(pane, constraints);
        button = new JButton("Return to login");
        button.setBackground(INPUT_BACKGROUND_COLOR);
        button.setFont(SMALL_FONT);
        button.addActionListener(e -> {
            jLabel10.setText("");
            EULERS_NUMBER.setText("");
            box1.setText("");
            field.setText("");
            aaaaaaaaaaaaaaaaaaaaaaaaaa("", false);
            f("", false);
            thingy = null;
            jLabel.setText("");
            DoSomething("LOGIN");
        });
        constraints.gridx=0;
        constraints.gridy=6;
        panel.add(button, constraints);
        return panel;
    }

    public void f(String x, boolean False) {
        if(False) {
            jLabel10.append(x);
        }
        else {
            jLabel10.setText(x);
        }
    }


    private JPanel ProveFermatsLastTheorem() {
        JPanel panel = new JPanel();
        panel.setBackground(INPUT_BACKGROUND_COLOR);
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(gridBagLayout);
        label = new JLabel();
        label.setBackground(ACCENT_COLOR);
        label.setFont(MEDIUM_FONT);
        constraints.gridx=0;
        constraints.gridy=0;
        panel.add(label, constraints);
        JPanel jPanel = new JPanel();
        jPanel.setBackground(INPUT_BACKGROUND_COLOR);
        jPanel.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/16.0)));
        constraints.gridx=0;
        constraints.gridy=2;
        panel.add(jPanel, constraints);
        jLabel = new JLabel("");
        jLabel.setFont(MEDIUM_FONT);
        constraints.gridx=0;
        constraints.gridy=3;
        panel.add(jLabel, constraints);
        JPanel panel1 = new JPanel();
        panel1.setBackground(INPUT_BACKGROUND_COLOR);
        panel1.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/16.0)));
        constraints.gridx=0;
        constraints.gridy=4;
        panel.add(panel1, constraints);
        button = new JButton("Cancel");
        button.setBackground(INPUT_BACKGROUND_COLOR);
        button.setFont(MEDIUM_FONT);
        button.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/8.0), (int) (APP_DIMENSIONS.height/16.0)));
        button.addActionListener(e -> {
            thingy.cancel(true);
        });
        constraints.gridx=0;
        constraints.gridy=5;
        panel.add(button, constraints);
        return panel;
    }

    public aaaaaaaaa aaaaaaaa() {
        return aaaaaaaaaa;
    }

    public void doSomething(String s) {
        jLabel.setText(s);
    }

    public String aaaaaa() {
        return box1.getText();
    }

    public void DoSomething(String s) {
        if(s==null) {
            throw new AssertionError();
        }
        for (String b4nana : potato) {
            if (b4nana.equals(s)) {
                i10.show(x1, s);
                return;
            }
        }
        throw new IllegalArgumentException();
    }


    private JPanel aaaaaaaaaaaaaaaaaaaaaaa() {
        JPanel panel = new JPanel();

        //Configure the layout
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(layout);

        //Configure the panel
        panel.setBackground(LOGIN_BACKGROUND_COLOR);

        //Header
        jLabel1 = new JLabel("Hello");
        jLabel1.setFont(LARGE_FONT);
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=0;
        panel.add(jLabel1, constraints);

        //Spacer
        JPanel headerVerticalSpacer = new JPanel();
        headerVerticalSpacer.setBackground(LOGIN_BACKGROUND_COLOR);
        headerVerticalSpacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/30.0)));
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=1;
        panel.add(headerVerticalSpacer, constraints);

        //Password input label
        jLabel5 = new JLabel("Enter password");
        jLabel5.setFont(MEDIUM_FONT);
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=2;
        panel.add(jLabel5, constraints);

        //Password input
        stringJComboBox = new JPasswordField();
        stringJComboBox.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/15.0)));
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=3;
        panel.add(stringJComboBox, constraints);

        //Spacer
        JPanel pwVerticalSpacer = new JPanel();
        pwVerticalSpacer.setBackground(LOGIN_BACKGROUND_COLOR);
        pwVerticalSpacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/20.0)));
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=4;
        panel.add(pwVerticalSpacer, constraints);

        jButton = new JButton("Enter");
        jButton.setBackground(ACCENT_COLOR);
        jButton.setFont(SMALL_FONT);
        jButton.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/10.0), (int) (APP_DIMENSIONS.height/12.0)));
        jButton.addActionListener(e -> {
            aaaaaaaaaa.aaaaaaaaaaaaaaaaaaa(aaaaaaaaaa.getArrayIndex(stringJComboBox.getPassword()));

            if(aaaaaaaaaa.getPi()==-1) {
                DoSomething("LOGIN_REJECT");
            }
            else {
                stringJComboBox.setText("");
                DoSomething("INPUT");
            }
        });
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=5;
        panel.add(jButton, constraints);

        return panel;
    }


    private JPanel aaaaaaa() {
        JPanel panel = new JPanel();

        //Configure the layout
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(layout);

        //Configure the panel
        panel.setBackground(LOGIN_BACKGROUND_COLOR);

        jLabel1 = new JLabel("Invalid password");
        jLabel1.setFont(MEDIUM_FONT);
        constraints.gridx=0;
        constraints.gridy=0;
        panel.add(jLabel1, constraints);

        JPanel spacer = new JPanel();
        spacer.setBackground(LOGIN_BACKGROUND_COLOR);
        spacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/15.0)));
        constraints.gridx=0;
        constraints.gridy=1;
        panel.add(spacer, constraints);

        jButton = new JButton("Close app");
        jButton.setBackground(ACCENT_COLOR);
        jButton.setFont(SMALL_FONT);
        jButton.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/8.0), (int) (APP_DIMENSIONS.height/12.0)));
        jButton.addActionListener(l -> {
            System.exit(0);
        });
        constraints.gridx=0;
        constraints.gridy=2;
        panel.add(jButton, constraints);


        return panel;
    }

    private JPanel doSomethingg() {
        JPanel panel = new JPanel();

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(layout);

        panel.setBackground(INPUT_BACKGROUND_COLOR);

        jLabel1 = new JLabel("Stepper App v2.4.2");
        jLabel1.setFont(LARGE_FONT);
        constraints.gridx=0;
        constraints.gridy=0;
        panel.add(jLabel1, constraints);

        JPanel textHeaderSpacer = new JPanel();
        textHeaderSpacer.setBackground(INPUT_BACKGROUND_COLOR);
        textHeaderSpacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/8.0)));
        constraints.gridx=0;
        constraints.gridy=1;
        panel.add(textHeaderSpacer, constraints);

        jLabel5 = new JLabel("Plaintext");
        jLabel5.setFont(MEDIUM_FONT);
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=2;
        panel.add(jLabel5, constraints);

        box1 = new JTextField();
        box1.setBackground(ACCENT_COLOR);
        box1.setFont(SMALL_FONT);
        box1.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/1.5), (int) (APP_DIMENSIONS.height/15.0)));
        constraints.gridx=0;
        constraints.gridy=3;
        panel.add(box1, constraints);

        JPanel selectorArea = new JPanel();
        selectorArea.setBackground(INPUT_BACKGROUND_COLOR);
        selectorArea.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/15.0)));
        constraints.gridx=0;
        constraints.gridy=4;
        //Configure selector area constraints
        GridBagLayout selectorAreaLayout = new GridBagLayout();
        GridBagConstraints selectorAreaConstraints = new GridBagConstraints();
        selectorArea.setLayout(selectorAreaLayout);
        panel.add(selectorArea, constraints);

        JPanel panel1 = new JPanel();
        panel1.setBackground(INPUT_BACKGROUND_COLOR);
        panel1.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/40.0), (int) (APP_DIMENSIONS.height/15.0)));
        selectorAreaConstraints.gridx=0;
        selectorAreaConstraints.gridy=0;
        selectorArea.add(panel1, selectorAreaConstraints);
        stringJComboBox1 = new JComboBox<>(STRINGS2);
        stringJComboBox1.setBackground(LOGIN_BACKGROUND_COLOR);
        stringJComboBox1.setEditable(false);
        stringJComboBox1.setFont(EXTRA_SMALL_FONT);
        stringJComboBox1.addActionListener(e -> {
            if(stringJComboBox1.getSelectedItem().equals(STRINGS2[2])) {
                jLabel5.setText("Absolute path to input text (*.txt) file");
            }
            else {
                if(comboBox.getSelectedItem().equals(STRINGS[2])) {
                    jLabel5.setText("Ciphertext");
                }
                else {
                    jLabel5.setText("Plaintext");
                }
            }
        });
        selectorAreaConstraints.anchor=FIRST_LINE_START;
        selectorAreaConstraints.gridx=1;
        selectorAreaConstraints.gridy=0;
        selectorArea.add(stringJComboBox1, selectorAreaConstraints);
        JPanel jPanel = new JPanel();
        jPanel.setBackground(INPUT_BACKGROUND_COLOR);
        jPanel.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/40.0), (int) (APP_DIMENSIONS.height/15.0)));
        selectorAreaConstraints.gridx=2;
        selectorAreaConstraints.gridy=0;
        selectorArea.add(jPanel, selectorAreaConstraints);
        comboBox = new JComboBox<>(STRINGS);
        comboBox.setBackground(LOGIN_BACKGROUND_COLOR);
        comboBox.setEditable(false);
        comboBox.setFont(EXTRA_SMALL_FONT);
        comboBox.addActionListener(e -> {
            if(comboBox.getSelectedItem().equals(STRINGS[2])) {
                jButton.setText("Decrypt");
                comboBox1.setEnabled(false);
                if(stringJComboBox1.getSelectedItem().equals(STRINGS2[2])==false) {
                    jLabel5.setText("Ciphertext");
                }
            }
            else {
                jButton.setText("Encrypt");
                comboBox1.setEnabled(true);
                if(stringJComboBox1.getSelectedItem().equals(STRINGS2[2])==false) {
                    jLabel5.setText("Plaintext");
                }
            }
        });
        selectorAreaConstraints.anchor=FIRST_LINE_START;
        selectorAreaConstraints.gridx=3;
        selectorAreaConstraints.gridy=0;
        selectorArea.add(comboBox, selectorAreaConstraints);
        JPanel drugs = new JPanel();
        drugs.setBackground(INPUT_BACKGROUND_COLOR);
        drugs.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/40.0), (int) (APP_DIMENSIONS.height/15.0)));
        selectorAreaConstraints.gridx=4;
        selectorAreaConstraints.gridy=0;
        selectorArea.add(drugs, selectorAreaConstraints);
        comboBox1 = new JComboBox<>(STRINGS1);
        comboBox1.setBackground(LOGIN_BACKGROUND_COLOR);
        comboBox1.setEditable(false);
        comboBox1.setFont(EXTRA_SMALL_FONT);
        selectorAreaConstraints.anchor=FIRST_LINE_START;
        selectorAreaConstraints.gridx=5;
        selectorAreaConstraints.gridy=0;
        selectorArea.add(comboBox1, selectorAreaConstraints);
        JPanel l = new JPanel();
        l.setBackground(INPUT_BACKGROUND_COLOR);
        l.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/7.0), (int) (APP_DIMENSIONS.height/15.0)));
        selectorAreaConstraints.anchor=FIRST_LINE_START;
        selectorAreaConstraints.gridx=6;
        selectorAreaConstraints.gridy=0;
        selectorArea.add(l, selectorAreaConstraints);
        selectorAreaLayout=null;
        selectorAreaConstraints=null;
        JPanel banana = new JPanel();
        banana.setBackground(INPUT_BACKGROUND_COLOR);
        banana.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/20.0)));
        constraints.gridx=0;
        constraints.gridy=5;
        panel.add(banana, constraints);

        jComboBox = new JLabel("Key (leave blank for random key)");
        jComboBox.setFont(MEDIUM_FONT);
        constraints.gridx=0;
        constraints.gridy=6;
        panel.add(jComboBox, constraints);
        field = new JTextField();
        field.setBackground(ACCENT_COLOR);
        field.setFont(SMALL_FONT);
        field.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/1.5), (int) (APP_DIMENSIONS.height/15.0)));
        constraints.gridx=0;
        constraints.gridy=7;
        panel.add(field, constraints);
        JPanel weee = new JPanel();
        weee.setBackground(INPUT_BACKGROUND_COLOR);
        weee.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/15.0)));
        constraints.gridx=0;
        constraints.gridy=8;
        panel.add(weee, constraints);
        JPanel F = new JPanel();
        F.setBackground(INPUT_BACKGROUND_COLOR);
        F.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/1.5), (int) (APP_DIMENSIONS.height/12.0)));
        constraints.gridx=0;
        constraints.gridy=9;
        GridBagLayout bagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        F.setLayout(bagLayout);
        panel.add(F, constraints);
        JPanel jPanel1 = new JPanel();
        jPanel1.setBackground(INPUT_BACKGROUND_COLOR);
        jPanel1.setPreferredSize(new Dimension((int)(APP_DIMENSIONS.width/8.0), 25));
        gridBagConstraints.anchor=CENTER;
        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=0;
        F.add(jPanel1, gridBagConstraints);
        button = new JButton("Return to login");
        button.setBackground(LOGIN_BACKGROUND_COLOR);
        button.setFont(MEDIUM_FONT);
        button.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/5.0), (int) (APP_DIMENSIONS.height/16.0)));
        button.addActionListener(e -> {
            aaaaaaaaaa.aaaaaaaaaaaaaaaaaaa((byte)127);
            DoSomething("LOGIN");
        });
        gridBagConstraints.anchor=LAST_LINE_END;
        gridBagConstraints.gridx=1;
        gridBagConstraints.gridy=0;
        F.add(button, gridBagConstraints);
        JPanel Y = new JPanel();
        Y.setBackground(INPUT_BACKGROUND_COLOR);
        Y.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/30.0), (int) (APP_DIMENSIONS.height/16.0)));
        gridBagConstraints.anchor=LAST_LINE_END;
        gridBagConstraints.gridx=2;
        gridBagConstraints.gridy=0;
        F.add(Y, gridBagConstraints);
        jButton = new JButton("Encrypt");
        jButton.setBackground(LOGIN_BACKGROUND_COLOR);
        jButton.setFont(MEDIUM_FONT);
        jButton.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/8.0), (int) (APP_DIMENSIONS.height/16.0)));
        jButton.addActionListener(event -> {
            String string = aaaaaaaaa.EULERS_NUMBER;
            if(stringJComboBox1.getSelectedItem().equals(STRINGS2[2])) {
                string = box1.getText();
            }
            if(field.getText().isEmpty() && comboBox.getSelectedItem().equals(STRINGS[2])) {
                int integer = JOptionPane.showConfirmDialog(this,
                        "The decryption key will be randomized. Continue?", "Warning", JOptionPane.YES_NO_OPTION);
                if(integer != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            boolean bool = !comboBox.getSelectedItem().equals(STRINGS[2]);
            byte longg=2;
            if(comboBox1.getSelectedItem().equals(STRINGS1[3])) {
                longg=0;
            }
            else if(comboBox1.getSelectedItem().equals(STRINGS1[2])) {
                longg=1;
            }
            if(!bool) {
                longg=0;
            }
            if(box.getSelectedItem().equals(EEEEEE[0])) {
                aaaaaaaaaa.aaaaaaaaaaaaaaaaaaaaaaaaaa(1);
            }
            else if(box.getSelectedItem().equals(EEEEEE[1])) {
                aaaaaaaaaa.aaaaaaaaaaaaaaaaaaaaaaaaaa(1);
            }
            else {
                aaaaaaaaaa.aaaaaaaaaaaaaaaaaaaaaaaaaa(Integer.parseInt((String) box.getSelectedItem()));
            }
            if(aaaaaaaaaa.getPi() != 0) {
                aaaaaaaaaa.aaaaaaaaaaaaaaaaaaaaaaaaaa(0);
                box1.setText("");
                field.setText("");
            }
            aaaaaaaaaa("Loading input...");
            doSomething("");
            thingy = new Succ(this, bool, longg, string);
            thingy.execute();
        });
        gridBagConstraints.anchor=LAST_LINE_END;
        gridBagConstraints.gridx=3;
        gridBagConstraints.gridy=0;
        F.add(jButton, gridBagConstraints);
        bagLayout = null;
        gridBagConstraints = null;
        box = new JComboBox<String>(EEEEEE);
        box.setBackground(LOGIN_BACKGROUND_COLOR);
        box.setEditable(false);
        box.setFont(EXTRA_SMALL_FONT);
        box.addActionListener(event -> {
            if(box.getSelectedItem().equals(EEEEEE[1])) {

                String string1 = (String) JOptionPane.showInputDialog(this,
                        "Enter number of threads to use\n\n Example: if your computer has 4 cores,\nuse 4 threads to take advantage of all your cores",
                        "Custom thread input", JOptionPane.QUESTION_MESSAGE, null, null, null);
                if(string1 == null) {
                    box.setSelectedItem(EEEEEE[0]);
                    return;
                }
                if(string1.isEmpty()) {
                    box.setSelectedItem(EEEEEE[0]);
                    JOptionPane.showMessageDialog(this, "The input cannot be empty",
                            aaaaaaaaa.ZZZZZZZ, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    int SIXTEEN = (int)Float.parseFloat(string1);
                    if(SIXTEEN<=0 || SIXTEEN> aaaaaaaaa.aaaaaaaaaaaaaaaa) {
                        throw new NumberFormatException();
                    }
                    if(SIXTEEN==1) {
                        box.setSelectedIndex(0);
                        return;
                    }
                    for(int ONE = 2; ONE< box.getItemCount(); ONE++) {
                        if(Integer.parseInt(box.getItemAt(ONE)) == SIXTEEN) {
                            box.setSelectedItem(String.valueOf(SIXTEEN));
                            return;
                        }
                        else if(Integer.parseInt(box.getItemAt(ONE)) > SIXTEEN) {

                            box.insertItemAt(String.valueOf(SIXTEEN), ONE);
                            box.setSelectedItem(String.valueOf(SIXTEEN));
                            return;
                        }
                    }
                    box.insertItemAt(String.valueOf(SIXTEEN), box.getItemCount());
                    box.setSelectedItem(String.valueOf(SIXTEEN));

                }
                catch(NumberFormatException e) {
                    box.setSelectedItem(EEEEEE[0]);
                    JOptionPane.showMessageDialog(this, "The input must be an integer between 1 and " + aaaaaaaaa.aaaaaaaaaaaaaaaa,
                            aaaaaaaaa.ZZZZZZZ, JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            return;
        });
        constraints.gridx=1;
        constraints.gridy=3;
        panel.add(box, constraints);

        return panel;
    }

    public void aaaaaaaaaa(String a) {
        label.setText(a);
    }

    public String aaaaaaaaaaa() {
        return field.getText();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            aaaaaaa NULL = new aaaaaaa();
        });
    }
}