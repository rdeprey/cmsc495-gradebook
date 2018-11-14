/* Connecting to the database: https://www.homeandlearn.co.uk/java/connect_to_a_database_using_java_code.html */
/* JTabbedPane starter code from: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TabbedPaneDemoProject/src/components/TabbedPaneDemo.java */
/* Verify string is integer from: https://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Gradebook extends JFrame {
    private static final Font f1 = new Font("Monospaced", Font.BOLD, 20);
    private static final Font f2 = new Font("Monospaced", Font.BOLD, 16);


    //creates GUI
    public Gradebook(final User user) throws Exception {
        String userName = user.getUsername();
        String date = new SimpleDateFormat("EEEEE MMMMM d, yyyy").format(new Date());
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
        titlePanel.setBackground(new Color(0,122,204));
        JLabel title = new JLabel("GradeBook", SwingConstants.CENTER);
        title.setFont(f1);
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.CENTER);
        JPanel nameDatePanel = new JPanel(new GridLayout(2,1));
        nameDatePanel.setBackground(new Color(0,122,204));
        JLabel userNameLabel = new JLabel(userName);
        userNameLabel.setForeground(Color.WHITE);
        JLabel dateLabel = new JLabel(date);
        dateLabel.setForeground(Color.WHITE);
        nameDatePanel.add(userNameLabel);
        nameDatePanel.add(dateLabel);
        titlePanel.add(nameDatePanel, BorderLayout.EAST);
        contentPane.add(titlePanel, c);

        //GreetingPanel
        c.gridy ++;
        c.gridwidth = 2;
        JPanel greetingPanel = new JPanel(new GridLayout(0,1));
        greetingPanel.setBackground(new Color(102,194,255));
        JLabel greetingLabel = new JLabel(greetingMessage + ",", SwingConstants.CENTER);
        greetingLabel.setFont(f2);
        greetingPanel.add(new JLabel());
        greetingPanel.add(greetingLabel);
        greetingPanel.add(new JLabel(userName, SwingConstants.CENTER));
        greetingPanel.add(new JLabel());
        contentPane.add(greetingPanel, c);

        //ProgressPanel
        c.gridy ++;
        c.gridwidth = 2;
        final JPanel progressPanel = new JPanel(new GridLayout(0,1,5,5));

        final JPanel currentClassesPanel = new JPanel(new GridLayout(0,1,5,5));
        drawCurrentClassesPanel(currentClassesPanel, progressPanel, user); // dynamic progress panels

        // Hard-coded progress panels
        progressPanel.add(createClassProgressPanel("Class A", "A", 40, 100));
        progressPanel.add(createClassProgressPanel("Class B", "A", 15, 80));
        progressPanel.add(createClassProgressPanel("Class C", "B", 10, 90));
        contentPane.add(progressPanel, c);


        //CompletedPanel
        c.gridy ++;
        c.gridwidth = 2;
        JPanel completedPanel = new JPanel(new GridLayout(0,1,5,5));
        JPanel compClassTitlePanel = new JPanel(new GridLayout(0,1,15,15));
        compClassTitlePanel.setBackground(new Color(179,224,255));
        JLabel compClassLabel = new JLabel("Completed Classes", SwingConstants.CENTER);
        compClassLabel.setFont(f2);
        compClassTitlePanel.add(compClassLabel);
        completedPanel.add(compClassTitlePanel);

        ArrayList<Class> completedClasses = Class.getCompletedClasses(user.getUserId());
        if (!completedClasses.isEmpty()) {
            for (int i = 0; i < completedClasses.size(); i++) {
                // Dynamic completed class panels
                completedPanel.add(createCompClassPanel(completedClasses.get(i).getClassName(), completedClasses.get(i).convertToLetterGrade()));
            }
        }

        // Hard-coded completed class panels
        completedPanel.add(createCompClassPanel("Class D", "A"));
        completedPanel.add(createCompClassPanel("Class E", "B"));
        contentPane.add(completedPanel, c);

        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 3;
        c.gridheight = 20;
        
        // Create tabbed pane container
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(600, 700));

//        for (int i = 0; i < currentClasses.size(); i++) {
//            JComponent classTemplatePanel = new JPanel(new GridLayout(0,1));
//            tabbedPane.addTab(currentClasses.get(i).getClassName(), null, classTemplatePanel); // Add tab to tab container
//            tabbedPane.setMnemonicAt(i, KeyEvent.VK_3); // keyboard event
//        }

       //New Class
        final JPanel newClassPanel = new JPanel(new BorderLayout());

        JLabel newClassLabel = new JLabel("Creating New Class", SwingConstants.CENTER);
        newClassLabel.setFont(f2);
        newClassPanel.add(newClassLabel, BorderLayout.PAGE_START);



        JPanel newClassFormPanel = new JPanel(new GridBagLayout());
        GridBagConstraints e = new GridBagConstraints();
        e.gridx = 0;
        e.gridy = 0;
        e.weighty = 1;
        e.fill = GridBagConstraints.HORIZONTAL;
        e.anchor = GridBagConstraints.FIRST_LINE_START;

        JPanel classInfoPanel = new JPanel(new GridLayout(0,2));

        classInfoPanel.add(new JLabel());
        classInfoPanel.add(new JLabel());

        final JLabel classNameLabel = new JLabel ("Class Name: ");
        classInfoPanel.add(classNameLabel);

        final JTextField nameTextField = new JTextField(10);
        classInfoPanel.add(nameTextField);

        final JLabel classStartDateLabel = new JLabel("Class Start Date: ");
        classInfoPanel.add(classStartDateLabel);

        final JTextField classStartDateTextField = new JTextField(5);
        classInfoPanel.add(classStartDateTextField);

        final JLabel classEndDateLabel = new JLabel("Class End Date: ");
        classInfoPanel.add(classEndDateLabel);

        final JTextField classEndDateTextField = new JTextField(5);
        classInfoPanel.add(classEndDateTextField);

        final JLabel numberOfAssignmentsLabel = new JLabel("Number of Assignments: ");
        classInfoPanel.add(numberOfAssignmentsLabel);

        final JTextField numOfAssignmentsTextField = new JTextField(5);
        classInfoPanel.add(numOfAssignmentsTextField);

        newClassFormPanel.add(classInfoPanel, e);

        e.gridx = 0;
        e.gridy++;
        e.gridwidth = 2;
        JButton createNewClassTemplate = new JButton("Create Class Template");


        // Create Class Template event handler
        createNewClassTemplate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String className = nameTextField.getText();
                String numOfAssignments = numOfAssignmentsTextField.getText();

                if (className == "" || numOfAssignments == "") {
                    // Show an error message
                }

                // Verify that the number of assignments is actually an integer value
                for (int i = 0; i < numOfAssignments.length(); i++) {
                    if (i == 0 && numOfAssignments.charAt(i) == '-') {
                        if (numOfAssignments.length() == 1) {
                            // Show error message - not an integer
                            return;
                        }
                        continue;
                    }
                    if (Character.digit(numOfAssignments.charAt(i), 10) < 0) {
                        // Show error message - not an integer
                        return;
                    }
                }

                // This means it's an integer
                int assignmentsInt = Integer.parseInt(numOfAssignments);

                // Create a class object
                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                c.add(Calendar.DATE, 90);
                Class classX = new Class(user.getUserId(), className, assignmentsInt, new Date(), c.getTime());
                try {
                     boolean isSuccess = Class.addClass(classX);
                     if (isSuccess) {
                         // Redraw the progress panels when new classes are added
                        drawCurrentClassesPanel(currentClassesPanel, progressPanel, user);
                     } else {
                         // Show error message

                     }
                } catch (Exception ex) {
                    // Show error in GUI if class isn't saved to database; don't allow user to continue in application
                    return;
                }
            }
        });

        newClassFormPanel.add(createNewClassTemplate, e);
        newClassPanel.add(newClassFormPanel, BorderLayout.CENTER);

        tabbedPane.addTab("New Class", null, newClassPanel); // Add tab to tab container
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1); // keyboard event


       //New Class Template Panel (moved to method, after values pulled from New Class)
        JComponent newClassTemplatePanel = new JPanel(new BorderLayout());
        JLabel classTemplateLabel = new JLabel("Entering Assignments into Class", SwingConstants.CENTER);
        classTemplateLabel.setFont(f2);
        newClassTemplatePanel.add(classTemplateLabel, BorderLayout.PAGE_START);

        //
        newClassTemplatePanel.add(createAssignmentForm(7));

        tabbedPane.addTab("New Class Template", null, newClassTemplatePanel); // Add tab to tab container
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_2); // keyboard event

        //Actual Class
        //Class Panel (moved to method, after values pulled from New Class & New Class Template)
        JComponent classPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 150,0));

        JPanel statusPanel = new JPanel(new GridLayout(0,1));
        String className = "My Class";
        JLabel classLabel = new JLabel( className, SwingConstants.LEADING);
        classLabel.setFont(f1);
        statusPanel.add(classLabel);
        char currentGrade = 'A';
        JLabel currentGradeLabel = new JLabel("Current Grade: " + currentGrade, SwingConstants.LEADING);
        currentGradeLabel.setFont(f2);
        statusPanel.add(currentGradeLabel);
        headerPanel.add(statusPanel);

        JPanel goalGradePanel = new JPanel(new GridLayout(0,1));
        JRadioButton A = new JRadioButton("A (90-100)");
        JRadioButton B = new JRadioButton("B (80-90)");
        JRadioButton C = new JRadioButton("C (70-80)");
        JRadioButton D = new JRadioButton("D (60-70");
        goalGradePanel.add(new JLabel("Goal Grade     ", SwingConstants.RIGHT));
        goalGradePanel.add(A);
        goalGradePanel.add(B);
        goalGradePanel.add(C);
        goalGradePanel.add(D);
        ButtonGroup goalGrade = new ButtonGroup();
        goalGrade.add(A);
        goalGrade.add(B);
        goalGrade.add(C);
        goalGrade.add(D);

        headerPanel.add(goalGradePanel);
        classPanel.add(headerPanel, BorderLayout.PAGE_START);




        tabbedPane.addTab(className, null, classPanel); // Add tab to tab container
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_3); // keyboard event

        // Add the tabbed pane to contentPane
        contentPane.add(tabbedPane,c);

        frame.setSize(1000, 800);
        frame.setVisible(true);

    }

    protected static String determineGreeting(){
        String greeting;
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date dateObj = new Date();
        int hour = Integer.parseInt(dateFormat.format(dateObj));

        if(hour >= 6 && hour<12) {
            //Good Morning 6am to 11am
            greeting = "Good Morning";
        }
        else if(hour >=12 && hour<17) {
            //Good Afternoon 12 to 4pm
            greeting = "Good Afternoon";
        }
        else if(hour >=17 && hour<=20) {
            //Good Evening 5pm to 8 pm
            greeting = "Good Evening";
        } else {
            //Good Night 8 pm to 5am
            greeting = "Good Night";
        }

        return greeting;
    }

    private static void drawCurrentClassesPanel(JPanel currentClassesPanel, JPanel progressPanel, User user) {
        try {
            ArrayList<Class> currentClasses = Class.getCurrentClasses(user.getUserId());
            if (!currentClasses.isEmpty()) {
                currentClassesPanel.removeAll();
                progressPanel.removeAll();

                currentClassesPanel.setBackground(new Color(179,224,255));
                JLabel currentClassesLabel = new JLabel("Current Classes", SwingConstants.CENTER);
                currentClassesLabel.setFont(f2);
                currentClassesPanel.add(currentClassesLabel);
                progressPanel.add(currentClassesPanel);

                for (int i = 0; i < currentClasses.size(); i++) {
                    progressPanel.add(createClassProgressPanel(currentClasses.get(i).getClassName(), currentClasses.get(i).convertToLetterGrade(), 40, 100));
                }
                currentClassesPanel.revalidate();
            }
        } catch (Exception ex) {
            System.out.println("We're sorry, but we cannot access your classes at this time. Please try again later.");
        }
    }

    protected static JPanel createClassProgressPanel(String className, String classGrade,
                                                     int completedWeight, int totalWeight){
        JPanel classProgressPanel = new JPanel(new GridLayout(0,1));

        //Class Name and Grade
        JPanel classProgressTitle = new JPanel(new GridLayout(0,2));
        JLabel classNameLabel = new JLabel(className);
        JLabel classGradeLabel = new JLabel(classGrade, SwingConstants.RIGHT);
        classProgressTitle.add(classNameLabel);
        classProgressTitle.add(classGradeLabel);
        classProgressPanel.add(classProgressTitle);
        JProgressBar progBar = new JProgressBar(0,totalWeight);
        progBar.setValue(completedWeight);
        progBar.setStringPainted(true);
        classProgressPanel.add(progBar);

        return classProgressPanel;
    }

    protected static JPanel createCompClassPanel(String className, String classGrade){
        JPanel compClassPanel = new JPanel(new GridLayout(0,2));
        JLabel compClassName = new JLabel(className);
        JLabel compClassGrade = new JLabel(classGrade, SwingConstants.RIGHT);
        compClassPanel.add(compClassName);
        compClassPanel.add(compClassGrade);

        return compClassPanel;
    }

    protected static JPanel createAssignmentForm(int numOfAssignments){
        Font f3 = new Font("Monospaced", Font.BOLD, 12);

        JPanel assignmentsFormPanel = new JPanel(new GridLayout(0,1));

        JPanel assignmentsLabelPanel = new JPanel(new GridBagLayout());
        GridBagConstraints f = new GridBagConstraints();
        f.gridx = 1;
        f.gridy = 0;
        JLabel dueDateLabel = new JLabel("Due Date");
        dueDateLabel.setFont(f3);
        assignmentsLabelPanel.add(dueDateLabel,f);

        f.gridx = 2;
        f.gridwidth = 2;
        f.fill = GridBagConstraints.HORIZONTAL;
        JLabel assignNameLabel = new JLabel("Assign. Name");
        assignNameLabel.setFont(f3);
        assignmentsLabelPanel.add(assignNameLabel,f);

        f.gridx = 4;
        f.gridwidth = 1;
        JLabel assignWeightLabel = new JLabel("Assign. Weight");
        assignWeightLabel.setFont(f3);
        assignmentsLabelPanel.add(assignWeightLabel,f);

        assignmentsFormPanel.add(assignmentsLabelPanel);

        int assignNo = 1;
        //create each assignment's form
        for(int i = 0; i < numOfAssignments; i++){
            //attempting to switch to gridBagLayout

            JPanel anAssignmentPanel = new JPanel(new GridBagLayout());
            GridBagConstraints g = new GridBagConstraints();
            g.gridx = 0;
            g.gridy = 0;
            JLabel assignNumLabel = new JLabel("Assignment " + assignNo);
            assignNo++;
            anAssignmentPanel.add(assignNumLabel, g);

            g.gridx = 1;
            JTextField dateTF = new JTextField("--/--/----", 9);
            anAssignmentPanel.add(dateTF, g);

            g.gridx = 2;
            g.gridwidth = 2;
            g.fill = GridBagConstraints.HORIZONTAL;
            JTextField assignNameTF = new JTextField("Assignment Name",15);
            anAssignmentPanel.add(assignNameTF, g);

            g.gridx = 4;
            g.gridwidth =1;
            JTextField assignWeightTextField = new JTextField("Weight",5);
            anAssignmentPanel.add(assignWeightTextField, g);

            assignmentsFormPanel.add(anAssignmentPanel);
        }
      
        return assignmentsFormPanel;
    }
}
