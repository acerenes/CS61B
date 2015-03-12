import java.util.ArrayList;
import java.lang.Math;

/** Class for testing the running times of various functions on
 *  different inputs. Usage:
 *      java Asymptotics x n
 *  (where x and n are positive integers and x is less than 5)
 *  prints the number of seconds that functionx(n) takes to run. */
public class Asymptotics {

    public static void main(String[] args) {
        if (args.length > 1) {
            try {
                int func = Integer.parseInt(args[0]);
                long arg = Long.parseLong(args[1]);
                if (arg < 0) {
                    usage();
                    return;
                }

                Stopwatch timer = new Stopwatch();
                switch (func) {
                case 1: function1(arg);
                    break;
                case 2: function2(arg);
                    break;
                case 3: function3(arg);
                    break;
                case 4: function4(arg);
                    break;
                case 5: function5(arg);
                    break;
                default: usage();
                        return;
                }
                System.out.println(timer.elapsedTime() + " seconds elapsed");
            } catch (NumberFormatException e) {
                usage();
            }
        } else {
            usage();
        }
    }

    public static void function1(long n) {
        ArrayList<Integer> a = new ArrayList<Integer>();
        for (int i = 0; i < n; i++) { // Worstcase go through n times.
            for (int j = 0; j < i; j++) { // 0 + 1 + 2 + .. + n
                a.add(0);
                a.clear();
            }
        }
    }

    public static void function2(long n) {
        ArrayList<Integer> a = new ArrayList<Integer>();
        int j = 0;
        for (int i = 0; i < n; i++){
            for (; j < i; j++) {
                a.add(0); // Amortized constant time.
                a.clear(); // Linear time?
            }
        }
    }


    public static void function3(long n) {
        ArrayList<Integer> a = new ArrayList<Integer>();
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n*n; j++) {
                a.add(0);
                a.clear();
            }
        }
    }

    /** Fill in the body of this function so that its running time is
     *  n^(1/2) and so that it returns true if and only if n is prime. */
    public static boolean function4(long n) {
        // Check square root?
        // Factors come in pairs.
        double newN = (double) n;
        double sqrt = Math.sqrt(newN);
        // 1 doesn't count as a factor.
        for (double i = 2; i < sqrt; i = i + 1) {
            if (newN % i == 0) {
                return false;
            }
        }
        if (sqrt % 1 == 0) {
            // Checks if n is perfect square.
            return false;
        }
        // ^ Constant operation, so should be fine.
	   return true; 
    }

    /** Fill in the body of this function so that its best case running
     *  time is log(n) and its worst case running time is n. */
    public static void function5(long n) {
        long counter = 1000000;
        // Do it 1000000000000 times, so runtime should be scaled by 1000000000000.
        long newN = Math.abs(n); 
        long changingN = newN;
        while (counter > 0) {
            changingN = newN;
            if (changingN % 2 == 0) {
                // Best Case
                while (changingN != 1) {
                    changingN = changingN / 2;
                }
            }
            else {
                while (changingN != 0) {
                    changingN = changingN - 1;
                }
            }
            counter = counter - 1;
        }
    }

    public static void usage() {
        System.out.println("To run function x with input n (where x and n " +
                           "are positive integers and x is less than 5) use" +
                           " the command 'java Asymptotics x n'");
    }

}
