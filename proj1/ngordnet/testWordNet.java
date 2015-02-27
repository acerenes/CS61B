package ngordnet; 
import org.junit.Test;
import static org.junit.Assert.*; 

public class TestWordNet {




	@Test 
	public void givenTests() {
		assertTrue(wn.isNoun("jump"));
		assertTrue(wn.isNoun("leap"));
		assertTrue(wn.isNoun("nasal_decongestant")); 
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
        */
        System.out.println("All nouns:");
        for (String noun : wn.nouns()) {
            System.out.println(noun);
        }

        /* The code below should print the following: 
            Hypnoyms of increase:
            augmentation
            increase
            leap
            jump
        */
        System.out.println("Hypnoyms of increase:");
        for (String noun : wn.hyponyms("increase")) {
            System.out.println(noun);
        }    

		jh61b.junit.textui.runClasses(TestWordNet.class); 


	}
}