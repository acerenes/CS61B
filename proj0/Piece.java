public class Piece {

	/* Declaring instance variables */
	/*private boolean element;
	private Board board; 
	private int xpos;
	private int ypos;
	private String piecetype;
	private Piece oldpiece; 
	private Piece newpiece; */

	public boolean element;
	public Board board; 
	public int xpos;
	public int ypos;
	public String piecetype;
	public Piece oldpiece;  
	public Piece newpiece; // Declare it up here so has captured can use it as well  
	// FOR TESTING

	/* Constructor for a Piece */
	public Piece(boolean isFire, Board b, int x, int y, String type) {
		element= isFire; 
		board= b;
		xpos= x; 
		ypos= y;
		piecetype= type; 
	}

	public boolean isFire() {
		if (side() == 0) {
			return true; 
		}
		else {
			return false;
		}
	}

	public int side() {
		if (element) {
			return 0;
		}
		else {
			return 1;
		}
	}

	public boolean isKing() {
		if (piecetype== "king") {
			return true; 
		}
		return false;
	}

	public boolean isBomb() {
		if (piecetype== "bomb") {
			return true;
		}
		return false;
	}

	public boolean isShield() {
		if (piecetype== "shield") {
			return true; 
		}
		return false; 
	}

	public void move(int x, int y) {
		newpiece= new Piece(element, board, x, y, piecetype); // Compiler didn't like it when I just used the original input variables- it "couldn't find symbol". Cause I guess they don't really exist anymore. 
		// also no Piece newpiece= new Piece(stuff) b/c then I would be like declaring a whole new thing or smth, instead of just assigning the variable I already declared at the top. The variable I declared at the top, that's being used by hasCaptured, would just stay empty then. 
		this= newpiece; 
		
	}

	public boolean hasCaptured() { 
		// Check positions! 
		// Assumes it was a valid move
		if ((Math.abs(newpiece.xpos - this.xpos)>=2) && (Math.abs(newpiece.ypos - this.ypos)>=2)) {
			return true;
		}
		else {
			return false;
		}

	}
	public void doneCapturing() {
		this.hasCaptured()= false; 
		// YOU AREN'T WORKING ALIUGHALIGHALIHGLAIUHELIHU
	}

}

/* Thanks to Sreesha Venkat and Karin Goh and Anna-Marie for help. */ 