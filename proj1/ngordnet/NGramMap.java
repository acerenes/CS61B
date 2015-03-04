public class NGramMap {


    /* Using sets again. 
        * One set for words, one for counts. */
    Set<String[]> words;
    Set<int[]> counts;

    /* Constructs an NGramMap from wordsFilename & countsFilename. */
    public NGramMap(String wordsFilename, String countsFilename) {
        // Similar to WordNet.
        words = new HashSet<String[]>();
        File wordsfile = new File(wordsFilename);
        try {
            Scanner wordsscanned = new Scanner(wordsfile);
            while (wordsscanned.hasNextLine()) {
                String wordsstring = wordsscanned.nextLine();
                String[] wordsarray = wordsstring.split("\t");
                words.add(wordsarray);
            }
        }


    }
}
