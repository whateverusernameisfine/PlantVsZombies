package entities;

import util.ImageLoader;
import java.awt.*;
import java.util.List;

public class Sunflower extends Plant {
    private int timer = 0;
    private static final int INTERVAL = 800;

    public Sunflower(int x, int y) {
        super(x, y);
        this.health = 300;
        this.name = "Sunflower";
        this.cost = 50;
    }

    public void update(List<Sun> suns) {
        updateDying();
        if (isDying) return;

        timer++;
        if (timer >= INTERVAL) {
            suns.add(new Sun(x + 15, y + 15, true));
            timer = 0;
        }
    }


    public boolean isDead() {
        return health <= 0 && !isDying;
    }

    @Override
    public void update() {
        updateDying();
    }

    @Override
    public void draw(Graphics g) {
        Image img;
        if (isDying) {
            img = ImageLoader.load("sun_flower_dying.gif"); // plant-specific if needed
        } else {
            img = ImageLoader.load("sun_flower.gif"); // or peashooter.png etc.
        }
        g.drawImage(img, x, y, width, height, null);
    }
}