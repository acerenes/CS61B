import org.junit.Test;
import static org.junit.Assert.*;

public class TestCard {

    @Test
    public void testEquals() {
        Card card1 = new Card(1, 1);
        Card card2 = new Card(1, 1);
        assertTrue(card1.equals(card2));

        Card card3 = new Card(2, 4);
        Card card4 = new Card(3, 13);
        assertFalse(card3.equals(card4));
    }


    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestCard.class);
    }

}
