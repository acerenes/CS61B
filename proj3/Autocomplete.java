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

            if ((prefixNode.ownWeight == null) || (endHighestNode != null && (endHighestNode.ownWeight > prefixNode.ownWeight))) {

                prefixNode = endHighestNode;
            }
        }


        return walkBackUp(prefixNode, new StringBuilder(""));


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



    public void modifiedTraversal(ACNode start, int numMatches) {
        //System.out.println("Calling modified traversal");
        if (start == null) {
            return;
        }

        // Maybe have a count of how many submaxweights you've looked at.
        // If you've looked at more, then just stop. 

        // What if, you just don't take the node out of the PQ until you put it in the list. 
        // And don't add unless your weight = your max subweight, or your max subweight is = the max subweight of the last thing in the list? 
            // Because it's only if you're an "along the way" node - 
                // If your max subweight is less, you should grab the one whose max subweight is their weight first.
                // If greater, WHY AREN'T YOU ALREADY IN THE QUEUE. 

        //Check to make sure list is within size limits. 


        if ((this.topResults.size() > numMatches) && ((this.numMaxSubWeights > numMatches) || (start.maxSubWeight < topResults.get(topResults.size() - 1).ownWeight))) {
            return;
        }


        // How on earth did buried get added 2x???
        // I think it's cause you add yourself to topResults, but also to the queue. 
        // URGGGHHHH

        // Checking that kth heaviest thing. 
        //WAIT GO BACK HERE
        if ((this.topResults.size() == numMatches) && (start.ownWeight != null) && (start.ownWeight > topResults.get(topResults.size() - 1).ownWeight)) {
            //System.out.println("Checking kth thing - " + start.ownWeight);

            //if (start.ownWeight <= topResults.getLast().ownWeight) {
            // if (start.ownWeight <= topResults.get(topResults.size() - 1).ownWeight) {
            //     // Can terminate search!
            //     return;
            // }

            // Check to make sure you're not already in topResults already, maybe.
            // It's not this. Because it's not going in here.


            // THIS HERE USED TO BE HERE. 
            /*if ((start.ownWeight <= topResults.get(numMatches - 1).ownWeight)) {
                return;
            }*/


            if (!topResults.contains(start)) { 
                int m = this.topResults.size() - 1;   
                int k = m;
                while (this.topResults.get(k).ownWeight < start.ownWeight) {
                    k = k - 1;
                }
                //System.out.println("removing node with weight " + topResults.get(topResults.size() - 1).ownWeight);

                topResults.remove((topResults.size()) - 1); // Remove the last thing? 
                if (k == m) {
                    // DIdn't move, so add to end.
                    //System.out.println("Line 216 Added to end: " + start.ownWeight);
                    topResults.add(start);
                } else {
                    //System.out.println("Line 219 Added to position " + (k + 1) + " weight " + start.ownWeight);
                    topResults.add(k + 1, start);
                }

                if (start.ownWeight.equals(start.maxSubWeight)) {
                    this.numMaxSubWeights = this.numMaxSubWeights + 1;
                }
            }


            /*while (this.topResults.get(this.topResults.size() -1).ownWeight <= start.ownWeight) {
                System.out.println("LIne 169 DO I EVER EVEN GO IN HERE WHERE I'M TRYING TO scootch all the stuff over");
                //topResults.removeLast();
                topResults.remove(topResults.size() - 1);
            }
            topResults.add(start);*/
        } else if (start.ownWeight != null && !topResults.contains(start)) {
            int i = this.topResults.size();
            //System.out.println("i = " + i);

            if (i == 0) {
                //System.out.println("Line 236 adding node to end  with weight " + start.ownWeight);
                this.topResults.add(start);
            } else if (i == 1) {
                //if (this.topResults.getLast().ownWeight < start.ownWeight) {
                if (this.topResults.get(0).ownWeight < start.ownWeight) {
                    //this.topResults.addFirst(start);
                    //System.out.println("Line 242 adding node, to front, with weight " + start.ownWeight);
                    this.topResults.add(0, start);
                } else {
                    //this.topResults.addLast(start);
                    //System.out.println("Line 246 adding node to end with weight " + start.ownWeight);
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
                    //System.out.println("Line 263 start weight = " + start.ownWeight + " being added to end");
                    this.topResults.add(start);
                } else {
                    //System.out.println("Line 224 added node with weight " + start.ownWeight + " in position " + j + 1);
                    //this.topResults.add(j, start);
                    //System.out.println("Line 268 start weight = " + start.ownWeight + " at position " + (j + 1));
                    this.topResults.add(j + 1, start);
                }
                //this.topResults.add(i, start);
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
        //System.out.print("top results length: " + topResults.size());
        //System.out.println("num matches: " + numMatches);
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
            //System.out.println(walkBackUp(currNode, new StringBuilder("")) + " weight" + currNode.ownWeight);
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
            if (key.length() == 0 || currPosition >= key.length() || (start == null)) {
                return null;
            }

            Character startC = start.c;

            if ((currPosition == key.length() - 1) && (startC != null) && (startC == key.charAt(currPosition))) {
                // This is the node. 
                return start;
            }
            
            Character currKeyChar = key.charAt(currPosition);

            if (currKeyChar < startC) {
                return findNode(start.left, key, currPosition);
            }
            if (currKeyChar > startC) {
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

            int i = key.length();

            Character currKeyChar = key.charAt(keyPosition);

            // Maybe I'll just do a check for contains here.
            if ((keyPosition == i - 1) && (start != null) && (start.c == currKeyChar) && (start.ownWeight != null)) {
                // THEN IT CONTAINS IT. GET MAD. 
                throw new IllegalArgumentException("Duplicate input terms.");
            }


            if (start == null || start.c == null) {
                start = new ACNode();
                start.c = currKeyChar;
            }

            if (currKeyChar < start.c) {
                
                start.left = put(start.left, startParent, key, newWeight, keyPosition);
                start.left.parent = startParent;

            } else if (currKeyChar > start.c) {
                
                start.right = put(start.right, startParent, key, newWeight, keyPosition);
                start.right.parent = startParent;

            } else if (keyPosition < i - 1) {

                start.middle = put(start.middle, start, key, newWeight, keyPosition + 1);
                start.middle.parent = start;
                
            } else {
                // I believe this is the final node. 

                start.ownWeight = newWeight;
                start.parent = startParent;

            }

            // TRY DOING THE MAXSUBWEIGHT STUFF NOWWWWW
            // Compare all 3 children, your curr maxsubweight, your curr weight, and the current thing you're inserting's weight (jk that's yourself). Take the max, that's your maxsubweight. 
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
