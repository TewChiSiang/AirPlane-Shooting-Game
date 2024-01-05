import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class EnemyPlane {
    private int x, y;
    public final int speed;
    private List<EnemyBullet> enemyBullets;
    private BufferedImage enemyImage;
    private Timer shootTimer;
    private final int shootingInterval = 2000; // Shooting interval in milliseconds
    private int maxBullets = 1; // Maximum number of bullets per enemy plane
    private int bulletCount = 0; // Current number of bullets
    private Timer bulletCountTimer;
    private int health = 2; // Set initial health to 2

    public EnemyPlane(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 1; // Adjust speed as needed
        enemyBullets = new ArrayList<>();

        // Initialize shooting timer, shooting at specified intervals
        shootTimer = new Timer(shootingInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shoot();
            }
        });
        shootTimer.start();

        try {
            // Read the enemy plane image
            enemyImage = ImageIO.read(getClass().getResource("/picture/enemyPlane.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Bullet count timer, resetting bullet count and shooting at intervals
        bulletCountTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bulletCount = 0; // Reset bullet count
                shoot(); // Trigger shooting
            }
        });
        bulletCountTimer.start(); // Start the timer
    }

    public List<EnemyBullet> getEnemyBullets() {
        return enemyBullets;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getImageWidth() {
        return enemyImage.getWidth();
    }

    public int getImageHeight() {
        return enemyImage.getHeight();
    }

    public void decreaseHealth() {
        health--;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    public void move() {
        y += speed; // Move the enemy plane downwards
        updateBullets();
    }

    private void updateBullets() {
        // Update bullet positions
        Iterator<EnemyBullet> iterator = enemyBullets.iterator();
        while (iterator.hasNext()) {
            EnemyBullet bullet = iterator.next();
            bullet.move();
            if (bullet.getY() > 800) { // Replace with actual screen height
                iterator.remove(); // Remove bullets that are out of the screen using iterator
            }
        }
    }

    public void shoot() {
        if (bulletCount < maxBullets) {
            // Create a new enemy bullet and add it to the bullet list
            EnemyBullet enemyBullet = new EnemyBullet(x + 15, y + 30); // Adjust bullet position
            enemyBullets.add(enemyBullet);
            bulletCount++;
        }
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(x, y, getImageWidth(), getImageHeight());
    }

    public void draw(Graphics g) {
        // Draw the enemy plane
        g.drawImage(enemyImage, x, y, 40, 40, null);

        // Draw bullets fired by the enemy plane
        for (EnemyBullet bullet : enemyBullets) {
            bullet.draw(g);
        }
    }
}
