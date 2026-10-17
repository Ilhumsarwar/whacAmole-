import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class MyFirstWindow{

    static int score = 0;
    static int seconds = 0;
    static int molePosition = -1;
    public static void main(String[] args) {
        JFrame frame = new JFrame("My first window");
        // frame.setLayout(new FlowLayout());
        frame.setSize(400,300); // size of window
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //cross button works for exit
        frame.setLocationRelativeTo(null);
        
        // // adding label a component to the window
        // JLabel label = new JLabel("Hello, Swing!");//build it
        // frame.add(label, BorderLayout.NORTH); // place it 
        // // button
        // JButton button = new JButton("Click me!");
        // frame.add(button, BorderLayout.WEST);
        
        // label.setText("New Text");
        // //text field
        // JTextField textField = new JTextField(3);
        // frame.add(textField, BorderLayout.SOUTH);
        
        // // new gridLayout(frame);
        
        //score panel
        JPanel scorePanel = new JPanel();
        JLabel scoreLabel = new JLabel("Score: 0 ", SwingConstants.CENTER);
        scorePanel.add(scoreLabel);

        // grid panel
        JButton[] buttons = new JButton[9];
        JPanel board = new JPanel(new GridLayout(3,3));
        for(int i=0;i<9;i++){
            board.add( buttons[i] = new JButton("HOLE " + (i+1)));
        }

        // adding buttons in a new panel for start and reset and it will be in the south part the Frame
        JPanel controls = new JPanel(new FlowLayout());
        JButton startButton,resetButton;
        controls.add( startButton = new JButton("Start"));
        controls.add( resetButton = new JButton("Reset"));
        
        //
        Random random = new Random();

        // Timer
        Timer timer = new Timer( 1000, e -> {

            if(molePosition != -1) buttons[molePosition].setText("HOLE "+ (molePosition+1));
            
            molePosition = random.nextInt(9);
            buttons[molePosition].setText("MOLE!");
            System.out.println("tick " + seconds++);
        });
        
        // if(molePosition != -1) buttons[molePosition].addActionListener(e -> {scoreLabel.setText("Score " + ++score); });
        

        //actionListener
        // startButton.addActionListener(e -> {System.out.println("Start was clicked!");});
        startButton.addActionListener(e -> {
            scoreLabel.setText("Game Started!");
            score = 0;
            seconds = 0;
            timer.start(); // this will start the timer. or else nothing happens

        });
        resetButton.addActionListener(e -> {
            scoreLabel.setText("Score: 0"); 
            score = 0;
            seconds = 0;
            if(molePosition != -1) buttons[molePosition].setText("HOLE "+ (molePosition+1));
            
            timer.stop(); // this stops the timer..
        });
        
        for(int i=0;i<9;i++){
            int index = i;
            buttons[i].addActionListener(e -> { 
                if(index == molePosition) scoreLabel.setText("Score " + ++score);
                else scoreLabel.setText("Score: " + --score);
                });
        }

        

        frame.add(controls, BorderLayout.SOUTH);
        frame.add(board, BorderLayout.CENTER);
        frame.add(scorePanel, BorderLayout.NORTH);


        frame.setVisible(true); // shows the window
        
    }
}

// class borderLayout extends MyFirstWindow{
//     borderLayout(JFrame frame){
//         JPanel board = new JPanel(new GridLayout(3,3));
 
//         frame.add(label, BorderLayout.NORTH);
//         frame.add(button, BorderLayout.WEST);
//         frame.add(textField, BorderLayout.CENTER);
//     }
// }

class flowLayout{
    flowLayout(JFrame frame){
        frame.setLayout(new FlowLayout());
    }
}

class gridLayout{
    gridLayout(JFrame frame){
        frame.setLayout(new GridLayout(3,3)); // 3 rows, 3 columns
        for(int i=1; i<=7;i++){
            frame.add(new JButton("Hole: " + i));
        }
    }
}

