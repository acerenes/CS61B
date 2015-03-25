import org.junit.Test;
import static org.junit.Assert.*;

public class TestBin15 {

    @Test
    public void testEquals() {
        Bin15 bin1 = new Bin15("111111111111111");
        Bin15 bin2 = new Bin15("111111111111111");
        assertTrue(bin1.equals(bin2));


        Bin15 bin3 = new Bin15("011111111111111");
        assertFalse(bin1.equals(bin3));
    }

    @Test
    public void testHashCode() {
        Bin15 bin1 = new Bin15("111111111111111");
        Bin15 bin2 = new Bin15("111111111111111");
        int hash1 = bin1.hashCode();
        int hash2 = bin2.hashCode();
        assertTrue(hash1 == hash2);

        Bin15 bin3 = new Bin15("011111111111111");
        int hash3 = bin3.hashCode();
        assertFalse(hash1 == hash3);
    }


    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestCard.class);
    }
}