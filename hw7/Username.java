import java.util.Random;

public class Username {

    // Potentially useless note: (int) '0' == 48, (int) 'a' == 97

    // Instance Variables (remember, they should be private!)
    private String user;
    private Random rand = new Random();

    /* Generates a random, valid username. */
    public Username() {
        user = chooseNumOrChar();
        user = user + chooseNumOrChar();
        // If true, 3 characters.
        if (generateRandomBoolean()) {
            user = user + chooseNumOrChar();
        }
    }

    String usernameString() {
        return this.user;
    }

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
        if (reqName == null) {
            throw new NullPointerException("Requested username is null!");
        } else if ((reqName.length() < 2) || (reqName.length() > 3)) {
            throw new IllegalArgumentException("Requested username incorrect length.");
        } else if (!reqName.matches("\\p{Alnum}*")) {
            // Apparently just 1 slash is escape. 
            throw new IllegalArgumentException("Username contains not alphanumerical characters.");
        } else {
            this.user = reqName;
        }
    }

    @Override
    public boolean equals(Object o) {
        if ((this != null) && (o != null) && (o instanceof Username)) {
            Username other = (Username) o;
            return (this.user).equals(other.user);
        }
        return false;
    }

    @Override
    public int hashCode() { 
        int hashVal = 0; // Initialize. 
        for (int i = 0; i < this.user.length(); i = i + 1) {
            char c = (this.user).charAt(i);
            hashVal = hashVal << 5; // 5 b/c why not. Should be far enough, and int has enuf bits.
            hashVal = hashVal + ((int) c);
        }
        return hashVal;
    }

    public static void main(String[] args) {
        // You can put some simple testing here.
    }
}