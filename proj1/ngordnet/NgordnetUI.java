package ngordnet;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;

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
        while (true) { // So I guess constant updating.
            System.out.print("> ");
            String line = StdIn.readLine(); 
            String[] rawTokens = line.split(" "); 
            String command = rawTokens[0];
            String [] tokens = new String[rawTokens.length - 1];
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);  
            switch (command) {
                case "quit": 
                    return; 
                case "help": 
                    In inHelp = new In("help.txt"); 
                    String helpStr = inHelp.readAll(); 
                    System.out.println(helpStr); 
                    break;
                case "range":
                    try {
                        int startDate = Integer.parseInt(tokens[0]);
                        int endDate = Integer.parseInt(tokens[1]);
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
            }
        }

    }
} 
