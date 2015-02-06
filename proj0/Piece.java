public class Piece {

	/* Declaring instance variables */
	private boolean element;
	private Board board; 
	private int xpos;
	private int ypos;
	private String class; 

	/* Constructor for a Piece */
	public Piece(boolean isFire, Board b, int x, int y, String type) {
		element= isFire; 
		board= b;
		xpos= x; 
		ypos= y;
		class= type; 
	}
}