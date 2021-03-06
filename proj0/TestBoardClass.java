import static org.junit.Assert.*;
import org.junit.Test;

public class TestBoardClass {

	@Test
	public void testpieceAt() {
		Board b= new Board(false);
		Piece testpiece= b.pieceAt(0, 0); // Already returns a piece, so not making like a NEW piece
		assertEquals(true, testpiece.element);
		/*assertEquals(b, testpiece.board); It just doesn't return the same board when I do this */
		assertEquals(0, testpiece.xpos);
		assertEquals(0, testpiece.ypos);
		assertEquals("pawn", testpiece.piecetype);
	}

	@Test
	public void testplace() {
		Board b= new Board(false);
		/* Test the null piece */
		Piece nullpiece= b.pieceAt(0, 1); // null
		b.place(nullpiece, 2, 2); // At this place= fire bomb
		Piece testpiece= b.pieceAt(2, 2);
		// Want to make sure it's still a fire bomb
		assertEquals(true, testpiece.element);
		assertEquals("bomb", testpiece.piecetype); 

		/* Test an actual moven piece - move the fire shield */
		Piece fireshield= b.pieceAt(5, 1); 
		b.place(fireshield, 7, 5); 
		Piece testpiece2= b.pieceAt(7, 5); 
		assertEquals(true, testpiece2.element);
		assertEquals("shield", testpiece2.piecetype);
	}

	/*@Test
	public void testvalidMove() {
		Board b= new Board(false); 
		//Test a fire, not king piece, single move 
		assertEquals(true, b.validMove(0, 2, 1, 3));
		// Test a water, multi-capture
		assertEquals(false, b.validMove(1, 5, 5, 1)); 
	}*/
	// I DON'T KNOW HOW TO MAKE THIS TEST WORK B/C PRIVATE ACCESS

	@Test
	public void testremove() {
		Board b= new Board(false); 
		assertEquals(null, b.remove(8, 8)); 
		assertEquals(null, b.remove(1, 0)); 
		assertEquals(b.pieceAt(2, 2), b.remove(2,2));

	}

	@Test 
	public void testwinner() {
		Board b= new Board(true); 
		assertEquals("No one", b.winner()); 
		Board b= new Board(false);
		assertEquals(null, b.winner()); 
		b.remove(7, 5); 
		assertEquals("Fire", b.winner()); 
		b.remove(7, 1);
		b.remove(6, 2);
		assertEquals()
	}




	public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestBoardClass.class);
    }
}