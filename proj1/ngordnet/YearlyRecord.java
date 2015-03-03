package ngordnet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

/* Okay I'm thinking 2 maps? 
 * One for word v. count.
 * Can have a size instance variable.
 * Then rank, something with an loop like Dis 6.3.1, create new map.  
   * But how to do the actual ranking? 
    * Maybe iterate through all the keys. 
      * If you pass one that is more common than you, plus 1. (Start at 1.)
      * But how deal with tiebreakers? 
        * Alphabetical order - u're b4, you're better.
        * So if =, only add if the key is "smaller" than you. 
 * So if your word is in the map & hasUpdated = false, take it. If not, generate. 
    * Actually I'm going to just update in the put method, and don't need a boolean at all. */
    

public class YearlyRecord {

    Map<String, Integer> count_map;
    Map<String, Integer> rank_map; 

    /* Creates a new empty YearlyRecord. */
    public YearlyRecord() {
        count_map = new HashMap<String, Integer>();
        rank_map = new HashMap<String, Integer>();
    }

    /* Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        count_map = otherCountMap;
        // Gotta construct rank_map.
        rank_map = new HashMap<String, Integer>();
        Set<String> keys = otherCountMap.keySet();
        for (String key : keys) {
            // Now figure out the rank.
            int rank = 1; 
            for (String key2 : keys) {
                if (otherCountMap.get(key2) > otherCountMap.get(key)) {
                    rank = rank + 1;
                }
                else if (otherCountMap.get(key2) == otherCountMap.get(key) && (key2.compareTo(key) > 0)) {
                    rank = rank + 1;
                }
            }
            // Figured out rank - put in. 
            rank_map.put(key, rank);
        }
    }

    /* Records that word occured count times in this year. */
    public void put(String word, int count) {
        count_map.put(word, count);
        // Update rank_map as well. 
        rank_map = new HashMap<String, Integer>();
        Set<String> keys = count_map.keySet();
        for (String key : keys) {
            // Figure out the rank.
            int rank = 1; 
            for (String key2 : keys) {
                if (count_map.get(key2) > count_map.get(key)) {
                    rank = rank + 1;
                }
                else if (count_map.get(key2) == count_map.get(key) && (key2.compareTo(key) > 0)) {
                    rank = rank + 1;
                }
            }
            // Figured out rank - put in. 
            rank_map.put(key, rank);
            // This will work b/c map drops its reference - put in new value.
        }
    }


    /* Returns rank of word; most common word = 1. */
    public int rank(String word) {
        return rank_map.get(word);
    }


}