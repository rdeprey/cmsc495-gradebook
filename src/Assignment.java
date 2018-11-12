import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Assignment {
    private int assignmentId;
    private int userId;
    private int classId;
    private String assignmentName;
    private Date assignmentDueDate;
    private float assignmentWeight;
    private float assignmentGrade;

    // Default constructor
    public Assignment() {

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
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public void setAssignmentDueDate(Date assignmentDueDate) {
        this.assignmentDueDate = assignmentDueDate;
    }

    public void setAssignmentWeight(float assignmentWeight) {
        this.assignmentWeight = assignmentWeight;
    }

    public void setAssignmentGrade(float assignmentGrade) {
        this.assignmentGrade = assignmentGrade;
    }

    // Methods needed to retrieve assignment data from the database
    private static Assignment getAssignmentFromResultSet(ResultSet rs) throws SQLException {
        Assignment assignment = new Assignment();
        assignment.setUserId(rs.getInt("userId"));
        assignment.setClassId(rs.getInt("classId"));
        assignment.setAssignmentName(rs.getString("assignmentName"));
        assignment.setAssignmentDueDate(rs.getDate("assignmentDueDate"));
        assignment.setAssignmentWeight(rs.getFloat("assignmentWeight"));
        return assignment;
    }

    public static boolean addAssignment(Assignment assignment) throws Exception {
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
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }

        return false;
    }

//    public static ArrayList<Assignment> getAssignmentsForClass(int ) throws Exception {
//        Connection dbCon = new DatabaseConnection().getConnection();
//        try {
//            Statement stmt = dbCon.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM Assignments WHERE classEndDate >= GETDATE()");
//
//            ArrayList<Assignment> assignments = new ArrayList<Assignment>();
//
//            while (rs.next()) {
//                Assignment assignment = getAssignmentFromResultSet(rs);
//                assignments.add(assignment);
//            }
//
//            return assignments;
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        } finally {
//            dbCon.close();
//        }
//
//        return null;
//    }
}
