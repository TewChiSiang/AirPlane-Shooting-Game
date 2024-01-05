import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class EnemyBullet {
    private int x;
    private int y;
    private final int speed;
    private BufferedImage enemyBulletImage; // Bullet image

    public EnemyBullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 2; // Adjust bullet speed
        loadBulletImage(); // Load bullet image
    }

    private void loadBulletImage() {
        try {
            enemyBulletImage = ImageIO.read(getClass().getResource("/picture/enemybullet.png"));
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
    
    public int getWidth() {
        return enemyBulletImage.getWidth();
    }

    public int getHeight() {
        return enemyBulletImage.getHeight();
    }
    
    public void move() {
        y += speed; // Move the bullet downwards
    }

    public void draw(Graphics g) {
        g.drawImage(enemyBulletImage, x, y, null);
    }
}
