package ngordnet;
import java.util.Collection;

public class WordLengthProcessor implements YearlyRecordProcessor {

    public double process(YearlyRecord yearlyRecord) {

        /* sum(count * length) / (total counts). */
        Collection<String> allWords = yearlyRecord.words();
        // Using long b/c seems to hold largest possible #s.
        long totalSum = 0; // Initializing.
        long totalCounts = 0;
        /*long overflows = 0;*/
        for (String word : allWords) {
            int length = word.length();
            int wordCount = yearlyRecord.count(word);
            totalSum = totalSum + (length * wordCount);
            /*// In case of overflow.
            if (totalSum < 0) {
                overflows = overflows + 1;
                totalSum = totalSum + 922337203685477580 + 1;
                // + 1 is for the step to get to the negativeness. 
            }*/
            totalCounts = totalCounts + wordCount;
        }

        if (totalCounts == 0) {
            // Like no words in the thing I guess.
            // Doing this to avoid if divide by 0.
            return 0;
        }
        return ((double) totalSum) / ((double) totalCounts);
        // Can't do division with longs b/c it's integer division - returns int. 

        /* long avgLength = (totalSum + (overflows * 2147483647)) / totalCounts; */
        /*long avgLength = (totalSum / totalCounts) + ((overflows / totalCounts) * 2147483647);*/
        // Commented it out b/c compiler doesn't like adding huge #s.
    }
}
