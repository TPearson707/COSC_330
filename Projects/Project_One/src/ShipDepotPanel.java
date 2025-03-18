import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ShipDepotPanel extends JPanel {
    private JLabel[] ships;
    private JButton randomPlacementButton;
    private JButton startGameButton;
    private ImageIcon shipImage;
    private Audio audio;
    private Model model;
    
    private static class ShipInfo {
        String name;
        int length;
        char direction;

        ShipInfo(String name, int length) {
            this.name = name;
            this.length = length;
            this.direction = 'E'; // Default direction
        }
    }

    public ShipDepotPanel() {
        model = Model.getInstance();
        audio = new Audio();
        setBorder(BorderFactory.createTitledBorder("Ship Depot"));
        setLayout(new FlowLayout());

        // Load ship image
        shipImage = new ImageIcon("assets/images/ocean_cell_ship.png");

        // Create ships and button
        ships = new JLabel[5];
        randomPlacementButton = new JButton("Random Placement");
        startGameButton = new JButton("Start Game");

        initializeShips();
    }

    private void initializeShips() {
        String[] shipNames = {"Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"};
        int[] shipLengths = {5, 4, 3, 3, 2};

        for (int i = 0; i < shipNames.length; i++) {
            ShipInfo shipInfo = new ShipInfo(shipNames[i], shipLengths[i]);

            JLabel shipLabel = new JLabel(shipNames[i]);
            shipLabel.setPreferredSize(new Dimension(120, 40));
            shipLabel.setIcon(shipImage);
            shipLabel.setHorizontalTextPosition(JLabel.CENTER);
            shipLabel.setVerticalTextPosition(JLabel.BOTTOM);

            // Store ShipInfo in the label
            shipLabel.putClientProperty("shipInfo", shipInfo);

            ships[i] = shipLabel;
            add(shipLabel);
        }
        add(startGameButton);
        add(randomPlacementButton);
    }

    public void addActionListener(ActionListener listener) {
        randomPlacementButton.addActionListener(listener);
        startGameButton.addActionListener(listener);
    }

    public void setupDragAndDrop(JPanel glassPane, OceanPanel oceanPanel) {
        MouseAdapter shipDragAdapter = new MouseAdapter() {
            private JLabel draggedLabel;

            @Override
            public void mousePressed(MouseEvent e) {
                JLabel sourceLabel = (JLabel) e.getSource();
                ShipInfo shipInfo = (ShipInfo) sourceLabel.getClientProperty("shipInfo");

                if (shipInfo == null) return; // Safety check

                draggedLabel = new JLabel();
                int width = (shipInfo.direction == 'E' || shipInfo.direction == 'W') ? 50 * shipInfo.length : 50;
                int height = (shipInfo.direction == 'N' || shipInfo.direction == 'S') ? 50 * shipInfo.length : 50;
                draggedLabel.setPreferredSize(new Dimension(width, height));
                draggedLabel.setIcon(shipImage);

                // Store ShipInfo directly in the dragged label
                draggedLabel.putClientProperty("shipInfo", shipInfo);

                // Add ship to glass pane
                Point p = SwingUtilities.convertPoint(sourceLabel, e.getPoint(), glassPane);
                draggedLabel.setBounds(p.x - 25, p.y - 25, width, height);
                glassPane.add(draggedLabel);
                glassPane.setVisible(true);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedLabel != null) {
                    Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), glassPane);
                    draggedLabel.setLocation(p.x - 25, p.y - 25);
                    glassPane.repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (draggedLabel != null) {
                    Point p = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), oceanPanel);
                    ShipInfo shipInfo = (ShipInfo) draggedLabel.getClientProperty("shipInfo");

                    if (p.x >= 0 && p.y >= 0 && p.x < oceanPanel.getWidth() && p.y < oceanPanel.getHeight()) {
                        Component targetComponent = oceanPanel.getComponentAt(p);
                        if (targetComponent instanceof JLabel) {
                            int targetIndex = oceanPanel.getComponentZOrder(targetComponent);
                            int row = targetIndex / 10; // Assuming a 10x10 grid
                            int col = targetIndex % 10;

                            boolean placedOnOcean = oceanPanel.placeShip(row, col, shipInfo.length, shipInfo.direction);
                            model.addShipToBoard(model.getPlayer(model.getCurrentPlayerIndex()), shipInfo.name,row+1, col+1, shipInfo.direction);


                            if (placedOnOcean) {
                                ((JLabel) e.getSource()).setVisible(false); // Hide the original ship label
                                audio.setSound("assets/sounds/water_splash.wav");
                                audio.playSound(false);
                            } else {
                                JOptionPane.showMessageDialog(null, "Invalid placement. Ship cannot be placed here.");
                            }
                        }
                    }

                glassPane.remove(draggedLabel);
                glassPane.setVisible(false);
                glassPane.repaint();
                draggedLabel = null;
                }
            }


            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) { // Right-click to rotate
                    JLabel sourceLabel = (JLabel) e.getSource();
                    ShipInfo shipInfo = (ShipInfo) sourceLabel.getClientProperty("shipInfo");
                    if (shipInfo != null) {
                        switch (shipInfo.direction) {
                            case 'E': shipInfo.direction = 'S'; break;
                            case 'S': shipInfo.direction = 'W'; break;
                            case 'W': shipInfo.direction = 'N'; break;
                            case 'N': shipInfo.direction = 'E'; break;
                        }
                        JOptionPane.showMessageDialog(null, "Ship orientation changed to " + shipInfo.direction);
                    }
                }
            }
        };

        // Attach listeners to all ship labels
        for (JLabel ship : ships) {
            ship.addMouseListener(shipDragAdapter);
            ship.addMouseMotionListener(shipDragAdapter);
        }
    }

    public JLabel[] getShips() {
        return ships;
    }
}
