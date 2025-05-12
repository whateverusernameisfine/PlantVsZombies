package entities;

import util.ImageLoader;
import java.awt.*;

public class FootballZombie extends Zombie {
    private Image aliveImg;
    private Image dyingImg;


    public FootballZombie(int x, int y) {
        super(x, y);
        this.health = 1100;  // tanky
        this.speed = 0.7;     // faster than normal
        aliveImg = ImageLoader.load("zombie_football.gif");
        dyingImg = ImageLoader.load("zombie_football_dying.gif");
    }

    @Override
    public int getScoreValue() {
        return 3;
    }

    @Override
    public void draw(Graphics g) {
        Image img = isDying ? dyingImg : aliveImg;
        g.drawImage(img, (int)x, y, width, height, null);
    }

}
