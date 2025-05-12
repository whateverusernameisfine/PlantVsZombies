package entities;

import util.ImageLoader;
import java.awt.*;
import java.util.List;

public class Beetroot extends Plant {
    private int shootTimer = 0;
    private static final int SHOOT_INTERVAL = 100; // slower than PeaShooter

    public Beetroot(int x, int y) {
        super(x, y);
        this.health = 100;
        this.name = "Beetroot";
        this.cost = 125;
    }

    @Override
    public void update() {
        updateDying();
    }

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
                bullets.add(new Beet(x + 40, y + 20));
                shootTimer = 0;
            }
        }
    }



    @Override
    public void draw(Graphics g) {
        String imgName = isDying ? "beetroot_dying.gif" : "beetroot.gif";
        Image img = ImageLoader.load(imgName);
        g.drawImage(img, x, y, width, height, null);
    }
}
