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

         

        char ch = key.charAt(position);

        start.c = ch;

        // Gotta keep on treeing. 
        start.links.put(ch, insert(start.links.get(ch), key, position + 1));

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

        //System.out.println("In preOrder");

        if (start == null) {
            return;
        }

        //System.out.println("checked node not null");

        //System.out.println("This node's char is " + start.getCharacter());

        /*String soFarAdded = null; // Intialize. 
        boolean addedChar = false;*/

        // If you're "blue", just return the string then.
        if (start.exists) {
            System.out.println(soFar);
        } 

        // Then gotta do for other letters too. grab the first char in the alphabet that you have a child link for. 
        // And grab that node, and keep...going. I guess. WHAT AM I DOING. 
        StringBuilder canChopThisAlphabet = new StringBuilder(alphabet.substring(0)); // Full copy.

        while (canChopThisAlphabet.toString() != null) {

            Character firstChild = firstChild(start, canChopThisAlphabet);
            //System.out.println("First child is " + firstChild);
            // canChopThisAlphabet will be chopped after this point too.
            //System.out.println("chopped alphabet is " + canChopThisAlphabet);
            if (firstChild == null) {
                //System.out.println("RETURNING FROM THE WHILE LOOP");

                //System.out.println(soFar);
                return;
            }

            //System.out.println("Before preOrder call again: child of the char is " + start.getLinks().get(firstChild).getCharacter());

            /*if (addedChar) {
                // Because you added a char to your string, you want to KEEP THE PROGRESS. 
                preOrder(start.getLinks().get(firstChild), alphabet, soFarAdded);

            } else {
                preOrder(start.getLinks().get(firstChild), alphabet, soFar);
                // You didn't add anything, so...just...keep going with that lack of progress...I guess...
            }*/

            if (start.getCharacter() != null) {
                //System.out.println("get character not null");
                //System.out.println("start.c is " + start.c);
                preOrder(start.getLinks().get(firstChild), alphabet, soFar + firstChild);
                // Tbh, don't know why this works and not adding start.c.
                    // Matt's thoughts: maybe, somehow, start is being reassigned to the last thing seen. the last word seen. And in the test.in, sequentially speaking, death was the last thing seen, so that's why there was all those d's? 
                    // Just like, weird recursion things were going on. O____O. 
                // So instead of adding the char when actually at that node, add it preemptively. You know you're about to go down it, so just add it.
                // EVERYTHING IS WEIRD I DUNNO WHY IT WAS BEING WEIRD.  
            } else {
                //System.out.println("Get character was null");
                preOrder(start.getLinks().get(firstChild), alphabet, soFar);
            }

        } 

    }

    /* Should chop off the alphabet while doing it. */
    private static Character firstChild(Node n, StringBuilder alphabetBuilder) {
        

        // Char in case no child - return null.

        if (n == null || n.links.isEmpty()) {
            return null;
        }

        String alphabet = alphabetBuilder.toString();

        Map<Character, Node> children = n.getLinks();

        for (int i = 0; i < alphabet.length(); i = i + 1) {
            char checkChar = alphabet.charAt(i);
            //System.out.println("In first child - checking char " + checkChar);
            //System.out.println("In first child - node's links keys are " + n.links.keySet());
            if (children.containsKey(checkChar)) {

                alphabet = alphabet.substring(i + 1); // Want to chop off current one as well. ]

                // STRING ARE IMMUTABLE. 
                // SO IT'S NOT CHANGING THE THING PASSED IN. ONLY CHANGING LOCALLY. 

                // Maybe try a StringBuilder? And directly mutate it in here. 

                // alphabetBuilder = new StringBuilder(alphabet);
                alphabetBuilder.replace(0, alphabetBuilder.length() + 1, alphabet);
                //System.out.println("In first child - chopped alphabet is " + alphabet);
                //System.out.println("Alphabet chopped: " + alphabet);
                //System.out.println("return first child = " + checkChar);
                return checkChar;
            }
        }

        return null;

    }
}
