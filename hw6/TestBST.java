import org.junit.Test;
import static org.junit.Assert.*;


public class TestBST {

    @Test
    public void testPutGetSize() {
        BSTMap<Integer, String> um = new BSTMap<Integer, String>();
        um.put(1, "Dios Basado");
        um.put(2, "is so real.");
        assertEquals("Dios Basado", um.get(1));
        /*assertTrue(um.containsKey("Gracias"));*/
        assertEquals(2 ,um.size());
        /*um.clear();
        assertFalse(um.containsKey("Gracias"));*/
    }


    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestBST.class);
    }

}