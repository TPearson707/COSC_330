import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Client extends JFrame {
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String chatServer;
    private Socket client;
    private String[][] boardData;
    private BattleshipFrame frame;
    private Model model;
    private boolean isMyTurn;

    public Client(String host) {
        super("Client");
        SwingUtilities.invokeLater(() -> {
            isMyTurn = false;
            frame.getTargetPanel().setTargetButtonsEnabled(false);
        });
        chatServer = host;
    }

    public void runClient() {
        while (true) {
            try {
                connectToServer();
                getStreams();
                processConnection();
            } catch (EOFException eofException) {
                displayMessage("\nClient terminated connection");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            } finally {
                closeConnection();
            }
        }
    }

    private void connectToServer() throws IOException {
        displayMessage("Attempting connection\n");
        client = new Socket(InetAddress.getByName(chatServer), 12345);
        displayMessage("Connected to: " + client.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException {
        input = new ObjectInputStream(client.getInputStream());
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();
        displayMessage("\nGot I/O streams\n");
    }

    private void processConnection() throws IOException {
        try {
            message = (String) input.readObject();
            displayMessage("\n" + message);

            do {
                try {
                    message = (String) input.readObject();
                    displayMessage("\n" + message);

                    if (message.equals("BOARD_DATA")) {
                        try {
                            String[][] receivedBoard = (String[][]) input.readObject();
                            setServerBoard(receivedBoard);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (message.startsWith("ATTACK ")) {
                        try {
                            // Check if game is already over before processing
                            if (model.isGameOver()) {
                                System.out.println("Ignoring attack as game is already over");
                                return;
                            }
                            String[] parts = message.split(" ");
                            if (parts.length != 3) {
                                System.err.println("Invalid ATTACK message: " + message);
                                return;
                            }
                            int x = Integer.parseInt(parts[1]);
                            int y = Integer.parseInt(parts[2]);
                            // Perform the attack and get the result
                            AttackResult result = model.performAttack(0, x, y);
                            // IMPORTANT: Check game over state AFTER the attack is processed
                            boolean gameOver = model.isGameOver();
                            // Immediately handle game over condition first
                            if (gameOver) {
                                // Update UI first to show the final attack result
                                SwingUtilities.invokeLater(() -> {
                                    frame.getOceanPanel().updateOcean(x, y, result.isHit());
                                    // If player 2's ships are sunk, player 1 (server) wins
                                    if (model.getPlayer(1).getBoard().allShipsSunk()) {
                                        updateMessage("Game Over! Server wins!");
                                    } else {
                                        // If player 1's ships are sunk, player 2 (client) wins
                                        updateMessage("Game Over! You Win!");
                                    }
                                    frame.getTargetPanel().setTargetButtonsEnabled(false);
                                });
                                
                                // Send GAME_OVER message to server after updating UI
                                sendData("GAME_OVER CLIENT");
                            } else {
                                // If game is not over, send attack result
                                String resultMsg = String.format("ATTACK_RESULT %d %d %s %b",
                                    x, y, result.serialize(), gameOver);
                                sendData(resultMsg);
                                // Update UI in a separate thread
                                SwingUtilities.invokeLater(() -> {
                                    frame.getOceanPanel().updateOcean(x, y, result.isHit());
                                    if (result.isHit()) {
                                        updateMessage("Opponent hit your ship at (" + x + ", " + y + ")!");
                                    } else {
                                        updateMessage("Opponent missed at (" + x + ", " + y + ").");
                                    }
                                    if (result.isSunk()) {
                                        updateMessage("Opponent " + result.toString());
                                    }
                                    // Send TURN message only if game is not over
                                    sendData("TURN CLIENT");
                                });
                            }
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
                            model.getPlayer(0).getBoard().attackCell(x, y);
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
                                // If player 1's ships are sunk, player 2 (client) wins
                                if (model.getPlayer(0).getBoard().allShipsSunk()) {
                                    updateMessage("Game Over! You Win!");
                                } else {
                                    // If player 2's ships are sunk, player 1 (server) wins
                                    updateMessage("Game Over! Client wins!");
                                }
                                frame.getTargetPanel().setTargetButtonsEnabled(false);
                            } else {
                                // Send TURN message to server
                                sendData("TURN SERVER");
                            }
                        });
                    } else if (message.startsWith("GAME_OVER ")) {
                        String winner = message.split(" ")[1];
                        SwingUtilities.invokeLater(() -> {
                            // If the message says SERVER wins, it means the server is reporting they won
                            if (winner.equals("SERVER")) {
                                updateMessage("Game Over! Server wins!");
                            } else {
                                updateMessage("Game Over! You Win!");
                            }
                            frame.getTargetPanel().setTargetButtonsEnabled(false);
                        });
                    } else if (message.startsWith("TURN ")) {
                        String turn = message.split(" ")[1];
                        isMyTurn = turn.equals("CLIENT");

                        SwingUtilities.invokeLater(() -> {
                            frame.getTargetPanel().setTargetButtonsEnabled(isMyTurn);
                        });
                    }
                } catch (ClassNotFoundException | IOException e) {
                    displayMessage("\nError reading from server: " + e.getMessage());
                    break;
                }
            } while (!message.equals("SERVER_TERMINATING"));
        } catch (ClassNotFoundException classNotFoundException) {
            displayMessage("\nUnknown object type received");
        }
        displayMessage("\nClosing connection");
    }

    public synchronized void setModel(Model model) {
        this.model = model;
    }

    public synchronized void setFrame(BattleshipFrame frame) {
        this.frame = frame;
    }

    public synchronized void setServerBoard(String[][] board) {
        this.boardData = board;
        notifyAll();
    }

    public synchronized String[][] getServerBoard() {
        return this.boardData;
    }

    private void closeConnection() {
        displayMessage("\nClosing connection");
        try {
            output.close();
            input.close();
            client.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void sendData(String message) {
        try {
            output.writeObject(message);
            output.flush();
            displayMessage("\nCLIENT>>> " + message);
        } catch (IOException ioException) {
            System.out.println("\nError writing object");
        }
    }

    public void sendClientBoard(String[][] board) {
        try {
            output.writeObject("BOARD_DATA");
            output.writeObject(board);
            output.flush();
            displayMessage("\nSent board data to server");
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
