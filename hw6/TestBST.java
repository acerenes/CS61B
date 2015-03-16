import org.junit.Test;
import static org.junit.Assert.*;


public class TestBST {

    @Test
    public void testBasic() {
        BSTMap<String, String> um = new BSTMap<String, String>();
        um.put("Gracias", "Dios Basado");
        um.put("The pain", "is so real.");
        assertEquals("Dios Basado", um.get("Gracias"));
        assertTrue(um.containsKey("Gracias"));
        assertEquals(2 ,um.size());
        um.clear();
        assertFalse(um.containsKey("Gracias"));
    }


    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(BSTMap.class);
    }

}