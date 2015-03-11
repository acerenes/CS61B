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
    public static final int NINETYFIVE = 1995;
    public static final double SIXTEENONE = 16.1;
    public static final int NINETYSIX = 1996;
    public static final double NEGFIFTEENSEVEN = -15.7;
    public static final int NINETYONE = 1991;
    public static final double NEGONEPTFOUR = -1.4;
    public static final double TENPTTWO = 10.2;

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
        ts.put(NINETYFIVE, SIXTEENONE);
        ts.put(NINETYSIX, NEGFIFTEENSEVEN);
        TimeSeries<Double> tsCut = new TimeSeries<Double>();
        tsCut.put(NINETYTHREE, NINEDOTTWO);
        tsCut.put(NINETYFOUR, FIFTEENTWO);
        TimeSeries<Double> testCut = new TimeSeries<Double>(ts, NINETYTHREE, NINETYFOUR);
        assertTrue(tsCut.equals(testCut));
    }


    @Test 
    public void testCopier() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(NINETYTWO, THREEDOTSIX);
        ts.put(NINETYTHREE, NINEDOTTWO);
        ts.put(NINETYFOUR, FIFTEENTWO);
        ts.put(NINETYFIVE, SIXTEENONE);
        ts.put(NINETYSIX, NEGFIFTEENSEVEN);
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
        test2.put(NINETYFIVE, 1.0);
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
        ts2.put(NINETYONE, 10);
        ts2.put(NINETYTWO, -5);
        ts2.put(NINETYTHREE, 1);
        ts2.put(NINETYFIVE, 7);
        TimeSeries<Double> actualsum = new TimeSeries<Double>();
        actualsum.put(NINETYONE, 10.0);
        actualsum.put(NINETYTWO, NEGONEPTFOUR);
        actualsum.put(NINETYTHREE, TENPTTWO);
        actualsum.put(NINETYFOUR, FIFTEENTWO);
        actualsum.put(NINETYFIVE, 7.0);
        assertTrue(actualsum.equals(ts.plus(ts2)));
    }


    @Test
    public void testYears() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(NINETYTWO, THREEDOTSIX);
        ts.put(NINETYTHREE, NINEDOTTWO);
        ts.put(NINETYFOUR, FIFTEENTWO);
        ts.put(NINETYFIVE, SIXTEENONE);
        ts.put(NINETYSIX, NEGFIFTEENSEVEN);
        Collection<Number> years = ts.years();
        assertTrue(years.contains(NINETYTWO));
        assertTrue(years.contains(NINETYTHREE));
        assertTrue(years.contains(NINETYFOUR));
        assertTrue(years.contains(NINETYFIVE));
        assertTrue(years.contains(NINETYSIX));
    }

    @Test
    public void testData() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(NINETYTWO, THREEDOTSIX);
        ts.put(NINETYTHREE, NINEDOTTWO);
        ts.put(NINETYFOUR, FIFTEENTWO);
        ts.put(NINETYFIVE, SIXTEENONE);
        ts.put(NINETYSIX, NEGFIFTEENSEVEN);
        Collection<Number> data = ts.data();
        assertTrue(data.contains(THREEDOTSIX));
        assertTrue(data.contains(NINEDOTTWO));
        assertTrue(data.contains(FIFTEENTWO));
        assertTrue(data.contains(SIXTEENONE));
        assertTrue(data.contains(NEGFIFTEENSEVEN));
    }

    public static void main(String[] args) {

        jh61b.junit.textui.runClasses(TestTimeSeries.class); 
    }

}
