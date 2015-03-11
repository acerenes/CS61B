package ngordnet;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestWordLengthProcessor {

    public static final int ONEHUNDRED = 100;
    public static final int THREEHUNDRED = 300;
    public static final double THREEPTFIVE = 3.5;

    @Test 
    public void testProcess() {
        YearlyRecord yr = new YearlyRecord();
        yr.put("sheep", ONEHUNDRED);
        yr.put("dog", THREEHUNDRED);
        WordLengthProcessor wlp = new WordLengthProcessor();
        assertEquals(THREEPTFIVE, wlp.process(yr), 0);
    }


    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestWordLengthProcessor.class);
    }
}
