/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author 
 */
public class Trie {

    // Merci beaucoup beaucoup to lecture 33's slides. 

    /* Thx 5 million to the Algs textbooks. */

    private Node root;

    private static class Node {
        /* Char in this node. */
        private char c;
        /* Subtries - left, middle, right. */
        private Node left;
        private Node middle;
        private Node right;
        private boolean exists;

    }


    public boolean find(String s, boolean isFullWord) {

        Node endNode = findNode(s, this.root, 0);

        // Or I could do a thing that's like, return the final node, and see if exists or not.

        // IT MIGHT RETURN NULL. THAT'S A DEF NO (I think). 

        if (endNode == null) {
            return false;
        }

        if (isFullWord) {
            return endNode.exists;
        } else {
            // It would only be false if it was null - but already null check - so it has to be true if it's made it this far. 
            return true;
        }
    }


    private Node findNode(String s, Node start, int position) {

        if (s == null || start == null) {
            return null;
        }

        if (position >= s.length()) {
            // Overshot it. 
            return null;
        }

        if (position == s.length() - 1) {
            // On the last thing; return self. 
            return start;
        }

        char newChar = s.charAt(position);
        char currChar = start.c;

        // Greater than - haven't found yet - try go right.
        if (newChar > currChar) {
            return findNode(s, start.right, position);
        }
        // Less than - haven't found yet - try left.
        if (newChar < currChar) {
            return findNode(s, start.left, position);
        }
        // Equal to - great! Try next char, using middle.
        return findNode(s, start.middle, position + 1);
    }


    
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

            if (position == key.length() - 1) {
                // Last element - exists!
                start.exists = true;
            }
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
