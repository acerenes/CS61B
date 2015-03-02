package ngordnet; 
import org.junit.Test;
import static org.junit.Assert.*; 
import java.util.TreeMap;
import java.util.Collection;


public class TestTimeSeries {


    @Test 
    public void testConstructor() {
        TreeMap tm = new TreeMap();
        tm.put(1992, 3.6);
        tm.put(1993, 9.2);
        tm.put(1994, 15.2);
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        assertTrue(tm.equals(ts));
    }

    @Test 
    public void testLimitedCopier() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);
        TimeSeries<Double> ts_cut = new TimeSeries<Double>();
        ts_cut.put(1993, 9.2);
        ts_cut.put(1994, 15.2);
        TimeSeries<Double> test_cut = new TimeSeries<Double>(ts, 1993, 1994);
        assertTrue(ts_cut.equals(test_cut));
    }


    @Test 
    public void testCopier() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);
        TimeSeries<Double> copy = new TimeSeries<Double>(ts);
        assertTrue(ts.equals(copy));
    }

    @Test
    public void testDividedBy() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(1992, 1.0);
        ts.put(1993, 2.0);
        ts.put(1994, 3.0);
        TimeSeries<Double> test1 = new TimeSeries<Double>();
        test1.put(1992, 2.0);
        test1.put(1993, 2.0);
        test1.put(1994, 1.0);
        TimeSeries<Double> ans1 = new TimeSeries<Double>();
        ans1.put(1992, 0.5);
        ans1.put(1993, 1.0);
        ans1.put(1994, 3.0);
        assertTrue(ans1.equals(ts.dividedBy(test1)));
    } 

    @Test (expected = IllegalArgumentException.class)
    public void testDividedByError() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(1992, 1.0);
        ts.put(1993, 2.0);
        ts.put(1994, 3.0);
        TimeSeries<Double> test2 = new TimeSeries<Double>();
        test2.put(1992, 2.0);
        test2.put(1993, 2.0);
        test2.put(1995, 1.0);
        ts.dividedBy(test2);
        // Thanks to StackOverflow for this weird Exception-testing error. 
    }


    @Test
    public void testPlus() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
        ts2.put(1991, 10);
        ts2.put(1992, -5);
        ts2.put(1993, 1);
        TimeSeries<Double> actualsum = new TimeSeries<Double>();
        actualsum.put(1991, 10.0);
        actualsum.put(1992, -1.4);
        actualsum.put(1993, 10.2);
        actualsum.put(1994, 15.2);
        assertTrue(actualsum.equals(ts.plus(ts2)));
    }


    @Test
    public void testYears() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);
        Collection<Number> years = ts.years();
        assertTrue(years.contains(1992));
        assertTrue(years.contains(1993));
        assertTrue(years.contains(1994));
        assertTrue(years.contains(1995));
        assertTrue(years.contains(1996));
    }

    @Test
    public void testData() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);
        Collection<Number> data = ts.data();
        assertTrue(data.contains(3.6));
        assertTrue(data.contains(9.2));
        assertTrue(data.contains(15.2));
        assertTrue(data.contains(16.1));
        assertTrue(data.contains(-15.7));
    }

    public static void main(String[] args) {

        jh61b.junit.textui.runClasses(TestTimeSeries.class); 
    }

}
