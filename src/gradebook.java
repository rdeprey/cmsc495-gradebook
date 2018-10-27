import javax.swing.*;
import java.awt.*;

public class gradebook {
    public static void main(String args[]) {
        JFrame frame = new JFrame("Gradebook");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        frame.add(panel);
        frame.setSize(1000, 500);
        frame.setVisible(true);
    }
}
