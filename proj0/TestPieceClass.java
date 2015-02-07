import static org.junit.Assert.*;
import org.junit.Test;

public class TestPieceClass {

	/* Test Constructor */
	@Test
	public void testConstructor_and_isFire() { // Can't really test Constructor by itself
		Board b= new Board(true); // Empty board. I think this is like calling the constructor, sorta?
		Piece pawn= new Piece(true, b, 0, 0, "pawn");
		assertEquals(true, pawn.isFire());

		Piece bomb= new Piece(false, b, 5, 5, "bomb");
		assertEquals(false, bomb.isFire());
	}

	/* Test side */
	@Test
	public void testside() {
		Board b= new Board(true);
		Piece bomb= new Piece(false, b, 5, 5, "bomb");
		assertEquals(1, bomb.side());

		Piece pawn= new Piece(true, b, 0, 0, "pawn");
		assertEquals(0, pawn.side());
	}


	public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestPieceClass.class);
    }
}