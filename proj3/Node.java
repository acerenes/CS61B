import java.util.HashMap; 
import java.util.Map;

public class Node {
    // /* Char in this node. */
    // private char c;
    // /* Subtries - left, middle, right. */
    // private Node left;
    // private Node middle;
    // private Node right;
    // private boolean exists;


    /* Thanks a million to lecture 33 notes. */
    Character c;
    boolean exists;
    Map<Character, Node> links;

    public Node() {
        links = new HashMap<Character, Node>();
        exists = false;
    }

    /*public Character getCharacter() {
        return this.c;
    }

    public Map<Character, Node> getLinks() {
        return this.links;
    }*/

    /*private Node insert(Node start, String key, int position) {

        if (position >= key.length()) {
            System.out.println("HIT NULL");
            return null; 
        }

        if (start == null) {
            start = new Node();
        }

        // NEED TO CREATE CHILLLLLDDDDDD. 
        // So you pass in parent, key, and position of the character the CHILD needs to have. 

        char ch = key.charAt(position);

        // start.c = ch;
        System.out.println("Node's c: " + start.c);

        // Gotta keep on treeing. 
        start.links.put(ch, insert(start.links.get(ch), key, position + 1));

        if (position == key.length() - 1) {
            // System.out.println("Key: " + key + "position: " + position);
            start.exists = true;
        }

        return start;
    }*/



}