/*
 * Authors: Dylan and Ari
 * King.java
 * This class handles all movement pertaining to the king piece.
 */
public class King extends ChessPiece
{
    boolean inCheck = false;
    private boolean hasMoved = false;

    public King(int initialRow, int initialCol, int pieceColor)
    {
        super(initialRow, initialCol, pieceColor);
    }

    public boolean canMove(int fr, int fc, ChessBoard b)
    {
        int or = this.getRow();
        int oc = this.getCol();

        // System.out.println("second: " + or + "," + oc + "  " + fr + "," + fc);

        /*
         * Unique movement checks:
         *  - Moves exactly one square in each direction. (8 total possible moves max)
         *  - King cannot move into check.
         *      - Checkmate wins the game.
         *          - Happens when King is in check, and it has no valid moves.
         *          - And if no other pieces can remove the King from check.
         *  - Castling, once per game.
         *      - King and Rook cannot have moved previously.
         *      - There is no spaces in between.
         *      - King can't be in check when castling.
         *      - King cannot move into check when castling.
         */
        // TODO: implement castling.
        // This should be the first thing checked for because of how weird it is.

        // if white:
        // if King is at pos(7,4) && hasntMoved && Rook is at pos(7,7) && hasntMoved
        //      or == 7  oc == 4  fr == 7  fc == 6
        // if 7,5 and 7,6 are null
        // if not in Check: 7,4 7,5 7,6

        if (Chess.useFullMoveSet) // only run this code if full game is running...
        {
            int i = 7;
            if (b.pieceAt(or,oc) != null && b.pieceAt(or,oc).getColor() == 1)
                i = 0;

            // CHECKING IF DOING A KINGSIDE CASTLE MOVE
            if (or == i && oc == 4 && fr == i && fc == 6)
            {
                // if King and rook are correct pos and havent moved
                boolean c1 = (b.pieceAt(i,4) != null && b.pieceAt(i,4) instanceof King && !hasMoved);
                boolean c2 = (b.pieceAt(i,7) != null && b.pieceAt(i,7) instanceof Rook && !((Rook) b.pieceAt(i, 7)).returnHasMoved());
                // no pieces between the king and rook
                boolean c3 = (b.pieceAt(i,5) == null && b.pieceAt(i,6) == null);
                setCheck(b);
                boolean c4 = (!returnCheck()); // if in check, false
                boolean c5 = (!b.ifIMoveAPieceHereIsKingInCheck(or,oc,i,5,b.pieceAt(or,oc).getColor(), b));
                boolean c6 = (!b.ifIMoveAPieceHereIsKingInCheck(or,oc,i,6,b.pieceAt(or,oc).getColor(), b));

                if (c1 && c2 && c3 && c4 && c5 && c6)
                    return true;
            }

            // CHECKING FOR QUEENSIDE CASTLE MOVE
            if (or == i && oc == 4 && fr == i && fc == 2)
            {
                // if King and rook are correct pos and havent moved
                boolean c1 = (b.pieceAt(i,4) != null && b.pieceAt(i,4) instanceof King && !hasMoved);
                boolean c2 = (b.pieceAt(i,0) != null && b.pieceAt(i,0) instanceof Rook && !((Rook) b.pieceAt(i, 0)).returnHasMoved());
                // no pieces between the king and rook
                boolean c3 = (b.pieceAt(i,3) == null && b.pieceAt(i,2) == null && b.pieceAt(i,1) == null);
                setCheck(b);
                boolean c4 = (!returnCheck()); // if in check, false
                boolean c5 = (!b.ifIMoveAPieceHereIsKingInCheck(or,oc,i,3,b.pieceAt(or,oc).getColor(), b));
                boolean c6 = (!b.ifIMoveAPieceHereIsKingInCheck(or,oc,i,2,b.pieceAt(or,oc).getColor(), b));

                if (c1 && c2 && c3 && c4 && c5 && c6)
                    return true;
            }
        }

        // King can only move one square in each direction.
        if (!(Math.abs(or-fr) <= 1 && Math.abs(oc-fc) <= 1))
            return false;

        // King cannot move into check.
        // This code cycles through all opposite color pieces to see if they are able to move to where the King wants to go.
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                // if the piece at location x,y is not null
                if (b.pieceAt(x,y) != null)
                {
                    // if the piece has the opposite color and it is not another King.
                    if (b.pieceAt(x,y).getColor() != b.pieceAt(or,oc).getColor() && !(b.pieceAt(x,y) instanceof King))
                    {
                        if (b.pieceAt(x,y).canMove(fr, fc, b)) // if the opposite color piece can move into where the king wants to go.
                        {
                            //System.out.println(b.pieceAt(x,y) + " prevents this move. King cannot move into check. " + "Piece can move to " + x + "," + y);
                            // this is needed because the pawn can move forward, but cannot take straight forward
                            if (!(b.pieceAt(x,y) instanceof Pawn && y == fc))
                                return false;
                        }
                    }
                }
            }
        }

        /*
         * Extension of "King cannot move into check".
         * This exists for the sole purpose of preventing a runtime error.
         * Calling King.canMove() within the testMove() method causes an infinite loop.
         */
        // if theres a King that blocks the move.
        // Opposite color king puts the moving king into check.
        // Finds the king, and determines if the other king is moving into its kill zone
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                if (b.pieceAt(x,y) instanceof King) // if pos xy has a king in it
                {
                    if (b.pieceAt(x,y).getColor() != b.pieceAt(or,oc).getColor()) // if opposite king
                    {
                        if (Math.abs(fr - x) <= 1 && Math.abs(fc - y) <= 1) // if within kill zone
                        {
                            //System.out.println(b.pieceAt(x,y) + " prevents this move. King cannot move into check. Piece can move to " + x + "," + y);
                            return false;
                        }
                    }
                }
            }
        }

        /*
         * Non-unique movement checks.
         *  - Cannot stay in place.
         *  - Cannot move into a square occupied by the same color.
         *  - If in check, it must move out of check.
         *      - TODO: Need to check if the King is in checkmate.
         */
        // stays in place
        if (or == fr && oc == fc)
            return false;

        // cannot take same color.
        if (b.pieceAt(fr,fc) != null) // if not null
            if (b.pieceAt(fr, fc).getColor() == this.getColor()) // cant take piece of same color
                return false;

        // if King is in check and the move doesn't take King out of check
        // TODO: implement this.
        //if (isInCheck())

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

            // move king
            b.changeBoard(r, c, b.pieceAt(ogR, ogC));
            b.changeBoard(ogR, ogC, null);
            b.pieceAt(r,c).setRow(r);
            b.pieceAt(r,c).setCol(c);

            if (c-ogC == 2) // KINGSIDE CASTLE, move rook
            {
                b.changeBoard(r, 5, b.pieceAt(r,7));
                b.changeBoard(r,7,null);
                b.pieceAt(r,5).setRow(r);
                b.pieceAt(r,5).setCol(5);
            }
            else if (ogC-c == 2) // QUEENSIDE CASTLE, move rook
            {
                b.changeBoard(r, 3, b.pieceAt(r,0));
                b.changeBoard(r, 0, null);
                b.pieceAt(r,3).setRow(r);
                b.pieceAt(r,3).setCol(3);
            }

            // im looking at this line 27MAR2020 8:15PM and I don't know why i wrote this
            // probably is a remnant from when we used availableMoves[][]
            // I dont want to get rid of it in case it does something -D
            //this.canMove(r,c,b); // new as of 1:48 3/21.

            // For castling
            hasMoved = true;

            return true;
        }

        return false;
    }

    // used every time a piece is moved.
    // Before the move, to check if the king is in check
    // After the move to check if they put the other king in check
    // note: this method is called using a piece.
    public void setCheck(ChessBoard b)
    {
        int kingRow = this.getRow();
        int kingCol = this.getCol();

        //System.out.println("for testing: " + kingRow + " " + kingCol + " are kings coords");

        // similar to the testMove method where I cycle through the opposite pieces to see if they put King in check.
        // Doesn't have to include King, because the one king can't get close enough to put the other King in check.
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                // if the piece at location x,y is not null
                if (b.pieceAt(x,y) != null)
                {
                    // if the piece has the opposite color and it is not another King.
                    if (b.pieceAt(x,y).getColor() != this.getColor() && !(b.pieceAt(x,y) instanceof King))
                    {
                        if (b.pieceAt(x,y).canMove(kingRow, kingCol, b)) // if King is in check
                        {
                            //System.out.println("[" + this + "] is in check from [" + b.pieceAt(x,y) + "] (" + x + ", " + y + ")");
                            inCheck = true;
                            return;
                        }
                    }
                }
            }
        }

        inCheck = false;
    }

    public boolean returnCheck()
    {
        return inCheck;
    }


    /**
     * Checks the King if it is in checkmate.
     * If it has no valid moves and other pieces can't save it.
     *
     * Assumes there is a checkmate and it is trying to prove it false.
     */
    public boolean checkCheckmate(ChessBoard b)
    {
        // assumes checkmate is true
        boolean returnVal = true;

        /*
         * Cycles through every single spot on the board, and checks to see if the king can move to it.
         * If the king has any available moves that don't leave the king in check, then checkmate is FALSE
         */
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                if (this.canMove(x,y,b) && !b.ifIMoveAPieceHereIsKingInCheck(this.getRow(), this.getCol(), x, y, this.getColor(), b))
                {
                    //System.out.println("SPOILER TAG " + x + y + " is valid move, no checkmate");
                    returnVal = false;
                }
            }
        }

        /*
         * Now the program determines if there are other pieces that can block the way and save the king.
         * Piece has to be the same color as the King.
         * Searches every single spot on the board for the same colored piece.
         * With each piece, it checks all 64 tiles again to see if any of those moves change the outcome of check.
         */
        // TODO: implement this
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                // searches for pieces of the same color as the king in check
                if (b.pieceAt(x,y) != null && !(b.pieceAt(x,y) instanceof King) && b.pieceAt(x,y).getColor() == this.getColor())
                {
                    // LOOPS AGAIN LOL
                    for (int h = 0; h < 8; h++)
                    {
                        for (int k = 0; k < 8; k++) // god i hope this works
                        {
                            if (b.pieceAt(x,y).canMove(h,k,b) && !b.ifIMoveAPieceHereIsKingInCheck(x,y,h,k,this.getColor(),b))
                            {
                                //System.out.println("PogU " + b.pieceAt(x,y) + " @ " + x + "," + y + " can move " + h +"," + k);
                                returnVal = false;
                            }
                        }
                    }
                }
            }
        }

        // Has to be in check and there has no be no valid moves
        return inCheck && returnVal;
    }

    public String toString()
    {
        // 0 is white
        // 1 is black
        if (super.getColor() == 0)
            return "wK";
        else
            return "bK";
    }

    public PieceType getType()
    {
        return PieceType.KING;
    }
}