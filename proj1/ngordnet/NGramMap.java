package ngordnet;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Collection;


public class NGramMap {


    /* Using sets again. 
        * One set for words, one for counts. */
    Set<String[]> words;
    Set<Number[]> counts;

    /* Constructs an NGramMap from wordsFilename & countsFilename. */
    public NGramMap(String wordsFilename, String countsFilename) {
        // Similar to WordNet.
        words = new HashSet<String[]>();
        File wordsfile = new File(wordsFilename);
        try {
            Scanner wordsscanned = new Scanner(wordsfile);
            while (wordsscanned.hasNextLine()) {
                String wordsstring = wordsscanned.nextLine();
                String[] wordsarray = wordsstring.split("\t");
                words.add(wordsarray);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("The words file is not valid.");
        }

        counts = new HashSet<Number[]>();
        File countsfile = new File(countsFilename);
        try {
            Scanner countsscanned = new Scanner(countsfile);
            while (countsscanned.hasNextLine()) {
                String countsInts = countsscanned.nextLine();
                String[] countsStringarray = countsInts.split(",");
                Number[] countsArray = new Number[2];
                for (int i = 0; i < 2; i = i + 1) {
                    try { // Have to create Number from string[].
                        Number element = Double.valueOf(countsStringarray[i]).doubleValue();
                        // Then add to int[].
                        countsArray[i] = element;
                    } catch (NumberFormatException nf) {
                        System.out.println("There is a problem in the counts file - an element is not an int.");
                    }
                }
                counts.add(countsArray);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("The counts file is not valid.");
        }
    }


    /* Returns the absolute count of word in the given year.
        * If word did not appear in given year, return 0. */
    public int countInYear(String word, int year) {
        // Just grab 3rd element in words file. 
        Iterator<String[]> wordsIterator = this.words.iterator();
        // First figure out where the word + year are.
        while (wordsIterator.hasNext()) {
            String[] wordsArray = wordsIterator.next();
            String theWord = wordsArray[0];
            int theYear = Integer.parseInt(wordsArray[1]);
            // Strings have to use .equals for values.
            if (theWord.equals(word) && theYear == year) {
                // Got the right line, return 3rd element.
                return Integer.parseInt(wordsArray[2]);
            }
        }
        return 0;
    }


    /* Returns a defensive copy of the YearlyRecord of the year. */
    public YearlyRecord getRecord(int year) {
        // I think defensive copy just means a COPY, not like, point at same thing.
        /* YearlyRecord takes in HashMap of word & count.
            * Find all the words that appear in that year.
            * Put word + year into HashMap.
            * Stick HashMap into YearlyRecord constructor. */
        HashMap<String, Integer> yearRecord = new HashMap<String, Integer>();
        Iterator<String[]> wordsIterator = this.words.iterator();
        // Find where the year is. 
        while (wordsIterator.hasNext()) {
            String[] wordsArray = wordsIterator.next();
            int theYear = Integer.parseInt(wordsArray[1]);
            if (theYear == year) {
                yearRecord.put(wordsArray[0], Integer.parseInt(wordsArray[2]));
            }
        }
        return new YearlyRecord(yearRecord);
    }


    /* Returns total # words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory() {
        // Going to use the counts now.
         /*Go through the counts.
            * Grab 1st & 2nd elements, put in TimeSeries.
                * TimeSeries: year, #. */
        TimeSeries<Long> totalCount = new TimeSeries();
        Iterator<Number[]> countIterator = this.counts.iterator();
        while (countIterator.hasNext()) {
            Number[] countArray = countIterator.next();
            totalCount.put(countArray[0].intValue(), countArray[1].longValue());
        }
        return totalCount;
    }


    /* Provides the history of word between startyear & endyear. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear) {
        /* Find all instances of word. 
            * Make sure year is within bounds (inclusive).
            * Grab its year and count, stick in. */
        TimeSeries<Integer> countHist = new TimeSeries();
        Iterator<String[]> histIterator = this.words.iterator();
        while (histIterator.hasNext()) {
            String[] histArray = histIterator.next();
            if (histArray[0].equals(word)) {
                int year = Integer.parseInt(histArray[1]);
                int count = Integer.parseInt(histArray[2]);
                if (year >= startYear && year <= endYear) {
                    countHist.put(year, count);
                }
            }
        }
        return countHist;
    }


