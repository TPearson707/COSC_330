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
                            isMyTurn = true;
                            if (isMyTurn) {
                                SwingUtilities.invokeLater(() -> {
                                    frame.getTargetPanel().setTargetButtonsEnabled(true);
                                });
                            }

                            String[] parts = message.split(" ");
                            if (parts.length != 3) {
                                System.err.println("Invalid ATTACK message: " + message);
                                return;
                            }

                            int x = Integer.parseInt(parts[1]);
                            int y = Integer.parseInt(parts[2]);

                            AttackResult result = model.performAttack(0, x, y);
                            boolean gameOver = model.isGameOver();

                            if (gameOver) {
                                // Send GAME_OVER message to server
                                sendData("GAME_OVER CLIENT");
                            } else {
                                // Send ATTACK_RESULT message to server
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
                                    updateMessage("Game Over! You win!");
                                    frame.getTargetPanel().setTargetButtonsEnabled(false);
                                } else {
                                    // Send TURN message to server
                                    sendData("TURN CLIENT");
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
                                updateMessage("Game Over! You lose!");
                                frame.getTargetPanel().setTargetButtonsEnabled(false);
                            } else {
                                // Send TURN message to server
                                sendData("TURN SERVER");
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
                        isMyTurn = turn.equals("CLIENT");

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