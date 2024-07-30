import java.awt.*;
import javax.swing.*;

import static java.awt.GridBagConstraints.*;

/**
 * Class to display and run an instance of StepperApp. Serves as the main application class.<br><br>
 *
 * Uses a StepperAppFields and StepperFunctions instance to hold its constants and variables not related to the GUI,
 * along with methods to operate on them.<br>
 * Contains a main method. The only class in this project permitted to have a main method.<br>
 *
 * Most of the work occurs in creating the various screens. The screen creation methods are called in the constructor.<br>
 *
 * Please follow the rules listed in "rules.txt". Failure to do so means I will track you down and [REDACTED]<br><br>
 *
 * Version 1.1 was completed on June 28, 2024.
 * Version 2.0 with enhanced encryption was completed on July 18, 2024.
 */
public class StepperApp extends JFrame {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //CONSTANTS: arranged in alphabetical order by datatype

    /**
     * Color to contrast with the background colors
     */
    final private static Color ACCENT_COLOR = new Color(180,180,180);

    /**
     * Background for the user input and results screen. Also used in some buttons, i.e. the processing screen's cancel button<br><br>
     * Simulates "Go Away Green".
     */
    final private static Color INPUT_BACKGROUND_COLOR = new Color(60,100,60);

    /**
     * Background for the login screen and some GUI components (i.e. JComboBoxes)
     */
    final private static Color LOGIN_BACKGROUND_COLOR = new Color(160,160,160);


    /**
     * Width and height of the window
     */
    final private static Dimension APP_DIMENSIONS = new Dimension(1000,500);


    /**
     * Font mostly for screen headers
     */
    final private static Font LARGE_FONT = new Font("Trebuchet MS", Font.PLAIN, 45);

    /**
     * Font mostly for section headers
     */
    final private static Font MEDIUM_FONT = new Font("Trebuchet MS", Font.PLAIN, 20);

    /**
     * Font mostly for text and button labels
     */
    final private static Font SMALL_FONT = new Font("Trebuchet MS", Font.PLAIN, 15);

    /**
     * Font mostly for small GUI components
     */
    final private static Font EXTRA_SMALL_FONT = new Font("Trebuchet MS", Font.BOLD, 10);


    /**
     * All possible options for the input mode
     */
    final private static String[] INPUT_SELECTION_OPTIONS = new String[] {"Select input source", "Text", "File"};

    /**
     * All possible options for the operation mode
     */
    final private static String[] OPERATION_SELECTION_OPTIONS = new String[] {"Select mode", "Encrypt", "Decrypt"};

    /**
     * All possible options for the punctuation mode
     */
    final private static String[] PUNCTUATION_SELECTION_OPTIONS = new String[] {"Select punctuation preferences",
            "Remove Punctuation", "Remove Spaces", "Include All Punctuation"};

    /**
     * Default options for the number of threads to use.<br><br>
     *
     * The first index should be interpreted as one thread. The second allows the user to enter a custom value.<br>
     *
     * Selecting the second option allows the user to add a value to the JComboBox constructed around this array
     * to add custom values to its selection options.<br><br>
     *
     * All default thread selection options (except for the first two indices), when Integer.parseInt is called on them,
     * must return a value on the interval [1, StepperFunctions.MAX_THREADS].
     */
    final private static String[] THREAD_SELECTION_OPTIONS = new String[] {"Select number of threads (default: 1)", "Custom...",
            "2", "4", "6", "8", "10", "12", "16", "20", "24", "32", "48", "64"};


    /**
     * Names of all possible screens
     */
    final private static String[] VALID_SCREEN_NAMES = new String[] {"LOGIN", "LOGIN_REJECT", "INPUT", "PROCESSING", "RESULTS"};


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //VARIABLE NON-GUI FIELDS


    /**
     * Contains non-GUI related fields. Can't be null
     */
    final private StepperAppFields fields;


    /**
     * A thread to start execution of the user input.<br><br>
     *
     * May be null until input processing begins. After processing finishes, this field may become null again.
     */
    private ParsingDispatcher executionDispatchThread;


    ///////////////////
    //GUI JCOMPONENTS (CONTAINERS)

    /**
     * Container for the GUI components
     */
    final private Container contentPane;

    /**
     * Holds the screens.
     * Any Cards (screens) inside must have a name in VALID_SCREEN_NAMES and cannot have the same name as any other screen.
     */
    final private CardLayout screens;



    ///////////////////
    //GUI JCOMPONENTS (USER INTERFACE COMPONENTS): Arranged in alphabetical order by JComponent type, then by variable name

    /**
     * Triggers an action
     */
    private JButton enterButton;

    /**
     * Triggers additional functions when a second button is required
     */
    private JButton utilityButton;


