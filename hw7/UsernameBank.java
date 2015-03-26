import java.util.Map;
import java.util.HashMap;

public class UsernameBank {

    // Instance variables (remember, they should be private!)
    private Map<String, String> userToMail;
    private Map<String, String> mailToUser;
    private Map<String, String> originalUserCase; // Values are original case.
    private Map<String, Integer> badEmails;
    private Map<String, Integer> badUsers;
    /*private Map<String, String> badUsersOriginalCase;*/

    /* Initializes all necessary inner data. */
    public UsernameBank() {
        userToMail = new HashMap<String, String>();
        mailToUser = new HashMap<String, String>();
        originalUserCase = new HashMap<String, String>();
        badEmails = new HashMap<String, Integer>();
        badUsers = new HashMap<String, Integer>();
        /*badUsersOriginalCase = new HashMap<String, String>();*/
        // All hold user in lower case except original usercase.
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
        } else if (userToMail.containsKey(username.toLowerCase())) {
            throw new IllegalArgumentException("This username is already taken.");
        } else {
            originalUserCase.put(username.toLowerCase(), username);
            userToMail.put(username.toLowerCase(), email);
            mailToUser.put(email, username.toLowerCase());
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
        if (username == null) {
            throw new NullPointerException("Username cannot be null.");
        } else if ((!isValidUsername(username)) || (!userToMail.containsKey(username.toLowerCase())))  {
            if (!badUsers.containsKey(username.toLowerCase())) {
                // First time in.
                badUsers.put(username.toLowerCase(), 1);
                /*badUsersOriginalCase.put(username.toLowerCase(), username);*/
            } else {
                int prevCount = badUsers.get(username.toLowerCase());
                badUsers.put(username.toLowerCase(), prevCount + 1);
            }
            return null;
        } else {
            return userToMail.get(username.toLowerCase());
        }
    }

    public String getUsername(String userEmail)  {
        if (userEmail == null) {
            throw new NullPointerException("Email cannot be null.");
        } else if (!mailToUser.containsKey(userEmail)) {
            if (!badEmails.containsKey(userEmail)) {
                // First time in. 
                badEmails.put(userEmail, 1);
            } else {
                int prevCount = badEmails.get(userEmail);
                badEmails.put(userEmail, prevCount + 1);
            }
            return null;
        } else {
            String allLower = mailToUser.get(userEmail);
            return originalUserCase.get(allLower); 
        }
    }

    public Map<String, Integer> getBadEmails() {
        return badEmails;
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