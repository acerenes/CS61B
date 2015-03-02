package ngordnet;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Set;
import java.util.ArrayList;


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
            // Gotta do this weird .doubleValue thing because apparently you can only do basic operations on primitive types or smth .___.
        }
        return quotient;
    }


    /* Returns the sum of this time series with the given ts. */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> sum = new TimeSeries<Double>();
        double sum1; // Java compiler wasn't happy when I just did double sum1 = stuff + stuff.
        double sum2;
        Set<Integer> keys1 = this.keySet();
        for (Integer key1 : keys1) {
            if (!ts.containsKey(key1)) {
                sum1 = this.get(key1).doubleValue() + 0;
            }
            else {
                sum1 = this.get(key1).doubleValue() + ts.get(key1).doubleValue();
            }
            sum.put(key1, sum1);
        }
        // I think I'll do it 2x - once for this and once for ts, in case of keys that one has that the other doesn't. 
        Set<Integer> keys2 = ts.keySet();
        for (Integer key2 : keys2) {
            if (!this.containsKey(key2)) {
                sum2 = ts.get(key2).doubleValue() + 0;
            }
            else {
                sum2 = ts.get(key2).doubleValue() + this.get(key2).doubleValue();
            }
            sum.put(key2, sum2);
        }
        return sum;
    }


    /* Returns all years for this time series in any order. */
    public Collection<Number> years() {
        Collection<Number> years = new ArrayList<Number>();
        // Thanks a million to Stackoverflow for getting me around the whole can't instantiate Collection interface thing.
        for (Number key : this.keySet()) {
            years.add((Number) key);
        }
        return years;
    }


    /* Returns all data for this time series in any order. */
    public Collection<Number> data() {
        Collection<Number> data = new ArrayList<Number>();
        for (Integer key : this.keySet()) {
            data.add(this.get(key));
        }
        return data;
    }



}
