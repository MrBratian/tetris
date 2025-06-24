package tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameMain extends JFrame {
    private Matrix matrix;
    private ScoreBoard scoreBoard;
    private HoldBox holdBox;
    private JButton startButton;
    private JButton retryButton;
    private JPanel mainPanel;
    private boolean paused = false;
    private JLabel pauseLabel;
    private State state = State.INITIALIZED;
    private boolean canHold = true;

    public GameMain() {
        super("Tetris - Hold & Accumulación");

        matrix = new Matrix();
        scoreBoard = new ScoreBoard();
        holdBox = new HoldBox();

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(scoreBoard, BorderLayout.NORTH);
        mainPanel.add(matrixPanel(), BorderLayout.CENTER);
        mainPanel.add(holdBoxPanel(), BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);

        startButton = new JButton("Iniciar Juego");
        startButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        startButton.addActionListener(e -> {
            matrix.newGame();
            scoreBoard.reset();
            holdBox.clear();
            state = State.PLAYING;
            canHold = true;
            mainPanel.remove(startButton);
            mainPanel.revalidate();
            mainPanel.repaint();
            requestFocusInWindow();
        });
        mainPanel.add(startButton, BorderLayout.SOUTH);

        
        
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    if (state == State.PLAYING) {
                        paused = true;
                        state = State.READY;
                        showPauseOverlay(true);
                    } else if (state == State.READY && paused) {
                        paused = false;
                        state = State.PLAYING;
                        showPauseOverlay(false);
                    }
                    return;
                }

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
                            retryButton.setVisible(true);
                            repaint();
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
    }

    private JPanel matrixPanel() {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                matrix.paint(g);
            }
        };
        panel.setPreferredSize(new Dimension(Matrix.COLS * Shape.CELL_SIZE, Matrix.ROWS * Shape.CELL_SIZE));
        return panel;
    }

    private JPanel holdBoxPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Hold Piece");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        holdBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(holdBox);

        panel.add(Box.createVerticalStrut(10));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Controles"));

        controlPanel.add(new JLabel("← / →  : Mover"));
        controlPanel.add(new JLabel("↓       : Bajar"));
        controlPanel.add(new JLabel("↑       : Rotar"));
        controlPanel.add(new JLabel("ESPACIO: Caída rápida"));
        controlPanel.add(new JLabel("SHIFT   : Cambiar ficha"));

        for (Component comp : controlPanel.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setFont(new Font("Monospaced", Font.PLAIN, 12));
            }
        }

        panel.add(controlPanel);
        panel.setPreferredSize(new Dimension(200, 260));
        return panel;
    }

    private void showPauseOverlay(boolean show) {
        if (pauseLabel == null) {
            pauseLabel = new JLabel("PAUSA", SwingConstants.CENTER);
            pauseLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
            pauseLabel.setForeground(Color.RED);
            pauseLabel.setOpaque(true);
            pauseLabel.setBackground(new Color(0, 0, 0, 150));
            pauseLabel.setBounds(0, 0, Matrix.COLS * Shape.CELL_SIZE, Matrix.ROWS * Shape.CELL_SIZE);
            getLayeredPane().add(pauseLabel, JLayeredPane.POPUP_LAYER);
        }
        pauseLabel.setVisible(show);
        requestFocusInWindow();
    }

    public static void main(String[] args) {
        new GameMain();
    }
}
