// Major thanks to the Algs textbook.
import java.lang.IllegalArgumentException;

public class TST {


    ACNode root;

    public class ACNode {

        Character c; 
        Double ownWeight;
        Double maxSubWeight;

        ACNode left;
        ACNode right;
        ACNode middle;

        ACNode parent;

        
    }

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
            //return null;
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

        if (this.contains(key)) {
            // According to the spec, throw an IllegalArgumentException if there are duplicate input terms.
            throw new IllegalArgumentException("Duplicate input terms.");
        }
        this.root = put(this.root, this.root.parent, key, ownWeight, 0);

        // Okay I did a thing in put that hopefully does the max sub trie thing. Hopefully. 
    }

    public ACNode put(ACNode start, ACNode startParent, String key, Double newWeight, int keyPosition) {
        // Parent is start's parent.
        //System.out.println("Inserting " + key);
        Character currKeyChar = key.charAt(keyPosition);
        if (start == null || start.c == null) {
            //System.out.println("Line 97");
            start = new ACNode();
            start.c = currKeyChar;
        }

        if (currKeyChar < start.c) {
            //System.out.println("Line 103");
            start.left = put(start.left, startParent, key, newWeight, keyPosition);
            start.left.parent = startParent;
            start.left.maxSubWeight = subMaxWeight(start.left);
        } else if (currKeyChar > start.c) {
            //System.out.println("Line 106");
            start.right = put(start.right, startParent, key, newWeight, keyPosition);
            start.right.parent = startParent;
            start.right.maxSubWeight = subMaxWeight(start.right);
        } else if (keyPosition < key.length() - 1) {
            //System.out.println("LIne 109");
            start.middle = put(start.middle, start, key, newWeight, keyPosition + 1);
            start.middle.parent = start;
            start.middle.maxSubWeight = subMaxWeight(start.middle);
        } else {
            // System.out.println("Line 112");
            //System.out.println("Line 113 Start is null: " + (start == null));
            start.ownWeight = newWeight;
            //System.out.println("line 115 start is null: " + (start == null));
            // DO SOMETHING ABOUT MAX SUB WEIGHT HERE. 
            start.maxSubWeight = subMaxWeight(start);
            start.parent = startParent;

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
