import java.util.HashMap; 
import java.util.Map;


/**
 * Node class that stores information for a Trie.
 * @author Alice Tarng.
 */
public class Node {

    /* Thanks a million to lecture 33 notes. */
    Character c;
    boolean exists;
    Map<Character, Node> links;


    /**
     * Constructs an empty node.
     */
    public Node() {
        links = new HashMap<Character, Node>();
        exists = false;
    }
}
