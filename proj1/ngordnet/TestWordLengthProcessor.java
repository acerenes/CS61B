package ngordnet;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestWordLengthProcessor {

    @Test 
    public void testProcess() {
        YearlyRecord yr = new YearlyRecord();
        yr.put("sheep", 100);
        yr.put("dog", 300);
        WordLengthProcessor wlp = new WordLengthProcessor();
        assertEquals(3.5, wlp.process(yr), 0);
    }


    public static void main (String[] args) {
        jh61b.junit.textui.runClasses(TestWordLengthProcessor.class);
    }
}
