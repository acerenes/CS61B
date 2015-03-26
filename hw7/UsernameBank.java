import java.util.Map;
import java.util.HashMap;

public class UsernameBank {

    // Instance variables (remember, they should be private!)
    private Map<String, String> userToMail;
    private Map<String, String> mailToUser;
    private Map<String, String> badEmails;
    private Map<String, String> badUsers;

    /* Initializes all necessary inner data. */
    public UsernameBank() {
        userToMail = new HashMap<String, String>();
        mailToUser = new HashMap<String, String>(); 
        badEmails = new HashMap<String, String>();
        badUsers = new HashMap<String, String>();
    }

    Map<String, String> getUsersAndMails() {
        // FOR TESTING - MAKE SURE TO COMMENT OUT LATER
        return this.userToMail;
    }

    public void generateUsername(String username, String email) {
        if ((username == null) || (email == null)) {
            throw new NullPointerException("Username / email cannot be null.");
        } else if (!isValidUsername(username)) {
            new Username(username); // Should throw the exception.
        } else if (userToMail.containsKey(username)) {
            throw new IllegalArgumentException("This username is already taken.");
        } else {
            userToMail.put(username, email);
            mailToUser.put(email, username);
        }
    }

    private boolean isValidUsername(String username) {
        try {
            Username testUser = new Username(username);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public String getEmail(String username) {
        // YOUR CODE HERE
        return null;
    }

    public String getUsername(String userEmail)  {
        // YOUR CODE HERE
        return null;
    }

    public Map<String, Integer> getBadEmails() {
        // YOUR CODE HERE
        return null;
    }

    public Map<String, Integer> getBadUsernames() {
        // YOUR CODE HERE
        return null;
    }

    public String suggestUsername() {
        // YOUR CODE HERE
        return null;
    }

    // The answer is somewhere in between 3 and 1000.
    public static final int followUp() {
        // YOUR CODE HERE
        return 0;
    }

    // Optional, suggested method. Use or delete as you prefer.
    private void recordBadUsername(String username) {
        // YOUR CODE HERE
    }

    // Optional, suggested method. Use or delete as you prefer.
    private void recordBadEmail(String email) {
        // YOUR CODE HERE
    }
}