/*
 * JUnit tests for the Triangle class
 */
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author melaniecebula
 */
public class TriangleTest {
  /**  We've already created a testScalene method.  Please fill in testEquilateral, and additionally
   *   create tests for Isosceles, Negative Sides, and Invalid sides
   **/

    @Test
    public void testScalene() {
        Triangle t = new Triangle(30, 40, 50);
        String result = t.triangleType();
        assertEquals("Scalene", result);
    }

    @Test
    public void testEquilateral() {
      Triangle tEq= new Triangle (30, 30, 30);
      String resultEq= tEq.triangleType();
      assertEquals("Equilateral", resultEq);
    }

    @Test
    public void testIsosceles() {
      Triangle tIs= new Triangle (20, 20, 2);
      String resultIs= tIs.triangleType();
      assertEquals("Isosceles", resultIs);
    }

    @Test
    public void testNegative() {
      Triangle tNeg= new Triangle(-1, -2, -3);
      String resultNeg= tNeg.triangleType();
      assertEquals("At least one length is less than 0!", resultNeg);
    }

    @Test
    public void testInvalid() {
      Triangle tInv= new Triangle(2, 3, 6);
      String resultInv= tInv.triangleType();
      assertEquals("The lengths of the triangles do not form a valid triangle!", resultInv);
    }


    public static void main(String[] args) {
      //TODO: RUN TESTS (Look in ArithmeticTest.java main method for help!
      jh61b.junit.textui.runClasses(TriangleTest.class);
    }
}
