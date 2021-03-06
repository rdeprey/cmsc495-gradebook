/*********************************************************************************************************
 * File name: DatabaseConnection.java
 * Date: November/December 2018
 * Author: Haemee Nabors, Rebecca Deprey, Devon Artist, Harry Giles, Brittany White, Ryan Haas
 * Purpose: This class uses the Java Database Connectivity (JDBC) library to connect to a Microsoft SQL
 * database hosted on the Amazon Web Services Relational Database Services platform. It reads the database
 * connection data from a .txt file and uses it to connect to the database.
 *
 * References:
 * 1. Connect to a Database Using Java Code. (n.d.). Retrieved from https://www.homeandlearn.co.uk/java/connect_to_a_database_using_java_code.html
 *********************************************************************************************************/

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DatabaseConnection {
    // Connects to the SQL database runnning on Amazon Web Services' RDS platform
    public Connection getConnection() throws Exception {
        // Get the file with the connection data
        File jarFile = new File(Gradebook.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
        String path = jarFile + File.separator + "dbconnection.txt";
        //String path = "dbconnection.txt";

        // Read the file to get the credentials to connect to the database
        FileInputStream fileInputStream = new FileInputStream(path);
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(fileInputStream));
        String st;
        String host = "";
        String username = "";
        String password = "";
        int count = 0;
        while ((st = bufferedReader.readLine()) != null) {
            if (count == 0) {
                host = "jdbc:sqlserver://" + st;
            } else if (count == 1) {
                username = st;
            } else if (count == 2) {
                password = st;
                break;
            }

            count += 1;

            // If the size of the counter is greater than the maximum value for integers, then throw an exception to prevent
            // security issues caused by overflows
            if (count >= Integer.MAX_VALUE) {
                throw new RuntimeException("Reading file caused integer overflow");
            }
        }

        // Use the Java Database Connection driver to connect to the database
        String jdbcUrl = host + ":1433;DatabaseName=gradebookdb";

        // Try to connect to the database
        try {
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new RuntimeException("Error connecting to the database", ex);
        }
    }
}