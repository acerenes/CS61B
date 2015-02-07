public class Board {

	public Board(boolean shouldBeEmpty) { // Almost like a constructor, yeah?
		if (shouldBeEmpty) { // Display a blank 8x8 board
			int scale= 8; // 8 because far side of 7 is 8
			StdDrawPlus.setXscale(0, scale);
			StdDrawPlus.setYscale(0, scale);
			for (int i=0; i<=7; i= i+1) { // Rows
				for (int j=0; j<=7; j= j+1) { // Columns
					// Decide which color the square will be
					if ((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0)) {
						StdDrawPlus.setPenColor(StdDrawPlus.GRAY);
					}
					else {
						StdDrawPlus.setPenColor(StdDrawPlus.RED);
					}
					// Now actually fill in the Square color
					StdDrawPlus.filledSquare(j+0.5, i+0.5, 0.5);
					StdDrawPlus.setPenColor(StdDrawPlus.WHITE); 

					}
				}
			}

		}

	public static void main(String [] args) {
		Board board= new Board(true);
	}
}