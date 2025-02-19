import javax.swing.*;

// Represents a TicTacToe button in the GUI : Subject
public class TicTacToeButton extends JButton implements Observer {
    private int row;
    private int col;
    private TicTacToe game;

    public TicTacToeButton(int row, int col, TicTacToe model) {
        this.row = row;
        this.col = col;
        game = model;
        
        // Register button as observer of the game model
        game.addObserver(this);

        // Set up button click behavior
        addActionListener(e -> game.makeMove(row,col));
    }

    @Override
    public void update(int row, int col, char player) {
        // Update button text based on game state
        if (this.row == row && this.col == col) {
            setText(Character.toString(player));
            game.switchPlayer();
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
