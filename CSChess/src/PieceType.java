public enum PieceType
{
    KING, QUEEN, ROOK, KNIGHT, BISHOP, PAWN;

    public String toString()
    {
        if(this == KING)
        {
            return "KING";
        }
        else if(this == QUEEN)
        {
            return "QUEEN";
        }
        else if(this == ROOK)
        {
            return "ROOK";
        }
        else if(this == KNIGHT)
        {
            return "KING";
        }
        else if(this == BISHOP)
        {
            return "BISHOP";
        }
        else
        {
            return "PAWN";
        }
    }
}