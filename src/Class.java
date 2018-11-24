import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

class Class {
    private int classId;
    private int userId;
    private String className;
    private int numberOfAssignments;
    private Date classStartDate;
    private Date classEndDate;
    private float classGrade;

    // Default constructor
    private Class() {

    }

    // Create a class
    public Class(int userId, String className, int numberOfAssignments, Date classStartDate, Date classEndDate) {
        this.userId = userId;
        this.className = className;
        this.numberOfAssignments = numberOfAssignments;
        this.classStartDate = classStartDate;
        this.classEndDate = classEndDate;
    }

    // Getters
    public Integer getClassId() {
        if (this.classId != 0) {
            return this.classId;
        } else {
            try {
                return getClassId(this.className, this.userId);
            } catch (Exception ex) {
                // TODO: Handle exception if you can't get the class ID
            }
        }

        return null;
    }

    public int getUserId() {
        return this.userId;
    }

    public String getClassName() {
        return this.className;
    }

    public int getNumberOfAssignments() {
        return this.numberOfAssignments;
    }

    public Date getClassStartDate() {
        return this.classStartDate;
    }

    public Date getClassEndDate() {
        return this.classEndDate;
    }

    public float getClassGrade() {
        return this.classGrade;
    }

    // Setters
    private void setClassId(int classId) {
        this.classId = classId;
    }

    private void setUserId(int userId) {
        this.userId = userId;
    }

    private void setClassName(String className) {
        this.className = className;
    }

    private void setNumberOfAssignments(int numberOfAssignments) {
        this.numberOfAssignments = numberOfAssignments;
    }

    private void setClassStartDate(Date classStartDate) {
        this.classStartDate = classStartDate;
    }

    private void setClassEndDate(Date classEndDate) {
        this.classEndDate = classEndDate;
    }

    public void setClassGrade(float classGrade) {
        this.classGrade = classGrade;
    }

    public String convertToLetterGrade() {
        if (this.classGrade >= 90.0 && this.classGrade <= 100.0) {
            return "A";
        } else if (this.classGrade >= 80.0 && this.classGrade <= 89.0) {
            return "B";
        } else if (this.classGrade >= 70.0 && this.classGrade <= 79.0) {
            return "C";
        } else if (this.classGrade >= 60.0 && this.classGrade <= 69.0) {
            return "D";
        } else {
            return "F";
        }
    }

    // Methods needed to retrieve class data from the database
    private static Class getClassFromResultSet(ResultSet rs) throws SQLException {
        Class classX = new Class();
        classX.setClassId(rs.getInt("classId"));
        classX.setUserId(rs.getInt("userId"));
        classX.setClassName(rs.getString("className"));
        classX.setNumberOfAssignments(rs.getInt("numberOfAssignments"));
        classX.setClassStartDate(rs.getDate("classStartDate"));
        classX.setClassEndDate(rs.getDate("classEndDate"));
        return classX;
    }

    public static boolean addClass(Class classX) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            PreparedStatement ps = dbCon.prepareStatement("INSERT INTO Classes (userId, className, numberOfAssignments, classStartDate, classEndDate, classGrade) VALUES (?,?,?,?,?,NULL)");
            ps.setInt(1, classX.userId);
            ps.setString(2, classX.className);
            ps.setInt(3, classX.numberOfAssignments);
            ps.setDate(4, new java.sql.Date(classX.classStartDate.getTime()));
            ps.setDate(5, new java.sql.Date(classX.classEndDate.getTime()));
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

    public static ArrayList<Class> getCurrentClasses(int userId) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            Statement stmt = dbCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Classes WHERE classEndDate >= GETDATE() AND userId=" + userId);

            ArrayList<Class> classes = new ArrayList<Class>();

            while (rs.next()) {
                Class classX = getClassFromResultSet(rs);
                classes.add(classX);
            }

            return classes;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }

        return null;
    }

    public static ArrayList<Class> getCompletedClasses(int userId) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            Statement stmt = dbCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Classes WHERE classEndDate < GETDATE() AND userId=" + userId);

            ArrayList<Class> classes = new ArrayList<Class>();

            while (rs.next()) {
                Class classX = getClassFromResultSet(rs);
                classes.add(classX);
            }

            return classes;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }

        return null;
    }

    private static Integer getClassId(String className, int userId) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            Statement stmt = dbCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT classId FROM Classes WHERE className='" + className + "' AND userId=" + userId);

            while (rs.next()) {
                return rs.getInt("classId");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }

        return null;
    }
}
