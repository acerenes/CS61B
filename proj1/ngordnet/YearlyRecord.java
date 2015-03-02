package ngordnet;
import java.util.Map;
import java.util.HashMap;

/* Okay I'm thinking 2 maps? 
 * One for word v. count.
 * Can have a size instance variable.
 * Then rank, something with an loop like Dis 6.3.1, create new map.  
   * But how to do the actual ranking? 
    * Maybe sort all keys, then iterate through all the keys. 
      * If you pass one that is more common than you, plus 1. (Start at 1.)
      * But how deal with tiebreakers? 
        * Alphabetical order - u're b4, you're better.
        * So if =, only add if the key is "smaller" than you. 
 * So if your word is in the map & hasUpdated = false, take it. If not, generate. */

public class YearlyRecord {

    Map<String, Integer> count_map;
    Map<String, Integer> rank_map;

    /* Creates a new empty YearlyRecord. */
    public YearlyRecord() {
        count_map = new HashMap<String, Integer>();
        rank_map = new HashMap<String, Integer>();
    }
}