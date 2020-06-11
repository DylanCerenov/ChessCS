/*
 * Authors: Dylan and Ari
 * Pawn.java
 * This class handles all movement pertaining to the pawn piece.
 */
public class Pawn extends ChessPiece
{
    private boolean hasDoubleJumped = false;

    public Pawn(int initialRow, int initialCol, int pieceColor)
    {
        super(initialRow, initialCol, pieceColor);
    }

    public boolean canMove(int fr, int fc, ChessBoard b)
    {
        int or = this.getRow();
        int oc = this.getCol();

        /*
         * Unique movement checks:
         *  - Pawns can only move forward.
         *  - Pawns can only take pieces diagonally, but still forward.
         *  - Pawns can only move one space at a time.
         *  - However, they can move two spaces if they've never moved before. (get initial row to determine)
         *  - They can only move forward if the space is empty.
         *  - Pawn Promotion ** accounted for in ChessBoard.java
         *      - When pawn reaches the opposite King's Row it must become another piece.
         *      - Pieces include Knight, Bishop, Rook, or Queen.
         *      - Contrary to popular belief, the pawn can become any piece (therefore multiple queens is allowed).
         *  - En Passante
         *      - Dont worry about this until the rest of the code is finished.
         *        TODO: extra credit move
         */

        if (Chess.useFullMoveSet) // only run this code if full game is running...
        {
            if(b.pieceAt(or, oc).getColor() == 0) //white
            {
                // if the row is right and the column changes by one
                // moving diagonally from row 3 and moving forward
                if (or == 3 && Math.abs(oc-fc) == 1 && or - fr == 1)
                {
                    // If pawn that just double jumped is last moved
                    if (b.pieceAt(or,fc) != null && b.pieceAt(or,fc) instanceof Pawn)
                    {
                        if (b.pieceAt(or, fc) == b.returnChessPieceMovedForEnPassant() && ((Pawn) b.pieceAt(or,fc)).returnDoubleJumped())
                        {
                            if (b.pieceAt(or,oc).getColor() != b.pieceAt(or,fc).getColor())
                                return true;
                        }
                    }
                }
            }
            else // piece is black
            {
                if (or == 4 && Math.abs(oc-fc) == 1 && or - fr == -1)
                {
                    if (b.pieceAt(or,fc) != null && b.pieceAt(or,fc) instanceof Pawn)
                    {
                        if (b.pieceAt(or, fc) == b.returnChessPieceMovedForEnPassant() && ((Pawn) b.pieceAt(or,fc)).returnDoubleJumped())
                        {
                            if (b.pieceAt(or,oc).getColor() != b.pieceAt(or,fc).getColor())
                                return true;
                        }
                    }
                }
            }    
        }

        int rowDifference = Math.abs(or-fr);
        int colDifference = Math.abs(oc-fc);

        // System.out.println(or + " " + oc + " has piece " + b.pieceAt(or,oc));

        // Pawns can only move forward.
        int directionOfMovement = fr-or; // + is for black - is for white
        //System.out.println("mvnt dir: " + directionOfMovement + " and or oc is " + fr + " " + fc);
        if (directionOfMovement < 0 && (this.getColor() == 1)) // if a black piece is moving backwards
        {
            //System.out.println("[error]: backwards movement is not possible with pawn.");
            return false;
        }
        if (directionOfMovement > 0 && (this.getColor() == 0)) // if a white piece is moving backwards
        {
            //System.out.println("[error]: backwards movement is not possible with pawn.");
            return false;
        }

        // Pawn movement checks
        if (oc == fc) // If columns stay same, Pawn is moving straight ahead.
        {
            /*
             * Three options here:
             *  - player moves one ahead
             *      - piece at final has to be null.
             *  - player moves two ahead
             *      - piece must be at original row location.
             *      - piece at final has to be null.
             *      - piece between oroc and frfc has to be null.
             *  - player moves incorrectly.
             */
            if (rowDifference == 1) // piece if moving one ahead
            {
                if (b.pieceAt(fr,fc) != null) // if there is a piece where he wants to move he cant
                    return false;
            }
            else if (rowDifference == 2) // piece moving two ahead
            {
                int tempDirection = -1; // Defaults to white piece moving, moves - direction
                if (this.getColor() == 1) // black piece moving, moves + direction
                    tempDirection = 1;

                // checks to see if there are pieces in the way
                if (b.pieceAt(or + tempDirection, oc) != null)
                    return false;
                if (b.pieceAt(or + (2*tempDirection), oc) != null)
                    return false;

                // checks original row location.
                if (this.getColor() == 0 && or != 6) // white piece not at row 6 but trying to jump 2
                    return false;
                if (this.getColor() == 1 && or != 1) // black piece not at row 1 but trying to jump 2
                    return false;
            }
            else
            {
                return false;
            }
        }
        else // Pawn is moving diagonally (trying to take piece) or moving incorrectly.
        {
            /*
             *  - player moves one ahead and one to the left
             *      - piece at final has to be opposite color.
             *  - player moves one ahead and one to the right
             *      - piece at final has to be opposite color.
             *  - player moves incorrectly.
             */

            if (!(rowDifference == 1 && colDifference == 1))
                return false; // trying to move diagonally, but doesn't move in 1 and 1, then false
            if (b.pieceAt(fr,fc) == null)
                return false; // piece cannot take an empty tile.
            // taking a same color piece is accounted for below.
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

//        return (!b.ifIMoveAPieceHereIsKingInCheck(or,oc,fr,fc,b.pieceAt(or,oc).getColor(), b));

        return true;
    }

    public boolean move(int r, int c, ChessBoard b)
    {
        int ogR = this.getRow();
        int ogC = this.getCol();

        if (this.canMove(r, c, b))
        {
            // STALEMATE CHECK:
            Chess.turnsSincePawnMovedOrCaptureMade = 0;

            if (b.pieceAt(r, c) != null)
            {
                if (b.pieceAt(ogR, ogC).getColor() == 0) // if white piece is taking black piece
                    System.out.println("White piece " + b.pieceAt(ogR, ogC) + " has taken black piece " + b.pieceAt(r,c) + ".");
                else // if black piece is taking white piece
                    System.out.println("Black piece " + b.pieceAt(ogR, ogC) + " has taken white piece " + b.pieceAt(r,c) + ".");

                // b.changeBoard(r,c,null);
            }

            // If the pawn is moving two rows, its double jumping.
            // This code is used for en passant
            if (Math.abs(ogR - r) == 2)
                hasDoubleJumped = true;

            // special movement for en passant that deletes the other pawn
            if (b.pieceAt(r,c) == null && Math.abs(ogC - c) == 1)
            {
                // deletes the other pawn... hopefully
                b.changeBoard(ogR, c, null);
            }

            // moves the pawn properly
            b.changeBoard(r, c, b.pieceAt(ogR, ogC));
            b.changeBoard(ogR, ogC, null);        

            b.pieceAt(r,c).setRow(r);
            b.pieceAt(r,c).setCol(c);

            //this.canMove(r,c,b); // new as of 1:48 3/21.

            return true;
        }

        return false;
    }

    public boolean returnDoubleJumped() //has double jumped
    {
        return hasDoubleJumped;
    }

    public String toString()
    {
        // 0 is white
        // 1 is black
        if (super.getColor() == 0)
            return "wP";
        else
            return "bP";
    }

    public PieceType getType()
    {
        return PieceType.PAWN;
    }
}