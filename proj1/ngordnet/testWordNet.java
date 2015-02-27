package ngordnet; 
import org.junit.Test;
import static org.junit.Assert.*; 
import java.util.HashMap;
import java.util.Set; 

public class TestWordNet {




	@Test 
	public void testisNoun() {
		assertTrue(wn.isNoun("jump"));
		assertTrue(wn.isNoun("leap"));
		assertTrue(wn.isNoun("nasal_decongestant")); 
	}

    @Test
    public void testnouns() {
        String nouns[] = {"augmentation", "nasal_decongestant", "change", "action", "actified", "antihistamine", "increase", "descent", "parachuting", "leap", "demotion", "jump"}; 
        Set<String> expected_nouns = new HashSet(Arrays.asList(nouns)); 
        // Thanks to java2s.com for how to create a set without manually adding each element  
        Set<String> actual_nouns = wn.nouns(); 
        assertEquals(expected_nouns, actual_nouns); 
    }

    @Test
    public void testhyponyms() {
        String hyponyms[] = {"agumentation", "increase", "leap", "jump"}; 
        Set<String> expected_hyponyms = new HashSet(Arrays.asList(hyponyms));  
        Set<String> actual_hyponyms = wn.hyponyms("increase"); 
        assertEquals(expected_hyponyms, actual_hyponyms); 
    }

	public static void main(String[] args) {
        WordNet wn = new WordNet("./wordnet/synsets11.txt", "./wordnet/hypernyms11.txt");
		

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