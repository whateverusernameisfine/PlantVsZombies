package ui;

import game.*;
import java.awt.Point;


public class PeashooterIcon extends SidebarIcon {
    public PeashooterIcon(GamePanel panel) {
        super(panel,
                "active_peashooter.png",
                "inactive_peashooter.png",
                0, 166,
                100); // cost
    }

    @Override
    protected void handleDrop(Point dropPoint) {
        gamePanel.tryPlacePeashooter(dropPoint);
    }
}
