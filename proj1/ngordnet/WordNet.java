package ngordnet; 
import java.util.*; 
import edu.princeton.cs.algs4.Digraph;
import java.util.HashMap;
import java.util.Set; 

public class WordNet {

	Digraph digraph; 
	int vertices; 

	/** Creates a WordNet using files from synsetFilename and hypernymFilename **/
	public WordNet(String synsetFilename, String hyponymFilename) {

		// Am I going to use a set? I'm pretty sure there won't be duplicates, because 
		// Synsets are all different- even if 2 different things wanted to use the same synset, would only read it in 1x
		// The hypernym file also won't, because it maps every synset to like its 5 million more specific babies

		// Okay, so I want to store all of the synsets into a set, right? How do that?
		String[] synset_string = StdIn.readAllLines(synsetFilename); // Returns as array of strings
        Set<String> synset = new HashSet(Arrays.asList(synset_string)); 


		vertices = synset.size(); // # of synsets should be # of vertices
		digraph = new Digraph(vertices); // Creates new Digraph with hopefully right # vertices


		// Now I have to analyze the hypononyms
		// Should I store the hyponyms, or just go ahead and map them? I should prbly store them. 
		int[] hyponym_array = StdIn.readAllInts(hyponymFilename);
		Set<int> hyponym = new HashSet(Arrays.asList(hyponym_array));

		// Iterate through the hyponym set, and start mapping
		// A set is a collection, so I can use an iterator
		Iterator<int> iter= hyponym.iterator(); 
		while (iter.hasNext()) {
			// But I'm thinking there's probably an array within a set
			int[] hyp_relations = iter.next();
			for (i = 1; i < hyp_relations.length; i = i + 1) {
				// First is synset; rest are offspring
				digraph.addEdge(hyp_relations[0], hyp_relations[i]);
			}
		}
		// I think that finishes making the Digraph, hopefully? 

	}






}