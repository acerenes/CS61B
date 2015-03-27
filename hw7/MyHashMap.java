import java.util.ArrayList;
import java.util.Set;

public class MyHashMap<K, V> implements Map61B<K, V> {

    ArrayList<Entry> map;
    int numBuckets;
    float reqLoad;
    int numMappings;
    boolean needToExpand;
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
            return Math.abs(this.key.hashCode());
        }
    }

    /* Constructors. */
    public MyHashMap() {
        this.map = new ArrayList<Entry>();
        numMappings = 0;
        needToExpand = false;
        needToRehash = false;
        reqLoad = (float) 0.75; 
        numBuckets = 10;
        for (int i = 0; i < numBuckets; i = i + 1) {
            map.add(i, null);
        }
    }

    public MyHashMap(int initialSize) {
        this.map = new ArrayList<Entry>(initialSize);
        numMappings = 0;
        needToExpand = false;
        needToRehash = false;
        reqLoad = (float) 0.75;
        numBuckets = initialSize;
        for (int i = 0; i < numBuckets; i = i + 1) {
            map.add(i, null);
        }
    }

    public MyHashMap(int initialSize, float loadFactor) {
        this.map = new ArrayList<Entry>(initialSize);
        this.reqLoad = loadFactor;
        numMappings = 0;
        needToExpand = false;
        needToRehash = false;
        numBuckets = initialSize;
        for (int i = 0; i < numBuckets; i = i + 1) {
            map.add(i, null);
        }
    }


    public void clear() {
        this.map = new ArrayList<Entry>();
        numMappings = 0;
        needToExpand = false;
        needToRehash = false;
        reqLoad = (float) 0.75; 
        numBuckets = 10;
        for (int i = 0; i < numBuckets; i = i + 1) {
            map.add(i, null);
        }
    }

    public boolean containsKey(K key) {
        if (needToRehash || needToExpand) {
            // Figure out how many buckets needed, then rehash.
            if (needToExpand) {
                numBuckets = (int) (numMappings / reqLoad) + 1; // +1 in case int round down.
            }
            rehashing(this.numBuckets);
            needToExpand = needExpand();
        }
        int index = index(key);
        if (!indexExists(index)) {
            return false;
        }
        Entry pointer = map.get(index);
        while (pointer != null) {
            if (pointer.key == key) {
                return true;
            }
            pointer = pointer.next;
        }
        return false;
    }

    public V get(K key) {
        if (needToRehash || needToExpand) {
            // Figure out how many buckets needed, then rehash.
            if (needToExpand) {
                numBuckets = (int) (numMappings / reqLoad) + 1; // +1 in case int round down.
            }
            rehashing(this.numBuckets);
            needToExpand = needExpand();
        }
        int lookingIndex = index(key);
        if (!indexExists(lookingIndex)) {
            return null;
        } else {
            Entry pointer = map.get(lookingIndex);
            while (pointer != null) {
                if (pointer.key == key) {
                    return pointer.value;
                }
                pointer = pointer.next;
            }
            return null; 
        }
    }
    
    /* I think this returns # elements mapped. */
    public int size() {
        return this.numMappings;
    }

    private int index(K key) {
        int newHashCode = Math.abs(key.hashCode());
        int lookingIndex = newHashCode % numBuckets;
        return lookingIndex;
    }

    private int index(Entry element) {
        int hashCode = element.hashCode();
        int returnindex = hashCode % numBuckets;
        return returnindex;
    }

    private boolean needExpand() {
        float currLoad = (float) this.size() / this.map.size();
        if (currLoad >= reqLoad) {
            return true;
        }
        return false;
    }

    private boolean indexExists(int index) {
        if (index >= map.size() || map.get(index) == null) {
            // No one's here.
            return false;
        }
        return true;
    }

    private void rehashing(int newNumBuckets) {
        // First create copy of old map to iterate over.
        ArrayList<Entry> oldMap = new ArrayList<Entry>(map.size());
        for (Entry oldElement : map) {
            if (oldElement != null) {
                oldMap.add(oldElement);
            }
        } 
        ArrayList<Entry> newMap = new ArrayList<Entry>(newNumBuckets);
        for (int i = 0; i < newNumBuckets; i = i + 1) {
            newMap.add(i, null);
        }
        /*map.ensureCapacity(newNumBuckets);*/
        for (Entry element : oldMap) {
            int newIndex = index(element);
            newMap.set(newIndex, element);
        }
        this.map = newMap;
        needToRehash = false;
    }

    private void putArrayList(Entry element, int indexToPut) {
        if (!indexExists(indexToPut)) {
            // No one's here, go right in. 
            map.add(indexToPut, element);
        } else {
            // Gotta attach to end of link list. 
            Entry pointer = map.get(indexToPut);
            while (pointer.next != null) {
                if (pointer.key == element.key) {
                    pointer.value = element.value;
                    return;
                }
                pointer = pointer.next;
            }
            pointer.next = element;
        }
    }


    public void put(K key, V value) {
        Entry info = new Entry(key, value);
        int index = index(info);
        putArrayList(info, index);
        numMappings = numMappings + 1;
        needToRehash = true;
    }

    
    public V remove(K key) {
        if (containsKey(key)) {
            int index = index(key);
            Entry prev = map.get(index);
            Entry curr = prev.next;
            if (prev.key == key) {
                V removedValue = prev.value;
                map.set(index, curr);
                numMappings = numMappings - 1;
                return removedValue;
            } else { 
                while (prev != null) {
                    if (curr.key == key) {
                        V removedValue = curr.value;
                        prev.next = curr.next;
                        numMappings = numMappings - 1;
                        return removedValue; 
                    }
                }
            }
        } 
        return null;
    }

    public V remove(K key, V value) {
        if (containsKey(key)) {
            if (get(key) != value) {
                return null;
            }
            return remove(key);
        }
        return null; // This key doesn't exist.

    }

    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }
}
