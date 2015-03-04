package ngordnet;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


public class NGramMap {


    /* Using sets again. 
        * One set for words, one for counts. */
    Set<String[]> words;
    Set<int[]> counts;

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

        counts = new HashSet<int[]>();
        File countsfile = new File(countsFilename);
        try {
            Scanner countsscanned = new Scanner(countsfile);
            while (countsscanned.hasNextLine()) {
                String countsInts = countsscanned.nextLine();
                String[] countsStringarray = countsInts.split(",");
                int[] countsArray = new int[countsStringarray.length];
                for (int i = 0; i < countsStringarray.length; i = i + 1) {
                    try { // Have to create int from string[].
                        int element = Integer.parseInt(countsStringarray[i]);
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
}
