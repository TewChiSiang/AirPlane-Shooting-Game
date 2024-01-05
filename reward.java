import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

public class reward {
    private int x;
    private int y;
    private final int speed;
    private BufferedImage rewardImage;
    private static final int windowHeight = 800; // Adjust the window height as needed
    private boolean rewardActive = true; // Initialize to true if rewards should be active initially
    private boolean isScoreReward;

    public reward(int x, int y, boolean isScoreReward) {
        this.x = x;
        this.y = y;
        this.speed = 2; // Adjust the reward item's movement speed
        this.isScoreReward = isScoreReward;
        try {
            rewardImage = ImageIO.read(getClass().getResource("/picture/reward.png"));
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

    public boolean isActive() {
        return rewardActive;
    }

    public boolean isScoreReward() {
        return isScoreReward;
    }

    public void move() {
        y += speed; // Move the reward item downward
        int rewardBottom = y + rewardImage.getHeight(); // Calculate the bottom edge of the reward

        if (rewardBottom > windowHeight) {
            // When the bottom of the reward goes beyond the bottom of the screen, deactivate it
            rewardActive = false;
        }
    }

    public void draw(Graphics g) {
        if (rewardActive) {
            g.drawImage(rewardImage, x, y, 50, 50, null); // Adjust the reward item's size
        }
    }
}
