import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;

/** ULLMapTest. You should write additional tests.
 *  @author Josh Hug
 */

public class ULLMapTest {
    @Test
    public void testBasic() {
        ULLMap<String, String> um = new ULLMap<String, String>();
        um.put("Gracias", "Dios Basado");
        um.put("The pain", "is so real.");
        assertEquals("Dios Basado", um.get("Gracias"));
        assertTrue(um.containsKey("Gracias"));
        assertEquals(2 ,um.size());
        um.clear();
        assertFalse(um.containsKey("Gracias"));
    }

    
    @Test
    public void testIterator() {
        ULLMap<Integer, String> um = new ULLMap<Integer, String>();
        um.put(0, "zero");
        um.put(1, "one");
        um.put(2, "two");
        Iterator<Integer> umi = um.iterator();
        assertEquals(2, (double) umi.next(), 0); // Because I put new ones in front, the first key is 2. 
        assertEquals(1, (double) umi.next(), 0);

    }


    /*@Test
    public void testInvert() {
        ULLMap<Integer, String> um = new ULLMap<Integer, String>();
        um.put(0, "zero");
        um.put(1, "one");
        um.put(2, "two");
        ULLMap<String, Integer> inversed_um = ULLMap.invert(um);
        assertEquals(3, um.size());
        assertEquals(3, inversed_um.size());
        assertTrue(inversed_um.containsKey("zero"));
        assertEquals(0, (double) inversed_um.get("zero"), 0);
    }*/
    

    /** Runs tests. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(ULLMapTest.class);
    }
}
