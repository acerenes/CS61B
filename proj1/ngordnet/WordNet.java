package ngordnet; 

import edu.princeton.cs.algs4.Digraph;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException; 

public class WordNet {

	Digraph digraph; 
	int vertices; 
	Set<String[]> synset; 
	Set<int[]> hyponym;
	
	
	/** Creates a WordNet using files from synsetFilename and hypernymFilename. **/
	public WordNet(String synsetFilename, String hyponymFilename) {

		/* Am I going to use a set? I'm pretty sure there won't be duplicates, b/c: 
			* Synsets all diff.
				* Even if 2 diff. things wanted to use same synset, would only read it in 1x.
			* Hypernym file also won't. 
				* Maps every synset to its 5 million more specific babies. */

	    synset = new HashSet<String[]>();
	    File synsetfile = new File(synsetFilename);
	    try {
	    	Scanner synsetscanned = new Scanner(synsetfile);
	    	while (synsetscanned.hasNextLine()) {
	    		String synsetstring = synsetscanned.nextLine();
	    		// Can have more than 1 word in the second element, seperated by a space. 
	    		// Going to try & take 2nd element and split that again by space.
	    		String[] synsetarr = synsetstring.split(",");
	    		synset.add(synsetarr); 
	    	}
	    } catch (FileNotFoundException ex) {
	    	System.out.println("The synset file is not valid.");
	    } 
	    

		vertices = synset.size(); // # of synsets should be # of vertices.
		digraph = new Digraph(vertices); // Creates new Digraph with hopefully right # vertices.


		hyponym = new HashSet<int[]>();
		File hyponymfile = new File(hyponymFilename); 
		try {
			Scanner hyponymscanned = new Scanner(hyponymfile);
			while (hyponymscanned.hasNextLine()) {
				String hyponymints = hyponymscanned.nextLine();
				String[] hyponym_Sarray = hyponymints.split(",");
				int[] hyponym_array = new int[hyponym_Sarray.length];
				for (int i = 0; i < hyponym_Sarray.length; i = i + 1){
					try { // Create int from string[].
						int element = Integer.parseInt(hyponym_Sarray[i]);
						// Add to our int[].
						hyponym_array[i] = element;
					} catch (NumberFormatException nf) {
						System.out.println("There is a problem in the hyponym file - an element is not an int.");
					}		
				}
			 	hyponym.add(hyponym_array);
			}
		} catch (FileNotFoundException ex) {
			System.out.println("The hyponym file is not valid.");
		}
		// Hyponym set made; Now iterate through and start mapping.
		Iterator<int[]> iter = hyponym.iterator(); 
		while (iter.hasNext()) {
			int[] hyp_relations = iter.next();
			for (int i = 1; i < hyp_relations.length; i = i + 1) {
				// First is synset; rest are offspring.
				digraph.addEdge(hyp_relations[0], hyp_relations[i]);
			}
		}
	}

	/* Returns true if noun is a word in some synset. */
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

	/* Returns set of all nouns. */
	public Set<String> nouns() {
		Set<String> all_nouns; 
		all_nouns = new HashSet<String>(); 
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


	/* Returns the set of all hyponyms of word including word itself. */
	public Set<String> hyponyms(String word) {
		/* First figure out all synset IDs of the word. 
		 * Then take those IDs, and find the rest of the #s in the hyponym file. 
		 * MUST BE THE FIRST # IN HYPONYM LINE.
		 * Also include own synonyms.
		 * Put all the words in the matching hyponyms into the set. */

		// Haave to get children of children as well.

		Set<String> all_hyponyms = new HashSet<String>();
		Iterator<String[]> syn_iter = this.synset.iterator();
		// Figure out where the word occurs.
		while (syn_iter.hasNext()) {
			String[] syn_array = syn_iter.next();
			String[] words = syn_array[1].split(" "); 
			for (int i = 0; i < words.length; i = i + 1) {
				if (words[i].equals(word)) {
					// Get the synset ID.
					int syn_ID = Integer.parseInt(syn_array[0]); 
					// Get self + synonyms
					Set<String> words2 = this.words(syn_ID);
					for (String add_word : words2) {
						all_hyponyms.add(add_word);
					}

					// Then find ALL YO DESCENDENTS, get their words, and store. 
					Set<Integer> all_children_IDs = this.allSynIDs(syn_ID);
					for (int syn_ids : all_children_IDs) {
						Set<String> add_these_words = this.words(syn_ids);
						for (String add_words : add_these_words) {
							all_hyponyms.add(add_words);
						}
					}
				}
			}
		} 
		return all_hyponyms; 
	}


	/* Returns ALL the synset IDs (descendents) need to take in, given your original synset ID. */
	private Set<Integer> allSynIDs(int syn_id) {
		Set<Integer> allchildren = new HashSet<Integer>(); 
		Iterator<int[]> hyp_iter = this.hyponym.iterator();
		while (hyp_iter.hasNext()) {
			int[] hyp_array = hyp_iter.next();
			if (hyp_array[0] == syn_id) {
				int[] hyp_IDs = Arrays.copyOfRange(hyp_array, 0, hyp_array.length);
				for (int j = 0; j < hyp_IDs.length; j = j + 1) {
					allchildren.add(hyp_IDs[j]);
				}
				// Now have to find the subchildren.
				for (int k = 1; k < hyp_IDs.length; k = k + 1) {
					// Start at 1 because you already found the children of yourself.
					Set<Integer> subchildren = this.allSynIDs(hyp_IDs[k]);
					// Have to take them all one by one into allchildren.
					for (Integer subchild : subchildren) {
						allchildren.add(subchild);
					}
				}
			}
		}
		return allchildren;
	}


	/* Returns the words given the syn ID. */
	private Set<String> words(int syn_id) {
		Set<String> words = new HashSet<String>();
		Iterator<String[]> syn_iter2 = this.synset.iterator();
		while (syn_iter2.hasNext()) {
			String[] syn_array2 = syn_iter2.next();
			String[] words2 = syn_array2[1].split(" ");
			int check_syn_ID = Integer.parseInt(syn_array2[0]);
			if (check_syn_ID == syn_id) {
				// Found right synset, take in words.
				for (int k = 0; k < words2.length; k = k + 1) {
					words.add(words2[k]);
				}
			}
		}
		return words;
	}


}

/* Major thanks to dude at office hours whose name I never figured out (his Macbook said Soham but there's no Soham on the CS61B staff page) who spent 2 hours of his life googling weird java errors and explaining how to fix them to me. */
