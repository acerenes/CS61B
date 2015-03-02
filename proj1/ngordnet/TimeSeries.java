package ngordnet;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Set;


public class TimeSeries<T extends Number> extends TreeMap<Integer,T> {


    // A TimeSeries is just a TreeMap with some extra methods. 

    private TimeSeries<T> copy; 

    /* Construct new empty TimeSeries */
    public TimeSeries() {
        // Just call the TreeMap constructor. 
        new TreeMap();
    }


    /* Creates a copy of ts, but only between startyear & endyear. Inclusive of both end points. */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        // Iterate through ts, get its keys. If its key is between the endpts, grab it+its value and copy it into the new guy.
        copy = new TimeSeries<T>();
        Set<Integer> keys = ts.keySet();
        for (Integer key : keys) {
            if (key >= startYear && key <= endYear) {
                copy.put(key, ts.get(key));
            }
        }
        
    }


    /*// To iterate through a TimeSeries ts, TimeSeries<T> has to be an iterable. 
    private TimeSeriesIter iterator() {
        return new TimeSeriesIter(this);
    }*/


    /*private class TimeSeriesIter implements Iterator<Integer> { // Well, TimeSeries is going to iterate over the keys, which are integers, I guess.     
        private TreeMap<Integer, T> iter;
        private Set<Integer> keys;
        private Integer[] keys_array;
        private int keys_array_pointer;

        private TimeSeriesIter(TimeSeries to_iterate) {
            iter = to_iterate.map;
            keys = iter.keySet();
            keys_array = keys.toArray();
            keys_array_pointer = 0;
        }

        private boolean hasNext() {
            return keys_array_pointer < keys_array.length;
        }

        private Integer next() {
            Integer old_k = keys_array[keys_array_pointer];
            keys_array_pointer = keys_array_pointer + 1;
            return old_k;
        }

        private void remove() {
            throw new UnsupportedOperationException("This TimeSeries iterator doesn't do remove.");
        }

    }*/







}
