/* Radix.java */

package radix;
import java.util.HashMap;

/**
 * Sorts is a class that contains an implementation of radix sort.
 * @author
 */
public class Sorts {


    /**
     *  Sorts an array of int keys according to the values of <b>one</b>
     *  of the base-16 digits of each key. Returns a <b>NEW</b> array and
     *  does not modify the input array.
     *  
     *  @param key is an array of ints.  Assume no key is negative.
     *  @param whichDigit is a number in 0...7 specifying which base-16 digit
     *    is the sort key. 0 indicates the least significant digit which
     *    7 indicates the most significant digit
     *  @return an array of type int, having the same length as "keys"
     *    and containing the same keys sorted according to the chosen digit.
     **/
    public static int[] countingSort(int[] keys, int whichDigit) {
        
        // Sort only on the whichDigith digit of each key. 
        // But we're doing R = 16, and for binary --> each thing of the key = 4-bit. 
        // And since ints are 32 bit --> each int has 8 things of its key. 
        // So the whichDigit goes from 0 to 7. 


        // Calculating the R-value is annoying, so I'm just gonna store it. 
        HashMap<Integer, Integer> rValues = new HashMap<Integer, Integer>();

        // Array of counts for each R value. Index = R value, value is frequency. 
        int[] counts = new int[15]; 
            // So iterate through the array. Calculate its R value. Update counts array. 
        for (int entry : keys) {
            // WAIT HOW DO I GET JUST THAT STACK OF 4-BITS THO????
            // Scoot and then and? 
            int shiftAmount = 32 - 4 * (whichDigit + 1);
            // I want to put 0s at the top, so logical right. >>>. 
            // WAIT BUT I JUST WANT THE VALUE OF THOSE 4 BITS. 
            // I guess I can and it with some weird 000011110000 combo. Everything 0 except for the 4-bits being looked at. 
            // 15 is 1111. 
            int andWithMe = 15 << shiftAmount;
            int shifted = entry >>> shiftAmount;
            int rValue = shifted & andWithMe;
            rValues.put(entry, rValue);
            counts[rValue] = counts[rValue] + 1;
        }


        // Array of starting indexes for each R value thing.
        int[] startingIndices = new int[15];
        for (int i = 1; i <= 15; i = i + 1) {
            startingIndices[i] = startingIndices[i - 1] + counts[i - 1];
        }


        // Create a final sorted array, and insert into using the starting indices thing. 
        int[] finalSorted = new int[keys.length];
            // Iterate through original array of course. 
        for (int entry2 : keys) {
            int rValue2 = rValues.get(entry2);
            int finalIndex = startingIndices[rValue2];
            finalSorted[finalIndex] = entry2;
            startingIndices[rValue2] = startingIndices[rValue2] + 1;
        }

        return finalSorted;

    }

    /**
     *  radixSort() sorts an array of int keys (using all 32 bits
     *  of each key to determine the ordering). Returns a <b>NEW</b> array
     *  and does not modify the input array
     *  @param key is an array of ints.  Assume no key is negative.
     *  @return an array of type int, having the same length as "keys"
     *    and containing the same keys in sorted order.
     **/
    public static int[] radixSort(int[] keys) {
        // MSD Sort. 

        int[] finalSort = countingSort(keys, 0);

        for (int i = 1; i < 8; i = i + 1) {
            finalSort = countingSort(finalSort, i);
        }

        return finalSort;
    }

}
