package org.typist.engine;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import org.typist.beans.*;
import org.typist.utils.*;

public class TypistUI extends JFrame implements ActionListener {
    /* This is responsible for the UI. Plese do some improvements on the look
     * and feel. I hate swing. Most of the code in this class is collected from
     * the net (yes, I admit but for this class ONLY) if time permits add a
     * third JTextArea (or whatever) vertically
     * along the right side of the screen. This should display the result of
     * the user. There will be a timer (which starts when one clicks on "Start"
     * (and stops when relevant)). Put this timer above or below this vertical
     * JTextArea. - That was my idea... but couldnt implement it dut to lac of
     * interest in swing :)... Supriya please throw some light / help here...
     * Other useful things like scrollbars may be added in addition
     */
    private SourceTextBean stb;

    JButton startButton;
    JButton stopButton;
    JTextArea sourceText; // upper text box with source text
    JTextPane destText;   // lower text box with user input
    AbstractDocument doc;

    public TypistUI(SourceTextBean stb) {

        this.stb = stb;

        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Typist");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Container pane = frame.getContentPane();
 
        //Set up the content pane.
        addComponentsToPane(pane);
        
        //get the document object of destination text box
        StyledDocument styledDoc = destText.getStyledDocument();
        doc = (AbstractDocument)styledDoc;
        doc.addDocumentListener(new DestDocumentListener());
 
        // add a menu (just for fun)
        addMenuBarToFrame(frame);

        frame.setSize(900, 800);
        frame.setVisible(true);
    }

    private void addMenuBarToFrame(JFrame frame) {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu appMenu = new JMenu("Application");
        JMenu aboutMenu = new JMenu("About");
        
        JMenuItem appMenuItemMan, appMenuItemExit;
        JMenuItem aboutMenuItemAbout, aboutMenuItemVer;

        appMenuItemMan = new JMenuItem("User Manual", KeyEvent.VK_T);
        appMenuItemExit = new JMenuItem("Exit", KeyEvent.VK_T);
        appMenu.add(appMenuItemMan);
        appMenu.add(appMenuItemExit);

        aboutMenuItemAbout = new JMenuItem("About Typist", KeyEvent.VK_T);
        aboutMenuItemVer = new JMenuItem("Version", KeyEvent.VK_T);
        aboutMenu.add(aboutMenuItemAbout);
        aboutMenu.add(aboutMenuItemVer);

        menuBar.add(appMenu);
        menuBar.add(aboutMenu);
        frame.setJMenuBar(menuBar);
    }

    public void addComponentsToPane(Container pane) {
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gBC = new GridBagConstraints();
        
        // build the source text component
        sourceText = new JTextArea(stb.getText(), 20, 50);
        //gBC.ipady = 300;
        gBC.gridx = 0;
        gBC.gridy = 0;
        gBC.weighty = 0.1;
        gBC.weightx = 0.1;
        sourceText.setEditable(false);
        sourceText.setLineWrap(true);
        JScrollPane scrollPaneForSource = new JScrollPane(sourceText);

        // build the dest text component
        destText = new JTextPane();
        gBC.gridx = 0;
        gBC.gridy = 85;
        destText.setCaretPosition(0);
        destText.setMargin(new Insets(5,5,5,5));
        destText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(destText);
        scrollPane.setPreferredSize(new Dimension(700, 350));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                                    scrollPaneForSource, scrollPane);
        splitPane.setOneTouchExpandable(true);

        // build the start button
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        startButton.setToolTipText("Start the timer and start typing.");
        startButton.setActionCommand("start");
        //gBC.gridx = 0;
        //gBC.gridy = 50;

