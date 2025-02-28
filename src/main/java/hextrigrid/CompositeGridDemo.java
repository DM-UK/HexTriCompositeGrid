package hextrigrid;

import hextrigrid.grid.DemoHexaTriagonalGridRenderer;
import hextrigrid.grid.HexaTriagonalGrid;

import javax.swing.*;
import java.awt.*;

public class HexaTriangleGridDemo {
    HexaTriagonalGrid grid = new HexaTriagonalGrid(7,7);
    private DemoHexaTriagonalGridRenderer renderer = new DemoHexaTriagonalGridRenderer(grid, 60);
    //private HexaTriagonalGridRenderer renderer = new HexaTriagonalGridRenderer(grid, 60);

    public HexaTriangleGridDemo()
    {
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Triangle Grid Demo");
        frame.setSize(800, 800);
        JPanel panel = createPreviewPane();
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JPanel createPreviewPane() {
        return new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(250, 150);
                renderer.drawGrid(g2d);
            }
        };
    }

    public static void main(String[] args) {
        new HexaTriangleGridDemo();
    }
}