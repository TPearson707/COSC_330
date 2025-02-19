/* Purpose: Interface for Observer pattern.
 * Observer pattern is used to update the view of the game when the model changes.
 * This interface is implemented by the TicTacToeButton class.
 * The update method is called by the model when a move is made.
 * The method updates the button text based on the game state.
 * The button is disabled after a move is made.
 * The row and column of the button are passed to the update method.
 * The player character is also passed to the update method.
 * The TicTacToeButton class is registered as an observer of the game model.
 * The TicTacToeButton class implements the ActionListener interface to handle button clicks.
 * The actionPerformed method calls the makeMove method of the game model.
 * The TicTacToeButton class extends the JButton class to represent a TicTacToe button in the GUI.
 * The TicTacToeButton class has fields for the row, column, and game model.
 */    

public interface Observer {
    void update(int row, int col, char player);
}
