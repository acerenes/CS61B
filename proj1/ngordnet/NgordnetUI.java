package ngordnet;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;
import java.util.Set;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;
import com.xeiam.xchart.ChartBuilder;
import java.util.ArrayList;

/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @author Alice Tarng 
 */


public class NgordnetUI {

    private static final int EIGHTHUNDRED = 800;
    private static final int SIXHUNDRED = 600;

    public static void main(String[] args) {
        In in = new In("./ngordnet/ngordnetui.config");
        String wordFile = in.readString();
        String countFile = in.readString();
        String synsetFile = in.readString();
        String hyponymFile = in.readString();
        System.out.println("\nBased on ngordnetui.config, using the following: "
                           + wordFile + ", " + countFile + ", " 
                           + synsetFile 
                           + ", and " + hyponymFile + ".");
        boolean yearsSet = false;
        int startYear = 0;
        int endYear = 0; // Initializing.
        NGramMap ngm = new NGramMap(wordFile, countFile);
        WordNet wn = new WordNet(synsetFile, hyponymFile);
        while (true) { 
            System.out.print("> ");
            String line = StdIn.readLine(); 
            String[] rawTokens = line.split(" "); 
            String command = rawTokens[0];
            String[] tokens = new String[rawTokens.length - 1]; // Tokens = command inputs.
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1); 
            switch (command) {
                case "quit": 
                    return; 
                case "help": 
                    helpCommand(); 
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
                    countCommand(tokens, ngm);
                    break;
                case "hyponyms":
                    hyponymsCommand(tokens, wn);
                    break;
                case "history":
                    historyCommand(tokens, wn, ngm, startYear, endYear, yearsSet);
                    break;
                case "hypohist":
                    hypohistCommand(tokens, wn, ngm, startYear, endYear, yearsSet);
                    break;
                case "wordlength":
                    if (yearsSet) {
                        plotWordLength(ngm, startYear, endYear);
                    } else {
                        plotWordLength(ngm);
                    }
                    break;
                case "zipf":
                    zipfCommand(tokens, wn, ngm);
                    break;
                default:
                    System.out.println("Invalid command.");
                    break;
            }
        }
    }

    /* Method for help command. */
    private static void helpCommand() {
        In inHelp = new In("./ngordnet/help.txt");
        System.out.println(inHelp.readAll()); 
    }

    /* Method for count command. */
    private static void countCommand(String[] tokens, NGramMap ngm) {
        try {
            String word = tokens[0];
            int year = Integer.parseInt(tokens[1]);
            System.out.println(ngm.countInYear(word, year));
        } catch (NumberFormatException ex) {
            System.out.println("count command called incorrectly.");
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("count command called incorrectly.");
        }
    }

    /* Method for hyponyms command. */
    private static void hyponymsCommand(String[] tokens, WordNet wn) {
        try {
            String word = tokens[0];
            Set<String> hyponyms = wn.hyponyms(word);
            System.out.println(hyponyms);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("hyponyms command called incorrectly.");
        }
    }

    /* Method for history command. */
    private static void historyCommand(String[] tokens, WordNet wn, NGramMap ngm, 
        int startYear, int endYear, boolean yearsSet) {
        if (tokens.length == 0) {
            System.out.println("history command called incorrectly");
        } else {
            try {
                if (yearsSet) {
                    Plotter.plotAllWords(ngm, tokens, startYear, endYear);
                } else {
                    String error = "Years range not set or history command called incorrectly.";
                    System.out.println(error);
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("Word not found in data or year range invalid.");
            }
        }
    }

    /* Method for hypohist command. */
    private static void hypohistCommand(String[] tokens, WordNet wn, NGramMap ngm, 
        int startYear, int endYear, boolean yearsSet) {
        if (tokens.length == 0) {
            System.out.println("history command called incorrectly");
        } else {
            try {
                if (yearsSet) {
                    Plotter.plotCategoryWeights(ngm, wn, tokens, startYear, endYear);
                } else {
                    String error2 = "Years range not set or hypohist command called wrongly.";
                    System.out.println(error2);
                }
            } catch (IllegalArgumentException ex) {
                System.out.println("hypohist command called incorrectly.");
            }
        }
    }

    /* Method for zipf command. */
    private static void zipfCommand(String[] tokens, WordNet wn, NGramMap ngm) {
        if (tokens.length == 0) {
            System.out.println("history command called incorrectly");
        } else {
            try {
                int year = Integer.parseInt(tokens[0]);
                plotZipfLog(ngm, year);
            } catch (NumberFormatException ex) {
                System.out.println("zipf command called incorrectly.");
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println("zipf command called incorrectly.");
            } catch (IllegalArgumentException ex) {
                System.out.println("zipf command called incorrectly.");
            }
        }
    }


    /* Plots average word length with bounded years. */
    private static void plotWordLength(NGramMap ngm, int startYear, int endYear) {
        TimeSeries<Double> wordLengthData = ngm.processedHistory(startYear, endYear, 
            new WordLengthProcessor());

        ArrayList<Number> xValues = new ArrayList<Number>();
        ArrayList<Number> yValues = new ArrayList<Number>();

        for (Number year : wordLengthData.years()) {
            xValues.add(year);
            yValues.add(wordLengthData.get(year));
        }

        Chart chart = QuickChart.getChart("Average Word Length during Years", 
            "Year", "Length", "Average Word Length", xValues, yValues);
        new SwingWrapper(chart).displayChart();
    }

    /* Plots average word length from all years. */
    private static void plotWordLength(NGramMap ngm) {
        TimeSeries<Double> wordLengthData = ngm.processedHistory(new WordLengthProcessor());

        ArrayList<Number> xValues = new ArrayList<Number>();
        ArrayList<Number> yValues = new ArrayList<Number>();

        for (Number year : wordLengthData.years()) {
            xValues.add(year);
            yValues.add(wordLengthData.get(year));
        }

        Chart chart = QuickChart.getChart("Average Word Length of All Years", 
            "Year", "Length", "Average Word Length", xValues, yValues);
        new SwingWrapper(chart).displayChart();
    }


    /* Plots Zipf's Law (count v. rank) on log-log scale. */
    private static void plotZipfLog(NGramMap ngm, int year) {
        YearlyRecord yr = ngm.getRecord(year);
        // YearlyRecord is word, count.

        ArrayList<Number> xValues = new ArrayList<Number>();
        ArrayList<Number> yValues = new ArrayList<Number>();

        for (String word : yr.words()) {
            xValues.add(yr.rank(word));
            yValues.add(yr.count(word));
        }

        String title = "Zipf's Law on Log-Log Scale for " + year;
        String ylabel = "Count (log)";
        String xlabel = "Rank (log)";
        String legend = Integer.toString(year);

        Chart chart = new ChartBuilder().width(EIGHTHUNDRED).height(SIXHUNDRED).title(title)
            .xAxisTitle(xlabel).yAxisTitle(ylabel).build();

        chart.getStyleManager().setYAxisLogarithmic(true);
        chart.getStyleManager().setXAxisLogarithmic(true);
        chart.addSeries(legend, xValues, yValues);

        new SwingWrapper(chart).displayChart();
    }



} 

/* Thanks 100 thousand million for ExampleUI.java. 
    * Sorry main method is super squished; had to be under 80 lines.
    * Used to catch each individual type of exception, but too many lines - 1 big RunTimeException.
    * Thanks Stylechecker. */

