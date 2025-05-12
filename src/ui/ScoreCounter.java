package ui;

import util.ImageLoader;
import javax.swing.*;
import java.awt.*;

public class ScoreCounter extends JComponent {
    private int score = 0;
    private Image counterBox;

    public ScoreCounter() {
        counterBox = ImageLoader.load("Counter.png");
    }

    public void incrementBy(int value) {
        score += value;
        repaint();
    }

    public int getScore() {
        return score;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw the counter box
        g.drawImage(counterBox, 0, 0, getWidth(), getHeight(), null);

        // Draw the score text
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.setColor(Color.BLACK);
        g.drawString("" + score, 20, 28); // Adjust for centering inside box
    }
}

