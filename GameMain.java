package tetris;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameMain extends JFrame implements StateTransition {
    private Matrix matrix = new Matrix();
    private GamePanel gamePanel = new GamePanel();
    private ScoreBoard scoreBoard = new ScoreBoard();

    public GameMain() {
        setTitle("Tetris");
        setLayout(new BorderLayout());
        add(gamePanel, BorderLayout.CENTER);
        add(scoreBoard, BorderLayout.EAST);
        setSize(400, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:  matrix.stepGame(Action.LEFT); break;
                    case KeyEvent.VK_RIGHT: matrix.stepGame(Action.RIGHT); break;
                    case KeyEvent.VK_DOWN:  matrix.stepGame(Action.DOWN); break;
                    case KeyEvent.VK_UP:    matrix.stepGame(Action.ROTATE_RIGHT); break;
                }
                gamePanel.repaint();
            }
        });

        Timer timer = new Timer(1000, e -> {
            matrix.stepGame(Action.DOWN);
            gamePanel.repaint();
        });
        timer.start();
    }

    class GamePanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            matrix.paint(g);
        }
    }

    public void initGame() {}
    public void newGame() {}
    public void startGame() {}
    public void stopGame() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameMain::new);
    }
}
