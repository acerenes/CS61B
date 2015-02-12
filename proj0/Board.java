public class Board {

	private static Piece[][] piece_array;
	private static Board board; 
	private int player= 1; // Start with fire
	private boolean has_selected_1= false;
	private boolean has_selected_0= false; 
	private boolean hasselected; 
	private int xval; 
	private int yval; 

	public static void main(String [] args) {
		piece_array= new Piece[8][8]; // Initialize piece_array to be 8x8 
		// technically rn full of nulls
		/*board= new Board(true); // Call the board constructor*/
		// --^ USE THAT FOR AUTOGRADER
		board= new Board(false);
		/* board.select(0, 0); */ // SELECT CAN HIGHLIGHT!!!
		
	}

	public Board(boolean shouldBeEmpty) { // Constructor
		if (shouldBeEmpty== false) {
			// Need to put in the default configuration of the board
			for(int i=0; i<8; i= i+1) { // Rows
				for(int j=0; j<8; j= j+1) { // Columns 
					if (i==0 && j%2==0) {
						piece_array[i][j]= new Piece(true, board, j, i, "pawn"); 
					}
					else if (i==1 && j%2==1) {
						piece_array[i][j]= new Piece(true, board, j, i, "shield"); 
					}
					else if (i==2 && j%2==0) {
						piece_array[i][j]= new Piece(true, board, j, i, "bomb"); 
					}
					else if (i==5 && j%2==1) {
						piece_array[i][j]= new Piece(false, board, j, i, "bomb");
					}
					else if (i==6 && j%2==0) {
						piece_array[i][j]= new Piece(false, board, j, i, "shield");
					}
					else if (i==7 && j%2==1) {
						piece_array[i][j]= new Piece(false, board, j, i, "pawn"); 
					}
					else {
						piece_array[i][j]= null; 
					}	
					
				}
			}
			drawBoard(8);
		}

		else {
			drawBoard(8);
		}
	}

	private static void drawBoard(int N) {
		int scale= 8;
		StdDrawPlus.setXscale(0, scale);
		StdDrawPlus.setYscale(0, scale);
		for (int i=0; i<N; i= i+1) { // Rows
			for (int j=0; j<N; j= j+1) { // Columns
				// Decide which color the square will be
				if ((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0)) {
					StdDrawPlus.setPenColor(StdDrawPlus.GRAY);
				}
				else {
					StdDrawPlus.setPenColor(StdDrawPlus.RED);
				}
				// Now actually fill in the Square color
				StdDrawPlus.filledSquare(j+0.5, i+0.5, 0.5);
			}
		}
		for (int i=0; i<N; i= i+1) {
			for (int j=0; j<N; j= j+1) {
				if (piece_array[i][j]==null) {
						continue; 
				}
				else if (piece_array[i][j].isFire() && piece_array[i][j].isBomb()==false && piece_array[i][j].isShield()==false) {
					StdDrawPlus.picture(j+0.5, i+0.5, "img/pawn-fire.png", 1, 1); 
				}
				else if (piece_array[i][j].isFire() && piece_array[i][j].isShield()) {
					StdDrawPlus.picture(j+0.5, i+0.5, "img/shield-fire.png", 1, 1);
				}
				else if (piece_array[i][j].isFire() && piece_array[i][j].isBomb()) {
					StdDrawPlus.picture(j+0.5, i+0.5, "img/bomb-fire.png", 1, 1);
				}
				else if (piece_array[i][j].isFire()== false && piece_array[i][j].isBomb()) {
					StdDrawPlus.picture(j+0.5, i+0.5, "img/bomb-water.png", 1, 1);
				}
				else if (piece_array[i][j].isFire()== false && piece_array[i][j].isShield()) {
					StdDrawPlus.picture(j+0.5, i+0.5, "img/shield-water.png", 1, 1);
				}
				else if (piece_array[i][j].isFire()== false && piece_array[i][j].isBomb()==false && piece_array[i][j].isShield()==false) {
					StdDrawPlus.picture(j+0.5, i+0.5, "img/pawn-water.png", 1, 1);
				}
			}
		}
	}

	public Piece pieceAt(int x, int y) {
		if (x>8 || y>8 || piece_array[y][x]==null) {
			return null; 
		}
		else {
			return piece_array[y][x];
		}
	}

	public boolean canSelect(int x, int y) {
		Piece piece= piece_array[y][x];
		if (player==0) {
			hasselected= has_selected_0;
		}
		else if (player==1) {
			hasselected= has_selected_1;
		}
		if (piece!=null) { // Square w/a piece
			if (piece.side()== player && (hasselected==false)) { // DRGHRLIGHALIHALUH HAVEN'T FINISHED IT
				return false; // CHANGE
			}
		}
		else { // Empty square
			return false; // CHANGE
		}
		return false; // CHANGE 
	}

	/* MAKE SURE THIS METHOD IS PRIVATE DLIRUGHDSLRIUGHDSLIRUHGLSDIRUGHLISDRUHGLIDSURHGLIDSUHGLISUD OKAY */
	private boolean validMove(int xi, int yi, int xf, int yf) {
		/* Make sure not trying to move it out of bounds */
		if (xf >8 || yf>8) {
			return false;
		}
		
		// Single Step
		if (Math.abs(xf-xi)==1 && Math.abs((yf-yi))==1) {
			return single_step(xi, yi, xf, yf); 
		}
		/* Single capture */
		if (Math.abs(xf-xi)==2 && Math.abs(yf-yi)==2) {
			return single_capture(xi, yi, xf, yf);
		}
		/* Multi-capture */
		if (Math.abs(xf-xi)>=3 && Math.abs(yf-yi)>=3 && Math.abs(xf-xi)==Math.abs(yf-yi) && pieceAt(xf, yf)==null) {
			return multi_capture(xi, yi, xf, yf);
		}
		else {
			return false; 
		}		
	}

	public void select(int x, int y) {
		Piece piece= piece_array[y][x]; 
		StdDrawPlus.setPenColor(StdDrawPlus.WHITE); 
		StdDrawPlus.filledSquare(x+0.5, y+0.5, 0.5); 
		if (piece.isFire() && piece.isBomb()==false && piece.isShield()==false) {
			StdDrawPlus.picture(x+0.5, y+0.5, "img/pawn-fire.png", 1, 1); 
		}
		else if (piece.isFire() && piece.isShield()== true) {
			StdDrawPlus.picture(x+0.5, y+0.5, "img/shield-fire.png", 1, 1);
		}
		else if (piece.isFire() && piece.isBomb()== true) {
			StdDrawPlus.picture(x+0.5, y+0.5, "img/bomb-fire.png", 1, 1);
		}
		else if (piece.isFire()== false && piece.isBomb()== true) {
			StdDrawPlus.picture(x+0.5, y+0.5, "img/bomb-water.png", 1, 1);
		}
		else if (piece.isFire()== false && piece.isShield()== true) {
			StdDrawPlus.picture(x+0.5, y+0.5, "img/shield-water.png", 1, 1);
		}
		else if (piece.isFire()== false && piece.isBomb()==false && piece.isShield()==false) {
			StdDrawPlus.picture(x+0.5, y+0.5, "img/pawn-water.png", 1, 1);
		}

		if (player==0) {
			has_selected_0= true; 
		}
		else if (player==1) {
			has_selected_1= true;
		}
	}

	public void place(Piece p, int x, int y) {
		if (x>8 || y>8 || p==null) {
			return;
		}
		else {
			int xval= getXPos(p);
			int yval= getYPos(p); // Current position
			piece_array[y][x]= p; 
			piece_array[yval][xval]= null; 
			p.move(x, y); // Have to update back-end stuff too 
		}

	}

	public Piece remove(int x, int y) {
		if (x>=8 || y>=8) {
			System.out.println("Input (" + x + ", " + y + ") is out of bounds.");
			return null; 
		}
		else if (piece_array[y][x]== null) {
			System.out.println("There is no piece at (" + x + ", " + y + ")."); 
			return null; 
		}
		else {
			Piece removed_piece= piece_array[y][x]; 
			piece_array[y][x]= null; 
			return removed_piece; 
		}
	}

	

	public boolean canEndTurn() {
		
		
	}

	public void endTurn() {
		
	}

	private int getXPos(Piece p) {
		for(int i=0; i<8; i= i+1) { // Rows
			for(int j=0; j<8; j= j+1) { // Columns
				if (piece_array[i][j]==p) {
					xval= j; 
				}
			}
		}
		return xval; 
	}

	private int getYPos(Piece p) {
		for(int i=0; i<8; i= i+1) {
			for(int j=0; j<8; j= j+1) {
				if (piece_array[i][j]==p) {
					yval= i;
				}
			}
		}
		return yval; 
	}

	private boolean single_step(int xi, int yi, int xf, int yf) {
		Piece curr_piece= pieceAt(xi, yi);
		if (curr_piece.isKing()==false) {
			if (curr_piece.isFire()) { // Fire
				if (Math.abs(xf-xi)==1 && (yf-yi)==1 && pieceAt(xf, yf)==null) {
					return true;
				}
				else {
					return false; 
				}
			}
			else { // Water
				if (Math.abs(xf-xi)==1 && (yf-yi)== -1 && pieceAt(xf, yf)==null) {
					return true; 
				}
				else {
					return false;
				}
			}
		}
		else { // Is a king- can move forward & back
			if (pieceAt(xf, yf)==null) {
				return true; 
			}
			return false; 
		}
	}

	private boolean single_capture(int xi, int yi, int xf, int yf) {
		Piece curr_piece= pieceAt(xi, yi);
		if (curr_piece.isKing()==false) {
			if (curr_piece.isFire()) { // Fire 
				if (Math.abs(xf-xi)==2 && (yf-yi)==2 && pieceAt((xi+xf)/2, yi+1).isFire()==false && pieceAt(xf, yf)==null) {
						return true;
					}
					return false;
				}
			else { // Water
				if (Math.abs(xf-xi)==2 && (yf-yi)== -2 && pieceAt((xi+xf)/2, yi-1).isFire() && pieceAt(xf, yf)==null) {
					return true;
				}
				return false; 
			}
		}
		else { // King can move forward & backward
			if (pieceAt((xi+xf)/2, (yi+yf)/2).isFire() != curr_piece.isFire() && pieceAt(xf, yf)==null) {
				return true;
			}
			return false;
		}
	}

	private boolean multi_capture(int xi, int yi, int xf, int yf) {
		Piece curr_piece= pieceAt(xi, yi);
		if (curr_piece.isKing()==false) {
			if (curr_piece.isFire()) { // Fire 
				if ((yf-yi)>=3 && Math.abs(xf-xi)==(yf-yi)) {
					int i= (xf-xi)/Math.abs(xf-xi); // -1 or 1
					while (xi!=xf) { // Supes complicated cause sign- right or left
						if (pieceAt(xi+i, yi+1).isFire()==false) {
							xi= xi + 2*i;
							yi= yi+2;
							continue; 
						}
						else {
							return false; 
						}
					}
					return true; 
				}
				else {
					return false;
				}
			}
			else { // Water
				if ((yf-yi)<=-3 && Math.abs(xf-xi)== -(yf-yi)) {
					int i= (xf-xi)/Math.abs(xf-xi); // -1 or 1
					while(xi!=xf) { 
						if (pieceAt(xi+i, yi-1).isFire()) {
							xi= xi+2*i;
							yi= yi-2;
							continue; 
						}
						else {
							return false; 
						}
					}
				}
				else {
					return false;
				}
			}
		}
		else { // King can move back+forward
			int i= (xf-xi)/Math.abs(xf-xi);
			int j= (yf-yi)/Math.abs(yf-yi);
			while(xi!=xf) {
				if (pieceAt(xi+i, yi+j).isFire() != curr_piece.isFire()) {
					xi= xi + 2*i; 
					yi= yi + 2*j;
					continue; 
				}
				else {
					return false; 
				}
			}
			return true; 
		}
		return false; // tbh I'm not sure why this is necessary, but java gets mad if I don't have it	
	}

	

	

	public String winner() {
		int fire_count= 0; 
		int water_count= 0;
		for (int i=0; i<8; i= i+1) { // Rows
			for (int j=0; j<8; j= j+1) { // Columns
				Piece curr_piece= piece_array[j][i];
				if (curr_piece==null) {
					continue;
				}
				else if (curr_piece.isFire()==true) {
					fire_count= fire_count +1;
				}
				else if (curr_piece.isFire()==false) {
					water_count= water_count+1; 
				}
			}
		}
		if (fire_count!=0 && water_count!=0) {
			return null; // You don't win until all of opponent's pieces are removed, oui?
		}
		if (fire_count== water_count) {
			return "No one";
		}
		if (fire_count> water_count) {
			return "Fire";
		}
		else if (water_count>fire_count) {
			return "Water";
		}
		return null; // I guess all other things are null
	}
	
}