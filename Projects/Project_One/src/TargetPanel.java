import javax.swing.*;
import java.awt.*;

public class TargetPanel extends JPanel {
    private static final int GRID_SIZE = 10;
    private JButton[][] gridButtons;

    public TargetPanel() {
        setBorder(BorderFactory.createTitledBorder("Target Grid"));
        setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
        
        gridButtons = new JButton[GRID_SIZE][GRID_SIZE];
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                gridButtons[i][j] = new JButton();
                gridButtons[i][j].setPreferredSize(new Dimension(40, 40));
                gridButtons[i][j].setBackground(Color.LIGHT_GRAY);
                add(gridButtons[i][j]);
            }
        }
    }

    public JButton[][] getGridButtons() {
        return gridButtons;
    }

    public void markHit(int x, int y) {
        gridButtons[x][y].setBackground(Color.RED);
        gridButtons[x][y].setOpaque(true);
        gridButtons[x][y].repaint();
    }

    public void markMiss(int x, int y) {
        gridButtons[x][y].setBackground(Color.BLUE);
        gridButtons[x][y].setOpaque(true);
        gridButtons[x][y].repaint();
    }

    public void updateResult(int x, int y, AttackResult result) {
        if (result.isHit()) {
            markHit(x, y);
        } else {
            markMiss(x, y);
        }
    }

    public void setTargetButtonsEnabled(boolean enabled) {
        for (int i = 0; i < gridButtons.length; i++) {
            for (int j = 0; j < gridButtons[i].length; j++) {
                gridButtons[i][j].setEnabled(enabled);
            }
        }
    }

    public void clearGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                gridButtons[i][j].setBackground(Color.LIGHT_GRAY);
                gridButtons[i][j].setOpaque(true);
                gridButtons[i][j].repaint();
            }
        }
    }
}