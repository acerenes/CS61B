/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author 
 */

import java.util.HashMap; 
import java.util.Map;


public class Trie {

    // Merci beaucoup beaucoup to lecture 33's slides. 

    /* Thx 5 million to the Algs textbooks. */

    Node root;

    public static class Node {
        // /* Char in this node. */
        // private char c;
        // /* Subtries - left, middle, right. */
        // private Node left;
        // private Node middle;
        // private Node right;
        // private boolean exists;


        /* Thanks a million to lecture 33 notes. */
        private Character c;
        private boolean exists;
        private Map<Character, Node> links;

        public Node() {
            links = new HashMap<Character, Node>();
            exists = false;
        }

        public Character getCharacter() {
            return this.c;
        }

        public Map<Character, Node> getLinks() {
            return this.links;
        }

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
        // System.out.println(newChar);
        // char currChar = start.c;

        // // Greater than - haven't found yet - try go right.
        // if (newChar > currChar) {
        //     return findNode(s, start.right, position);
        // }
        // // Less than - haven't found yet - try left.
        // if (newChar < currChar) {
        //     return findNode(s, start.left, position);
        // }
        // Equal to - great! Try next char, using middle.
        //return findNode(s, start.middle, position + 1);


        return findNode(s, start.links.get(newChar), position + 1);

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

        if (start == null) {
            start = new Node();
        }

        if (position == key.length() - 1) {
            // System.out.println("Key: " + key + "position: " + position);
            start.exists = true;
            return start;
        }

        char c = key.charAt(position);

        // Gotta keep on treeing. 
        start.links.put(c, insert(start.links.get(c), key, position + 1));

        return start;

        // char newChar = key.charAt(position);

        // if (start == null) {
        //     // Should be able to directly insert into this node.
        //     start = new Node();
        //     start.c = newChar;

        //     if (position == key.length() - 1) {
        //         // Last element - exists!
        //         start.exists = true;
        //     }
        // }

        // // Okay, now need to continue with the inserting. Start definitely exists now. 
        // if (newChar < start.c) {
        //     // I haven't found where I belong yet. 
        //     // Less than - have to try at node's "left".
        //     start.left = insert(start.left, key, position);

        // } else if (newChar > start.c) {
        //     // I haven't found where I belong yet either. 
        //     // Greater than goes to "right".
        //     start.right = insert(start.right, key, position);

        // } else {
        //     // I've already found home! Move onto next char. 
        //     // Equal - insert into middle. 
        //     start.middle = insert(start.middle, key, position + 1);
        // }
        // return start;
    }


    /* For AlphabetSort. */
    static void preOrder(Node start, String alphabet, String soFar) {
        // If your char is not null, add your char to the string. 

        System.out.println("In preOrder");

        if (start == null) {
            return;
        }

        System.out.println("checked node not null");

        if (start.getCharacter() != null) {
            soFar = soFar + start.c;
            //System.out.println("soFar: " + soFar);
        }

        // If you're "blue", just return the string then.
        
        if (start.exists) {
            System.out.println(soFar);
        } 

        // Then gotta do for other letters too. grab the first char in the alphabet that you have a child link for. 
        // And grab that node, and keep...going. I guess. WHAT AM I DOING. 
        String canChopThisAlphabet = alphabet.substring(0); // Full copy.
        while (canChopThisAlphabet != null) {

            Character firstChild = firstChild(start, canChopThisAlphabet);
            System.out.println("First child is " + firstChild);
            // canChopThisAlphabet will be chopped after this point too.
            System.out.println("chopped alphabet is " + canChopThisAlphabet);
            if (firstChild == null) {
                return;
            }
            preOrder(start.getLinks().get(firstChild), alphabet, soFar);

        } 

    }

    private static Character firstChild(Node n, String alphabet) {
        // Should chop off the alphabet while doing it.

        // Char in case no child - return null.

        if (n == null || n.links.isEmpty()) {
            return null;
        }

        Map<Character, Node> children = n.getLinks();

        for (int i = 0; i < alphabet.length(); i = i + 1) {
            char checkChar = alphabet.charAt(i);
            System.out.println("In first child - checking char " + checkChar);
            System.out.println("In first child - node's links keys are " + n.links.keySet());
            if (children.containsKey(checkChar)) {

                alphabet = alphabet.substring(i + 1); // Want to chop off current one as well. 
                System.out.println("In first child - chopped alphabet is " + alphabet);
                return checkChar;
            }
        }

        return null;

    }
}
