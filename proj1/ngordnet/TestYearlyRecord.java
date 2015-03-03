package ngordnet;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestYearlyRecord {

    public static final int NINETYFIVE = 95;
    public static final int THREEFORTY = 340;
    public static final int ONEEIGHTONE = 181;


    @Test
    public void testEmptyRank() {
        YearlyRecord yr = new YearlyRecord();
        yr.put("quayside", NINETYFIVE);
        yr.put("surrogate", THREEFORTY);
        yr.put("merchantman", ONEEIGHTONE);
        assertEquals(1, yr.rank("surrogate"));
        assertEquals(2, yr.rank("merchantman"));
        assertEquals(3, yr.rank("quayside"));
    }

    /*@Test
    public void testNonEmptyRank() { OMG CHANGE THIS ALICE DLRGDSLRIUGHLDSRIUH
        YearlyRecord yr = new YearlyRecord();
        yr.put("quayside", NINETYFIVE);
        yr.put("surrogate", THREEFORTY);
        yr.put("merchantman", ONEEIGHTONE);
        assertEquals(1, yr.rank("surrogate"));
        assertEquals(2, yr.rank("merchantman"));
        assertEquals(3, yr.rank("quayside"));
    }*/





    public static void main (String[] args) {
        jh61b.junit.textui.runClasses(TestYearlyRecord.class);
    }
}