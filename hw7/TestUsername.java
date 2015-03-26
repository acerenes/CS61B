import org.junit.Test;
import static org.junit.Assert.*;

public class TestUsername {

    @Test
    public void testRandomUsername() {
        System.out.println("Printing a random username below: ");
        System.out.println((new Username()).usernameString());
    }

    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestUsername.class);
    }
}
