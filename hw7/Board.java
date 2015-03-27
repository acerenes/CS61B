public class Board {

    public static final int SIZE = 8;
    // You can call this variable by Board.SIZE.

	private Piece[][] pieces;
    private boolean isFireTurn;

    public Board() {
        pieces = new Piece[SIZE][SIZE];
        isFireTurn = true;
    }

    /** Makes a custom Board. Not a completely safe operation because you could do
    * some bad stuff here, but this is for the purposes of testing out hash
    * codes so let's forgive the author. 
    */
    public Board(Piece[][] pieces) {
        this.pieces = pieces;
        isFireTurn = true;
    }

	@Override
	public boolean equals(Object o) {
        if ((this != null) && (o != null) && (o instanceof Board)) {
            Board other = (Board) o;
            if ((this.isFireTurn == other.isFireTurn) && (this.SIZE == other.SIZE)) {
                for (int i = 0; i < this.pieces.length; i = i + 1) {
                    for (int j = 0; j < this.pieces[i].length; j = j + 1) {
                        if (this.pieces[i][j] != other.pieces[i][j]) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        return false;
	}

    @Override
    public int hashCode() {
        if (this == null) {
            return 0;
        }
        int returnHash = 0;
        for (int i = 0; i < this.pieces.length; i = i + 1) {
            for (int j = 0; j < this.pieces[i].length; j = j + 1) {
                if (this.pieces[i][j] != null) {
                    returnHash = returnHash + this.pieces[i][j].hashCode();
                }
                returnHash = returnHash << 1;
            }
        }
        return returnHash;
    }
}