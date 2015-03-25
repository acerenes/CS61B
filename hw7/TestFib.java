import org.junit.Test;
import static org.junit.Assert.*;

public class TestFib {

    @Test
    public void testFib() {
        assertEquals(0, FibonacciMemo.fibMemo(0));
        assertEquals(1, FibonacciMemo.fibMemo(1));
        assertEquals(1836311903, FibonacciMemo.fibMemo(46));
    }


    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestFib.class);
    }
}
