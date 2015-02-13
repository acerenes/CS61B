public class Piece {

	/* Declaring instance variables */
	private boolean element;
	private Board board; 
	private int xpos;
	private int ypos;
	private String piecetype;
	private Piece oldpiece; 
	private Piece newpiece;
	private Piece captured= null; 
	private boolean isFireKing= false; 
	private boolean isWaterKing= false; 


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
		if (isFire() && isFireKing) {
			return true; 
		}
		else if (!isFire() && isWaterKing) {
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


	public void move(int x, int y) { // Only called if move is valid 
		int oldx= this.xpos;
		int oldy= this.ypos;
		this.xpos= x;
		this.ypos= y;

		if (board==null) {
			System.out.println("Board is null"); 
		}

		if (board.pieceAt(oldx, oldy)!=null) {
			board.remove(oldx, oldy); 
		}
		board.place(this, x, y); // Place yourself on there if I move you
		/* I think if you jumped over something, take that piece into a variable, and then remove it */
		// Only go through this if moved more than 1 space- get the captured piece- possibility of a capture
		if (Math.abs(x-oldx)>=2 && Math.abs(y-oldy)>=2) {
			int i= (x- oldx)/ Math.abs(x- oldx); 
			int j= (y- oldy)/ Math.abs(y- oldy); // direction
			
			Piece captured= board.pieceAt(oldx+i, oldy+j); 
			board.remove(oldx+i, oldy+j);

			/* Now take care of bomb pieces exploding */
			if (this.isBomb()) {
				int xstart= x-1; 
				int ystart= y-1;
				while (xstart<=x+1 && xstart>=0 && xstart<=7) {
					ystart= y-1; // Set back to y-1 because gotta start back at y-1 place
					while (ystart<=y+1 && ystart>=0 && ystart<=7) {
						Piece gonepiece= board.pieceAt(xstart, ystart);
						if (gonepiece!= null && !gonepiece.isShield()) {
							board.remove(xstart, ystart);
						}
						ystart= ystart+1; 
					}
					xstart= xstart+1;
				}
			}
			/*if HAVE TO CHECK IF CAN CAPTURE AGAIN*/
		}

		if (board.pieceAt(x, y) != null) {
			if (reachedEnd(board.pieceAt(x, y), y)) { // Yes king
			/*DURSYGHDLSIRUGHDSLRIUHGDSRLIU HEY WRITE THE WHOLE CROWNING THING AND CHANGING ALSO 
			IF TIS A KING AND CAN CAPTURE AGAIN, DO THAT*/
				boolean isFire= false; 
				if (y==7) {
					isFire= true; 
				}
			}
		}
	}

	private boolean reachedEnd(Piece p, int y) {
		if (p.isFire()) {
			if (y==7) {
				isFireKing= true; 
				return true; 
			}
			return false; 
		}
		else {
			if (y==0) {
				isWaterKing= true; 
				return true;
			}
			return false; 
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
	}


/* Thanks to Sreesha Venkat and Karin Goh and Anna-Marie and Jenny for help. */
}