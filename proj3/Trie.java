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

    public Trie() {
        this.root = new Node();
        // System.out.println("I CREATED A NEW TRIE");
        // System.out.println("this.root is null: " + (this.root == null));
    }


    public boolean find(String s, boolean isFullWord) {

        //Node endNode = findNode(s, this.root, 0);
        // Wait. Should prbly call on child. To avoid the null pointer thing. 
        char firstLetter = s.charAt(0);
        // System.out.println("searching on firstLetter " + firstLetter);
        Node child = this.root.links.get(firstLetter);
        Node endNode = findNode(s, child, 0);

        // Or I could do a thing that's like, return the final node, and see if exists or not.

        // IT MIGHT RETURN NULL. THAT'S A DEF NO (I think). 

        if (endNode == null) {
            return false;
        }

        char lastLetter = s.charAt(s.length() - 1);

        if (isFullWord) {
            return endNode.exists && (endNode.c == lastLetter);
        } else {
            // It would only be false if it was null - but already null check - so it has to be true if it's made it this far. 
            // Check the last letter tho. 
            // DON'T FORGET TO CHECK LAST LETTER. 
            return endNode.c == lastLetter;
        }
    }


    private Node findNode(String s, Node start, int position) {

        // System.out.println("CALLING COMPLICATED FINDNODE");
        // System.out.println("position is " + position);
        // System.out.println("root node's character is " + this.root.c);

        if (s == null || start == null) {
            // System.out.println("In null case");
            // System.out.println("String is " + s);
            // System.out.println("start node is null: " + (start == null));
            
            //System.out.println("Start's character is " + start.c);
            return null;
        }

        if (position >= s.length()) {
            //System.out.println("Overshot");
            // Overshot it. 
            return null;
        }

        if (position == s.length() - 1) {
            // On the last thing; return self. 
            //System.out.println("Just return last node");
            //System.out.println(start.c);
            return start;
        }

        if (start.c == null || start.c != s.charAt(position)) {
            //System.out.println("Line 80. Something's wrong.");
            return null; // You've already gone wrong. 
        }

        char newChar = s.charAt(position + 1);
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

        //System.out.println("103, is root null? " + (this.root == null));
        Node child = this.root.links.get(s.charAt(0));
        //child = insert(child, s, 0);

        this.root.links.put(s.charAt(0), insert(child, s, 0));
        //this.root.links.put(s.charAt(0), insert(this.root.links.get(s.charAt(0)), s, 0)); // Just initialize, before doing stuff. 
        //this.root.links.get(s.charAt(0)) = insert(this.root.links.get(s.charAt(0), s, 0));

        //this.root = insert(this.root, s, 0);

        //this.root = insert(this.root, s, -1);

    }

     private Node insert(Node start, String key, int position) {

        if (position >= key.length()) {
            //System.out.println("HIT NULL");
            return null; 
        }

        if (start == null) {
            start = new Node();
            char ch = key.charAt(position);

            start.c = ch;
            //System.out.println("Node's c: " + start.c);
        }

        // NEED TO CREATE CHILLLLLDDDDDD. 
        // So you pass in parent, key, and position of the character the CHILD needs to have. 

        

        if (position < key.length() - 1) {
            char nextChar = key.charAt(position + 1);

            // Gotta keep on treeing. 
            Node child = start.links.get(nextChar);
            //child = insert(child, key, position + 1);

            start.links.put(nextChar, insert(child, key, position + 1));

            //start.links.put(nextChar, insert(start.links.get(nextChar), key, position + 1));
        }

        if (position == key.length() - 1) {
            // System.out.println("Key: " + key + "position: " + position);
            start.exists = true;
        }

        //System.out.println("Line 150, is this.root null? " + (this.root == null));

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
} 


