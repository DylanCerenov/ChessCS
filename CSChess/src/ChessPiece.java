/**
 *
 * @author Dylan and Ari
 */
public abstract class ChessPiece
{
    private int row;
    private int col;
    private int color;
    public boolean[][] availableMoves; //changed to public because you need access in Knight.java to pass this in an
    // overridden method.

    /**
     * Creates a ChessPiece with a row, column, and color.
     *
     * @param r the given row
     * @param c the given column
     * @param co the color (0 is white, 1 is black)
     */
    public ChessPiece(int r, int c, int co)
    {
        row = r;
        col = c;
        color = co;
        availableMoves = new boolean[8][8];

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                availableMoves[i][j] = false;
            }
        }
    }

    public int getRow()         { return row; }
    public int getCol()         { return col; }
    public void setRow(int r)   { row = r; }
    public void setCol(int c)   { col = c; }
    public int getColor()       { return color; } // 0 white 1 black

    /**
     * @return The index of the available move.
     */
    public boolean getAM(int r, int c)
    {
        return availableMoves[r][c];
    }

    /**
     * Changes the value of the available move index.
     * @param r
     * @param c
     * @param bool
     */
    public void changeAM(int r, int c, boolean bool)
    {
        availableMoves[r][c] = bool;
    }

    /**
     *
     * @param r the row of the destination
     * @param c the column of the destination
     * @return true if this piece can move to the location (r,c)
     */
    public abstract boolean canMove(int r, int c, ChessBoard b);

    /**
     * If possible, moves this ChessPiece to (r,c). If this piece moves successfully, returns true. Returns false otherwise.
     *
     * @param r the row of the destination
     * @param c the column of the destination
     * @param b the board where the piece is moving
     * @return true if the piece moves successfully, false otherwise
     */
    public abstract boolean move(int r, int c, ChessBoard b);

    /**
     * Gets the 8x8 boolean array of possible locations this piece can move to.
     *
     * @return an 8x8 boolean array of possible locations this piece can move to
     */
    public abstract boolean[][] getAvailableMoves(ChessBoard b);

    /**
     * Returns the String representation of this ChessPiece. This will be b or w, representing black or white, followed by either a capital P, B, R, N, Q, or K, representing either Pawn, Bishop, Rook, Knight, Queen, or King respectively.
     *
     * @return The String representation of this ChessPiece
     */
    public abstract String toString();
}