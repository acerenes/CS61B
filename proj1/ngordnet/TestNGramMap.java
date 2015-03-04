package ngordnet;
import org.junit.Test;
import static org.junit.Assert.*;



public class TestNGramMap {

    public static final int ONESEVENFIVE = 175702;
    public static final int SIXNINESEVEN = 697645;
    public static final int TENEIGHT = 108634;

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
        YearlyRecord yr = ngm.getRecord(1736);
        assertEquals(139, yr.count("quantity"));
    }

    @Test 
    public void testTotalCountHistory() {
        NGramMap shortNGM = new NGramMap("./p1data/ngrams/very_short.csv", "./p1data/ngrams/total_counts.csv");
       TimeSeries<Long> totalCountHistory = ngm.totalCountHistory();
       assertEquals(8049773, totalCountHistory.get(1736));
    }




    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestNGramMap.class);
    }
}