    /* Provides a defensive copy of the history of word. */
    public TimeSeries<Integer> countHistory(String word) {
        // Just like above, but no limits.
        TimeSeries<Integer> countHist = new TimeSeries();
        Iterator<String[]> histIterator = this.words.iterator();
        while (histIterator.hasNext()) {
            String[] histArray = histIterator.next();
            if (histArray[0].equals(word)) {
                int year = Integer.parseInt(histArray[1]);
                int count = Integer.parseInt(histArray[2]);
                countHist.put(year, count);
            }
        }
        return countHist;
    }


    /* Provides the relative frequency of word between startyear and endyear. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear) {
        /* So like countHistory with bounds from above.
            * But divide count by total from counts. */
        TimeSeries<Double> weightHist = new TimeSeries();
        Iterator<String[]> weightIterator = this.words.iterator();
        while (weightIterator.hasNext()) {
            String[] weightArray = weightIterator.next();
            if (weightArray[0].equals(word)) {
                int year = Integer.parseInt(weightArray[1]);
                int count = Integer.parseInt(weightArray[2]);
                if (year >= startYear && year <= endYear) {
                    // Need to find the total counts. 
                    Iterator<Number[]> totalCountIterator = this.counts.iterator();
                    while (totalCountIterator.hasNext()) {
                        Number[] totalCountArray = totalCountIterator.next();
                        if ((totalCountArray[0].intValue()) == year) {
                            Number totalCount = totalCountArray[1];
                            weightHist.put(year, Integer.valueOf(count).doubleValue() / totalCount.doubleValue());
                        }
                    }
                }
            }
        }
        return weightHist;
    }


    /* Provides the relative frequency of word. */
    public TimeSeries<Double> weightHistory(String word) {
        // Above with no bounds. 
        TimeSeries<Double> weightHist = new TimeSeries();
        Iterator<String[]> weightIterator = this.words.iterator();
        while (weightIterator.hasNext()) {
            String[] weightArray = weightIterator.next();
            if (weightArray[0].equals(word)) {
                int year = Integer.parseInt(weightArray[1]);
                int count = Integer.parseInt(weightArray[2]);
                // Find total counts.
                Iterator<Number[]> totalCountIterator = this.counts.iterator();
                while (totalCountIterator.hasNext()) {
                    Number[] totalCountArray = totalCountIterator.next();
                    if ((totalCountArray[0].intValue()) == year) {
                        Number totalCount = totalCountArray[1];
                        weightHist.put(year, Integer.valueOf(count).doubleValue() / totalCount.doubleValue());
                    }
                }
            }
        }
        return weightHist;
    }


    /* Provides the summed relative frequency of all words between startyear & endyear. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, int startYear, int endYear) {

        TimeSeries<Double> summedWeightHist = new TimeSeries();
        for (int currYear = startYear; currYear <= endYear; currYear = currYear + 1) {
            // For 1 year. 
            double sumFrequency = 0; // Also initialize.
            for (String word : words) {
                TimeSeries<Double> currTimeSeries = weightHistory(word, startYear, endYear);
                if (currTimeSeries.get(currYear) == null) {
                    // Just in case - don't add anything.
                    sumFrequency = sumFrequency;
                } else { 
                    sumFrequency = sumFrequency + currTimeSeries.get(currYear);
                }
            }
            summedWeightHist.put(currYear, sumFrequency);
        }
        return summedWeightHist;
    }


    /* Returns the summed relative frequencey of all words. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words) {
        // Above with no boundaries. 
        TimeSeries<Double> summedWeightHist = new TimeSeries();
        /* Go through one word at a time. 
            * Go through entire words set, grab year + #. 
            * For each year, get relative freq + add to prev rel. freq.
                * First word: if return list doesn't have that year as a key yet:
                    * Just add relative freq to 0. 
            * Add year & summned rel. freq. to TimeSeries.
            * Repeat loop. */
        for (String word : words) {
            for (String[] wordData : this.words) {
                if (wordData[0].equals(word)) {
                    int currYear = Integer.parseInt(wordData[1]);
                    TimeSeries<Double> relFreqData = weightHistory(word);
                    double currFreq = relFreqData.get(currYear);
                    if (summedWeightHist.containsKey(currYear)) {
                        currFreq = currFreq + summedWeightHist.get(currYear);
                        // Summing up if already have data.
                    }
                    summedWeightHist.put(currYear, currFreq);
                    // Should've taken care of all years for this word.
                }
            }
        }
        return summedWeightHist;
    }

}
