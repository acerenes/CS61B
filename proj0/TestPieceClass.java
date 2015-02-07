import static org.junit.Assert.*;
import org.junit.Test;

public class TestPieceClass {

	/* Test Constructor */
	@Test
	public void testConstructor_and_isFire() { 
		Board b= new Board(true); // Empty board???? Non-static method so can't do class.thing
		Piece pawn= new Piece(true, b, 0, 0, "pawn");
		assertEquals(true, pawn.isFire());

		Piece bomb= new Piece(false, b, 5, 5, "bomb");
		assertEquals(false, bomb.isFire());
	}


	public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestPieceClass.class);
    }
}