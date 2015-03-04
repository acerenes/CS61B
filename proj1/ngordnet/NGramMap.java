package ngordnet;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.HashMap;


public class NGramMap {


    /* Using sets again. 
        * One set for words, one for counts. */
    Set<String[]> words;
    Set<Long[]> counts;

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

        counts = new HashSet<Long[]>();
        File countsfile = new File(countsFilename);
        try {
            Scanner countsscanned = new Scanner(countsfile);
            while (countsscanned.hasNextLine()) {
                String countsInts = countsscanned.nextLine();
                String[] countsStringarray = countsInts.split(",");
                Long[] countsArray = new Long[countsStringarray.length];
                for (int i = 0; i < countsStringarray.length; i = i + 1) {
                    try { // Have to create Number from string[].
                        Long element = Long.valueOf(countsStringarray[i]).longValue();
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
            totalCount.put
        }

}
