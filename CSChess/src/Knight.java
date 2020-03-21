// 12MAR2020 9:24PM STATUS: incomplete at the moment due to the check movement.
public class Knight extends ChessPiece
{
    public Knight(int initialRow, int initialCol, int pieceColor)
    {
        super(initialRow, initialCol, pieceColor);
    }

    // uses availableMoves to check if the move is valid.
    public boolean canMove(int r, int c, ChessBoard b)
    {
        // can move if the available move is there. Color of pieces should be
        // accounted for in the testMove method.
        super.availableMoves = this.getAvailableMoves(b);
        return this.getAM(r, c);
    }

    // handles all movement policy
    // checks if movement is possible
    // takes pieces off the board
    // moves the objects
    public boolean move(int r, int c, ChessBoard b)
    {
        int ogR = this.getRow();
        int ogC = this.getCol();

        if (this.canMove(r, c, b)) // movement is possible
        {
            // moves piece
            // if taking a piece
            if (b.pieceAt(r, c) != null)
            {
                // prints out the notification
                System.out.println(b.pieceAt(ogR, ogC).getColor() + " " + b.pieceAt(ogR, ogC) + " has taken " + b.pieceAt(r, c).getColor() + " " + b.pieceAt(r, c) + "!");

                b.changeBoard(r,c,null); // deletes the captured piece
            }

            b.changeBoard(r, c, b.pieceAt(ogR, ogC));
            b.changeBoard(ogR, ogC, null); // removes original piece from last position

            // This a was problem before, where the actual values inside the object weren't changed when the piece moved.
            // Before, only the location inside the array moved. Not both location along with piece values.
            this.setRow(r);
            this.setCol(c);

            return true;
        }

        return false; // movement is impossible.
    }

    public boolean[][] getAvailableMoves(ChessBoard b)
    {
        // super.availableMoves[][]
        // get original coordinates
        int ogR = this.getRow();
        int ogC = this.getCol();

        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                if (testMove(ogR, ogC, i, j, b))
                    this.changeAM(i,j,true); // I can remove changeAM if AM is allowed to stay public in CP.java.
            }
        }
        return super.availableMoves;
    }

    // used to test each individual move for the getAvailableMethods()

    /**
     * Tries to test if the move works, returns true.
     * If none of the tests works it returns false.
     * Tries to prove true until false.
     */
    public boolean testMove(int or, int oc, int fr, int fc, ChessBoard b)
    {
        // move must either be to an empty tile or opposite color piece
        // this method tests if knight can move from or oc to fr fc
        // cycles through valid move locations and if the fr and fc match up return true

        /*
         * Unique movement checks.
         *  - Can move in L shapes
         *  - Can jump over pieces.
         */
        for (int x = -2; x < 3; x++)
        {
            if (x == 0) // cycles through -2 -1 1 2
                x++;

            if (x % 2 == 0) // if even, check +1 -1
            {
                // if spacing is right and the other tile is null or opposite color
                /*if (or+x == fr && oc+1 == fc && (b.pieceAt(fr,fc).getColor() != this.getColor() || b.pieceAt(fr,fc) == null))
                    return true;
                else if (((or+x == fr) && (oc-1 == fc) && (b.pieceAt(fr,fc) == null)) || (b.pieceAt(fr,fc).getColor() != this.getColor()))
                    return true;*/

                // check null first, if not null then check for color.
                // problem with the code was that the code automatically calls b.pieceAt and there might not be
                if (b.pieceAt(fr,fc) != null)
                {
                    if (or+x == fr && oc+1 == fc && (b.pieceAt(fr,fc).getColor() != this.getColor()))
                        return true;
                    else if ((or+x == fr) && (oc-1 == fc) && (b.pieceAt(fr,fc).getColor() != this.getColor()))
                        return true;
                }
                else // if null dont check color.
                {
                    if (or+x == fr && oc+1 == fc)
                        return true;
                    else if ((or+x == fr) && (oc-1 == fc))
                        return true;
                }

            }
            else // odd, check +2 -2
            {
                /*if (or+x == fr && oc+2 == fc && (b.pieceAt(fr,fc).getColor() != this.getColor() || b.pieceAt(fr,fc) == null))
                    return true;
                else if (or+x == fr && oc-2 == fc && (b.pieceAt(fr,fc).getColor() != this.getColor() || b.pieceAt(fr,fc) == null))
                    return true;*/
                if (b.pieceAt(fr,fc) != null)
                {
                    if (or+x == fr && oc+2 == fc && (b.pieceAt(fr,fc).getColor() != this.getColor()))
                        return true;
                    else if ((or+x == fr) && (oc-2 == fc) && (b.pieceAt(fr,fc).getColor() != this.getColor()))
                        return true;
                }
                else // if null dont check color.
                {
                    if (or+x == fr && oc+2 == fc)
                        return true;
                    else if ((or+x == fr) && (oc-2 == fc))
                        return true;
                }
            }
        }

        /*
         * Non-unique movement checks.
         *  - Cannot stay in place.
         *  - Cannot move into a square occupied by the same color.
         *  - Cannot move if the King is in check.
         *      - Unless the move takes the King out of check.
         */
        // stays in place
        if (or == fr && oc == fc)
            return false;

        // cannot take same color.
        if (b.pieceAt(fr,fc) != null) // if not null
            if (b.pieceAt(fr, fc).getColor() == this.getColor()) // cant take piece of same color
                return false;

        // cannot move if King is in check.
        // TODO: come back to this after King is complete.
        // If King is in check and the other piece removes it from check.
        // *finds the original piece's color, and finds the king of that color.
        // *then checks if the king is in check
        if (b.getKing(b.pieceAt(or,oc).getColor()).returnCheck()) // new installment 20MAR2020 8:23PM
        {
            if (b.ifIMoveAPieceHereDoesItRemoveKingFromCheck(fr, fc, b.pieceAt(or, oc).getColor(), b))
            {
                return true;
            }
        }

        return false;
    }

    public String toString()
    {
        // 0 is white
        // 1 is black
        if (super.getColor() == 0)
            return "wN";
        else
            return "bN";
    }

    public PieceType getType()
    {
        return PieceType.KNIGHT;
    }
}