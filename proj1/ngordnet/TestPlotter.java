package ngordnet;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestPlotter {

    public static final int NINETYTWO = 1992;
    public static final double THREEDOTSIX = 3.6;
    public static final int NINETYTHREE = 1993;
    public static final double NINEDOTTWO = 9.2;
    public static final int NINETYFOUR = 1994;
    public static final double FIFTEENTWO = 15.2;
    public static final int NINTYFIVE = 1995;
    public static final double SIXTEENONE = 16.1;
    public static final int NINTYSIX = 1996;
    public static final double FIFTEENSEVEN = -15.7;

    @Test 
    public void testPlotTS() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(NINETYTWO, THREEDOTSIX);
        ts.put(NINETYTHREE, NINEDOTTWO);
        ts.put(NINETYFOUR, FIFTEENTWO);
        ts.put(NINTYFIVE, SIXTEENONE);
        ts.put(NINTYSIX, FIFTEENSEVEN);
        Plotter.plotTS(ts, "Title", "XLabel", "YLabel", "Legend");
    }

    @Test
    public void testPlotCountHistory() {
        NGramMap shortNGM = new NGramMap("./p1data/ngrams/very_short.csv", "./p1data/ngrams/total_counts.csv");
        Plotter.plotCountHistory(shortNGM, "wandered", 2005, 2008);
    }



    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestPlotter.class);
    }

}
