public class Piece {

	/* Declaring instance variables */
	/*private boolean element;
	private Board board; 
	private int xpos;
	private int ypos;
	private String piecetype;
	private Piece oldpiece; 
	private Piece newpiece;
	private Piece captured= null; */

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
		int oldx= this.xpos;
		int oldy= this.ypos;
		/*board.place(this, x, y);*/ 
		this.xpos= x;
		this.ypos= y;
		/* I think if you jumped over something, take that piece into a variable, and then remove it */
		// Only go through this if moved more than 1 space- possibility of a capture
		if (Math.abs(x-oldx)>=2 && Math.abs(y-oldy)>=2) {
			int i= (x- oldx)/ Math.abs(x- oldx); 
			int j= (y- oldy)/ Math.abs(y- oldy); 
			while(oldx!=x) {
				if ((i>0 && oldx>x) || (i<0 && oldx<x)) {
					break;
				}
				Piece jumped= board.pieceAt(oldx+i, oldy+j); 
				if (jumped==null) {
					oldx= oldx+2*i; 
					oldy= oldy+2*j;  
				}
				else if (jumped.element != this.element) {
					captured= board.remove(oldx+i, oldy+j);
					oldx= oldx+2*i; 
					oldy= oldy+2*j; 
				}
				else {
					break; 
				}

			}
		}
	}

	public boolean hasCaptured() { 
		if (captured != null) {
			return true;
		}
		return false;

	}
	public void doneCapturing() {
		captured= null; 
		/*this.hasCaptured()= false; */
		// YOU AREN'T WORKING ALIUGHALIGHALIHGLAIUHELIHU
	}

}

/* Thanks to Sreesha Venkat and Karin Goh and Anna-Marie for help. */ 