package ngordnet; 
import edu.princeton.cs.algs4.Digraph;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Scanner;
import java.io.*; 

public class WordNet {

	Digraph digraph; 
	int vertices; 
	Set<String[]> synset; 
	Set<int[]> hyponym;
	String[] words;

	/** Creates a WordNet using files from synsetFilename and hypernymFilename **/
	public WordNet(String synsetFilename, String hyponymFilename) {

		// Am I going to use a set? I'm pretty sure there won't be duplicates, because 
		// Synsets are all different- even if 2 different things wanted to use the same synset, would only read it in 1x
		// The hypernym file also won't, because it maps every synset to like its 5 million more specific babies

	    synset = new HashSet<String[]>();
	    File synset_file = new File(synsetFilename);
	    try {
	    	Scanner synset_scanned = new Scanner(synset_file);
	    	while (synset_scanned.hasNextLine()) {
	    		String synset_string = synset_scanned.nextLine();
	    		System.out.println("The whole string is " + synset_string); // TESTING
	    		String[] synset_arr = synset_string.split(",");
	    		// Okay I see what the problem is. There can be more than 1 word in the second element, seperated by a space. I'm going to try and take the second element and split that again, by space. 
	    		System.out.println("The split string is " + synset_arr[0] + synset_arr[1] + synset_arr[2]); // TESTING
	    		synset.add(synset_arr); 
	    	}
	    } 
	    catch (FileNotFoundException ex) {
	    	System.out.println("The synset file is not valid.");
	    } 
	    

		vertices = synset.size(); // # of synsets should be # of vertices
		digraph = new Digraph(vertices); // Creates new Digraph with hopefully right # vertices


		hyponym = new HashSet<int[]>();
		File hyponym_file = new File(hyponymFilename); 
		try {
			Scanner hyponym_scanned = new Scanner(hyponym_file);
			while (hyponym_scanned.hasNextLine()) {
				System.out.println("inside while loop");
				String hyponym_Ints = hyponym_scanned.nextLine();
				String[] hyponym_Sarray = hyponym_Ints.split(",");
				int[] hyponym_array = new int[hyponym_Sarray.length];
				for (int i = 0; i < hyponym_Sarray.length; i = i + 1){
					try { //create int from string[]
						System.out.println(hyponym_Sarray[i]); // TESTING

						int element = Integer.parseInt(hyponym_Sarray[i]);
						//add to our int[]
						hyponym_array[i] = element;
					}
					catch (NumberFormatException nf) {
						System.out.println("There is a problem in the hyponym file - an element is not an int."); // 
					}		
				}
			 	hyponym.add(hyponym_array);
			}
		}
		catch (FileNotFoundException ex) {
			System.out.println("The hyponym file is not valid.");
		}
		// hyponym set made; Now iterate through and start mapping
		Iterator<int[]> iter = hyponym.iterator(); 
		while (iter.hasNext()) {
			int[] hyp_relations = iter.next();
			for (int i = 1; i < hyp_relations.length; i = i + 1) {
				// First is synset; rest are offspring
				digraph.addEdge(hyp_relations[0], hyp_relations[i]);
			}
		}
	}

	/* Returns true if noun is a word in some synset */
	public boolean isNoun(String noun) {
		Iterator<String[]> syn_iter = this.synset.iterator();
		while (syn_iter.hasNext()) {
			String[] syn_array = syn_iter.next();
			actual_words= syn_array[1].split(" ");
			for (int i = 0; i < actual_words.length; i = i + 1) {
				System.out.println(actual_words[i]); // TESTING
				if (actual_words[i].equals(noun)) { 
				// Okay I can't put an array within an array. Somehow I'll parse it here, then...? And if it equals, then good?
					return true; 
				}
			}
		}
		return false; 
	}

	/* Returns set of all nouns */
	public Set<String> nouns() {
		Set<String> all_nouns; 
		all_nouns = new HashSet<String>(); // lbr I don't even know what I'm doing anymore; just copying my previous code
		Iterator<String[]> syn_iter = this.synset.iterator();
		while (syn_iter.hasNext()) {
			String[] syn_array = syn_iter.next();
			words = syn_array[1].split(" ");
			for (int i = 0; i < words.length; i = i + 1) {
				all_nouns.add(words[i]);
			}
		}
		return all_nouns; 
	}





}

/* Major thanks to dude at office hours whose name I never figured out (his Macbook said Soham but there's no Soham on the CS61B staff page) who spent 2 hours of his life googling weird java errors and explaining how to fix them to me. 