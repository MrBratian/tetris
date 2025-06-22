package tetris;

public class Tetris {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GameMain();
            }
        });
    }
}
