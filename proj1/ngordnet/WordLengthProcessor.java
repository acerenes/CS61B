package ngordnet;
import java.util.Collection;

public class WordLengthProcessor implements YearlyRecordProcessor {

    public double process(YearlyRecord yearlyRecord) {

        /* sum(count * length) / (total counts). 
            * So sum((count * length) / (total counts)) should work. 
            * Doing this b/c want to avoid overflow problems. */
        Collection<String> allWords = yearlyRecord.words();
        double avgLength = 0; // Initializing
        double totalCounts = 0;
        // Going to find totalCounts first, so can use it for distributive magic.
        for (String word : allWords) {
            int wordCount = yearlyRecord.count(word);
            totalCounts = totalCounts + wordCount;
        }

        if (totalCounts == 0) {
            // No words in the thing (avoid divide by 0).
            return 0;
        }

        for (String word2 : allWords) {
            int length = word2.length();
            int wordCount = yearlyRecord.count(word2);
            avgLength = avgLength + ((double) (wordCount * length) / totalCounts);
        }

        return avgLength;
    }
}
