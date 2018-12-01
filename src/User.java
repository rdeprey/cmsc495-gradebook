/*********************************************************************************************************
 * File name: User.java
 * Date: November 2018
 * Author: Haemee Nabors, Rebecca Deprey, Devon Artist, Harry Giles, Brittany White, Ryan Haas
 * Purpose: This class serves as a data transfer object for the User table in the Microsoft SQL
 * database. It maps each field in the database table to private fields for the User objects used
 * in the program. It also has methods that get, set, and update data in the database based on changes in
 * the application GUI as well as getter and setter methods used throughout the application.
 *
 * References:
 * 1. Java Tutorials Code Sample – getCodeSampleUrl(); document.write(codeSampleName);. (n.d.). Retrieved
 * from https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial
 * /uiswing/examples/components/TabbedPaneDemoProject/src/components/TabbedPaneDemo.java
 *********************************************************************************************************/

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

class User {
    private int userId;
    private String emailAddress;
    private String username;
    private String password;

    // Default constructor
    public User() {}

    // Create a user
    public User(String emailAddress, String username, String password) {
        this.emailAddress = emailAddress;
        this.username = username;
        this.password = password;
    }

    // Getters
    public int getUserId() {
        return this.userId;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public String getUsername() {
        return this.username;
    }

    // Setters
    private void setUserId(int userId) {
        this.userId = userId;
    }

    private void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    // Methods needed to retrieve user data from the database
    private static User getUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("userId"));
        user.setEmailAddress(rs.getString("emailAddress"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        return user;
    }

    public static User getUser(String username, String password) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            PreparedStatement ps = dbCon.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return getUserFromResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }

        return null;
    }

    public static User getUser(String emailAddress) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            PreparedStatement ps = dbCon.prepareStatement("SELECT * FROM users WHERE emailAddress=?");
            ps.setString(1, emailAddress);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return getUserFromResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }

        return null;
    }
}
