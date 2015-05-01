 
import java.util.HashSet;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import java.io.InputStreamReader;


/**
 * Implements AlphabetSort for given alphabet and words.
 * @author Alice Tarng.
 */
public class AlphabetSort {


    // Really hope I'm doing this StdIn stuff right. Thx to mkyong.com.

    
    /**
     * Checks that alphabet does not repeat letters.
     * @param s string that we don't want to have repeated lettters.
     * @param seenLetters the letters we've already seen. Don't repeat.
     * @param position position in the string we're currently looking at.
     * @return boolean whether the alphabet repeated letters.
     */
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

    /**
     * Traverses through the trie in order of the alphabet, printing along way.
     * @param start node we're currently examining.
     * @param alphabet given alphabet whose order we must follow.
     * @param soFar is string we have so far, of the word.
     */
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

        // Then gotta do for other letters too. 
        // grab the first char in the alphabet that you have a child link for. 
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

            preOrder(start.links.get(firstChild), alphabet, soFar + firstChild);

        } 

    }

    /**
     * Returns character that is the earliest one node n has a child for.
     * @param n node whose children we're currently examining.
     * @param alphabetBuilder possibly chopped alphabet whose letters we run through.
     * @return character that is the earliest one in alphabet n has child for.
     */
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


    /**
     * Test client. Reads the data from input file, containing info to be sorted.
     * @param args takes name of an input file.
     */
    public static void main(String[] args) {

        // IllegalArgumentException when no words or alphabet given, 
        //OR letter appears multiple times in alphabet. Or if StdIn is empty. 

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
