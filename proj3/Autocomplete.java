/**
 * Implements autocomplete on prefixes for a given dictionary of terms and weights.
 */

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.lang.IllegalArgumentException;
import java.util.Arrays;


public class Autocomplete {

    TST allWords;
    PriorityQueue<ACNode> checkOut;
    LinkedList<ACNode> topResults; // Should contain a bunch of nodes. 

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
        checkOut = new PriorityQueue<ACNode>(1, new NodeComparator()); // Default initial capacity is 11. So. Yeah.
        // And a list or smth???? 
        topResults = new LinkedList<ACNode>();


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

        // Else, you found the node, so you can descend.
        ACNode prefixChild = prefixNode.middle;

        // i HOPE THIS IS RIGHT I'M JUST GOING OFF THE DRAWING AT THIS POINT. 
        ACNode endHighestNode = highestNode(prefixChild);

        if (endHighestNode == null) {
            // No matching term, I believe. 
            return null;
        }
        // Else, gonna have to do a walking back up the trie...
        return walkBackUp(endHighestNode, "");


    }

    public String walkBackUp(ACNode start, String soFar) {
        if (start == null) {
            // I'm going to assume you're never going to call this on a null starting node, cause that's just too hard. 
            // So I'm going to assume this is, you got all the way up, to no more parent. 
            return soFar;
        }
        return walkBackUp(start.parent, start.c + soFar);
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
        //System.out.println("Starting modified traversal on " + start);
        // if (start != null) {
        //     System.out.println("Starting modified traversal: node weight " + start.ownWeight);
        // }

        // Okay maybe how I'll do it, is stop when the topResults hits a certain length. So for top Match, go till it's length 1?

        // NO. you gotta do the thing they say in the spec. If the thing in the PQ is less than or equal to the weight of the kth heaviest term in the list. 

        if (start == null) {
            return;
        }

        // Check to make sure list is within size limits. 
        if (this.topResults.size() > numMatches) {
            return;
        }

        // Checking that kth heaviest thing. 
        if ((this.topResults.size() == numMatches) && (start.ownWeight != null)) {
            if (start.ownWeight <= topResults.getLast().ownWeight) {
                // Can terminate search!
                return;
            }
            // Else, you'd have to replace it... I dunno when you'd do this though, because ... you add to the list in order! So.... I'll just say replace the last one... and change it later if necessary.

            // JK I'M BACK. NEED TO MOVE IT ALL THE WAY.
            //int i = this.topResults.size() - 1; 
            while (this.topResults.getLast().ownWeight <= start.ownWeight) {
                //System.out.println("LIne 171 DO I EVER EVEN GO IN HERE");
                topResults.removeLast();
            }
            topResults.add(start);
        }

        // OKay my comparator is based on max subweight. 
        // Stop when 
        // Okay you take it out, and then put in all its children, and then do it again. 
        // Stop when The node you're on has its own weight?
        // Then put the node into the list of topResults. And keep traversing, I guess. 

        // YEAAHHH???

        if (start.ownWeight != null) {
            // Add yourself to the list, yeah?
            // System.out.println("Line 191 trying to add start to list.");
            // System.out.println("start.ownWeight = " + start.ownWeight);
            // Maybe I should add yourself in order.
            //System.out.println("LINE 192 SUSPICIOUSSSS");
            int i = this.topResults.size();

            if (i == 0) {
                //System.out.println("In i = 0 case");
                this.topResults.add(start);
            } else if (i == 1) {
                //System.out.println("In i = 1 case");
                // Check if greater or less than the current thing in there. 
                if (this.topResults.getLast().ownWeight < start.ownWeight) {
                    //System.out.println("i = 1, start took over new head.");
                    // Then start takes over new head. 
                    this.topResults.addFirst(start);
                } else {
                    // Stick to end.
                    //System.out.println("in i = 1, start stuck into end");
                    this.topResults.addLast(start);
                }
            } else {

                //System.out.println("Am in in line 208 at all?");
                //System.out.println("Gotta do some actual checking");

                int j = i - 1; // Because 0 indexing.
                //System.out.println("Doing actual checking - j = " + j);

                while (j > 0 && this.topResults.get(j).ownWeight < start.ownWeight) {
                    //System.out.println("Am I in here? Line 220");
                    j = j - 1;
                }

                if (j == i - 1) {
                    // Never moved the pointer, so stick onto last.
                    this.topResults.addLast(start);
                } else {
                    // Wait but I think the indexing is off by 1.
                    // Jk no I think it's good. 
                    this.topResults.add(j, start);
                }
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

        // Okay, so have added all children to priority queue.
        // So check out the first one. 

        // HOLD UP.
        // IF THE THING YOU'RE CHECKING OUT RIGHT NOW WORKS, wait, does this work? Because you start out calling it on just the direct child... so you're not neceesarily already traversing in order...
        // Wait I think this should be covered by the testing kth element thing. I believe so. Okay can just carry on then I think. I hope. 

        

        // Then uh....do the thing...on the first thing in the priority queue?
        // WAIT SHOULD YOU TAKE YOURSELF OUT OF THE PRIORITY QUEUE THO??? 
        // Yeah. Why not. 
        // System.out.println("Going to do modifiedTraversal on node " + this.checkOut.poll());

        // if (this.checkOut.poll() != null) {
        //     System.out.println("Going to do modifiedTraversal on node with weight " + this.checkOut.poll().ownWeight);
        // }
        // if (this.checkOut.poll() != null) {
        //     System.out.println("Calling modified Traversal on node with char " + this.checkOut.poll().c);
        // }
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


        LinkedList<String> finalResults = new LinkedList<String>();
        int resultSize = this.topResults.size();
        for (int i = 0; i < k && i < resultSize; i = i + 1) {
            ACNode currNode = this.topResults.get(i);
            //System.out.println(walkBackUp(currNode, "") + " weight" + currNode.ownWeight);
            finalResults.add(walkBackUp(currNode, ""));
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

        public ACNode findNode(ACNode start, String key, int currPosition) {
            if (key == null) {
                throw new NullPointerException("In findNode, tried to get a null key.");
            }
            if (key.length() == 0) {
                throw new IllegalArgumentException("In findNode, tried to get a key with length 0.");
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
            this.root.maxSubWeight = subMaxWeight(this.root);

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
            return start;
        }


        // Changing it as I go too.....
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
        }

    }
}
