import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

        public class Main {
                private static JFrame gameFrame;
                private static PlaneShootingGame game;
                private static JFrame gameOverFrame;

                public static void main(String[] args) {
                SwingUtilities.invokeLater(Main::createAndShowStartPage);
                }

                private static JFrame createFrame(String title, int width, int height) {
                JFrame frame = new JFrame(title);
                frame.setResizable(false);
                frame.setSize(width, height);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                return frame;
                }

                private static JLabel createLabel(String text, int fontSize, int fontStyle) {
                JLabel label = new JLabel(text);
                label.setFont(new Font("Verdana", fontStyle, fontSize)); // Updated font to Verdana
                return label;
                }

                private static JButton createButton(String text, int fontSize) {
                JButton button = new JButton(text);
                button.setFont(new Font("Verdana", Font.PLAIN, fontSize)); // Updated font to Verdana
                return button;
                }

                private static void styleButton(JButton button) {
                button.setBackground(new Color(59, 89, 182));
                button.setForeground(Color.WHITE);
                button.setFocusPainted(false);
                button.setBorderPainted(false);
                button.setPreferredSize(new Dimension(150, 50));
                button.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(40, 60, 150)); // Change background color on hover
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(59, 89, 182)); // Restore original background color
                }
                });
                    }

                private static void createAndShowStartPage() {
                JFrame startFrame = createFrame("Main Menu", 400, 500);
                JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 90));

                JLabel mainMenuLabel = createLabel("AirPlane Shooting Game", 25, Font.BOLD);
                startPanel.add(mainMenuLabel);

                JButton startButton = createButton("Start", 24);
                JButton exitButton = createButton("Exit", 24);

                startButton.addActionListener(e -> {
                startFrame.dispose();
                startGame();
                });

                exitButton.addActionListener(e -> System.exit(0));

                styleButton(startButton);
                styleButton(exitButton);

                startPanel.add(startButton);
                startPanel.add(exitButton);

                startFrame.add(startPanel);
                startFrame.setVisible(true);
                }

            public static void showGameOverPage() {
            if (gameOverFrame != null) {
                gameOverFrame.dispose();
            }
            if (game != null) {
                game.stopGameTimer(); // Stop the game timer
            }
            gameOverFrame = new JFrame("Game Over");
            JPanel gameOverPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 60));
            JLabel gameOverLabel = new JLabel("Game Over! Score: " + game.getScore());
            JButton restartButton = new JButton("Restart");
            JButton mainMenuButton = new JButton("Main Menu");
            JButton exitButton = new JButton("Exit");

            gameOverLabel.setFont(new Font("Verdana", Font.PLAIN, 22));
            restartButton.setFont(new Font("Verdana", Font.PLAIN, 22));
            mainMenuButton.setFont(new Font("Verdana", Font.PLAIN, 21));
            exitButton.setFont(new Font("Verdana", Font.PLAIN, 22));

            gameOverPanel.add(gameOverLabel);
            gameOverPanel.add(restartButton);
            gameOverPanel.add(mainMenuButton);
            gameOverPanel.add(exitButton);

            gameOverFrame.add(gameOverPanel);
            gameOverFrame.setResizable(false);
            gameOverFrame.setSize(400, 500);
            gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameOverFrame.setLocationRelativeTo(null);

            restartButton.addActionListener(e -> {
                disposeGame(); // Dispose the game frame
                game.restartGame(); // Restart the game
                gameOverFrame.dispose(); // Dispose the game over frame
                startGame();
            });

            mainMenuButton.addActionListener(e -> {
                disposeGame();
                game.restartGame();
                gameOverFrame.dispose();
                createAndShowStartPage();
            });

            exitButton.addActionListener(e -> System.exit(0));

            styleButton(restartButton);
            styleButton(mainMenuButton);
            styleButton(exitButton);

            gameOverLabel.setText("Game Over! Score: " + game.getScore());

            gameOverFrame.setVisible(true);
        }

                private static void startGame() {
                    if (gameFrame != null) {
                    gameFrame.dispose();

                    }
                     if (game != null) {
                         game.restartGame(); 
                        }


                    game = new PlaneShootingGame();                   
                    gameFrame = new JFrame("AirPlane Shooting Game");
                    gameFrame.getContentPane().removeAll();
                    gameFrame.add(game);
                    gameFrame.setResizable(false);
                    gameFrame.setSize(600, 800);
                    gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    gameFrame.setLocationRelativeTo(null);


                    JMenuBar menuBar = new JMenuBar();
                    JMenu fileMenu = new JMenu("File");
                    JMenuItem restartMenuItem = new JMenuItem("Restart");
                    JMenuItem exitMenuItem = new JMenuItem("Exit");
                    fileMenu.add(restartMenuItem);
                    fileMenu.add(exitMenuItem);
                    menuBar.add(fileMenu);
                    gameFrame.setJMenuBar(menuBar);

                    restartMenuItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            game.restartGame(); // Call PlaneShootingGame's restartGame method

                        }
                    });

                    exitMenuItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.exit(0);
                        }

                    });
                    gameFrame.setVisible(true);
                }

                private static void disposeGame() {
                    if (gameFrame != null) {
                    gameFrame.dispose();
                        }
                    }


            }
