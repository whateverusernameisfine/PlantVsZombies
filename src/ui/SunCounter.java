package ui;

import javax.swing.*;
import java.awt.*;

public class SunCounter extends JLabel {
    private int sunAmount = 100;

    public SunCounter() {
        setFont(new Font("Arial", Font.BOLD, 18));
        setForeground(Color.BLACK);
        setOpaque(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        updateDisplay();
    }

    public void addSun(int amount) {
        sunAmount += amount;
        updateDisplay();
    }

    public boolean spendSun(int amount) {
        if (sunAmount >= amount) {
            sunAmount -= amount;
            updateDisplay();
            return true;
        }
        return false;
    }

    private void updateDisplay() {
        setText("" + sunAmount);
    }

    public int getValue() {
        return (sunAmount);
    }
}
