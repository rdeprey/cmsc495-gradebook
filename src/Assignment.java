/*********************************************************************************************************
 * File name: Assignment.java
 * Date: November 2018
 * Author: Haemee Nabors, Rebecca Deprey, Devon Artist, Harry Giles, Brittany White, Ryan Haas
 * Purpose: This class serves as a data transfer object for the Assignment table in the Microsoft SQL
 * database. It maps each field in the database table to private fields for the Assignment objects used
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
import java.util.ArrayList;
import java.util.Date;

class Assignment {
    private int assignmentId;
    private int userId;
    private int classId;
    private String assignmentName;
    private Date assignmentDueDate;
    private float assignmentWeight;
    private float assignmentGrade;

    // Default constructor
    private Assignment() {

    }

    // Create an assignment
    public Assignment(int userId, int classId, String assignmentName, Date assignmentDueDate, float assignmentWeight) {
        this.userId = userId;
        this.classId = classId;
        this.assignmentName = assignmentName;
        this.assignmentDueDate = assignmentDueDate;
        this.assignmentWeight = assignmentWeight;
    }

    // Getters
    public int getAssignmentId() {
        return this.assignmentId;
    }

    public int getUserId() {
        return this.userId;
    }

    public int getClassId() {
        return this.classId;
    }

    public String getAssignmentName() {
        return this.assignmentName;
    }

    public Date getAssignmentDueDate() {
        return this.assignmentDueDate;
    }

    public float getAssignmentWeight() {
        return this.assignmentWeight;
    }

    public float getAssignmentGrade() {
        return this.assignmentGrade;
    }

    // Setters
    private void setAssignmentId(int assignmentId) { this.assignmentId = assignmentId; }
    private void setUserId(int userId) {
        this.userId = userId;
    }

    private void setClassId(int classId) {
        this.classId = classId;
    }

    private void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    private void setAssignmentDueDate(Date assignmentDueDate) {
        this.assignmentDueDate = assignmentDueDate;
    }

    private void setAssignmentWeight(float assignmentWeight) {
        this.assignmentWeight = assignmentWeight;
    }

    private void setAssignmentGrade(float assignmentGrade) {
        this.assignmentGrade = assignmentGrade;
    }

    // Methods needed to retrieve assignment data from the database
    private static Assignment getAssignmentFromResultSet(ResultSet rs) throws SQLException {
        Assignment assignment = new Assignment();
        assignment.setAssignmentId(rs.getInt("assignmentId"));
        assignment.setUserId(rs.getInt("userId"));
        assignment.setClassId(rs.getInt("classId"));
        assignment.setAssignmentName(rs.getString("assignmentName"));
        assignment.setAssignmentDueDate(rs.getDate("assignmentDueDate"));
        assignment.setAssignmentWeight(rs.getFloat("assignmentWeight"));
        assignment.setAssignmentGrade(rs.getFloat("assignmentGrade"));
        return assignment;
    }

    public static void addAssignment(Assignment assignment) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            PreparedStatement ps = dbCon.prepareStatement("INSERT INTO Assignments (userId, classId, assignmentName, assignmentDueDate, assignmentWeight, assignmentGrade) VALUES (?,?,?,?,?,NULL)");
            ps.setInt(1, assignment.userId);
            ps.setInt(2, assignment.classId);
            ps.setString(3, assignment.assignmentName);
            ps.setDate(4, new java.sql.Date(assignment.assignmentDueDate.getTime()));
            ps.setFloat(5, assignment.assignmentWeight);
            int i = ps.executeUpdate();

            if (i == 1) {
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }

    }

    public static void updateAssignment(int assignmentId, float assignmentGrade) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            PreparedStatement ps = dbCon.prepareStatement("UPDATE Assignments SET assignmentGrade=? WHERE assignmentId=?");
            ps.setFloat(1, assignmentGrade);
            ps.setInt(2, assignmentId);
            int i = ps.executeUpdate();

            if (i == 1) {
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }
    }

    public static ArrayList<Assignment> getAssignmentsForClass(int userId, int classId) throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            PreparedStatement ps = dbCon.prepareStatement("SELECT * FROM Assignments WHERE userId=? AND classId=?");
            ps.setInt(1,userId);
            ps.setInt(2, classId);
            ResultSet rs = ps.executeQuery();

            ArrayList<Assignment> assignments = new ArrayList<Assignment>();

            while (rs.next()) {
                Assignment assignment = getAssignmentFromResultSet(rs);
                assignments.add(assignment);
            }

            return assignments;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }

        return null;
    }
}
