import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class User {
    private int userId;
    private String emailAddress;
    private String username;
    private String password;
    private int resetToken;

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

    public String getPassword() {
        return this.password;
    }

    // Setters
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setResetToken(int resetToken) {
        this.resetToken = resetToken;
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
            Statement stmt = dbCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username='" + username + "' AND password='" + password + "'");

            if (rs.next()) {
                User user = getUserFromResultSet(rs);
                return user;
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
            Statement stmt = dbCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE emailAddress='" + emailAddress + "'");

            if (rs.next()) {
                User user = getUserFromResultSet(rs);
                return user;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }

        return null;
    }

    public static Set<User> getAllUsers() throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            Statement stmt = dbCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            Set users = new HashSet();

            while (rs.next()) {
                User user = getUserFromResultSet(rs);
                users.add(user);
            }

            return users;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }

        return null;
    }

    // ONLY NEEDED ON LOGIN
//    public User getUserByUsernameAndPassword(String username, String password) throws Exception {
//        Connection dbCon = new DatabaseConnection().getConnection();
//        try {
//            Statement stmt = dbCon.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE username='" + username + "'AND password='" + password + "'");
//
//            if (rs.next()) {
//                User user = getUserFromResultSet(rs);
//                return user;
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        } finally {
//            dbCon.close();
//        }
//
//        return null;
//    }
    // ONLY NEEDED ON LOGIN
//    public boolean insertUser() {
//
//    }
//    public boolean updateUser(int userId) throws Exception {
//        Connection dbCon = new DatabaseConnection().getConnection();
//        try {
//            PreparedStatement ps = dbCon.prepareStatement("UPDATE user SET");
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        } finally {
//            dbCon.close();
//        }
//
//        return false;
//    }

    // Get user from email address
//    public User getUser(String emailAddress) {
//        return User;
//    }

    // Get username
//    public String getUsername(String emailAddress) {
//        return username;
//    }
}
