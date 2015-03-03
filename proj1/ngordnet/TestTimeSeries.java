package ngordnet; 
import org.junit.Test;
import static org.junit.Assert.*; 
import java.util.TreeMap;
import java.util.Collection;


public class TestTimeSeries {

    public static final int NINETYTWO = 1992;
    public static final double THREEDOTSIX = 3.6;
    public static final int NINETYTHREE = 1993;
    public static final double NINEDOTTWO = 9.2;
    public static final int NINETYFOUR = 1994;
    public static final double FIFTEENTWO = 15.2;

    @Test 
    public void testConstructor() {
        TreeMap tm = new TreeMap();
        tm.put(NINETYTWO, THREEDOTSIX);
        tm.put(NINETYTHREE, NINEDOTTWO);
        tm.put(NINETYFOUR, FIFTEENTWO);
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(NINETYTWO, THREEDOTSIX);
        ts.put(NINETYTHREE, NINEDOTTWO);
        ts.put(NINETYFOUR, FIFTEENTWO);
        assertTrue(tm.equals(ts));
    }

    @Test 
    public void testLimitedCopier() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(NINETYTWO, THREEDOTSIX);
        ts.put(NINETYTHREE, NINEDOTTWO);
        ts.put(NINETYFOUR, FIFTEENTWO);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);
        TimeSeries<Double> ts_cut = new TimeSeries<Double>();
        ts_cut.put(NINETYTHREE, NINEDOTTWO);
        ts_cut.put(NINETYFOUR, FIFTEENTWO);
        TimeSeries<Double> test_cut = new TimeSeries<Double>(ts, NINETYTHREE, NINETYFOUR);
        assertTrue(ts_cut.equals(test_cut));
    }


    @Test 
    public void testCopier() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(NINETYTWO, THREEDOTSIX);
        ts.put(NINETYTHREE, NINEDOTTWO);
        ts.put(NINETYFOUR, FIFTEENTWO);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);
        TimeSeries<Double> copy = new TimeSeries<Double>(ts);
        assertTrue(ts.equals(copy));
    }

    @Test
    public void testDividedBy() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(NINETYTWO, 1.0);
        ts.put(NINETYTHREE, 2.0);
        ts.put(NINETYFOUR, 3.0);
        TimeSeries<Double> test1 = new TimeSeries<Double>();
        test1.put(NINETYTWO, 2.0);
        test1.put(NINETYTHREE, 2.0);
        test1.put(NINETYFOUR, 1.0);
        TimeSeries<Double> ans1 = new TimeSeries<Double>();
        ans1.put(NINETYTWO, 0.5);
        ans1.put(NINETYTHREE, 1.0);
        ans1.put(NINETYFOUR, 3.0);
        assertTrue(ans1.equals(ts.dividedBy(test1)));
    } 

    @Test (expected = IllegalArgumentException.class)
    public void testDividedByError() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(NINETYTWO, 1.0);
        ts.put(NINETYTHREE, 2.0);
        ts.put(NINETYFOUR, 3.0);
        TimeSeries<Double> test2 = new TimeSeries<Double>();
        test2.put(NINETYTWO, 2.0);
        test2.put(NINETYTHREE, 2.0);
        test2.put(1995, 1.0);
        ts.dividedBy(test2);
        // Thanks to StackOverflow for this weird Exception-testing error. 
    }


    @Test
    public void testPlus() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(NINETYTWO, THREEDOTSIX);
        ts.put(NINETYTHREE, NINEDOTTWO);
        ts.put(NINETYFOUR, FIFTEENTWO);
        TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
        ts2.put(1991, 10);
        ts2.put(NINETYTWO, -5);
        ts2.put(NINETYTHREE, 1);
        ts2.put(1995, 7);
        TimeSeries<Double> actualsum = new TimeSeries<Double>();
        actualsum.put(1991, 10.0);
        actualsum.put(NINETYTWO, -1.4);
        actualsum.put(NINETYTHREE, 10.2);
        actualsum.put(NINETYFOUR, FIFTEENTWO);
        actualsum.put(1995, 7.0);
        assertTrue(actualsum.equals(ts.plus(ts2)));
    }


    @Test
    public void testYears() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(NINETYTWO, THREEDOTSIX);
        ts.put(NINETYTHREE, NINEDOTTWO);
        ts.put(NINETYFOUR, FIFTEENTWO);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);
        Collection<Number> years = ts.years();
        assertTrue(years.contains(NINETYTWO));
        assertTrue(years.contains(NINETYTHREE));
        assertTrue(years.contains(NINETYFOUR));
        assertTrue(years.contains(1995));
        assertTrue(years.contains(1996));
    }

    @Test
    public void testData() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(NINETYTWO, THREEDOTSIX);
        ts.put(NINETYTHREE, NINEDOTTWO);
        ts.put(NINETYFOUR, FIFTEENTWO);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);
        Collection<Number> data = ts.data();
        assertTrue(data.contains(THREEDOTSIX));
        assertTrue(data.contains(NINEDOTTWO));
        assertTrue(data.contains(FIFTEENTWO));
        assertTrue(data.contains(16.1));
        assertTrue(data.contains(-15.7));
    }

    public static void main(String[] args) {

        jh61b.junit.textui.runClasses(TestTimeSeries.class); 
    }

}
