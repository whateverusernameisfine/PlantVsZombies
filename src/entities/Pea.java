package entities;

import util.ImageLoader;
import java.awt.*;

public class Pea extends Bullet {
    public Pea(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        Image img = ImageLoader.load("pea.png");
        g.drawImage(img, x, y, 20, 20, null);
    }
}
