package ngordnet;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

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

    private Map<String, Integer> countMap;
    private Map<String, Integer> rankMap; 
    private int size;

    /* Creates a new empty YearlyRecord. */
    public YearlyRecord() {
        countMap = new HashMap<String, Integer>();
        rankMap = new HashMap<String, Integer>();
        size = 0;
    }

    /* Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        // I think you're supposed to create a new object.
        countMap = new HashMap<String, Integer>();
        rankMap = new HashMap<String, Integer>();
        Set<String> keys = otherCountMap.keySet();
        // First construct countMap.
        for (String key : keys) {
            countMap.put(key, otherCountMap.get(key));
            size = size + 1;
        }
        // Construct rankMap.
        for (String key : keys) {
            // Now figure out the rank.
            int rank = 1; 
            Integer mapValue = otherCountMap.get(key);
            for (String key2 : keys) { // Other guys.
                Integer mapValue2 = otherCountMap.get(key2);
                if (mapValue2 > mapValue) {
                    rank = rank + 1;
                } else if (mapValue2 == mapValue && (key.compareTo(key2) > 0)) {
                    // You are alphabetically lower - increase rank.
                    rank = rank + 1;
                }
            }
            // Figured out rank - put in. 
            rankMap.put(key, rank);
        }
    }

    /* Records that word occured count times in this year. */
    public void put(String word, int count) {
        countMap.put(word, count);
        size = size + 1;
        // Update rankMap as well. 

        /* Okay Alice, you're doing this wrong.
            * Why are you creating a whole new HashMap?
            * Why don't you just readjust the ranks & stick it in.
                * Maybe start out 1.
                * Iterate through countMap. 
                * Increase rank accordingly like above.
                    * If you "passed" them, increase THEIR rank. 
                * Then you could figure out its rank, and stick it in rankMap. 
                * And the other guys' ranks should have been increased during your iteration. 
                * So all good. Hopefully. */


        /* STILL NOT FAST ENOUGH. 
            * Somehow sort the keys.
                * Start from the back and go until find the one that's smaller than you. 
                * If you're passing other guys, increase their rank. 
                * Take the guy who's smaller than you, and your rank = their rank + 1. 
                * I think this may be faster, but I'll leave it for later. */

        int rank = 1; /*
        if (rankMap.isEmpty()) {
            rankMap = new HashMap<String, Integer>();
        } else {*/
            /*Set<String> keys = rankMap.keySet();*/
        for (String key : rankMap.keySet()) {
            Integer comparingCount = countMap.get(key);
            if (comparingCount > count) { // Increase your rank.
                rank = rank + 1;
            } else if (comparingCount == count && word.compareTo(key) > 0) {
                // You're lower alphabetically - increase your rank.
                rank = rank + 1;
            } else {
                // You passed them - increase THEIR rank. 
                    // Don't think can change directly with get, so gonna just put new pair in.
                rankMap.put(key, rankMap.get(key) + 1);
            }
        }
        rankMap.put(word, rank);
    }


    /* Returns # words recorded this year. */
    public int size() {
        return size; 
    }


    /* Returns # times word appeared in this year. */
    public int count(String word) {
        return countMap.get(word);
    }


    /* Returns rank of word; most common word = 1. */
    public int rank(String word) {
        return rankMap.get(word);
    }


    /* Returns all words in ascending order of count. */
    public Collection<String> words() {
        /* What I'm thinking here is:
            * Size of YearlyRecord = lowest rank.
            * Maintain a counter that goes from size --> 1.
            * Iterate through the keys. 
            * If the value = counter, put the key in the list. 
            * Update counter. */
        List<String> ascendingWords = new ArrayList<String>();
        Map<String, Integer> rankedmap = this.rankMap;
        Set<String> keys = rankedmap.keySet();
        int counter = this.size();
        while (counter >= 1) {
            for (String key : keys) {
                if (counter == rankedmap.get(key)) {
                    ascendingWords.add(key);
                }
            }
            counter = counter - 1;
        }
        return ascendingWords;
    }


    /* Returns all counts in ascending order of count. */
    public Collection<Number> counts() {
        List<Number> ascendingCount = new ArrayList<Number>();
        Map<String, Integer> rankedmap = this.rankMap;
        Map<String, Integer> countmap = this.countMap;
        Set<String> keys = rankedmap.keySet();
        int counter = this.size();
        while (counter >= 1) {
            for (String key : keys) {
                if (counter == rankedmap.get(key)) {
                    ascendingCount.add(countmap.get(key));
                }
            }
            counter = counter - 1;
        }
        return ascendingCount;
    }


}
