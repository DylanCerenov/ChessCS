// 12MAR2020 9:24PM STATUS: incomplete at the moment due to the check movement.
public class Rook extends ChessPiece
{
    public Rook(int initialRow, int initialCol, int pieceColor)
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

        // cannot move if King is in check.
        // TODO: come back to this after King is complete.
//        System.out.println("iopiop" + b.getKing(clr));
        if (b.getKing(b.pieceAt(or,oc).getColor()).returnCheck()) // new installment 20MAR2020 8:23PM
            return b.ifIMoveAPieceHereDoesItRemoveKingFromCheck(fr, fc, b.pieceAt(or,oc).getColor(), b);

        return true;
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