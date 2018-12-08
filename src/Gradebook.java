/*********************************************************************************************************
 * File name: Gradebook.java
 * Date: November 2018
 * Author: Haemee Nabors, Rebecca Deprey, Devon Artist, Harry Giles, Brittany White, Ryan Haas
 * Purpose: This class extends the JFrame class to create a JFrame window for the GradeBook GUI. It has
 * methods to allow students to add classes and assignments; add grades for assignments; view their
 * progress in current classes; and review their grades from completed classes.
 *
 * The GUI uses Java Swing components.
 *
 * References:
 * 1. Java Tutorials Code Sample – getCodeSampleUrl(); document.write(codeSampleName);. (n.d.). Retrieved
 * from https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial
 * /uiswing/examples/components/TabbedPaneDemoProject/src/components/TabbedPaneDemo.java
 *
 * 2. Determine if a String is an Integer in Java. (n.d.). Retrieved from https://stackoverflow.com/questions
 * /5439529/determine-if-a-string-is-an-integer-in-java
 *********************************************************************************************************/

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

class Gradebook extends JFrame {
    private static User user;
    private static final Font f1 = new Font("Monospaced", Font.BOLD, 20);
    private static final Font f2 = new Font("Monospaced", Font.BOLD, 16);
    private static final Font tableHeadFont = new Font("Monospaced", Font.BOLD, 13);
    private static ArrayList<Class> currentClasses;
    private static final JPanel progressPanel = new JPanel(new GridLayout(0, 1, 5, 5));
    private static final JPanel currentClassesPanel = new JPanel(new GridLayout(0, 1, 5, 5));
    private static final JPanel completedPanel = new JPanel(new GridLayout(0, 1, 5, 5));
    private static final JPanel completedClassesPanel = new JPanel(new GridLayout(0, 1, 5, 5));
    private static final JPanel classInfoPanel = createAddClassPanel();
    private static final JTabbedPane tabbedPane = new JTabbedPane();
    private static final int sizeLimit = 100; // Maximum number of classes to retrieve
    private static BGlistener bglistener = new BGlistener();

