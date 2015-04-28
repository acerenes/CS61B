import org.junit.Test;
import static org.junit.Assert.*;

public class TestAutocomplete {

    @Test
    public void testStructure() {
        String[] terms = new String[] {"smog", "buck", "sad", "spite", "spit", "spy"};
        double[] weights = {(double) 5, (double) 10, (double) 12, (double) 20, (double) 15, (double) 7};
        Autocomplete a = new Autocomplete(terms, weights);
        assertNotNull(a);
    }

    @Test
    public void testWeightOf() {
        String[] terms2 = new String[] {"smog", "buck", "sad", "spite", "spit", "spy"};
        double[] weights2 = {(double) 5, (double) 10, (double) 12, (double) 20, (double) 15, (double) 7};
        Autocomplete a2 = new Autocomplete(terms2, weights2);

        assertEquals(a2.weightOf("spit"), 15, 0);
        assertEquals(a2.weightOf("spite"), 20, 0);
        assertEquals(a2.weightOf("smmg"), 0, 0);
    }

    @Test
    public void testTopMatch() {
        String[] terms3 = new String[] {"smog", "buck", "sad", "spite", "spit", "spy"};
        double[] weights3 = {(double) 5, (double) 10, (double) 12, (double) 20, (double) 15, (double) 7};
        Autocomplete a3 = new Autocomplete(terms3, weights3);

        assertEquals(a3.topMatch("s"), "spite");
        assertNull(a3.topMatch("soie"));
    }

    @Test
    public void testTopMatches() {
        String[] terms4 = new String[] {"smog", "buck", "sad", "spite", "spit", "spy"};
        double[] weights4 = {(double) 5, (double) 10, (double) 12, (double) 20, (double) 15, (double) 7};
        Autocomplete a4 = new Autocomplete(terms4, weights4);

        Iterable<String> checkMatches = a4.topMatches("s", 3);
        String[] theMatches = new String[3];
        
        int i = 0;
        for (String match : checkMatches) {
            theMatches[i] = match;
            i = i + 1;
        }

        assertEquals("spite", theMatches[0]);
        assertEquals("spit", theMatches[1]);
        assertEquals("sad", theMatches[2]);
    }

    @Test
    public void testTopMatches2() {
        String[] terms5 = new String[] {"smog", "buck", "sad", "spite", "spit", "spy"};
        double[] weights5 = {(double) 5, (double) 10, (double) 12, (double) 20, (double) 15, (double) 7};
        Autocomplete a5 = new Autocomplete(terms5, weights5);

        Iterable<String> checkMatches = a5.topMatches("s", 4);
        String[] theMatches = new String[4];
        
        int i = 0;
        for (String match : checkMatches) {
            theMatches[i] = match;
            i = i + 1;
        }

        Iterable<String> checkMatchesNull = a5.topMatches("smoggy", 1);
        assertNull(checkMatchesNull);

        assertEquals("spite", theMatches[0]);
        assertEquals("spit", theMatches[1]);
        assertEquals("sad", theMatches[2]);
        assertEquals("spy", theMatches[3]);
    }


    public static void main(String[] args) {
            jh61b.junit.textui.runClasses(TestAutocomplete.class);
        }

}
