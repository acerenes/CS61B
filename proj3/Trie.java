/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author 
 */
public class Trie {

    // Merci beaucoup beaucoup to lecture 33's slides. 

    private Node root;

    private static class Node {
        /* Char in this node. */
        private char c;
        /* Subtries - left, middle, right. */
        private Node left;
        private Node middle;
        private Node right;
    }


    public boolean find(String s, boolean isFullWord) {
    }


    /* Thx 5 million to the Algs textbooks. */
    public void insert(String s) {

        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException("Cannot add null or empty string to trie.");
        }

        this.root = insert(this.root, s, 0);

    }

    private Node insert(Node start, String key, int position) {

        if (position >= key.length()) {
            return null; 
        }

        char newChar = key.charAt(position);

        if (start == null) {
            // Should be able to directly insert into this node.
            start = new Node();
            start.c = newChar;
        }

        // Okay, now need to continue with the inserting. Start definitely exists now. 
        if (newChar < start.c) {
            // I haven't found where I belong yet. 
            // Less than - have to try at node's "left".
            start.left = insert(start.left, key, position);

        } else if (newChar > start.c) {
            // I haven't found where I belong yet either. 
            // Greater than goes to "right".
            start.right = insert(start.right, key, position);

        } else {
            // I've already found home! Move onto next char. 
            // Equal - insert into middle. 
            start.middle = insert(start.middle, key, position + 1);
        }
        return start;
    }
}
