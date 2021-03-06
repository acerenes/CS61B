import java.util.HashMap; // Import Java's HashMap so we can use it

public class FibonacciMemo {

    /**
     * The classic recursive implementation with no memoization. Don't care
     * about graceful error catching, we're only interested in performance.
     * 
     * @param n
     * @return The nth fibonacci number
     */
    public static int fibNoMemo(int n) {
        if (n <= 1) {
            return n;
        }
        return fibNoMemo(n - 2) + fibNoMemo(n - 1);
    }

    /**
     * Your optimized recursive implementation with memoization. 
     * You may assume that n is non-negative.
     * 
     * @param n
     * @return The nth fibonacci number
     */

    private static HashMap<Integer, Integer> fibNums = new HashMap<Integer, Integer>();
    /* They said to make it static, so I did. 
    I'm assuming because fib #s are the same everywhere, so want 1 map for all runnings of fib? */

    public static int fibMemo(int n) {
        if (n <= 1) { // Base Case
            fibNums.put(n, n);
            return n;
        } else if (fibNums.containsKey((Integer) n)) {
            return fibNums.get((Integer) n);
        }
        int returnFib = fibMemo(n - 1) + fibMemo(n - 2);
        fibNums.put((Integer) n, returnFib);
        return returnFib;
    }

    /**
     * Answer the following question as a returned String in this method:
     * Why does even a correctly implemented fibMemo not return 2,971,215,073
     * as the 47th Fibonacci number?
     */
    public static String why47() {
        String answer = "There is integer overflow - ints can only go up to around 2 billion. Anything larger, it'll wrap back around to negatives. ";
        /*answer += ", " + answer + " and tapioca";*/
        return answer;
    }

    public static void main(String[] args) {
        // Optional testing here        
        String m = "Fibonacci's real name was Leonardo Pisano Bigollo.";
        m += "\n" + "He was the son of a wealthy merchant.\n";
        System.out.println(m);
        System.out.println("0: " + FibonacciMemo.fibMemo(0));
        System.out.println("1: " + FibonacciMemo.fibNoMemo(1));
        System.out.println("2: " + FibonacciMemo.fibNoMemo(2));
        System.out.println("3: " + FibonacciMemo.fibNoMemo(3));
        System.out.println("4: " + FibonacciMemo.fibNoMemo(4));

        // 46th Fibonacci = 1,836,311,903
        // 47th Fibonacci = 2,971,215,073
    }
}
