package tetris;
import javax.swing.*;

public class ScoreBoard extends JPanel {
    private int score = 0;
    private JTextField tfScore = new JTextField("0", 5);

    public ScoreBoard() {
        add(new JLabel("Score:"));
        add(tfScore);
        tfScore.setEditable(false);
    }

    public void updateScore(int lines) {
        score += lines * 100;
        tfScore.setText(String.valueOf(score));
    }
}
