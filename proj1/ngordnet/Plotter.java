package ngordnet;
import java.util.ArrayList;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.SwingWrapper;


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

    /* Creates a plot of the total normalized count of every word that

    



}
