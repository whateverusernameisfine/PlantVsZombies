package game;
import javax.swing.*;

public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Plants vs Zombies");
        setSize(1111, 602);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel panel = new GamePanel();
        add(panel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartScreen::new);
    }
}
