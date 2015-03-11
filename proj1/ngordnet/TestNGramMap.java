package ngordnet;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;



public class TestNGramMap {

    public static final int ONESEVENFIVE = 175702;
    public static final int SIXNINESEVEN = 697645;
    public static final int TENEIGHT = 108634;
    public static final int SEVENTEENTHIRTYSIX = 1736;
    public static final int SEVENTEENTWENTYFOUR = 1724;
    public static final int SEVENTEENFIFTYSEVEN = 1757;
    public static final int FOURHUNDRED = 400;
    public static final int OHFIVE = 2005;
    public static final int OHSIX = 2006;
    public static final int OHSEVEN = 2007;
    public static final int OHEIGHT = 2008;
    public static final int ONETHREENINE = 139;
    public static final int FIFTEENFOURTEEN = 1514;
    public static final int EIGHTLONGNUM = 8049773;
    public static final int FOURTHREESEVENFIVE = 4375;
    public static final int FIFTEENTWENTYSEVEN = 1527;
    public static final int THIRTYFOUR = 34;
    public static final int SIXTEENEIGHTYSIX = 1686;
    public static final int SEVENTEENTHIRTYEIGHT = 1738;
    public static final int SEVENLONGNUM = 719377;
    public static final int FIFTEENOHFIVE = 1505;
    public static final int TWOBIGNUM = 212883;
    public static final int TWOTHOUSAND = 2000;

    @Test 
    public void testCountInYear() {
        NGramMap ngm = new NGramMap("./ngrams/words_that_start_with_q.csv", 
            "./ngrams/total_counts.csv");
        assertEquals(ONETHREENINE, ngm.countInYear("quantity", SEVENTEENTHIRTYSIX));
        assertEquals(0, ngm.countInYear("questioning", FIFTEENFOURTEEN));
    }

    @Test
    public void testGetRecord() {
        NGramMap shortNGM = new NGramMap("./ngrams/very_short.csv", "./ngrams/total_counts.csv");
        YearlyRecord jBond = new YearlyRecord();
        jBond.put("airport", ONESEVENFIVE);
        jBond.put("request", SIXNINESEVEN);
        jBond.put("wandered", TENEIGHT);
        YearlyRecord test = shortNGM.getRecord(OHSEVEN);
        assertEquals(jBond.size(), test.size());
        assertEquals(ONESEVENFIVE, test.count("airport"));
        assertEquals(SIXNINESEVEN, test.count("request"));
        assertEquals(TENEIGHT, test.count("wandered"));

        NGramMap ngm = new NGramMap("./ngrams/words_that_start_with_q.csv", 
            "./ngrams/total_counts.csv");
        YearlyRecord yr = ngm.getRecord(SEVENTEENTHIRTYSIX);
        assertEquals(ONETHREENINE, yr.count("quantity"));
    }

    @Test 
    public void testTotalCountHistory() {
        NGramMap shortNGM = new NGramMap("./ngrams/very_short.csv", "./ngrams/total_counts.csv");
        TimeSeries<Long> totalCountHistory = shortNGM.totalCountHistory();
        assertEquals(EIGHTLONGNUM, (double) totalCountHistory.get(SEVENTEENTHIRTYSIX), 0);
        assertEquals(FOURTHREESEVENFIVE, (double) totalCountHistory.get(FIFTEENTWENTYSEVEN), 0);
    }

    @Test 
    public void testCountHistory() {
        NGramMap ngm = new NGramMap("./ngrams/words_that_start_with_q.csv", "./ngrams/total_counts.csv");
        TimeSeries<Integer> countHistory = ngm.countHistory("quantity", SEVENTEENTWENTYFOUR, SEVENTEENFIFTYSEVEN);
        assertEquals(THIRTYFOUR, countHistory.size());
        assertEquals(ONETHREENINE, (double) countHistory.get(SEVENTEENTHIRTYSIX), 0);
        assertEquals(SIXTEENEIGHTYSIX, (double) countHistory.get(SEVENTEENTHIRTYEIGHT), 0);

        TimeSeries<Integer> countHistory2 = ngm.countHistory("quantity");
        assertEquals(FOURHUNDRED, countHistory2.size());
        assertEquals(SEVENLONGNUM, (double) countHistory2.get(OHEIGHT), 0);
        assertEquals(1, (double) countHistory2.get(FIFTEENOHFIVE), 0);
    }

