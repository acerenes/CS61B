package ngordnet; 
import org.junit.Test;
import static org.junit.Assert.*; 
import java.util.HashSet;
import java.util.Set; 
import java.util.Arrays;
import java.util.Collections;

public class TestWordNet {


	@Test 
	public void testisNoun() {
         WordNet wn = new WordNet("./p1data/wordnet/synsets11.txt", "./p1data/wordnet/hyponyms11.txt");
		assertTrue(wn.isNoun("jump"));
		assertTrue(wn.isNoun("leap"));
		assertTrue(wn.isNoun("nasal_decongestant")); 
	}

    @Test
    public void testnouns() {
         WordNet wn = new WordNet("./p1data/wordnet/synsets11.txt", "./p1data/wordnet/hyponyms11.txt");
        String nouns[]= {"augmentation", "nasal_decongestant", "change", "action", "actifed", "antihistamine", "increase", "descent", "parachuting", "leap", "demotion", "jump"}; 
        Set<String> expected_nouns = new HashSet<String>(Arrays.asList(nouns)); 
        // Thanks to java2s.com for how to create a set without manually adding each element  
        Set<String> actual_nouns = wn.nouns();
        assertTrue(actual_nouns.equals(expected_nouns)); 
    }

    @Test
    public void testhyponyms() {
         WordNet wn = new WordNet("./p1data/wordnet/synsets11.txt", "./p1data/wordnet/hyponyms11.txt");
        String hyponyms[] = {"augmentation", "increase", "leap", "jump"}; 
        Set<String> expected_hyponyms = new HashSet(Arrays.asList(hyponyms));  
        Set<String> actual_hyponyms = wn.hyponyms("increase"); 
        System.out.println("Expected hyponyms: " + expected_hyponyms); // TESTING
        System.out.println("Actual hyponyms: " + actual_hyponyms); // TESTING
        assertTrue(actual_hyponyms.equals(expected_hyponyms)); 
    }

	public static void main(String[] args) {
       
		

        /* The code below should print the following: 
            All nouns:
            augmentation
            nasal_decongestant
            change
            action
            actifed
            antihistamine
            increase
            descent
            parachuting
            leap
            demotion
            jump
        
        System.out.println("All nouns:");
        for (String noun : wn.nouns()) {
            System.out.println(noun);
        }*/

        /* The code below should print the following: 
            Hypnoyms of increase:
            augmentation
            increase
            leap
            jump
        
        System.out.println("Hypnoyms of increase:");
        for (String noun : wn.hyponyms("increase")) {
            System.out.println(noun);
        }  */  

		jh61b.junit.textui.runClasses(TestWordNet.class); 


	}
}