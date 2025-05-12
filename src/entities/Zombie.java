package entities;

import java.awt.*;
import java.util.List;

// =======================================
// ABSTRACT BASE CLASS: ZOMBIE
// =======================================
public abstract class Zombie {

    // =======================================
    // POSITION & SIZE
    // =======================================
    protected double x;           // for smooth horizontal movement
    protected int y;
    protected int drawX;          // converted integer position for drawing
    protected int width = 80;
    protected int height = 100;

    // =======================================
    // STATS & STATE FLAGS
    // =======================================
    protected int health;
    protected double speed;
    protected boolean isColliding = false;
    protected boolean isDying = false;
    protected int dyingTimer = 0;
    protected int dyingDuration = 15; // ~1 second at 30 FPS
    protected int attackCooldown = 0;

    protected Plant targetPlant = null;

    // =======================================
    // CONSTRUCTOR
    // =======================================
    public Zombie(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // =======================================
    // ZOMBIE GAME LOGIC (called per frame)
    // =======================================
    public void update(List<Plant> plants) {
        isColliding = false;
        targetPlant = null;

        // Check for plant collision in the same row
        for (Plant plant : plants) {
            if (Math.abs(plant.getY() - this.y) <= 20 &&
                    plant.getBounds().intersects(this.getBounds()) &&
                    plant.isAlive()) {

                isColliding = true;
                targetPlant = plant;
                break;
            }
        }

        // Handle attack or movement
        if (isColliding && targetPlant != null) {
            attackCooldown++;
            if (attackCooldown >= 30) { // attack every 30 frames
                targetPlant.takeDamage(100);
                System.out.println("Bullet hit! " + targetPlant.name + "health: " + targetPlant.health);
                attackCooldown = 0;

                if (targetPlant.isDead()) {
                    targetPlant.startDying();
                }
            }
        } else {
            x -= speed;
            drawX = (int) x;
        }
    }

    // =======================================
    // ABSTRACT DRAW METHOD
    // =======================================
    public abstract void draw(Graphics g);

    // =======================================
    // DYING & REMOVAL LOGIC
    // =======================================
    public boolean isDead() {
        return health <= 0 && !isDying;
    }

    public void startDying() {
        isDying = true;
        dyingTimer = 0;
    }

    public void updateDying() {
        if (isDying) dyingTimer++;
    }

    public boolean isMarkedForRemoval() {
        return isDying && dyingTimer >= dyingDuration;
    }

    public boolean isDying() {
        return isDying;
    }

    // =======================================
    // DAMAGE & COLLISION
    // =======================================
    public void takeDamage(int amount) {
        health -= amount;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, y, width, height);
    }

    // =======================================
    // GETTERS
    // =======================================
    public int getX() {
        return (int) x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public int getScoreValue() {
        return 1; // default value; override in subclasses
    }
}
