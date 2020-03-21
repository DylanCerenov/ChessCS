// 12MAR2020 9:24PM STATUS: incomplete at the moment due to the check movement.
public class Bishop extends ChessPiece
{
    public Bishop(int initialRow, int initialCol, int pieceColor)
    {
        super(initialRow, initialCol, pieceColor);
    }

    public boolean canMove(int r, int c, ChessBoard b)
    {
        super.availableMoves = this.getAvailableMoves(b);
        return this.getAM(r, c);
    }

    public boolean move(int r, int c, ChessBoard b)
    {
        int ogR = this.getRow();
        int ogC = this.getCol();

        if (this.canMove(r, c, b))
        {
            if (b.pieceAt(r, c) != null)
            {
                System.out.println(b.pieceAt(ogR, ogC).getColor() + " " + b.pieceAt(ogR, ogC) + " has taken " + b.pieceAt(r, c).getColor() + " " + b.pieceAt(r, c) + "!");

                b.changeBoard(r,c,null);
            }

            b.changeBoard(r, c, b.pieceAt(ogR, ogC));
            b.changeBoard(ogR, ogC, null);

            this.setRow(r);
            this.setCol(c);

            return true;
        }

        return false;
    }

    // gets available moves for the individual piece.
    public boolean[][] getAvailableMoves(ChessBoard b)
    {
        int ogR = this.getRow();
        int ogC = this.getCol();

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (testMove(ogR, ogC, i, j, b))
                    this.changeAM(i,j,true);
            }
        }
        return super.availableMoves;
    }

    /*
     * Tries to prove false until true.
     * Checks for all the things that will prevent it from working.
     */
    public boolean testMove(int or, int oc, int fr, int fc, ChessBoard b)
    {
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

        // cannot move if King is in check.
        // TODO: come back to this after King is complete.
        if (b.getKing(b.pieceAt(or,oc).getColor()).returnCheck()) // new installment 20MAR2020 8:23PM
            return b.ifIMoveAPieceHereDoesItRemoveKingFromCheck(fr, fc, b.pieceAt(or,oc).getColor(), b);

        return true;
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