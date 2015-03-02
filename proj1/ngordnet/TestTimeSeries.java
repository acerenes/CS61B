package ngordnet; 
import org.junit.Test;
import static org.junit.Assert.*; 
import java.util.TreeMap;

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

    /*@Test
    public void testYears() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);

        Collection<Number> years = new ArrayList<String>(Arrays.asList(1992, 1993, 1994, 1995, 1996)); // Thanks to Stackoverflow for this syntax
        assertEquals(years, ts.years());
    }*/



    public static void main(String[] args) {

        jh61b.junit.textui.runClasses(TestTimeSeries.class); 
    }

}
