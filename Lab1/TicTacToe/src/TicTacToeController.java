import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
public class TicTacToeController {
    
    private TicTacToeView view;
    private TicTacToe game;

    public TicTacToeController(TicTacToeView view, TicTacToe game) {
        this.view = view;
        this.game = game;
    }

    // This is the method that handles the button click from the view
    public void handleButtonClick(int row, int col) {
        char currentPlayer = game.getPlayer();

        if (game.makeMove(row, col)) {
            view.updateButtonText(row, col, currentPlayer); // Update button text
            view.setButtonEditable(row, col, false); // Disable the button

            // Check for win or draw after move
            if (game.checkWin()) {
                view.updateTextPane("Player " + currentPlayer + " wins!");
                view.setButtonsEditable(false); // Disable all buttons
            } else if (game.checkDraw()) {
                view.updateTextPane("The game is a draw!");
                view.setButtonsEditable(false); // Disable all buttons
            } else {
                game.switchPlayer();
                view.updateTextPane("Player " + game.getPlayer() + "'s turn"); // Update turn info
            }
        }
    }

    // ActionListener for the buttons, moved to the controller
    public class ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof TicTacToeButton) {
                TicTacToeButton button = (TicTacToeButton) e.getSource();
                int row = button.getRow();
                int col = button.getCol();

                // Delegate the button click handling to the controller
                handleButtonClick(row, col);
            }
        }
    }

    // Add action listeners to all buttons
    public void addActionListenersToButton(JButton[][] buttons) {
        ButtonActionListener listener = new ButtonActionListener();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                buttons[row][col].addActionListener(listener);
            }
        }
    }

    public void setView(TicTacToeView view) {
        this.view = view;
    }
}
