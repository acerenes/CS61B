package ngordnet;
import org.junit.Test;
import static org.junit.Assert.*;



public class TestNGramMap {

    public static final int ONESEVENFIVE = 175702;
    public static final int SIXNINESEVEN = 697645;
    public static final int TENEIGHT = 108634;
    public static final int SEVENTEENTHIRTYSIX = 1736;

    @Test 
    public void testCountInYear() {
        NGramMap ngm = new NGramMap("./p1data/ngrams/words_that_start_with_q.csv", "./p1data/ngrams/total_counts.csv");
        assertEquals(139, ngm.countInYear("quantity", 1736));
        assertEquals(0, ngm.countInYear("questioning", 1514));
    }

    @Test
    public void testGetRecord() {
        NGramMap shortNGM = new NGramMap("./p1data/ngrams/very_short.csv", "./p1data/ngrams/total_counts.csv");
        YearlyRecord jBond = new YearlyRecord();
        jBond.put("airport", ONESEVENFIVE);
        jBond.put("request", SIXNINESEVEN);
        jBond.put("wandered", TENEIGHT);
        YearlyRecord test = shortNGM.getRecord(2007);
        assertEquals(jBond.size(), test.size());
        assertEquals(ONESEVENFIVE, test.count("airport"));
        assertEquals(SIXNINESEVEN, test.count("request"));
        assertEquals(TENEIGHT, test.count("wandered"));

        NGramMap ngm = new NGramMap("./p1data/ngrams/words_that_start_with_q.csv", "./p1data/ngrams/total_counts.csv");
        YearlyRecord yr = ngm.getRecord(SEVENTEENTHIRTYSIX);
        assertEquals(139, yr.count("quantity"));
    }

    @Test 
    public void testTotalCountHistory() {
        NGramMap shortNGM = new NGramMap("./p1data/ngrams/very_short.csv", "./p1data/ngrams/total_counts.csv");
       TimeSeries<Long> totalCountHistory = shortNGM.totalCountHistory();
       assertEquals(8049773, (double) totalCountHistory.get(SEVENTEENTHIRTYSIX), 0);
       assertEquals(4375, (double) totalCountHistory.get(1527), 0);
    }

    @Test 
    public void testCountHistory() {
        NGramMap ngm = new NGramMap("./p1data/ngrams/words_that_start_with_q.csv", "./p1data/ngrams/total_counts.csv");
        TimeSeries<Integer> countHistory = ngm.countHistory("quantity", 1724, 1757);
        assertEquals(34, countHistory.size());
        assertEquals(139, (double) countHistory.get(SEVENTEENTHIRTYSIX), 0);
        assertEquals(1686, (double) countHistory.get(1738), 0);

        TimeSeries<Integer> countHistory2 = ngm.countHistory("quantity");
        assertEquals(400, countHistory2.size());
        assertEquals(719377, (double) countHistory2.get(2008), 0);
        assertEquals(1, (double) countHistory2.get(1505), 0);
    }




    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestNGramMap.class);
    }
}