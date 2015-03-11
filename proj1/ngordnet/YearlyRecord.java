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
    

public class YearlyRecord {

    private Map<String, Integer> countMap; // Word v. count.
    private Map<String, Integer> rankMap;  // Word v. rank.
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
        countMap = new TreeMap<String, Integer>();
        rankMap = new TreeMap<String, Integer>();
        Set<String> keys = otherCountMap.keySet();
        // First construct countMap.
        for (String key : keys) {
            countMap.put(key, otherCountMap.get(key));
            size = size + 1;
        }
        // Construct rankMap.
        buildRankMap();
        isRanked = true;
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
        if (countMap.containsKey(word)) {
            return countMap.get(word);
        }
        return 0;
    }


    /* Returns rank of word; most common word = 1. */
    public int rank(String word) {
        if (!countMap.containsKey(word)) {
            return 0;
        } 
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


    /* Inner class considered part of containing class --> Full access to all privates both ways.
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
        } else if (isRanked) {
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
        } else if (isRanked) {
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
