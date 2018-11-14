import java.sql.*;

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

    public static Integer getToken(int userId) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            Statement stmt = dbCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT resetToken FROM Users WHERE userId=" + userId);

            if (rs.next()) {
                return rs.getInt("resetToken");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }

        return null;
    }

    // Deletes the reset token after the user resets his/her password
    public static void deleteToken(int userId) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            PreparedStatement ps = dbCon.prepareStatement("UPDATE Users SET resetToken=NULL WHERE userId=?");
            ps.setInt(1, userId);
            int i = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }
    }

    // When the user asks to reset their password or get their username (i.e., enter email address step)
    public static boolean forgotPasswordOrUsername(String emailAddress) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            Statement stmt = dbCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Users WHERE emailAddress='" + emailAddress + "'");

            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }

        return false;
    }

    // When the user actually updates his/her password
    public static boolean resetPassword(int userId, String password) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            PreparedStatement ps = dbCon.prepareStatement("UPDATE Users SET password=? WHERE userId=?");
            ps.setString(1, password);
            ps.setInt(2, userId);
            int i = ps.executeUpdate();

            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }
        return false;
    }
}
