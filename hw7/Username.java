import java.util.Random;

public class Username {

    // Potentially useless note: (int) '0' == 48, (int) 'a' == 97

    // Instance Variables (remember, they should be private!)
    private String user;
    private Random rand = new Random();

    public Username() {
        user = chooseNumOrChar();
        user = user + chooseNumOrChar();
        // If true, 3 characters.
        if (generateRandomBoolean()) {
            user = user + chooseNumOrChar();
        }
    }

    /*String usernameString() {
        // For testing purposes - MUST COMMENT OUT LATER
        return this.user;
    }*/

    private String chooseNumOrChar() {
        // If true, char.
        // Then if true again, upper char. 
        if (generateRandomBoolean()) {
            if (generateRandomBoolean()) {
                return ((Character) generateUpperChar()).toString();
            }
            return ((Character) generateLowerChar()).toString();
        }
        return ((Integer) generateRandomOneTen()).toString();
    }

    private boolean generateRandomBoolean() {
        return rand.nextBoolean();
    }

    private int generateRandomOneTen() {
        int randomNum2 = rand.nextInt((9 - 0) + 1) + 0;
        return randomNum2;
    }

    private int generateRandomInt() {
        /* Thanks to StackOverflow. */
        int randomNum = rand.nextInt((25 - 0) + 1) + 0;
        return randomNum;
    }

    private char generateLowerChar() {
        return (char) (97 + generateRandomInt());
    }

    private char generateUpperChar() {
        return (char) (65 + generateRandomInt());
    }

    public Username(String reqName) {
        // YOUR CODE HERE
    }

    @Override
    public boolean equals(Object o) {
        // YOUR CODE HERE
        return false;
    }

    @Override
    public int hashCode() { 
        // YOUR CODE HERE
        return 0;
    }

    public static void main(String[] args) {
        // You can put some simple testing here.
    }
}