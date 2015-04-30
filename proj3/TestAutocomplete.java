import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class TestAutocomplete {

    /*@Test
    public void testStructureBasic() {
        String[] terms = new String[] {"smog", "buck", "sad"};
        double[] weights = {(double) 5, (double) 10, (double) 12};
        Autocomplete a = new Autocomplete(terms, weights);
        assertNotNull(a);
    }

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

        assertEquals("spite", theMatches[0]);
        assertEquals("spit", theMatches[1]);
        assertEquals("sad", theMatches[2]);
        assertEquals("spy", theMatches[3]);
    }

    @Test
    public void testTopMatchesWithSelf() {
        String[] terms = new String[] {"the", "they", "their", "them", "there"};
        double[] weights = {56271872.0, 3340398.0, 2820265.0, 2509917.0, 1961200.0};
        Autocomplete a = new Autocomplete(terms, weights);

        Iterable<String> checkMatches = a.topMatches("the", 5);
        String[] theMatches = new String[5];
        
        int i = 0;
        for (String match : checkMatches) {
            theMatches[i] = match;
            i = i + 1;
        }

        assertEquals("the", theMatches[0]);
        assertEquals("they", theMatches[1]);
        assertEquals("their", theMatches[2]);
        assertEquals("them", theMatches[3]);
        assertEquals("there", theMatches[4]);
    }

    @Test
    public void testComp() {
        String[] terms = new String[] {"come", "comes", "comedy", "comely", "company", "complete", "companion", "completely", "comply"};
        double[] weights = {873007.0, 153299.0, 11718.2, 5122.6, 133159.0, 78039.8, 60384.9, 52050.3, 44817.7};
        Autocomplete a = new Autocomplete(terms, weights);

        Iterable<String> checkMatches = a.topMatches("comp", 5);
        String[] theMatches = new String[5];
        
        int i = 0;
        for (String match : checkMatches) {
            theMatches[i] = match;
            i = i + 1;
        }

        assertEquals("company", theMatches[0]);
        assertEquals("complete", theMatches[1]);
        assertEquals("companion", theMatches[2]);
        assertEquals("completely", theMatches[3]);
        assertEquals("comply", theMatches[4]);
    }

    @Test
    public void testAOrder() {
        String[] terms = new String[] {"and", "as", "at", "an", "a"};
        double[] weights = {29944184.0, 7037543.0, 5091841.0, 2641417.0, 1135294.0};
        Autocomplete a = new Autocomplete(terms, weights);

        Iterable<String> checkMatches = a.topMatches("a", 5);
        String[] theMatches = new String[5];
        
        int i = 0;
        for (String match : checkMatches) {
            theMatches[i] = match;
            //System.out.println(match);
            i = i + 1;
        }

        assertEquals("and", theMatches[0]);
        assertEquals("as", theMatches[1]);
        assertEquals("at", theMatches[2]);
        assertEquals("an", theMatches[3]);
        assertEquals("a", theMatches[4]);
    }

    @Test
    public void testMCities() {
        String[] terms = new String[] {"Mumbai, India", "Mexico City, Distrito Federal, Mexico", "Aye Yo, CA"};
        double[] weights = {12691836.0, 12294193.0, 3255944.0};
        Autocomplete a = new Autocomplete(terms, weights);

        Iterable<String> checkMatches = a.topMatches("M", 7);
        String[] theMatches = new String[2];
        
        int i = 0;
        for (String match : checkMatches) {
            theMatches[i] = match;
            i = i + 1;
        }

        assertEquals(2, theMatches.length);
        assertEquals("Mumbai, India", theMatches[0]);
        assertEquals("Mexico City, Distrito Federal, Mexico", theMatches[1]);
    }

    @Test(expected = IllegalArgumentException.class) 
    public void testDifferentLength() {
        String[] terms = new String[] {"Mumbai, India", "Mexico City, Distrito Federal, Mexico", "Aye Yo, CA"};
        double[] weights = {12691836.0, 12294193.0, 3255944.0, 3871829.9};
        Autocomplete a = new Autocomplete(terms, weights);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateInputs() {
        String[] terms = new String[] {"Mumbai, India", "Mexico City, Distrito Federal, Mexico", "Aye Yo, CA", "Mumbai, India"};
        double[] weights = {12691836.0, 12294193.0, 3255944.0, 3871829.9};
        Autocomplete a = new Autocomplete(terms, weights);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeWeights() {
        String[] terms = new String[] {"Mumbai, India", "Mexico City, Distrito Federal, Mexico", "Aye Yo, CA"};
        double[] weights = {12691836.0, 12294193.0, -3255944.0};
        Autocomplete a = new Autocomplete(terms, weights);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonPositiveKTerms() {
        String[] terms = new String[] {"Mumbai, India", "Mexico City, Distrito Federal, Mexico", "Aye Yo, CA"};
        double[] weights = {12691836.0, 12294193.0, 3255944.0};
        Autocomplete a = new Autocomplete(terms, weights);

        Iterable<String> checkMatches = a.topMatches("M", 0);
    }


    // TAKE THIS OUTDLUGHDLRSIUHGDLIUHTIRUHTDLSIUHRTLDISHLTDISRUHILTRUHILTRUHGLDIURHGLIDSHGILTDSHGLIDSUHLIDHSLISUD
    @Test
    public void testTiny() {
        String[] terms = new String[] {"smog", "buck", "sad", "spite", "spit", "spy"};
        double[] weights = {5.0, 10.0, 12.0, 20.0, 15.0, 7.0};
        Autocomplete a = new Autocomplete(terms, weights);

        Iterable<String> checkMatches = a.topMatches("buck", 1);
        String[] theMatches = new String[1];
        
        int i = 0;
        for (String match : checkMatches) {
            theMatches[i] = match;
            i = i + 1;
        }

        assertEquals("buck", theMatches[0]);

        checkMatches = a.topMatches("sa", 1);
        theMatches = new String[1];
        
        i = 0;
        for (String match : checkMatches) {
            theMatches[i] = match;
            i = i + 1;
        }

        assertEquals("sad", theMatches[0]);
    }

    @Test
    public void testTinyWeight() {
        String[] terms = new String[] {"smog", "buck", "sad", "spite", "spit", "spy"};
        double[] weights = {5.0, 10.0, 12.0, 20.0, 15.0, 7.0};
        Autocomplete a = new Autocomplete(terms, weights);

        assertEquals(10.0, a.weightOf("buck"), 0);
        assertEquals("buck", a.topMatch("buck"));
        assertEquals(10.0, a.weightOf(a.topMatch("buck")), 0);
    }

    @Test
    public void testTinyWeightSa() {
        String[] terms = new String[] {"smog", "buck", "sad", "spite", "spit", "spy"};
        double[] weights = {5.0, 10.0, 12.0, 20.0, 15.0, 7.0};
        Autocomplete a = new Autocomplete(terms, weights);

        assertEquals(12.0, a.weightOf("sad"), 0);
        assertEquals("sad", a.topMatch("sa"));
        assertEquals(12.0, a.weightOf(a.topMatch("sa")), 0);
    }

    @Test
    public void testTinySm() {
        String[] terms = new String[] {"smog", "buck", "sad", "spite", "spit", "spy"};
        double[] weights = {5.0, 10.0, 12.0, 20.0, 15.0, 7.0};
        Autocomplete a = new Autocomplete(terms, weights);

        assertEquals(5.0, a.weightOf("smog"), 0);
        assertEquals("smog", a.topMatch("sm"));
        assertEquals(5.0, a.weightOf(a.topMatch("sm")), 0);
    }

    @Test
    public void testSimilarNames() {
        String[] terms = new String[] {"Bree", "Brandin", "Brandie", "Brandio", "Brandii", "Brandik", "Brandic", "Brandiy", "Brandit", "Brandkick", "Brandraisin"};
        double[] weights = {0.1, 28.0, 20.0, 37.7, 48.3, 94.2, 102.1, 674.0, 675.1, 2040.1, 6700.4};
        Autocomplete a = new Autocomplete(terms, weights);

        Iterable<String> checkMatches = a.topMatches("Brand", 10);
        String[] theMatches = new String[10];
        
        int i = 0;
        for (String match : checkMatches) {
            theMatches[i] = match;
            i = i + 1;
        }

        assertEquals("Brandraisin", theMatches[0]);
        assertEquals("Brandkick", theMatches[1]);
        assertEquals("Brandit", theMatches[2]);
        assertEquals("Brandiy", theMatches[3]);
        assertEquals("Brandic", theMatches[4]);
        assertEquals("Brandik", theMatches[5]);
        assertEquals("Brandii", theMatches[6]);
        assertEquals("Brandio", theMatches[7]);
        assertEquals("Brandin", theMatches[8]);
        assertEquals("Brandie", theMatches[9]);
    }

    @Test
    public void testEmptyPrefix() {
        String[] terms = new String[] {"the", "and", "I"};
        double[] weights = {1000.0, 500.0, 250.0};
        Autocomplete a = new Autocomplete(terms, weights);

        Iterable<String> checkMatches = a.topMatches("", 20);
        String[] theMatches = new String[3];
        
        int i = 0;
        for (String match : checkMatches) {
            theMatches[i] = match;
            i = i + 1;
        }

        assertEquals("the", theMatches[0]);
        assertEquals("and", theMatches[1]);
        assertEquals("I", theMatches[2]);

        assertEquals("the", a.topMatch(""));

        ArrayList<String> shouldBeEmpty = (ArrayList<String>) a.topMatches("sdiushiuh", 20);
        assertTrue(shouldBeEmpty.isEmpty());
    }*/

    @Test
    public void testWhyStackOverflow() {
        In in = new In("wiktionary.txt");
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }

        Autocomplete autocomplete = new Autocomplete(terms, weights);

        for (String term : autocomplete.topMatches("", 20)) {
            StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);
        }
    }





    public static void main(String[] args) {
            jh61b.junit.textui.runClasses(TestAutocomplete.class);
        }

}