    /**
     * Allows the user to select input mode (text input or file). User selects from one of INPUT_SELECTION_OPTIONS
     */
    private JComboBox<String> inputModeSelector;

    /**
     * Allows the user to select an operation. User selects from one of OPERATION_SELECTION_OPTIONS
     */
    private JComboBox<String> operationModeSelector;

    /**
     * Allows the user to select punctuation settings for the output. User selects from one of PUNCTUATION_SELECTION_OPTIONS
     */
    private JComboBox<String> punctSelector;

    /**
     * Allows the user to select number of threads in the operation.<br><br>
     *
     * User selects from one of THREAD_SELECTION_OPTIONS.<br>
     * If the "Custom" option is chosen, Strings may be added. Any Strings, after calling the Integer.parseInt method on them,
     * must evaluate to an integer on the interval [1, 999].<br>
     * If a non-integer is selected, the value of the Selector should be changed to "1" just before processing.
     */
    private JComboBox<String> threadCountSelector;


    /**
     * Loading text
     */
    private JLabel loadingStatusText;

    /**
     * Header for the current screen
     */
    private JLabel textHeader;

    /**
     * Label for the top text input. Also serves as the label for the password input.
     */
    private JLabel textInputTopLabel;

    /**
     * Label for the bottom text input, if it exists
     */
    private JLabel textInputBottomLabel;


    /**
     * Password input on the login screen
     */
    private JPasswordField passwordInput;


    /**
     * Holds the output key. Recommended to not make this text area editable.
     */
    private JTextArea outputKey;

    /**
     * Holds the output text. Recommended to not make this text area editable.
     */
    private JTextArea outputText;


    /**
     * First user text input
     */
    private JTextField textInputTop;

    /**
     * Second user text input.<br><br>
     * If the current screen contains one text input, this field should not appear on the GUI.<br>
     * Should always appear below textInputTop.
     */
    private JTextField textInputBottom;


    //ADDITIONAL FIELDS (IF NECESSARY)

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //CONSTRUCTOR

    /**
     * Creates a new StepperApp and initializes its fields
     */
    public StepperApp() {
        //Configure field classes
        fields = new StepperAppFields();

        //Idiot check: all thread count choices in the default selection must be on the interval [1, MAX_THREADS]
        for(int i=2; i<THREAD_SELECTION_OPTIONS.length; i++) {
            try {
                int currentValue = Integer.parseInt(THREAD_SELECTION_OPTIONS[i]);
                if(currentValue<1 || currentValue>StepperAppFields.MAX_THREADS) {
                    throw new AssertionError("All default thread selection options except for the first two must represent integers"
                    + " on the interval [1, " + StepperAppFields.MAX_THREADS + "]");
                }
            }
            catch(NumberFormatException e) {
                throw new AssertionError("All default thread selection options except for the first two must represent integers");
            }
        }

        //Configure the CardLayout
        screens = new CardLayout();
        contentPane = getContentPane();
        contentPane.setLayout(screens);

        //Create and add all the screens to the CardLayout
        addToLayout(contentPane, makeLoginScreen(), "LOGIN");
        addToLayout(contentPane, makeLoginRejectScreen(), "LOGIN_REJECT");
        addToLayout(contentPane, makeUserInputScreen(), "INPUT");
        addToLayout(contentPane, makeProcessingScreen(), "PROCESSING");
        addToLayout(contentPane, makeResultsScreen(), "RESULTS");

        //Show the login screen
        setScreen("LOGIN");

        //Initialize the app window
        setTitle("Stepper 2 App");
        setSize(APP_DIMENSIONS);
        setFocusable(true);
        requestFocus();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);

