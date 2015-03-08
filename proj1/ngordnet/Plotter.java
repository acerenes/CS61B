package ngordnet;
import java.util.ArrayList;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;
import java.util.Set;
import com.xeiam.xchart.ChartBuilder;


public class Plotter {

    /* Creates a plot of TimeSeries ts.
     * Labels graph with title, xlabel, ylabel, legend. */
    public static void plotTS(TimeSeries<? extends Number> ts, String title, String xlabel, String ylabel, String legend) {
        /* TimeSeries are year to "data". 
            * I'm doing year on x, data on y. */
        // THANK GOODNESS FOR XCHARTDEMO
        ArrayList<Number> xValues = new ArrayList<Number>();
        ArrayList<Number> yValues = new ArrayList<Number>();

        for (Number year : ts.years()) {
            xValues.add(year);
            yValues.add(ts.get(year));
        }

        // Create chart.
        Chart chart = QuickChart.getChart(title, xlabel, ylabel, legend, xValues, yValues);

        // Show it.
        new SwingWrapper(chart).displayChart();
    }


    /* Creates a plot of the abs word counts for word from startyear to endyear.
     * Inclusive years. 
     * Uses ngm as a data source. */
    public static void plotCountHistory(NGramMap ngm, String word, int startYear, int endYear) {
        TimeSeries<Integer> countHist = ngm.countHistory(word, startYear, endYear);

        ArrayList<Number> xValues = new ArrayList<Number>();
        ArrayList<Number> yValues = new ArrayList<Number>();
        // Have to both be Numbers - java unhappy when I tried to make y Integer.

        for (Number year : countHist.years()) {
            xValues.add(year);
            yValues.add(countHist.get(year));
        }
        Chart chart = QuickChart.getChart("Absolute Word Counts for " + word, "Year", "Absolute Counts", word, xValues, yValues);
        new SwingWrapper(chart).displayChart();
    }


    /* Creates a plot of the normalized weight counts for word from startyear to endyear.
        * Uses ngm as a data source. */
    public static void plotWeightHistory(NGramMap ngm, String word, int startYear, int endYear) {
        TimeSeries<Double> weightHist = ngm.weightHistory(word, startYear, endYear);

        ArrayList<Number> xValues = new ArrayList<Number>();
        ArrayList<Number> yValues = new ArrayList<Number>();

        for (Number year : weightHist.years()) {
            xValues.add(year);
            yValues.add(weightHist.get(year));
        }

        Chart chart = QuickChart.getChart("Normalized Weight Counts for " + word, "Year", "Normalized Weight Counts", word, xValues, yValues);
        new SwingWrapper(chart).displayChart();
    }

    /* Creates plot of the total normalized count of every word that is a hyponym of categorylabel.
        * From startyear to endyear using ngm and wn as data sources. */
    public static void plotCategoryWeights(NGramMap ngm, WordNet wn, String categoryLabel, 
        int startYear, int endYear) {

        Set<String> hyponyms = wn.hyponyms(categoryLabel);
        // A set is a collection.
        TimeSeries<Double> totalNormCount = ngm.summedWeightHistory(hyponyms, startYear, endYear);

        ArrayList<Number> xValues = new ArrayList<Number>();
        ArrayList<Number> yValues = new ArrayList<Number>();

        for (Number year : totalNormCount.years()) {
            xValues.add(year);
            yValues.add(totalNormCount.get(year));
        }

        Chart chart = QuickChart.getChart("Total Normalized Count for All Hyponyms of " + categoryLabel, "Year", "Total Normalized Counts", categoryLabel, xValues, yValues);
        new SwingWrapper(chart).displayChart();
    }


    /* Creates overlaid category weight plots for each category label in categorylabels.
        * From startyear to endyear using ngm & wn as data sources. */
    public static void plotCategoryWeights(NGramMap ngm, WordNet wn, String[] categoryLabels, 
        int startYear, int endYear) {

        String title = "Total Normalized Counts for Words and Their Hyponyms";

        Chart chart = new ChartBuilder().width(800).height(600).title(title).xAxisTitle("Years").yAxisTitle("Total Normalized Counts").build();

        for (String catLabel : categoryLabels) {
            ArrayList<Number> xValues = new ArrayList<Number>();
            ArrayList<Number> yValues = new ArrayList<Number>();
            Set<String> hyponyms = wn.hyponyms(catLabel);
            TimeSeries<Double> totalNormCount = ngm.summedWeightHistory(hyponyms, startYear, endYear);
            String legend = catLabel;
            for (Number year : totalNormCount.years()) {
                xValues.add(year);
                yValues.add(totalNormCount.get(year));
            }
            chart.addSeries(legend, xValues, yValues);
        }
        new SwingWrapper(chart).displayChart();
    }


    /* Makes a plot showing overlaid individual normalized count for every word in words.
        * From startyear to endyear using ngm as data source. */
    public static void plotAllWords(NGramMap ngm, String[] words, int startYear, int endYear) {

        String title = "Normalized Count for Words";

        Chart chart = new ChartBuilder().width(800).height(600).title(title).xAxisTitle("Years").yAxisTitle("Normalized Counts").build();

        for (String word : words) {
            ArrayList<Number> xValues = new ArrayList<Number>();
            ArrayList<Number> yValues = new ArrayList<Number>();
            TimeSeries<Double> normCount = ngm.weightHistory(word);
            String legend = word;
            for (Number year : normCount.years()) {
                if (year.intValue() >= startYear && year.intValue() <= endYear) {
                    xValues.add(year);
                    yValues.add(normCount.get(year));
                }
            }
            chart.addSeries(legend, xValues, yValues);
        }
        new SwingWrapper(chart).displayChart();
    }



}
