import org.junit.Test;
import static org.junit.Assert.*;

public class TestAsymptotics {

    @Test
    public void testFunction4() {
        long prime = 997;
        long notprime = 10000;
        assertTrue(Asymptotics.function4(prime));
        assertFalse(Asymptotics.function4(notprime));
    }



    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestAsymptotics.class);
    }
}