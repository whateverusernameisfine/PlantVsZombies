// =======================================
// PACKAGE & IMPORTS
// =======================================
package game;

import entities.*;
import ui.*;
import util.ImageLoader;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

// =======================================
// CLASS DEFINITION
// =======================================
public class GamePanel extends JPanel implements ActionListener, MouseListener {

    // =======================================
    // GAME STATE VARIABLES
    // =======================================
    private boolean gameOver = false;
    private boolean showWaveBanner = false;
    private boolean finalWaveTriggered = false;
    private long gameStartTime = System.currentTimeMillis();
    private Random random = new Random();
    private Timer timer;

    private int sunSpawnTimer = 0;
    private int zombieSpawnTimer = 0;
    private int waveBannerTimer = 0;

    private static final int ZOMBIE_SPAWN_INTERVAL = 800;

    // =======================================
    // IMAGE RESOURCES
    // =======================================
    private Image background;
    private Image counterBox;
    private Image waveBannerImage;

    // =======================================
    // UI COMPONENTS
    // =======================================
    private FadePanel fadePanel;
    private Shovel shovel;
    private SunCounter sunCounter;
    private ScoreCounter scoreCounter;
    private SunflowerIcon sunflowerIcon;
    private PeashooterIcon peaShooterIcon;
    private WalnutIcon walnutIcon;
    private BeetrootIcon beetrootIcon;

    // =======================================
    // ENTITY LISTS
    // =======================================
    private Set<Point> occupiedCells;
    private java.util.List<Sun> suns;
    private List<Plant> plants;
    private List<Bullet> bullets;
    private List<Zombie> zombies;
    private List<Lawnmower> mowers;

    // =======================================
    // GRID CONFIGURATION
    // =======================================
    private final int[] columns = {290, 371, 451, 541, 621, 714, 798, 885, 972};
    private final int[] rows = {35, 139, 232, 335, 439};
    private static final int CELL_WIDTH = 80;
    private static final int CELL_HEIGHT = 80;

    // =======================================
    // CONSTRUCTOR: SETUP GAME PANEL
    // =======================================
    public GamePanel() {
        setLayout(null);
        setFocusable(true);
        addMouseListener(this);

        // Load images
        background = ImageLoader.load("backyard.jpg");
        counterBox = ImageLoader.load("Counter.png");
        waveBannerImage = ImageLoader.load("huge_wave_of_zombies_text.png");

        // Add fade overlay
        fadePanel = new FadePanel();
        fadePanel.setBounds(0, 0, 1100, 600);
        add(fadePanel);
        setComponentZOrder(fadePanel, 0);

        // Initialize and add sidebar icons
        sunflowerIcon = new SunflowerIcon(this); add(sunflowerIcon);
        peaShooterIcon = new PeashooterIcon(this); add(peaShooterIcon);
        walnutIcon = new WalnutIcon(this); add(walnutIcon);
        beetrootIcon = new BeetrootIcon(this); add(beetrootIcon);

        // Initialize and add counters
        sunCounter = new SunCounter(); sunCounter.setBounds(0, 520, 90, 40); add(sunCounter);
        scoreCounter = new ScoreCounter(); scoreCounter.setBounds(100, 25, 90, 40); add(scoreCounter);

        // Create game objects
        shovel = new Shovel(); add(shovel);
        suns = new ArrayList<>();
        plants = new ArrayList<>();
        bullets = new ArrayList<>();
        zombies = new ArrayList<>();
        mowers = new ArrayList<>();
        occupiedCells = new HashSet<>();

        for (int y : rows) {
            mowers.add(new Lawnmower(220, y));
        }

        // Start game loop
        timer = new Timer(30, this);
        timer.start();
    }

    // =======================================
    // RENDERING: DRAW BACKGROUND & ENTITIES
    // =======================================
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(counterBox, 7, 520, 80, 40, this);

        for (Lawnmower mower : mowers) mower.draw(g);
        for (Plant plant : plants) plant.draw(g);
        for (Sun sun : suns) sun.draw(g);
        for (Bullet bullet : bullets) bullet.draw(g);
        for (Zombie z : zombies) z.draw(g);

