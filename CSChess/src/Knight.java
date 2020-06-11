/*
 * Authors: Dylan and Ari
 * Knight.java
 * This class handles all movement pertaining to the knight piece.
 */
public class Knight extends ChessPiece
{
    public Knight(int initialRow, int initialCol, int pieceColor)
    {
        super(initialRow, initialCol, pieceColor);
    }

    public boolean canMove(int fr, int fc, ChessBoard b)
    {
        int or = this.getRow();
        int oc = this.getCol();

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

        /*// piece cannot move if it leaves the King in check.
        if (b.ifIRemoveThePieceHereDoesItPutKingInCheck(or, oc, fr, fc, b.pieceAt(or,oc).getColor(), b))
            return false; // If it puts king in check, move can't work.

        // cannot move if King is in check.
        // TODO: come back to this after King is complete.
        // If King is in check and the other piece removes it from check.
        // *finds the original piece's color, and finds the king of that color.
        // *then checks if the king is in check
        if (b.getKing(b.pieceAt(or,oc).getColor()).returnCheck()) // new installment 20MAR2020 8:23PM
            return b.ifIMoveAPieceHereDoesItRemoveKingFromCheck(or, oc, fr, fc, b.pieceAt(or, oc).getColor(), b);*/

        // if I move a piece there and king is not in check, move works
//        return (!b.ifIMoveAPieceHereIsKingInCheck(or,oc,fr,fc,b.pieceAt(or,oc).getColor(), b));

        return false;
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
                if (b.pieceAt(ogR, ogC).getColor() == 0) // if white piece is taking black piece
                    System.out.println("White piece " + b.pieceAt(ogR, ogC) + " has taken black piece " + b.pieceAt(r,c) + ".");
                else // if black piece is taking white piece
                    System.out.println("Black piece " + b.pieceAt(ogR, ogC) + " has taken white piece " + b.pieceAt(r,c) + ".");

                // STALEMATE CHECK:
                Chess.turnsSincePawnMovedOrCaptureMade = 0;

                b.changeBoard(r,c,null); // deletes the captured piece
            }

            b.changeBoard(r, c, b.pieceAt(ogR, ogC)); // moves og piece
            b.changeBoard(ogR, ogC, null); // removes original piece from last position

            // This a was problem before, where the actual values inside the object weren't changed when the piece moved.
            // Before, only the location inside the array moved. Not both location along with piece values.
            b.pieceAt(r,c).setRow(r);
            b.pieceAt(r,c).setCol(c);

            //this.canMove(r,c,b); // new as of 1:48 3/21.

            return true;
        }

        return false; // movement is impossible.
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