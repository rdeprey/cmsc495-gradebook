/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logins;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author H.Giles
 */
public class Login_sTest {
    
    public Login_sTest() {
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
     * Test of main method, of class Login_s.
     */
    @Test
    public void testEventListener() {
        System.out.println("main");
        String[] args = null;
        Login_s.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
        @Test
        public void login() {
        setText(By.name("username"), "test")
        .setText(By.name("password"), "abc123")
        

        .validateTextPresent("You are now logged in");
    }



}
