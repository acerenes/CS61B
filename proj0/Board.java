public class Board {

	private static Piece[][] piece_array;
	private static Board board; 

	public Board(boolean shouldBeEmpty) { // Constructor
		if (shouldBeEmpty== false) {
			// Need to put in the default configuration of the board
			drawBoard(8); // Put at beginning so won't update everytime I run through the loops
			for(int i=0; i<8; i= i+1) { // Rows
				for(int j=0; j<8; j= j+1) { // Columns 
					if (i==0 && j%2==0) {
						piece_array[i][j]= new Piece(true, board, j, i, "pawn"); 
					}
					if (i==1 && j%2==1) {
						piece_array[i][j]= new Piece(true, board, j, i, "shield"); 
					}
					if (i==2 && j%2==0) {
						piece_array[i][j]= new Piece(true, board, j, i, "bomb"); 
					}
					if (i==5 && j%2==1) {
						piece_array[i][j]= new Piece(false, board, j, i, "bomb");
					}
					if (i==6 && j%2==0) {
						piece_array[i][j]= new Piece(false, board, j, i, "shield");
					}
					if (i==7 && j%2==1) {
						piece_array[i][j]= new Piece(false, board, j, i, "pawn"); 
					}

					/* Now put in the pictures */
					if (i==0 && j%2==0) {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/pawn-fire.png", 1, 1); 
					}
					if (i==1 && j%2==1) {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/shield-fire.png", 1, 1);
					}
					if (i==2 && j%2==0) {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/bomb-fire.png", 1, 1);
					}
					if (i==5 && j%2==1) {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/bomb-water.png", 1, 1);
					}
					if (i==6 && j%2==0) {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/shield-water.png", 1, 1);
					}
					if (i==7 && j%2==1) {
						StdDrawPlus.picture(j+0.5, i+0.5, "img/pawn-water.png", 1, 1);
					}
				}
			}
		}

		else {
			drawBoard(8);
		}
	}


	private static void drawBoard(int N) {
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
	}

	public Piece pieceAt(int x, int y) {
		if (x>8 || y>8) {
			return null; 
		}
		else {
			return piece_array[y][x];
		}
	}

	public void place(Piece p, int x, int y) {
		if (x>8 || y>8 || p==null) {
			return;
		}
		else {
			piece_array[y][x]= p; 
		}

	}

	public static void main(String [] args) {
		int scale= 8; // 8 because far side of 7 is 8
		StdDrawPlus.setXscale(0, scale);
		StdDrawPlus.setYscale(0, scale);
		piece_array= new Piece[8][8]; // Initialize piece_array to be 8x8 
		// technically rn full of nulls
		board= new Board(false); // Call the board constructor
		
	}
}