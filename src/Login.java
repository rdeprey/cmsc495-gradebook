import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Login extends Application {

    private Object httpSession;
    private User currentUser;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("GradeBook Login");
        // Grid Pane divides your window into grids
        final GridPane grid = new GridPane();
        // Align to Center
        // Note Position is geometric object for alignment
        grid.setAlignment(Pos.CENTER);
        // Set gap between the components
        // Larger numbers mean bigger spaces
        grid.setHgap(10);
        grid.setVgap(10);

        // Create some text to place in the scene
        Text scenetitle = new Text("Please, login to continue.");
        // Add text to grid 0,0 span 2 columns, 1 row
        grid.add(scenetitle, 0, 0, 2, 1);

        // Create Label
        Label userName = new Label("User Name:");
        // Add label to grid 0,1
        grid.add(userName, 0, 1);

        // Create Textfield
        final TextField userTextField = new TextField();
        // Add textfield to grid 1,1
        grid.add(userTextField, 1, 1);

        // Create Label
        Label pw = new Label("Password:");
        // Add label to grid 0,2
        grid.add(pw, 0, 2);

        // Create Passwordfield
        final PasswordField pwBox = new PasswordField();
        // Add Password field to grid 1,2
        grid.add(pwBox, 1, 2);

        // Create Login Button
        Button btn = new Button("Login");
        // Add button to grid 1,4
        grid.add(btn, 1, 4);

        // Create Reset Button
        Button reset = new Button("Reset");
        // Add button to grid 2,4
        grid.add(reset, 0, 4);


        // Create Forgot Button for Username/Password
        CheckBox forgotButton = new CheckBox("Forgot Username/Password");
        grid.add(forgotButton, 0,5,2,2);
        forgotButton.setSelected(false);

        // Create forgot button action
        forgotButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Update the view to panel that asks user for their email address
                GridPane grid6 = new GridPane();
                // Align to Center
                // Note Position is geometric object for alignment
                grid6.setAlignment(Pos.CENTER);
                // Set gap between the components
                // Larger numbers mean bigger spaces
                grid6.setHgap(10);
                grid6.setVgap(10);
                Text enterEmailTitle = new Text("Please enter the email address associated with your account.");
                grid6.add(enterEmailTitle, 0, 0, 2, 1);
                Label emailAddressLbl = new Label("Email Address: ");
                grid6.add(emailAddressLbl, 0, 1);
                TextField emailAddressTxt = new TextField();
                grid6.add(emailAddressTxt, 0, 2);
                Button submitEmailBtn = new Button("Submit");

                // Check the database for the user's email address when the submit button is clicked
                submitEmailBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        try {
                            // Check if email exists in database
                            User user = User.getUser(emailAddressTxt.getText());

                            // If the email does exist, send an email to the user and prompt him/her to enter the security
                            // token from the email
                            if (user != null) {
                                javamail(user);
                                grid.setVisible(false);
                                GridPane grid3 = new GridPane();
                                // Align to Center
                                // Note Position is geometric object for alignment
                                grid3.setAlignment(Pos.CENTER);
                                // Set gap between the components
                                // Larger numbers mean bigger spaces
                                grid3.setHgap(10);
                                grid3.setVgap(10);
                                Text scenetitle = new Text("Your username is " + user.getUsername() + ".\n\nPlease check your email " + userTextField.getText()
                                        + "\nand input your given security code to continue.");
                                // Add text to grid 0,0 span 2 columns, 1 row
                                grid3.add(scenetitle, 0, 0, 2, 1);
                                // Create Label for security Code
                                Label securecode = new Label("Security Code: ");
                                // Add label to grid 0,1
                                grid3.add(securecode, 0, 1);
                                // Create Textfield for code input
                                TextField emailcode = new TextField();
                                // Add textfield to grid 1,1
                                grid3.add(emailcode, 0, 2);
                                // Create Login Button
                                Button secbtn = new Button("Confirm");
                                // Add button to grid 1,4
                                grid3.add(secbtn, 0, 4, 2, 1);
                                Scene scene = new Scene(grid3, 500, 400);
                                primaryStage.setScene(scene);
                                primaryStage.show();

                                secbtn.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent e) {
                                        try {
                                            secure();
                                        } catch (Exception ex) {
                                            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        boolean authen = confirmation(userTextField.getText(), emailcode.getText());
                                        if(authen == false){
                                            grid.setVisible(false);
                                            GridPane grid4 = new GridPane();
                                            // Align to Center
                                            // Note Position is geometric object for alignment
                                            grid4.setAlignment(Pos.CENTER);
                                            // Set gap between the components
                                            // Larger numbers mean bigger spaces
                                            grid4.setHgap(10);
                                            grid4.setVgap(10);
                                            Text scenetitle = new Text("ACCESS DENIED!\n" +
                                                    "You've failed to enter the correct security code.\n" +
                                                    "Your information will be reviewed by our system's personnel.\n" +
                                                    " If you think an error has been made please contact our\n" +
                                                    "Help Desk immediately.");
                                            scenetitle.setFill(Color.FIREBRICK);
                                            // Add text to grid 0,0 span 2 columns, 1 row
                                            grid4.add(scenetitle, 0, 0, 2, 2);
                                            Scene scene = new Scene(grid4, 500, 400);
                                            primaryStage.setScene(scene);
                                            primaryStage.show();
                                        }
                                        else {
                                            grid.setVisible(false);
                                            GridPane grid5 = new GridPane();
                                            // Align to Center
                                            // Note Position is geometric object for alignment
                                            grid5.setAlignment(Pos.CENTER);
                                            // Set gap between the components
                                            // Larger numbers mean bigger spaces
                                            grid5.setHgap(10);
                                            grid5.setVgap(10);
                                            Text scenetitle = new Text("Welcome " + userTextField.getText() + "!\n"
                                                    + "Thank you for your identity confirmation!");
                                            // Add text to grid 0,0 span 2 columns, 1 row
                                            grid5.add(scenetitle, 0, 0, 2, 1);
                                            // Add a button to open the Gradebook
                                            Button openGradebookBtn = new Button("Open Gradebook");

                                            // Add a button handler for the click event
                                            openGradebookBtn.setOnAction(new EventHandler<ActionEvent>() {
                                                @Override
                                                public void handle(ActionEvent actionEvent) {
                                                    try {
                                                        new Gradebook(user);
                                                    } catch (Exception ex) {

                                                    }
                                                }
                                            });

                                            grid5.add(openGradebookBtn, 0, 2, 2, 1);
                                            Scene scene = new Scene(grid5, 500, 400);
                                            primaryStage.setScene(scene);
                                            primaryStage.show();
                                        }
                                    }
                                });
                            } else {
                                System.out.println("Email wasn't found in the database.");
                            }
                        } catch (Exception ex) {
                            // TODO: Show error if the email can't be found?
                            System.out.println("Unable to access the database");
                            final Text actiontarget = new Text();
                            grid.add(actiontarget, 1, 10);
                            actiontarget.setFill(Color.FIREBRICK);
                            actiontarget.setText("Please try again.");
                        }
                    }
                });

                grid6.add(submitEmailBtn, 0, 4,2,1);
                Scene enterEmailScene = new Scene(grid6, 500, 400);
                primaryStage.setScene(enterEmailScene);
                primaryStage.show();
            }
        });


        //Create Warning Disclaimer
        Text warning = new Text ("Privacy Disclaimer:\n" +
                "This system is for the use of authorized students only. \n" +
                "Individuals using this system without authority, or in excess of their authority, are subject\n" +
                "to having all of their activities on this system monitored and recorded by system personnel.");
        warning.setStyle("-fx-font: 10 arial;");
        grid.add(warning, 0, 8, 3, 2);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 10);



        // Set the Action when button is clicked
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    // Authenticate the user
                    boolean isValid = authenticate(userTextField.getText(), pwBox.getText());

                    int counter = counter();
                    int maxAttempts = 2;
                    // If valid clear the grid and Welcome the user
                    if (isValid) {
                        new Gradebook(currentUser);
                    } else if (counter == maxAttempts) {
                        grid.setVisible(false);
                        GridPane grid2 = new GridPane();
                        // Align to Center
                        // Note Position is geometric object for alignment
                        grid2.setAlignment(Pos.CENTER);
                        // Set gap between the components
                        // Larger numbers mean bigger spaces
                        grid2.setHgap(10);
                        grid2.setVgap(10);
                        Text scenetitle = new Text("ACCESS DENIED!\n" +
                                "You have had mutiple failed Login Attempts.\n" +
                                "Please contact Help Desk for further assistance.");
                        scenetitle.setFill(Color.FIREBRICK);
                        // Add text to grid 0,0 span 2 columns, 1 row
                        grid2.add(scenetitle, 0, 0, 2, 2);
                        Scene scene = new Scene(grid2, 500, 400);
                        primaryStage.setScene(scene);
                        primaryStage.show();

                        // If Invalid Ask user to try again
                    } else if(counter < maxAttempts){

                        final Text actiontarget = new Text();
                        grid.add(actiontarget, 1, 10);
                        actiontarget.setFill(Color.FIREBRICK);
                        actiontarget.setText("Please try again.");
                        System.out.println(counter);
                    }
                } catch (Exception ex) {
                    System.out.println("Unable to access the database");
                }
            }
        });
        // Set the size of Scene
        Scene scene = new Scene(grid, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Set the reset button click event handler
        reset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                userTextField.setText("");
                pwBox.setText("");
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }
    String filename = "Logins.txt";
    String line;
    final boolean append = true;
    BufferedWriter writer = null;

    Date date = new Date();
    private int count = 0;
    public int attempts;

    Random rand = new Random();
    int  n = rand.nextInt(9999) + 1;
    String code = Integer.toString(n);

    //Create a function that is able to encrypt the audit records
    //encrypt each line
    public void secure() throws Exception{
        try {
            SecretKey key = KeyGenerator.getInstance("AES").generateKey();
            AESEncrypter encrypter = new AESEncrypter(key);
            BufferedReader br = new BufferedReader(new FileReader(filename));
            while((line = br.readLine()) != null) {
                encrypter.encrypt(line);

            }
        }
        catch (NoSuchAlgorithmException e) {
            System.err.println("No such algorithm exists.");
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            filename + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + filename + "'");
        }
    }
    /**
     * @param user the username entered
     * @param pword the password entered
     * @return isValid true for authenticated
     */
    public boolean authenticate(String user, String pword) throws Exception {

        boolean isValid = false;
        currentUser = User.getUser(user, pword);
        if (currentUser != null) {
            isValid = true;
            try {
                writer = new BufferedWriter(new FileWriter(filename,append));
                writer.newLine();
                writer.append("Successful Login on " + date + " in which the following username: " + user);
                writer.newLine();
                writer.append("and password: " + pword + " were entered correctly by the user to access their account.");
                writer.newLine();
                writer.append("It took the user " + (attempts + 1) + " attempts to access their account successfully.");
                writer.newLine();
            }
            // print error message if there is one
            catch (IOException io) {
                System.out.println("File IO Exception" + io.getMessage());
            }
        }
        else{
            try {
                writer = new BufferedWriter(new FileWriter(filename, append));
                writer.newLine();
                writer.append("Error: Invalid Login Credentials on " + date + " in which the");
                writer.newLine();
                writer.append("following username: " + user + " and password: " + pword + " were entered by a user");
                writer.newLine();
                writer.append("in the attempt to access an account. This is users " + (attempts + 1)
                        + " attempt to login into this system");
                writer.newLine();
                writer.append("unsuccessfully. This should be reviewed by a systems administrator for further action.");
                writer.newLine();
            }
            // print error message if there is one
            catch (IOException io) {
                System.out.println("File IO Exception" + io.getMessage());
            }
        }
        try {
            if (writer != null) {
                writer.close();
            }
        }
        //print error message if there is one
        catch (IOException io) {
            System.out.println("Issue closing the File." + io.getMessage());
        }
        return isValid;
    }

    public int counter(){
        int oneUp = count++;
        attempts = oneUp + 1;
        return oneUp;
    }


    public boolean confirmation(String user, String secureNum) {
        boolean authen = false;
        if (secureNum.equalsIgnoreCase(code)) {
            authen = true;
            try {
                writer = new BufferedWriter(new FileWriter(filename,append));
                writer.newLine();
                writer.append("Successful Email Authentication on " + date + " in which the following");
                writer.newLine();
                writer.append("username:" + user + " entered the correct security code that was emailed.");
                writer.newLine();
            }
            // print error message if there is one
            catch (IOException io) {
                System.out.println("File IO Exception" + io.getMessage());
            }
        }
        else{
            try {
                writer = new BufferedWriter(new FileWriter(filename, append));
                writer.newLine();
                writer.append("Error: Invalid Security Code Credentials on " + date + " in which the");
                writer.newLine();
                writer.append("following username: " + user + " entered the incorrect emailed security code.");
                writer.newLine();
            }

            // print error message if there is one
            catch (IOException io) {
                System.out.println("File IO Exception" + io.getMessage());
            }
        }
        try {
            if (writer != null) {
                writer.close();
            }
        }
        //print error message if there is one
        catch (IOException io) {
            System.out.println("Issue closing the File." + io.getMessage());
        }
        return authen;
    }
    public void javamail(User user){
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("cmsc495gradebook","1234test%");
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("cmsc495gradebook@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmailAddress()));
            message.setSubject("Login Security Code");
            message.setText("Dear " + user.getUsername() + "," +
                    "\n\nThank you for attempting to login to GradeBook."
                    + " To confirm your identity please now type in" +
                    " Security Code below into the application and click" +
                    " 'Confirm' button to continue." +
                    "\n\nThe Security Code is: " + "\n" + code);

            Transport.send(message);


        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}