        if (showWaveBanner && waveBannerImage != null) {
            int imgWidth = 600, imgHeight = 100;
            int x = (getWidth() - imgWidth) / 2, y = 50;
            g.drawImage(waveBannerImage, x, y, imgWidth, imgHeight, this);
            waveBannerTimer--;
            if (waveBannerTimer <= 0) showWaveBanner = false;
        }
    }

    // =======================================
    // GAME LOOP (called every 30ms)
    // =======================================
    @Override
    public void actionPerformed(ActionEvent e) {
        // -- Sun spawning
        sunSpawnTimer++;
        if (sunSpawnTimer == 50 || sunSpawnTimer >= 1000) {
            suns.add(new Sun(random.nextInt(900) + 200, 0, false));
            sunSpawnTimer = 51;
        }

        for (Sun sun : suns) sun.update();

        // -- Plant behavior
        for (Plant plant : plants) {
            plant.update();
            if (plant instanceof Peashooter shooter) shooter.update(bullets, zombies);
            else if (plant instanceof Beetroot beet) beet.update(bullets, zombies);
            else if (plant instanceof Sunflower sf) sf.update(suns);
        }

        // -- Check for zombies crossing the baseline
        for (Zombie z : zombies) {
            if (z.getX() <= 220) {
                Lawnmower mower = getMowerInRow(z.getY());
                if (mower != null && !mower.isUsed() && !mower.isActive()) mower.activate();
                else if (mower == null || mower.isUsed()) {
                    triggerGameOver(); break;
                }
            }
        }

        // -- Zombie stage progression
        zombieSpawnTimer++;
        long now = System.currentTimeMillis();
        long elapsedSeconds = (now - gameStartTime) / 1000;

        if (zombieSpawnTimer >= ZOMBIE_SPAWN_INTERVAL) {
            spawnZombiesByStage(elapsedSeconds);
            zombieSpawnTimer = 0;
        }

        // -- Bullet collision with zombies
        List<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            Rectangle bBox = bullet.getBounds();
            for (Zombie zombie : zombies) {
                if (bBox.intersects(zombie.getBounds())) {
                    zombie.takeDamage(bullet.getDamage());
                    bulletsToRemove.add(bullet);
                    System.out.println("Bullet hit! Zombie health: " + zombie.getHealth());
                    break;
                }
            }
        }
        bullets.removeAll(bulletsToRemove);

        // -- Update zombies
        for (Zombie z : zombies) {
            if (z.isDead() && !z.isDying()) {
                z.startDying(); scoreCounter.incrementBy(z.getScoreValue());
            } else {
                z.update(plants);
            }
            z.updateDying();
        }
        zombies.removeIf(Zombie::isMarkedForRemoval);

        // -- Update plant deaths
        for (Plant p : plants) {
            if (p.isDead()) p.startDying();
        }
        plants.removeIf(Plant::isMarkedForRemoval);

        // -- Update projectiles
        for (Bullet b : bullets) b.update();

        // -- Lawnmower logic
        for (Lawnmower mower : mowers) mower.update(zombies, scoreCounter);
        mowers.removeIf(Lawnmower::isUsed);

        // -- Icon activation thresholds
        sunflowerIcon.setActive(sunCounter.getValue() >= 50);
        peaShooterIcon.setActive(sunCounter.getValue() >= 100);
        walnutIcon.setActive(sunCounter.getValue() >= 25);
        beetrootIcon.setActive(sunCounter.getValue() >= 125);

        if (gameOver) return;

        repaint();
    }

    // =======================================
    // SPAWN ZOMBIES BY STAGE PROGRESSION
    // =======================================
    private void spawnZombiesByStage(long seconds) {
        if (finalWaveTriggered) return;

        if (seconds < 60) {
            // Occasionally spawn one normal zombie
            if (random.nextInt(100) < 30) {
                spawnZombieRandom("normal");
            }
        } else if (seconds < 120) {
            // Spawn conehead and 1–2 normal zombies
            spawnZombieRandom("conehead");
            int count = 1 + random.nextInt(2);
            for (int i = 0; i < count; i++) spawnZombieRandom("normal");
        } else if (seconds < 180) {
            // Spawn normal, conehead, and sometimes a football zombie
            spawnZombieRandom("normal");
            spawnZombieRandom("conehead");
            if (random.nextInt(100) < 40) spawnZombieRandom("football");
        } else {
            // Final wave: 1–3 random zombies in every row
            for (int i = 0; i < rows.length; i++) {
                int rowY = rows[i];
                int count = 1 + random.nextInt(3); // 1 to 3 zombies in this row

                for (int j = 0; j < count; j++) {
                    int x = getWidth();
                    int type = random.nextInt(3); // 0 = normal, 1 = conehead, 2 = football

                    if (type == 0) {
                        zombies.add(new NormalZombie(x, rowY));
                    } else if (type == 1) {
                        zombies.add(new ConeheadZombie(x, rowY));
                    } else {
                        zombies.add(new FootballZombie(x, rowY));
                    }
                }
            }
            showWaveBanner = true;
            waveBannerTimer = 60;
            finalWaveTriggered = true;
        }
    }

    // =======================================
    // SPAWN A SINGLE ZOMBIE AT RANDOM ROW
    // =======================================
    private void spawnZombieRandom(String type) {
        int row = rows[random.nextInt(rows.length)];
        int x = getWidth();
        if (type.equals("normal")) {
            zombies.add(new NormalZombie(x, row));
        } else if (type.equals("conehead")) {
            zombies.add(new ConeheadZombie(x, row));
        } else if (type.equals("football")) {
            zombies.add(new FootballZombie(x, row));
        }
    }

    // =======================================
    // CHECK FOR LAWNMOWER IN SAME ROW
    // =======================================
    private Lawnmower getMowerInRow(int y) {
        for (Lawnmower mower : mowers) {
            if (Math.abs(mower.getY() - y) <= 20) {
                return mower;
            }
        }
        return null;
    }

    // =======================================
    // TRIGGER GAME OVER SEQUENCE
    // =======================================
    private void triggerGameOver() {
        if (gameOver) return;
        gameOver = true;
        timer.stop();

        int finalScore = scoreCounter.getScore();
        fadePanel.fadeIn(1000, () -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.setContentPane(new GameOverPanel(finalScore));
            frame.revalidate();
        });
    }

    // =======================================
    // MOUSE CLICK HANDLING
    // =======================================
    @Override
    public void mouseClicked(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        Point cell = snapToGrid(mx, my);

        System.out.println("Mouse clicked at: (" + mx + ", " + my + ")");
        Iterator<Sun> iterator = suns.iterator();
        while (iterator.hasNext()) {
            Sun sun = iterator.next();
            if (sun.contains(mx, my)) {
                iterator.remove();
                sunCounter.addSun(50);
                break;
            }
        }

        if (shovel.isActive()) {
            removePlantAt(cell);
            shovel.deactivate(); // Optional: auto-deactivate
            return;
        }
    }

    // =======================================
    // GRID ALIGNMENT LOGIC
    // =======================================
    private Point snapToGrid(int x, int y) {
        for (int rowY : rows) {
            for (int colX : columns) {
                if (x >= colX && x <= colX + CELL_WIDTH && y >= rowY && y <= rowY + CELL_HEIGHT) {
                    return new Point(colX, rowY);
                }
            }
        }
        return null;
    }

    // =======================================
    // REMOVE PLANT AND REFUND SUN
    // =======================================
    private void removePlantAt(Point cell) {
        Iterator<Plant> iterator = plants.iterator();

        while (iterator.hasNext()) {
            Plant plant = iterator.next();
            if (plant.getX() == cell.x && plant.getY() == cell.y) {
                iterator.remove();
                occupiedCells.remove(cell);

                int refund = plant.getCost() / 2;
                sunCounter.addSun(refund);
                System.out.println("Removed plant at: " + cell + " → Refunded: " + refund + " sun");
                break;
            }
        }
    }

    // =======================================
    // PLANT PLACEMENT METHODS
    // =======================================
    public void tryPlaceSunflower(Point p) {
        Point cell = snapToGrid(p.x, p.y);
        if (cell != null && !isPlantOccupied(cell) && sunCounter.spendSun(50)) {
            plants.add(new Sunflower(cell.x, cell.y));
            occupiedCells.add(cell);
        } else {
            System.out.println("Failed to place sunflower at " + p);
        }
    }

    public void tryPlacePeashooter(Point p) {
        Point cell = snapToGrid(p.x, p.y);
        if (cell != null && !isPlantOccupied(cell) && sunCounter.spendSun(100)) {
            plants.add(new Peashooter(cell.x, cell.y));
            occupiedCells.add(cell);
        } else {
            System.out.println("Failed to place peashooter at " + p);
        }
    }

    public void tryPlaceWalnut(Point p) {
        Point cell = snapToGrid(p.x, p.y);
        if (cell != null && !isPlantOccupied(cell) && sunCounter.spendSun(25)) {
            plants.add(new Walnut(cell.x, cell.y));
            occupiedCells.add(cell);
        } else {
            System.out.println("Failed to place walnut at " + p);
        }
    }

    public void tryPlaceBeetroot(Point p) {
        Point cell = snapToGrid(p.x, p.y);
        if (cell != null && !isPlantOccupied(cell) && sunCounter.spendSun(125)) {
            plants.add(new Beetroot(cell.x, cell.y));
            occupiedCells.add(cell);
        } else {
            System.out.println("Failed to place beetroot at " + p);
        }
    }

    // =======================================
    // HELPER: CHECK IF GRID CELL IS TAKEN
    // =======================================
    private boolean isPlantOccupied(Point cell) {
        for (Plant plant : plants) {
            if (plant.getX() == cell.x && plant.getY() == cell.y) {
                return true;
            }
        }
        return false;
    }

    // =======================================
    // UNUSED MOUSE EVENTS
    // =======================================
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    // (End of class)
}
