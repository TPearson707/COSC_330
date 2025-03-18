import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BattleshipFrame extends JFrame {
    private ClientMessagePanel messagesPanel;
    private OceanPanel oceanPanel;
    private TargetPanel targetPanel;
    private ShipDepotPanel shipDepotPanel;
    private Player currentPlayer;
    private Player opponent;
    private ActionListener targetGridListener;

    public BattleshipFrame() {
        setTitle("Battleship Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Set up glass pane for drag and drop
        JPanel glassPane = (JPanel)getGlassPane();
        glassPane.setLayout(null);
        glassPane.setVisible(false);

        // Create main game panel
        JPanel gamePanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Initialize panels
        messagesPanel = new ClientMessagePanel("Welcome to Battleship!");
        oceanPanel = new OceanPanel();
        targetPanel = new TargetPanel();
        shipDepotPanel = new ShipDepotPanel();

        // Setup drag and drop with glass pane
        // Setup drag and drop with glass pane
        // oceanPanel.setupDragAndDrop(glassPane);
        shipDepotPanel.setupDragAndDrop(glassPane, oceanPanel);

        // Add panels to frame
        gamePanel.add(oceanPanel);
        gamePanel.add(targetPanel);
        add(messagesPanel, BorderLayout.NORTH);
        add(gamePanel, BorderLayout.CENTER);
        add(shipDepotPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    public void setPlayers(Player player1, Player player2) {
        this.currentPlayer = player1;
        this.opponent = player2;
    }

    public void switchPlayers() {
        Player temp = currentPlayer;
        currentPlayer = opponent;
        opponent = temp;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getOpponent() {
        return opponent;
    }

    public ClientMessagePanel getMessagesPanel() {
        return messagesPanel;
    }

    public TargetPanel getTargetPanel() {
        return targetPanel;
    }

    public OceanPanel getOceanPanel() {
        return oceanPanel;
    }

    public ShipDepotPanel getShipDepotPanel() {
        return shipDepotPanel;
    }

    public void addTargetGridButtonListener(ActionListener listener) {
        // Remove any existing listeners first
        removeTargetGridButtonListener();
        
        // Store the new listener
        this.targetGridListener = listener;
        
        // Add the new listener to all grid buttons
        JButton[][] buttons = targetPanel.getGridButtons();
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].addActionListener(listener);
                    
            }
        }
    }
    
    public void removeTargetGridButtonListener() {
        // Remove the listener from all grid buttons
        JButton[][] buttons = targetPanel.getGridButtons();
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                buttons[i][j].removeActionListener(targetGridListener);
            }
        }
    }

    // add action listener to random placement button
    public void addRandomPlacementButtonListener(ActionListener listener) {
        shipDepotPanel.addActionListener(listener);
    }

    // Add listener to ocean grid (Logic for listener defined in Controller)
    public void addOceanGridImageListener(MouseListener listener) {
        JLabel[][] Images = oceanPanel.getGridLabels();

        for (int i = 0; i < Images.length; i++) {
            for (int j = 0; j < Images[i].length; j++) {
                Images[i][j].addMouseListener(listener);
            }
        }
    }
}