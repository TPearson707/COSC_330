import java.util.Scanner; // Impot Scanner class
import java.util.HashMap; // Import HashMap

public class Main {
    public static void main(String[] args) {
        char letter = 'x'; // x by default
        int turnCounter = 1; // start at 1 so if turn count is odd it's x turn else it's o turn
        int x, y;
        TicTacToe game;

        game = TicTacToe.getInstance();
        
        Scanner stdin = new Scanner(System.in);

        System.out.println("Welcome to Tic Tac Toe!");

        while (!game.checkWin()) {

            if (turnCounter % 2 == 0) {
                letter = 'o';
            } else {
                letter = 'x';
            }

            System.out.println("Starting " + letter + "'s turn.");

            System.out.print("Enter row 1-3: ");
            x = stdin.nextInt();

            System.out.print("Enter column 1-3: ");
            y = stdin.nextInt();

            if (game.setBoard(letter, x - 1, y - 1)) {
                System.out.println("Good Choice " + letter + "!");
                game.printBoard();

                turnCounter++;

            } else {
                System.out.println("That spot is taken!");
                continue;
            }

            if (game.checkDraw()) {
                System.out.println("The game is a draw!");
                break;
            }
        } 

        if (game.checkWin()) {
            System.out.println("Congratulations " + letter + ", you won!");
        }

        stdin.close();
    }
}
