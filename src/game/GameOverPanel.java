package game;

import util.ImageLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameOverPanel extends JPanel {
    private Image gameOverImage;
    private int finalScore;

    public GameOverPanel(int score) {
        this.finalScore = score;
        gameOverImage = ImageLoader.load("gameOver.jpg");
        setLayout(null);

        // Create clickable Try Again button (as a GIF)
        Image tryAgainImg = ImageLoader.load("gameOver.gif");
        JLabel tryAgainButton = new JLabel(new ImageIcon(tryAgainImg));
        tryAgainButton.setBounds(440, 420, 220, 60); // Adjust position/size
        add(tryAgainButton);

        tryAgainButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Return to start screen
                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(GameOverPanel.this);
                topFrame.dispose();
                new StartScreen(); // Restart the game from the beginning
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(gameOverImage, 0, 0, getWidth(), getHeight(), this);

        // Draw final score
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.setColor(Color.WHITE);
        g.drawString("" + finalScore, 560, 531); // Adjust position as needed
    }
}
