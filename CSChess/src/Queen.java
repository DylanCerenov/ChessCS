/*
 * Authors: Dylan and Ari
 * Queen.java
 * This class handles all movement pertaining to the queen piece.
 */
public class Queen extends ChessPiece
{
    public Queen(int initialRow, int initialCol, int pieceColor)
    {
        super(initialRow, initialCol, pieceColor);
    }

    public boolean canMove(int fr, int fc, ChessBoard b)
    {
        int or = this.getRow();
        int oc = this.getCol();

        // queen can move in every direction
        // determine if it's trying to move as a "rook" or "bishop"

        /*
         * Unique movement checks:
         *  - Can either move as a Rook or a Bishop.
         *  - Tests if the move is valid through recycled code.
         */
        // tryna move as rook
        if (or == fr || oc == fc)
        {
            // recycled code from rook:
            //********************************************************
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
            //********************************************************
        }
        else // moving as a bishop.
        {
            // recycled code from bishop:
            //********************************************************
            if (Math.abs(or-fr) != Math.abs(oc-fc))
                return false;

            for (int i = 1; i <= Math.abs(or - fr)-1; i++)
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
            return "wQ";
        else
            return "bQ";
    }

    public PieceType getType()
    {
        return PieceType.QUEEN;
    }
}