import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;
    // Making Nodes do all the work.


    private class Node {

        private K key;
        private V value;
        private Node left; // Should be default null. 
        private Node right;
        private int size;

        private Node(K key, V value, int size) {
            this.key = key;
            this.value = value;
            this.size = size;
        }
    }

    /* All the static-ness needs to match up. 
        * V & K are non static - can't use a static method on it. 
        * Non-static type variable cannot be referenced from a static context.
        * SOMETHING LIKE THAT. 
        * Also these methods depend on the instance of the class - can't be static. */
    @Override
    public V get(K key) {
        return get(this.root, key);
    }

    private V get(Node start, K key) {
        if (start == null) {
            return null;
        }
        int cmp = key.compareTo(start.key);
        if (cmp < 0) { // Need to go smaller. 
            return get(start.left, key);
        }
        else if (cmp > 0) { // Need to go bigger.
            return get(start.right, key);
        }
        else { // Found it.
            return start.value;
        }

    }


    @Override
    public void put(K key, V value) {
        if (key == null) {
            return;
        }
        this.root = put(this.root, key, value);
    }

    private Node put(Node start, K key, V value) {
        // I think you have to return the new "root".
        if (start == null) {
            return new Node(key, value, 1);
        }
        int cmp = key.compareTo(start.key);
        if (cmp < 0) {
            // Move left.
            start.left = put(start.left, key, value);
        }
        else if (cmp > 0) {
            start.right = put(start.right, key, value);
        }
        else {
            // Update the node.
            start.value = value;
        }
        start.size = 1 + size(start.left) + size(start.right);
        return start;
    }



    @Override
    public int size() {
        return size(this.root);
    }
    private int size(Node r) {
        if (r == null) {
            return 0;
        }
        return r.size;
    }



    @Override
    public boolean containsKey(K key) {
        return containsKey(this.root, key);
    }

    private boolean containsKey(Node start, K key) {
        if (start == null) {
            return false;
        }
        int cmp = key.compareTo(start.key);
        if (cmp < 0) {
            return containsKey(start.left, key);
        }
        else if (cmp > 0) {
            return containsKey(start.right, key);
        }
        else {
            // Found it!
            return true;
        }
    }



    
    @Override
    public void clear() {
        this.root = null;
    }



    public void printInOrder() {
        printInOrder(this.root);
        
    }
    private void printInOrder(Node start) {
        if (start != null) {
            printInOrder(start.left);
            // Once all left stuff is done: 
            System.out.println(start.key + " : " + start.value);
            printInOrder(start.right);
            // Then go back up the level, to the frame you never finished.
        }
        /* It's good to draw a pic for this. 
            * Go down left all the way. 
            * Once the left branch is null, the function won't do anything.
            * So like, go back up a level. 
            * Print out your stuff, print out right stuff. 
            * And just keep going back up the levels. */

        /* Thanks StackOverflow for the idea. */
    }
    
    


    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }


}

/* So much millions of thanks to the Algs book. */
