package entities;

import java.awt.*;
import util.ImageLoader;

public class Beet extends Bullet {
    public Beet(int x, int y) {
        super(x, y);
        this.speed = 4; // slightly slower but deals more damage
        this.damage = 50; // stronger
    }

    @Override
    public void draw(Graphics g) {
        Image img = ImageLoader.load("beetbullet.png");
        g.drawImage(img, x, y, 24, 24, null);
    }
}
