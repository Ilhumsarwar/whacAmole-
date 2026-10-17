import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SimpleShapeDetector {

    // Keeps track of the buttons the user hovered over in order
    // Example sequence: [1, 5, 3] means they drew a V shape
    static ArrayList<Integer> trackPattern = new ArrayList<>();
    
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Shape Detector Game");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // 1. Text display at the top
        JPanel topPanel = new JPanel();
        JLabel statusLabel = new JLabel("Hold Mouse Click & Drag across holes to draw a shape!", SwingConstants.CENTER);
        topPanel.add(statusLabel);

        // 2. Control buttons at the bottom
        JPanel controls = new JPanel();
        JButton clearButton = new JButton("Clear / Reset");
        controls.add(clearButton);

        // 3. The Drawing Board (3x3 grid)
        JPanel board = new JPanel(new GridLayout(3, 3, 5, 5)); // 5px gaps
        JButton[] buttons = new JButton[9];

        for (int i = 0; i < 9; i++) {
            int buttonIndex = i; // Save the index for our listener
            buttons[i] = new JButton("Hole " + (i + 1));
            buttons[i].setBackground(Color.LIGHT_GRAY);
            board.add(buttons[i]);

            // MAGIC HAPPENS HERE: Listen to mouse entering the button area
            buttons[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    // Check if the user is holding down the left mouse button while moving
                    if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
                        
                        // If we haven't already recorded this button in our current stroke
                        if (trackPattern.isEmpty() || trackPattern.get(trackPattern.size() - 1) != buttonIndex) {
                            trackPattern.add(buttonIndex);
                            buttons[buttonIndex].setBackground(Color.CYAN); // Light up the path!
                            
                            // Every time they move, check if the shape matches
                            String detected = checkShapePattern();
                            if (!detected.equals("Unknown")) {
                                statusLabel.setText("✨ DETECTED: " + detected + "! ✨");
                            }
                        }
                    }
                }
            });
        }

        // Reset everything when the clear button is clicked
        clearButton.addActionListener(e -> {
            trackPattern.clear();
            statusLabel.setText("Hold Mouse Click & Drag across holes to draw a shape!");
            for (JButton btn : buttons) {
                btn.setBackground(Color.LIGHT_GRAY);
            }
        });

        // Add everything to the frame
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(board, BorderLayout.CENTER);
        frame.add(controls, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    /**
     * This method looks at the button indices the user touched and identifies the shape!
     * Button layout reference:
     * 0  1  2
     * 3  4  5
     * 6  7  8
     */
    public static String checkShapePattern() {
        // A V-Shape means going down-right, then up-right. 
        // Example: starting at top-left (0), going to bottom-middle (7), up to top-right (2)
        if (trackPattern.contains(0) && trackPattern.contains(7) && trackPattern.contains(2)) {
            // Make sure the bottom point (7) came after the start (0) and before the end (2)
            if (trackPattern.indexOf(0) < trackPattern.indexOf(7) && trackPattern.indexOf(7) < trackPattern.indexOf(2)) {
                return "V-Shape / Triangle Peak";
            }
        }

        // Horizontal Line across the middle row (Buttons 3 -> 4 -> 5)
        if (trackPattern.contains(3) && trackPattern.contains(4) && trackPattern.contains(5)) {
            return "Horizontal Line";
        }

        // Vertical Line down the middle column (Buttons 1 -> 4 -> 7)
        if (trackPattern.contains(1) && trackPattern.contains(4) && trackPattern.contains(7)) {
            return "Vertical Line";
        }

        // Circle / Square Loop (touching all outer edges)
        if (trackPattern.contains(0) && trackPattern.contains(2) && trackPattern.contains(8) && trackPattern.contains(6)) {
            return "Circle / Box Loop";
        }

        return "Unknown";
    }
}
