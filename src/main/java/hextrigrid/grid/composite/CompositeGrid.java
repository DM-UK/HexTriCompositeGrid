package hextrigrid.grid.composite;

import hextrigrid.grid.Tile;
import hextrigrid.grid.triangle.TriangleGrid;

import java.awt.*;

/**
 * Represents a composite grid combining hexagonal and triangular structures.
 * The hexagonal grid is embedded within a triangular grid.
 */
public class CompositeGrid {
    private final TriangleGrid triangleGrid;
    private final int gridWidth;
    private final int gridHeight;
    private final Tile[][] hexagonTiles;

    /**
     * Construct a composite grid with the given width and height.
     *
     * @param gridWidth  The number of hexagonal columns in the grid.
     * @param gridHeight The number of hexagonal rows in the grid.
     */
    public CompositeGrid(int gridWidth, int gridHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        // Creates a TriangleGrid with adjusted dimensions to fit the hexagonal layout.
        this.triangleGrid = new TriangleGrid(2 + gridWidth + ((gridWidth - 1) / 2), (gridHeight * 2) + 2);
        hexagonTiles = new Tile[gridHeight][];
        createGrid();
    }

    /**
     * Initializes the hexagonal grid structure by creating tile instances.
     */
    private void createGrid() {
        for (int y = 0; y < gridHeight; y++) {
            hexagonTiles[y] = new Tile[gridWidth];
            for (int x = 0; x < gridWidth; x++) {
                hexagonTiles[y][x] = new Tile();
            }
        }
    }

    /**
     * Determines if a given (x, y) coordinate corresponds to the center of a hexagon.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return {@code true} if the coordinate is a hexagon center; otherwise, {@code false}.
     */
    public boolean isHexagonCentre(int x, int y) {
        return (x % 3 == 1 && y % 2 == 1) || (x % 3 == 2 && y % 2 == 0);
    }

    /**
     * Retrieves the hexagonal tile at a specified (x, y) coordinate.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The tile at the specified position, or {@code null} if out of bounds.
     */
    public Tile getHexagonTile(int x, int y) {
        if (y < 0 || y >= gridHeight || x < 0 || x >= gridWidth) {
            return null;
        }
        return hexagonTiles[y][x];
    }

    /**
     * Returns the underlying triangular grid.
     *
     * @return The TriangleGrid instance.
     */
    public TriangleGrid getTriangleGrid() {
        return triangleGrid;
    }

    /**
     * Retrieves the hexagonal tiles adjacent to a given edge in the triangular grid.
     *
     * @param x         The x-coordinate of the triangle edge.
     * @param y         The y-coordinate of the triangle edge.
     * @param edgeIndex The index of the edge (0-2 in a triangle).
     * @return An array containing one or two adjacent hexagonal tiles.
     */
    public Tile[] getAdjacentTilesOfEdge(int x, int y, int edgeIndex){
        Tile tileA;
        Tile tileB;
        Point endOfEdge = TriangleGrid.getVertexCoordinateAtEndOfEdge(x, y, edgeIndex);

        //if edge starts in a hexagon centre
        if (isHexagonCentre(x, y)){
            //then return only one adjacent hexagon (the edge start hexagon)
            tileA = getHexagonTile(x, y);
            tileB = tileA;
        }
        //if edge finishes in a hexagon centre
        else if (isHexagonCentre(endOfEdge.x, endOfEdge.y)){
            //then return only one adjacent hexagon (the edge finish hexagon)
            tileA = getHexagonTile(endOfEdge.x, endOfEdge.y);
            tileB = tileA;
        }
        //else, on a hexagon border return both hexagons that are on the opposite vertices of the edge
        else{
            Point[] oppositeCoordinates = TriangleGrid.getOppositeCoordinatesOfEdge(x, y, edgeIndex);
            tileA = getHexagonTile(oppositeCoordinates[0].x, oppositeCoordinates[0].y);
            tileB = getHexagonTile(oppositeCoordinates[1].x, oppositeCoordinates[1].y);
        }

        return new Tile[]{tileA, tileB};
    }
}