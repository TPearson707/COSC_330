public class TicTacToe {

    // private static instance of the same class
    private static TicTacToe instance = null;
    private char[][] board;
    private int rows = 3;
    private int columns = 3;
    private char letter = 'x';

    // private constructor to prevent instantiation
    private TicTacToe() {
        board = new char[3][3];

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

    public boolean setBoard(int x, int y) {
        if (board[x][y] == '-') {
            board[x][y] = letter;
            return true;
        }

        return false;

    }

    public void setLetter(char l) {
        letter = l;
    }

    public char getLetter() {
        return letter;
    }

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

    public void printBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