    //creates GUI
    public Gradebook(final User user) throws Exception {
        Gradebook.user = user;
        String userName = user.getUsername();
        String date = new SimpleDateFormat("EEEEE, MMMMM d, yyyy").format(new Date());
        String greetingMessage = determineGreeting();

        // Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int a = JOptionPane.showConfirmDialog(null, "Logout?");
                if(a == JOptionPane.YES_OPTION){
                    System.exit(0);
                }

            }
        });

        JFrame frame = new JFrame("GradeBook");
        frame.setSize(1500, 1000);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel(new GridBagLayout());
        frame.setContentPane(contentPane);
        frame.setMinimumSize(new Dimension(1500, 600));
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
        nameDatePanel.add(logoutBtn);
        titlePanel.add(nameDatePanel, BorderLayout.EAST);
        contentPane.add(titlePanel, c);

        //GreetingPanel
        c.gridy += 1;
        c.gridwidth = 2;
        JPanel greetingPanel = new JPanel(new GridLayout(0, 1));
        greetingPanel.setBackground(new Color(102, 194, 255));
        JLabel greetingLabel = new JLabel(greetingMessage + ",", SwingConstants.CENTER);
        greetingLabel.setFont(f2);
        greetingPanel.add(new JLabel());
        greetingPanel.add(greetingLabel);
        JLabel usernameLbl = new JLabel(userName, SwingConstants.CENTER);
        usernameLbl.setFont(f2);
        greetingPanel.add(usernameLbl);
        JLabel dateLbl = new JLabel("<html><p style='text-align:center;'>" + date + "</p></html>");
        dateLbl.setHorizontalAlignment(SwingConstants.CENTER);
        greetingPanel.add(dateLbl);
        greetingPanel.add(new JLabel(" "));
        contentPane.add(greetingPanel, c);

        //ProgressPanel
        c.gridy += 1;
        c.gridwidth = 2;
        drawCurrentClassesPanel(user); // dynamic progress panels
        JScrollPane sp = new JScrollPane(progressPanel);
        JPanel jp = new JPanel(new GridLayout(0, 1, 5, 5));
        jp.setPreferredSize(new Dimension(200, 200));
        jp.add(sp);
        contentPane.add(jp, c);


        //CompletedPanel
        c.gridy += 1;
        c.gridwidth = 2;
        JPanel compClassTitlePanel = new JPanel(new GridLayout(0, 1, 15, 15));
        compClassTitlePanel.setBackground(new Color(179, 224, 255));
        JLabel compClassLabel = new JLabel("Completed Classes", SwingConstants.CENTER);
        compClassLabel.setFont(f2);
        compClassTitlePanel.add(compClassLabel);
        contentPane.add(compClassTitlePanel, c);

        c.gridy += 1;
        drawCompletedClassesPanel(user);
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
        tabbedPane.setPreferredSize(new Dimension(1100, 700));

        //New Class
        final JPanel newClassPanel = new JPanel(new BorderLayout());
        final JPanel newClassFormPanel = new JPanel(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.PAGE_START;
        newClassFormPanel.add(classInfoPanel, constraints);
        newClassPanel.add(newClassFormPanel, BorderLayout.CENTER);
        tabbedPane.addTab("New Class", null, newClassPanel); // Add tab to tab container
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1); // keyboard event

        if (currentClasses.size() > sizeLimit) {
            throw new ArithmeticException("List of current classes is too large");
        }

        // Class panel
        for (int i = 0; i < currentClasses.size(); i++) {
            String className = currentClasses.get(i).getClassName();
            JComponent parentPanel = createCurrentClassTab(className, currentClasses.get(i).getClassId(), 0.0f);
            tabbedPane.addTab(className, null, parentPanel); // Add tab to tab container
            tabbedPane.setMnemonicAt(0, KeyEvent.VK_3); // keyboard event
        }

        // Add the tabbed pane to contentPane
        contentPane.add(tabbedPane, c);

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
            //Good Evening 4 pm to 5am
            greeting = "Good Evening";
        }

        return greeting;
    }

    private static void drawCurrentClassesPanel(User user) {
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

                if (currentClasses.size() > sizeLimit) {
                    throw new ArithmeticException("List of current classes is too large");
                }

                for (int i = 0; i < currentClasses.size(); i++) {
                    String classGrade = Class.getClassGrade(user.getUserId(), currentClasses.get(i).getClassId());
                    String letterGrade = (classGrade == null) ? null : classGrade;
                    ArrayList<Assignment> assignments = Assignment.getAssignmentsForClass(user.getUserId(), currentClasses.get(i).getClassId());
                    float completedAssignmentWeight = 0;

                    if (assignments.size() > sizeLimit) {
                        throw new ArithmeticException("List of assignments is too large");
                    }

                    for (int j = 0; j < assignments.size(); j++) {
                        if (assignments.get(j).getAssignmentGrade() != 0.0) {
                            float currentWeight = assignments.get(j).getAssignmentWeight();
                            completedAssignmentWeight += currentWeight;
                        }
                    }

                    progressPanel.add(createClassProgressPanel(currentClasses.get(i).getClassName(), letterGrade, completedAssignmentWeight, 100));
                }
                currentClassesPanel.revalidate();
            }
        } catch (Exception ex) {
            System.out.println("We're sorry, but we cannot access your classes at this time. Please try again later.");
        }
    }

    private static void drawCompletedClassesPanel(User user) {
        try {
            ArrayList<Class> completedClasses = Class.getCompletedClasses(user.getUserId());
            if (completedClasses.size() > 0 && completedClasses.size() <= sizeLimit) {
                completedClassesPanel.removeAll();
                completedPanel.removeAll();

                for (int i = 0; i < completedClasses.size(); i++) {
                    // Dynamic completed class panels
                    completedPanel.add(createCompClassPanel(completedClasses.get(i).getClassName(), Class.convertToLetterGrade(completedClasses.get(i).getClassGrade())));
                }
                completedClassesPanel.revalidate();
            } else if (completedClasses.size() > sizeLimit) {
                throw new ArithmeticException("List of completed classes is too large.");
            }
        } catch (Exception ex) {
            System.out.println("We're sorry, but we cannot access your classes at this time. Please try again later.");
        }
    }

    private static JPanel createClassProgressPanel(String className, String classGrade,
                                                   float completedWeight, int totalWeight) {
        JPanel classProgressPanel = new JPanel(new GridLayout(0, 1));

        //Class Name and Grade
        JPanel classProgressTitle = new JPanel(new GridLayout(0, 2));
        JLabel classNameLabel = new JLabel(className);
        JLabel classGradeLabel = new JLabel(classGrade, SwingConstants.RIGHT);
        classProgressTitle.add(classNameLabel);
        classProgressTitle.add(classGradeLabel);
        classProgressPanel.add(classProgressTitle);
        JProgressBar progBar = new JProgressBar(0, totalWeight);
        progBar.setValue(Math.round(completedWeight));
        progBar.setStringPainted(true);
        progBar.setString(String.valueOf(Math.round(completedWeight)) + "% completed");
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
            return null;
        }
    }

    private static boolean isInteger(String value) {
        if (value.length() > sizeLimit) {
            throw new ArithmeticException("String value is too large.");
        }

        for (int i = 0; i < value.length(); i++) {
            if (i == 0 && value.charAt(i) == '-') {
                if (value.length() == 1) {
                    return false;
                }
                continue;
            }
            if (Character.digit(value.charAt(i), 10) < 0) {
                return false;
            }
        }
        return true;
    }

    private static JPanel createAddClassPanel() {
        Map<String, Boolean> isValid = new HashMap<>();
        isValid.put("className", false);
        isValid.put("startDate", false);
        isValid.put("endDate", false);
        isValid.put("numberOfAssignments", false);

        JButton createNewClassBtn = new JButton("Add Assignments");

        final JPanel classInfoPanel = new JPanel(new GridBagLayout());
        classInfoPanel.setSize(new Dimension(1100, 700));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.PAGE_START;

        g.gridwidth = 5;
        g.weightx = 1.0;
        g.gridx = 2;
        g.gridy = 0;
        JPanel newClassLabelPanel = new JPanel(new GridLayout(0,1));
        JLabel newClassLabel = new JLabel("Create a New Class");
        newClassLabel.setHorizontalAlignment(SwingConstants.CENTER);
        newClassLabel.setFont(f2);
        JLabel allFieldsRequiredLabel = new JLabel("All Fields Are Required");
        allFieldsRequiredLabel.setForeground(Color.red);
        allFieldsRequiredLabel.setHorizontalAlignment(SwingConstants.CENTER);
        newClassLabelPanel.add(newClassLabel);
        newClassLabelPanel.add(allFieldsRequiredLabel);
        newClassLabelPanel.add(new JLabel(""));
        classInfoPanel.add(newClassLabelPanel, g);

        g.gridx = 0;
        g.gridy = 1;
        g.insets = new Insets(0, 0, 0, 100);
        classInfoPanel.add(new JLabel(), g);

        g.gridx = 0;
        g.gridy = 2;
        final JLabel classNameLabel = new JLabel("Class Name: ");
        classInfoPanel.add(classNameLabel, g);
        final JLabel classNameError = new JLabel();
        classNameError.setForeground(Color.red);
        classNameError.setVisible(false);

        g.gridx = 6;
        g.gridy = 2;
        final JTextField nameTextField = new JTextField(10);
        nameTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                classNameError.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (nameTextField.getText().isEmpty()) {
                    classNameError.setText("You must enter a class name.");
                    classNameError.setVisible(true);
                    isValid.put("className", false);
                    enableButton(createNewClassBtn, isValid);
                } else {
                    isValid.put("className", true);
                    enableButton(createNewClassBtn, isValid);
                }
            }
        });
        classInfoPanel.add(nameTextField, g);

        g.gridx = 0;
        g.gridy = 3;
        classInfoPanel.add(classNameError, g);

        g.gridx = 0;
        g.gridy = 4;
        final JLabel classStartDateLabel = new JLabel("Class Start Date: ");
        classInfoPanel.add(classStartDateLabel, g);
        final JLabel classStartDateError = new JLabel();
        classStartDateError.setVisible(false);
        classStartDateError.setForeground(Color.RED);


        g.gridx = 6;
        g.gridy = 4;
        final JTextField classStartDateTextField = new JTextField("MM/DD/YYYY", 9);
        classStartDateTextField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                classStartDateError.setVisible(false);
                classStartDateTextField.setText("");
                classStartDateTextField.setForeground(new Color(50, 50, 50));
            }

            public void focusLost(FocusEvent e) {
                if (classStartDateTextField.getText().length() == 0) {
                    classStartDateTextField.setText("MM/DD/YYYY");
                    classStartDateTextField.setForeground(new Color(150, 150, 150));
                }

                Date date = new Date();
                LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int currentYear  = localDate.getYear();

                Date classStartDateVal = convertStringToDate(classStartDateTextField.getText());
                LocalDate startLocalDate = classStartDateVal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                int startYear = startLocalDate.getYear();
                if (classStartDateVal == null) {
                    classStartDateError.setText("You must enter a valid date.");
                    classStartDateError.setVisible(true);
                    isValid.put("startDate", false);
                    enableButton(createNewClassBtn, isValid);
                } else if (startYear < currentYear) {
                    classStartDateError.setText("The start date must be within the current year.");
                    classStartDateError.setVisible(true);
                    isValid.put("startDate", false);
                    enableButton(createNewClassBtn, isValid);
                } else {
                    isValid.put("startDate", true);
                    enableButton(createNewClassBtn, isValid);
                }
            }
        });
        classInfoPanel.add(classStartDateTextField, g);

        g.gridx = 0;
        g.gridy = 5;
        classInfoPanel.add(classStartDateError, g);

        g.gridx = 0;
        g.gridy = 6;
        final JLabel classEndDateLabel = new JLabel("Class End Date: ");
        classInfoPanel.add(classEndDateLabel, g);
        final JLabel classEndDateError = new JLabel();
        classEndDateError.setVisible(false);
        classEndDateError.setForeground(Color.RED);

        g.gridx = 6;
        g.gridy = 6;
        final JTextField classEndDateTextField = new JTextField("MM/DD/YYYY", 9);
        classEndDateTextField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                classEndDateError.setVisible(false);
                classEndDateTextField.setText("");
                classEndDateTextField.setForeground(new Color(50, 50, 50));
            }

            public void focusLost(FocusEvent e) {
                if (classEndDateTextField.getText().length() == 0) {
                    classEndDateTextField.setText("MM/DD/YYYY");
                    classEndDateTextField.setForeground(new Color(150, 150, 150));
                }

                Date classEndDateVal = convertStringToDate(classEndDateTextField.getText());
                if (classEndDateVal == null) {
                    classEndDateError.setText("You must enter a valid date.");
                    classEndDateError.setVisible(true);
                    isValid.put("endDate", false);
                    enableButton(createNewClassBtn, isValid);
                } else if (classEndDateVal.before(convertStringToDate(classStartDateTextField.getText()))) {
                    classEndDateError.setText("The class end date must be later than the class start date.");
                    classEndDateError.setVisible(true);
                    isValid.put("endDate", false);
                    enableButton(createNewClassBtn, isValid);
                } else {
                    isValid.put("endDate", true);
                    enableButton(createNewClassBtn, isValid);
                }
            }
        });
        classInfoPanel.add(classEndDateTextField, g);

        g.gridx = 0;
        g.gridy = 7;
        classInfoPanel.add(classEndDateError, g);

        g.gridx = 0;
        g.gridy = 8;
        final JLabel numberOfAssignmentsLabel = new JLabel("Number of Assignments: ");
        classInfoPanel.add(numberOfAssignmentsLabel, g);
        final JLabel numberOfAssignmentsError = new JLabel();
        numberOfAssignmentsError.setVisible(false);
        numberOfAssignmentsError.setForeground(Color.RED);

        g.gridx = 6;
        g.gridy = 8;
        final JTextField numOfAssignmentsTextField = new JTextField(5);
        numOfAssignmentsTextField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                numberOfAssignmentsError.setVisible(false);
            }

            @Override
            public void focusLost(FocusEvent e) {
                // Verify that the number of assignments is actually an integer value
                boolean numAssignmentsIsInteger = isInteger(numOfAssignmentsTextField.getText());
                setTextfieldValidity(numAssignmentsIsInteger, numOfAssignmentsTextField.getText(), isValid, numberOfAssignmentsError, createNewClassBtn);
            }
        });

        numOfAssignmentsTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                boolean numAssignmentsIsInteger = isInteger(numOfAssignmentsTextField.getText());
                setTextfieldValidity(numAssignmentsIsInteger, numOfAssignmentsTextField.getText(), isValid, numberOfAssignmentsError, createNewClassBtn);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                boolean numAssignmentsIsInteger = isInteger(numOfAssignmentsTextField.getText());
                setTextfieldValidity(numAssignmentsIsInteger, numOfAssignmentsTextField.getText(), isValid, numberOfAssignmentsError, createNewClassBtn);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        classInfoPanel.add(numOfAssignmentsTextField, g);

        g.gridx = 0;
        g.gridy = 9;
        classInfoPanel.add(numberOfAssignmentsError, g);

        g.gridwidth = 10;
        g.gridx = 0;
        g.gridy = 10;
        createNewClassBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String className = nameTextField.getText();
                Date classStartDate = convertStringToDate(classStartDateTextField.getText());
                Date classEndDate = convertStringToDate(classEndDateTextField.getText());
                int numOfAssignments = Integer.parseInt(numOfAssignmentsTextField.getText());

                // Create a class object
                final Class classX = new Class(user.getUserId(), className, numOfAssignments, classStartDate, classEndDate);

                // Try to save the class object to the database
                try {
                    boolean classIsSuccess = Class.addClass(classX);

                    // If the class is saved successfully, let the user continue to the add assignments view
                    if (classIsSuccess) {
                        // If the user input is valid, show the assignment panel
                        classInfoPanel.removeAll();
                        classInfoPanel.revalidate();
                        classInfoPanel.repaint();
                        final JComponent newClassTemplatePanel = new JPanel(new BorderLayout());
                        JPanel classTemplatePanel = new JPanel(new GridLayout(0,1));
                        JLabel classTemplateLabel = new JLabel("Entering Assignments into Class", SwingConstants.CENTER);
                        classTemplateLabel.setFont(f2);
                        classTemplateLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        JLabel allFieldsRequiredLabel = new JLabel("All Fields Are Required");
                        allFieldsRequiredLabel.setForeground(Color.red);
                        allFieldsRequiredLabel.setHorizontalAlignment(SwingConstants.CENTER);
                        classTemplatePanel.add(classTemplateLabel);
                        classTemplatePanel.add(allFieldsRequiredLabel);
                        classTemplatePanel.add(new JLabel(""));

                        newClassTemplatePanel.add(classTemplatePanel, BorderLayout.PAGE_START);

                        // Generate the number of assignment fields specified by the user
                        newClassTemplatePanel.add(createAssignmentForm(numOfAssignments));

                        // Create new panel with border layout for the save and delete buttons
                        JPanel addClassBtnPanel = new JPanel(new GridBagLayout());
                        GridBagConstraints btnConstraints = new GridBagConstraints();

                        JButton createClassWithAssignmentsBtn = new JButton("Save Class Information");
                        createClassWithAssignmentsBtn.addActionListener(e1 -> {
                            int classId = classX.getClassId();

                            // Create a class object
                            for (Component b : newClassTemplatePanel.getComponents()) {
                                if (b instanceof JPanel) {
                                    int counter = 0;
                                    String assignmentDate = null;
                                    String assignmentName = null;
                                    String assignmentWeight = null;
                                    for (Component f : ((JPanel) b).getComponents()) {
                                        if (f instanceof JPanel) {
                                            for (Component k : ((JPanel) f).getComponents()) {
                                                if (k instanceof JTextField) {
                                                    switch (counter) {
                                                        case 0:
                                                            assignmentDate = ((JTextField) k).getText();
                                                            break;
                                                        case 1:
                                                            assignmentName = ((JTextField) k).getText();
                                                            break;
                                                        case 2:
                                                            assignmentWeight = ((JTextField) k).getText();
                                                            break;
                                                    }

                                                    if (counter == 2) {
                                                        counter = 0;
                                                        // Create an assignment object
                                                        Assignment assignment = new Assignment(user.getUserId(), classId, assignmentName, convertStringToDate(assignmentDate), Float.parseFloat(assignmentWeight));
                                                        assignmentDate = null;
                                                        assignmentName = null;
                                                        assignmentWeight = null;

                                                        // Try to add the assignment object to the database
                                                        try {
                                                            Assignment.addAssignment(assignment);
                                                            counter = 0;
                                                        } catch (Exception ex) {
                                                            System.out.println("Failed to add assignment to the database");
                                                        }
                                                    } else {
                                                        counter += 1;

                                                        if (counter > 150) {
                                                            throw new ArithmeticException("Counter is too large.");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Redraw the progress panels when new classes are added
                            drawCurrentClassesPanel(user);
                            drawCompletedClassesPanel(user);

                            if (classEndDate.after(new Date())) {
                                tabbedPane.addTab(className, null, createCurrentClassTab(className, classId, 0.0f));
                            }

                            // Restore the add class tab to its original state
                            classInfoPanel.removeAll();
                            classInfoPanel.revalidate();
                            classInfoPanel.repaint();
                            classInfoPanel.add(createAddClassPanel());
                        });

                        btnConstraints.anchor = GridBagConstraints.PAGE_END;
                        btnConstraints.fill = GridBagConstraints.CENTER;
                        btnConstraints.gridx = 1;
                        btnConstraints.gridy = 0;
                        addClassBtnPanel.add(createClassWithAssignmentsBtn, btnConstraints);

                        JButton delete = new JButton("Cancel");
                        delete.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                int a = JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel?");
                                if(a == JOptionPane.YES_OPTION) {
                                    try {
                                        // Delete the class and its assignments from the database
                                        Class.deleteClass(user.getUserId(), classX.getClassId());

                                        // Reset the create new class view
                                        classInfoPanel.removeAll();
                                        classInfoPanel.revalidate();
                                        classInfoPanel.repaint();
                                        classInfoPanel.add(createAddClassPanel());
                                    } catch (Exception ex) {
                                        System.out.println("Can't cancel class creation.");
                                    }
                                }
                            }
                        });

                        btnConstraints.gridx = 0;
                        addClassBtnPanel.add(delete, btnConstraints);
                        newClassTemplatePanel.add(addClassBtnPanel, BorderLayout.PAGE_END);

                        classInfoPanel.add(newClassTemplatePanel, BorderLayout.PAGE_START);
                    } else {
                       System.out.println("Class wasn't successfully saved to the database.");
                    }
                } catch (Exception ex) {
                    System.out.println("Class couldn't be saved to the database.");
                }
            }
        });

        classInfoPanel.add(createNewClassBtn, g);

        enableButton(createNewClassBtn, isValid);

        return classInfoPanel;
    }

    private static void setTextfieldValidity(boolean isInteger, String value, Map<String, Boolean> isValid, JLabel numberOfAssignmentsError, JButton createNewClassBtn) {
        if (!isInteger) {
            numberOfAssignmentsError.setText("Please enter an integer value for the number of assignments");
            numberOfAssignmentsError.setVisible(true);
            isValid.put("numberOfAssignments", false);
            enableButton(createNewClassBtn, isValid);
        } else if (!value.isEmpty() && Integer.parseInt(value) == 0) {
            numberOfAssignmentsError.setText("Please enter a value greater than zero.");
            numberOfAssignmentsError.setVisible(true);
            isValid.put("numberOfAssignments", false);
            enableButton(createNewClassBtn, isValid);
        } else {
            numberOfAssignmentsError.setVisible(false);
            isValid.put("numberOfAssignments", true);
            enableButton(createNewClassBtn, isValid);
        }
    }

    private static void enableButton(JButton button, Map<String, Boolean> isValid) {
        if (isValid.containsValue(false)) {
            button.setEnabled(false);
        } else {
            button.setEnabled(true);
        }
    }

    private static JPanel createAssignmentForm(int numOfAssignments) {
        Font f3 = new Font("Monospaced", Font.BOLD, 12);

        if (numOfAssignments > sizeLimit) {
            throw new ArithmeticException("The number of assignments is too large.");
        }

        JPanel assignmentsFormPanel = new JPanel(new GridLayout(1 + numOfAssignments, 1));
        assignmentsFormPanel.setSize(new Dimension(1100, 700));

        JPanel assignmentsLabelPanel = new JPanel(new GridBagLayout());
        GridBagConstraints f = new GridBagConstraints();
        f.fill = GridBagConstraints.HORIZONTAL;
        f.anchor = GridBagConstraints.PAGE_START;

        f.gridx = 0;
        f.gridy = 0;
        JLabel spaceLabel = new JLabel("");
        spaceLabel.setPreferredSize(new Dimension(100, 20));
        assignmentsLabelPanel.add(spaceLabel, f);

        f.gridx = 2;
        f.gridy = 0;
        JLabel dueDateLabel = new JLabel("Due Date");
        dueDateLabel.setPreferredSize(new Dimension(220, 20));
        dueDateLabel.setFont(f3);
        assignmentsLabelPanel.add(dueDateLabel, f);

        f.gridx = 5;
        f.gridwidth = 2;
        JLabel assignNameLabel = new JLabel("Assignment Name");
        assignNameLabel.setPreferredSize(new Dimension(260, 20));
        assignNameLabel.setFont(f3);
        assignmentsLabelPanel.add(assignNameLabel, f);

        f.gridx = 8;
        f.gridwidth = 1;
        JLabel assignWeightLabel = new JLabel("Assignment Weight");
        assignWeightLabel.setFont(f3);
        assignmentsLabelPanel.add(assignWeightLabel, f);

        assignmentsFormPanel.add(assignmentsLabelPanel);

        int assignNo = 1;

        //create each assignment's form
        for (int i = 0; i < numOfAssignments; i++) {
            if ((i + 1) > sizeLimit) {
                throw new ArithmeticException("Counter is too large.");
            }

            JPanel anAssignmentPanel = new JPanel(new GridBagLayout());
            anAssignmentPanel.setSize(new Dimension(1100, 700));
            GridBagConstraints g = new GridBagConstraints();
            g.fill = GridBagConstraints.HORIZONTAL;
            g.anchor = GridBagConstraints.PAGE_START;
            g.insets = new Insets(0, 0, 0, 50);

            JLabel assignmentDateError = new JLabel();
            assignmentDateError.setForeground(Color.RED);
            assignmentDateError.setVisible(false);
            g.gridx = 2;
            g.gridy = i + 1;
            anAssignmentPanel.add(assignmentDateError, g);

            g.gridx = 0;
            g.gridy = i;
            JLabel assignNumLabel = new JLabel("Assignment " + assignNo);
            assignNo += 1;
            anAssignmentPanel.add(assignNumLabel, g);

            g.gridx = 2;
            g.gridy = i;
            final JTextField dateTF = new JTextField("MM/DD/YYYY");
            dateTF.setPreferredSize(new Dimension(200, 25));
            dateTF.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                    dateTF.setText("");
                    dateTF.setForeground(new Color(50, 50, 50));
                    assignmentDateError.setVisible(false);
                }

                public void focusLost(FocusEvent e) {
                    if (dateTF.getText().length() == 0) {
                        dateTF.setText("MM/DD/YYYY");
                        dateTF.setForeground(new Color(150, 150, 150));

                        Date assignmentDate = convertStringToDate(dateTF.getText());
                        if (dateTF.getText().length() == 0 || assignmentDate == null) {
                            assignmentDateError.setText("You must enter a valid date.");
                            assignmentDateError.setVisible(true);
                        }
                    }
                }
            });
            anAssignmentPanel.add(dateTF, g);

            JLabel assignmentNameError = new JLabel();
            assignmentNameError.setForeground(Color.RED);
            assignmentNameError.setVisible(false);
            g.gridx = 4;
            g.gridy = i + 1;
            anAssignmentPanel.add(assignmentNameError, g);

            g.gridx = 4;
            g.gridy = i;
            final JTextField assignNameTF = new JTextField("Assignment Name");
            assignNameTF.setPreferredSize(new Dimension(240, 25));
            assignNameTF.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                    assignNameTF.setText("");
                    assignNameTF.setForeground(new Color(50, 50, 50));
                    assignmentNameError.setVisible(false);
                }

                public void focusLost(FocusEvent e) {
                    if (assignNameTF.getText().length() == 0) {
                        assignNameTF.setText("Assignment Name");
                        assignNameTF.setForeground(new Color(150, 150, 150));

                        assignmentNameError.setText("You must enter an assignment name.");
                        assignmentNameError.setVisible(true);
                    }
                }
            });
            anAssignmentPanel.add(assignNameTF, g);

            JLabel assignmentWeightError = new JLabel();
            assignmentWeightError.setForeground(Color.RED);
            assignmentWeightError.setVisible(false);
            g.gridx = 8;
            g.gridy = i + 1;
            anAssignmentPanel.add(assignmentWeightError, g);

            g.gridx = 8;
            g.gridy = i;
            final JTextField assignWeightTextField = new JTextField("Weight");
            assignWeightTextField.setPreferredSize(new Dimension(200, 25));
            assignWeightTextField.addFocusListener(new FocusListener() {
                public void focusGained(FocusEvent e) {
                    assignWeightTextField.setText("");
                    assignWeightTextField.setForeground(new Color(50, 50, 50));
                    assignmentWeightError.setVisible(false);
                }

                public void focusLost(FocusEvent e) {
                    if (assignWeightTextField.getText().length() == 0) {
                        assignWeightTextField.setText("Weight");
                        assignWeightTextField.setForeground(new Color(150, 150, 150));
                    }

                    try {
                        if (assignWeightTextField.getText() != null) {
                            Float.parseFloat(assignWeightTextField.getText());
                        } else {
                            assignmentWeightError.setText("You must enter a valid weight");
                            assignmentWeightError.setVisible(true);
                        }
                    } catch (NumberFormatException ex) {
                        assignmentWeightError.setText("You must enter a valid weight");
                        assignmentWeightError.setVisible(true);
                    }
                }
            });
            anAssignmentPanel.add(assignWeightTextField, g);

            assignmentsFormPanel.add(anAssignmentPanel);
        }

        return assignmentsFormPanel;
    }

    private static JPanel classAssignmentPanel(int classId, int assignmentId, Date dueDate, String assignmentName, float assignmentWeight, float grade, float goalGrade) {
        Dimension labelDimensions = new Dimension(157, 25);
        JPanel assignmentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints h = new GridBagConstraints();

        h.weightx = 1;
        h.weighty = 1;
        h.gridx = 0;
        h.gridy = 0;
        h.gridwidth = 1;
        h.insets = new Insets(10, 10, 0, 10);
        h.fill = GridBagConstraints.HORIZONTAL;
        h.anchor = GridBagConstraints.NORTHWEST;
        JLabel dueDateLabel = new JLabel(new SimpleDateFormat("MM/dd/yyyy").format(dueDate));
        dueDateLabel.setPreferredSize(labelDimensions);
        dueDateLabel.setMaximumSize(labelDimensions);
        dueDateLabel.setMinimumSize(labelDimensions);
        assignmentPanel.add(dueDateLabel, h);

        h.gridx = 1;
        JLabel assignmentNameLabel = new JLabel(assignmentName);
        assignmentNameLabel.setPreferredSize(labelDimensions);
        assignmentNameLabel.setMaximumSize(labelDimensions);
        assignmentNameLabel.setMinimumSize(labelDimensions);
        assignmentPanel.add(assignmentNameLabel, h);

        h.gridx = 2;
        JLabel assingmentWeight = new JLabel(new DecimalFormat("#.#").format(assignmentWeight));
        assingmentWeight.setPreferredSize(labelDimensions);
        assingmentWeight.setMaximumSize(labelDimensions);
        assingmentWeight.setMinimumSize(labelDimensions);
        assignmentPanel.add(assingmentWeight, h);

        if (grade != 0.0) {
            float earnedAssignmentWeight = assignmentWeight * (grade / 100);

            h.gridx = 3;
            JLabel earnedAssignmentWeightLabel = new JLabel(new DecimalFormat("#.#").format(earnedAssignmentWeight));
            earnedAssignmentWeightLabel.setPreferredSize(labelDimensions);
            earnedAssignmentWeightLabel.setMaximumSize(labelDimensions);
            earnedAssignmentWeightLabel.setMinimumSize(labelDimensions);
            assignmentPanel.add(earnedAssignmentWeightLabel, h);

            //goalGradeLabel calcBaseLine
            h.gridx = 4;
            JLabel goalGradeLabel;
            goalGradeLabel = new JLabel(" ");
            goalGradeLabel.setPreferredSize(labelDimensions);
            goalGradeLabel.setMaximumSize(labelDimensions);
            goalGradeLabel.setMinimumSize(labelDimensions);
            assignmentPanel.add(goalGradeLabel, h);

            h.gridx = 5;
            JLabel gradeEarnedLabel = new JLabel(new DecimalFormat("#.#").format(grade));
            gradeEarnedLabel.setPreferredSize(labelDimensions);
            gradeEarnedLabel.setMaximumSize(labelDimensions);
            gradeEarnedLabel.setMinimumSize(labelDimensions);
            assignmentPanel.add(gradeEarnedLabel, h);

            h.gridx = 6;
            JLabel spacer = new JLabel(" ");
            spacer.setPreferredSize(labelDimensions);
            spacer.setMaximumSize(labelDimensions);
            spacer.setMinimumSize(labelDimensions);
            assignmentPanel.add(spacer, h);
        } else {
            h.gridx = 3;
            JLabel earnedAssignmentWeightSpacer = new JLabel(" ");
            earnedAssignmentWeightSpacer.setPreferredSize(labelDimensions);
            earnedAssignmentWeightSpacer.setMaximumSize(labelDimensions);
            earnedAssignmentWeightSpacer.setMinimumSize(labelDimensions);
            assignmentPanel.add(earnedAssignmentWeightSpacer, h);

            JLabel gradeError = new JLabel("Please enter grade as a float.");
            gradeError.setPreferredSize(labelDimensions);
            gradeError.setForeground(Color.RED);
            gradeError.setVisible(false);

            h.gridx = 4;
            JLabel goalGradeLabel;
            if (goalGrade == 0.0f) {
                goalGradeLabel = new JLabel(" ");
            } else {
                goalGradeLabel = new JLabel(new DecimalFormat("#.#").format(goalGrade));
            }
            goalGradeLabel.setPreferredSize(labelDimensions);
            goalGradeLabel.setMaximumSize(labelDimensions);
            goalGradeLabel.setMinimumSize(labelDimensions);
            assignmentPanel.add(goalGradeLabel, h);

            h.gridx = 5;
            JTextField assignmentGradeTextField = new JTextField();
            assignmentGradeTextField.setPreferredSize(labelDimensions);
            assignmentGradeTextField.setMaximumSize(labelDimensions);
            assignmentGradeTextField.setMinimumSize(labelDimensions);
            assignmentGradeTextField.addFocusListener(new FocusListener() {
                @Override
                public void focusGained(FocusEvent e) {
                    assignmentGradeTextField.setText("");
                    gradeError.setVisible(false);
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (assignmentGradeTextField.getText().length() == 0) {
                        assignmentGradeTextField.setText("Enter grade %");

                        gradeError.setVisible(true);
                    } else {
                        try {
                            Float.parseFloat(assignmentGradeTextField.getText());
                        } catch (NumberFormatException ex) {
                            gradeError.setVisible(true);
                        }
                    }
                }
            });
            assignmentPanel.add(assignmentGradeTextField, h);

            h.gridx = 6;
            h.fill = GridBagConstraints.NONE;
            JButton submitButton = new JButton("Submit");
            submitButton.setPreferredSize(labelDimensions);
            submitButton.setMinimumSize(labelDimensions);
            submitButton.setMaximumSize(labelDimensions);
            submitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        gradeError.setVisible(false);

                        float gradeVal = Float.parseFloat(assignmentGradeTextField.getText());
                        Assignment.updateAssignment(assignmentId, gradeVal);
                        Class.setClassGrade(user.getUserId(), classId);

                        assignmentPanel.remove(earnedAssignmentWeightSpacer);
                        assignmentPanel.remove(assignmentGradeTextField);
                        assignmentPanel.remove(submitButton);

                        h.gridx = 3;
                        h.gridy = 0;
                        h.gridwidth = 1;
                        h.fill = GridBagConstraints.HORIZONTAL;
                        float earnedAssignmentWeight = assignmentWeight * (gradeVal / 100);
                        JLabel earnedAssignmentWeightLabel = new JLabel(new DecimalFormat("#.#").format(earnedAssignmentWeight));
                        earnedAssignmentWeightLabel.setPreferredSize(labelDimensions);
                        earnedAssignmentWeightLabel.setMaximumSize(labelDimensions);
                        earnedAssignmentWeightLabel.setMinimumSize(labelDimensions);
                        assignmentPanel.add(earnedAssignmentWeightLabel, h);

                        h.gridx = 4;
                        JLabel goalGradeLabel;
                        goalGradeLabel = new JLabel(" ");
                        goalGradeLabel.setPreferredSize(labelDimensions);
                        goalGradeLabel.setMaximumSize(labelDimensions);
                        goalGradeLabel.setMinimumSize(labelDimensions);
                        assignmentPanel.add(goalGradeLabel, h);

                        h.gridx = 5;
                        JLabel gradeEarnedLabel = new JLabel(new DecimalFormat("#.#").format(gradeVal));
                        gradeEarnedLabel.setPreferredSize(labelDimensions);
                        gradeEarnedLabel.setMaximumSize(labelDimensions);
                        gradeEarnedLabel.setMinimumSize(labelDimensions);
                        assignmentPanel.add(gradeEarnedLabel, h);

                        h.gridx = 6;
                        JLabel spacer = new JLabel(" ");
                        spacer.setPreferredSize(labelDimensions);
                        spacer.setMaximumSize(labelDimensions);
                        spacer.setMinimumSize(labelDimensions);
                        assignmentPanel.add(spacer, h);

                       // assignmentPanel.revalidate();
                       // assignmentPanel.repaint();

                        int selectedTabIndex = tabbedPane.getSelectedIndex();
                        String className = tabbedPane.getTitleAt(selectedTabIndex);
                        tabbedPane.setComponentAt(selectedTabIndex, null);
                        tabbedPane.setComponentAt(selectedTabIndex, createCurrentClassTab(className, Class.getClassId(className, user.getUserId()),bglistener.getTotalWeight()));

                        drawCurrentClassesPanel(user);
                        drawCompletedClassesPanel(user);
                    } catch (NumberFormatException ex) {
                        gradeError.setVisible(true);
                    } catch (Exception ex) {
                        gradeError.setText("Cannot connect to the database. Try again later.");
                        gradeError.setVisible(true);
                    }
                }
            });
            assignmentPanel.add(submitButton, h);

            h.gridy = 1;
            h.gridx = 4;
            h.gridwidth = 2;
            assignmentPanel.add(gradeError, h);
        }

        return assignmentPanel;
    }

    private static JComponent createCurrentClassTab(String classNameVal, int classIdVal, float goalGradeVal) {
        Dimension assignmentLabelDimesions = new Dimension(157, 25);
        JComponent parentPanel = new JPanel(new BorderLayout());
        JComponent classPanel = new JPanel(new GridBagLayout());
        GridBagConstraints classPanelConstraints = new GridBagConstraints();
        classPanelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        classPanelConstraints.fill = GridBagConstraints.HORIZONTAL;
        classPanelConstraints.weightx = 1;

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 150, 0));
        JPanel statusPanel = new JPanel(new GridLayout(0, 1));
        String className = classNameVal;
        JLabel classLabel = new JLabel(className, SwingConstants.LEADING);
        classLabel.setFont(f1);
        statusPanel.add(classLabel);

        String currentGrade = Class.getClassGrade(user.getUserId(), classIdVal);
        JLabel currentGradeLabel;
        if (currentGrade != null) {
            currentGradeLabel = new JLabel("Current Grade: " + currentGrade, SwingConstants.LEADING);
        } else {
            currentGradeLabel = new JLabel("Current Grade: ", SwingConstants.LEADING);
        }
        currentGradeLabel.setFont(f2);
        statusPanel.add(currentGradeLabel);
        headerPanel.add(statusPanel);

        JPanel goalGradePanel = new JPanel(new GridLayout(5, 1));
        JRadioButton A = new JRadioButton("A (90-100)");
        A.setActionCommand("A");
        A.addActionListener(bglistener);
        JRadioButton B = new JRadioButton("B (80-89)");
        B.setActionCommand("B");
        B.addActionListener(bglistener);
        JRadioButton C = new JRadioButton("C (70-79)");
        C.setActionCommand("C");
        C.addActionListener(bglistener);
        JRadioButton D = new JRadioButton("D (60-69)");
        D.setActionCommand("D");
        D.addActionListener(bglistener);
        ButtonGroup goalGrade = new ButtonGroup();
        goalGrade.add(A);
        goalGrade.add(B);
        goalGrade.add(C);
        goalGrade.add(D);
        goalGradePanel.add(new JLabel("Goal Grade     ", SwingConstants.RIGHT));
        goalGradePanel.add(A);
        goalGradePanel.add(B);
        goalGradePanel.add(C);
        goalGradePanel.add(D);

        if (goalGradeVal != 0.0f) {
            if (goalGradeVal == 90.0f) {
                A.setSelected(true);
            } else if (goalGradeVal == 80.0f) {
                B.setSelected(true);
            } else if (goalGradeVal == 70.0f) {
                C.setSelected(true);
            } else if (goalGradeVal == 60.0f) {
                D.setSelected(true);
            }
        }

        headerPanel.add(goalGradePanel);
        classPanelConstraints.gridx = 0;
        classPanelConstraints.gridy = 0;
        classPanel.add(headerPanel, classPanelConstraints);

        JPanel assignLabelPanel = new JPanel(new GridBagLayout());
        GridBagConstraints h = new GridBagConstraints();
        h.weightx = 1;
        h.weighty = 1;
        h.gridx = 0;
        h.gridy = 0;
        h.gridwidth = 1;
        h.insets = new Insets(10, 10, 0, 10);
        h.fill = GridBagConstraints.HORIZONTAL;
        h.anchor = GridBagConstraints.NORTHWEST;
        JLabel headline = new JLabel("Assignments");
        headline.setFont(f2);
        assignLabelPanel.add(headline, h);

        h.gridx = 0;
        h.gridy = 1;
        JLabel dueDateLabel = new JLabel("Due Date");
        dueDateLabel.setPreferredSize(assignmentLabelDimesions);
        dueDateLabel.setMaximumSize(assignmentLabelDimesions);
        dueDateLabel.setMinimumSize(assignmentLabelDimesions);
        dueDateLabel.setFont(tableHeadFont);
        assignLabelPanel.add(dueDateLabel, h);

        h.gridx = 1;
        JLabel assignmentName = new JLabel("Name");
        assignmentName.setPreferredSize(assignmentLabelDimesions);
        assignmentName.setMaximumSize(assignmentLabelDimesions);
        assignmentName.setMinimumSize(assignmentLabelDimesions);
        assignmentName.setFont(tableHeadFont);
        assignLabelPanel.add(assignmentName, h);

        h.gridx = 2;
        JLabel assignmentWeight = new JLabel("Weight");
        assignmentWeight.setPreferredSize(assignmentLabelDimesions);
        assignmentWeight.setMaximumSize(assignmentLabelDimesions);
        assignmentWeight.setMinimumSize(assignmentLabelDimesions);
        assignmentWeight.setFont(tableHeadFont);
        assignLabelPanel.add(assignmentWeight, h);

        h.gridx = 3;
        JLabel earnedWeight = new JLabel("Earned Weight");
        earnedWeight.setPreferredSize(assignmentLabelDimesions);
        earnedWeight.setMaximumSize(assignmentLabelDimesions);
        earnedWeight.setMinimumSize(assignmentLabelDimesions);
        earnedWeight.setFont(tableHeadFont);
        assignLabelPanel.add(earnedWeight, h);

        //***********

        h.gridx = 4;
        JLabel baselineGrade = new JLabel("Goal Grade");
        baselineGrade.setPreferredSize(assignmentLabelDimesions);
        baselineGrade.setMaximumSize(assignmentLabelDimesions);
        baselineGrade.setMinimumSize(assignmentLabelDimesions);
        baselineGrade.setFont(tableHeadFont);
        assignLabelPanel.add(baselineGrade, h);

        h.gridx = 5;
        JLabel grade = new JLabel("Grade");
        grade.setPreferredSize(assignmentLabelDimesions);
        grade.setMaximumSize(assignmentLabelDimesions);
        grade.setMinimumSize(assignmentLabelDimesions);
        grade.setFont(tableHeadFont);
        assignLabelPanel.add(grade, h);

        h.gridx = 6;
        JLabel headerSpacer = new JLabel(" ");
        headerSpacer.setPreferredSize(assignmentLabelDimesions);
        headerSpacer.setMaximumSize(assignmentLabelDimesions);
        headerSpacer.setMinimumSize(assignmentLabelDimesions);
        assignLabelPanel.add(headerSpacer, h);

        classPanelConstraints.gridy = 1;
        classPanel.add(assignLabelPanel, classPanelConstraints);

        try {
            ArrayList<Assignment> assignmentsForClass = Assignment.getAssignmentsForClass(user.getUserId(), classIdVal);

            if (assignmentsForClass.size() > sizeLimit) {
                throw new ArithmeticException("List of assignments is too large.");
            }

            if (assignmentsForClass.size() != 0) {
                JPanel assignmentsPanel = new JPanel(new GridLayout(assignmentsForClass.size(), 1, 0, 0));
                GridBagConstraints aConstraints = new GridBagConstraints();
                aConstraints.anchor = GridBagConstraints.NORTHWEST;
                aConstraints.gridx = 0;
                aConstraints.weighty = 1;

                for (int j = 0; j < assignmentsForClass.size(); j++) {
                    aConstraints.gridy = j;

                    if (goalGradeVal == 0.0f) {
                        assignmentsPanel.add(classAssignmentPanel(classIdVal, assignmentsForClass.get(j).getAssignmentId(), assignmentsForClass.get(j).getAssignmentDueDate(), assignmentsForClass.get(j).getAssignmentName(), assignmentsForClass.get(j).getAssignmentWeight(), assignmentsForClass.get(j).getAssignmentGrade(), 0.0f), aConstraints);
                    } else {
                        assignmentsPanel.add(classAssignmentPanel(classIdVal, assignmentsForClass.get(j).getAssignmentId(), assignmentsForClass.get(j).getAssignmentDueDate(), assignmentsForClass.get(j).getAssignmentName(), assignmentsForClass.get(j).getAssignmentWeight(), assignmentsForClass.get(j).getAssignmentGrade(), calculateBaselineGrade(assignmentsForClass.get(j).getAssignmentWeight(), user.getUserId(), classIdVal)), aConstraints);
                    }
                }

                classPanelConstraints.gridy = 2;
                classPanel.add(assignmentsPanel, classPanelConstraints);

                classPanelConstraints.gridy = 3;
                classPanelConstraints.insets = new Insets(10,0,0, 0);
                classPanelConstraints.fill = GridBagConstraints.WEST;
                JButton delete = new JButton("Delete Class");
                delete.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int a = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this class?");
                        if(a == JOptionPane.YES_OPTION) {
                            try {
                                // Delete the class and its assignments from the database
                                Class.deleteClass(user.getUserId(), classIdVal);

                                // Remove the class from current classes panel and tabs
                                tabbedPane.remove(tabbedPane.getSelectedIndex());
                                drawCurrentClassesPanel(user);
                            } catch (Exception ex) {
                                System.out.println("Can't delete class.");
                            }
                        }
                    }
                });
                classPanel.add(delete, classPanelConstraints);
            }

            parentPanel.add(classPanel, BorderLayout.NORTH);
        } catch (Exception ex) {
            System.out.println("Unable to connect to the database.");
        }

        return parentPanel;
    }

    private static class BGlistener implements ActionListener {
        float totalWeight;

        public void actionPerformed(ActionEvent e) {
            if ("A".equals(e.getActionCommand())) {
                totalWeight = 90.0f;
            } else if ("B".equals(e.getActionCommand())) {
                totalWeight = 80.0f;
            } else if ("C".equals(e.getActionCommand())) {
                totalWeight = 70.0f;
            } else if ("D".equals(e.getActionCommand())) {
                totalWeight = 60.0f;
            }

            // Get current tab index
            int currentTabIndex = tabbedPane.getSelectedIndex();
            String className = tabbedPane.getTitleAt(currentTabIndex);
            Class classX = new Class();
            classX.setClassName(className);
            classX.setUserId(user.getUserId());
            int classId = classX.getClassId();

            // Update the tab to show the goal grade
            tabbedPane.setComponentAt(currentTabIndex, null);
            tabbedPane.setComponentAt(currentTabIndex, createCurrentClassTab(className, classId, totalWeight));
        }
        public float getTotalWeight() {
            return totalWeight;
        }
    }

    private static float calculateBaselineGrade(float assignmentWeight, int userId, int classId) {
        float baselineGrade = 0.0f;

        float goalGrade = bglistener.getTotalWeight();
        float totalWeight = 0.0f;
        float totalEarnedWeight = 0.0f;
        float weightPassed = 0.0f;

        try {
            ArrayList<Assignment> assignments = Assignment.getAssignmentsForClass(userId, classId);

            if (assignments.size() > sizeLimit) {
                throw new ArithmeticException("List of assignments is too large");
            }

            for (int i = 0; i < assignments.size(); i++) {
                totalWeight += assignments.get(i).getAssignmentWeight();

                if (assignments.get(i).getAssignmentGrade() != 0.0) {
                    totalEarnedWeight += (assignments.get(i).getAssignmentWeight() * (assignments.get(i).getAssignmentGrade() / 100));
                    weightPassed += assignments.get(i).getAssignmentWeight();
                }
            }

            float goalWeight = goalGrade / totalWeight;
            float completedWeight = totalEarnedWeight / totalWeight;
            goalWeight -= completedWeight;
            goalWeight *= totalWeight;
            float weightLeftOver = totalWeight - weightPassed;
            float weightValue = goalWeight / weightLeftOver;
            float baseLineWeight = weightValue * assignmentWeight;
            baselineGrade = (baseLineWeight * 100) / assignmentWeight;


            return baselineGrade;
        } catch (Exception ex) {
            System.out.println("Couldn't get assignments for this user and class.");
        }

        return baselineGrade;
    }
}

