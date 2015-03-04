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

    private Digraph digraph; 
    private int vertices; 
    private Set<String[]> synset; 
    private Set<int[]> hyponym;
    
    
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
                String[] hyponymSarray = hyponymints.split(",");
                int[] hyponymArray = new int[hyponymSarray.length];
                for (int i = 0; i < hyponymSarray.length; i = i + 1) {
                    try { // Create int from string[].
                        int element = Integer.parseInt(hyponymSarray[i]);
                        // Add to our int[].
                        hyponymArray[i] = element;
                    } catch (NumberFormatException nf) {
                        System.out.println("Problem in hyponym file - an element is not an int.");
                    }       
                }
                hyponym.add(hyponymArray);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("The hyponym file is not valid.");
        }
        // Hyponym set made; Now iterate through and start mapping.
        Iterator<int[]> iter = hyponym.iterator(); 
        while (iter.hasNext()) {
            int[] hypRelations = iter.next();
            for (int i = 1; i < hypRelations.length; i = i + 1) {
                // First is synset; rest are offspring.
                digraph.addEdge(hypRelations[0], hypRelations[i]);
            }
        }
    }

    /* Returns true if noun is a word in some synset. */
    public boolean isNoun(String noun) {
        Iterator<String[]> synIter = this.synset.iterator();
        while (synIter.hasNext()) {
            String[] synArray = synIter.next();
            String[] actualWords = synArray[1].split(" ");
            for (int i = 0; i < actualWords.length; i = i + 1) {
                if (actualWords[i].equals(noun)) {
                    return true; 
                }
            }
        }
        return false; 
    }

    /* Returns set of all nouns. */
    public Set<String> nouns() {
        Set<String> allNouns; 
        allNouns = new HashSet<String>(); 
        Iterator<String[]> synIter = this.synset.iterator();
        while (synIter.hasNext()) {
            String[] synArray = synIter.next();
            String[] words = synArray[1].split(" ");
            for (int i = 0; i < words.length; i = i + 1) {
                allNouns.add(words[i]);
            }
        }
        return allNouns; 
    }


    /* Returns the set of all hyponyms of word including word itself. */
    public Set<String> hyponyms(String word) {
        /* First figure out all synset IDs of the word. 
         * Then take those IDs, and find the rest of the #s in the hyponym file. 
         * MUST BE THE FIRST # IN HYPONYM LINE.
         * Also include own synonyms.
         * Put all the words in the matching hyponyms into the set. */

        // Haave to get children of children as well.

        Set<String> allHyponyms = new HashSet<String>();
        Iterator<String[]> synIter = this.synset.iterator();
        // Figure out where the word occurs.
        while (synIter.hasNext()) {
            String[] synArray = synIter.next();
            String[] words = synArray[1].split(" "); 
            for (int i = 0; i < words.length; i = i + 1) {
                if (words[i].equals(word)) {
                    // Get the synset ID.
                    int synID = Integer.parseInt(synArray[0]); 
                    // Get self + synonyms
                    Set<String> words2 = this.words(synID);
                    for (String addWord : words2) {
                        allHyponyms.add(addWord);
                    }

                    // Then find ALL YO DESCENDENTS, get their words, and store. 
                    Set<Integer> allChildrenIDs = this.allSynIDs(synID);
                    for (int synIDs : allChildrenIDs) {
                        Set<String> addTheseWords = this.words(synIDs);
                        for (String addWords : addTheseWords) {
                            allHyponyms.add(addWords);
                        }
                    }
                }
            }
        } 
        return allHyponyms; 
    }


    /* Returns ALL the synset IDs (descendents) need to take in, given your original synset ID. */
    private Set<Integer> allSynIDs(int synID) {
        Set<Integer> allchildren = new HashSet<Integer>(); 
        Iterator<int[]> hypIter = this.hyponym.iterator();
        while (hypIter.hasNext()) {
            int[] hypArray = hypIter.next();
            if (hypArray[0] == synID) {
                int[] hypIDs = Arrays.copyOfRange(hypArray, 0, hypArray.length);
                for (int j = 0; j < hypIDs.length; j = j + 1) {
                    allchildren.add(hypIDs[j]);
                }
                // Now have to find the subchildren.
                for (int k = 1; k < hypIDs.length; k = k + 1) {
                    // Start at 1 because you already found the children of yourself.
                    Set<Integer> subchildren = this.allSynIDs(hypIDs[k]);
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
    private Set<String> words(int synID) {
        Set<String> words = new HashSet<String>();
        Iterator<String[]> synIter2 = this.synset.iterator();
        while (synIter2.hasNext()) {
            String[] synArray2 = synIter2.next();
            String[] words2 = synArray2[1].split(" ");
            int check_synID = Integer.parseInt(synArray2[0]);
            if (check_synID == synID) {
                // Found right synset, take in words.
                for (int k = 0; k < words2.length; k = k + 1) {
                    words.add(words2[k]);
                }
            }
        }
        return words;
    }


/* Major thanks to dude at office hours.
    * Whose name I never figured out.
    * His Macbook said Soham but there's no Soham on the CS61B staff page.
    * He Spent 2 hours of his life googling weird java errors and explaining how to fix them.
    * Thanks bruh. */
} 
