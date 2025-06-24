package tetris;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class GameMain extends JFrame {
    private Matrix matrix;
    private ScoreBoard scoreBoard;
    private HoldBox holdBox;
    private boolean canHold = true;
    private State state;

    public GameMain() {
        super("Tetris - Hold & Accumulación");

        matrix = new Matrix();
        scoreBoard = new ScoreBoard();
        holdBox = new HoldBox();

        setLayout(new BorderLayout());
        add(scoreBoard, BorderLayout.NORTH);
        add(matrixPanel(), BorderLayout.CENTER);
        add(holdBoxPanel(), BorderLayout.EAST);

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (state != State.PLAYING) return;

                Action action = null;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT: action = Action.LEFT; break;
                    case KeyEvent.VK_RIGHT: action = Action.RIGHT; break;
                    case KeyEvent.VK_DOWN: action = Action.DOWN; break;
                    case KeyEvent.VK_UP: action = Action.ROTATE_RIGHT; break;
                    case KeyEvent.VK_SPACE: action = Action.HARD_DROP; break;
                    case KeyEvent.VK_SHIFT:
                        if (canHold) {
                            Shape temp = holdBox.getHeldShape();
                            holdBox.setHeldShape(matrix.shape.cloneShape());
                            matrix.shape = (temp == null) ? Shape.newShape() : temp;
                            canHold = false;
                            repaint();
                        }
                        return;
                }
                if (action != null) {
                    boolean atBottom = matrix.stepGame(action);
                    if (atBottom) {
                        int cleared = matrix.lockDown();
                        scoreBoard.addScore(cleared);
                        canHold = true;
                        matrix.shape = Shape.newShape();
                        if (matrix.isGameOver()) {
                            state = State.GAMEOVER;
                            JOptionPane.showMessageDialog(null, "¡Game Over!");
                        }
                    }
                    repaint();
                }
            }
        });

        Timer timer = new Timer(500, e -> {
            if (state == State.PLAYING) {
                boolean atBottom = matrix.stepGame(Action.DOWN);
                if (atBottom) {
                    int cleared = matrix.lockDown();
                    scoreBoard.addScore(cleared);
                    canHold = true;
                    matrix.shape = Shape.newShape();
                    if (matrix.isGameOver()) {
                        state = State.GAMEOVER;
                        JOptionPane.showMessageDialog(null, "¡Game Over!");
                    }
                }
                repaint();
            }
        });
        timer.start();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        matrix.newGame();
        state = State.PLAYING;
    }

    private JPanel matrixPanel() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                matrix.paint(g);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(Matrix.COLS * Shape.CELL_SIZE,
                                     Matrix.ROWS * Shape.CELL_SIZE);
            }
        };
        return p;
    }

    private JPanel holdBoxPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel("Hold Piece"), BorderLayout.NORTH);
        p.add(holdBox, BorderLayout.CENTER);
        p.setPreferredSize(new Dimension(150, 150));
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameMain::new);
    }
}
