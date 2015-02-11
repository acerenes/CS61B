public class Piece {

	/* Declaring instance variables */
	/*private boolean element;
	private Board board; 
	private int xpos;
	private int ypos;
	private String piecetype;
	private Piece oldpiece; 
	private Piece newpiece;
	private static Piece[] captured; */

	public boolean element;
	public Board board; 
	public int xpos;
	public int ypos;
	public String piecetype;
	public Piece oldpiece;  
	public Piece newpiece; 
	public Piece captured= null;// Declare it up here so has captured can use it as well  
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
		oldpiece= new Piece(this.element, this.board, this.xpos, this.ypos, this.piecetype); 
		newpiece= new Piece(this.element, this.board, x, y, this.piecetype); 	
		this.xpos= x;
		this.ypos= y; 
		/* I think if you jumped over something, take that piece into a variable, and then remove it */
		int i= (x- oldpiece.xpos)/ Math.abs(x- oldpiece.xpos); 
		int j= (y- oldpiece.ypos)/ Math.abs(y- oldpiece.ypos); 
		while(oldpiece.xpos!=x) {
			Piece current_piece= board.pieceAt(oldpiece.xpos+i, oldpiece.ypos+j); 
			if (current_piece==null) {
				return; 
			}
			else if (current_piece.element != oldpiece.element) {
				captured= board.remove(oldpiece.xpos+i, oldpiece.ypos+j);
			}
			oldpiece.xpos= oldpiece.xpos+2*i; 
			oldpiece.ypos= oldpiece.ypos+2*j; 
		}
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
		/*this.hasCaptured()= false; */
		// YOU AREN'T WORKING ALIUGHALIGHALIHGLAIUHELIHU
	}

}

/* Thanks to Sreesha Venkat and Karin Goh and Anna-Marie for help. */ 