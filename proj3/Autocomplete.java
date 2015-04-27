/**
 * Implements autocomplete on prefixes for a given dictionary of terms and weights.
 */

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.lang.IllegalArgumentException;


public class Autocomplete {

    TST allWords;
    PriorityQueue checkOut;
    LinkedList topResults;

    /**
     * Initializes required data structures from parallel arrays.
     * @param terms Array of terms.
     * @param weights Array of weights.
     */
    public Autocomplete(String[] terms, double[] weights) {

        // So, like, using a TST? 
        this.allWords = new TST();
        // And a PQ?
            // DARN IT THE PQ HAS TO ORDER BY MAX SUB WEIGHT THO. LSDFIUGHDLSIUGHDSLIUGHDSLRIGHSDLRIUGHDSLIRUGHLDIRSGHLDSIGUHDLRIGHDLIGHDLIGHDLSIU
            // Darn it I have to write a comparator.
            // I DON'T WANNA WRITE A COMPARATOR. 
        checkOut = new PriorityQueue(11, new NodeComparator()); // Default initial capacity is 11. So. Yeah.
        // And a list or smth???? 
        topResults = new LinkedList();


        // Error Cases:
            // Length of the terms and weight arrays are different.
        if (terms.length != weights.length) {
            throw new IllegalArgumentException("Length of terms and weights arrays different.");
        }

            
        for (int i = 0; i < terms.length; i = i + 1) {
            // Duplicate input terms.
            // Okay according to what I have in TST.java, if the thing I'm trying to put is already in there, I throw an IllegalArgumentException, so that should be fine. 

            // Negative weights.
            if (weights[i] < 0) {
                throw new IllegalArgumentException("Weights cannot be negative.");
            }
            this.allWords.put(terms[i], weights[i]);
        }
            
            
    }

    public class NodeComparator implements Comparator<ACNode> {

        public int compare(ACNode node1, ACNode node2) {
            return (int) (node1.maxSubWeight - node2.maxSubWeight);
        }
    }




    /**
     * Find the weight of a given term. If it is not in the dictionary, return 0.0
     * @param term
     * @return
     */
    public double weightOf(String term) {
        if (this.allWords.getOwnWeight(term) == null) {
            // The term doesn't exist.
            return 0.0;
        }
        return this.allWords.getOwnWeight(term);
    }

    /**
     * Return the top match for given prefix, or null if there is no matching term.
     * @param prefix Input prefix to match against.
     * @return Best (highest weight) matching string in the dictionary.
     */
    public String topMatch(String prefix) {
        // SNUBBING
        return "drool";
    }

    /**
     * Returns the top k matching terms (in descending order of weight) as an iterable.
     * If there are less than k matches, return all the matching terms.
     * @param prefix
     * @param k
     * @return
     */
    public Iterable<String> topMatches(String prefix, int k) {
        // SNUBBING
        return null;

        // Trying to find the k top matches for non-positive k. 
    }

    /**
     * Returns the highest weighted matches within k edit distance of the word.
     * If the word is in the dictionary, then return an empty list.
     * @param word The word to spell-check
     * @param dist Maximum edit distance to search
     * @param k    Number of results to return 
     * @return Iterable in descending weight order of the matches
     */
    public Iterable<String> spellCheck(String word, int dist, int k) {
        LinkedList<String> results = new LinkedList<String>();  
        /* YOUR CODE HERE; LEAVE BLANK IF NOT PURSUING BONUS */
        return results;
    }
    /**
     * Test client. Reads the data from the file, 
     * then repeatedly reads autocomplete queries from standard input and prints out the top k matching terms.
     * @param args takes the name of an input file and an integer k as command-line arguments
     */
    public static void main(String[] args) {
        // initialize autocomplete data structure
        In in = new In(args[0]);
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }

        Autocomplete autocomplete = new Autocomplete(terms, weights);

