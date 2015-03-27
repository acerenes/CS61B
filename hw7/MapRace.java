import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
// We don't need these! We made our own!

import java.util.Random;

public class MapRace {

    /* Tests the put action the specified number of times. */
    private static long timePuts61B(Map<Integer, Integer> map, 
                int num_puts, int key_range, int val_range) {
        // Thanks to StackOverflow for how to generate random in range.
        long prevTime = System.currentTimeMillis();
        while (num_puts > 0) {
            map.put((int)(Math.random() * (key_range + 1)), (int)(Math.random() * (val_range + 1)));
            num_puts = num_puts - 1;
        }
        long currTime = System.currentTimeMillis();
        return currTime - prevTime;
    }

    /* Tests the get action the specified number of times. */
    private static long timeGets61B(Map<Integer, Integer> map, 
                int num_gets, int key_range) {
        Integer whatever = null; // Initialize.
        long prevTime = System.currentTimeMillis();
        while (num_gets > 0) {
            whatever = map.get((int)(Math.random() * (key_range + 1)));
            num_gets = num_gets - 1;
        }
        long currTime = System.currentTimeMillis();
        return currTime - prevTime;
    }

    /* Tests the get action the specified number of times. */
    private static long timeRemove61B(Map<Integer, Integer> map, 
                int num_removes, int key_range) {
        Integer whatever = null; // Initialize.
        long prevTime = System.currentTimeMillis();
        while (num_removes > 0) {
            whatever = map.remove((int)(Math.random() * (key_range + 1)));
            num_removes = num_removes - 1;
        }
        long currTime = System.currentTimeMillis();
        return currTime - prevTime;
    }

    /* Warms up Java to get the cache hot and ready. If you don't warm up, 
     * you'll see that the first test has an unfair handicap. */
    private static void warmup() {
        Map<Integer, Integer> trashMap1 = new HashMap<Integer, Integer>();
        Map<Integer, Integer> trashMap2 = new TreeMap<Integer, Integer>();
        timePuts61B(trashMap1, MIL, MIL, MIL);
        timePuts61B(trashMap2, MIL, MIL, MIL);
        timeGets61B(trashMap1, MIL, MIL);
        timeGets61B(trashMap2, MIL, MIL);
    }

    private static final int MIL = 1000000;

    private static void run61BTimedTests(int num, int key_range,
                int val_range) {
        Map<Integer, Integer> hMap = new HashMap<Integer, Integer>();
        Map<Integer, Integer> tMap = new TreeMap<Integer, Integer>();

        // TreeMap puts
        long tPuts = timePuts61B(tMap, num, key_range, val_range);
        String tm = "TreeMap " + num + " puts: " + tPuts + " ms.";
        System.out.println(tm);

        // HashMap puts
        long hPuts = timePuts61B(hMap, num, key_range, val_range);
        String hm = "HashMap " + num + " puts: " + hPuts + " ms.";
        System.out.println(hm);

        // TreeMap gets
        long tGets = timeGets61B(tMap, num, key_range);
        String tg = "TreeMap " + num + " gets: " + tGets + " ms.";
        System.out.println(tg);

        // HashMap gets
        long hGets = timeGets61B(hMap, num, key_range);
        String hg = "HashMap " + num + " gets: " + hGets + " ms.";
        System.out.println(hg);

        // HashMap removes
        long hRemove = timeRemove61B(hMap, num, key_range);
        String hr = "HashMap " + num + " removes: " + hRemove + " ms.";
        System.out.println(hr);

        // TreeMap removes
        long tRemove = timeRemove61B(tMap, num, key_range);
        String tr = "TreeMap " + num + " removes: " + tRemove + " ms.";
        System.out.println(tr);
    }

    public static final String followUp() {
        // YOUR ANSWER HERE
        String note = "Note: I used java's HashMap and TreeMap.";
        String answer = "The results were consistent with my expectations. I expected HashMaps to be faster, because for put, they don't require traveling down a tree and refixing links; for get, they only have to access an index of an array and not travel down a tree; and for remove, they don't have to refix all the links broken by removing a node. At the 1 million stage, for get, the data showed that TreeMap was actually faster than HashMap, but huh weird okay.";
        return note + answer;
    }

    public static void main(String[] args) {
        warmup();
        System.out.println("######## 1 Million ########");
        run61BTimedTests(MIL, MIL, MIL);

        System.out.println();
        System.out.println("######## 5 Million ########");
        run61BTimedTests(5 * MIL, 5 * MIL, 5 * MIL);

        System.out.println();
        System.out.println("######## 10 Million ########");
        run61BTimedTests(10 * MIL, 10 * MIL, 10 * MIL);

        System.out.println();
        System.out.println("######## 50 Million ########");
        run61BTimedTests(50 * MIL, 50 * MIL, 50 * MIL);
    }
}