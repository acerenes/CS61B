package ngordnet;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestYearlyRecord {

    public static final NINETYFIVE = 95;
    public static final THREEFORTY = 340;
    public static final ONEEIGHTONE = 181;

    @Test
    public void testrank() {
        YearRecord yr = new YearlyRecord();
        yr.put("quayside", NINETYFIVE);
        yr.put("surrogate", THREEFORTY);
        yr.put("merchantman", ONEEIGHTONE);
        assertEquals(1, yr.rank("surrogate"));
        assertEquals(3, yr.rank("quayside"));
    }





    public static void main (String[] args) {
        jh61b.junit.textui.runClasses(TestYearlyRecord.class);
    }
}