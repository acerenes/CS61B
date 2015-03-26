import org.junit.Test;
import static org.junit.Assert.*;

public class TestUsernameBank {

    @Test
    public void testGoodGenerateUsername() {
        UsernameBank bank = new UsernameBank();
        bank.generateUsername("aat", "at@berkeley.edu");
        assertEquals("at@berkeley.edu", (bank.getUsersAndMails()).get("aat"));
    }

    @Test (expected = NullPointerException.class)
    public void testNullGenerateUsername() {
        UsernameBank bank2 = new UsernameBank();
        bank2.generateUsername(null, "at@berkeley.edu");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBadGenerateUsername() {
        UsernameBank bank3 = new UsernameBank();
        bank3.generateUsername("aaat", "at@berkeley.edu");
    }



    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestUsernameBank.class);
    }
}