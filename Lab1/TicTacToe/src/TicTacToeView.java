// TicTacToeView.java - View Component

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class TicTacToeView extends JFrame {
    
    // Constants
    private static final char INITIAL_VALUE = 'x';
    
    // Components
    private TicTacToe game;
    private JFrame frame;
    private JPanel panel;
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
        frame.setSize(400, 400);

        // set up panel
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Configure constraints
        gbc.insets = new Insets(5, 5, 5, 5); // padding around components
        gbc.fill = GridBagConstraints.BOTH; // Make buttons expand
        gbc.weightx = 1.0; // Allow horizontal stretching
        gbc.weighty = 1.0; // Allow vertical stretching

        // Add Buttons
        JButton[][] buttons = new JButton[3][3];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton button = new JButton(); // Create new button
                button.setFont(new Font("Arial", Font.BOLD, 40));
                button.setFocusPainted(false); // Remove focus border
                button.setPreferredSize(new Dimension(100, 100)); // Set fixed preferred size
                buttons[row][col] = button; // add button to buttons array

                // Add action listener to each button
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (button.getText().equals("")) {

                            if (game.getLetter() == 'x') {
                                button.setForeground(Color.RED);
                            } else {
                                button.setForeground(Color.GREEN);
                            }

                            button.setText(Character.toString(game.getLetter()));
                            game.setLetter(game.getLetter() == 'x' ? 'o' : 'x');
                        }
                    }
                });

                // Set grid position
                gbc.gridx = col;
                gbc.gridy = row;
                panel.add(buttons[row][col], gbc);
            }
        }

        // Add panel to frame
        frame.add(panel);
        frame.setVisible(true);
    }
};