    @Test
    public void testWeightHistory() {
        NGramMap ngm = new NGramMap("./ngrams/words_that_start_with_q.csv", "./ngrams/total_counts.csv");
        TimeSeries<Double> weightHistory = ngm.weightHistory("quantity", SEVENTEENTWENTYFOUR, SEVENTEENFIFTYSEVEN);
        System.out.println("The weightHistory method with bounds returns " + weightHistory.get(SEVENTEENTHIRTYSIX));
        System.out.println("NGramMapDemo says the method should return roughly 1.7267E-5");
        assertEquals(THIRTYFOUR, weightHistory.size());


        TimeSeries<Double> weightHistory2 = ngm.weightHistory("quantity");
        System.out.println("Part 2: weightHistory method with no bounds returns " + weightHistory2.get(SEVENTEENTHIRTYSIX));
        System.out.println("NGramMapDemo says the method should return roughly 1.7267E-5.");
        assertEquals(FOURHUNDRED, weightHistory2.size());
    }

    @Test
    public void testSummedWeightHistory() {
        NGramMap ngm = new NGramMap("./ngrams/words_that_start_with_q.csv", "./ngrams/total_counts.csv");
        ArrayList<String> words = new ArrayList<String>();
        words.add("quantity");
        words.add("quality");
        TimeSeries<Double> sum = ngm.summedWeightHistory(words, SEVENTEENTWENTYFOUR, SEVENTEENFIFTYSEVEN);
        System.out.println("summedWeightHistory returns " + sum.get(SEVENTEENTHIRTYSIX));
        System.out.println("NGramMapDemo says it should be about 3.875E-5.");
        assertEquals(THIRTYFOUR, sum.size());

        TimeSeries<Double> sum2 = ngm.summedWeightHistory(words);
        System.out.println("Part 2 summedWeightHistory returns " + sum2.get(SEVENTEENTHIRTYSIX));
        System.out.println("NGramMapDemo says it should be about 3.875E-5.");
        // I don't know what the size of this collection should be, so no test. Just faith and hope.
        System.out.println("The size of the unbounded summedWeightHistory is " + sum2.size());
    }


    @Test 
    public void testCodeSpeed() {
        NGramMap ngmAll = new NGramMap("./ngrams/all_words.csv", "./ngrams/total_counts.csv");
        assertEquals(TWOBIGNUM, ngmAll.countInYear("deny", TWOTHOUSAND));
        assertEquals(5, ngmAll.countInYear("Art", FIFTEENOHFIVE));
    }

    @Test
    public void testProcessedHistory() {
        NGramMap ngm = new NGramMap("./ngrams/very_short.csv", "./ngrams/total_counts.csv");
        WordLengthProcessor wlp = new WordLengthProcessor();
        TimeSeries<Double> processedHist = ngm.processedHistory(OHSIX, OHEIGHT, wlp);
        System.out.println("Processed History 2006 should be about 7.1145.");
        System.out.println("Code 2006 returns " + processedHist.get(OHSIX));
        System.out.println("Processed History 2007 should be about 7.1106.");
        System.out.println("Code 2007 returns " + processedHist.get(OHSEVEN));
        System.out.println("Processed History 2008 should be about 7.15007.");
        System.out.println("Code 2008 returns " + processedHist.get(OHEIGHT));
        System.out.println("THIS WAS A TEST THAT USED PRINT CHECK ABOVE STUFF ALICE.");
    }

    @Test
    public void testProcessedHistory2() {
        NGramMap ngm = new NGramMap("./ngrams/very_short.csv", "./ngrams/total_counts.csv");
        WordLengthProcessor wlp = new WordLengthProcessor();
        TimeSeries<Double> processedHist = ngm.processedHistory(wlp);
        System.out.println("Processed History2 2005 should be about 7.11476");
        System.out.println("Code 2005 returns " + processedHist.get(OHFIVE));
        System.out.println("Processed History2 2006 should be about 7.1145.");
        System.out.println("Code 2006 returns " + processedHist.get(OHSIX));
        System.out.println("Processed History2 2007 should be about 7.1106.");
        System.out.println("Code 2007 returns " + processedHist.get(OHSEVEN));
        System.out.println("Processed History2 2008 should be about 7.15007.");
        System.out.println("Code 2008 returns " + processedHist.get(OHEIGHT));
        System.out.println("THIS WAS A TEST THAT USED PRINT CHECK ABOVE STUFF ALICE.");
    }


    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestNGramMap.class);
    }
}
