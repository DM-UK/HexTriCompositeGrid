package hextrigrid.grid.composite;

import hextrigrid.grid.Tile;
import hextrigrid.grid.triangle.TriangleCoordinate;
import hextrigrid.grid.Edge;
import hextrigrid.grid.triangle.TriangleGrid;
import hextrigrid.grid.triangle.TriangleGridNode;
import hextrigrid.grid.Vertex;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public abstract class CompositeGridRenderer {
    /** Grid to render */
    private final CompositeGrid grid;
    /** Hexagon used for geometry */
    protected final Hexagon hexagon;
    /** Triangle shape in the two orientations  */
    private final Shape pointyUpTriangle;
    private final Shape pointyDownTriangle;
    /** Distance between two triangles along x-axis  */
    private final double columnSpacing;
    /** Distance between two triangles along y-axis  */
    private final double rowSpacing;
    /** Offset to be applied to the x-axis on every other row*/
    private final double oddRowOffset;

    /**
     * Constructs a CompositeGridRenderer with a given grid and edge length.
     *
     * @param grid The CompositeGrid grid to render.
     * @param edgeLength The length of each hexagon's edge.
     */
    public CompositeGridRenderer(CompositeGrid grid, double edgeLength) {
        this.grid = grid;
        //initialize variables needed for geometry
        this.hexagon = new Hexagon(edgeLength);
        this.pointyUpTriangle = hexagon.createTriangle(0, 1);
        this.pointyDownTriangle = hexagon.createTriangle(3, 4);
        this.columnSpacing = hexagon.edgeLength;
        this.rowSpacing = hexagon.halfHeight;
        this.oddRowOffset = hexagon.halfEdgeLength;
    }

    /**
     * Draws the entire grid on the provided Graphics2D object.
     *
     * @param g2d The Graphics2D object to render the grid onto.
     */
    public void drawGrid(Graphics2D g2d) {
        AffineTransform originalTransform = g2d.getTransform();

        // Iterate through grid drawing operations: Faces, Edges, Vertices
        // moving the Graphics2D object to the required vertex position
        for (int drawingOperation = 0; drawingOperation < 3; drawingOperation++) {
            for (int y = 0; y < grid.getTriangleGrid().getHeight(); y++) {
                if (y % 2 == 1)
                    g2d.translate(-oddRowOffset, 0);

                for (int x = 0; x < grid.getTriangleGrid().getWidth(); x++) {
                    TriangleGridNode node = grid.getTriangleGrid().getNode(x, y);
                    boolean isHexagonCentre = grid.isHexagonCentre(x, y);

                    if (node == null)
                        break;

                    drawNode(g2d, node, drawingOperation, x, y, isHexagonCentre);
                    g2d.translate(columnSpacing, 0);
                }

                //reset
                g2d.setTransform(originalTransform);
                g2d.translate(0, (y + 1) * rowSpacing);
            }

            //reset
            g2d.setTransform(originalTransform);
        }
    }

    /**
     * Draws the given node based on the current drawing operation (Faces, Edges, Vertices).
     *
     * @param g2d              The Graphics2D object to render onto.
     * @param node             The node to be drawn.
     * @param drawingOperation The type of drawing (0=Faces, 1=Edges, 2=Vertices).
     * @param x
     * @param y
     * @param isHexagonCentre
     */
    protected void drawNode(Graphics2D g2d, TriangleGridNode node, int drawingOperation, int x, int y, boolean isHexagonCentre) {
        switch (drawingOperation) {
            case 0:
                if (isHexagonCentre) {
                    drawHexagon(g2d, x, y);
                }
                break;
            case 1:
                drawEdges(g2d, node.edges, x, y, isHexagonCentre);
                break;
            case 2:
                if (isHexagonCentre)
                    drawHexagonCentreVertex(g2d, node.vertex);
                else
                    drawExternalEdgeVertex(g2d, node.vertex);
                break;
        }
    }

    private void drawHexagon(Graphics2D g2d, int x, int y) {
        //convert from triangle coordinates and provide hexagon tile for rendering
        TriangleCoordinate triCoordinate = new TriangleCoordinate(x, y);
        Point hexCoordinate = triCoordinate.getHexagonCoordinate();
        Tile tile = grid.getHexagonTile(hexCoordinate.x, hexCoordinate.y);
        drawHexagon(g2d, tile);
    }

    protected void drawEdges(Graphics2D g2d, Edge[] edges, int x, int y, boolean isHexagonCentre) {
        //cycle through the 3 edges held by the triangle node
        //each edge's origin is the triangle node to the vertex index of a hexagon
        for (int edgeIndex = 0; edgeIndex < 3; edgeIndex++) {
            Point2D vertex = hexagon.getVertex(edgeIndex);

            //make sure it exists (non-border)
            if (edges[edgeIndex] != null) {
                //get triangle coordinate of the vertex of the edge
                Point vertexCoordinate = TriangleGrid.getVertexCoordinateAtEndOfEdge(x, y, edgeIndex);
                //is it a hexagon centre?
                boolean isEdgeVertexHexagonCentre = grid.isHexagonCentre(vertexCoordinate.x, vertexCoordinate.y);

                //is either the triangle node or the edge vertex the hexagon centre?
                if (isHexagonCentre || isEdgeVertexHexagonCentre)
                    drawInternalEdge(g2d, edges[edgeIndex], vertex.getX(), vertex.getY());
                else
                    drawExternalEdge(g2d, edges[edgeIndex], vertex.getX(), vertex.getY());
            }
        }
    }

    protected abstract void drawHexagon(Graphics2D g2d, Tile tile);

    protected abstract void drawInternalEdge(Graphics2D g2d, Edge edge, double x, double y);

    protected abstract void drawExternalEdge(Graphics2D g2d, Edge edge, double x, double y);

    protected abstract void drawHexagonCentreVertex(Graphics2D g2d, Vertex vertex);

    protected abstract void drawExternalEdgeVertex(Graphics2D g2d, Vertex vertex);

    public static class Hexagon {
        /** Cache root 3 */
        private static final double SQRT_3 = Math.sqrt(3);
        /** Hexagon vertices from centre of hexagon */
        private final Point2D[] vertices = new Point2D[6];
        /** Length of an edge */
        final double edgeLength;
        final double halfEdgeLength;
        /** Maximum height of hexagon */
        final double height;
        final double halfHeight;
        /** Maximum width of hexagon */
        final double width;

        public Hexagon(double edgeLength) {
            this.edgeLength = edgeLength;
            this.halfEdgeLength = edgeLength / 2;
            this.height = (SQRT_3 * edgeLength);
            this.halfHeight = (SQRT_3 * edgeLength / 2.0);
            this.width = edgeLength * 2;
            vertices[0] = new Point2D.Double(-halfEdgeLength, -halfHeight);
            vertices[1] = new Point2D.Double(+halfEdgeLength, -halfHeight);
            vertices[2] = new Point2D.Double(+edgeLength, 0);
            vertices[3] = new Point2D.Double(+halfEdgeLength, +halfHeight);
            vertices[4] = new Point2D.Double(-halfEdgeLength, +halfHeight);
            vertices[5] = new Point2D.Double(-edgeLength, 0);
        }

        /**
         * Returns the vertex at the given index.
         *
         * @param vertexIndex The index of the vertex (0-5).
         * @return The Point2D representing the vertex.
         */
        public Point2D getVertex(int vertexIndex) {
            return vertices[vertexIndex % 6];
        }

        /**
         * Creates a triangle shape from the centre to two vertices in the hexagon.
         *
         * @param vertexIndexA The first vertex index (0-5).
         * @param vertexIndexB The second vertex index (0-5).
         * @return The Shape representing the triangle.
         */
        public Shape createTriangle(int vertexIndexA, int vertexIndexB) {
            Path2D path = new Path2D.Double();
            path.moveTo(0, 0);
            path.lineTo(getVertex(vertexIndexA).getX(),
                    getVertex(vertexIndexA).getY());

            path.lineTo(getVertex(vertexIndexB).getX(),
                    getVertex(vertexIndexB).getY());

            path.closePath();
            return path;
        }

        public Shape toShape() {
            Path2D path = new Path2D.Double();
            path.moveTo(getVertex(0).getX(),
                        getVertex(0).getY());

            for  (int i = 1; i < 6; i++){
                path.lineTo(getVertex(i).getX(),
                            getVertex(i).getY());
            }
            path.closePath();
            return path;
        }
    }
}
