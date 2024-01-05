import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class Bullet {
    private int x;
    private int y;
    private final int speed;
    private BufferedImage bulletImage;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 5; // Adjust the speed as needed
        try {
            bulletImage = ImageIO.read(getClass().getResource("/picture/bullet.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void move() {
        y -= speed; // Move the bullet upwards
    }

    public boolean hitsEnemy(EnemyPlane enemy) {
        Rectangle bulletBounds = new Rectangle(x, y, bulletImage.getWidth(), bulletImage.getHeight());
        Rectangle enemyBounds = enemy.getBoundingBox();
        
        return bulletBounds.intersects(enemyBounds);
    }

    public void draw(Graphics g) {
        g.drawImage(bulletImage, x, y, 5, 10, null);
    }
}
