package entities;

import util.ImageLoader;
import java.awt.*;

public class NormalZombie extends Zombie {
    private Image aliveImg;
    private Image dyingImg;

    public NormalZombie(int x, int y) {
        super(x, y);
        this.health = 190;
        this.speed = 0.36;
        aliveImg = ImageLoader.load("zombie_normal.gif");
        dyingImg = ImageLoader.load("zombie_normal_dying.gif"); // only load once
    }

    @Override
    public void draw(Graphics g) {
        Image img = isDying ? dyingImg : aliveImg;
        g.drawImage(img, (int)x, y, width, height, null);
    }

}
