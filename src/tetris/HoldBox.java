package tetris;

import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JPanel;

public class HoldBox extends JPanel {
    private Shape heldShape;
    private boolean hasShape = false;

    public void setHeldShape(Shape shape) {
        this.heldShape = shape;
        this.hasShape = (shape != null);
        repaint();
    }
    
    public void clear() {
        heldShape = null;
        repaint();
    }

    
    public Shape getHeldShape() {
        return heldShape;
    }

    public boolean hasShape() {
        return hasShape;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (heldShape != null) {
            Shape.paintMini(g, heldShape);  // Método estático que pintará versión mini
        } else {
            g.setColor(Color.GRAY);
            g.drawRect(5, 5, 100, 100);
            g.drawString("Hold", 40, 120);
        }
    }
}
