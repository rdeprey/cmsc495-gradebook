/*********************************************************************************************************
 * File name: Class.java
 * Date: November/December 2018
 * Author: Haemee Nabors, Rebecca Deprey, Devon Artist, Harry Giles, Brittany White, Ryan Haas
 * Purpose: This class serves as a data transfer object for the Class table in the Microsoft SQL
 * database. It maps each field in the database table to private fields for the Class objects used
 * in the program. It also has methods that get, set, and update data in the database based on changes in
 * the application GUI as well as getter and setter methods used throughout the application.
 *
 * References:
 * 1. Java Tutorials Code Sample – getCodeSampleUrl(); document.write(codeSampleName);. (n.d.). Retrieved
 * from https://docs.oracle.com/javase/tutorial/displayCode.html?code=https://docs.oracle.com/javase/tutorial
 * /uiswing/examples/components/TabbedPaneDemoProject/src/components/TabbedPaneDemo.java
 *********************************************************************************************************/

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

final class Class {
    private int classId;
    private int userId;
    private String className;
    private int numberOfAssignments;
    private Date classStartDate;
    private Date classEndDate;
    private float classGrade;


    // Default constructor
    public Class() {

    }

    // Creates a class
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
                System.out.println("Can't get classId.");
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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setClassName(String className) {
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
        return convertToLetterGrade(this.classGrade);
    }

    // Converts float value grade to letter grade
    public static String convertToLetterGrade(float grade) {
        if (grade >= 90.0) {
            return "A";
        } else if (grade >= 80.0 && grade <= 89.0) {
            return "B";
        } else if (grade >= 70.0 && grade <= 79.0) {
            return "C";
        } else if (grade >= 60.0 && grade <= 69.0) {
            return "D";
        } else {
            return "F";
        }
    }

    // Gets the letter grade for a class
    public static String getClassGrade(int userId, int classId) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            PreparedStatement ps = dbCon.prepareStatement("SELECT classGrade FROM Classes WHERE userId=? AND classId=? AND classGrade IS NOT NULL");
            ps.setInt(1, userId);
            ps.setInt(2, classId);
            ResultSet rs = ps.executeQuery();

            String grade = null;

            while (rs.next()) {
                grade = convertToLetterGrade(rs.getFloat("classGrade"));
            }

            return grade;
        } catch (SQLException ex) {
            System.out.println("Couldn't get assignments.");
        }

        return null;
    }

    // Creates a Class object from the response returned by the database
    private static Class getClassFromResultSet(ResultSet rs) throws SQLException {
        Class classX = new Class();
        classX.setClassId(rs.getInt("classId"));
        classX.setUserId(rs.getInt("userId"));
        classX.setClassName(rs.getString("className"));
        classX.setNumberOfAssignments(rs.getInt("numberOfAssignments"));
        classX.setClassStartDate(rs.getDate("classStartDate"));
        classX.setClassEndDate(rs.getDate("classEndDate"));
        classX.setClassGrade(rs.getFloat("classGrade"));
        return classX;
    }

    // Adds a class to the database
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

    // Gets classes that don't have any assignments associated with them
    public static ArrayList<Integer> getClassesWithoutAssignments(int userId) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            PreparedStatement ps = dbCon.prepareStatement("SELECT classId FROM Classes WHERE userId=? AND NOT EXISTS(SELECT * FROM Assignments WHERE Assignments.classId = Classes.classId)");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            ArrayList<Integer> classIds = new ArrayList<>();

            while (rs.next()) {
                int id = rs.getInt("classId");
                classIds.add(id);
            }

            return classIds;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }

        return null;
    }

    // Gets all classes with an end date greater than or equal to the current date
    public static ArrayList<Class> getCurrentClasses(int userId) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            PreparedStatement ps = dbCon.prepareStatement("SELECT * FROM Classes WHERE classEndDate >= GETDATE() AND userId=?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

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

    // Gets all classes with an end date before the current date
    public static ArrayList<Class> getCompletedClasses(int userId) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            PreparedStatement ps = dbCon.prepareStatement("SELECT * FROM Classes WHERE classEndDate < GETDATE() AND userId=?");
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

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

    // Gets the ID for a class from the database
    public static Integer getClassId(String className, int userId) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            PreparedStatement ps = dbCon.prepareStatement("SELECT classId FROM Classes WHERE className=? AND userId=?");
            ps.setString(1, className);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();

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

    // Deletes a class and its related assignments from the database
    public static boolean deleteClass(int userId, int classId) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            // Delete the assignments for a class
            Assignment.deleteAssignmentsForClass(userId, classId);

            // Delete the class
            PreparedStatement ps = dbCon.prepareStatement("DELETE FROM Classes WHERE userId=? AND classId=?");
            ps.setInt(1, userId);
            ps.setInt(2, classId);
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

    // Sets the grade for a class in the database
    public static boolean setClassGrade(int userId, int classId) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            // Get grades for all class assignments
            ArrayList<Assignment> assignments = Assignment.getAssignmentsForClass(userId, classId);
            float completedAssignmentWeight = 0;
            float currentGrade = 0;

            if (assignments != null && assignments.size() > 100) {
                throw new ArithmeticException("List of assignments is too large");
            }

            for (int j = 0; j < assignments.size(); j++) {
                if (assignments.get(j).getAssignmentGrade() != 0.0) {
                    float currentWeight = assignments.get(j).getAssignmentWeight();
                    completedAssignmentWeight += currentWeight;
                    currentGrade += (currentWeight * (assignments.get(j).getAssignmentGrade() / 100));
                }
            }

            PreparedStatement ps = dbCon.prepareStatement("UPDATE Classes SET classGrade=? WHERE userId=? AND classId=?");
            ps.setFloat(1,(currentGrade / completedAssignmentWeight) * 100);
            ps.setInt(2, userId);
            ps.setInt(3, classId);

            int rs = ps.executeUpdate();
            if (rs == 1) {
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
