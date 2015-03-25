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


    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestCard.class);
    }
}