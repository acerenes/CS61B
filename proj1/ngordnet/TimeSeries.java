package ngordnet;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Set;


public class TimeSeries<T extends Number> extends TreeMap<Integer,T> {


    // A TimeSeries is just a TreeMap with some extra methods. 


    /* Construct new empty TimeSeries */
    public TimeSeries() {
        // Just call the TreeMap constructor, because tis just a TreeMap. 
        new TreeMap();
    }


    /* Creates a copy of ts, but only between startyear & endyear. Inclusive of both end points. */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        // Iterate through ts, get its keys. If its key is between the endpts, grab it+its value and copy it into the new guy.
        new TimeSeries<T>(); // Constructing new TimeSeries.
        Set<Integer> keys = ts.keySet();
        for (Integer key : keys) {
            if (key >= startYear && key <= endYear) {
                put(key, ts.get(key));
            }
        } 
    }


    /* Creates a copy of ts */
    public TimeSeries(TimeSeries<T> ts) {
        new TimeSeries<T>(); // Constructing new TimeSeries.
        Set<Integer> keys = ts.keySet();
        for (Integer key : keys) {
            put(key, ts.get(key));
        }
    }


    /* Returns the quotient of this time series divided by the relevant value in ts. 
        * If ts is missing a key in this time series, return an IllegalArgumentException. */
    public TimeSeries<Double> dividedBy (TimeSeries<? extends Number> ts) {
        // Iterate thrrough this. Get your value and their value, divide. Put into new TimeSeries. 
        TimeSeries<Double> quotient = new TimeSeries<Double>();
        Set<Integer> keys = this.keySet();
        for (Integer key : keys) {
            if (!ts.containsKey(key)) {
                throw new IllegalArgumentException("In method dividedBy, the given argument is missing a key.");
            }
            quotient.put(key, (this.get(key).doubleValue() / ts.get(key).doubleValue()));
        }
        return quotient;
    }



}