       // add components to pane
        pane.add(splitPane);
        pane.add(startButton);
        // pane.add(stopButton);
    }
    
    public void actionPerformed(ActionEvent ae) {
        if ("start".equals(ae.getActionCommand())) {
            destText.setEnabled(true);
            destText.setEditable(true);
            StatsBean.setStartTime();
            startButton.setEnabled(false);
            destText.requestFocusInWindow();
        }
    }

    //And this one listens for any changes to the destination document.
    //Change validation and display is triggered by spacebar.
    protected class DestDocumentListener
                    implements DocumentListener {
       /* Below var checks if space was added from kbd, we validate, else if
        * space was added from program, do nothing. This variable makes sure
        * that the validation and display code does not run when we enter a
        * space programatically (if we ignore this, then we may go into an
        * infinite loop as every space entered from the program triggers this
        * code an in effect a space is appended, which again...
        */
        private boolean spaceAddedFromKbd = true;

        public void insertUpdate(DocumentEvent e) {
            //Any and all validations should be done ONLY when inserting text.
            Document destDoc = e.getDocument();
            int docLength = destDoc.getLength();
            char lastChar; // last input char

            if ((docLength > 0) && spaceAddedFromKbd) {
                try {
                    lastChar = destDoc.getText(0, docLength).charAt(docLength - 1);
                    //If the last input was a space only then should we validate, 
                    //else, do nothing
                    if (lastChar == ' ') {
                        validateAndDisplayDestText(destDoc);
                    }
                } catch (BadLocationException ble) {
                    ble.printStackTrace();
                }
            } else {
                spaceAddedFromKbd = true;
                return;
            }
        }
        public void removeUpdate(DocumentEvent e) {
//            validateDisplayDestText(e);
        }
        public void changedUpdate(DocumentEvent e) {
//            validateDisplayDestText(e);
        }
        
        private void validateAndDisplayDestText(final Document destDoc) {
            /* This is where all the action is. This method checks whether the
             * last entered word matches with the word in the same position in
             * source textbox, if not then the last word is erased (removed)
             * and rewritten in red. A black space is appended at the end to
             * discontinue the red color. The code here is in a seperate thread
             * because we canot simultaneously write in a JTextPane (the
             * document associated with the JTextPane, to be more specific) and
             * update the same JTextPane, this causes an IllegalStateException.
             * Hence the "invokeLater".
             */
            final SimpleAttributeSet[] errAttr = getErrAttribute();

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        final int docLength = destDoc.getLength();
                        final String docText = destDoc.getText(0, docLength);
                        final String lastWord; 
                        final int lastWordOffset; // The index.
                        final int lastWordPos; // The position of the word
                                               // like 3rd word or 12th 
                                               // word (NOT array index).

                        lastWord = GeneralUtils.getLastWord(docText);
                        // validate last word here; if ok, simple insert
                        // else insert in red color
                        lastWordOffset = GeneralUtils.getLastWordOffset(docText);
                        lastWordPos = GeneralUtils.getLastWordPos(docText);
                       if (TextValidator.isValidText(lastWord.trim(), lastWordPos)) {

                            // Case OK(correct word): remove last word and rewrite with
                            // black. This is done to handle situations where the user
                            // might do backspace and update the word correctly.
                            destDoc.remove(lastWordOffset,
                                           docLength - lastWordOffset);
                            spaceAddedFromKbd = false;
                            destDoc.insertString(destDoc.getLength(),
                                                lastWord + " ",
                                                errAttr[1]);
                        } else {
                            // Case err: remove last word from document and insert
                            // the same word in red color and a trailing space in
                            // black.

                            destDoc.remove(lastWordOffset,
                                           docLength - lastWordOffset);

                            destDoc.insertString(destDoc.getLength(),
                                                lastWord,
                                                errAttr[0]);
                            spaceAddedFromKbd = false;
                            destDoc.insertString(destDoc.getLength(),
                                                " ",
                                                errAttr[1]);
                            // next line shows if we are looping infinitely
                            System.out.println("running");
                       }
                       if (TextValidator.userIsDone(docText)) {
                            spaceAddedFromKbd = false;
                            destDoc.insertString(destDoc.getLength(),
                                    GeneralUtils.getStats(),
                                    errAttr[2]);
                            destText.setEditable(false);
                            return;
                       }
                } catch (Exception e) {e.printStackTrace();};
            }});
        }
    
        private SimpleAttributeSet[] getErrAttribute() {
            // These are sort of style templates that we can apply to some text
            SimpleAttributeSet[] errAttr = new SimpleAttributeSet[3];

            errAttr[0] = new SimpleAttributeSet();
            StyleConstants.setForeground(errAttr[0], Color.red);

            errAttr[1] = new SimpleAttributeSet();
            StyleConstants.setForeground(errAttr[1], Color.black);

            errAttr[2] = new SimpleAttributeSet();
            StyleConstants.setForeground(errAttr[2], Color.blue);

            return errAttr;
        }
    }
}
