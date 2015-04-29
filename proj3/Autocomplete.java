/**
 * Implements autocomplete on prefixes for a given dictionary of terms and weights.
 */

// LIDRUHGLDIRUHGLDSIHLIU ALICE TAKE OUT TINY.TXT OKAY OKAY

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.lang.IllegalArgumentException;
import java.util.Arrays;
import java.util.ArrayList;


public class Autocomplete {

    TST allWords;
    PriorityQueue<ACNode> checkOut;
    //LinkedList<ACNode> topResults; // Should contain a bunch of nodes.
    ArrayList<ACNode> topResults; 

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
            // DARN IT THE PQ HAS TO ORDER BY MAX SUB WEIGHT THO. LSDFIUGHDLSIUGHDSLIUGHDSLRIGHSDLRIUGHDSLIRUGHLDIRSGHLDSIGUHDLRIGHDLIGHDLIGHDLSIU
            // Darn it I have to write a comparator.
            // I DON'T WANNA WRITE A COMPARATOR. 
        checkOut = new PriorityQueue<ACNode>(1, new NodeComparator()); // Default initial capacity is 11. So. Yeah.
        // And a list or smth???? 
        //topResults = new LinkedList<ACNode>();
        topResults = new ArrayList<ACNode>();


        

            
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
            // Priority Queues implement by least first tho.
            // Switching it around so I can do most first. 
            return (int) (node2.maxSubWeight - node1.maxSubWeight);
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
        
        ACNode prefixNode = this.allWords.findNode(allWords.root, prefix, 0);

        // If no matching term:
        if (prefixNode == null) {
            return null;
        }

        //System.out.println("Prefix node's character: " + prefixNode.c);
        // Oh fml I think I know the problem. I go the whole way down, without checking if it's part of the word. DARN IT. 

        // Else, you found the node, so you can descend.
        ACNode prefixChild = prefixNode.middle;

        // i HOPE THIS IS RIGHT I'M JUST GOING OFF THE DRAWING AT THIS POINT. 
        ACNode endHighestNode = highestNode(prefixChild);
        //ACNode endHighestNode = highestNode(prefixNode);
        //System.out.println("endHighestNode's character: " + endHighestNode.c );

        // Maybe I'll just compare prefix child and self. 


        // if (endHighestNode == null) {
        //     // No matching term, I believe. 
        //     return null;
        // }

        ACNode theHighNode = prefixNode;
        //System.out.println("prefix Node's own Weight: " + prefixNode.ownWeight);

        if ((prefixNode.ownWeight == null) || (endHighestNode != null && (endHighestNode.ownWeight > prefixNode.ownWeight))) {

            //System.out.println("AM I EVEN GOING IN HERE");
            theHighNode = endHighestNode;
        }


