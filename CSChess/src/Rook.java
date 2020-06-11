/*
 * Authors: Dylan and Ari
 * Rook.java
 * This class handles all movement pertaining to the rook piece.
 */
public class Rook extends ChessPiece
{
    private boolean hasMoved = false;

    public Rook(int initialRow, int initialCol, int pieceColor)
    {
        super(initialRow, initialCol, pieceColor);
    }

    public boolean canMove(int fr, int fc, ChessBoard b)
    {
        int or = this.getRow();
        int oc = this.getCol();

        // rook can move in + patterns
        // can only move row or col at one time
        // can not move through other pieces
        // can only move to final piece if it is opposite color

        /*
         * Unique movement checks.
         *  - Moves any number of vacant squares in the up/down directions.
         *      - Places in between must be blank.
         *      - Final spot must be blank or opposite color.
         *  - Castling TODO: install this later.
         */
        // Has to move in the up/down direction.
        if (or - fr != 0 && oc - fc != 0) // if it moves diagonally at all
            return false;

        if (or == fr) // if it moves columns, goes left/right
        {
            if (oc > fc) // moving left
            {
                for (int i = oc-1; i > fc; i--)
                    if (b.pieceAt(or, i) != null) // if theres a piece between where it starts and where it ends (not inclusive)
                        return false;
            }
            else // moving right
            {
                for (int i = oc+1; i < fc; i++)
                    if (b.pieceAt(or, i) != null)
                        return false;
            }
        }
        else // it moves rows up and down
        {
            if (or > fr) // if moving up
            {
                for (int i = or-1; i > fr; i--)
                    if (b.pieceAt(i, oc) != null)
                        return false;
            }
            else
            {
                for (int i = or+1; i < fr; i++)
                    if (b.pieceAt(i, oc) != null)
                        return false;
            }
        }

        /*
         * Non-unique movement checks.
         *  - Cannot stay in place.
         *  - Cannot move into a square occupied by the same color.
         *  - Cannot move if the King is in check.
         *      - Unless the move takes the King out of check.
         */
        // Cannot stay in place.
        if (or == fr && oc == fc)
            return false;

        // Cannot take same color.
        if (b.pieceAt(fr,fc) != null) // if not null
            if (b.pieceAt(fr, fc).getColor() == this.getColor()) // cant take piece of same color
                return false;

//        return (!b.ifIMoveAPieceHereIsKingInCheck(or,oc,fr,fc,b.pieceAt(or,oc).getColor(), b));

        return true;
    }

    public boolean move(int r, int c, ChessBoard b)
    {
        int ogR = this.getRow();
        int ogC = this.getCol();

        if (this.canMove(r, c, b))
        {
            if (b.pieceAt(r, c) != null)
            {
                if (b.pieceAt(ogR, ogC).getColor() == 0) // if white piece is taking black piece
                    System.out.println("White piece " + b.pieceAt(ogR, ogC) + " has taken black piece " + b.pieceAt(r,c) + ".");
                else // if black piece is taking white piece
                    System.out.println("Black piece " + b.pieceAt(ogR, ogC) + " has taken white piece " + b.pieceAt(r,c) + ".");

                // STALEMATE CHECK:
                Chess.turnsSincePawnMovedOrCaptureMade = 0;

                b.changeBoard(r,c,null);
            }

            b.changeBoard(r, c, b.pieceAt(ogR, ogC));
            b.changeBoard(ogR, ogC, null);

            b.pieceAt(r,c).setRow(r);
            b.pieceAt(r,c).setCol(c);

            //this.canMove(r,c,b); // new as of 1:48 3/21.

            // For Castling
            hasMoved = true;

            return true;
        }

        return false;
    }

    // For castling
    public boolean returnHasMoved()
    {
        return hasMoved;
    }


    public String toString()
    {
        // 0 is white
        // 1 is black
        if (super.getColor() == 0)
            return "wR";
        else
            return "bR";
    }

    public PieceType getType()
    {
        return PieceType.ROOK;
    }
}