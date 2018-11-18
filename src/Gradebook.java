/* Connecting to the database: https://www.homeandlearn.co.uk/java/connect_to_a_database_using_java_code.html */
/* JTabbedPane starter code from: https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TabbedPaneDemoProject/src/components/TabbedPaneDemo.java */
/* Verify string is integer from: https://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Gradebook extends JFrame {
    private static final Font f1 = new Font("Monospaced", Font.BOLD, 20);
    private static final Font f2 = new Font("Monospaced", Font.BOLD, 16);
    private static ArrayList<Class> currentClasses;

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
        titlePanel.setBackground(new Color(0, 122, 204));
        JLabel title = new JLabel("GradeBook", SwingConstants.CENTER);
        title.setFont(f1);
        title.setForeground(Color.WHITE);
        titlePanel.add(title, BorderLayout.CENTER);
        JPanel nameDatePanel = new JPanel(new GridLayout(2, 1));
        nameDatePanel.setBackground(new Color(0, 122, 204));
        JLabel userNameLabel = new JLabel(userName);
        userNameLabel.setForeground(Color.WHITE);
        JLabel dateLabel = new JLabel(date);
        dateLabel.setForeground(Color.WHITE);
        nameDatePanel.add(userNameLabel);
        nameDatePanel.add(dateLabel);
        titlePanel.add(nameDatePanel, BorderLayout.EAST);
        contentPane.add(titlePanel, c);

        //GreetingPanel
        c.gridy++;
        c.gridwidth = 2;
        JPanel greetingPanel = new JPanel(new GridLayout(0, 1));
        greetingPanel.setBackground(new Color(102, 194, 255));
        JLabel greetingLabel = new JLabel(greetingMessage + ",", SwingConstants.CENTER);
        greetingLabel.setFont(f2);
        greetingPanel.add(new JLabel());
        greetingPanel.add(greetingLabel);
        greetingPanel.add(new JLabel(userName, SwingConstants.CENTER));
        greetingPanel.add(new JLabel());
        contentPane.add(greetingPanel, c);

        //ProgressPanel
        c.gridy++;
        c.gridwidth = 2;
        final JPanel progressPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        final JPanel currentClassesPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        drawCurrentClassesPanel(currentClassesPanel, progressPanel, user); // dynamic progress panels
        JScrollPane sp = new JScrollPane(progressPanel);
        JPanel jp = new JPanel(new GridLayout(0, 1, 5, 5));
        jp.setPreferredSize(new Dimension(200, 200));
        jp.add(sp);
        contentPane.add(jp, c);


        //CompletedPanel
        c.gridy++;
        c.gridwidth = 2;
        JPanel compClassTitlePanel = new JPanel(new GridLayout(0, 1, 15, 15));
        compClassTitlePanel.setBackground(new Color(179, 224, 255));
        JLabel compClassLabel = new JLabel("Completed Classes", SwingConstants.CENTER);
        compClassLabel.setFont(f2);
        compClassTitlePanel.add(compClassLabel);
        contentPane.add(compClassTitlePanel, c);

        c.gridy++;
        JPanel completedPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JPanel completedClassesPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        drawCompletedClassesPanel(completedClassesPanel, completedPanel, user);
        JScrollPane completedClassScrollPane = new JScrollPane(completedPanel);
        JPanel completedClassParentPane = new JPanel(new GridLayout(0, 1, 5, 5));
        completedClassParentPane.setPreferredSize(new Dimension(200, 200));
        completedClassParentPane.add(completedClassScrollPane);
        contentPane.add(completedClassParentPane, c);

        c.gridx = 3;
        c.gridy = 1;
        c.gridwidth = 3;
        c.gridheight = 20;

        // Create tabbed pane container
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(600, 700));

        //New Class
        final JPanel newClassPanel = new JPanel(new BorderLayout());
        JLabel newClassLabel = new JLabel("Creating New Class", SwingConstants.CENTER);
        newClassLabel.setFont(f2);
        newClassPanel.add(newClassLabel, BorderLayout.PAGE_START);


        final JPanel newClassFormPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;

        final JPanel classInfoPanel = createAddClassPanel();
        newClassFormPanel.add(classInfoPanel, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        constraints.gridwidth = 2;
        JButton createNewClassTemplate = new JButton("Add Assignments");


        // Create Class Template event handler
        createNewClassTemplate.addActionListener(new ActionListener() {
            String className = null;
            String classStartDate = null;
            String classEndDate = null;
            String numOfAssignments = null;

            public void actionPerformed(ActionEvent e) {
                for (Component c : newClassFormPanel.getComponents()) {
                    if (c instanceof JPanel) {
                        int counter = 0;
                        for (Component d : classInfoPanel.getComponents()) {
                            if (d instanceof JTextField) {
                                switch (counter) {
                                    case 0:
                                        className = ((JTextField) d).getText();
                                        ((JTextField) d).setText(""); // Reset text field for later use after getting value
                                        break;
                                    case 1:
                                        classStartDate = ((JTextField) d).getText();
                                        ((JTextField) d).setText("");
                                        break;
                                    case 2:
                                        classEndDate = ((JTextField) d).getText();
                                        ((JTextField) d).setText("");
                                        break;
                                    case 3:
                                        numOfAssignments = ((JTextField) d).getText();
                                        ((JTextField) d).setText("");
                                        break;
                                }
                                counter++;
                            }
                        }
                    }
                }


                if (className == null || classStartDate == null || classEndDate == null || numOfAssignments == null) {
                    // TODO: Show an error message
                    return;
                }

                // Verify that the number of assignments is actually an integer value
                for (int i = 0; i < numOfAssignments.length(); i++) {
                    if (i == 0 && numOfAssignments.charAt(i) == '-') {
                        if (numOfAssignments.length() == 1) {
                            // TODO: Show error message - not an integer
                            return;
                        }
                        continue;
                    }
                    if (Character.digit(numOfAssignments.charAt(i), 10) < 0) {
                        // TODO: Show error message - not an integer
                        return;
                    }
                }

                // This means it's an integer
                final int assignmentsInt = Integer.parseInt(numOfAssignments);

                // Verify that dates are valid
                final Date classStartDateVal = convertStringToDate(classStartDate);
                final Date classEndDateVal = convertStringToDate(classEndDate);

                if (classStartDateVal != null && classEndDateVal != null) {
                    // Create a class object
                    final Class classX = new Class(user.getUserId(), className, assignmentsInt, classStartDateVal, classEndDateVal);

                    // Try to save the class object to the database
                    try {
                        boolean classIsSuccess = Class.addClass(classX);

                        // If the class is saved successfully, let the user continue to the add assignments view
                        if (classIsSuccess) {
                            // If the user input is valid, show the assignment panel
                            newClassPanel.removeAll();
                            newClassPanel.revalidate();
                            newClassPanel.repaint();
                            final JComponent newClassTemplatePanel = new JPanel(new BorderLayout());
                            JLabel classTemplateLabel = new JLabel("Entering Assignments into Class", SwingConstants.CENTER);
                            classTemplateLabel.setFont(f2);
                            newClassTemplatePanel.add(classTemplateLabel, BorderLayout.PAGE_START);

                            // Generate the number of assignment fields specified by the user
                            newClassTemplatePanel.add(createAssignmentForm(assignmentsInt));

                            JButton createClassWithAssignmentsBtn = new JButton("Save Class Information");

                            createClassWithAssignmentsBtn.addActionListener(e1 -> {
                            // Create a class object
                            for (Component b : newClassTemplatePanel.getComponents()) {
                                if (b instanceof JPanel) {
                                    for (Component d : ((JPanel) b).getComponents()) {
                                        if (d instanceof JPanel) {
                                            int counter = 0;
                                            String assignmentDate = null;
                                            String assignmentName = null;
                                            String assignmentWeight = null;
                                            for (Component f : ((JPanel) d).getComponents()) {
                                                if (f instanceof JTextField) {
                                                    switch (counter) {
                                                        case 0:
                                                            assignmentDate = ((JTextField) f).getText();
                                                            break;
                                                        case 1:
                                                            assignmentName = ((JTextField) f).getText();
                                                            break;
                                                        case 2:
                                                            assignmentWeight = ((JTextField) f).getText();
                                                            break;
                                                    }
                                                    counter++;
                                                }
                                            }

                                            if (assignmentDate != null && assignmentName != null && assignmentWeight != null) {
                                                // Make sure the assignmentDate is actually a valid date
                                                Date assignmentDateVal = convertStringToDate(assignmentDate);

                                                // Make sure that the assignment weight is a float
                                                float assignmentWeightVal;
                                                try {
                                                    assignmentWeightVal = Float.parseFloat(assignmentWeight);
                                                } catch (NumberFormatException ex) {
                                                    // TODO: Add error message if the string isn't actually a float
                                                    System.out.println("Assignment weight is not a float");
                                                    return;
                                                }

                                                if (assignmentDateVal != null) {
                                                    // Create an assignment object
                                                    int classId = classX.getClassId();
                                                    Assignment assignment = new Assignment(user.getUserId(), classId, assignmentName, assignmentDateVal, assignmentWeightVal);

                                                    // Try to add the assignment object to the database
                                                    try {
                                                        Assignment.addAssignment(assignment);

                                                    } catch (Exception ex) {
                                                        // TODO: Show error message if assignment can't be added to the database
                                                        System.out.println("Failed to add assignment to the database");
                                                    }
                                                }

                                                assignmentDate = null;
                                                assignmentName = null;
                                                assignmentWeight = null;
                                            } else {
                                                // TODO: Show error message if any of the fields are empty for a row
                                            }
                                        }
                                    }
                                }
                            }

                            // Redraw the progress panels when new classes are added
                            drawCurrentClassesPanel(currentClassesPanel, progressPanel, user);
                            drawCompletedClassesPanel(completedClassesPanel, completedPanel, user);

                            // Restore the add class tab to its original state
                            newClassPanel.removeAll();
                            newClassPanel.revalidate();
                            newClassPanel.repaint();
                            newClassFormPanel.add(createNewClassTemplate, constraints);
                            newClassPanel.add(newClassFormPanel, BorderLayout.CENTER);
                            });
                            newClassTemplatePanel.add(createClassWithAssignmentsBtn, BorderLayout.PAGE_END);
                            newClassPanel.add(newClassTemplatePanel, BorderLayout.PAGE_START);
                        } else {
                            // Show error message

                        }
                    } catch (Exception ex) {
                        // Show error in GUI if class isn't saved to database; don't allow user to continue in application
                    }
                }
            }
        });
        newClassFormPanel.add(createNewClassTemplate, constraints);
        newClassPanel.add(newClassFormPanel, BorderLayout.CENTER);
        tabbedPane.addTab("New Class", null, newClassPanel); // Add tab to tab container
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1); // keyboard event

        //Actual Class
        //Class Panel (moved to method, after values pulled from New Class & New Class Template)
        for (int i = 0; i < currentClasses.size(); i++) {
            JComponent classPanel = new JPanel(new BorderLayout());

            JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 150, 0));

            JPanel statusPanel = new JPanel(new GridLayout(0, 1));
            String className = currentClasses.get(i).getClassName();
            JLabel classLabel = new JLabel(className, SwingConstants.LEADING);
            classLabel.setFont(f1);
            statusPanel.add(classLabel);
            char currentGrade = 'A';
            JLabel currentGradeLabel = new JLabel("Current Grade: " + currentGrade, SwingConstants.LEADING);
            currentGradeLabel.setFont(f2);
            statusPanel.add(currentGradeLabel);
            headerPanel.add(statusPanel);

            JPanel goalGradePanel = new JPanel(new GridLayout(0, 1));
            JRadioButton A = new JRadioButton("A (90-100)");
            JRadioButton B = new JRadioButton("B (80-89)");
            JRadioButton C = new JRadioButton("C (70-79)");
            JRadioButton D = new JRadioButton("D (60-69)");
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

            JPanel assignmentsPanel = new JPanel(new GridBagLayout());
            GridBagConstraints h = new GridBagConstraints();

            h.gridx = 0;
            h.gridy = 0;
            h.gridwidth = 1;
            h.fill = GridBagConstraints.HORIZONTAL;
            JLabel dueDateLabel = new JLabel("Due Date");
            assignmentsPanel.add(dueDateLabel, h);

            h.gridx = 1;
            h.gridwidth = 2;
            JLabel assignmentName = new JLabel("Assignment Name");
            assignmentsPanel.add(assignmentName, h);

            h.gridx = 3;
            h.gridwidth = 1;
            JLabel assignmentWeight = new JLabel("Assignment Weight");
            assignmentsPanel.add(assignmentWeight, h);

            h.gridx = 4;
            String earnedAssignmentWeightString = "0.0";
            JLabel earnedAssignmentWeight = new JLabel(earnedAssignmentWeightString);
            assignmentsPanel.add(earnedAssignmentWeight, h);

            h.gridx++;
            final JTextField assignmentGradeTextField = new JTextField("Enter Grade %", 10);
            assignmentGradeTextField.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                    assignmentGradeTextField.setText("");
                    assignmentGradeTextField.setForeground(new Color(50, 50, 50));
                }

                public void focusLost(FocusEvent e) {
                    if (assignmentGradeTextField.getText().length() == 0) {
                        assignmentGradeTextField.setText("Enter Grade %");
                        assignmentGradeTextField.setForeground(new Color(150, 150, 150));
                    }
                }
            });
            assignmentsPanel.add(assignmentGradeTextField, h);

            h.gridx++;
            JButton submitButton = new JButton("Submit");
            assignmentsPanel.add(submitButton, h);

            classPanel.add(assignmentsPanel);

            tabbedPane.addTab(className, null, classPanel); // Add tab to tab container
            tabbedPane.setMnemonicAt(0, KeyEvent.VK_3); // keyboard event

            // Add the tabbed pane to contentPane
            contentPane.add(tabbedPane, c);
        }

        frame.setSize(1000, 800);
        frame.setVisible(true);

    }

    private static String determineGreeting() {
        String greeting;
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date dateObj = new Date();
        int hour = Integer.parseInt(dateFormat.format(dateObj));

        if (hour >= 6 && hour < 12) {
            //Good Morning 6am to 11am
            greeting = "Good Morning";
        } else if (hour >= 12 && hour < 17) {
            //Good Afternoon 12 to 4pm
            greeting = "Good Afternoon";
        } else {
            //Good Night 8 pm to 5am
            greeting = "Good Evening";
        }

        return greeting;
    }

    private static void drawCurrentClassesPanel(JPanel currentClassesPanel, JPanel progressPanel, User user) {
        try {
            currentClasses = Class.getCurrentClasses(user.getUserId());
            if (currentClasses.size() > 0) {
                currentClassesPanel.removeAll();
                progressPanel.removeAll();

                currentClassesPanel.setBackground(new Color(179, 224, 255));
                JLabel currentClassesLabel = new JLabel("Current Classes", SwingConstants.CENTER);
                currentClassesLabel.setFont(f2);
                currentClassesPanel.add(currentClassesLabel);
                progressPanel.add(currentClassesPanel);

                for (int i = 0; i < currentClasses.size(); i++) {
                    progressPanel.add(createClassProgressPanel(currentClasses.get(i).getClassName(), currentClasses.get(i).convertToLetterGrade(), 0, 100));
                }
                currentClassesPanel.revalidate();
            }
        } catch (Exception ex) {
            System.out.println("We're sorry, but we cannot access your classes at this time. Please try again later.");
        }
    }

    private static void drawCompletedClassesPanel(JPanel completedClassesPanel, JPanel completedPanel, User user) {
        try {
            ArrayList<Class> completedClasses = Class.getCompletedClasses(user.getUserId());
            if (completedClasses.size() > 0) {
                completedClassesPanel.removeAll();
                completedPanel.removeAll();

                for (int i = 0; i < completedClasses.size(); i++) {
                    // Dynamic completed class panels
                    completedPanel.add(createCompClassPanel(completedClasses.get(i).getClassName(), completedClasses.get(i).convertToLetterGrade()));
                }
                completedClassesPanel.revalidate();
            }
        } catch (Exception ex) {
            System.out.println("We're sorry, but we cannot access your classes at this time. Please try again later.");
        }
    }

    private static JPanel createClassProgressPanel(String className, String classGrade,
                                                     int completedWeight, int totalWeight) {
        JPanel classProgressPanel = new JPanel(new GridLayout(0, 1));

        //Class Name and Grade
        JPanel classProgressTitle = new JPanel(new GridLayout(0, 2));
        JLabel classNameLabel = new JLabel(className);
        JLabel classGradeLabel = new JLabel(classGrade, SwingConstants.RIGHT);
        classProgressTitle.add(classNameLabel);
        classProgressTitle.add(classGradeLabel);
        classProgressPanel.add(classProgressTitle);
        JProgressBar progBar = new JProgressBar(0, totalWeight);
        progBar.setValue(completedWeight);
        progBar.setStringPainted(true);
        classProgressPanel.add(progBar);

        return classProgressPanel;
    }

    private static JPanel createCompClassPanel(String className, String classGrade) {
        JPanel compClassPanel = new JPanel(new GridLayout(0, 2));
        JLabel compClassName = new JLabel(className);
        JLabel compClassGrade = new JLabel(classGrade, SwingConstants.RIGHT);
        compClassPanel.add(compClassName);
        compClassPanel.add(compClassGrade);

        return compClassPanel;
    }

    private static Date convertStringToDate(String date) {
        try {
            return new SimpleDateFormat("MM/dd/yyyy").parse(date.trim());
        } catch (ParseException ex) {
            // TODO: Add error message if the string isn't actually a date
            System.out.println("Not a date");
            return null;
        }
    }

    private static JPanel createAddClassPanel() {
        final JPanel classInfoPanel = new JPanel(new GridLayout(0, 2));

        classInfoPanel.add(new JLabel());
        classInfoPanel.add(new JLabel());

        final JLabel classNameLabel = new JLabel("Class Name: ");
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

        return classInfoPanel;
    }

    private static JPanel createAssignmentForm(int numOfAssignments) {
        Font f3 = new Font("Monospaced", Font.BOLD, 12);

        JPanel assignmentsFormPanel = new JPanel(new GridLayout(0, 1));

        JPanel assignmentsLabelPanel = new JPanel(new GridBagLayout());
        GridBagConstraints f = new GridBagConstraints();
        f.gridx = 1;
        f.gridy = 0;
        JLabel dueDateLabel = new JLabel("Due Date");
        dueDateLabel.setFont(f3);
        assignmentsLabelPanel.add(dueDateLabel, f);

        f.gridx = 2;
        f.gridwidth = 2;
        f.fill = GridBagConstraints.HORIZONTAL;
        JLabel assignNameLabel = new JLabel("Assign. Name");
        assignNameLabel.setFont(f3);
        assignmentsLabelPanel.add(assignNameLabel, f);

        f.gridx = 4;
        f.gridwidth = 1;
        JLabel assignWeightLabel = new JLabel("Assign. Weight");
        assignWeightLabel.setFont(f3);
        assignmentsLabelPanel.add(assignWeightLabel, f);

        assignmentsFormPanel.add(assignmentsLabelPanel);

        int assignNo = 1;
        //create each assignment's form
        for (int i = 0; i < numOfAssignments; i++) {
            //attempting to switch to gridBagLayout

            JPanel anAssignmentPanel = new JPanel(new GridBagLayout());
            GridBagConstraints g = new GridBagConstraints();
            g.gridx = 0;
            g.gridy = 0;
            JLabel assignNumLabel = new JLabel("Assignment " + assignNo);
            assignNo++;
            anAssignmentPanel.add(assignNumLabel, g);

            g.gridx = 1;
            final JTextField dateTF = new JTextField("--/--/----", 9);
            dateTF.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                    dateTF.setText("");
                    dateTF.setForeground(new Color(50, 50, 50));
                }

                public void focusLost(FocusEvent e) {
                    if (dateTF.getText().length() == 0) {
                        dateTF.setText("--/--/----");
                        dateTF.setForeground(new Color(150, 150, 150));
                    }
                }
            });
            anAssignmentPanel.add(dateTF, g);

            g.gridx = 2;
            g.gridwidth = 2;
            g.fill = GridBagConstraints.HORIZONTAL;
            final JTextField assignNameTF = new JTextField("Assignment Name", 15);
            assignNameTF.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                    assignNameTF.setText("");
                    assignNameTF.setForeground(new Color(50, 50, 50));
                }

                public void focusLost(FocusEvent e) {
                    if (assignNameTF.getText().length() == 0) {
                        assignNameTF.setText("Assignment Name");
                        assignNameTF.setForeground(new Color(150, 150, 150));
                    }
                }
            });
            anAssignmentPanel.add(assignNameTF, g);

            g.gridx = 4;
            g.gridwidth = 1;
            final JTextField assignWeightTextField = new JTextField("Weight", 5);
            assignWeightTextField.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                    assignWeightTextField.setText("");
                    assignWeightTextField.setForeground(new Color(50, 50, 50));
                }

                public void focusLost(FocusEvent e) {
                    if (assignWeightTextField.getText().length() == 0) {
                        assignWeightTextField.setText("Weight");
                        assignWeightTextField.setForeground(new Color(150, 150, 150));
                    }
                }
            });
            anAssignmentPanel.add(assignWeightTextField, g);

            assignmentsFormPanel.add(anAssignmentPanel);
        }

        return assignmentsFormPanel;
    }
}
