public class Board {

	private Piece[][] piece_array;
	//okay so you don't declare private Board board because every class automatically has a field that is itself. So for the rest of the methods BESIDES MAIN use this.
	private int player= 1; 
	private boolean has_selected_1= false;
	private boolean has_selected_0= false; 
	private boolean hasselected; 
	private int xval; 
	private int yval; 
	private boolean hasmoved= false; 
	private boolean prevselected= false; 
	private Piece prevselectedpiece= null; 
	private boolean hascaptured= false; 
	private Piece selectedpiece= null;
	private Piece prepped_piece_4move= null;  

	private static boolean[][] pieces; 



	// FOR TESTING
	/*public Piece[][] piece_array;
	//okay so you don't declare private Board board because every class automatically has a field that is itself. So for the rest of the methods BESIDES MAIN use this.
	public int player= 1; 
	public boolean has_selected_1= false;
	public boolean has_selected_0= false; 
	public boolean hasselected; 
	public int xval; 
	public int yval; 
	public boolean hasmoved= false; */
	/*private boolean prevselected= false; */
	/*public Piece prevselectedpiece= null; 
	public boolean hascaptured= false; 
	public Piece selectedpiece= null;
	public Piece prepped_piece_4move= null;  

	public static boolean[][] pieces; */


	public static void main(String [] args) {
		int scale= 8;
		StdDrawPlus.setXscale(0, scale);
		StdDrawPlus.setYscale(0, scale);
		pieces= new boolean[scale][scale]; 
		Board board= new Board(false); // but declare a new board here because like main methods are ~seperate~ from the rest of the class, so in order to like call up a new board, can call it something else (not this)
		// And so for the rest of the main method, to access stuff from this board that you newly created, have to use board.STUFF not this.STUFF
		
		while (true) {
			/*board.drawBoard(scale); */
			board.updateBoard();
			if (StdDrawPlus.mousePressed()) {
				double x= StdDrawPlus.mouseX(); 
				double y= StdDrawPlus.mouseY();
				if (board.canSelect((int) x, (int) y)) {
					board.select((int) x, (int) y); 

				}
			
			/*pieces[(int) x][(int) y]= true;*/
			/*board.updateBoard();
*/			}
			if (StdDrawPlus.isSpacePressed() ) {
				System.out.println("Space pressed"); 
				if (board.canEndTurn()) {
					board.endTurn(); // b/c non-static methods- need the .
					System.out.println("ended turn"); 
				}
			}
			StdDrawPlus.show(1); 
			/*board.updateBoard();*/ // updating stuff in while loop. Outside while loop, nothing happens, because the while loop is always true. 
		}



		/* board.select(0, 0); */ // SELECT CAN HIGHLIGHT!!!
		
	}

	private void updateBoard() {
		this.drawBoard(8); 
	}

	public Board(boolean shouldBeEmpty) { // Constructor
		piece_array= new Piece[8][8]; 
		if (shouldBeEmpty== false) {
			// Need to put in the default configuration of the board
			for(int i=0; i<8; i= i+1) { // Rows
				for(int j=0; j<8; j= j+1) { // Columns 
					if (i==0 && j%2==0) {
						piece_array[i][j]= new Piece(true, this, j, i, "pawn"); 
					}
					else if (i==1 && j%2==1) {
						piece_array[i][j]= new Piece(true, this, j, i, "shield"); 
					}
					else if (i==2 && j%2==0) {
						piece_array[i][j]= new Piece(true, this, j, i, "bomb"); 
					}
					else if (i==5 && j%2==1) {
						piece_array[i][j]= new Piece(false, this, j, i, "bomb");
					}
					else if (i==6 && j%2==0) {
						piece_array[i][j]= new Piece(false, this, j, i, "shield");
					}
					else if (i==7 && j%2==1) {
						piece_array[i][j]= new Piece(false, this, j, i, "pawn"); 
					}
					else {
						piece_array[i][j]= null; 
					}	
					
				}
			}
		}

		else {
			return; 
		}
	}

