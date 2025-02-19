import java.awt.*;
import javax.swing.*;

public class Themes {
    
    // Applies the Nordic Blue Theme
    public static void applyNordicBlueTheme(JPanel panel, JTextPane textPane, JButton[][] buttons) {
        // Theme attributes
        Font themeFont = new Font("Segoe UI", Font.BOLD, 30);

        Color panelBackground = new Color(0x1A1B26);        // Dark navy blue
        Color textPaneBackground = new Color(0x1F2335);     // Dark Purple
        Color textPaneForeground = new Color(0x7AA2F7);     // Neon blue
        Color buttonForeground = new Color(0x7AA2F7);       // Neon blue

        // Different button backgrounds for each row
        Color[] buttonRowColors = {
            new Color(0x7DCFFF), // Top row - Neon Cyan
            new Color(0xBB9AF7), // Middle row - Electric Purple
            new Color(0xF7768E)  // Bottom row - Deep Magenta
        };

        // Apply theme to text pane
        textPane.setFont(themeFont);
        textPane.setBackground(textPaneBackground);
        textPane.setForeground(textPaneForeground);
        textPane.revalidate();
        textPane.repaint();

        // Apply theme to the panel
        panel.setBackground(panelBackground);
        panel.revalidate();
        panel.repaint();

        // Apply theme to each button
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                buttons[row][col].setFont(themeFont);
                buttons[row][col].setForeground(buttonForeground);
                buttons[row][col].setBackground(buttonRowColors[row]);
                buttons[row][col].setOpaque(true);
                buttons[row][col].setBorderPainted(false);
                buttons[row][col].revalidate();
                buttons[row][col].repaint();
            }
        }
    }


    // Applies the Tokyo Midnight Theme
    public static void applyTokyoMidnightTheme(JPanel panel, JTextPane textPane, JButton[][] buttons) {
        // Theme attributes
        Font themeFont = new Font("Segoe UI", Font.BOLD, 30);

        Color panelBackground = new Color(0x1A1B26);        // Dark navy blue
        Color textPaneBackground = new Color(0x1F2335);     // Dark Purple
        Color textPaneForeground = new Color(0x7AA2F7);     // Neon blue
        Color buttonForeground = new Color(0x7AA2F7);       // Neon blue

        // Different button backgrounds for each row
        Color[] buttonRowColors = {
            new Color(0x7DCFFF), // Top row - Neon Cyan
            new Color(0xBB9AF7), // Middle row - Electric Purple
            new Color(0xF7768E)  // Bottom row - Deep Magenta
        };

        // Apply theme to text pane
        textPane.setFont(themeFont);
        textPane.setBackground(textPaneBackground);
        textPane.setForeground(textPaneForeground);
        textPane.revalidate();
        textPane.repaint();

        // Apply theme to the panel
        panel.setBackground(panelBackground);
        panel.revalidate();
        panel.repaint();

        // Apply theme to each button
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {
                buttons[row][col].setFont(themeFont);
                buttons[row][col].setForeground(buttonForeground);
                buttons[row][col].setBackground(buttonRowColors[row]);
                buttons[row][col].setOpaque(true);
                buttons[row][col].setBorderPainted(false);
                buttons[row][col].revalidate();
                buttons[row][col].repaint();
            }
        }
    }

};
