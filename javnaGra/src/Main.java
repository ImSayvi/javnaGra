import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Javna Gra");

        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JavnaGra javnaGra = new JavnaGra();
        frame.add(javnaGra);
        frame.pack();
        javnaGra.requestFocus();
        frame.setVisible(true);
    }
}