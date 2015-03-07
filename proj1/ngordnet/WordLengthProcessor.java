package ngordnet;
import java.util.Collection;

public class WordLengthProcessor implements YearlyRecordProcessor {

    public double process(YearlyRecord yearlyRecord) {
        /* 
        YearlyRecord yr = new YearlyRecord();
        yr.put("sheep", 100);
        yr.put("dog", 300);
        WordLengthProcessor wlp = new WordLengthProcessor();

        Since sheep appears 100 times and has length 5.
        And dog appears 300 tiems and has length 3.
        The average length in this year was 3.5. 
        */

        /* So it's sum(count * length) / (total counts). */
        Collection<String> allWords = yearlyRecord.words();
        // Going to use long b/c seems to hold largest possible #s.
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