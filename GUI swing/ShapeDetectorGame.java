import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ShapeDetectorGame extends JPanel {
    // Stores the current path being drawn by the user
    private final List<Point> currentPath = new ArrayList<>();
    private String detectedShape = "Draw something!";

    public ShapeDetectorGame() {
        setBackground(Color.DARK_GRAY);

        // Capture mouse events
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                currentPath.clear();
                detectedShape = "Drawing...";
                currentPath.add(e.getPoint());
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                currentPath.add(e.getPoint());
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (currentPath.size() > 5) {
                    detectedShape = analyzeShape(currentPath);
                } else {
                    detectedShape = "Too short! Try again.";
                }
                repaint();
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    /**
     * Algorithmic Heuristics to classify basic shapes
     */
    private String analyzeShape(List<Point> path) {
        Point start = path.get(0);
        Point end = path.get(path.size() - 1);

        // 1. Calculate Bounding Box dimensions
        int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
        for (Point p : path) {
            if (p.x < minX) minX = p.x;
            if (p.x > maxX) maxX = p.x;
            if (p.y < minY) minY = p.y;
            if (p.y > maxY) maxY = p.y;
        }
        int width = maxX - minX;
        int height = maxY - minY;

        // 2. Check for Circle (Start and end points meet, and width/height are similar)
        double startEndDist = start.distance(end);
        double perimeterEstimate = (width + height) * 2;
        if (startEndDist < Math.max(width, height) * 0.4 && width > 30 && height > 30) {
            // Check aspect ratio to differentiate a circle from a squished line loop
            double aspectRatio = (double) width / height;
            if (aspectRatio > 0.5 && aspectRatio < 2.0) {
                return "✨ DETECTED: Circle ✨";
            }
        }

        // 3. Check for Straight Lines based on aspect ratios
        if (height < 25 && width > 50) {
            return "✨ DETECTED: Horizontal Line ✨";
        }
        if (width < 25 && height > 50) {
            return "✨ DETECTED: Vertical Line ✨";
        }

        // 4. Check for V-Shape / Triangle Top (Finds the extreme peak point)
        int peakY = Integer.MAX_VALUE;
        int peakX = 0;
        int peakIdx = 0;
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i).y < peakY) {
                peakY = path.get(i).y;
                peakX = path.get(i).x;
                peakIdx = i;
            }
        }
        // If the peak is near the center index of the path and is significantly higher up than start/end
        if (peakIdx > path.size() * 0.25 && peakIdx < path.size() * 0.75) {
            if ((start.y - peakY) > 40 && (end.y - peakY) > 40) {
                return "✨ DETECTED: V-Shape (Peak) ✨";
            }
        }

        return "Unknown Shape. Try a Circle, Line, or V-shape!";
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw HUD Text
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString(detectedShape, 30, 40);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("Draw with your mouse: Circles, Lines, or V-shapes", 30, 65);

        // Draw Mouse Stroke Path
        if (currentPath.size() > 1) {
            g2.setColor(Color.CYAN);
            g2.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            for (int i = 0; i < currentPath.size() - 1; i++) {
                Point p1 = currentPath.get(i);
                Point p2 = currentPath.get(i + 1);
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Mouse Gesture Detector Game");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 600);
            frame.add(new ShapeDetectorGame());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
