import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashSet;

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

    @Test
    public void testRemove() {
        MyHashMap<String, Integer> map = new MyHashMap(1);
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        assertEquals(1, map.remove("one"), 0);
        assertFalse(map.containsKey("one"));
    }

    @Test
    public void testSpecialRemove() {
        MyHashMap<String, Integer> map = new MyHashMap(1);
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        assertEquals(null, map.remove("one", 2));
        assertTrue(map.containsKey("one"));

    }

    @Test
    public void testKeySet() {
        MyHashMap<String, Integer> map = new MyHashMap(1);
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        HashSet<String> testSet = new HashSet<String>();
        testSet.add("one");
        testSet.add("two");
        testSet.add("three");
        assertTrue(testSet.equals(map.keySet()));
    }

    @Test
    public void testGet() {
        MyHashMap<String, Integer> map = new MyHashMap(1000);
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        map.put("five", 5);
        map.put("six", 6);
        map.put("harry potter", 7);
        map.put("three", 33);
        map.put("WHHYYYYYYYY", 2312);
        assertEquals(2312, map.get("WHHYYYYYYYY"), 0);
        assertEquals(6, map.get("six"), 0);
        assertEquals(7, map.get("harry potter"), 0);
        assertEquals(33, map.get("three"), 0);
        assertEquals(null, map.get("four"));
    }


    @Test
    public void testContainsKey() {
        MyHashMap<Integer, String> map = new MyHashMap(2);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        assertTrue(map.containsKey(1));
        assertTrue(map.containsKey(3));
        assertFalse(map.containsKey(33));
    }

    @Test
    public void testSize() {
        MyHashMap<Integer, String> map = new MyHashMap(2);
        map.put(1, "one");
        map.put(2, "two");
        map.put(3, "three");
        assertEquals(3, map.size());
        assertEquals("two", map.remove(2));
        assertEquals(2, map.size());
    }

    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(MyHashMapTest.class);
    }
}