        // process queries from standard input
        int k = Integer.parseInt(args[1]);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            for (String term : autocomplete.topMatches(prefix, k))
                StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);
        }
    }


    public class ACNode {

            Character c; 
            Double ownWeight;
            Double maxSubWeight;

            ACNode left;
            ACNode right;
            ACNode middle;

            
        }


    public class TST {


        ACNode root;

        

        public TST() {
            this.root = new ACNode();
        }


        public boolean contains(String key) {
            return getOwnWeight(key) != null;
        }

        public Double getOwnWeight(String key) {
            if (key == null) {
                throw new NullPointerException("Tried to check if TST contains null string.");
            }
            if (key.length() == 0) {
                throw new IllegalArgumentException("Tried to check if TST contains string of length 0.");
            }
            ACNode x = get(this.root, key, 0); // Trying to get the Node for the end of the word. 
            if (x == null) {
                return null;
            }
            return x.ownWeight;
        }


        public ACNode get(ACNode start, String key, int keyPosition) {
            if (key == null) {
                throw new NullPointerException("In ACNode, tried to get a null key.");
            }
            if (key.length() == 0) {
                throw new IllegalArgumentException("In ACNode, tried to get a key with length 0.");
            }
            if (keyPosition >= key.length()) {
                // Overshot it - it doesn't exist.
                return null; 
            }
            if (start == null) {
                return null;
            }

            Character currKeyChar = key.charAt(keyPosition);
            if (start.c != null) {
                if (currKeyChar < start.c) {
                    // Obstacle, turn left, you haven't found the char at this position yet, so keep it there. 
                    return get(start.left, key, keyPosition);
                }
                if (currKeyChar > start.c) {
                    return get(start.right, key, keyPosition);
                }
                if (keyPosition < key.length() - 1) {
                    // You still have to move on; aren't looking for the end of the word yet. 
                    // But in good news, you found the correct character! So you can move on to the next character!
                    return get(start.middle, key, keyPosition + 1);
                }
            }
            // Okay, you've found the last character. Return the node you're currently on.
            return start;
        }

         
        public void put(String key, Double ownWeight) {

            if (this.contains(key)) {
                // According to the spec, throw an IllegalArgumentException if there are duplicate input terms.
                throw new IllegalArgumentException("Duplicate input terms.");
            }
            this.root = put(this.root, key, ownWeight, 0);

            // Okay I did a thing in put that hopefully does the max sub trie thing. Hopefully. 
        }

        public ACNode put(ACNode start, String key, Double newWeight, int keyPosition) {
            //System.out.println("Inserting " + key);
            Character currKeyChar = key.charAt(keyPosition);
            if (start == null || start.c == null) {
                //System.out.println("Line 97");
                start = new ACNode();
                start.c = currKeyChar;
            }

            if (currKeyChar < start.c) {
                //System.out.println("Line 103");
                start.left = put(start.left, key, newWeight, keyPosition);
            } else if (currKeyChar > start.c) {
                //System.out.println("Line 106");
                start.right = put(start.right, key, newWeight, keyPosition);
            } else if (keyPosition < key.length() - 1) {
                //System.out.println("LIne 109");
                start.middle = put(start.middle, key, newWeight, keyPosition + 1);
            } else {
                // System.out.println("Line 112");
                //System.out.println("Line 113 Start is null: " + (start == null));
                start.ownWeight = newWeight;
                //System.out.println("line 115 start is null: " + (start == null));
                // DO SOMETHING ABOUT MAX SUB WEIGHT HERE. 
                start.maxSubWeight = subMaxWeight(start);

            }
            return start;
        }


        // Changing it as I go too.....
        public Double subMaxWeight(ACNode start) {
            //System.out.println("Line 126 Start is null: " + (start == null));
            if (start == null) {
                //System.out.println("Line 128");
                //System.out.println("Line 129 Start is null: " + (start == null));
                //System.out.println("Start.maxSubWeight = " + start.maxSubWeight);
                //start.maxSubWeight = (double) 0;
                return (double) 0;

            } else if (start.left == null && start.right == null && start.middle == null) {
                //System.out.println("LIne 134");
                start.maxSubWeight = start.ownWeight;

            } else {
                //System.out.println("Line 138");
                Double rightAndLeftMax = Math.max(subMaxWeight(start.left), subMaxWeight(start.right));

                if (start.ownWeight == null) {
                    // Then don't have to worry about own weight.
                    start.maxSubWeight = Math.max(rightAndLeftMax, subMaxWeight(start.middle));
                } else {
                    Double selfAndMiddleMax = Math.max(start.ownWeight, subMaxWeight(start.middle));
                    start.maxSubWeight = Math.max(rightAndLeftMax, selfAndMiddleMax);
                }
            }

            return start.maxSubWeight;
        }

    }
}
