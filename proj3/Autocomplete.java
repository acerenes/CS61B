

// LIDRUHGLDIRUHGLDSIHLIU ALICE TAKE OUT TINY.TXT OKAY OKAY

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.ArrayList;


/**
 * Implements autocomplete on prefixes for a given dictionary of terms and weights.
 * @author Alice Tarng.
 */
public class Autocomplete {

    TST allWords;
    PriorityQueue<ACNode> checkOut;
    //LinkedList<ACNode> topResults; // Should contain a bunch of nodes.
    ArrayList<ACNode> topResults; 
    int numMaxSubWeights = 0;

    /**
     * Initializes required data structures from parallel arrays.
     * @param terms Array of terms.
     * @param weights Array of weights.
     */
    public Autocomplete(String[] terms, double[] weights) {

        // Error Cases:
            // Length of the terms and weight arrays are different.
        if (terms.length != weights.length) {
            throw new IllegalArgumentException("Length of terms and weights arrays different.");
        }

        // So, like, using a TST? 
        this.allWords = new TST();
        // And a PQ?
        checkOut = new PriorityQueue<ACNode>(1, new NodeComparator()); 
        // And a list or smth???? 
        //topResults = new LinkedList<ACNode>();
        topResults = new ArrayList<ACNode>();


        

            
        for (int i = 0; i < terms.length; i = i + 1) {
            // Duplicate input terms.
            // In TST.java, if duplicate, throws IllegalArgumentException already. 

            // Negative weights.
            if (weights[i] < 0) {
                throw new IllegalArgumentException("Weights cannot be negative.");
            }
            this.allWords.put(terms[i], weights[i]);
        }
            
            
    }

    /**
     * Implements comparator for nodes based on their maxSubWeight.
     * @author Alice Tarng.
     */
    public class NodeComparator implements Comparator<ACNode> {

        /**
         * Does the actual comparing of maxSubWeights.
         * @param node1 first node being compared.
         * @param node2 second node being compared.
         * @return int positive, 0, or negative for comparison.
         */
        public int compare(ACNode node1, ACNode node2) {
            // Priority Queues implement by least first tho.
            // Switching it around so I can do most first. 
            return (int) (node2.maxSubWeight - node1.maxSubWeight);
        }
    }

    /**
     * Find the weight of a given term. If it is not in the dictionary, return 0.0
     * @param term string whose weight we want.
     * @return double that is the node / term's weight.
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

        if (prefix == null) {
            throw new NullPointerException("Cannot find top match for null prefix.");
        }

        ACNode prefixNode = null;
        if (prefix.length() == 0) {
            prefixNode = highestNode(this.allWords.root);

        } else {
        
            prefixNode = this.allWords.findNode(allWords.root, prefix, 0);
            

            // If no matching term:
            if (prefixNode == null) {
                return null;
            }

            // Else, you found the node, so you can descend.
            ACNode prefixChild = prefixNode.middle;

            // i HOPE THIS IS RIGHT I'M JUST GOING OFF THE DRAWING AT THIS POINT. 
            ACNode endHighestNode = highestNode(prefixChild);

            if (prefixNode.ownWeight == null) {

                prefixNode = endHighestNode;

            } else if (endHighestNode != null && endHighestNode.ownWeight > prefixNode.ownWeight) {

                // Just because 100 character limit.
                prefixNode = endHighestNode;
            }
        }


        return walkBackUp(prefixNode, new StringBuilder(""));


    }

    /**
     * Return the string for the node we are at.
     * @param start node currently being examined.
     * @param soFar stringBuilder of the string we have so far for the node. 
     * @return string of the entire word.
     */
    public String walkBackUp(ACNode start, StringBuilder soFar) {
        if (start == null) {
            // So I'm going to assume this is, you got all the way up, to no more parent. 
            return soFar.toString();
        }
        return walkBackUp(start.parent, soFar.insert(0, start.c));
    }


