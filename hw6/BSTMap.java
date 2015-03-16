

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

    


} 