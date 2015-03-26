import java.util.ArrayList;
import java.util.Set;

public class MyHashMap<K, V> implements Map61B<K, V> {

    ArrayList<Entry> map;
    int numBuckets;
    float reqLoad;
    int numMappings;
    boolean needToRehash;


    private class Entry {

        private K key;
        private V value;
        private Entry next;

        private Entry(K k, V val) {
            this.key = k;
            this.value = val;
        }

        @Override
        public int hashCode() {
            return this.key.hashCode();
        }
    }

    /* Constructors. */
    public MyHashMap() {
        this.map = new ArrayList<Entry>();
        numMappings = 0;
        needToRehash = false;
        reqLoad = (float) 0.75; 
        numBuckets = 10;
    }

    public MyHashMap(int initialSize) {
        this.map = new ArrayList<Entry>(initialSize);
        numMappings = 0;
        needToRehash = false;
        reqLoad = (float) 0.75;
        numBuckets = initialSize;
    }

    public MyHashMap(int initialSize, float loadFactor) {
        this.map = new ArrayList<Entry>(initialSize);
        this.reqLoad = loadFactor;
        numMappings = 0;
        needToRehash = false;
        numBuckets = initialSize;
    }




    public void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean containsKey(K key) {
        throw new UnsupportedOperationException();
    }

    public V get(K key) {
        throw new UnsupportedOperationException();
    }
    
    /* I think this returns # elements mapped. */
    public int size() {
        return this.numMappings;
    }

    
    public void put(K key, V value) {
        Entry info = new Entry(key, value);
        int entryHashCode = info.hashCode();
        int index = Math.abs(entryHashCode % numBuckets);
        if (index >= map.size() || map.get(index) == null) {
            // No one's here, go right in. 
            map.add(index, info);
        } else {
            // Gotta attach to end of link list. 
            Entry pointer = map.get(index);
            while (pointer.next != null) {
                pointer = pointer.next;
            }
            pointer.next = info;
        }
        numMappings = numMappings + 1;
        float currLoad = (float) this.size() / this.map.size();
        if (currLoad >= reqLoad) {
            needToRehash = true;
        }
    }

    
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }
}
