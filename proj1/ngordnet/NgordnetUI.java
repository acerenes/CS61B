package ngordnet;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;
import java.util.Set;

/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @author Alice Tarng 
 */

public class NgordnetUI {
    public static void main(String[] args) {
        In in = new In("./ngordnet/ngordnetui.config");
        System.out.println("Reading ngordnetui.config...");

        String wordFile = in.readString();
        String countFile = in.readString();
        String synsetFile = in.readString();
        String hyponymFile = in.readString();
        System.out.println("\nBased on ngordnetui.config, using the following: "
                           + wordFile + ", " + countFile + ", " + synsetFile +
                           ", and " + hyponymFile + ".");


        /* Thanks 100 thousand million for ExampleUI.java. */
        boolean yearsSet = false;
        int startYear;
        int endYear;
        while (true) { // So I guess constant updating.
            System.out.print("> ");
            String line = StdIn.readLine(); 
            String[] rawTokens = line.split(" "); 
            // RawTokens is everything user typed.
            String command = rawTokens[0];
            String [] tokens = new String[rawTokens.length - 1];
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
            // Tokens should be the stuff after the command.
            switch (command) {
                case "quit": 
                    return; 
                case "help": 
                    In inHelp = new In("./ngordnet/help.txt"); 
                    String helpStr = inHelp.readAll(); 
                    System.out.println(helpStr); 
                    break;
                case "range":
                    try {
                        startYear = Integer.parseInt(tokens[0]);
                        endYear = Integer.parseInt(tokens[1]);
                        yearsSet = true;
                    } catch (NumberFormatException ex) {
                        System.out.println("range command called incorrectly.");
                    }
                    break;
                case "count":
                    NGramMap wordCounts = new NGramMap(wordFile, countFile);
                    try {
                        String word = tokens[0];
                        int year = Integer.parseInt(tokens[1]);
                        System.out.println(wordCounts.countInYear(word, year));
                    } catch (NumberFormatException ex) {
                        System.out.println("count command called incorrectly.");
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        System.out.println("count command called incorrectly.");
                    }
                    break;
                case "hyponyms":
                    try {
                        String word = tokens[0];
                        WordNet wordHyponyms = new WordNet(synsetFile, hyponymFile);
                        Set<String> hyponyms = wordHyponyms.hyponyms(word);
                        System.out.println(hyponyms);
                    } catch (ArrayIndexOutOfBoundsException ex) {
                        System.out.println("hyponyms command called incorrectly.");
                    }
                    break;
                case "history":
                    // DON'T FORGET THE YEARS LIMIT.
                    try {
                        NGramMap ngm = new NGramMap(wordFile, countFile);
                        if (yearsSet) {
                            Plotter.plotAllWords(ngm, tokens, startYear, endYear);
                        } else {
                            /* But then what are the year bounds? 
                                * All the years I guess. 
                             * But how do I figure out all the years?
                                * Only idea I have right now is iterate through all the words & their years and find max & min. */
                        }
                    } /*catch (ArrayIndexOutOfBoundsException ex) {
                        System.out.println("history command called incorrectly.");
                    }*/
            }
        }

    }
} 
