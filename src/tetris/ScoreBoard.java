package tetris;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;

public class ScoreBoard extends JPanel {
    private JLabel scoreLabel;
    private int score;

    public ScoreBoard() {
        score = 0;
        scoreLabel = new JLabel("Puntaje: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(scoreLabel);
    }

    public void addScore(int linesCleared) {
        switch (linesCleared) {
            case 1: score += 100; break;
            case 2: score += 300; break;
            case 3: score += 500; break;
            case 4: score += 800; break;
            default: break;
        }
        updateLabel();
    }

    public void resetScore() {
        score = 0;
        updateLabel();
    }

    private void updateLabel() {
        scoreLabel.setText("Puntaje: " + score);
    }
}
