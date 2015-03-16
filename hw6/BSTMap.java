

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root;


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

    @Override
    public V get(K key) {
        return get(this.root, key);
    }
    private static V get(Node start, K key) {
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
        SOMEETHTHININIGNIGNING HERREEEEEE
    }
    private 


}

/* So much millions of thanks to the Algs book. */
