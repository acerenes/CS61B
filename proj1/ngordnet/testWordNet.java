package ngordnet; 
import org.junit.Test;
import static org.junit.Assert.*; 
import java.util.HashSet;
import java.util.Set; 
import java.util.Arrays;

public class TestWordNet {


    @Test 
    public void testisNoun() {
         WordNet wn = new WordNet("./wordnet/synsets11.txt", "./wordnet/hyponyms11.txt");
        assertTrue(wn.isNoun("jump"));
        assertTrue(wn.isNoun("leap"));
        assertTrue(wn.isNoun("nasal_decongestant")); 
    }

    @Test
    public void testnouns() {
         WordNet wn = new WordNet("./wordnet/synsets11.txt", "./wordnet/hyponyms11.txt");
        String nouns[]= {"augmentation", "nasal_decongestant", "change", "action", "actifed", "antihistamine", "increase", "descent", "parachuting", "leap", "demotion", "jump"}; 
        Set<String> expected_nouns = new HashSet<String>(Arrays.asList(nouns)); 
        // Thanks to java2s.com for how to create a set without manually adding each element  
        Set<String> actual_nouns = wn.nouns();
        assertTrue(actual_nouns.equals(expected_nouns)); 
    }

    @Test
    public void testhyponyms() {
         WordNet wn = new WordNet("./wordnet/synsets11.txt", "./wordnet/hyponyms11.txt");
        String hyponyms[] = {"augmentation", "increase", "leap", "jump"}; 
        Set<String> expected_hyponyms = new HashSet(Arrays.asList(hyponyms));  
        Set<String> actual_hyponyms = wn.hyponyms("increase");
        assertTrue(actual_hyponyms.equals(expected_hyponyms)); 

        String hyponyms2[] = {"parachuting", "leap", "jump"};
        Set<String> expected_hyponyms2 = new HashSet(Arrays.asList(hyponyms2));
        Set<String> actual_hyponyms2 = wn.hyponyms("jump");
        assertTrue(actual_hyponyms2.equals(expected_hyponyms2));

        WordNet wn2 = new WordNet("./wordnet/synsets14.txt", "./wordnet/hyponyms14.txt");
        String hyponyms3[] = {"alteration", "saltation", "modification", "change", "variation", "increase", "transition", "demotion", "leap", "jump"};
        Set<String> expected_hyponyms3 = new HashSet(Arrays.asList(hyponyms3));
        Set<String> actual_hyponyms3 = wn2.hyponyms("change");
        assertTrue(actual_hyponyms3.equals(expected_hyponyms3));
    }

    public static void main(String[] args) {

        jh61b.junit.textui.runClasses(TestWordNet.class); 


    }
}
