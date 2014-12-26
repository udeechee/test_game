package game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

class Bullet extends Polygon {

    double x, y;

    public Bullet(double x, double y, double size) {
        super(
                x, y,
                x, y - size,
                x + 2 * size, y - size,
                x + 3 * size, y,
                x + 2 * size, y + size,
                x, y + size,
                x, y
        );
        this.x = x;
        this.y = y;
        setFill(Color.GREY);
    }

    public void destroy() {
        this.setVisible(false);
    }

}
