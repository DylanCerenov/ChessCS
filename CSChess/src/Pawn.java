// 12MAR2020 9:24PM STATUS: incomplete at the moment.
public class Pawn extends ChessPiece
{
    public Pawn(int initialRow, int initialCol, int pieceColor)
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

    //plus one is down
    //plus one is right
    /*
     * Tries to prove false until true.
     */
    public boolean testMove(int or, int oc, int fr, int fc, ChessBoard b)
    {
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

        int rowDifference = Math.abs(or-fr);
        int colDifference = Math.abs(oc-fc);

        // Pawns can only move forward.
        int directionOfMovement = fr-or; // + is for black - is for white
        if (directionOfMovement < 0 && b.pieceAt(or,oc).getColor() == 1) // if a black piece is moving backwards
            return false;
        if (directionOfMovement > 0 && b.pieceAt(or,oc).getColor() == 0) // if a white piece is moving backwards
            return false;

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
                if (b.pieceAt(or,oc).getColor() == 1) // black piece moving, moves + direction
                    tempDirection = 1;

                // checks to see if there are pieces in the way
                if (b.pieceAt(or + tempDirection, oc) != null)
                    return false;
                if (b.pieceAt(or + (2*tempDirection), oc) != null)
                    return false;

                // checks original row location.
                if (b.pieceAt(or,oc).getColor() == 0 && or != 6) // white piece not at row 6 but trying to jump 2
                    return false;
                if (b.pieceAt(or,oc).getColor() == 1 && or != 1) // black piece not at row 1 but trying to jump 2
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

        // cannot move if King is in check.
        // TODO: come back to this after King is complete.*/
        if (b.getKing(b.pieceAt(or,oc).getColor()).returnCheck()) // new installment 20MAR2020 8:23PM
            return b.ifIMoveAPieceHereDoesItRemoveKingFromCheck(fr, fc, b.pieceAt(or,oc).getColor(), b);

        return true;
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