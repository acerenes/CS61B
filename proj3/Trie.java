/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author Alice Tarng.
 */


public class Trie {

    // Merci beaucoup beaucoup to lecture 33's slides. 

    /* Aussi merci to the Algs textbooks. */

    Node root;

    /**
     * Constructor for the Trie data structure.
     */
    public Trie() {
        this.root = new Node();
    }


    /**
     * Checks whether word exists in the trie.
     * @param s "word" being looked for.
     * @param isFullWord whether or not we're looking for prefix or full word.
     * @return boolean whether the word and its conditions are in the trie.
     */
    public boolean find(String s, boolean isFullWord) {

        char firstLetter = s.charAt(0);
        Node child = this.root.links.get(firstLetter);
        Node endNode = findNode(s, child, 0);

        if (endNode == null) {
            return false;
        }

        char lastLetter = s.charAt(s.length() - 1);

        if (isFullWord) {
            return endNode.exists && (endNode.c == lastLetter);
        } else {
            // But don't forget to check last letter.
            return endNode.c == lastLetter;
        }
    }



    /**
     * Finds the end node for the string.
     * @param s string being looked for.
     * @param start node from which we begin to search.
     * @param position current position in the string we're at.
     * @return end node at which the string is contained.
     */
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

        if (start.c == null || start.c != s.charAt(position)) {
            // You've already gone wrong. 
            return null; 
        }

        char newChar = s.charAt(position + 1);

        return findNode(s, start.links.get(newChar), position + 1);

    }


    /**
     * Inserts string into the trie.
     * @param s string being inserted into the trie.
     */
    public void insert(String s) {

        if (s == null || s.isEmpty()) {
            throw new IllegalArgumentException("Cannot add null or empty string to trie.");
        }

        Node child = this.root.links.get(s.charAt(0));

        this.root.links.put(s.charAt(0), insert(child, s, 0));

    }


    /**
     * Does the actual, under the cover inserting for the trie.
     * @param start node from which we begin to traverse the trie.
     * @param key string being inserted.
     * @param position position in the string currently being considered.
     * @return self as node, all pretty and complete.
     */
    private Node insert(Node start, String key, int position) {

        if (position >= key.length()) {
            return null; 
        }

        if (start == null) {
            start = new Node();
            char ch = key.charAt(position);

            start.c = ch;
        }

        // Create child: pass in parent, key, position of the character the child needs to have.

        if (position < key.length() - 1) {
            char nextChar = key.charAt(position + 1);

            // Gotta keep on treeing. 
            Node child = start.links.get(nextChar);

            start.links.put(nextChar, insert(child, key, position + 1));
        }

        if (position == key.length() - 1) {
            start.exists = true;
        }

        return start;
    } 
} 


