public class TicTacToe {

    // private static instance of the same class
    private static TicTacToe instance = null;
    private char[][] board;
    private int rows;
    private int columns;
    private char currentPlayer;

    // private constructor to prevent instantiation
    private TicTacToe() {
        rows = 3;
        columns = 3;
        currentPlayer = 'x';
        board = new char[rows][columns];

        // Initialize the board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }

    // Public method to provide access to the instance
    public static TicTacToe getInstance() {
        if (instance == null) {
            instance = new TicTacToe();
        }

        return instance;
    }

    /* SETTERS */

    // function to update board
    public boolean makeMove(int x, int y) {
        if (board[x][y] == '-') {
            board[x][y] = currentPlayer;

            return true;
        }

        return false;

    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 'x') ? 'o' : 'x';
    }

    // Function to set the current currentPlayer
    public void setPlayer(char l) {
        currentPlayer = l;
    }

    // Function to set the rows
    public void setRows(int r) {
        rows = r;
    }

    // Function to set the columns
    public void setColumns(int c) {
        columns = c;
    }

    /* END SETTERS */

    /* GETTERS */

    // Function to get the current board
    public char[][] getBoard() {
        return board;
    }

    // Function to get the current currentPlayer
    public char getPlayer() {
        return currentPlayer;
    }

    // Function to get the rows
    public int getRows() {
        return rows;
    }

    // Function to get the rows
    public int getColumns() {
        return columns;
    }

    /* END GETTERS */

    /* GAME LOGIC */

    // Function to check if the game has been won
    public boolean checkWin() {

        // Check rows for a win
        for (int i = 0; i < rows; i++) {
            if (board[i][0] != '-' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
        }

        // Check columns for a win
        for (int j = 0; j < columns; j++) {
            if (board[0][j] != '-' && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return true;
            }
        }

        // Check the first diagonal for a win
        if (board[0][0] != '-' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return true;
        }

        // Check the second diagonal for a win
        if (board[0][2] != '-' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return true;
        }

        // no winner
        return false;
    }

    // Function to check if the game resulted in a draw
    public boolean checkDraw() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                if (board[i][j] == '-') {
                    // Found an empty spot, not a draw
                    return false;
                }
            }
        }

        // No empty spots and no winner means it's a draw
        return true;
    }

    /* END GAME LOGIC */

    /* UTILITY FUNCTIONS FOR TERMINAL USE */

    // Function to return the current board as a string for terminal use
    public String stringifyBoard() {
        String str = "";

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                str += (board[i][j] + " ");
            }
            str += "\n";
        }

        return str;
    }

    // Function to print out the board
    public void printBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    /* END UTILITY FUNCTIONS FOR TERMINAL USE */
} // End class TicTacToe
