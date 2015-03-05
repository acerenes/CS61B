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



}
