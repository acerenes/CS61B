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
 * So if your word is in the map & hasUpdated = false, take it. If not, generate. */

public class YearlyRecord {

    Map<String, Integer> count_map;
    // Won't touch rank_map until start ranking
    Map<String, Integer> rank_map; 
    // Changes to true when put something NEW in. 
    boolean hasUpdated = false; 

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
                else if (otherCountMap.get(key2) == otherCountMap.get(key) || (key2.compareTo(key) < 0)) {
                    rank = rank + 1;
                }
            }
            // Figured out rank - put in. 
            rank_map.put(key, rank);
        }
    }


}