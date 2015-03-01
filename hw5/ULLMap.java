import java.util.Set; /* java.util.Set needed only for challenge problem. */
import java.util.Iterator; 
import java.lang.Iterable;

/** A data structure that uses a linked list to store pairs of keys and values.
 *  Any key must appear at most once in the dictionary, but values may appear multiple
 *  times. Supports get(key), put(key, value), and contains(key) methods. The value
 *  associated to a key is the value in the last call to put with that key. 
 *
 *  For simplicity, you may assume that nobody ever inserts a null key or value
 *  into your map.
 */ 
public class ULLMap<K, V> implements Map61B<K, V>, Iterable<K> { //FIX ME
    /** Keys and values are stored in a linked list of Entry objects.
      * This variable stores the first pair in this linked list. You may
      * point this at a sentinel node, or use it as a the actual front item
      * of the linked list. 
      */
    private Entry front;
    private int size = 0;

    @Override
    public V get(K key) { //FIX ME
    //FILL ME IN
        if (this.containsKey(key)) {
            return this.front.get(key).val;
        }
        return null;
    }

    @Override
    public void put(K key, V val) { //FIX ME
    //FILL ME IN
        if (this.containsKey(key)) {
            // Find where the key is
            Entry pointer = this.front; 
            while (pointer.key != key) {
                pointer = pointer.next;
            }
            // Found the key- change the value
            pointer.val = val;
            return;
        }
        this.front = new Entry(key, val, this.front); // Don't care about order, so stick in front
        this.size = this.size + 1;
    }

    @Override
    public boolean containsKey(K key) { //FIX ME
    //FILL ME IN
        Entry pointer = front; // Using a pointer because the method doesn't take in front, so don't want to actually change front.
        while (pointer != null) {
            if (pointer.key.equals(key)) {
                return true; 
            }
            pointer = pointer.next;
        }
        return false; //FIX ME
    }

    @Override
    public int size() {
        return this.size; // FIX ME (you can add extra instance variables if you want)
    }

    @Override
    public void clear() {
    //FILL ME IN
        front = null;
    }

    

    public static <V, K> ULLMap<V, K> invert(ULLMap<K, V> map) {
        ULLMap<V, K> inversed_map = new ULLMap<V, K>();
        for (K keys : map) { // map has to be an iterable; and K keys will iterate through the K of map (I think)
            K new_value = keys; // old key
            V new_key = map.get(keys); // old value
            inversed_map.put(new_key, new_value);
        }
        return inversed_map;
    }

    /** Represents one node in the linked list that stores the key-value pairs
     *  in the dictionary. */
    private class Entry { // don't do Entry<K, V> because nested non-static class: accesses instance variables&methods of outside
    
        /** Stores KEY as the key in this key-value pair, VAL as the value, and
         *  NEXT as the next node in the linked list. */
        public Entry(K k, V v, Entry n) { //FIX ME
            key = k;
            val = v;
            next = n;
        }

        /** Returns the Entry in this linked list of key-value pairs whose key
         *  is equal to KEY, or null if no such Entry exists. */
        public Entry get(K k) { //FIX ME
            //FILL ME IN (using equals, not ==)
            Entry pointer = this; 
            while (pointer != null) {
                if (pointer.key.equals(k)) {
                    return pointer;
                }
                pointer = pointer.next;
            }
            return null; //FIX ME
        }

        /** Stores the key of the key-value pair of this node in the list. */
        private K key; //FIX ME
        /** Stores the value of the key-value pair of this node in the list. */
        private V val; //FIX ME
        /** Stores the next Entry in the linked list. */
        private Entry next;
    
    }

    /* Methods below are all challenge problems. Will not be graded in any way. 
     * Autograder will not test these. */
    @Override
    public V remove(K key) { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() { //FIX ME SO I COMPILE
        throw new UnsupportedOperationException(); 
    }

    
    public ULLMapIter iterator() {
        return new ULLMapIter(this);
    }


    private class ULLMapIter implements Iterator<K> {
 
        private Entry iter; 

        public ULLMapIter(ULLMap<K, V> map) {
            iter = map.front;
        }

        public boolean hasNext() {
            return iter != null; 
        }

        public K next() {
            K old_key = iter.key;
            iter = iter.next;
            return old_key;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }


    }


}