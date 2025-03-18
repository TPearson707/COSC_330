import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Server extends JFrame {
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
    private int counter = 1;
    private String[][] boardData;
    private BattleshipFrame frame;
    private Model model;
    private boolean isMyTurn;

    public Server() {
        super("Server");
        isMyTurn = true;
    }

    public void runServer() {
        try {
            server = new ServerSocket(12345, 100);
            while (true) {
                try {
                    waitForConnection();
                    getStreams();
                    processConnection();
                } catch (EOFException eofException) {
                    displayMessage("\nServer terminated connection");
                } finally {
                    closeConnection();
                    counter++;
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void waitForConnection() throws IOException {
        displayMessage("Waiting for connection\n");
        connection = server.accept();
        displayMessage("Connection " + counter + " received from: " +
                connection.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        displayMessage("\nGot I/O streams\n");
    }

    private void processConnection() throws IOException {
        String message = "Connection successful";
        sendData(message);

        do {
            try {
                message = (String) input.readObject();
                displayMessage("\n" + message);

                if (message.equals("BOARD_DATA")) {
                    try {
                        isMyTurn = true;
                        if (isMyTurn) {
                            SwingUtilities.invokeLater(() -> {
                                frame.getTargetPanel().setTargetButtonsEnabled(true);
                            });
                        }

                        String[][] receivedBoard = (String[][]) input.readObject();
                        setClientBoard(receivedBoard);
                        sendData("BOARD_RECEIVED");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                if (message.startsWith("ATTACK ")) {
                    try {
                        String[] parts = message.split(" ");
                        if (parts.length != 3) {
                            System.err.println("Invalid ATTACK message: " + message);
                            return;
                        }

                        int x = Integer.parseInt(parts[1]);
                        int y = Integer.parseInt(parts[2]);

                        AttackResult result = model.performAttack(1, x, y);
                        boolean gameOver = model.isGameOver();

                        if (gameOver) {
                            // Send GAME_OVER message to client
                            sendData("GAME_OVER SERVER");
                        } else {
                            // Send ATTACK_RESULT message to client
                            String resultMsg = String.format("ATTACK_RESULT %d %d %s %b",
                                    x, y, result.serialize(), gameOver);
                            sendData(resultMsg);
                        }

                        SwingUtilities.invokeLater(() -> {
                            frame.getOceanPanel().updateOcean(x, y, result.isHit());

                            if (result.isHit()) {
                                updateMessage("Opponent hit your ship at (" + x + ", " + y + ")!");
                            } else {
                                updateMessage("Opponent missed at (" + x + ", " + y + ").");
                            }
                            if (result.isSunk()) {
                                updateMessage(result.toString());
                            }
                            if (gameOver) {
                                updateMessage("Game Over! You Win!");
                                frame.getTargetPanel().setTargetButtonsEnabled(false);
                            } else {
                                // Send TURN message to client
                                sendData("TURN SERVER");
                            }
                        });
                    } catch (Exception e) {
                        System.err.println("Error processing attack: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else if (message.startsWith("ATTACK_RESULT ")) {
                    String[] parts = message.split(" ", 5);
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    AttackResult result = AttackResult.deserialize(parts[3]);
                    boolean gameOver = Boolean.parseBoolean(parts[4]);

                    SwingUtilities.invokeLater(() -> {
                        model.getPlayer(1).getBoard().attackCell(x, y);
                        frame.getTargetPanel().updateResult(x, y, result);

                        if (result.isHit()) {
                            updateMessage("You hit a ship at (" + x + ", " + y + ")!");
                        } else {
                            updateMessage("You missed at (" + x + ", " + y + ").");
                        }
                        if (result.isSunk()) {
                            updateMessage(result.toString());
                        }
                        if (gameOver) {
                            updateMessage("Game Over! You lose!");
                            frame.getTargetPanel().setTargetButtonsEnabled(false);
                        } else {
                            // Send TURN message to client
                            sendData("TURN CLIENT");
                        }
                    });
                } else if (message.startsWith("GAME_OVER ")) {
                    String winner = message.split(" ")[1];
                    SwingUtilities.invokeLater(() -> {
                        updateMessage("Game Over! " + winner + " wins!");
                        frame.getTargetPanel().setTargetButtonsEnabled(false);
                    });
                } else if (message.startsWith("TURN ")) {
                    String turn = message.split(" ")[1];
                    isMyTurn = turn.equals("SERVER");

                    SwingUtilities.invokeLater(() -> {
                        frame.getTargetPanel().setTargetButtonsEnabled(isMyTurn);
                        if (isMyTurn) {
                            updateMessage("Your turn!");
                        } else {
                            updateMessage("Opponent's turn...");
                        }
                    });
                }
            } catch (ClassNotFoundException | IOException e) {
                displayMessage("\nError reading from client: " + e.getMessage());
                break;
            }
        } while (!message.equals("CLIENT>>> TERMINATE"));
    }

    public synchronized void setModel(Model model) {
        this.model = model;
    }

    public synchronized void setFrame(BattleshipFrame frame) {
        this.frame = frame;
    }

    public synchronized void setClientBoard(String[][] board) {
        this.boardData = board;
        notifyAll();
    }

    public synchronized String[][] getClientBoard() {
        return this.boardData;
    }

    private void closeConnection() {
        displayMessage("\nTerminating connection\n");
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void sendData(String message) {
        try {
            output.writeObject(message);
            output.flush();
            displayMessage("\nSERVER>>> " + message);
        } catch (IOException ioException) {
            displayMessage("\nError writing object: " + ioException.getMessage());
        }
    }

    public void sendServerBoard(String[][] board) {
        try {
            output.writeObject("BOARD_DATA");
            output.writeObject(board);
            output.flush();
            displayMessage("\nSent board data to client");
        } catch (IOException ioException) {
            displayMessage("\nError sending board data: " + ioException.getMessage());
            ioException.printStackTrace();
        }
    }

    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(() -> {
            System.out.println(messageToDisplay);
        });
    }

    private void updateMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(() -> {
            frame.getMessagesPanel().setMessage(messageToDisplay);
        });
    }
}