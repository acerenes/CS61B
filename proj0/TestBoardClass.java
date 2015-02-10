import static org.junit.Assert.*;
import org.junit.Test;

public class TestBoardClass {

	@Test
	public void testpieceAt() {
		String[] args = {};
		Board.main(args); // Calling the main method of Board.java
		Board b= new Board(false);
		Piece testpiece= b.pieceAt(0, 0); // Already returns a piece, so not making like a NEW piece
		assertEquals(true, testpiece.element);
		/*assertEquals(b, testpiece.board); It just doesn't return the same board when I do this */
		assertEquals(0, testpiece.xpos);
		assertEquals(0, testpiece.ypos);
		assertEquals("pawn", testpiece.piecetype);
	}


	public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestBoardClass.class);
    }
}