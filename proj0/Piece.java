public class Piece {

	/* Declaring instance variables */
	public boolean element;
	public Board board; 
	public int xpos;
	public int ypos;
	public  String piecetype; // CHANGE TO PRIVATE SLIGUHDSRLIGHSDLIRHUdDRLIUGHLSIRHGLISRHUGLISDHGLIDSHGLIDHSLGIRHUDSLIRUGHSDRDLSIHGLDIFHGLDIHGLISUHLIU

	/* Constructor for a Piece */
	public Piece(boolean isFire, Board b, int x, int y, String type) {
		element= isFire; 
		board= b;
		xpos= x; 
		ypos= y;
		piecetype= type; 
	}

	/* Fire piece? */
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
}