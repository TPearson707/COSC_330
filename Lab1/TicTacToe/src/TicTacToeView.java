// TicTacToeView.java - View Component

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class TicTacToeView extends JFrame {
    
    // Constants
    private static final char INITIAL_VALUE = 'x';
    
    // Components
    private TicTacToe m_model;

    // Constructor
    /*
     * Pass in model
     * model is same instance because singleton
     * set initial values of the game
     * have controller update for the runtime
     */
    TicTacToeView(TicTacToe model) {
        // set up logic here
        m_model = model;
        m_model.setLetter(INITIAL_VALUE);
    }
};