	private void drawBoard(int N) {
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
					if (piece_array[i][j].isKing()) {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/pawn-fire-crowned.png", 1, 1); 
					}
					else {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/pawn-fire.png", 1, 1); 
					}
				}
				else if (piece_array[i][j].isFire() && piece_array[i][j].isShield()) {
					if (piece_array[i][j].isKing()) {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/shield-fire-crowned.png", 1, 1); 
					}
					else {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/shield-fire.png", 1, 1);
					}
				}
				else if (piece_array[i][j].isFire() && piece_array[i][j].isBomb()) {
					if (piece_array[i][j].isKing()) {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/bomb-fire-crowned.png", 1, 1);
					}
					else {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/bomb-fire.png", 1, 1);
					}
				}
				else if (piece_array[i][j].isFire()== false && piece_array[i][j].isBomb()) {
					if (piece_array[i][j].isKing()) {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/bomb-water-crowned.png", 1, 1);
					}
					else {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/bomb-water.png", 1, 1);
					}
				}
				else if (piece_array[i][j].isFire()== false && piece_array[i][j].isShield()) {
					if (piece_array[i][j].isKing()) {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/shield-water-crowned.png", 1, 1);
					}
					else {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/shield-water.png", 1, 1);
					}
				}
				else if (piece_array[i][j].isFire()== false && piece_array[i][j].isBomb()==false && piece_array[i][j].isShield()==false) {
					if (piece_array[i][j].isKing()) {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/pawn-water-crowned.png", 1, 1); 
					}
					else {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/pawn-water.png", 1, 1);
					}
				}
			}
		}
	}

	public Piece pieceAt(int x, int y) {
		if (x>7 || y>7 || x<0 || y<0 || piece_array[y][x]==null) {
			return null; 
		}
		else {
			return piece_array[y][x];
		}
	}

	public boolean canSelect(int x, int y) {
		System.out.println("I am indeed running canSelect"); 
		Piece piece= piece_array[y][x];
		if (player==0) {
			hasselected= has_selected_0;
		}
		else if (player==1) {
			hasselected= has_selected_1;
		}
		if (piece!=null) { // Square w/a piece
			// for side(), Fire= 0
			if (piece.side()!= player && hasselected==false) { 
				/*prevselected= true; */
				/*System.out.println("went into loop 206");*/ 
				prevselectedpiece= piece; 
				/*hasselected= true; 
				if (player==0) {
						has_selected_0= true; 
					}
					else if (player==1) {
						has_selected_1= true; 
					}*/
				System.out.println("canSelect = true"); 
				return true; 
			}
			else if (piece.side()!= player && hasselected && !hasmoved) {
				/*System.out.println("went into loop 213"); */
				prevselectedpiece= piece; 
				/*hasselected= true; 
				if (player==0) {
						has_selected_0= true; 
					}
					else if (player==1) {
						has_selected_1= true; 
					}*/
				System.out.println("canSelect = true"); 
				return true; 
			}
			System.out.println("canSelect = false"); 
			return false;
		}
		else { // Empty square
			if (prevselectedpiece!=null) {
				/*System.out.println("went into loop 222"); 
				System.out.println("prevselectedpiece = " + prevselectedpiece); */
				int xi= getXPos(prevselectedpiece); 
				int yi= getYPos(prevselectedpiece); 
				if (piece==null && validMove(xi, yi, x, y)) {
					/*System.out.println("went into loop 226");*/
					/*hasselected= true; 
					if (player==0) {
						has_selected_0= true; 
					}
					else if (player==1) {
						has_selected_1= true; 
					}*/
					System.out.println("canSelect = true"); 
					return true; 
				}
				if (prevselectedpiece!=null && hascaptured && hasselected) {
					/*System.out.println("went into loop 232");*/
					/*hasselected= true; 
					if (player==0) {
						has_selected_0= true; 
					}
					else if (player==1) {
						has_selected_1= true; 
					}*/
					System.out.println("canSelect = true"); 
					return true; 
				}
			}
			/*hasselected= false;
			if (player==0) {
				has_selected_0= false; 
			}
			else if (player==1) {
				has_selected_1= false; 
			}*/
			System.out.println("canSelect = false"); 
			return false; 
		}
	}

	/* MAKE SURE THIS METHOD IS PRIVATE DLIRUGHDSLRIUGHDSLIRUHGLSDIRUGHLISDRUHGLIDSURHGLIDSUHGLISUD OKAY */
	private boolean validMove(int xi, int yi, int xf, int yf) {
		/* Make sure not trying to move it out of bounds */
		if (xf >8 || yf>8) {
			return false;
		}
		
		// Single Step
		if (Math.abs(xf-xi)==1 && Math.abs((yf-yi))==1) {
			if (hasmoved) {
				return false;
			}
			hasmoved= true; 
			return single_step(xi, yi, xf, yf); 
		}
		/* Single capture */
		if (Math.abs(xf-xi)==2 && Math.abs(yf-yi)==2) {
			hascaptured= true; 
			return single_capture(xi, yi, xf, yf);
		}
		/* Multi-capture */
		if (Math.abs(xf-xi)>=3 && Math.abs(yf-yi)>=3 && Math.abs(xf-xi)==Math.abs(yf-yi) && pieceAt(xf, yf)==null) {
			hascaptured= true; 
			return multi_capture(xi, yi, xf, yf);
		}
		else {
			return false; 
		}		
	}

	public void select(int x, int y) {
		/*Piece piece= piece_array[y][x]; 
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
		}*/
		
		selectedpiece= pieceAt(x, y); 
		if (selectedpiece==null && prepped_piece_4move==null) {
			return; 
		}
		else if (selectedpiece!=null) { // square with a piece
			prepped_piece_4move= selectedpiece; 
			int xpos= getXPos(selectedpiece); 
			int ypos= getYPos(selectedpiece); 
			/*board.place(xpos, ypos); */ // I don't think you actually move anything if you just prep a piece for movement
		}
		else if (selectedpiece==null && prepped_piece_4move!=null) {
			int xpos= getXPos(prepped_piece_4move); 
			int ypos= getYPos(prepped_piece_4move);
			prepped_piece_4move.move(x, y); 
			/*board.place(x, y); */
			remove(xpos, ypos); 
			hasmoved= true; 
			/*this.updateBoard();*/
		}
		
		if (player==0) {
			has_selected_0= true; 

		}
		else if (player==1) {
			has_selected_1= true;
		}
		/*hasselected= true; */
		/*System.out.println("hasselected= " + hasselected); 
		System.out.println("hascaptured= " + hascaptured); 
		System.out.println("hasmoved= " + hasmoved); */
	}

	public void place(Piece p, int x, int y) {
		if (x>7 || y>7 || x<0 || y<0 || p==null) {
			return;
		}
		else {
			piece_array[y][x]= p; 
			/*hasmoved= true; */
		}

	}

	public Piece remove(int x, int y) {
		if (x>7 || y>7) {
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
		if (hasmoved || hascaptured) {
			return true; 
		}
		return false; 
	}
	

	public void endTurn() {
		if (player==1) {
			player=0; 
		}
		else if (player==0) {
			player=1; 
		}
		has_selected_1= false; 
		has_selected_0= false; 
		hasselected= false; 
		hasmoved= false; 
		prevselected= false; 
		prevselectedpiece= null; 
		hascaptured= false; 
		selectedpiece= null; 
		prepped_piece_4move= null; 
		// Basically I'm just setting them all back to their default states, for a clean slate for the next turn 
		
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
		if (fire_count==0 && water_count==0) {
			return "No one"; 
		}

		else if (fire_count>0 && water_count==0) {
			return "Fire"; 
		}

		else if (water_count>0 && fire_count==0) {
			return "Water";
		}

		else {
			return null; 
		}
	}
	
}