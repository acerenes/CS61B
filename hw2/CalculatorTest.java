import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CalculatorTest {
    /* Do not change this to be private. For silly testing reasons it is public. */
    public Calculator tester;

    /**
     * setUp() performs setup work for your Calculator.  In short, we 
     * initialize the appropriate Calculator for you to work with.
     * By default, we have set up the Staff Calculator for you to test your 
     * tests.  To use your unit tests for your own implementation, comment 
     * out the StaffCalculator() line and uncomment the Calculator() line.
     **/
    @Before
    public void setUp() {
        tester = new StaffCalculator(); // Comment me out to test your Calculator
        // tester = new Calculator();   // Un-comment me to test your Calculator
    }

    // TASK 1: WRITE JUNIT TESTS
    @Test
    public void testadd1() {
        assertEquals(891, tester.add(324, 567)); 
    }

    @Test
    public void testadd2() {
        assertEquals(-2, tester.add(-6, 4));
    }

    @Test
    public void testadd3() {
        assertEquals(-2147483647, tester.add(2147483647, 2));
    }

    /* Run the unit tests in this file. */
    public static void main(String... args) {
        jh61b.junit.textui.runClasses(CalculatorTest.class);
    } 


    // Tests for Multiply
    @Test
    public void testmultiply() {
        assertEquals(6, tester.multiply(2, 3));
        assertEquals(144, tester.multiply(12, 12));
        assertEquals(-10, tester.multiply(-2, 5));
        assertEquals(-2147479015, tester.multiply(46341, 46341));
    }     
}