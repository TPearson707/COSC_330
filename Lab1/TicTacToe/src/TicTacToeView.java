import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.event.*;

class TicTacToeView extends JFrame {
    
    // Constants
    private static final char INITIAL_VALUE = 'x';
    private static final int BUTTON_SIZE = 60;

    // Components
    private TicTacToe game;
    private JPanel panel;
    private JTextPane textPane;
    private JButton[][] buttons = new JButton[3][3];
    private Font customFont = new Font("Segoe UI", Font.BOLD, 30); // Initial font
    private TicTacToeController controller;  // Reference to the controller

    // Constructor for the view
    TicTacToeView(TicTacToe model) {
        // Set up frame
        super("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);

        // set up logic here
        game = model;
        game.setPlayer(INITIAL_VALUE);
        this.controller = new TicTacToeController(this, game);  // Pass the controller to the view

        // set up panel
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(0x2E3B4E)); // Dark blue-gray
        GridBagConstraints gbc = new GridBagConstraints();

        // Configure constraints
        gbc.insets = new Insets(5, 5, 5, 5); // padding around components
        gbc.fill = GridBagConstraints.BOTH; // Make buttons expand
        gbc.weightx = 1.0; // Allow horizontal stretching

        // Add Text Pane
        textPane = new JTextPane(); 
        textPane.setEditable(false);
        textPane.setFont(customFont);
        textPane.setForeground(Color.WHITE);
        textPane.setBackground(new Color(0x1D2B36));

        // Center the text in the JTextPane
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        updateTextPane("Welcome to Tic Tac Toe!");

        // Set preferred size for the JTextPane
        textPane.setPreferredSize(new Dimension(0, 50)); // Width: 0 (ignored), Height: 50 pixels

        // Add text area to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Use the whole row
        panel.add(textPane, gbc);

        // Reset gridwidth for buttons
        gbc.gridwidth = 1;

        gbc.weighty = 1.0; // Allow vertical stretching

        // Initialize buttons
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col] = new TicTacToeButton(row, col); // Create buttons[row][col] with coordinates
                buttons[row][col].setForeground(Color.WHITE); // White text color 
                buttons[row][col].setBackground(new Color(0x3A4C6B)); // Soft blue-gray 
                buttons[row][col].setOpaque(true); // Make sure background color is visible
                buttons[row][col].setBorderPainted(false); // Remove buttons[row][col] borders clean look
                buttons[row][col].setPreferredSize(new Dimension(50, 50)); // Set fixed preferred size
                buttons[row][col].setEnabled(true); // Disable buttons[row][col] initially

                // Set grid position
                gbc.gridx = col;
                gbc.gridy = row + 1; // offset by 1 to make room for text area
                panel.add(buttons[row][col], gbc);
            }
        }

        controller.addActionListenersToButton(buttons); // Add action listeners to buttons

        // Dynamically set buttons[row][col] font size
        setButtonFontSize(BUTTON_SIZE);

        // Add panel to frame
        add(panel);
        setVisible(true);

        // Use a Swing Timer to enable buttons after a delay
        Timer timer = new Timer(1500, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Enable all buttons
                setButtonsEditable(true);
                // Update the text pane
                textPane.setText("Player " + game.getPlayer() + "'s turn");
            }
        });
        timer.setRepeats(false); // Ensure the timer only runs once
        timer.start(); // Start the timer
    }

    // Method to update the text displayed in the JTextPane
    public void updateTextPane(String text) {
        textPane.setText(text);  // Update the text displayed in the JTextPane
    }

    // Method to update the text displayed on a button
    public void updateButtonText(int row, int col, char text) {
        buttons[row][col].setText(Character.toString(text));  // Update the text displayed on the button
    }

    public void addButtonListener(ActionListener listener) {
        // This method is now for adding the listener in the controller
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].addActionListener(listener);  // Register listeners for buttons
            }
        }
    }

    // Method to dynamically change buttons[row][col] font size
    public void setButtonFontSize(int newSize) {
        customFont = new Font("Segoe UI", Font.BOLD, newSize); // Update the font with the new size
        
        // Apply the new font size to all buttons
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].setFont(customFont); // Set new font size
            }
        }

        // Revalidate the buttons to make sure changes are applied
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].revalidate(); // Revalidate layout
                buttons[row][col].repaint(); // Repaint the buttons[row][col]
            }
        }
    }

    // method to set a all buttons editable or not
    public void setButtonsEditable(boolean editable) {

        if (game == null) {
            return;
        }

        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getColumns(); j++) {
                buttons[i][j].setEnabled(editable);
            }
        }
    }

    // method to set a specific button[row][col] to be editable or not
    public void setButtonEditable(int row, int col, boolean editable) {
        buttons[row][col].setEnabled(editable);
    }
};
