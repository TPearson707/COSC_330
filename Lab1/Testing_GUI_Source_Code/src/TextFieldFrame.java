package src;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class TextFieldFrame extends JFrame{
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JPasswordField passwordField;

    // TextFieldFrame constructor adds JTextFields to JFrame
    public TextFieldFrame() {
        super("Testing JTextField and JPasswordField");
        setLayout(new FlowLayout());

        // construct textfield with 10 columns
        textField1 = new JTextField(10);
        add(textField1); // add textField1 to JFrame

        // construct textfield with default text
        textField2 = new JTextField("Enter text here");
        add(textField2); // add textField2 to JFrame

        // construct textfield with default text and 21 columns
        textField3 = new JTextField("Uneditable text field", 21);
        textField3.setEditable(false); // disable editing
        add(textField3); // add textField3 to JFrame

        // construct passwordfield with default text
        passwordField = new JPasswordField("Hidden text");
        add(passwordField); // add passwordField to JFrame

        // create and register event handlers
        TextFieldHandler handler = new TextFieldHandler();

        // create and register event handlers
        textField1.addActionListener(handler);
        textField2.addActionListener(handler);
        textField3.addActionListener(handler);
        passwordField.addActionListener(handler);
    } // end TextFieldFrame constructor

    private class TextFieldHandler implements ActionListener {
        // process textfield events
        public void actionPerformed(ActionEvent event) {
            String string =""; // declare string to display

            // user pressed Enter in JTextField textField1
            if (event.getSource() == textField1) {
                string = String.format("textField1: %s", event.getActionCommand());
            }

            // user pressed Enter in JTextField textField2
            else if (event.getSource() == textField2) {
                string = String.format("textField2: %s", event.getActionCommand());
            }

            // user pressed Enter in JTextField textField3
            else if (event.getSource() == textField3) {
                string = String.format("textField3: %s", event.getActionCommand());
            }

            // user pressed Enter in JTextField passwordField
            else if (event.getSource() == passwordField) {
                string = String.format("passwordField: %s", event.getActionCommand());
            }

            // display JTextField content
            JOptionPane.showMessageDialog(null, string);
        } // end method actionPerformed
    } // end class TextFieldHandler
} // end class TextFieldFrame




