package ngordnet; 
import org.junit.Test;
import static org.junit.Assert.*; 

public class TestTimeSeries {

    @Test
    public static void testYears() {
        TimeSeries<Double> ts = new TimeSeries<Double>();
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);

        Collection<Number> years = new ArrayList<String>(Arrays.asList(1992, 1993, 1994, 1995, 1996)); // Thanks to Stackoverflow for this syntax
        assertEquals(years, ts.years());
    }



    public static void main(String[] args) {

        jh61b.junit.textui.runClasses(TestTimeSeries.class); 
    }

}
