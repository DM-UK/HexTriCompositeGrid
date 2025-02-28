package hextrigrid.grid;

import trianglegrid.Edge;
import trianglegrid.Vertex;

import java.awt.*;
import java.util.Random;

public class DemoHexaTriagonalGridRenderer extends HexaTriagonalGridRenderer {
    private final static float[] dashPattern = {5, 5}; // 5 pixels on, 5 pixels off
    private final static BasicStroke dashedStroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dashPattern, 0);
    private final static BasicStroke defaultStroke = new BasicStroke(2.0f);
    private final static BasicStroke borderStroke = new BasicStroke(6.0f);

    public DemoHexaTriagonalGridRenderer(HexaTriagonalGrid grid, double edgeLength) {
        super(grid, edgeLength);
    }

    @Override
    protected void drawHexagon(Graphics2D g2d, Tile tile) {
        Random random = new Random();
        Color randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        g2d.setColor(randomColor);

        if (tile != null)
            g2d.fill(hexagon.toShape());
    }

    @Override
    protected void drawInternalEdge(Graphics2D g2d, Edge edge, double x, double y) {
        g2d.setColor(Color.black);
        g2d.setStroke(dashedStroke);
        g2d.drawLine(0, 0, (int) x, (int) y);
    }

    @Override
    protected void drawExternalEdge(Graphics2D g2d, Edge edge, double x, double y) {
        g2d.setColor(Color.black);
        g2d.setStroke(borderStroke);
        g2d.drawLine(0, 0, (int) x, (int) y);
    }

    @Override
    protected void drawHexagonCentreVertex(Graphics2D g2d, Vertex vertex, int x, int y) {
        g2d.setStroke(defaultStroke);
        g2d.setColor(Color.white);
        g2d.fillOval(-12, -12, 24, 24);
        g2d.setColor(Color.black);
        g2d.drawOval(-12, -12, 24, 24);
        TriangleCoordinate triCoordinate = new TriangleCoordinate(x, y);
        Point hexCoordinate = triCoordinate.getHexagonCoordinate();
        g2d.drawString(hexCoordinate.x+","+(hexCoordinate.y)+",", -7, 5);
    }

    @Override
    protected void drawExternalEdgeVertex(Graphics2D g2d, Vertex vertex, int x, int y) {
        g2d.setStroke(defaultStroke);
        g2d.setColor(Color.black);
        g2d.fillOval(-9, -9, 18, 18);
        g2d.setColor(Color.red);
        g2d.fillOval(-6, -6, 12, 12);
    }
}
