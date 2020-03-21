// 12MAR2020 9:24PM STATUS: incomplete at the moment due to the in-check movement and the checkmate method.
public class King extends ChessPiece
{
    boolean inCheck = false;

    public King(int initialRow, int initialCol, int pieceColor)
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

    /**
     * Tries to prove false until true.
     * Checks for all the things that will prevent it from working.
     */
    public boolean testMove(int or, int oc, int fr, int fc, ChessBoard b)
    {
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
                        if ((b.pieceAt(x,y)).canMove(fr,fc,b)) // if the opposite color piece can move into where the king wants to go.
                        {
                            System.out.println(b.pieceAt(x,y) + " prevents this move. King cannot move into check. Piece can move to " + x + "," + y);
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
                            System.out.println(b.pieceAt(x,y) + " prevents this move. King cannot move into check. Piece can move to " + x + "," + y);
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
        //{

        //}

        //System.out.println(returnCheck());

        return true;
    }

    // used every time a piece is moved.
    // Before the move, to check if the king is in check
    // After the move to check if they put the other king in check
    // note: this method is called using a piece.
    public void setCheck(ChessBoard b)
    {
        int kingRow = this.getRow();
        int kingCol = this.getCol();

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
                        if ((b.pieceAt(x,y)).canMove(kingRow,kingCol,b)) // if King is in check
                        {
                            System.out.println("[" + this + "] is in check from [" + b.pieceAt(x,y) + "] (" + x + ", " + y + ")");
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
        // Check to see if there are no valid moves.
        // Cycles through the entire boolean array.
        super.availableMoves = this.getAvailableMoves(b); // updates the array just in case.
        // -DYL 20MAR2020 7:21PM removing the line above made the code work. I hope this stays that way
        // -DYL 20MAR2020 8:59PM past dylan is an idiot.
        //      This line is needed in order to update the King's moves one last time. I learned this during the knight save scenario.
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (getAM(x,y))
                {
                    System.out.println("distinctive tag " + x + y + " is valid move");
                    return false;
                }
            }
        }

        // Now it tries to see if there are other pieces that can save it.
        // Moving piece has to be of the same color as King.
        // I have to identify what piece is putting the king into chess (and if there are multiple) and see if there
        //      is a move that blocks the King from check.
        // TODO: implement this
//        for (int x = 0; x < 8; x++)
//        {
//            for (int y = 0; y < 8; y++)
//            {
//
//            }
//        }
        if (inCheck)
            return true;
        return false;
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