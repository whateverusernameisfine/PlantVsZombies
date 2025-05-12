package entities;

import util.ImageLoader;
import java.awt.*;
import java.util.List;

public class Peashooter extends Plant {
    private int shootTimer = 0;
    private static final int SHOOT_INTERVAL = 50; // 1.5s

    public Peashooter(int x, int y) {
        super(x, y);
        this.health = 300;
        this.name = "Peashooter";
        this.cost = 100;
    }

    // Called explicitly in GamePanel
    public void update(List<Bullet> bullets,  List<Zombie> zombies) {
        updateDying();
        if (isDying) return;

        boolean hasTarget = false;

        for (Zombie z : zombies) {
            if (!z.isDead() && Math.abs(z.getY() - this.y) <= 20 && z.getX() > this.x) {
                hasTarget = true;
                break;
            }
        }

        if (hasTarget) {
            shootTimer++;
            if (shootTimer >= SHOOT_INTERVAL) {
                bullets.add(new Pea(x + 40, y + 20));
                shootTimer = 0;
            }
        }
    }

    @Override
    public void update() {
        updateDying();
    }

    @Override
    public void draw(Graphics g) {
        String imgName = isDying ? "pea_shooter_dying.gif" : "pea_shooter.gif";
        Image img = ImageLoader.load(imgName);
        g.drawImage(img, x, y, width, height, null);
    }
}
