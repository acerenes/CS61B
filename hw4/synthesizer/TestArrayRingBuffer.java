package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void testEnqueue() {
        ArrayRingBuffer arb = new ArrayRingBuffer(10);
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);
        assertEquals(1, arb.dequeue(), 0); // Now arb= [blank, 2, 3]
        // Java's telling me to do some weird assertEquals(expected, actual, delta) to compare floating-point numbers
        arb.enqueue(4);
        arb.enqueue(5); 
        assertEquals(2, arb.peek(), 0); 

    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 