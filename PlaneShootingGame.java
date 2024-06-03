import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PlaneShootingGame extends JPanel implements ActionListener {
    // Game state variables
    private int score = 0;
    private double time = 60;
    private long startTime; 
    private int elapsedSeconds = 0;
    private double countdownDuration = 60;

    // Player state variables
    private int planeX = 50;
    private int planeY = 250;
    private boolean isMovingUp = false;
    private boolean isMovingDown = false;
    private boolean isMovingLeft = false;
    private boolean isMovingRight = false;

    // Game objects
    private List<Bullet> bullets = new ArrayList<>();
    private List<EnemyPlane> enemyPlanes = new ArrayList<>();
    private BufferedImage background;
    private BufferedImage planeImage;
    private reward Reward;
    private int rewardCount = 0;
    private boolean RewardActive = false;
    private long rewardStartTime;
    private boolean multiBulletMode = false;
    private int multiBulletCount = 0;
    
    // Timer for game loop
    private Timer timer;
    
      public int getScore() {
        return score;
    }
 
    // Constructor
    public PlaneShootingGame() {
        timer = new Timer(10, this);
        timer.start();
        startTime = System.currentTimeMillis();
        setupKeyBindings();
        loadResources();
        setFocusable(true);
    }

    // Setting up key bindings
    private void setupKeyBindings() {
        removeKeyListeners();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyRelease(e);
            }
        });
    }

    // Loading game resources (images)
    private void loadResources() {
        try {
            planeImage = ImageIO.read(getClass().getResource("/picture/AirPlane.png"));
            background = ImageIO.read(getClass().getResource("/picture/Background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Removing all existing key listeners
    private void removeKeyListeners() {
        for (KeyListener listener : getKeyListeners()) {
            removeKeyListener(listener);
        }
    }
    public void handleKeyPress(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE) {
            if (multiBulletMode) {
            int bulletOffset = 20;
            bullets.add(new Bullet(planeX + 25 - bulletOffset, planeY - 5));
            bullets.add(new Bullet(planeX + 35 - bulletOffset, planeY - 5));
            bullets.add(new Bullet(planeX + 45 - bulletOffset, planeY - 5));
            bullets.add(new Bullet(planeX + 55 - bulletOffset, planeY - 5));
            bullets.add(new Bullet(planeX + 65 - bulletOffset, planeY - 5));
            bullets.add(new Bullet(planeX + 75 - bulletOffset, planeY - 5));
            multiBulletCount++;
            if (multiBulletCount >= 10) {
                multiBulletMode = false;
            }
        } else {
            int bulletOffset = 2; // Offset from the center of the plane    
            bullets.add(new Bullet(planeX + 35 - bulletOffset, planeY - 5));
        }
    } else {
        updatePlaneDirection(key, true);
    }
    }

    public void handleKeyRelease(KeyEvent e) {
        updatePlaneDirection(e.getKeyCode(), false);
    }

    public void updatePlaneDirection(int key, boolean isPressed) {
        switch (key) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                isMovingUp = isPressed;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                isMovingDown = isPressed;
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                isMovingLeft = isPressed;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                isMovingRight = isPressed;
                break;
        }
    }

  

    public void spawnRandomEnemyPlane() {
    Random rand = new Random();
    int randomX = rand.nextInt(getWidth() - 10);   
    int factor = elapsedSeconds / 7; 
  
    if (enemyPlanes.size() < (2 + factor)) {
        enemyPlanes.add(new EnemyPlane(randomX, -30));
    }
}

    @Override
    public void actionPerformed(ActionEvent e) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;

        if (elapsedTime >= (elapsedSeconds + 1) * 1000) {
            elapsedSeconds++;
        }

        if (elapsedSeconds >= countdownDuration) {
            timer.stop();
            Main.showGameOverPage();
            
        } else {
            time = countdownDuration - elapsedSeconds;
        }

        if (elapsedSeconds % 4 == 0 && elapsedSeconds != 0) {
            spawnRandomEnemyPlane();
        }

        updatePlayerPosition();

        if (isMovingUp && planeY > 0) {
            planeY -= 4;
        }
        if (isMovingDown && planeY < getHeight() - 30) {
            planeY += 4;
        }
        if (isMovingLeft && planeX > 0) {
            planeX -= 4;
        }
        if (isMovingRight && planeX < getWidth() - 10) {
            planeX += 4;
        }

        if (isFiring()) {
            moveBullets();
        }
        
        if (enemyBulletHitsPlayer()) {
           score -= 2;
    }   
        if (!RewardActive && rewardCount < 5 && Math.random() < 0.01) {
            int randomX = (int) (Math.random() * getWidth());
            boolean isScoreReward = Math.random() < 0.5; // Randomly determine the reward type
            Reward  = new reward(randomX, -30, isScoreReward);
            RewardActive = true;
            rewardStartTime = System.currentTimeMillis();
            rewardCount++;
        }
        if (RewardActive) {
            Reward.move();

            if (playerHitsreward()) {
                handleRewardEffect();
                RewardActive = false; // Player collected the reward
            }

            long currentRewardTime = System.currentTimeMillis();
            long rewardElapsedTime = currentRewardTime - rewardStartTime;
            if (rewardElapsedTime >= 8000) { // Reward duration: 5 seconds
                RewardActive = false; // Reward expired
            }
        
       }       
        moveEnemyPlanes();
        checkCollisions();

        repaint();     
    }
    
    private boolean playerHitsreward() {
        Rectangle playerBounds = new Rectangle(planeX, planeY, planeImage.getWidth(), planeImage.getHeight());
        Rectangle powerUpBounds = new Rectangle(Reward.getX(), Reward.getY(), 30, 30); 

        return playerBounds.intersects(powerUpBounds);
    }

    private void handleRewardEffect() {
        if (Reward.isScoreReward()) {
            score += 20; // Increase score for score reward
        } else {
            // Handle the effect for the other type of reward (increasing bullets)
            // For example, activate multi-bullet mode or increase bullet count
            multiBulletMode = true;
            multiBulletCount = 0;
        }
    }

    public void updatePlayerPosition() {
        // Update player position logic here
    }

    // Checking if the player is firing
    public boolean isFiring() {
        return !bullets.isEmpty();
    }

    // Moving bullets
    public void moveBullets() {
        Iterator<Bullet> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Bullet bullet = iterator.next();
            bullet.move();
            if (bullet.getY() < 0) {
                iterator.remove();
            }
        }
    }

    // Moving enemy planes
    public void moveEnemyPlanes() {
        Iterator<EnemyPlane> iterator = enemyPlanes.iterator();
        while (iterator.hasNext()) {
            EnemyPlane enemyPlane = iterator.next();
            enemyPlane.shoot();
            enemyPlane.move();
            if (enemyPlane.getY() > getHeight()) {
                iterator.remove();
            }
        }
    }

    protected void checkCollisions() {
    checkPlayerWithEnemyCollisions();
    checkBulletWithEnemyCollisions();
}

