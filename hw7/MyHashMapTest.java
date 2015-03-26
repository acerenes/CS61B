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
    }

    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(MyHashMapTest.class);
    }
}