package ngordnet; 
import java.util.*; 
import edu.princeton.cs.algs4.Digraph;
import java.util.HashMap;
import java.util.Set; 

public class WordNet {

	Digraph digraph; 
	int vertices; 
	Set<String[]> synset; 
	Set<Integer[]> hyponym;

	/** Creates a WordNet using files from synsetFilename and hypernymFilename **/
	public WordNet(String synsetFilename, String hyponymFilename) {

		// Am I going to use a set? I'm pretty sure there won't be duplicates, because 
		// Synsets are all different- even if 2 different things wanted to use the same synset, would only read it in 1x
		// The hypernym file also won't, because it maps every synset to like its 5 million more specific babies

		// Okay, so I want to store all of the synsets into a set, right? How do that?
		/*while (StdIn.isEmpty(synsetFilename)) {
			String[] synset_string = StdIn.readLine(synsetFilename); // Returns as array of strings
	        Set<String> synset = new HashSet(Arrays.asList(synset_string)); 
	    }
*/
	    synset = new HashSet<String[]>(); 
	    In synset_file = new In(synsetFilename); // At this point I'm just copying hw1
	    while (!synset_file.isEmpty()) {
	    	String[] synset_string = synset_file.readLine();
	    	synset.add(synset_string); 
	    }

		vertices = synset.size(); // # of synsets should be # of vertices
		digraph = new Digraph(vertices); // Creates new Digraph with hopefully right # vertices


		hyponym = new HashSet<Integer[]>(); 
		In hyponym_file = new In(hyponymFilename);
		while (!hyponym_file.isEmpty()) {
			Integer[] hyponym_array = hyponym_file.readLine();
			hyponym.add(hyponym_array); 
		}
		// hyponym set made; Now iterate through and start mapping
		Iterator<Integer[]> iter= hyponym.iterator(); 
		while (iter.hasNext()) {
			Integer[] hyp_relations = iter.next();
			for (int i = 1; i < hyp_relations.length; i = i + 1) {
				// First is synset; rest are offspring
				digraph.addEdge(hyp_relations[0], hyp_relations[i]);
			}
		}
		// I think that finishes making the Digraph, hopefully? 

	}






}