private void checkPlayerWithEnemyCollisions() {
    Iterator<EnemyPlane> enemyIterator = enemyPlanes.iterator();
    while (enemyIterator.hasNext()) {
        EnemyPlane enemyPlane = enemyIterator.next();
        if (playerHitsEnemy(enemyPlane)) {
            score -= 4; // Deduct points on collision
            enemyIterator.remove(); // Remove collided enemy plane
            // Add logic for player health or game over here if needed
        }
    }
}

private void checkBulletWithEnemyCollisions() {
    Iterator<Bullet> bulletIterator = bullets.iterator();
    while (bulletIterator.hasNext()) {
        Bullet bullet = bulletIterator.next();
        if (checkAndHandleBulletEnemyCollision(bullet, bulletIterator)) {
            break; // Breaks if a bullet has hit an enemy
        }
    }
}

private boolean checkAndHandleBulletEnemyCollision(Bullet bullet, Iterator<Bullet> bulletIterator) {
    for (EnemyPlane enemyPlane : enemyPlanes) {
        if (bulletHitsEnemy(bullet, enemyPlane)) {
            enemyPlane.decreaseHealth();
            bulletIterator.remove(); // Remove the bullet on hit

            if (enemyPlane.isDestroyed()) {
                enemyPlanes.remove(enemyPlane);
                score += 5; // Increase score for destroying an enemy
            }
            return true; // Bullet hit an enemy
        }
    }
    return false; // Bullet did not hit any enemy
}

public boolean bulletHitsEnemy(Bullet bullet, EnemyPlane enemyPlane) {
    return bullet.getX() >= enemyPlane.getX() &&
           bullet.getX() <= enemyPlane.getX() + 30 &&
           bullet.getY() >= enemyPlane.getY() &&
           bullet.getY() <= enemyPlane.getY() + 10;
}

public boolean playerHitsEnemy(EnemyPlane enemyPlane) {
    Rectangle playerBounds = new Rectangle(planeX + 20, planeY + 20, 
                                           planeImage.getWidth() - 40, 
                                           planeImage.getHeight() - 40);
    Rectangle enemyBounds = new Rectangle(enemyPlane.getX() + 20, enemyPlane.getY() + 20, 
                                          enemyPlane.getImageWidth() - 40, 
                                          enemyPlane.getImageHeight() - 40);
    return playerBounds.intersects(enemyBounds);
}
    
    public boolean enemyBulletHitsPlayer() {
        Rectangle playerBounds = new Rectangle(planeX, planeY, planeImage.getWidth(), planeImage.getHeight());
        for (EnemyPlane enemyPlane : enemyPlanes) {
            for (EnemyBullet bullet : enemyPlane.getEnemyBullets()) {
                Rectangle bulletBounds = new Rectangle(bullet.getX(), bullet.getY(), bullet.getWidth(), bullet.getHeight());
                if (playerBounds.intersects(bulletBounds)) {
                    // Remove the bullet on collision
                    enemyPlane.getEnemyBullets().remove(bullet);
                    return true; // Collision detected
                }
            }
        }
        return false; // No collision
    }
      
    public void restartGame() {
        // Necessary cleanup like stopping the timer
        timer.stop();
        // Clearing lists of bullets and enemy planes
        bullets.clear();
        enemyPlanes.clear();
        // Reinitializing game state
        time = countdownDuration;
        elapsedSeconds = 0;
        // Restarting timer and other necessary components
        startTime = System.currentTimeMillis(); 
        timer = new Timer(10, this);
        timer.start();
        repaint();
        setFocusable(true);
        System.gc();
    }

    public void stopGameTimer() {
        if (timer != null) {
            timer.stop();
        }
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Drawing the background and the player's plane
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        g.drawImage(planeImage, planeX, planeY, 70, 70, this); // Adjusting size and position
        
        // Drawing enemy planes
        for (EnemyPlane enemyPlane : enemyPlanes) {
            enemyPlane.draw(g);
        }
        
        // Drawing reward if active
        if (RewardActive) {
            Reward.draw(g);
        }
        
        // Drawing bullets if firing
        if (isFiring()) {
            for (Bullet bullet : bullets) {
                bullet.draw(g);
            }
        }

        // Displaying score and time
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Time: " + time, 10, 40);
    }
}