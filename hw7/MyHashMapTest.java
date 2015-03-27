import org.junit.Test;
import static org.junit.Assert.*;

public class MyHashMapTest {

    @Test
    public void testBasics() {
        MyHashMap<String, Integer> map = new MyHashMap(1);
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        assertEquals(3, map.size());
        assertEquals(3, map.get("three"), 0);
    }

    @Test
    public void testClearing() {
        MyHashMap<String, Integer> map = new MyHashMap(1);
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        assertTrue(map.containsKey("three"));
        assertFalse(map.containsKey("four"));
        map.clear();
        assertEquals(0, map.size());
    }

    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(MyHashMapTest.class);
    }
}