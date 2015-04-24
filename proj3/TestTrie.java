import org.junit.Test;
import static org.junit.Assert.*;

public class TestTrie {

    @Test
    public void testInsertAndFind() {
        Trie t = new Trie();
        t.insert("hello");
        t.insert("hey");
        t.insert("goodbye");
        assertTrue(t.find("hell", false));
        assertTrue(t.find("hello", true));
        assertTrue(t.find("good", false));
        assertFalse(t.find("bye", false));
        assertFalse(t.find("heyy", false));
        assertFalse(t.find("hell", true));

        assertEquals(null, t.root.c);
    }

    @Test
    public void testLastCharacter() {
        Trie t2 = new Trie();
        t2.insert("yoo");
        assertTrue(t2.find("yoo", true));
        assertFalse(t2.find("yoe", true));

        assertTrue(t2.find("yo", false));
        assertFalse(t2.find("ye", false));

        assertFalse(t2.find("aoo", true));
        assertFalse(t2.find("aoo", false));

        assertFalse(t2.find("yeo", true));
        assertFalse(t2.find("yeo", false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionNull() {
        Trie t2 = new Trie();
        t2.insert(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionEmpty() {
        Trie t3 = new Trie();
        t3.insert("");
    }


    public static void main(String[] args) {
            jh61b.junit.textui.runClasses(TestTrie.class);
        }

}