/*
 * Authors: Dylan and Ari
 * Bishop.java
 * This class handles all movement pertaining to the bishop piece.
 */
public class Bishop extends ChessPiece
{
    public Bishop(int initialRow, int initialCol, int pieceColor)
    {
        super(initialRow, initialCol, pieceColor);
    }

    public boolean canMove(int fr, int fc, ChessBoard b)
    {
        int or = this.getRow();
        int oc = this.getCol();

        // piece must move diagonally.
        //  - piece must go the game units up/down as it goes left/right.
        // piece can't be intersected by another piece.
        //  - cycle through
        // final piece must be opposite color OR null.

        /*
         * Unique movement checks.
         *  - Moves any number of vacant squares in the up/down directions.
         *      - Places in between must be blank.
         *      - Final spot must be blank or opposite color.
         */
        // can't be in the same row or coloumn as before
        if (or == fr || oc == fc)
            return false;

        // has to be diagonal.
        // if it moves upright one, it moves right one and up one. Change up and down == change sideways
        if (Math.abs(or-fr) != Math.abs(oc-fc))
            return false;

        // have to cycle through like in Rook.
        // upleft    -r -c
        // upright   -r +c
        // downleft  +r -c
        // downright +r +c

        // change in row and col diagonally, minus two = number of places you need to check
        for (int i = 1; i <= Math.abs(or - fr)-1; i++) // amount of places needed to check
        {
            //System.out.print(or + " " + oc + " " + fr + " " + fc);
            //System.out.println("i: " + i + " ...: " + (Math.abs(or-fr)-1) + ".");
            if (or > fr && oc > fc) // moving upleft -r -c
            {
                if (b.pieceAt(or + (i*-1), oc + (i*-1)) != null)
                    return false;
            }
            else if (or > fr && oc < fc) // upright
            {
                if (b.pieceAt(or + (i*-1), oc + (i*1)) != null)
                    return false;
            }
            else if (or < fr && oc > fc) // downleft
            {
                if (b.pieceAt(or + (i*1), oc + (i*-1)) != null)
                    return false;
            }
            else // if (or < fr && oc < fc)
            {
                if (b.pieceAt(or + (i*1), oc + (i*1)) != null)
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

            return true;
        }

        return false;
    }

    public String toString()
    {
        // 0 is white
        // 1 is black
        if (super.getColor() == 0)
            return "wB";
        else
            return "bB";
    }

    public PieceType getType()
    {
        return PieceType.BISHOP;
    }
}