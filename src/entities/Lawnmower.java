package entities;

import util.ImageLoader;
import java.awt.*;
import java.util.List;
import ui.ScoreCounter;

public class Lawnmower {
    private int x, y;
    private int width = 60, height = 60;
    private boolean active = false;
    private boolean used = false;
    private int speed = 10;

    public Lawnmower(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(List<Zombie> zombies, ScoreCounter scoreCounter) {
        if (active) {
            x += speed;

            for (Zombie z : zombies) {
                if (!z.isDying && Math.abs(z.getY() - y) <= 20 && z.getBounds().intersects(getBounds())) {
                    z.startDying();
                    scoreCounter.incrementBy(z.getScoreValue()); // ✅ Add score!
                }
            }

            if (x > 1100) {
                used = true;
            }
        }
    }


    public void activate() {
        if (!used && !active) {
            active = true;
        }
    }

    public boolean isUsed() {
        return used;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public void draw(Graphics g) {
        Image img = ImageLoader.load("lawn_mower.gif");
        g.drawImage(img, x, y, width, height, null);
    }

    public int getY() {
        return y;
    }

    public boolean isActive() {
        return active;
    }
}
