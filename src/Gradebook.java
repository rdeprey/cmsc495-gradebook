/* JTabbedPane starter code from: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TabbedPaneDemoProject/src/components/TabbedPaneDemo.java */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Gradebook extends JFrame {
    public static void main(String[] args) {
        Gradebook gradebook = new Gradebook();
    }

    //creates GUI
    public Gradebook(){
        String userName = "username";
        String date = "date";
        String greetingMessage = determineGreeting();

        JFrame frame = new JFrame("Gradebook");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel(new GridBagLayout());
        frame.setContentPane(contentPane);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 10;
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;

        //TitlePanel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel title = new JLabel("GradeBook", SwingConstants.CENTER);
        Font f1 = new Font(Font.MONOSPACED, Font.BOLD, 20);
        title.setFont(f1);
        titlePanel.add(title, BorderLayout.CENTER);
        JPanel nameDatePanel = new JPanel(new GridLayout(2,1));
        nameDatePanel.add(new JLabel(userName));
        nameDatePanel.add(new JLabel(date));
        titlePanel.add(nameDatePanel, BorderLayout.EAST);
        contentPane.add(titlePanel, c);

        //GreetingPanel
        c.gridy++;
        c.gridwidth = 5;
        c.anchor = GridBagConstraints.SOUTHWEST;
        Font f2 = new Font(Font.MONOSPACED, Font.BOLD, 16);
        JPanel greetingPanel = new JPanel(new GridBagLayout());
        greetingPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel greetingLabel = new JLabel(greetingMessage + ", ", SwingConstants.CENTER);
        greetingLabel.setFont(f2);
        greetingPanel.add(greetingLabel);
        greetingPanel.add(new JLabel(userName));
        contentPane.add(greetingPanel, c);

        //ProgressPanel
        c.gridy ++;
        JPanel progressPanel = new JPanel(new GridLayout(0,1));
        progressPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JPanel currentClassesPanel = new JPanel();
        currentClassesPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel currentClassesLabel = new JLabel("Current Classes", SwingConstants.CENTER);
        currentClassesLabel.setFont(f2);
        currentClassesPanel.add(currentClassesLabel);
        progressPanel.add(currentClassesPanel);

        progressPanel.add(createClassProgressPanel("Class A", "A"));
        progressPanel.add(createClassProgressPanel("Class B", "A"));
        progressPanel.add(createClassProgressPanel("Class C", "B"));

        contentPane.add(progressPanel, c);

        //CompletedPanel
        c.gridy++;
        c.anchor = GridBagConstraints.SOUTHWEST;
        JPanel completedPanel = new JPanel(new GridLayout(0,1));
        completedPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JLabel compClassLabel = new JLabel("Completed Classes");
        compClassLabel.setFont(f2);
        completedPanel.add(compClassLabel);
        JPanel compClassPanel = new JPanel();
        JLabel compClassName = new JLabel("Class Name");
        JLabel compClassGrade = new JLabel("A");
        compClassPanel.add(compClassName);
        compClassPanel.add(compClassGrade);
        completedPanel.add(compClassPanel);
        contentPane.add(completedPanel, c);

        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 3;
        c.gridheight = 20;

        //TabbedPanel
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension (600,700));


        //Create tab 1
        JComponent panel1 = makeTextPanel("Panel #1");
        tabbedPane.addTab("Class A", null, panel1, "Placeholder 1"); // Add tab to tab container
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1); // keyboard event

        // Create tab 2
        JComponent panel2 = makeTextPanel("Panel #2");
        tabbedPane.addTab("Class B", null, panel2, "Placeholder 2"); // Add tab to tab container
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_2); // keyboard event

        // Create tab 3
        JComponent panel3 = makeTextPanel("Panel #3");
        tabbedPane.addTab("New Tab", null, panel3, "Placeholder 3"); // Add tab to tab container
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_3); // keyboard event

        JComponent panel4 = makeTextPanel(" ");
        JPanel mPanel = new JPanel();
        JTextField classField = new JTextField(5);
        JTextField assField = new JTextField(5);

        mPanel.add(new JLabel("Assignments: "));
        mPanel.add(assField);
        tabbedPane.addTab("New Class", null, panel4, "Placeholder");
        tabbedPane.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent ce){
                String nClass = JOptionPane.showInputDialog(frame, "What is the name of your class?");
                int numAssign = JOptionPane.showConfirmDialog(frame,mPanel, "How many assignments?", JOptionPane.OK_CANCEL_OPTION);
            }
        });

        contentPane.add(tabbedPane, c);



        frame.setSize(1000,800);
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

    protected static String determineGreeting(){
        //There has to be better way to differentiate time...
        String greeting;
        int hour = Calendar.HOUR;
        int AMPM = Calendar.AM_PM; // AM = 0, PM = 1
        System.out.println(Calendar.HOUR);

        //Good Morning 6am to 11:59 pm
        if((AMPM == 0 && hour >=6) && (AMPM == 1 & hour<12)) {
            return greeting = "Good Morning";
        }
        //Good Afternoon 12 to 4:59 pm
        if((AMPM == 1 && hour >=12) && (AMPM == 1 && hour<17)) {
            return greeting = "Good Afternoon";
        }
        //Good Evening 5pm to 8 pm
        if((AMPM == 1 && hour >=17) && (AMPM == 1 && hour<=20)) {
            return greeting = "Good Morning";
        }
        //Good Night 8 pm to 5:59am
        greeting = "Good Night";

        return greeting;
    }

    protected static JPanel createClassProgressPanel(String className, String classGrade){
        JPanel classProgressPanel = new JPanel(new GridLayout(0,1));

        //Class Name and Grade
        JPanel classProgressTitle = new JPanel(new GridLayout(0,2));
        JLabel classNameLabel = new JLabel(className);
        JLabel classGradeLabel = new JLabel(classGrade, SwingConstants.RIGHT);

        classProgressTitle.add(classNameLabel);
        classProgressTitle.add(classGradeLabel);

        classProgressPanel.add(classProgressTitle);
        classProgressPanel.add(new JProgressBar());

        return classProgressPanel;
    }
}