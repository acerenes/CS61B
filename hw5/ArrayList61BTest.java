import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

/** ArrayList61BTest. You should write additional tests.
 *  @author Josh Hug
 */

public class ArrayList61BTest {
    @Test
    public void basicTest() {
        List<Integer> L = new ArrayList61B<Integer>();
        L.add(5);
        L.add(10);
        assertTrue(L.contains(5));        
        assertFalse(L.contains(0));

    }

    @Test
    public void testAll() {
        List<Integer> L = new ArrayList61B<Integer>(2);
        L.add(1);
        L.add(2);
        L.add(3);
        L.add(4);
        L.add(5);
        assertEquals(4, (double) L.get(3), 0);
        assertEquals(5, L.size());
    }

    /** Runs tests. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(ArrayList61BTest.class);
    }
}   