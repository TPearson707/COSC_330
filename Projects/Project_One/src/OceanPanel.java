import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class OceanPanel extends JPanel {
    private static final int GRID_SIZE = 10;
    private JLabel[][] gridLabels;
    private ImageIcon originalImage;
    private ImageIcon shipImage;
    private ImageIcon shipHitImage;

    public OceanPanel() {
        setBorder(BorderFactory.createTitledBorder("Your Ocean"));
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        gridLabels = new JLabel[GRID_SIZE][GRID_SIZE];
        
        originalImage = new ImageIcon("assets/images/ocean_cell.png");
        shipImage = new ImageIcon("assets/images/ocean_cell_ship.png");
        shipHitImage = new ImageIcon("assets/images/ocean_cell_hit.png");

        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                gridLabels[i][j] = new JLabel(originalImage);
                gridLabels[i][j].setBackground(Color.BLUE);
                gridLabels[i][j].setOpaque(true);
                add(gridLabels[i][j]);
            }
        }
    }

    

    // New helper method to place ships
public boolean placeShip(int startRow, int startCol, int length, char direction) {
    // Check if the ship can be placed
    switch (direction) {
        case 'N':
            if (startRow - length + 1 < 0) return false;
            for (int i = 0; i < length; i++) {
                if (gridLabels[startRow - i][startCol].getIcon().equals(shipImage)) {
                    return false;
                }
            }
            for (int i = 0; i < length; i++) {
                gridLabels[startRow - i][startCol].setIcon(shipImage);
            }
            break;
        
        case 'S':
            if (startRow + length > GRID_SIZE) return false;
            for (int i = 0; i < length; i++) {
                if (gridLabels[startRow + i][startCol].getIcon().equals(shipImage)) {
                    return false;
                }
            }
            for (int i = 0; i < length; i++) {
                gridLabels[startRow + i][startCol].setIcon(shipImage);
            }
            break;
        
        case 'E':
            if (startCol + length > GRID_SIZE) return false;
            for (int i = 0; i < length; i++) {
                if (gridLabels[startRow][startCol + i].getIcon().equals(shipImage)) {
                    return false;
                }
            }
            for (int i = 0; i < length; i++) {
                gridLabels[startRow][startCol + i].setIcon(shipImage);
            }
            break;
        
        case 'W':
            if (startCol - length + 1 < 0) return false;
            for (int i = 0; i < length; i++) {
                if (gridLabels[startRow][startCol - i].getIcon().equals(shipImage)) {
                    return false;
                }
            }
            for (int i = 0; i < length; i++) {
                gridLabels[startRow][startCol - i].setIcon(shipImage);
            }
            break;
        
        default:
            return false;
    }
    
    return true;
}

    public JLabel[][] getGridLabels() {
        return gridLabels;
    }

    public void addShipsToOcean(int x, int y) {
        JLabel label = gridLabels[x][y];
        label.setIcon(shipImage);
        label.setOpaque(true);
        label.repaint();
    }

    public void updateOcean(int x, int y, boolean isHit) {
        if (isHit) {
            System.out.println("Hit! updating ocean");
            gridLabels[x][y].setIcon(shipHitImage);;
        }

        gridLabels[x][y].setOpaque(true);
        gridLabels[x][y].repaint();
    }

    public void clearOcean() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                gridLabels[i][j].setIcon(originalImage);
                gridLabels[i][j].setBackground(Color.BLUE);
                gridLabels[i][j].setOpaque(true);
                gridLabels[i][j].repaint();
            }
        }
    }
}
