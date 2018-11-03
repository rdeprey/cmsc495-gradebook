/* JTabbedPane starter code from: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TabbedPaneDemoProject/src/components/TabbedPaneDemo.java */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class gradebook {
    public static void main(String args[]) {
        JFrame frame = new JFrame("Gradebook");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Create tabbed pane container
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.setPreferredSize(new Dimension(900, 400));

        // Create tab 1
        JComponent panel1 = makeTextPanel("Panel #1");
        tabbedPane.addTab("Tab 1", null, panel1, "Placeholder 1"); // Add tab to tab container
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1); // keyboard event

        // Create tab 2
        JComponent panel2 = makeTextPanel("Panel #2");
        tabbedPane.addTab("Tab 2", null, panel2, "Placeholder 2"); // Add tab to tab container
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_2); // keyboard event

        // Create tab 3
        JComponent panel3 = makeTextPanel("Panel #3");
        tabbedPane.addTab("Tab 3", null, panel3, "Placeholder 3"); // Add tab to tab container
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_3); // keyboard event

        // Add the tabbed pane to the panel
        panel.add(tabbedPane);

        frame.add(panel);
        frame.setSize(1000, 500);
        frame.setVisible(true);
    }

    protected  static JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }
}
