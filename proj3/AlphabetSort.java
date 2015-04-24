// Am I... radix sorting this? I think so yeah. MSD sort? 
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.io.InputStreamReader;

public class AlphabetSort {



    // Traverse lexicographically, but in given order. 
    // Don't need to change how to insert. 

    // Wait, would you.... start at the root...then in the order of the alphabet, if the link exists, go through that link? 
        // How's the runtime tho?????????????????????????????????????????????????????????????
        // I hope it's okay?

    // Really hope I'm doing this StdIn stuff right. Thx to mkyong.com.

    

    public static boolean repeatsLetters(String s, HashSet<Character> seenLetters, int position) {

        if (s == null || position >= s.length()) {
            return false;
        }

        char newChar = s.charAt(position);

        if (seenLetters.contains(newChar)) {
            return true;
        }

        seenLetters.add(newChar);
        return repeatsLetters(s, seenLetters, position + 1);

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
            // System.out.println("Node printing at: " + start.c);
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

            // if (start.getCharacter() != null) {
            //     //System.out.println("get character not null");
            //     //System.out.println("start.c is " + start.c);
            //     preOrder(start.getLinks().get(firstChild), alphabet, soFar + firstChild);
            //     // Tbh, don't know why this works and not adding start.c.
            //         // Matt's thoughts: maybe, somehow, start is being reassigned to the last thing seen. the last word seen. And in the test.in, sequentially speaking, death was the last thing seen, so that's why there was all those d's? 
            //         // Just like, weird recursion things were going on. O____O. 
            //     // So instead of adding the char when actually at that node, add it preemptively. You know you're about to go down it, so just add it.
            //     // EVERYTHING IS WEIRD I DUNNO WHY IT WAS BEING WEIRD.  
            // } else {
            //     //System.out.println("Get character was null");
            //     preOrder(start.getLinks().get(firstChild), alphabet, soFar);
            // }

            //System.out.println("First child is " + firstChild);
            // if (start.getLinks().get(firstChild) != null) {
            //     System.out.println("First child, node, character is " + start.getLinks().get(firstChild).c);
            // }

            preOrder(start.links.get(firstChild), alphabet, soFar + firstChild);

        } 

    }

    /* Should chop off the alphabet while doing it. */
    private static Character firstChild(Node n, StringBuilder alphabetBuilder) {
        

        // Char in case no child - return null.

        if (n == null || n.links.isEmpty()) {
            return null;
        }

        String alphabet = alphabetBuilder.toString();

        Map<Character, Node> children = n.links;

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



    public static void main (String args[]) {

        // IllegalArgumentException when no words or alphabet given, OR letter appears multiple times in alphabet. Or if StdIn is empty. 

        // Shoot how do multiple times in alphabet?

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            //System.out.println("Made BufferedReader");

            String alphabet = br.readLine();
            //System.out.println("Read alphabet line");

            if (repeatsLetters(alphabet, new HashSet<Character>(), 0)) {
                throw new IllegalArgumentException("Letter appears multiple times in alphabet.");
            }

            //System.out.println("Checked alphabet didn't repeat.");

            String word = br.readLine();
            // System.out.println("Word is " + word);
            //System.out.println("Read in first word");
            if (word == null) {
                // No words given! Or maybe no alphabet given either.
                throw new IllegalArgumentException("No words / alphabet given.");
            }

            Trie wordTrie = new Trie();
            //System.out.println("Created new trie.");

            // First I think I have to put them into the Trie.
            while (word != null) {
                wordTrie.insert(word);
                word = br.readLine();
            }

            //System.out.println("Inserted all the words");

            // Then I can traverse and print in sorted order. 
            preOrder(wordTrie.root, alphabet, "");


        } catch (IOException io) {
            System.out.println("ARE YOU GOING INTO THE EXCEPTION???");
            throw new IllegalArgumentException("No alphabet or words given.");
        }
    }

}
