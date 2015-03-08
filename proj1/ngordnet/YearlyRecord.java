package ngordnet;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;

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
 * So if your word is in the map & hasRanked = true, take it. If not, generate. 
    * The whole cached boolean makes it fast. */
    

public class YearlyRecord {

    private Map<String, Integer> countMap;
    private Map<String, Integer> rankMap; 
    private int size;
    private boolean isRanked = false;

    /* Creates a new empty YearlyRecord. */
    public YearlyRecord() {
        countMap = new TreeMap<String, Integer>();
        rankMap = new TreeMap<String, Integer>();
        size = 0;
    }

    /* Creates a YearlyRecord using the given data. */
    public YearlyRecord(HashMap<String, Integer> otherCountMap) {
        // I think you're supposed to create a new object.
        countMap = new TreeMap<String, Integer>();
        rankMap = new TreeMap<String, Integer>();
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
        /* Don't need to update the rank ALL THE TIME.
            * Just do it when needed - rank is called. */
        isRanked = false;
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
        if (isRanked) {
            return rankMap.get(word);
        }
        this.buildRankMap();
        return rankMap.get(word);
    }


    /* Helper fxn to build rankMap. */
    private void buildRankMap() {
        if (!isRanked) {
            /* Build rankMap. */
            /* Dump entires of countMap to array. */
            String[] wordsArray = countMap.keySet().toArray(new String[0]);
            // ^ B/c just toArray returns Object[]. Thx to StackOverflow for the trick.*/
            Arrays.sort(wordsArray, new StringCountComparator());
            ArrayList<String> wordsArrayList = new ArrayList<String>(Arrays.asList(wordsArray));
            for (String word2 : wordsArrayList) {
                rankMap.put(word2, wordsArrayList.indexOf(word2) + 1);
            }
            // Need +1 b/c rank starts at 1, not 0.
        }
        isRanked = true;
    }


    /* Inner class considered to be part of the containing class --> Full access to all privates both ways.
        * Access control. */
    private class StringCountComparator implements Comparator<String> {
        @Override
        public int compare(String word1, String word2) {
            // Sort words by descending order of count. 
            return countMap.get(word2) - countMap.get(word1);
        }
    }


    /* Returns all words in ascending order of count. */
    public Collection<String> words() {
        /* What I'm thinking here is:
            * Size of YearlyRecord = lowest rank.
            * Maintain a counter that goes from size --> 1.
            * Iterate through the keys. 
            * If the value = counter, put the key in the list. 
            * Update counter. */
        if (countMap.isEmpty()) {
            return new ArrayList<String>();
        }
        else if (isRanked) {
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

        // Build the rankMap.
        this.buildRankMap();
        return this.words();
    }


    /* Returns all counts in ascending order of count. */
    public Collection<Number> counts() {
        if (countMap.isEmpty()) {
            return new ArrayList<Number>();
        }
        else if (isRanked) {
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
        // Build the rankMap.
        this.buildRankMap();
        return this.counts();
    }


}
