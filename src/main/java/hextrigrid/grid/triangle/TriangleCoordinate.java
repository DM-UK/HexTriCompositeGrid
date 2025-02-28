package hextrigrid.grid.triangle;

import java.awt.*;

public class TriangleCoordinate {
    public final int x;
    public final int y;

    public TriangleCoordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point getHexagonCoordinate() {
        int a = (2 * x) / 3;
        int b = (y / 2) - (a & 1);
        return new Point(a, b);
    }
}
