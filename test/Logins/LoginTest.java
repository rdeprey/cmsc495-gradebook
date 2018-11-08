package Logins;

import java.io.BufferedReader;
import java.io.FileReader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.*;
import org.junit.runners.*;

/**
 *
 * @author H.Giles
 */
public class LoginTest {

    public LoginTest() {
    }

    private static boolean equalFiles(String expectedFileName, String result) {
        BufferedReader bExp = null;
        String expLine;

        try {
            bExp = new BufferedReader(new FileReader(expectedFileName));

            if ((bExp != null)) {
                expLine = bExp.readLine();
                String match = expLine.substring(0, result.length());
                System.out.println(match);
                System.out.println(result);
                return match.equals(result);
            }
        } catch (Exception e) {
        } finally {
            try {
                if (bExp != null) {
                    bExp.close();
                }
            } catch (Exception e) {
            }

        }

        return false;

    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class Login.
     */
    // Test that the user cant login after 3 invalid attempts 
    @Test
    public void testInvalidLogin() {
        Login login = new Login();
        assertFalse(login.login("admin", "test"));
        assertEquals(1, login.getAttempts());
        assertFalse(login.login("admin", "test"));
        assertEquals(2, login.getAttempts());
        assertFalse(login.login("admin", "test"));
        assertEquals(3, login.getAttempts());
        assertFalse(login.login("admin", "admin123"));
    }
    // Test that the log3.txt was created 

    @Test
    public void testUnsuccessfulLog() {
        Login login = new Login();
        assertFalse(login.login("Test", "test"));
        String match = "Username:" + "Test" + "  Unsuccessful Login, Timestamp at: ";
        assertTrue(equalFiles("log3.txt", match));

    }
    // Test that the User cant login with invalid password and invalid username 
    @Test
    public void testInvalidLogin1() {
        Login login = new Login();
        assertFalse(login.login("Test", "test"));

    }

    // Test that the User cant login with invalid password and valid username 
    @Test
    public void testInvalidLogin2() {
        Login login = new Login();
        assertFalse(login.login("admin", "test"));

    }
    
    // Test that the User cant login with valid password and invalid username 
    @Test
    public void testInvalidLogin3() {
        Login login = new Login();
        assertFalse(login.login("Test", "test"));

    }
    
    //Test that the user can login with valid username and password.
    @Test
    public void testValidLogin() {
        Login login = new Login();
        assertTrue(login.login("admin", "admin123"));
    }

    
 
    
    
    
    
    private static class InterfaceReader {

        public InterfaceReader() {
        }
    }

// Tests for successful handling of user text inputs
    @RunWith(Parameterized.class)
    public class InterfaceReaderTest {

        // Declare manipulable test parameters
        private InterfaceReader FIELDS;
        private final boolean val;
        private final boolean cor;
        private final String user;
        private final String pass;

        // Initialize test parameters
        public InterfaceReaderTest(String user, String pass, boolean val, boolean cor) {
            this.val = val;
            this.cor = cor;
            this.user = user;
            this.pass = pass;
        } // End class constructor
    }
}
