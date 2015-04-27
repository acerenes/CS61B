import org.junit.Test;
import static org.junit.Assert.*;

public class TestAutocompleteTST {

    @Test
    public void testBasics() {
        TST t = new TST();
        t.put("I", (double) 1);
        t.put("lit", (double) 3);
        t.put("up", (double) 2);
        t.put("like", (double) 4);
        t.put("a", (double) 1);
        t.put("Christmas", (double) 9);
        t.put("tree", (double) 4);
        assertTrue(t.contains("Christmas"));
        assertFalse(t.contains("lik"));
        assertTrue(t.contains("I"));
        assertFalse(t.contains("i"));
        assertFalse(t.contains("ul"));

        assertEquals(t.getOwnWeight("I"), 1, 0);
        assertNull(t.getOwnWeight("lik"));
        assertNull(t.getOwnWeight("tret"));
    }

    @Test
    public void testExample() {
        TST t2 = new TST();
        t2.put("smog", (double) 5);
        t2.put("buck", (double) 10);
        t2.put("sad", (double) 12);
        t2.put("spite", (double) 20);
        t2.put("spit", (double) 15);
        t2.put("spy", (double) 7);

        assertTrue(t2.contains("smog"));
        assertFalse(t2.contains("sbuck"));
        assertFalse(t2.contains("bucc"));
        assertTrue(t2.contains("spy"));
        assertFalse(t2.contains("soy"));

        assertEquals(t2.getOwnWeight("spite"), 20, 0);
        assertEquals(t2.getOwnWeight("spit"), 15, 0);

        assertEquals(t2.subMaxWeight(t2.root), 20, 0);

        //ACNode tOfSpit = t2.root.middle.right.middle.middle;
        assertEquals(t2.subMaxWeight(t2.root.middle.right.middle.middle), 20, 0);
    }

    @Test
    public void testFindNode() {
        TST t3 = new TST();
        t3.put("smog", (double) 5);
        t3.put("buck", (double) 10);
        t3.put("sad", (double) 12);
        t3.put("spite", (double) 20);
        t3.put("spit", (double) 15);

        assertEquals(t3.findNode(t3.root, "spite", 0).ownWeight, 20, 0);
    }




    public static void main(String[] args) {
            jh61b.junit.textui.runClasses(TestAutocompleteTST.class);
        }

}