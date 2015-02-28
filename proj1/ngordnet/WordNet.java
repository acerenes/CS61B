package ngordnet; 
import edu.princeton.cs.algs4.Digraph;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Arrays;
import java.io.*; 

public class WordNet {

	Digraph digraph; 
	int vertices; 
	Set<String[]> synset; 
	Set<int[]> hyponym;
	

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
	    		String[] synset_arr = synset_string.split(",");
	    		// Okay I see what the problem is. There can be more than 1 word in the second element, seperated by a space. I'm going to try and take the second element and split that again, by space.
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
				String hyponym_Ints = hyponym_scanned.nextLine();
				String[] hyponym_Sarray = hyponym_Ints.split(",");
				int[] hyponym_array = new int[hyponym_Sarray.length];
				for (int i = 0; i < hyponym_Sarray.length; i = i + 1){
					try { // Create int from string[]
						int element = Integer.parseInt(hyponym_Sarray[i]);
						// Add to our int[]
						hyponym_array[i] = element;
					}
					catch (NumberFormatException nf) {
						System.out.println("There is a problem in the hyponym file - an element is not an int.");
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
			String[] actual_words= syn_array[1].split(" ");
			for (int i = 0; i < actual_words.length; i = i + 1) {
				if (actual_words[i].equals(noun)) {
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
			String[] words = syn_array[1].split(" ");
			for (int i = 0; i < words.length; i = i + 1) {
				all_nouns.add(words[i]);
			}
		}
		return all_nouns; 
	}


	/* Returns the set of all hyponyms of word including word itself */
	public Set<String> hyponyms(String word) {
		// First figure out all synset IDs of the word
		// Then take those IDs, and find the, like, rest of the #s in the hyponym file
		// Put all the words in the matching hyponyms into the set
		// Then put in word
		Set<String> all_hyponyms;
		all_hyponyms = new HashSet<String>();
		Iterator<String[]> syn_iter = this.synset.iterator();
		// Figure out where the word occurs
		while (syn_iter.hasNext()) {
			String[] syn_array = syn_iter.next();
			String[] words = syn_array[1].split(" "); 
			for (int i = 0; i < words.length; i = i + 1) {
				if (words[i].equals(word)) {
					// Get the synset ID
					int syn_ID = Integer.parseInt(syn_array[0]); 
					// Take the ID and find its hyponym IDs in the hyponym file
					// FINDING THE SYN_ID IN THE HYPONYM FILE
					Iterator<int[]> hyp_iter = this.hyponym.iterator();
					while (hyp_iter.hasNext()) {
						int[] hyp_array = hyp_iter.next();
						if (hyp_array[0] == syn_ID) {
							int[] hyp_IDs = Arrays.copyOfRange(hyp_array, 1, hyp_array.length); // hyp_array[1:]
							for (int j = 0; j < hyp_IDs.length; j= j + 1) {
								// Take each of the hyponym IDs, find the entry in the synset, take the words, and store them into the returning set
								Iterator<String[]> syn_iter_2 = this.synset.iterator(); 
								while (syn_iter_2.hasNext()) {
									String[] syn_array_2 = syn_iter_2.next();
									String[] words_2 = syn_array_2[1].split(" ");
									int check_syn_ID = Integer.parseInt(syn_array_2[0]);
									if (check_syn_ID == hyp_IDs[j]) {
										// Take in the words
										for (int k = 0; k < words_2.length; k = k + 1) {
											all_hyponyms.add(words_2[k]);
										}
									}
								}
							}
						}
					}
				}
			}
		}
		all_hyponyms.add(word);
		return all_hyponyms; 
	}





}

/* Major thanks to dude at office hours whose name I never figured out (his Macbook said Soham but there's no Soham on the CS61B staff page) who spent 2 hours of his life googling weird java errors and explaining how to fix them to me. */