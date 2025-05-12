package game;

import util.ImageLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StartScreen extends JFrame {

    public StartScreen() {
        setTitle("Plants vs Zombies - Start");
        setSize(1100, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Load background
        Image bgImage = ImageLoader.load("start_screen.jpeg");
        JLabel background = new JLabel(new ImageIcon(bgImage));
        background.setBounds(0, 0, 1100, 600);
        background.setLayout(null);
        add(background);

        // Create image-based start button
        Image startImg = ImageLoader.load("click_to_start.gif");
        JLabel startButton = new JLabel(new ImageIcon(startImg));
        startButton.setBounds(310, 440, 492, 114); // Adjust to match button size
        background.add(startButton);

        // Click to start game
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();           // close start screen
                new GameFrame();     // start game
            }
        });

        setVisible(true);
    }
}