    /**
     * Return the node with the highest weight.
     * @param start node whose highest node we want.
     * @return node that is the node with the highest weight under start.
     */
    public ACNode highestNode(ACNode start) {

        if (start == null) {
            return null;
        }

        double lookForThisWeight = start.maxSubWeight;

        if (start.ownWeight != null && start.ownWeight == lookForThisWeight) {
            return start;
        }

        // Otherwise, it's gotta be in its left, middle, or right. 
        if ((start.left != null) && start.left.maxSubWeight == lookForThisWeight) {
            return highestNode(start.left);
        }
        if ((start.right != null) && start.right.maxSubWeight == lookForThisWeight) {
            return highestNode(start.right);
        }
        return highestNode(start.middle);
    }


    /**
     * Traverses trie and adds nodes to the list of highest matches.
     * @param start node at which we start examining.
     * @param numMatches the number of matches we are looking for.
     */
    public void modifiedTraversal(ACNode start, int numMatches) {
        if (start == null) {
            return;
        }
        int resultSize = this.topResults.size(); 
        ACNode lastResult = null; Double lastWeight = null; Double startWeight = start.ownWeight;

        if (resultSize > 0) {
            lastResult = topResults.get(resultSize - 1);
            lastWeight = lastResult.ownWeight;
        }
        // DARN YOU CHARACTER COUNT.
        if (resultSize > numMatches) {
            if ((numMaxSubWeights > numMatches)) {
                return;
            }
            if ((start.maxSubWeight < topResults.get(resultSize - 1).ownWeight)) {
                return;
            }
        }

        // Checking that kth heaviest thing. 
        if ((resultSize == numMatches) && (startWeight != null) && (startWeight > lastWeight)) {

            if (!topResults.contains(start)) { 
                int m = this.topResults.size() - 1;   
                int k = m;
                while (this.topResults.get(k).ownWeight < start.ownWeight) {
                    k = k - 1;
                }

                topResults.remove((topResults.size()) - 1); // Remove the last thing? 
                if (k == m) {
                    // DIdn't move, so add to end.
                    topResults.add(start);
                } else {
                    topResults.add(k + 1, start);
                }
                if (start.ownWeight.equals(start.maxSubWeight)) {
                    this.numMaxSubWeights = this.numMaxSubWeights + 1;
                }
            }
            
        } else if (start.ownWeight != null && !topResults.contains(start)) {
            int i = this.topResults.size();
            if (i == 0) {
                this.topResults.add(start);
            } else if (i == 1) {
                if (this.topResults.get(0).ownWeight < start.ownWeight) {
                    this.topResults.add(0, start);
                } else {
                    this.topResults.add(start);
                }
            } else {
                int j = i - 1; // Because 0 indexing.
                while (j >= 0 && this.topResults.get(j).ownWeight < start.ownWeight) {
                    j = j - 1;
                }
                if (j == i - 1) {
                    // Never moved the pointer, so stick onto last.
                    this.topResults.add(start);
                } else {
                    this.topResults.add(j + 1, start);
                } 
            }
            if (start.ownWeight.equals(start.maxSubWeight)) {
                this.numMaxSubWeights = this.numMaxSubWeights + 1;
            }
        }
        if (start.left != null) {
            this.checkOut.add(start.left);
        }
        if (start.right != null) {
            this.checkOut.add(start.right);
        }
        if (start.middle != null) {
            this.checkOut.add(start.middle);
        }
        modifiedTraversal(this.checkOut.poll(), numMatches);
    }




