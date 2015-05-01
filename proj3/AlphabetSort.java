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


    // StdIn stuff, thanks to mkyong.com.
    
    /**
     * Checks that alphabet does not repeat letters.
     * @param s string that we don't want to have repeated letters in.
     * @param seenLetters the letters we've already seen. Don't repeat any.
     * @param position position in the string we're currently at.
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
     * Traverses through the trie in order of the alphabet, printing along the way.
     * @param start node we're currently examining.
     * @param alphabet given alphabet whose order we must follow.
     * @param soFar string we have so far, of the word.
     */
    static void preOrder(Node start, String alphabet, String soFar) {
        // If your char is not null, add your char to the string. 

        if (start == null) {
            return;
        }

        // If you're "blue" (end of word), just return the string.
        if (start.exists) {
            System.out.println(soFar);
        } 

        // Then do other letters too. 
        // Grab the first char in the alphabet that you have a child link for. 

        StringBuilder canChopThisAlphabet = new StringBuilder(alphabet.substring(0)); 
        // Full copy --^

        while (canChopThisAlphabet.toString() != null) {

            Character firstChild = firstChild(start, canChopThisAlphabet);
            // canChopThisAlphabet will be chopped after this point.

            if (firstChild == null) {
                return;
            }

            preOrder(start.links.get(firstChild), alphabet, soFar + firstChild);
        } 

    }


    /**
     * Returns character that is the earliest node n has a child for.
     * @param n node whose children we're currently examining.
     * @param alphabetBuilder (possibly) chopped alphabet whose letters we run through.
     * @return character that is the earliest one in alphabet node n has child for.
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

            if (children.containsKey(checkChar)) {

                // Want to chop off current one as well.
                alphabet = alphabet.substring(i + 1); 

                alphabetBuilder.replace(0, alphabetBuilder.length() + 1, alphabet);
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

        try {

            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            String alphabet = br.readLine();

            if (repeatsLetters(alphabet, new HashSet<Character>(), 0)) {
                throw new IllegalArgumentException("Letter appears multiple times in alphabet.");
            }

            String word = br.readLine();
            if (word == null) {
                // No words given! Or maybe no alphabet given either.
                throw new IllegalArgumentException("No words / alphabet given.");
            }

            Trie wordTrie = new Trie();

            // First put words into the Trie.
            while (word != null) {
                wordTrie.insert(word);
                word = br.readLine();
            }

            // Then traverse and print in sorted order. 
            preOrder(wordTrie.root, alphabet, "");


        } catch (IOException io) {
            throw new IllegalArgumentException("No alphabet or words given.");
        }
    }

}
