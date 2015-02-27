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

	/** Creates a WordNet using files from synsetFilename and hypernymFilename **/
	public WordNet(String synsetFilename, String hyponymFilename) {

		// Am I going to use a set? I'm pretty sure there won't be duplicates, because 
		// Synsets are all different- even if 2 different things wanted to use the same synset, would only read it in 1x
		// The hypernym file also won't, because it maps every synset to like its 5 million more specific babies

	    synset = new HashSet<String[]>();
	    File synset_file = new File(synsetFilename);
	    Scanner synset_scanned = new Scanner(synset_file); 
	    while (synset_scanned.hasNextLine()) {
	    	String synset_string = synset_scanned.nextLine();
	    	String[] synset_arr = synset_string.split(",");
	    	synset.add(synset_arr); 
	    }

		vertices = synset.size(); // # of synsets should be # of vertices
		digraph = new Digraph(vertices); // Creates new Digraph with hopefully right # vertices


		hyponym = new HashSet<int[]>();
		File hyponym_file = new File(hyponymFilename); 
		Scanner hyponym_scanned = new Scanner(File hyponym_file);
		while (hyponym_scanned.hasNextLine()) {
			System.out.println("inside while loop");
			String hyponym_Ints = hyponym_scanned.nextLine();
			String[] hyponym_Sarray = hyponym_Ints.split(",");
			int[] hyponym_array = new int[hyponym_Sarray.length];
			for (int i = 0; i < hyponym_Sarray.length; i = i + 1){
				try {//create int from string[]
				System.out.println(hyponym_Sarray[i]);

				int element = Integer.parseInt(hyponym_Sarray[i]);
				//add to our int[]
				hyponym_array[i] = element;
			} catch (NumberFormatException nf) {
				System.out.println(hyponym_Sarray[i]);
			}
				
			}
			hyponym.add(hyponym_array); 
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

	public boolean isNoun(String noun) {
		Iterator<String[]> syn_iter = synset.iterator();
		while (syn_iter.hasNext()) {
			String[] syn_array = syn_iter.next();
			for (int i = 0; i < syn_array.length; i = i + 1) {
				if (syn_array[i] == noun) {
					return true; 
				}
			}
		}
		return false; 
	}





}