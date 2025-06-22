package tetris;

import java.awt.Color;
import java.awt.Graphics;

public class Matrix {
    public static final int ROWS = 20;
    public static final int COLS = 10;
    public static final int CELL_SIZE = Shape.CELL_SIZE;

    private static final Color COLOR_OCCUPIED = Color.LIGHT_GRAY;
    private static final Color COLOR_EMPTY = Color.WHITE;

    boolean[][] map = new boolean[ROWS][COLS];
    Shape shape;

    public Matrix() {
        newGame();
    }

    public void newGame() {
        // Limpiar tablero
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                map[row][col] = false;
            }
        }
        // Crear nueva figura
        shape = Shape.newShape();
    }

    public boolean stepGame(Action action) {
        switch (action) {
            case LEFT:
                shape.x--;
                if (!actionAllowed()) shape.x++;
                break;
            case RIGHT:
                shape.x++;
                if (!actionAllowed()) shape.x--;
                break;
            case ROTATE_LEFT:
                shape.rotateLeft();
                if (!actionAllowed()) shape.undoRotate();
                break;
            case ROTATE_RIGHT:
                shape.rotateRight();
                if (!actionAllowed()) shape.undoRotate();
                break;
            case DOWN:
                shape.y++;
                if (!actionAllowed()) {
                    shape.y--;
                    return true; // Toca el fondo o colisión: fijar bloque
                }
                break;
            case HARD_DROP:
            case SOFT_DROP:
                while (!stepGame(Action.DOWN)) {
                    // Repite hasta que baje completamente
                }
                return true;
        }
        return false;
    }

    public boolean actionAllowed() {
        for (int r = 0; r < shape.rows; r++) {
            for (int c = 0; c < shape.cols; c++) {
                if (shape.map[r][c]) {
                    int boardY = shape.y + r;
                    int boardX = shape.x + c;

                    if (boardX < 0 || boardX >= COLS || boardY >= ROWS) {
                        return false;
                    }

                    if (boardY >= 0 && map[boardY][boardX]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public int lockDown() {
    for (int r = 0; r < shape.rows; r++) {
        for (int c = 0; c < shape.cols; c++) {
            if (shape.map[r][c]) {
                int x = shape.x + c;
                int y = shape.y + r;
                if (y >= 0 && y < ROWS && x >= 0 && x < COLS) {
                    map[y][x] = true;
                }
            }
        }
    }
    return clearLines();
    }


    public int clearLines() {
        int linesCleared = 0;
        for (int row = ROWS - 1; row >= 0; row--) {
            boolean full = true;
            for (int col = 0; col < COLS; col++) {
                if (!map[row][col]) {
                    full = false;
                    break;
                }
            }

            if (full) {
                // Desplazar filas hacia abajo
                for (int r = row; r > 0; r--) {
                    for (int c = 0; c < COLS; c++) {
                        map[r][c] = map[r - 1][c];
                    }
                }
                // Vaciar fila superior
                for (int c = 0; c < COLS; c++) {
                    map[0][c] = false;
                }
                row++; // Volver a chequear esta fila después del desplazamiento
                linesCleared++;
            }
        }
        return linesCleared;
    }

    public void paint(Graphics g) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                g.setColor(map[row][col] ? COLOR_OCCUPIED : COLOR_EMPTY);
                g.fill3DRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE, true);
            }
        }
        // Pintar la figura actual
        shape.paint(g);
    }
}
