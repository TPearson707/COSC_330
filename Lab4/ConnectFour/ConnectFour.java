import java.util.Scanner;

class ConnectFour {
    /* Constants */
    private final int rows = 6;
    private final int columns = 7;

    /* Data members */
    private char[][] grid;
    private int winner;
    private boolean turn;
    private boolean replay;
    private int xPos;
    private int yPos;
    private static ConnectFour instance = null;

    /* Constructors */
    private ConnectFour() {
        winner = 0; // no one wins
        turn = false; // start player 1
        replay = false; // default do not replay
        xPos = 0;
        yPos = 0;

        grid = new char[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                grid[i][j] = ' ';
            }
        }
    }

    /* Methods */
    public static ConnectFour getInstance() {
        if (instance == null) {
            instance = new ConnectFour();
        }

        return instance;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        do {
            // Reset game state
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    grid[i][j] = ' ';
                }
            }
            winner = 0;
            turn = false;

            boolean gameWon = false;
            int totalMoves = 0;

            while (!gameWon && totalMoves < rows * columns) {
                printBoard();
                System.out.print("\nPlayer " + (turn ? "2 (O)" : "1 (X)") + ", enter a column (1-7): ");

                int col;
                try {
                    col = Integer.parseInt(scanner.nextLine()) - 1;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 7.");
                    continue;
                }

                if (col < 0 || col >= columns) {
                    System.out.println("Invalid column. Try again.");
                    continue;
                }

                int row = getAvailableRow(col);
                if (row == -1) {
                    System.out.println("That column is full. Try a different one.");
                    continue;
                }

                char playerChar = turn ? 'O' : 'X';
                grid[row][col] = playerChar;
                setXPos(col);
                setYPos(row);
                totalMoves++;

                if (checkWinCondition(playerChar)) {
                    printBoard();
                    System.out.println("\nPlayer " + (turn ? "2 (O)" : "1 (X)") + " wins!");
                    gameWon = true;
                } else {
                    switchTurn();
                }
            }

            if (!gameWon) {
                printBoard();
                System.out.println("\nIt's a tie!");
            }

            askReplay();

        } while (replay);

        scanner.close();
    }


    private void askReplay() {
        Scanner stdin = new Scanner(System.in);
        System.out.print("Do you want to play again? (y/N): ");

        String answer = stdin.nextLine().trim().toLowerCase();

        if (answer.equals("y") || answer.equals("yes")) {
            replay = true;
        } else {
            replay = false;
        }
    }

    private boolean checkWinCondition(char player) {
        // Horizontal check
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col <= columns - 4; col++) {
                if (grid[row][col] == player &&
                    grid[row][col + 1] == player &&
                    grid[row][col + 2] == player &&
                    grid[row][col + 3] == player) {
                    winner = player == 'X' ? 1 : 2;
                    return true;
                }
            }
        }

        // Vertical check
        for (int row = 0; row <= rows - 4; row++) {
            for (int col = 0; col < columns; col++) {
                if (grid[row][col] == player &&
                    grid[row + 1][col] == player &&
                    grid[row + 2][col] == player &&
                    grid[row + 3][col] == player) {
                    winner = player == 'X' ? 1 : 2;
                    return true;
                }
            }
        }

        // Diagonal down-right
        for (int row = 0; row <= rows - 4; row++) {
            for (int col = 0; col <= columns - 4; col++) {
                if (grid[row][col] == player &&
                    grid[row + 1][col + 1] == player &&
                    grid[row + 2][col + 2] == player &&
                    grid[row + 3][col + 3] == player) {
                    winner = player == 'X' ? 1 : 2;
                    return true;
                }
            }
        }

        // Diagonal up-right
        for (int row = 3; row < rows; row++) {
            for (int col = 0; col <= columns - 4; col++) {
                if (grid[row][col] == player &&
                    grid[row - 1][col + 1] == player &&
                    grid[row - 2][col + 2] == player &&
                    grid[row - 3][col + 3] == player) {
                    winner = player == 'X' ? 1 : 2;
                    return true;
                }
            }
        }

        return false;
    }

    /* Method to switch turn */
    private void switchTurn() {
        turn = !turn;
    }

    /* Setters */
    private void setXPos(int xPos) {
        this.xPos = xPos;
    }

    private void setYPos(int yPos) {
        this.yPos = yPos;
    }

    /* Getters */
    private int getXPos() {
        return xPos;
    }

    private int getYPos() {
        return yPos;
    }

    private int getAvailableRow(int col) {
        for (int row = 0; row < rows; row++) {
            if (grid[row][col] == ' ') {
                return row;
            }
        }
        return -1; // column is full
    }

    private void printBoard() {
        for (int row = 5; row >= 0; row--) {
            for (int col = 0; col < 7; col++) {
                System.out.print(" " + grid[row][col]);
                if (col < 6) {
                    System.out.print(" |");
                }
            }
            System.out.println();
        }

        System.out.println("---------------------------");
        System.out.println(" 1 | 2 | 3 | 4 | 5 | 6 | 7");
    }

    private void skipTwoLines() {
        System.out.println("\n");
    }




}
