import static org.junit.Assert.*;
import org.junit.Test;

public class TestPieceClass {

	/* Test Constructor */
	@Test
	public void testConstructor() { 
		/* If want to testConstructor, have to change instance variables to public */
		Board b= new Board(true); // Empty board. I think this is like calling the constructor, sorta?
		Piece pawn= new Piece(true, b, 0, 0, "pawn");
		assertEquals(true, pawn.element);
		assertEquals(b, pawn.board);
		assertEquals(0, pawn.xpos);
		assertEquals(0, pawn.ypos);
		assertEquals("pawn", pawn.piecetype);
	}

	/* Test isFire */
	@Test
	public void testisFire() {
		Board b= new Board(true);
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

	@Test
	public void testisKing() {
		Board b= new Board(true);
		Piece king= new Piece(false, b, 5, 5, "king"); 
		assertEquals(true, king.isKing()); 

		Piece notking= new Piece(true, b, 7, 0, "bomb"); 
		assertEquals(false, notking.isKing());
	}


	public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestPieceClass.class);
    }
}