        //Run the garbage collector
        System.gc();
    }

    /**
     * Adds `screen` to `pane` and assigns it the name `screenName`.<br><br>
     * If `screenName` doesn't appear in the VALID_SCREEN_NAMES array, throws IllegalArgumentException.<br>
     * If any arguments are null, throws IllegalArgumentException.<br>
     *
     * This method is necessary to prevent silent errors if the screen names have typos.<br><br>
     *
     * Helper to the class constructor. Should only be called from the constructor.
     *
     * @param pane Container to add the new screen to
     * @param screen JPanel representing the screen to be added
     * @param screenName name of the new screen
     * @throws IllegalArgumentException if `screenName` is not in VALID_SCREEN_NAMES or any arguments are null
     */
    private void addToLayout(Container pane, JPanel screen, String screenName) {
        //Null check
        if(pane==null || screen==null) {
            throw new IllegalArgumentException("The input container and screen cannot be null");
        }
        if(screenName==null) {
            throw new IllegalArgumentException("The screen name cannot be null");
        }

        //Look through VALID_SCREEN_NAMES. If a match is found, change the screen to `screenName`
        for(int i=0; i<VALID_SCREEN_NAMES.length; i++) {
            if(VALID_SCREEN_NAMES[i].equals(screenName)) {
                contentPane.add(screen,screenName);
                return;
            }
        }

        //Not returning by now means `screenName` is not in VALID_SCREEN_NAMES, so the method throws an exception
        throw new IllegalArgumentException("The screen name \"" + screenName + "\" is not in the VALID_SCREEN_NAMES array");
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //UTILITIES

    /**
     * Returns the app's fields
     *
     * @return reference to (not a copy of) the app's fields
     */
    public StepperAppFields fields() {
        return fields;
    }


    /**
     * Returns the current contents of the top text input
     * @return contents of `textInputTop`
     */
    public String topInputValue() {
        return textInputTop.getText();
    }


    /**
     * Sets the text and key output to hold `text` and `key`, respectively
     *
     * @param text new text to be in the output
     * @param key new key to be in the output
     */
    public void setOutput(String text, String key) {
        outputText.setText(text);
        outputKey.setText(key);
    }


    /**
     * Sets the loading status text to `newText`.
     *
     * @param newText what to display on the loading text
     */
    public void setLoadingStatusText(String newText) {
        loadingStatusText.setText(newText);
    }


    /**
     * Sets the GUI to display the screen named `screenName`.<br>
     * If `screenName` is null or not in VALID_SCREEN_NAMES, throws IllegalArgumentException.
     *
     * @throws IllegalArgumentException if `screenName` is null or not in the VALID_SCREEN_NAMES array. Gives a descriptive error message
     */
    public void setScreen(String screenName) {
        //Null check
        if(screenName==null) {
            throw new IllegalArgumentException("Screen name cannot be null");
        }

        //Check all the possible screen names
        for(int i=0; i<VALID_SCREEN_NAMES.length; i++) {
            //If a valid screen name is found, set the screen
            if(VALID_SCREEN_NAMES[i].equals(screenName)) {
                screens.show(contentPane, screenName);
                System.gc();
                return;
            }
        }

        throw new IllegalArgumentException("The screen name \"" + screenName + "\" is not in the VALID_SCREEN_NAMES array");
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //CARD LAYOUT SETUP METHODS: Should be private and called only in the constructor.


    /**
     * Returns a JPanel containing the App's JComponent fields arranged into the login screen.<br><br>
     *
     * This method, ideally, does not create any new JComponents. It should prioritize using JComponents that exist as fields in this class.<br>
     * Any newly created field should not be affected by user input (i.e. a JLabel or a JPanel used as a spacer).<br><br>
     *
     * Call from constructor only.
     *
     * @return JPanel containing the login screen
     */
    private JPanel makeLoginScreen() {
        JPanel panel = new JPanel();

        //Configure the layout
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(layout);

        //Configure the panel
        panel.setBackground(LOGIN_BACKGROUND_COLOR);

        //Header
        textHeader = new JLabel("Hello");
        textHeader.setFont(LARGE_FONT);
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=0;
        panel.add(textHeader, constraints);

        //Spacer
        JPanel headerVerticalSpacer = new JPanel();
        headerVerticalSpacer.setBackground(LOGIN_BACKGROUND_COLOR);
        headerVerticalSpacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/30.0)));
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=1;
        panel.add(headerVerticalSpacer, constraints);

        //Password input label
        textInputTopLabel = new JLabel("Enter password");
        textInputTopLabel.setFont(MEDIUM_FONT);
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=2;
        panel.add(textInputTopLabel, constraints);

        //Password input
        passwordInput = new JPasswordField();
        passwordInput.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/15.0)));
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=3;
        panel.add(passwordInput, constraints);

        //Spacer
        JPanel pwVerticalSpacer = new JPanel();
        pwVerticalSpacer.setBackground(LOGIN_BACKGROUND_COLOR);
        pwVerticalSpacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/20.0)));
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=4;
        panel.add(pwVerticalSpacer, constraints);

        //Enter button
        enterButton = new JButton("Enter");
        enterButton.setBackground(ACCENT_COLOR);
        enterButton.setFont(SMALL_FONT);
        enterButton.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/10.0), (int) (APP_DIMENSIONS.height/12.0)));
        enterButton.addActionListener(e -> {
            fields.setLoginCredentials(fields.login(passwordInput.getPassword()));

            if(fields.loginCredentials()==-1) {
                setScreen("LOGIN_REJECT");
            }
            else {
                passwordInput.setText("");
                setScreen("INPUT");
            }
        });
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=5;
        panel.add(enterButton, constraints);

        return panel;
    }

    /**
     * Returns a JPanel containing the App's JComponent fields arranged into the login rejection screen.<br><br>
     *
     * This method, ideally, does not create any new JComponents. It should prioritize using JComponents that exist as fields in this class.<br>
     * Any newly created field should not be affected by user input (i.e. a JLabel or a JPanel used as a spacer).<br><br>
     *
     * Call from constructor only.
     *
     * @return JPanel containing the login rejection screen
     */
    private JPanel makeLoginRejectScreen() {
        JPanel panel = new JPanel();

        //Configure the layout
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(layout);

        //Configure the panel
        panel.setBackground(LOGIN_BACKGROUND_COLOR);


        //"Incorrect password" text
        textHeader = new JLabel("Incorrect password");
        textHeader.setFont(MEDIUM_FONT);
        constraints.gridx=0;
        constraints.gridy=0;
        panel.add(textHeader, constraints);

        //Spacer
        JPanel spacer = new JPanel();
        spacer.setBackground(LOGIN_BACKGROUND_COLOR);
        spacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/15.0)));
        constraints.gridx=0;
        constraints.gridy=1;
        panel.add(spacer, constraints);

        //Exit app button
        enterButton = new JButton("Close app");
        enterButton.setBackground(ACCENT_COLOR);
        enterButton.setFont(SMALL_FONT);
        enterButton.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/8.0), (int) (APP_DIMENSIONS.height/12.0)));
        enterButton.addActionListener(l -> {
            System.exit(0);
        });
        constraints.gridx=0;
        constraints.gridy=2;
        panel.add(enterButton, constraints);


        return panel;
    }


    /**
     * Returns a JPanel containing the App's JComponent fields arranged into the user input screen.<br><br>
     *
     * This method, ideally, does not create any new JComponents. It should prioritize using JComponents that exist as fields in this class.<br>
     * Any newly created field should not be affected by user input (i.e. a JLabel or a JPanel used as a spacer).<br><br>
     *
     * Call from constructor only.
     *
     * @return JPanel containing the user input screen
     */
    private JPanel makeUserInputScreen() {
        JPanel panel = new JPanel();

        //Configure the layout
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(layout);

        //Configure the panel
        panel.setBackground(INPUT_BACKGROUND_COLOR);


        //Screen header
        textHeader = new JLabel("Stepper App v2.4");
        textHeader.setFont(LARGE_FONT);
        constraints.gridx=0;
        constraints.gridy=0;
        panel.add(textHeader, constraints);

        //Spacer
        JPanel textHeaderSpacer = new JPanel();
        textHeaderSpacer.setBackground(INPUT_BACKGROUND_COLOR);
        textHeaderSpacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/8.0)));
        constraints.gridx=0;
        constraints.gridy=1;
        panel.add(textHeaderSpacer, constraints);

        //Top input label
        textInputTopLabel = new JLabel("Plaintext");
        textInputTopLabel.setFont(MEDIUM_FONT);
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=2;
        panel.add(textInputTopLabel, constraints);

        //Top text input
        textInputTop = new JTextField();
        textInputTop.setBackground(ACCENT_COLOR);
        textInputTop.setFont(SMALL_FONT);
        textInputTop.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/1.5), (int) (APP_DIMENSIONS.height/15.0)));
        constraints.gridx=0;
        constraints.gridy=3;
        panel.add(textInputTop, constraints);

        //Selector area- create and position
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


        //////////
        //Configure selector area
        //Doesn't work if items don't fit in the selector area

        //Spacer
        JPanel selectorLabelSpacer = new JPanel();
        selectorLabelSpacer.setBackground(INPUT_BACKGROUND_COLOR);
        selectorLabelSpacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/40.0), (int) (APP_DIMENSIONS.height/15.0)));
        selectorAreaConstraints.gridx=0;
        selectorAreaConstraints.gridy=0;
        selectorArea.add(selectorLabelSpacer, selectorAreaConstraints);

        //Input mode selector (inside the selector area)
        inputModeSelector = new JComboBox<>(INPUT_SELECTION_OPTIONS);
        inputModeSelector.setBackground(LOGIN_BACKGROUND_COLOR);
        inputModeSelector.setEditable(false);
        inputModeSelector.setFont(EXTRA_SMALL_FONT);
        inputModeSelector.addActionListener(e -> {
            //Change text of the top input text
            if(inputModeSelector.getSelectedItem().equals(INPUT_SELECTION_OPTIONS[2])) {
                textInputTopLabel.setText("Absolute path to input text (*.txt) file");
            }
            else {
                if(operationModeSelector.getSelectedItem().equals(OPERATION_SELECTION_OPTIONS[2])) {
                    textInputTopLabel.setText("Ciphertext");
                }
                else {
                    textInputTopLabel.setText("Plaintext");
                }
            }
        });
        selectorAreaConstraints.anchor=FIRST_LINE_START;
        selectorAreaConstraints.gridx=1;
        selectorAreaConstraints.gridy=0;
        selectorArea.add(inputModeSelector, selectorAreaConstraints);

        //Spacer
        JPanel inputModeSelectorSpacer = new JPanel();
        inputModeSelectorSpacer.setBackground(INPUT_BACKGROUND_COLOR);
        inputModeSelectorSpacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/40.0), (int) (APP_DIMENSIONS.height/15.0)));
        selectorAreaConstraints.gridx=2;
        selectorAreaConstraints.gridy=0;
        selectorArea.add(inputModeSelectorSpacer, selectorAreaConstraints);

        //Operation mode selector (inside the selector area)
        operationModeSelector = new JComboBox<>(OPERATION_SELECTION_OPTIONS);
        operationModeSelector.setBackground(LOGIN_BACKGROUND_COLOR);
        operationModeSelector.setEditable(false);
        operationModeSelector.setFont(EXTRA_SMALL_FONT);
        operationModeSelector.addActionListener(e -> {
            //Change text on the Enter button
            if(operationModeSelector.getSelectedItem().equals(OPERATION_SELECTION_OPTIONS[2])) {
                enterButton.setText("Decrypt");
                punctSelector.setEnabled(false);
                if(inputModeSelector.getSelectedItem().equals(INPUT_SELECTION_OPTIONS[2])==false) {
                    textInputTopLabel.setText("Ciphertext");
                }
            }
            else {
                enterButton.setText("Encrypt");
                punctSelector.setEnabled(true);
                if(inputModeSelector.getSelectedItem().equals(INPUT_SELECTION_OPTIONS[2])==false) {
                    textInputTopLabel.setText("Plaintext");
                }
            }
        });
        selectorAreaConstraints.anchor=FIRST_LINE_START;
        selectorAreaConstraints.gridx=3;
        selectorAreaConstraints.gridy=0;
        selectorArea.add(operationModeSelector, selectorAreaConstraints);

        //Spacer
        JPanel operationModeSelectorSpacer = new JPanel();
        operationModeSelectorSpacer.setBackground(INPUT_BACKGROUND_COLOR);
        operationModeSelectorSpacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/40.0), (int) (APP_DIMENSIONS.height/15.0)));
        selectorAreaConstraints.gridx=4;
        selectorAreaConstraints.gridy=0;
        selectorArea.add(operationModeSelectorSpacer, selectorAreaConstraints);

        //Punct selector (inside the selector area)
        punctSelector = new JComboBox<>(PUNCTUATION_SELECTION_OPTIONS);
        punctSelector.setBackground(LOGIN_BACKGROUND_COLOR);
        punctSelector.setEditable(false);
        punctSelector.setFont(EXTRA_SMALL_FONT);
        selectorAreaConstraints.anchor=FIRST_LINE_START;
        selectorAreaConstraints.gridx=5;
        selectorAreaConstraints.gridy=0;
        selectorArea.add(punctSelector, selectorAreaConstraints);

        //Spacer
        JPanel punctSelectorSpacer = new JPanel();
        punctSelectorSpacer.setBackground(INPUT_BACKGROUND_COLOR);
        punctSelectorSpacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/7.0), (int) (APP_DIMENSIONS.height/15.0)));
        selectorAreaConstraints.anchor=FIRST_LINE_START;
        selectorAreaConstraints.gridx=6;
        selectorAreaConstraints.gridy=0;
        selectorArea.add(punctSelectorSpacer, selectorAreaConstraints);

        //Make all the panel-specific setup utilities null so they can't be used again
        selectorAreaLayout=null;
        selectorAreaConstraints=null;

        //////////


        //Spacer
        JPanel selectorAreaSpacer = new JPanel();
        selectorAreaSpacer.setBackground(INPUT_BACKGROUND_COLOR);
        selectorAreaSpacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/20.0)));
        constraints.gridx=0;
        constraints.gridy=5;
        panel.add(selectorAreaSpacer, constraints);

        //Bottom input label
        textInputBottomLabel = new JLabel("Key (leave blank for random key)");
        textInputBottomLabel.setFont(MEDIUM_FONT);
        constraints.gridx=0;
        constraints.gridy=6;
        panel.add(textInputBottomLabel, constraints);

        //Bottom input
        textInputBottom = new JTextField();
        textInputBottom.setBackground(ACCENT_COLOR);
        textInputBottom.setFont(SMALL_FONT);
        textInputBottom.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/1.5), (int) (APP_DIMENSIONS.height/15.0)));
        constraints.gridx=0;
        constraints.gridy=7;
        panel.add(textInputBottom, constraints);

        //Spacer
        JPanel textInputBottomSpacer = new JPanel();
        textInputBottomSpacer.setBackground(INPUT_BACKGROUND_COLOR);
        textInputBottomSpacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/15.0)));
        constraints.gridx=0;
        constraints.gridy=8;
        panel.add(textInputBottomSpacer, constraints);

        //Enter button area
        JPanel enterButtonArea = new JPanel();
        enterButtonArea.setBackground(INPUT_BACKGROUND_COLOR);
        enterButtonArea.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/1.5), (int) (APP_DIMENSIONS.height/12.0)));
        constraints.gridx=0;
        constraints.gridy=9;
        //Configure enter button area constraints
        GridBagLayout enterAreaLayout = new GridBagLayout();
        GridBagConstraints enterAreaConstraints = new GridBagConstraints();
        enterButtonArea.setLayout(enterAreaLayout);
        panel.add(enterButtonArea, constraints);


        ///////////////
        //Configure enter button area


        //Spacer
        JPanel enterButtonAreaSpacer = new JPanel();
        enterButtonAreaSpacer.setBackground(INPUT_BACKGROUND_COLOR);
        enterButtonAreaSpacer.setPreferredSize(new Dimension((int)(APP_DIMENSIONS.width/8.0), 25));
        enterAreaConstraints.anchor=CENTER;
        enterAreaConstraints.gridx=0;
        enterAreaConstraints.gridy=0;
        enterButtonArea.add(enterButtonAreaSpacer, enterAreaConstraints);

        //Return to login button
        utilityButton = new JButton("Return to login");
        utilityButton.setBackground(LOGIN_BACKGROUND_COLOR);
        utilityButton.setFont(MEDIUM_FONT);
        utilityButton.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/5.0), (int) (APP_DIMENSIONS.height/16.0)));
        utilityButton.addActionListener(e -> {
            fields.setLoginCredentials((byte)127);
            setScreen("LOGIN");
        });
        enterAreaConstraints.anchor=LAST_LINE_END;
        enterAreaConstraints.gridx=1;
        enterAreaConstraints.gridy=0;
        enterButtonArea.add(utilityButton, enterAreaConstraints);

        //Spacer
        JPanel utilityButtonSpacer = new JPanel();
        utilityButtonSpacer.setBackground(INPUT_BACKGROUND_COLOR);
        utilityButtonSpacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/30.0), (int) (APP_DIMENSIONS.height/16.0)));
        enterAreaConstraints.anchor=LAST_LINE_END;
        enterAreaConstraints.gridx=2;
        enterAreaConstraints.gridy=0;
        enterButtonArea.add(utilityButtonSpacer, enterAreaConstraints);

        //Enter button
        enterButton = new JButton("Encrypt");
        enterButton.setBackground(LOGIN_BACKGROUND_COLOR);
        enterButton.setFont(MEDIUM_FONT);
        enterButton.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/8.0), (int) (APP_DIMENSIONS.height/16.0)));
        enterButton.addActionListener(event -> {

            //Set input preferences. Text loading is done in the Boss thread
            String filename = StepperAppFields.TEXT_LOAD_SIGNAL;
            if(inputModeSelector.getSelectedItem().equals(INPUT_SELECTION_OPTIONS[2])) {
                filename = textInputTop.getText();

                //Bug fix
                if(filename.equals(StepperAppFields.TEXT_LOAD_SIGNAL)) {
                    filename = "";
                }
            }

            //Load key
            fields.setKey(textInputBottom.getText());
            //Check the key if it's empty and decryption is selected
            if(fields.key().length()<=0 && operationModeSelector.getSelectedItem().equals(OPERATION_SELECTION_OPTIONS[2])) {
                //Ask user to continue and take the user's choice
                int choice = JOptionPane.showConfirmDialog(this,
                        "The decryption key will be randomized. Continue?", "Input warning", JOptionPane.YES_NO_OPTION);

                //If user doesn't want to continue, stop
                if(choice != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            //Set the operation mode based on the user's selection
            boolean encrypting = !operationModeSelector.getSelectedItem().equals(OPERATION_SELECTION_OPTIONS[2]);

            //Set the punctuation mode based on selection. 0=leave as-is, 1=no spaces, 2=no punctuation
            byte punctMode=2;
            if(punctSelector.getSelectedItem().equals(PUNCTUATION_SELECTION_OPTIONS[3])) {
                punctMode=0;
            }
            else if(punctSelector.getSelectedItem().equals(PUNCTUATION_SELECTION_OPTIONS[2])) {
                punctMode=1;
            }
            //Set punct mode to include everything if decrypting
            if(!encrypting) {
                punctMode=0;
            }


            //Set number of threads

            //"Select threads": set to default
            if(threadCountSelector.getSelectedItem().equals(THREAD_SELECTION_OPTIONS[0])) {
                fields.setThreadCount(1);
            }
            //"Custom": should not happen
            else if(threadCountSelector.getSelectedItem().equals(THREAD_SELECTION_OPTIONS[1])) {
                //This option should make a dialog box prompting the user for a number.
                fields.setThreadCount(1); //just in case
            }
            //Anything else: use the selector's chosen option
            else {
                fields.setThreadCount(Integer.parseInt((String) threadCountSelector.getSelectedItem()));
            }

            //Yeet the threads
            if(fields.loginCredentials() != 0) {
                fields.setThreadCount(0);
                fields.setText("");
                fields.setKey("");
            }

            //Reset the progress
            setLoadingStatusText("Loading input...");

            //Make the main thread
            executionDispatchThread = new ParsingDispatcher(this, encrypting, punctMode, filename);

            //Start parsing the string. Text loading is done in the Boss thread
            executionDispatchThread.execute();
        });
        enterAreaConstraints.anchor=LAST_LINE_END;
        enterAreaConstraints.gridx=3;
        enterAreaConstraints.gridy=0;
        enterButtonArea.add(enterButton, enterAreaConstraints);


        enterAreaLayout = null;
        enterAreaConstraints = null;
        //////////////


        //Thread count
        threadCountSelector = new JComboBox<String>(THREAD_SELECTION_OPTIONS);
        threadCountSelector.setBackground(LOGIN_BACKGROUND_COLOR);
        threadCountSelector.setEditable(false);
        threadCountSelector.setFont(EXTRA_SMALL_FONT);
        threadCountSelector.addActionListener(event -> {

            //Ask user for number of threads
            if(threadCountSelector.getSelectedItem().equals(THREAD_SELECTION_OPTIONS[1])) {

                String customInput = (String) JOptionPane.showInputDialog(this,
                        "Enter number of threads to use\n\n Example: if your computer has 4 cores,\nuse 4 threads to take advantage of all your cores",
                        "Custom thread input", JOptionPane.QUESTION_MESSAGE, null, null, null);

                //Dialog box was closed: reset the selector and do nothing
                if(customInput == null) {
                    threadCountSelector.setSelectedItem(THREAD_SELECTION_OPTIONS[0]);
                    return;
                }

                //Empty input: display error and stop looking
                if(customInput.equals("")) {
                    threadCountSelector.setSelectedItem(THREAD_SELECTION_OPTIONS[0]);
                    JOptionPane.showMessageDialog(this, "The input cannot be empty",
                            "Invalid input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //Take input. If not a valid integer, error message
                try {
                    fields.setThreadCount(Integer.parseInt(customInput));

                    //For some reason the setThreadCount method allows 0 as an acceptable input
                    if(Integer.parseInt(customInput)==0) {
                        throw new IllegalArgumentException();
                    }

                    //Add the new value to the box
                    threadCountSelector.insertItemAt(customInput, 1);
                    threadCountSelector.setSelectedItem(customInput);
                }
                catch(IllegalArgumentException e) {
                    threadCountSelector.setSelectedItem(THREAD_SELECTION_OPTIONS[0]);
                    JOptionPane.showMessageDialog(this, "The input must be an integer between 1 and " + StepperAppFields.MAX_THREADS,
                            "Invalid input", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

        });
        constraints.gridx=1;
        constraints.gridy=3;
        panel.add(threadCountSelector, constraints);

        return panel;
    }


    /**
     * Returns a JPanel containing the App's JComponent fields arranged into the input processing screen.<br><br>
     *
     * This method, ideally, does not create any new JComponents. It should prioritize using JComponents that exist as fields in this class.<br>
     * Any newly created field should not be affected by user input (i.e. a JLabel or a JPanel used as a spacer).<br><br>
     *
     * Call from constructor only.
     *
     * @return JPanel containing the input processing screen
     */
    private JPanel makeProcessingScreen() {
        JPanel panel = new JPanel();
        panel.setBackground(INPUT_BACKGROUND_COLOR);

        //Configure the layout
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(layout);

        //"Loading input" text
//        textHeader = new JLabel("Loading input...");
//        textHeader.setFont(MEDIUM_FONT);
//        constraints.gridx=0;
//        constraints.gridy=0;
//        panel.add(textHeader, constraints);

        //Loading text
        loadingStatusText = new JLabel();
        loadingStatusText.setBackground(ACCENT_COLOR);
        loadingStatusText.setFont(MEDIUM_FONT);
        constraints.gridx=0;
        constraints.gridy=0;
        panel.add(loadingStatusText, constraints);

        //Spacer
        JPanel textHeaderSpacer = new JPanel();
        textHeaderSpacer.setBackground(INPUT_BACKGROUND_COLOR);
        textHeaderSpacer.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/2.0), (int) (APP_DIMENSIONS.height/16.0)));
        constraints.gridx=0;
        constraints.gridy=2;
        panel.add(textHeaderSpacer, constraints);

        //Cancel button: uses the utility button because the enter button keeps its text from the user input screen
        utilityButton = new JButton("Cancel");
        utilityButton.setBackground(INPUT_BACKGROUND_COLOR);
        utilityButton.setFont(MEDIUM_FONT);
        utilityButton.setPreferredSize(new Dimension((int) (APP_DIMENSIONS.width/8.0), (int) (APP_DIMENSIONS.height/16.0)));
        utilityButton.addActionListener(e -> {
            executionDispatchThread.cancel(true);
            //The thread handles the screen change.
        });
        constraints.gridx=0;
        constraints.gridy=3;
        panel.add(utilityButton, constraints);

        return panel;
    }


    /**
     * Returns a JPanel containing the App's JComponent fields arranged into the input processing screen.<br><br>
     *
     * This method, ideally, does not create any new JComponents. It should prioritize using JComponents that exist as fields in this class.<br>
     * Any newly created field should not be affected by user input (i.e. a JLabel or a JPanel used as a spacer).<br><br>
     *
     * Call from constructor only.
     *
     * @return JPanel containing the input processing screen
     */
    private JPanel makeResultsScreen() {
        JPanel panel = new JPanel();
        panel.setBackground(INPUT_BACKGROUND_COLOR);

        //Configure the layout
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(layout);

        //Screen header
        textHeader = new JLabel("Output");
        textHeader.setFont(MEDIUM_FONT);
        constraints.gridx=0;
        constraints.gridy=0;
        panel.add(textHeader, constraints);

        //Output header. Not a field because of bugs
        JLabel outputHeader = new JLabel("Text");
        outputHeader.setFont(SMALL_FONT);
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=1;
        panel.add(outputHeader, constraints);

        //Output
        outputText = new JTextArea(10, 50); //DO NOT SET PREFERRED SIZE! SET ROWS AND COLUMNS INSTEAD!!
        outputText.setBackground(INPUT_BACKGROUND_COLOR);
        outputText.setEditable(false);
        outputText.setFont(SMALL_FONT);
        outputText.setLineWrap(true);
        //The ParsingBoss will load the text area
        JScrollPane textScroll = new JScrollPane(outputText);
        textScroll.setBackground(INPUT_BACKGROUND_COLOR);
        textScroll.setSize(new Dimension(10, 40));
        textScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        constraints.anchor=CENTER;
        constraints.gridx=0;
        constraints.gridy=2;
        panel.add(textScroll, constraints);

        //Spacer
        JPanel outputTextSpacer = new JPanel();
        outputTextSpacer.setBackground(INPUT_BACKGROUND_COLOR);
        outputTextSpacer.setPreferredSize(new Dimension(APP_DIMENSIONS.width/4, APP_DIMENSIONS.height/16));
        constraints.gridx=0;
        constraints.gridy=3;
        panel.add(outputTextSpacer, constraints);

        //Key header
        JLabel keyHeader = new JLabel("Key");
        keyHeader.setFont(SMALL_FONT);
        constraints.anchor=FIRST_LINE_START;
        constraints.gridx=0;
        constraints.gridy=4;
        panel.add(keyHeader, constraints);

        //Key
        outputKey = new JTextArea(2, 50); //DO NOT SET PREFERRED SIZE! SET ROWS AND COLUMNS INSTEAD!!
        outputKey.setBackground(INPUT_BACKGROUND_COLOR);
        outputKey.setEditable(false);
        outputKey.setFont(SMALL_FONT);
        outputKey.setLineWrap(true);
        //The ParsingBoss will load the text area
        JScrollPane keyScroll = new JScrollPane(outputKey);
        keyScroll.setBackground(INPUT_BACKGROUND_COLOR);
        keyScroll.setSize(new Dimension(10, 40));
        keyScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        constraints.anchor=CENTER;
        constraints.gridx=0;
        constraints.gridy=5;
        panel.add(keyScroll, constraints);

        //Return to login button. Uses the utility button because the enter button causes problems
        utilityButton = new JButton("Return to login");
        utilityButton.setBackground(INPUT_BACKGROUND_COLOR);
        utilityButton.setFont(SMALL_FONT);
        utilityButton.addActionListener(e -> {
            outputText.setText("");
            outputKey.setText("");
            textInputTop.setText("");
            textInputBottom.setText("");
            fields.setText("");
            fields.setKey("");
            executionDispatchThread = null;
            setScreen("LOGIN");
        });
        constraints.gridx=0;
        constraints.gridy=6;
        panel.add(utilityButton, constraints);

        return panel;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //MAIN

    /**
     * Creates a StepperApp and runs it.
     * @param args program arguments (likely not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StepperApp app = new StepperApp();
        });
    }
}