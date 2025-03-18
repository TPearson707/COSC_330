import javax.swing.*;
import java.awt.*;

public class ClientMessagePanel extends JPanel {
    private JLabel messageLabel;

    public ClientMessagePanel(String initialMessage) {
        setBorder(BorderFactory.createTitledBorder("Client Messages"));
        setLayout(new BorderLayout());
        
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageLabel.setText(initialMessage);

        add(messageLabel, BorderLayout.CENTER);
    }

    public void setMessage(String messages) {
        messageLabel.setText(messages);
        revalidate();
        repaint();
    }

    public void clearMessage() {
        messageLabel.setText("");
    }
}