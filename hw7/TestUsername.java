import org.junit.Test;
import static org.junit.Assert.*;

public class TestUsername {

    @Test
    public void testRandomUsername() {
        System.out.println("Printing a random username below: ");
        System.out.println((new Username()).usernameString());
    }

    @Test (expected = NullPointerException.class)
    public void testUsernameNullException() {
        Username nullUser = new Username(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBadLength() {
        Username badLengthUser = new Username("AA44");
    }

    @Test (expected = IllegalArgumentException.class)
    public void testBadChars() {
        Username badCharsUser = new Username("A$3");
    }

    @Test
    public void testGoodUsername() {
        System.out.println("Make sure below printed = Aa4");
        System.out.println((new Username("Aa4")).usernameString());
    }



    @Test
    public void testEquals() {
        Username user1 = new Username("abc");
        Username user2 = new Username("abc");
        assertTrue(user1.equals(user2));

        Username user3 = new Username("abd");
        assertFalse(user1.equals(user3));
    }

    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestUsername.class);
    }
}
