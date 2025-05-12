package ui;

import game.*;
import java.awt.Point;

public class SunflowerIcon extends SidebarIcon {
    public SunflowerIcon(GamePanel panel) {
        super(panel,
                "active_sunflower.png",
                "inactive_sunflower.png",
                0, 70,
                50); // cost
    }

    @Override
    protected void handleDrop(Point dropPoint) {
        gamePanel.tryPlaceSunflower(dropPoint);
    }
}