    /**
     * Returns the top k matching terms (in descending order of weight) as an iterable.
     * If there are less than k matches, return all the matching terms.
     * @param prefix String under who we must look.
     * @param k how many matches we want.
     * @return an interable of Strings of the top matches.
     */
    public Iterable<String> topMatches(String prefix, int k) {

        // Trying to find the k top matches for non-positive k. 

        if (k <= 0) {
            throw new IllegalArgumentException("K must be positive for topMatches.");
        }

        // Should prbly empty the stuff after every run too. Or empty before every run. 
        this.checkOut.clear();
        this.topResults.clear();
        this.numMaxSubWeights = 0;

        ACNode prefixChild = null;


        if (prefix == null) {
            throw new NullPointerException("Trying to find top matches for null prefix.");
        } else if (prefix.length() == 0) {
            prefixChild = this.allWords.root;
        } else {

            // CALL THE THING
            ACNode prefixNode = this.allWords.findNode(allWords.root, prefix, 0);


            // If no matching term:
            if (prefixNode == null) {
                return new ArrayList<String>(); // Return Iterable with no elements in it. 
            }

            // Else, found the node, so can descend.
            prefixChild = prefixNode.middle;

            if (prefixNode.ownWeight != null) {
                this.topResults.add(prefixNode);
            }
        }     

        // I HOPE THIS IS RIGHT I REALLY HOPE SO.

        modifiedTraversal(prefixChild, k);

        

        // THEN TAKE THE FIRST K ELEMENTS OF THE ARRAY.
        // ACNode[] allResults = this.topResults.toArray(new ACNode[1]);
        // ACNode[] kResults = Arrays.copyOfRange(allResults, 0, k); // Upper bound is exclusive. 

        // // THEN MAKE SURE TO CHANGE THEM ALL BACK INTO STRINGS - walk back up the trie. 
        // String[] finalResults = new String[k];
        // for (int i = 0; i < k; i = i + 1) {
        //     finalResults[i] = walkBackUp(kResults[i], "");
        // }
        // return finalResults;


        ArrayList<String> finalResults = new ArrayList<String>();

        
        int resultSize = this.topResults.size();
        int goTil = Math.min(k, resultSize);
        //System.out.println("go til " + goTil);
        for (int i = 0; i < goTil; i = i + 1) {
            ACNode currNode = this.topResults.get(i);
            finalResults.add(walkBackUp(currNode, new StringBuilder("")));
        }

        //System.out.println("final results size : " + finalResults.size());
        return finalResults;

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
     * then repeatedly reads autocomplete queries from std input 
     * and prints out the top k matching terms.
     * @param args takes name of an input file and an integer k as command-line arguments
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
            for (String term : autocomplete.topMatches(prefix, k)) {
                StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);
            }
        }
    }


    /**
     * Node class that stores all info for the TST.
     * @author Alice Tarng.
     */
    public class ACNode {

        Character c; 
        Double ownWeight;
        Double maxSubWeight;

        ACNode left;
        ACNode right;
        ACNode middle;

        ACNode parent;
    }


    /**
     * The TST data structure used for autocomplete.
     * @author Alice Tarng.
     */
    public class TST {


        ACNode root;

        
        /**
         * Constructor for the TST.
         */
        public TST() {
            this.root = new ACNode();
            this.root.parent = null;
        }



        // public boolean contains(String key) {
        //     return getOwnWeight(key) != null;
        // }


        /**
         * Returns weight of the key.
         * @param key the string whose information we want.
         * @return double of the key's weight.
         */
        public Double getOwnWeight(String key) {
            if (key == null) {
                //throw new NullPointerException("Tried to check if TST contains null string.");
                return null;
            }
            if (key.length() == 0) {
                throw new IllegalArgumentException("TST does not contain string of length 0.");
            }
            //ACNode x = get(this.root, key, 0); // Trying to get the Node for the end of the word. 
            ACNode x = findNode(this.root, key, 0);
            if (x == null) {
                return null;
            }
            return x.ownWeight;
        }


        /**
         * Returns endNode for the key. 
         * @param start which node we are starting from.
         * @param key the string whose end we are searching for.
         * @param currPos the current position in the string key we are looking for.
         * @return node that is the key's end. 
         */
        public ACNode findNode(ACNode start, String key, int currPos) {
            if (key == null) {
                throw new NullPointerException("In findNode, tried to get a null key.");
            }

            int keyL = key.length();

            if (keyL == 0 || currPos >= keyL || (start == null)) {
                return null;
            }

            Character startC = start.c;

            if ((currPos == keyL - 1) && (startC != null) && (startC == key.charAt(currPos))) {
                // This is the node. 
                return start;
            }
            
            Character currKeyChar = key.charAt(currPos);

            if (currKeyChar < startC) {
                return findNode(start.left, key, currPos);
            }
            if (currKeyChar > startC) {
                return findNode(start.right, key, currPos);
            }
            if (currPos < key.length() - 1) {
                return findNode(start.middle, key, currPos + 1);
            }
            return null; // I guess, to make Java compiler happy. 
        }



        /**
         * Returns node 
         * @param key the string whose information we want.
         * @return double of the key's weight.
         */
        /*public ACNode get(ACNode start, String key, int keyPosition) {
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
                    // Obstacle, turn left, 
                    // you haven't found the char at this position yet, so keep it there. 
                    return get(start.left, key, keyPosition);
                }
                if (currKeyChar > start.c) {
                    return get(start.right, key, keyPosition);
                }
                if (keyPosition < key.length() - 1) {
                    // You still have to move on; aren't looking for the end of the word yet. 
                    // Found the correct character! 
                    // So you can move on to the next character!
                    return get(start.middle, key, keyPosition + 1);
                }
            }
            // Okay, you've found the last character. Return the node you're currently on.
            return start;
        }*/

         
        /**
         * Puts the key and weight into the TST.
         * @param key the string we insert into the TST.
         * @param ownWeight the value associated with this key.
         */
        public void put(String key, Double ownWeight) {

            
            this.root = put(this.root, this.root.parent, key, ownWeight, 0);

            // TRYING NOW
            // Urgh okay no do maxSubweight AS YOU TRAVERSE. 
            // I WAS HEERRREEEE
            //this.root.maxSubWeight = subMaxWeight(this.root);

            // Okay I did a thing in put that hopefully does the max sub trie thing. Hopefully. 
        }


        /**
         * Returns node, self after all its information set.
         * @param start the node at which we start to traverse.
         * @param parent the node that will be the parent of this node.
         * @param key the string we're putting into the TST.
         * @param newWeight weight of this key.
         * @param keyPos the position in the key currrently being examined.
         * @return the node after all its info is set.
         */
        public ACNode put(ACNode start, ACNode parent, String key, Double newWeight, int keyPos) {

            int i = key.length();

            Character keyChar = key.charAt(keyPos);
            Double sw = null;
            if (start != null) {
                sw = start.ownWeight;
            }

            // Maybe I'll just do a check for contains here.
            if ((keyPos == i - 1) && (start != null) && (start.c == keyChar) && (sw != null)) {
                // THEN IT CONTAINS IT. GET MAD. 
                throw new IllegalArgumentException("Duplicate input terms.");
            }


            if (start == null || start.c == null) {
                start = new ACNode();
                start.c = keyChar;
            }

            if (keyChar < start.c) {
                
                start.left = put(start.left, parent, key, newWeight, keyPos);
                start.left.parent = parent;

            } else if (keyChar > start.c) {
                
                start.right = put(start.right, parent, key, newWeight, keyPos);
                start.right.parent = parent;

            } else if (keyPos < i - 1) {

                start.middle = put(start.middle, start, key, newWeight, keyPos + 1);
                start.middle.parent = start;
                
            } else {
                // I believe this is the final node. 

                start.ownWeight = newWeight;
                start.parent = parent;

            }

            // TRY DOING THE MAXSUBWEIGHT STUFF NOWWWWW
            // Compare all 3 children, 
            // your curr maxsubweight, your curr weight, 
            // Take the max, that's your maxsubweight. 
            Double maxWeight = 0.0; // Your weight can't be negative.
            // Your curr maxsubweight.
            if (start.maxSubWeight != null) {
                maxWeight = Math.max(maxWeight, start.maxSubWeight);
            }
            // Your own weight.
            if (start.ownWeight != null) {
                maxWeight = Math.max(maxWeight, start.ownWeight);
            }
            // COMPARE ALL CHILDREN.
            // Try left.
            if (start.left != null) {
                maxWeight = Math.max(maxWeight, start.left.maxSubWeight);
            }
            // Try right.
            if (start.right != null) {
                maxWeight = Math.max(maxWeight, start.right.maxSubWeight);
            }
            // Try middle.
            if (start.middle != null) {
                maxWeight = Math.max(maxWeight, start.middle.maxSubWeight);
            }
            start.maxSubWeight = maxWeight;
            return start;
        }


    }
}
