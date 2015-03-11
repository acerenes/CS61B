package ngordnet;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Set;
import java.util.ArrayList;


public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {

    /* Construct new empty TimeSeries */
    public TimeSeries() {
        super();
    }


    /* Creates a copy of ts, but only between startyear & endyear. Inclusive of both end points. */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        new TimeSeries();
        Set<Integer> keys = ts.keySet();
        for (Integer key : keys) {
            if (key >= startYear && key <= endYear) {
                put(key, ts.get(key));
            }
        } 
    }


    /* Creates a copy of ts */
    public TimeSeries(TimeSeries<T> ts) {
        new TimeSeries<T>(); 
        Set<Integer> keys = ts.keySet();
        for (Integer key : keys) {
            put(key, ts.get(key));
        }
    }


    /* Returns the quotient of this time series divided by the relevant value in ts. 
        * If ts is missing a key in this time series, return an IllegalArgumentException. */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> quotient = new TimeSeries<Double>();
        Set<Integer> keys = this.keySet();
        for (Integer key : keys) {
            if (!ts.containsKey(key)) {
                throw new IllegalArgumentException("In dividedBy, argument is missing a key.");
            }
            quotient.put(key, (this.get(key).doubleValue() / ts.get(key).doubleValue()));
        }
        return quotient;
    }


    /* Returns the sum of this time series with the given ts. */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
        TimeSeries<Double> sum = new TimeSeries<Double>();
        double sum1; 
        double sum2;
        Set<Integer> keys1 = this.keySet();
        for (Integer key1 : keys1) {
            if (!ts.containsKey(key1)) {
                sum1 = this.get(key1).doubleValue() + 0;
            } else {
                sum1 = this.get(key1).doubleValue() + ts.get(key1).doubleValue();
            }
            sum.put(key1, sum1);
        }
        // Do 2x - for this and for ts, in case of keys that one has that the other doesn't. 
        Set<Integer> keys2 = ts.keySet();
        for (Integer key2 : keys2) {
            if (!this.containsKey(key2)) {
                sum2 = ts.get(key2).doubleValue() + 0;
            } else {
                sum2 = ts.get(key2).doubleValue() + this.get(key2).doubleValue();
            }
            sum.put(key2, sum2);
        }
        return sum;
    }


    /* Returns all years for this time series in any order. */
    public Collection<Number> years() {
        Collection<Number> years = new ArrayList<Number>();
        // Thanks Stackoverflow for getting around the can't instantiate Collection interface thing.
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
