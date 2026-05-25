package org.app.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import org.app.model.Point;
import org.app.model.Vertex;

public class GraphPanel extends JPanel {
    private Vertex[] vertices;
    private Point[] points;
    private Map<Integer, Point> pointMap;
    
    private static final int NODE_RADIUS = 8;
    private static final Color NODE_COLOR = Color.BLUE;
    private static final Color EDGE_COLOR = new Color(100, 100, 100);
    private static final Color LABEL_COLOR = Color.BLACK;
    private static final int PADDING = 40;
     //potrzebne zmienne do obrotu grafu
    private double pitch = 0;
    private double yaw = 0;
    private int lastMouseX, lastMouseY;
    
    public GraphPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(900, 700));
        pointMap = new HashMap<>();

        java.awt.event.MouseAdapter mouseAdapter = new java.awt.event.MouseAdapter() {

        @Override
        public void mousePressed(java.awt.event.MouseEvent e) {
            lastMouseX = e.getX();
            lastMouseY = e.getY();
        }

        @Override
        public void mouseDragged(java.awt.event.MouseEvent e) {
            int dx = e.getX() - lastMouseX;
            int dy = e.getY() - lastMouseY;

            // Zmiana kątów na podstawie ruchu myszki (czułość obrotu)
            yaw += dx * 0.01;
            pitch -= dy * 0.01; 

            lastMouseX = e.getX();
            lastMouseY = e.getY();
            
            // Wymuś ponowne narysowanie grafu
            repaint();
        }
    };
    addMouseListener(mouseAdapter);
    addMouseMotionListener(mouseAdapter);
}
    
    public void setGraphData(Vertex[] vertices, Point[] points) {
        this.vertices = vertices;
        this.points = points;
        
        // Build map for quick coordinate lookup
        pointMap.clear();
        if (points != null) {
            for (Point p : points) {
                try {
                    int nodeId = Integer.parseInt(p.getId());
                    pointMap.put(nodeId, p);
                } catch (NumberFormatException e) {
                    // Skip invalid node IDs
                }
            }
        }
        
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (vertices == null || points == null || pointMap.isEmpty()) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawString("Load input file and output file to visualize graph", 50, 50);
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Find min/max coordinates
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
        
        for (Point p : points) {
            minX = Math.min(minX, p.getX());
            maxX = Math.max(maxX, p.getX());
            minY = Math.min(minY, p.getY());
            maxY = Math.max(maxY, p.getY());
        }
        
        double scaleX = (width - 2 * PADDING) / (maxX - minX);
        double scaleY = (height - 2 * PADDING) / (maxY - minY);
        double scale = Math.min(scaleX, scaleY);

        double dataCenterX = (minX + maxX) / 2.0;
        double dataCenterY = (minY + maxY) / 2.0;
        int screenCenterX = width / 2;
        int screenCenterY = height / 2;
        
        // Draw edges first
        g2d.setColor(EDGE_COLOR);
        g2d.setStroke(new BasicStroke(1.5f));
        
        for (Vertex v : vertices) {
            Point p1 = pointMap.get(v.getIdA());
            Point p2 = pointMap.get(v.getIdB());
            
            if (p1 != null && p2 != null) {
                int[] pos1 = project3D(p1.getX() - dataCenterX, p1.getY() - dataCenterY, scale);
                int[] pos2 = project3D(p2.getX() - dataCenterX, p2.getY() - dataCenterY, scale);
                
                g2d.drawLine(screenCenterX + pos1[0], screenCenterY + pos1[1], 
                             screenCenterX + pos2[0], screenCenterY + pos2[1]);
            }
        }
        
        // Draw nodes
        g2d.setColor(NODE_COLOR);
        for (Point p : points) {
            int[] pos = project3D(p.getX() - dataCenterX, p.getY() - dataCenterY, scale);
            int drawX = screenCenterX + pos[0];
            int drawY = screenCenterY + pos[1];
            
            g2d.fillOval(drawX - NODE_RADIUS, drawY - NODE_RADIUS, 2 * NODE_RADIUS, 2 * NODE_RADIUS);
            
            // Draw node label
            g2d.setColor(LABEL_COLOR);
            FontMetrics fm = g2d.getFontMetrics();
            String label = p.getId();
            int labelX = drawX - fm.stringWidth(label) / 2;
            int labelY = drawY - NODE_RADIUS - 5;
            g2d.drawString(label, labelX, labelY);
            
            g2d.setColor(NODE_COLOR);
        }
    }
    private int[] project3D(double x, double y, double scale) {
        double z = 0; 

        double x1 = x * Math.cos(yaw) - z * Math.sin(yaw);
        double z1 = x * Math.sin(yaw) + z * Math.cos(yaw);

        double y2 = y * Math.cos(pitch) - z1 * Math.sin(pitch);

        return new int[] { (int)(x1 * scale), (int)(y2 * scale) };
    }

}
