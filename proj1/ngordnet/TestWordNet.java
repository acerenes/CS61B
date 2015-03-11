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
        String[] nouns = {"augmentation", "nasal_decongestant", "change", "action", "actifed", 
            "antihistamine", "increase", "descent", "parachuting", "leap", "demotion", "jump"}; 
        Set<String> expectedNouns = new HashSet<String>(Arrays.asList(nouns)); 
        // Thanks to java2s.com for how to create a set without manually adding each element
        Set<String> actualNouns = wn.nouns();
        assertTrue(actualNouns.equals(expectedNouns)); 
    }

    @Test
    public void testhyponyms() {
        WordNet wn = new WordNet("./wordnet/synsets11.txt", "./wordnet/hyponyms11.txt");
        String[] hyponyms = {"augmentation", "increase", "leap", "jump"}; 
        Set<String> expectedHyponyms = new HashSet(Arrays.asList(hyponyms));  
        Set<String> actualHyponyms = wn.hyponyms("increase");
        assertTrue(actualHyponyms.equals(expectedHyponyms)); 

        String[] hyponyms2 = {"parachuting", "leap", "jump"};
        Set<String> expectedHyponyms2 = new HashSet(Arrays.asList(hyponyms2));
        Set<String> actualHyponyms2 = wn.hyponyms("jump");
        assertTrue(actualHyponyms2.equals(expectedHyponyms2));

        WordNet wn2 = new WordNet("./wordnet/synsets14.txt", "./wordnet/hyponyms14.txt");
        String[] hyponyms3 = {"alteration", "saltation", "modification", 
            "change", "variation", "increase", "transition", "demotion", "leap", "jump"};
        Set<String> expectedHyponyms3 = new HashSet(Arrays.asList(hyponyms3));
        Set<String> actualHyponyms3 = wn2.hyponyms("change");
        assertTrue(actualHyponyms3.equals(expectedHyponyms3));

        String[] hyponyms4 = {"event", "happening", "occurrence", "occurrent", "natural_event", 
            "change", "alteration", "modification", "transition", "increase", "jump", 
            "leap", "saltation", "act", "human_action", "human_activity", 
            "action", "demotion", "variation", "adjustment", "conversion", "mutation"};
        Set<String> expectedHyponyms4 = new HashSet(Arrays.asList(hyponyms4));
        Set<String> actualHyponyms4 = wn2.hyponyms("event");
        assertTrue(actualHyponyms4.equals(expectedHyponyms4));
    }

    public static void main(String[] args) {

        jh61b.junit.textui.runClasses(TestWordNet.class); 


    }
}
