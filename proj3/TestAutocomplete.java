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


    public static void main(String[] args) {
            jh61b.junit.textui.runClasses(TestAutocomplete.class);
        }

}
