package entities;

import util.ImageLoader;
import java.awt.*;

public class ConeheadZombie extends Zombie {
    private Image aliveImg;
    private Image dyingImg;


    public ConeheadZombie(int x, int y) {
        super(x, y);
        this.health = 560;  // tanky
        this.speed = 0.36;
        aliveImg = ImageLoader.load("Conehead_Zombie.gif");
        dyingImg = ImageLoader.load("zombie_normal_dying.gif");
    }

    @Override
    public int getScoreValue() {
        return 2;
    }

    @Override
    public void draw(Graphics g) {
        Image img = isDying ? dyingImg : aliveImg;
        g.drawImage(img, (int)x, y, width, height, null);
    }

}
