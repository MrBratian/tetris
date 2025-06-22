package tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Shape {
    public static final int CELL_SIZE = 32;

    private static Shape shape;  // singleton
    int x, y;
    boolean[][] map;
    int rows, cols;
    int shapeIdx;
    private boolean[][] mapSaved = new boolean[5][5];

    private static final boolean[][][] SHAPES_MAP = {
        {{ false, true, false }, { true, true, false }, { true, false, false }},
        {{ false, true, false }, { false, true, false }, { false, true, true }},
        {{ true, true }, { true, true }},
        {{ false, true, false }, { false, true, true }, { false, false, true }},
        {{ false, true, false, false }, { false, true, false, false }, { false, true, false, false }, { false, true, false, false }},
        {{ false, true, false }, { false, true, false }, { true, true, false }},
        {{ false, true, false }, { true, true, true }, { false, false, false }}
    };

    private static final Color[] SHAPES_COLOR = {
        new Color(245, 45, 65), Color.ORANGE, Color.YELLOW, Color.GREEN,
        Color.CYAN, new Color(76, 181, 245), Color.PINK
    };

    private static final Random rand = new Random();

    private Shape() {}

    public static Shape newShape() {
        if (shape == null) shape = new Shape();

        shape.shapeIdx = rand.nextInt(SHAPES_MAP.length);
        shape.map = SHAPES_MAP[shape.shapeIdx];
        shape.rows = shape.map.length;
        shape.cols = shape.map[0].length;
        shape.x = (Matrix.COLS - shape.cols) / 2;

        for (int r = 0; r < shape.rows; r++) {
            for (int c = 0; c < shape.cols; c++) {
                if (shape.map[r][c]) {
                    shape.y = -r;
                    return shape;
                }
            }
        }
        shape.y = 0;
        return shape;
    }

    public Shape cloneShape() {
        Shape clone = new Shape();
        clone.shapeIdx = this.shapeIdx;
        clone.rows = this.rows;
        clone.cols = this.cols;
        clone.map = new boolean[rows][cols];
        for (int r = 0; r < rows; r++) {
            System.arraycopy(this.map[r], 0, clone.map[r], 0, cols);
        }
        clone.x = 0;
        clone.y = 0;
        return clone;
    }

    public void rotateRight() {
        for (int r = 0; r < rows; r++)
            System.arraycopy(map[r], 0, mapSaved[r], 0, cols);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                map[r][c] = mapSaved[cols - 1 - c][r];
    }

    public void rotateLeft() {
        for (int r = 0; r < rows; r++)
            System.arraycopy(map[r], 0, mapSaved[r], 0, cols);
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                map[r][c] = mapSaved[c][rows - 1 - r];
    }

    public void undoRotate() {
        for (int r = 0; r < rows; r++)
            System.arraycopy(mapSaved[r], 0, map[r], 0, cols);
    }

    public void paint(Graphics g) {
        int yOffset = 1;
        g.setColor(SHAPES_COLOR[shapeIdx]);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (map[r][c]) {
                    g.fill3DRect((x + c) * CELL_SIZE, (y + r) * CELL_SIZE + yOffset,
                                 CELL_SIZE, CELL_SIZE, true);
                }
            }
        }
    }

    public static void paintMini(Graphics g, Shape shape) {
        int cell = 20;
        g.setColor(SHAPES_COLOR[shape.shapeIdx]);
        for (int r = 0; r < shape.rows; r++) {
            for (int c = 0; c < shape.cols; c++) {
                if (shape.map[r][c]) {
                    g.fill3DRect(c * cell + 10, r * cell + 10, cell, cell, true);
                }
            }
        }
    }
}
