import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SendEmailNotication {
    public static void main(String[] args) {
        try {
            ArrayList<Assignment> assignments = getUpcomingAssignments();
            for (int i = 0; i < assignments.size(); i++) {
                System.out.print(assignments.get(i));
            }
           // sendSimpleMessage();
        } catch (Exception ex) {
            System.out.println("Failed");
            System.out.println(ex);
        }
    }

    // Methods needed to retrieve assignment data from the database
//    private static Assignment getAssignmentFromResultSet(ResultSet rs) throws SQLException {
////        Object userAssignments = new Object();
////        userAssignments["assignmentName"] = rs.getString("assignmentName");
////        assignment.setAssignmentDueDate(rs.getDate("assignmentDueDate"));
////        assignment.setAssignmentWeight(rs.getFloat("assignmentWeight"));
////        return assignment;
//    }

    public static ArrayList<Assignment> getUpcomingAssignments() throws Exception {
        Connection dbCon = new DatabaseConnection().getConnection();
        try {
            Statement stmt = dbCon.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT userId FROM Users");
            //ResultSet rs = stmt.executeQuery("SELECT assignmentName, assignmentDueDate, emailAddress FROM Assignments INNER JOIN Users ON Assignments.userId = Users.userId");

            while (rs.next()) {
                Statement assignmentsStmt = dbCon.createStatement();
                ResultSet assignmentsRs = assignmentsStmt.executeQuery("SELECT assignmentName, assignmentDueDate, emailAddress FROM Assignments INNER JOIN Users ON Assignments.userId = Users.userId WHERE Users.userId=" + rs.getInt("userId"));
                while (assignmentsRs.next()) {
                    System.out.print(assignmentsRs.getString("assignmentName"));
                    System.out.print(assignmentsRs.getDate("assignmentDueDate"));
                    System.out.print(assignmentsRs.getString("emailAddress"));
                }
            }

//            ArrayList<Assignment> assignments = new ArrayList<Assignment>();
//
//            while (rs.next()) {
//                Assignment assignment = getAssignmentFromResultSet(rs);
//                assignments.add(assignment);
//            }

            //return assignments;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            dbCon.close();
        }

        return null;
    }

    // Send an email notification for any assignments due in three days or less
    public static JsonNode sendSimpleMessage() throws UnirestException {
        HttpResponse<JsonNode> request = Unirest.post("https://api.mailgun.net/v3/www.gradebookcalculator.com/messages")
                .basicAuth("api", "404ad30e7c430579da7f22cda0c83e21-4412457b-76eb7e38")
                .queryString("from", "Gradebook App <webmaster@gradebookcalculator.com>")
                .queryString("to", "rdeprey@gmail.com")
                .queryString("subject", "hello")
                .queryString("text", "testing")
                .asJson();

        return request.getBody();
    }
}
