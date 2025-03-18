import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.sound.sampled.*;

public class Controller implements ActionListener {

    private BattleshipFrame frame;
    private Model model;
    private Player attacker;
    private Player opponent;
    private Audio audio;
    private Server server;
    private Client client;
    private int choice;

    public Controller(BattleshipFrame frame) {
        String[] options = {"Server", "Client"};
        int choice = JOptionPane.showOptionDialog(null, "Choose a role", "Battleship", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        this.choice = choice;

        this.frame = frame;
        frame.setVisible(true);
        model = Model.getInstance();
        audio = new Audio();

        // Start network components in separate threads
        if (choice == 0) { // Server
            server = new Server();
            server.setModel(model); // Pass the model to the server
            server.setFrame(frame);
            // Set the initial player
            model.setCurrentPlayerIndex(choice);
            
            attacker = model.getPlayer(model.getCurrentPlayerIndex());
            opponent = model.getOpponent();

            frame.setPlayers(attacker, opponent);

            // Add action listeners to the buttons
            frame.addTargetGridButtonListener(this);
            frame.addRandomPlacementButtonListener(this);
            new Thread(() -> {
                server.runServer();
            }).start();
        } else { // Client
            client = new Client("127.0.0.1");
            client.setModel(model); // Pass the model to the client
            client.setFrame(frame);
            // Set the initial player
            model.setCurrentPlayerIndex(choice);
            
            attacker = model.getPlayer(model.getCurrentPlayerIndex());
            opponent = model.getOpponent();

            frame.setPlayers(attacker, opponent);

            // Add action listeners to the buttons
            frame.addTargetGridButtonListener(this);
            frame.addRandomPlacementButtonListener(this);
            new Thread(() -> {
                client.runClient();
            }).start();
        }
    }

    public void startGame() {
        // Initialize the model
        audio.setSound("assets/sounds/argenti.wav");
        audio.playSound(true);
        model.startGame();

    }

    public void switchPlayers() {
        if (model.getCurrentPlayerIndex() == 0) {
            model.setCurrentPlayerIndex(1);
        } else {
            model.setCurrentPlayerIndex(0);
        }

        // Update the GUI
        frame.switchPlayers();
    }

    public void addShipsToOceanGrid(Player player) {
        String[][] grid = player.getBoard().getGrid();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (grid[i][j] != "~") {
                    frame.getOceanPanel().addShipsToOcean(i, j);
                }
            }
        }
    }

    // Implement the actionPerformed method
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();


        if (button.getText().equals("Random Placement")) { // Check if it’s the correct button
            System.out.println("Adding ships to the ocean randomly...");
            model.setRandomPlacement(true);
            model.startGame();
            addShipsToOceanGrid(attacker);
                if (choice == 0 && server != null) { // Server
                    try {
                        System.out.println("Hosting game - initiating board exchange");
                
                        // 1. Generate and send server's board first
                        model.setRandomPlacement(true);
                        addShipsToOceanGrid(attacker);
                        server.sendServerBoard(attacker.getBoard().getGrid());
                
                        // 2. Wait for client's board
                    
                            try {
                                int attempts = 0;
                                while (attempts < 10 && server.getClientBoard() == null) {
                                    Thread.sleep(500); // Wait for client's board
                                    attempts++;
                                }
                
                                if (server.getClientBoard() != null) {
                                    SwingUtilities.invokeLater(() -> {
                                        opponent.getBoard().setGrid(server.getClientBoard()); // Set client's board
                                        model.startGame();
                                        frame.getMessagesPanel().setMessage("Both boards received! Game starting!");
                                    });
                                } else {
                                    SwingUtilities.invokeLater(() -> {
                                        frame.getMessagesPanel().setMessage("Failed to receive client's board.");
                                    });
                                }
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        
                    } catch (Exception ex) {
                        frame.getMessagesPanel().setMessage("Network error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
            } else if (choice == 1 && client != null) { // Client
                System.out.println("Joining game - awaiting board exchange");
               
                    try {
                        // 1. Wait for server's board
                        int attempts = 0;
                        while (attempts < 10 && client.getServerBoard() == null) {
                            Thread.sleep(500); // Wait for server's board
                            attempts++;
                        }
            
                        if (client.getServerBoard() != null) {
                            SwingUtilities.invokeLater(() -> {
                                // 2. Set server's board and send our board
                                opponent.getBoard().setGrid(client.getServerBoard()); // Set server's board
                                client.sendClientBoard(attacker.getBoard().getGrid()); // Send client's board
                                model.startGame();
                                frame.getMessagesPanel().setMessage("Boards exchanged! Game starting!");
                            });
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                frame.getMessagesPanel().setMessage("Failed to receive server's board.");
                            });
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                
            }
                return;
        } else if (button.getText().equals("Start Game")) { // Check if it’s the correct button
            audio.setSound("assets/sounds/game_start.wav");
            audio.playSound(false);
            System.out.println("Adding ships to the ocean manually...");
            model.setRandomPlacement(false); // Disable random placement
            model.startGame(); // Start the game without random placement
        
            // Add ships to the ocean grid based on manual placement
            addShipsToOceanGrid(attacker);
        
            if (choice == 0 && server != null) { // Server
                try {
                    System.out.println("Hosting game - initiating board exchange");
        
                    // 1. Generate and send server's board first
                    addShipsToOceanGrid(attacker); // Add manually placed ships to the grid
                    server.sendServerBoard(attacker.getBoard().getGrid()); // Send server's board to client
        
                    
                        try {
                            int attempts = 0;
                            while (attempts < 10 && server.getClientBoard() == null) {
                                Thread.sleep(500); // Wait for client's board
                                attempts++;
                            }
        
                            if (server.getClientBoard() != null) {
                                SwingUtilities.invokeLater(() -> {
                                    // Set the client's board as the opponent's board
                                    opponent.getBoard().setGrid(server.getClientBoard());
                                    model.startGame();
                                    frame.getMessagesPanel().setMessage("Both boards received! Game starting!");
                                });
                            } else {
                                SwingUtilities.invokeLater(() -> {
                                    frame.getMessagesPanel().setMessage("Failed to receive client's board.");
                                });
                            }
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                   
                } catch (Exception ex) {
                    frame.getMessagesPanel().setMessage("Network error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            } else if (choice == 1 && client != null) { // Client
                System.out.println("Joining game - awaiting board exchange");
                    try {
                        // 1. Wait for server's board
                        int attempts = 0;
                        while (attempts < 10 && client.getServerBoard() == null) {
                            Thread.sleep(500); // Wait for server's board
                            attempts++;
                        }
        
                        if (client.getServerBoard() != null) {
                            SwingUtilities.invokeLater(() -> {
                                // 2. Set server's board and send our board
                                opponent.getBoard().setGrid(client.getServerBoard()); // Set server's board
                                client.sendClientBoard(attacker.getBoard().getGrid()); // Send client's board
                                model.startGame();
                                frame.getMessagesPanel().setMessage("Boards exchanged! Game starting!");
                            });
                        } else {
                            SwingUtilities.invokeLater(() -> {
                                frame.getMessagesPanel().setMessage("Failed to receive server's board.");
                            });
                        }
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                
            }

        }

        if (!button.getText().equals("Random Placement") && !button.getText().equals("Start Game")) {
            // Handle target grid button click
            JButton[][] buttons = frame.getTargetPanel().getGridButtons();

            for (int i = 0; i < buttons.length; i++) {
                for (int j = 0; j < buttons[i].length; j++) {
                    if (buttons[i][j] == button) {
                        int x = i;
                        int y = j;

                        // Send attack to opponent
                        String attackMsg = String.format("ATTACK %d %d", x, y);

                        if (choice == 0) { // Server
                            server.sendData(attackMsg);

                            
                        } else { // Client
                            client.sendData(attackMsg);
                        }

                        // Disable button and play sound
                        audio.setSound("assets/sounds/cannon.wav");
                        audio.playSound(false);
                        button.setEnabled(false);
                        return;
                    }
                }
            }
        }
    }
}
