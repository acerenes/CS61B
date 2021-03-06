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


    private Set<String[]> wordsSet;
    private Set<Number[]> counts;

    /* Constructs an NGramMap from wordsFilename & countsFilename. */
    public NGramMap(String wordsFilename, String countsFilename) {
        wordsSet = new HashSet<String[]>();
        File wordsfile = new File(wordsFilename);
        try {
            Scanner wordsscanned = new Scanner(wordsfile);
            while (wordsscanned.hasNextLine()) {
                String wordsstring = wordsscanned.nextLine();
                String[] wordsarray = wordsstring.split("\t");
                wordsSet.add(wordsarray);
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
                    try { 
                        // Create a Number from string[].
                        Number element = Double.valueOf(countsStringarray[i]).doubleValue();
                        countsArray[i] = element;
                    } catch (NumberFormatException nf) {
                        System.out.println("Problem in counts file - an element is not an int.");
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
        // 3rd element in words file. 
        Iterator<String[]> wordsIterator = this.wordsSet.iterator();
        // Figure out where the word + year are.
        while (wordsIterator.hasNext()) {
            String[] wordsArray = wordsIterator.next();
            String theWord = wordsArray[0];
            int theYear = Integer.parseInt(wordsArray[1]);
            if (theWord.equals(word) && theYear == year) {
                return Integer.parseInt(wordsArray[2]);
            }
        }
        return 0;
    }


    /* Returns a defensive copy of the YearlyRecord of the year. */
    public YearlyRecord getRecord(int year) {
        /* YearlyRecord takes in HashMap of word & count.
            * Find all the words that appear in that year.
            * Put word + year into HashMap.
            * Stick HashMap into YearlyRecord constructor. */
        HashMap<String, Integer> yearRecord = new HashMap<String, Integer>();
        Iterator<String[]> wordsIterator = this.wordsSet.iterator();
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
        /* Go through the counts.
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
            * Grab its year and count. */
        TimeSeries<Integer> countHist = new TimeSeries();
        Iterator<String[]> histIterator = this.wordsSet.iterator();
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
        TimeSeries<Integer> countHist = new TimeSeries();
        Iterator<String[]> histIterator = this.wordsSet.iterator();
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
        /* Like countHistory but divide count by total from counts. */
        TimeSeries<Double> weightHist = new TimeSeries();
        Iterator<String[]> weightIterator = this.wordsSet.iterator();
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
                            double countDouble = Integer.valueOf(count).doubleValue();
                            weightHist.put(year, countDouble / totalCount.doubleValue());
                        }
                    }
                }
            }
        }
        return weightHist;
    }


    /* Provides the relative frequency of word. */
    public TimeSeries<Double> weightHistory(String word) {
        TimeSeries<Double> weightHist = new TimeSeries();
        Iterator<String[]> weightIterator = this.wordsSet.iterator();
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
                        double countDouble = Integer.valueOf(count).doubleValue();
                        weightHist.put(year, countDouble / totalCount.doubleValue());
                    }
                }
            }
        }
        return weightHist;
    }


    /* Provides the summed relative frequency of all words between startyear & endyear. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, 
        int startYear, int endYear) {

        TimeSeries<Double> summedWeightHist = new TimeSeries();
        for (int currYear = startYear; currYear <= endYear; currYear = currYear + 1) {
            double sumFrequency = 0; 
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
        TimeSeries<Double> summedWeightHist = new TimeSeries();
        /* Go through one word at a time. 
            * Go through entire words set, grab year + #. 
            * For each year, get relative freq + add to prev rel. freq.
                * First word: if return list doesn't have that year as a key yet:
                    * Just don't add anything. 
            * Add year & summned rel. freq. to TimeSeries.
            * Repeat loop. */
        for (String word : words) {
            for (String[] wordData : this.wordsSet) {
                if (wordData[0].equals(word)) {
                    int currYear = Integer.parseInt(wordData[1]);
                    TimeSeries<Double> relFreqData = weightHistory(word);
                    double currFreq = relFreqData.get(currYear);
                    if (summedWeightHist.containsKey(currYear)) {
                        currFreq = currFreq + summedWeightHist.get(currYear);
                        // Summing up if already have data.
                    }
                    summedWeightHist.put(currYear, currFreq);
                    // Should take care of all years for this word.
                }
            }
        }
        return summedWeightHist;
    }


    /* Provides processed history of all words between startyear and endyear as processed by yrp. */
    public TimeSeries<Double> processedHistory(int startYear, int endYear,
        YearlyRecordProcessor yrp) {
        /* TimeSeries = year then data. 
            * So for one year, calculate data, put into TimeSeries. 
            * Also make a YearlyRecord for the processor. */
        if (endYear < startYear) {
            System.out.println("endYear must be greater than startYear.");
            return new TimeSeries<Double>();
        }
        TimeSeries<Double> processedHist = new TimeSeries<Double>();
        while (endYear >= startYear) {
            YearlyRecord yr = this.getRecord(endYear);
            double avgLength = yrp.process(yr);
            processedHist.put(endYear, avgLength);
            endYear = endYear - 1;
        }
        return processedHist;
    }


    /*  Provides processed history of all words ever as processed by yrp. */
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp) {
        TimeSeries<Double> processedHist = new TimeSeries<Double>();
        for (int year : this.allYears()) {
            YearlyRecord yr = this.getRecord(year);
            double avgLength = yrp.process(yr);
            processedHist.put(year, avgLength);
        }
        return processedHist;
    }


    /* Returns all years of the words. */
    private Set<Integer> allYears() {
        Set<Integer> allYears = new HashSet<Integer>();
        // Grab 2nd element, stick in. */
        for (String[] wordsArray : wordsSet) {
            int year = Integer.parseInt(wordsArray[1]);
            allYears.add(year);
        }
        return allYears;
    }

}
