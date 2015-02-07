public class Board {

	public Board(boolean shouldBeEmpty) {
		if (shouldBeEmpty) { // Display a blank 8x8 board
			for (i==0; i<=7; i= i+1) { // Rows
				for (j==0; j<=7; j= j+1) { // Columns
					// Decide which color the square will be
					if ((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0)) {
						StdDrawPlus.setPenColor(StdDrawPlus.GRAY);
					else {
						StdDrawPlus.setPenColor(StdDrawPlus.RED);
					}

					}
				}
			}

		}
	}

	public static void main(String [] args) {
		StdDrawPlus.setPenColor(StdDrawPlus.Color GRAY)
		// Drawing the Board???
		for (int i=0, int j=0; i<=7,  )
			StdDrawPlus.filledSquare()
	}
}