import org.junit.Test;
import static org.junit.Assert.*;


public class TestBST {

    @Test
    public void testPutGetSize() {
        BSTMap<Integer, String> um = new BSTMap<Integer, String>();
        um.put(1, "Dios Basado");
        um.put(2, "is so real.");
        assertEquals("Dios Basado", um.get(1));
        assertEquals(2 ,um.size());
    }

    @Test
    public void testClearContainsKey() {
        BSTMap<Integer, String> um = new BSTMap<Integer, String>();
        um.put(1, "Dios Basado");
        um.put(2, "is so real.");
        assertTrue(um.containsKey(1));
        um.clear();
        assertFalse(um.containsKey(1));
    }

    @Test
    public void testPrintInOrder() {
        BSTMap<Integer, String> um = new BSTMap<Integer, String>();
        um.put(14, "Fourteen");
        um.put(-5, "Negative Five");
        um.put(1, "One");
        um.put(3, "Three");
        um.put(2, "Two");
        um.put(-6, "Negative Six");
        System.out.println("Hey Alice check that the below things print in order of increasing key.");
        um.printInOrder();
    }


    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestBST.class);
    }

}