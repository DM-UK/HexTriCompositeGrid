package hextrigrid;

import hextrigrid.grid.composite.CompositeGrid;
import hextrigrid.grid.composite.CompositeGridRenderer;
import hextrigrid.grid.Tile;
import hextrigrid.grid.Edge;
import hextrigrid.grid.Vertex;

import java.awt.*;
import java.util.Random;

public class CompositeGridDemoRenderer extends CompositeGridRenderer {
    private final static float[] dashPattern = {5, 5}; // 5 pixels on, 5 pixels off
    private final static BasicStroke dashedStroke = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, dashPattern, 0);
    private final static BasicStroke defaultStroke = new BasicStroke(2.0f);
    private final static BasicStroke borderStroke = new BasicStroke(6.0f);

    public CompositeGridDemoRenderer(CompositeGrid grid, double edgeLength) {
        super(grid, edgeLength);
    }

    @Override
    protected void drawHexagon(Graphics2D g2d, Tile tile) {
        if (tile ==null)
            return;

        Random random = new Random(tile.hashCode());
        Color randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        g2d.setColor(randomColor);
        g2d.fill(hexagon.toShape());
    }

    @Override
    protected void drawInternalEdge(Graphics2D g2d, Edge edge, double x, double y) {

        Random random = new Random(edge.hashCode());
        Color randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        g2d.setColor(randomColor);
       // g2d.setColor(Color.black);
        g2d.setStroke(dashedStroke);
        g2d.drawLine(0, 0, (int) x, (int) y);
    }

    @Override
    protected void drawExternalEdge(Graphics2D g2d, Edge edge, double x, double y) {
        Random random = new Random(edge.hashCode());
        Color randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        g2d.setColor(randomColor);
       // g2d.setColor(Color.black);
        g2d.setStroke(borderStroke);
        g2d.drawLine(0, 0, (int) x, (int) y);
    }

    @Override
    protected void drawHexagonCentreVertex(Graphics2D g2d, Vertex vertex) {
        g2d.setStroke(defaultStroke);
        g2d.setColor(Color.black);
        g2d.fillOval(-9, -9, 18, 18);
        Random random = new Random(vertex.hashCode());
        Color randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        g2d.setColor(randomColor);

        //g2d.setColor(Color.red);
        g2d.fillOval(-6, -6, 12, 12);
    }

    @Override
    protected void drawExternalEdgeVertex(Graphics2D g2d, Vertex vertex) {
        g2d.setStroke(defaultStroke);
        g2d.setColor(Color.black);
        g2d.fillOval(-9, -9, 18, 18);
        Random random = new Random(vertex.hashCode());
        Color randomColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        g2d.setColor(randomColor);

        //g2d.setColor(Color.red);
        g2d.fillOval(-6, -6, 12, 12);
    }
}
