package ui;

import game.*;
import java.awt.Point;


public class BeetrootIcon extends SidebarIcon {
    public BeetrootIcon(GamePanel panel) {
        super(panel,
                "active_beetroot.png",
                "inactive_beetroot.png",
                0, 358,
                125); // cost
    }

    @Override
    protected void handleDrop(Point dropPoint) {
        gamePanel.tryPlaceBeetroot(dropPoint);
    }
}