        // Else, gonna have to do a walking back up the trie...
        return walkBackUp(theHighNode, new StringBuilder(""));


    }

    public String walkBackUp(ACNode start, StringBuilder soFar) {
        if (start == null) {
            // I'm going to assume you're never going to call this on a null starting node, cause that's just too hard. 
            // So I'm going to assume this is, you got all the way up, to no more parent. 
            return soFar.toString();
        }
        return walkBackUp(start.parent, soFar.insert(0, start.c));
    }

    public ACNode highestNode(ACNode start) {

        if (start == null) {
            return null;
        }

        Double lookForThisWeight = start.maxSubWeight;
        //System.out.println("start.maxsubweight is null: " + (start.maxSubWeight == null));

        //System.out.println("start.ownWeight is null: " + (start.ownWeight == null));

        if (start.ownWeight != null && start.ownWeight.equals(lookForThisWeight)) {
            return start;
        }

        // Otherwise, it's gotta be in its left, middle, or right. 
        if ((start.left != null) && start.left.maxSubWeight.equals(lookForThisWeight)) {
            return highestNode(start.left);
        }
        if ((start.right != null) && start.right.maxSubWeight.equals(lookForThisWeight)) {
            return highestNode(start.right);
        }
        return highestNode(start.middle);
    }



    public void modifiedTraversal(ACNode start, int numMatches) {
        if (start == null) {
            return;
        }

        // Check to make sure list is within size limits. 
        // if (this.topResults.size() > numMatches) {
        //     return;
        // }

        // Checking that kth heaviest thing. 
        //WAIT GO BACK HERE
        if ((this.topResults.size() == numMatches) && (start.ownWeight != null)) {
            //if (start.ownWeight <= topResults.getLast().ownWeight) {
            // if (start.ownWeight <= topResults.get(topResults.size() - 1).ownWeight) {
            //     // Can terminate search!
            //     return;
            // }

            if (start.ownWeight <= topResults.get(numMatches - 1).ownWeight) {
                return;
            }

            //while (this.topResults.getLast().ownWeight <= start.ownWeight) {
            // Try an indexing thing instead.
            int k = this.topResults.size() - 1;
            while (this.topResults.get(k).ownWeight < start.ownWeight) {
                k = k - 1;
            }
            //System.out.println("Line 179 adding node with weight " + start.ownWeight);
            topResults.add(k, start);


            /*while (this.topResults.get(this.topResults.size() -1).ownWeight <= start.ownWeight) {
                System.out.println("LIne 169 DO I EVER EVEN GO IN HERE WHERE I'M TRYING TO scootch all the stuff over");
                //topResults.removeLast();
                topResults.remove(topResults.size() - 1);
            }
            topResults.add(start);*/
        }


        if (start.ownWeight != null) {
            int i = this.topResults.size();
            //System.out.println("i = " + i);

            if (i == 0) {
                //System.out.println("Line 196 adding node with weight " + start.ownWeight);
                this.topResults.add(start);
            } else if (i == 1) {
                //if (this.topResults.getLast().ownWeight < start.ownWeight) {
                if (this.topResults.get(0).ownWeight < start.ownWeight) {
                    //this.topResults.addFirst(start);
                    //System.out.println("Line 202 adding node, to front, with weight " + start.ownWeight);
                    this.topResults.add(0, start);
                } else {
                    //this.topResults.addLast(start);
                    //System.out.println("Line 206 adding node with weight " + start.ownWeight);
                    this.topResults.add(start);
                }
            } else {

                int j = i - 1; // Because 0 indexing.
                

                while (j >= 0 && this.topResults.get(j).ownWeight < start.ownWeight) {
                    //System.out.println("Line 215, j = " + j);
                    j = j - 1;
                }

                if (j == i - 1) {
                    // Never moved the pointer, so stick onto last.
                    //this.topResults.addLast(start);
                    //System.out.println("Line 221 added node with weight " + start.ownWeight);
                    this.topResults.add(start);
                } else {
                    //System.out.println("Line 224 added node with weight " + start.ownWeight + " in position " + j + 1);
                    //this.topResults.add(j, start);
                    this.topResults.add(j + 1, start);
                }
                //this.topResults.add(i, start);
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
     * @param prefix
     * @param k
     * @return
     */
    public Iterable<String> topMatches(String prefix, int k) {

        // Trying to find the k top matches for non-positive k. 

        if (k <= 0) {
            throw new IllegalArgumentException("Trying to find the k top matches for non-positive k.");
        }

        // Should prbly empty the stuff after every run too. Or empty before every run. 
        this.checkOut.clear();
        this.topResults.clear();

        // CALL THE THING
        ACNode prefixNode = this.allWords.findNode(allWords.root, prefix, 0);

        // If no matching term:
        if (prefixNode == null) {
            return null;
        }

        if (prefixNode.ownWeight != null) {
            this.topResults.add(prefixNode);
        }

        // Else, found the node, so can descend.
        ACNode prefixChild = prefixNode.middle;

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
        for (int i = 0; i < k && i < resultSize; i = i + 1) {
            ACNode currNode = this.topResults.get(i);
            //System.out.println(walkBackUp(currNode, "") + " weight" + currNode.ownWeight);
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

            ACNode parent;
        }


    public class TST {


        ACNode root;

        

        public TST() {
            this.root = new ACNode();
            this.root.parent = null;
        }


        public boolean contains(String key) {
            return getOwnWeight(key) != null;
        }

        public Double getOwnWeight(String key) {
            if (key == null) {
                //throw new NullPointerException("Tried to check if TST contains null string.");
                return null;
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

        public ACNode findNode(ACNode start, String key, int currPosition) {
            if (key == null) {
                throw new NullPointerException("In findNode, tried to get a null key.");
            }
            if (key.length() == 0) {
                //throw new IllegalArgumentException("In findNode, tried to get a key with length 0.");
                return null;
            }
            if (currPosition >= key.length() || (start == null)) {
                // Overshot it - it doesn't exist.
                return null;
            }

            if (currPosition == key.length() - 1) {
                // This is the node. 
                if ((start.c != null) && (start.c == key.charAt(currPosition))) {
                    return start;
                }
                // Gotta make sure the last character is right though.
                // return null; Don't want to actually return null, because you can keep checking. Let the rest of the code take care of that. 
            }
            
            Character currKeyChar = key.charAt(currPosition);

            if (currKeyChar < start.c) {
                return findNode(start.left, key, currPosition);
            }
            if (currKeyChar > start.c) {
                return findNode(start.right, key, currPosition);
            }
            if (currPosition < key.length() - 1) {
                return findNode(start.middle, key, currPosition + 1);
            }
            return null; // I guess, to make Java compiler happy. 
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

            /*if (this.contains(key)) {
                // According to the spec, throw an IllegalArgumentException if there are duplicate input terms.
                throw new IllegalArgumentException("Duplicate input terms.");
            }*/
            this.root = put(this.root, this.root.parent, key, ownWeight, 0);

            // TRYING NOW
            // Urgh okay no do maxSubweight AS YOU TRAVERSE. 
            // I WAS HEERRREEEE
            //this.root.maxSubWeight = subMaxWeight(this.root);

            // Okay I did a thing in put that hopefully does the max sub trie thing. Hopefully. 
        }

        public ACNode put(ACNode start, ACNode startParent, String key, Double newWeight, int keyPosition) {
            //System.out.println("Inserting " + key);
            Character currKeyChar = key.charAt(keyPosition);

            // Maybe I'll just do a check for contains here.
            if ((keyPosition == key.length() - 1) && (start != null) && (start.c == currKeyChar) && (start.ownWeight != null)) {
                // THEN IT CONTAINS IT. GET MAD. 
                throw new IllegalArgumentException("Duplicate input terms.");
            }


            if (start == null || start.c == null) {
                //System.out.println("Line 97");
                start = new ACNode();
                start.c = currKeyChar;
            }

            if (currKeyChar < start.c) {
                //System.out.println("Line 103");
                
                start.left = put(start.left, startParent, key, newWeight, keyPosition);
                start.left.parent = startParent;
                //start.left.maxSubWeight = subMaxWeight(start.left);
            } else if (currKeyChar > start.c) {
                //System.out.println("Line 106");
                
                start.right = put(start.right, startParent, key, newWeight, keyPosition);
                start.right.parent = startParent;
                //start.right.maxSubWeight = subMaxWeight(start.right);
            } else if (keyPosition < key.length() - 1) {
                //System.out.println("LIne 109");
                start.middle = put(start.middle, start, key, newWeight, keyPosition + 1);
                start.middle.parent = start;
                //start.middle.maxSubWeight = subMaxWeight(start.middle);
            } else {
                // I believe this is the final node. 

                // System.out.println("Line 112");
                //System.out.println("Line 113 Start is null: " + (start == null));
                start.ownWeight = newWeight;
                //System.out.println("line 115 start is null: " + (start == null));
                // DO SOMETHING ABOUT MAX SUB WEIGHT HERE. 
                //start.maxSubWeight = subMaxWeight(start);
                start.parent = startParent;

            }

            // TRY DOING THE MAXSUBWEIGHT STUFF NOWWWWW
            // Compare all 3 children, your curr maxsubweight, your curr weight, and the current thing you're inserting's weight (jk that's yourself). Take the max, that's your maxsubweight. 
            Double maxWeight = (double) 0; // Your weight can't be negative.
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


        /*// Changing it as I go too.....
        public Double subMaxWeight(ACNode start) {

            // What if I start from bottom
            // WAIT WHAT IF I SET THE SUBMAX WEIGHT ONLY WHEN I'M DONE WITH THE WORD. 

            //System.out.println("Line 126 Start is null: " + (start == null));
            if (start == null) {
                //System.out.println("Line 128");
                //System.out.println("Line 129 Start is null: " + (start == null));
                //System.out.println("Start.maxSubWeight = " + start.maxSubWeight);
                //start.maxSubWeight = (double) 0;
                return (double) 0;

            } else if (start.left == null && start.right == null && start.middle == null) {
                //System.out.println("How often do you just take your own weight; letter = " + start.c);
                start.maxSubWeight = start.ownWeight;

            } else {
                //System.out.println("How often do you actually have to calculate / compare; letter = " + start.c);
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
        }*/

    }
}
