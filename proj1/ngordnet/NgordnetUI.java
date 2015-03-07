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
        int startYear = 0;
        int endYear = 0; 
        // ^ Intializing.
        while (true) { // So I guess constant updating.
            System.out.print("> ");
            String line = StdIn.readLine(); 
            String[] rawTokens = line.split(" "); 
            // RawTokens is everything user typed.
            String command = rawTokens[0];
            String [] tokens = new String[rawTokens.length - 1];
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
            // Tokens should be the inputs for the command.
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
                        if (startYear > endYear) {
                            System.out.println("Start must be less than end.");
                            yearsSet = false;
                        }
                    } catch (NumberFormatException ex) {
                        System.out.println("range command called incorrectly.");
                    } catch (ArrayIndexOutOfBoundsException ex2) {
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
                    NGramMap ngm = new NGramMap(wordFile, countFile);
                    if (yearsSet) {
                        Plotter.plotAllWords(ngm, tokens, startYear, endYear);
                    } else {
                        String error = "Years range not set or history command called incorrectly.";
                        System.out.println(error);
                    }
                    break;
                case "hypohist":
                    NGramMap ngm2 = new NGramMap(wordFile, countFile);
                    WordNet wn = new WordNet(synsetFile, hyponymFile);
                    if (yearsSet) {
                        Plotter.plotCategoryWeights(ngm2, wn, tokens, startYear, endYear);
                    } else {
                        String error2 = "Years range not set or hypohist command called wrongly.";
                        System.out.println(error2);
                    }
                    break;
                default:
                    System.out.println("Invalid command.");
                    break;
            }
        }
    }
} 
