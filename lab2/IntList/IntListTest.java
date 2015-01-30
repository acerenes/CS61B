 import static org.junit.Assert.*;
import org.junit.Test;

public class IntListTest {

    /** Example test that verifies correctness of the IntList.list static 
     *  method. The main point of this is to convince you that 
     *  assertEquals knows how to handle IntLists just fine.
     */

    @Test 
    public void testList() {
        IntList one = new IntList(1, null);
        IntList twoOne = new IntList(2, one);
        IntList threeTwoOne = new IntList(3, twoOne);

        IntList x = IntList.list(3, 2, 1);
        assertEquals(threeTwoOne, x);
    }

    @Test
    public void testdSquareList() {
      IntList L = IntList.list(1, 2, 3);
      IntList.dSquareList(L);
      assertEquals(IntList.list(1, 4, 9), L);
    }

    /** Do not use the new keyword in your tests. You can create
     *  lists using the handy IntList.list method.  
     * 
     *  Make sure to include test cases involving lists of various sizes
     *  on both sides of the operation. That includes the empty list, which
     *  can be instantiated, for example, with 
     *  IntList empty = IntList.list(). 
     *
     *  Keep in mind that dcatenate(A, B) is NOT required to leave A untouched.
     *  Anything can happen to A. 
     */

    @Test
    public void testsquareListRecursive() {
        IntList empty= IntList.list();
        IntList emptysquare= IntList.squareListRecursive(empty);
        assertEquals(null, emptysquare);
        assertEquals(IntList.list(), empty);

        IntList T= IntList.list(1, 2, 3, 4, 5);
        IntList Tsquare= IntList.squareListRecursive(T);
        assertEquals(IntList.list(1, 4, 9, 16, 25), Tsquare);
        assertEquals(IntList.list(1, 2, 3, 4, 5), T);
    }
    
    @Test
    public void testdcatenate() {
        IntList A= IntList.list(1, 2, 3, 4);
        IntList B= IntList.list(5, 6, 7, 8);
        IntList Dres= IntList.dcatenate(A, B);
        assertEquals(IntList.list(1, 2, 3, 4, 5, 6, 7, 8), Dres);
        assertEquals(Dres, A);

        IntList a= IntList.list(10);
        IntList b= IntList.list(9, 8, 7);
        IntList res= IntList.dcatenate(a, b);
        assertEquals(IntList.list(10, 9, 8, 7), res);
        assertEquals(res, a);        
    }

    @Test
    public void testcatenate() {
        IntList AC= IntList.list(10, 9, 8);
        IntList BC= IntList.list(7, 6, 5);
        IntList Cres= IntList.catenate(AC, BC);
        assertEquals(IntList.list(10, 9, 8, 7, 6, 5), Cres);
        assertEquals(IntList.list(10, 9, 8), AC);
    }

    /* Run the unit tests in this file. */
    public static void main(String... args) {
        jh61b.junit.textui.runClasses(IntListTest.class);
    }       
}   
