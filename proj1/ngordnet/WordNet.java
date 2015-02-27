package ngordnet; 
import edu.princeton.cs.algs4.Digraph;

public class WordNet {

	Diagraph digraph; 
	int vertices; 

	/** Creates a WordNet using files from synsetFilename and hypernymFilename **/
	public WordNet(String synsetFilename, String hyponymFilename) {

		// Am I going to use a set? I'm pretty sure there won't be duplicates, because 
		// Synsets are all different- even if 2 different things wanted to use the same synset, would only read it in 1x
		// The hypernym file also won't, because it maps every synset to like its 5 million more specific babies

		// Okay, so I want to store all of the synsets into a set, right? How do that?
		String[] synset_string = StdDraw.readAllLines(synsetFilename); // Returns as array of strings
        Set<String> synset = new HashSet(Arrays.asList(hyponyms)); 

		vertices = synset.size(); // # of synsets should be # of vertices
		diagraph = Digraph(vertices); // Creates new Digraph with hopefully right # vertices
		
	}






}