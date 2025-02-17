// TicTacToeView.java - View Component

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
    private JFrame frame;
    private JPanel panel;
    private JTextPane textPane;
    private JButton[][] buttons = new JButton[3][3];
    private Font customFont = new Font("Segoe UI", Font.BOLD, 30); // Initial font
    // Constructor
    /*
     * Pass in model
     * model is same instance because singleton
     * set initial values of the game
     * have controller update for the runtime
     */
    TicTacToeView(TicTacToe model) {
        // set up logic here
        game = model;
        game.setLetter(INITIAL_VALUE);

        // set up frame
        frame = new JFrame("Tic Tac Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

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

        textPane.setText("Welcome to Tic Tac Toe!");

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
                JButton button = new JButton(); // Create new button
                button.setForeground(Color.WHITE); // White text color 
                button.setBackground(new Color(0x3A4C6B)); // Soft blue-gray 
                button.setOpaque(true); // Make sure background color is visible
                button.setBorderPainted(false); // Remove button borders clean look
                button.setPreferredSize(new Dimension(50, 50)); // Set fixed preferred size
                buttons[row][col] = button; // add button to buttons array

                // Add action listener to each button
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (button.getText().equals("")) {

                    

                            button.setText(Character.toString(game.getLetter()));
                            game.setLetter(game.getLetter() == 'x' ? 'o' : 'x');
                            textPane.setText("Player " + game.getLetter() + "'s turn");
                        }

                        // Check full (for testing)
                        /* Boolean full = false;
                        for (int row = 0; row < 3; row++) {
                            for (int col = 0; col < 3; col++) {
                                if (buttons[row][col] != null && buttons[row][col].getText().equals("")) {
                                    full = false;
                                    break;
                                } else {
                                    full = true;

                                }
                            }
                        }

                        if (full) {
                            textPane.setText("Game Over");
                            
                            for (int row = 0; row < 3; row++) {
                                for (int col = 0; col < 3; col++) {
                                    buttons[row][col].setEnabled(false);
                                }
                            }
                        }
                            }
                        }); */

                button.setEnabled(false); // Disable button initially

                // Set grid position
                gbc.gridx = col;
                gbc.gridy = row + 1; // offset by 1 to make room for text area
                panel.add(buttons[row][col], gbc);
            }
        }

        // Dynamically set button font size
        setButtonFontSize(BUTTON_SIZE);

        // Add panel to frame
        frame.add(panel);
        frame.setVisible(true);

        // Use a Swing Timer to enable buttons after a delay
        Timer timer = new Timer(1500, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // Enable all buttons
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        buttons[row][col].setEnabled(true);
                    }
                }
                // Update the text pane
                textPane.setText("Player " + game.getLetter() + "'s turn");
            }
        });
        timer.setRepeats(false); // Ensure the timer only runs once
        timer.start(); // Start the timer
    }

    // Method to dynamically change button font size
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
                buttons[row][col].repaint(); // Repaint the button
            }
        }
    }
};
