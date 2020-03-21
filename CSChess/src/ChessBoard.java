import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ChessBoard
{
    private ChessPiece[][] board;

    /**
     * Creates a default 8x8 chessboard with ChessPieces in their default locations. Populates board.
     */
    public ChessBoard()
    {
        board = new ChessPiece[8][8];

        // Fills the board with blanks
        for (int i = 0; i < 8; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                board[i][j] = null;
            }
        }

        // 0 is white, 1 is black

        board[0][0] = new Rook(0,0,1);
        board[0][7] = new Rook(0,7,1);
        board[7][0] = new Rook(7,0,0);
        board[7][7] = new Rook(7,7,0);

        board[0][1] = new Knight(0,1,1);
        board[0][6] = new Knight(0,6,1);
        board[7][1] = new Knight(7,1,0);
        board[7][6] = new Knight(7,6,0);

        board[0][2] = new Bishop(0,2,1);
        board[0][5] = new Bishop(0,5,1);
        board[7][2] = new Bishop(7,2,0);
        board[7][5] = new Bishop(7,5,0);
        // i just chased down a bug for like 30 minutes and it was because the initial row was instantiated incorrectly.

        board[0][4] = new King(0,4,1);
        board[0][3] = new Queen(0,3,1);
        board[7][4] = new King(7,4,0);
        board[7][3] = new Queen(7,3,0);

        for(int i = 0; i < 8; i++)
        {
            board[1][i] = new Pawn(1,i,1);
            board[6][i] = new Pawn(6,i,0);
        }

        // for testing pawns
        /*board[1][3] = new Pawn(1,3,1);
        board[1][4] = new Pawn(1,4,1);
        board[6][3] = new Pawn(6,3,0);
        board[6][4] = new Pawn(6,4,0);

        board[6][1] = new Pawn(6,1,0);
        board[1][1] = new Pawn(1,1,1);

        board[3][3] = new Queen(3,3,1);
        board[4][4] = new Queen(4,4,0);
        board[3][4] = new Queen(3,4,0);
        board[4][3] = new Queen(4,3,1);*/

        // for testing king
        /*board[2][2] = new Queen(2,2,0);
        board[2][3] = new Queen(2,3,0);
        board[2][4] = new Queen(2,4,0);
        board[3][2] = new Queen(3,2,0);
        board[3][5] = new Queen(3,4,0);
        board[4][2] = new Queen(4,2,0);
        board[4][3] = new Queen(4,3,0);
        board[5][5] = new Queen(5,5,0);

        board[3][3] = new King(3,3,1);*/

        // for testing check movement
        /*
        board[0][7] = new King(0,7,1);
        board[0][0] = new Rook(0,0,0);
        board[2][1] = new Rook(2,1,0);
        board[7][3] = new King(7,3,0);
        board[2][5] = new Knight(2,5,1);*/
    }

    /**
     * Creates a board with a custom arrangement of pieces
     *
     * @param f a text file of the board
     */
    public ChessBoard(File f) throws FileNotFoundException
    {
        ArrayList<String> input = new ArrayList<String>();
        try {
            Scanner scan = new Scanner(f);
            int count = 0;
            while (scan.hasNext())
            {
                input.add(scan.next());
            }

            for(int i = 0; i < input.size(); i++)
            {
                int switchColor = 0;
                if(input.get(i).charAt(0) == 'w')
                    switchColor = 1;
                if (input.get(i).charAt(1) == 'P')
                    board[i / 8][i % 8] = new Pawn(i / 8, i % 8, switchColor);
                if (input.get(i).charAt(1) == 'R')
                    board[i / 8][i % 8] = new Rook(i / 8, i % 8, switchColor);
                if (input.get(i).charAt(1) == 'N')
                    board[i / 8][i % 8] = new Knight(i / 8, i % 8, switchColor);
                if (input.get(i).charAt(1) == 'B')
                    board[i / 8][i % 8] = new Bishop(i / 8, i % 8, switchColor);
                if (input.get(i).charAt(1) == 'Q')
                    board[i / 8][i % 8] = new Queen(i / 8, i % 8, switchColor);
                if (input.get(i).charAt(1) == 'K')
                    board[i / 8][i % 8] = new King(i / 8, i % 8, switchColor);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    /**
     * Moves the ChessPiece at location ox, oy to location dx, dy
     *
     * @param or row of origin
     * @param oc column coordinate of origin
     * @param dr row coordinate of destination
     * @param dc column coordinate of destination
     */
    public boolean move(int or, int oc, int dr, int dc)
    {
        /*
         * TODO: Add additional criteria to move
         */
        if (pieceAt(or, oc) == null)
        {
            System.out.println("[error]: initial piece location is null.");
            return false;
        }

        // System.out.println("Main count: " + Main.count);

        // wrong piece color being moved
        // spent 20 minutes figuring out why colors were opposite. Turns out this was the problem.
        // also went on a wild goose chase figuring out that we mixed up the colors in the instantiation of the pieces.
        // white is 0 black is 1
        // I had to fix the toStrings for each method too >:(
        // - Dylan 20MAR2020 3:21PM
        if (Chess.count % 2 == 0 && pieceAt(or,oc).getColor() != 0)
        {
            System.out.println("[ERROR]: White can only move white pieces.");
            return false;
        }
        else if (Chess.count % 2 == 1 && pieceAt(or,oc).getColor() != 1)
        {
            System.out.println("[ERROR]: Black can only move black pieces.");
            return false;
        }

        // Checks if piece can move successfully.
        if (pieceAt(or, oc).canMove(dr, dc, this))
        {
            // if true it moves
            pieceAt(or, oc).move(dr, dc, this); // moves piece

            System.out.println("TESTING: SUCCESSFUL MOVE");

            // checks to see if pawn promotion...
            if (pieceAt(dr,dc) instanceof Pawn)
            {
                // white and is in opposite kings row
                if ((pieceAt(dr,dc).getColor() == 0) && (dr == 0))
                {
                    this.pawnPromote(dr, dc, 0, this);
                }
                else if ((pieceAt(dr,dc).getColor() == 1) && (dr == 7)) // if black and opposite kings row
                {
                    this.pawnPromote(dr, dc, 1, this);
                }
            }

            return true;
        }

        // if move can't be done
        System.out.println(pieceAt(or,oc) + " can NOT move");
        return false;
    }

    /*
     * @param color
     * @return King
     */
    public King getKing(int color)
    {
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                if ((board[x][y]) instanceof King && (board[x][y]).getColor() == color)
                {
//                    System.out.println("ssdsd" + (King) board[x][y]);
//                    System.out.println("(" + x + "," + y + ")");
//                    System.out.println("King is at " + x +"," +y);
                    return (King) board[x][y];
                }
            }
        }
        return null;
    }

    /*
     * Call this method when the King is in check already.
     * Code moves a "dummy" piece temporarily in order to call King.isInCheck() again.
     */
    public boolean ifIMoveAPieceHereDoesItRemoveKingFromCheck(int row, int col, int color, ChessBoard b)
    {
        boolean result = false;
        // I might have to temporarily remove a piece and place it back later.

        if (b.pieceAt(row,col) == null) // no piece to worry about
        {
            // arbitrarily use queen as a body shield for testing b/c I dont have neutral pieces
            board[row][col] = new Queen(row,col,color);

            // get whats needed
            b.getKing(color).setCheck(b);
            result = b.getKing(color).returnCheck();

            // now fix everything to back to where it was
            board[row][col] = null;
            b.getKing(color).setCheck(b);
        }
        else // i have to worry about removing a piece temporarily.
        {
            int tempRow = board[row][col].getRow();
            int tempCol = board[row][col].getCol();
            int tempClr = board[row][col].getColor();

            ChessPiece temp = null;
            if (board[row][col] instanceof Knight)
                temp = new Knight(tempRow, tempCol, tempClr);
            else if (board[row][col] instanceof Rook)
                temp = new Rook(tempRow, tempCol, tempClr);
            else if (board[row][col] instanceof Bishop)
                temp = new Bishop(tempRow, tempCol, tempClr);
            else if (board[row][col] instanceof Queen)
                temp = new Queen(tempRow, tempCol, tempClr);
            else if (board[row][col] instanceof Pawn)
                temp = new Pawn(tempRow, tempCol, tempClr);

            // like above, use queen as a random pick
            board[row][col] = new Queen(row,col,color);

            // get whats needed
            b.getKing(color).setCheck(b);
            result = b.getKing(color).returnCheck();

            // now fix everything to back to where it was
            board[row][col] = null;
            board[row][col] = temp;
            b.getKing(color).setCheck(b);
        }
        return result;
    }

    /*
     * Pawn promotion method
     * Pieces include Knight, Bishop, Rook, or Queen.
     * color 0 white and 1 black
     */
    public void pawnPromote(int row, int col, int color, ChessBoard b)
    {
        Scanner scan = new Scanner(System.in);

        System.out.println("Pawn Promotion! What will you promote it to? (Enter B N R or Q)");
        String resp = scan.nextLine();
        while (!resp.equalsIgnoreCase("B") && !resp.equalsIgnoreCase("N") && !resp.equalsIgnoreCase("R") && !resp.equalsIgnoreCase("Q"))
        {
            System.out.println("[error] wrong input format. (Enter B N R or Q)");
            resp = scan.nextLine();
        }

        board[row][col] = null;
        if (resp.equalsIgnoreCase("B")) // if bishop
            board[row][col] = new Bishop(row,col,color);
        else if (resp.equalsIgnoreCase("N")) // if knight
            board[row][col] = new Knight(row,col,color);
        else if (resp.equalsIgnoreCase("R")) // if rook
            board[row][col] = new Rook(row,col,color);
        else if (resp.equalsIgnoreCase("Q")) // if queen
            board[row][col] = new Queen(row,col,color);
    }

    /**
     *
     * @return The 2D ChessPiece array representation of the ChessBoard
     */
    public ChessPiece[][] getBoard()
    {
        return board;
    }

    /**
     * Used to change the board b/c its a private instance variable
     * @param row
     * @param col
     * @param piece
     */
    public void changeBoard(int row, int col, ChessPiece piece)
    {
        board[row][col] = piece;
    }

    public ChessPiece pieceAt(int row, int col)
    {
        return board[row][col];
    }

    /**
     * Makes sure the input follows correct protocol.
     * @return false if there is an error in the input.
     * @return true if there is no error
     */
    public boolean incorrectSyntax(String s1, String s2)
    {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        // checks length first
        if (s1.length() != 2 || s2.length() != 2) {
            System.out.println("Error: Incorrect input length.");
            return true;
        }

        // checks characters in each string
        // checks chars
        if (!(s1.charAt(0) > 61 && s1.charAt(0) < 'h'+1) || !(s2.charAt(0) > 61 && s2.charAt(0) < 'h'+1)) {
            System.out.println("Error: Invalid characters used.");
            return true;
        }

        // checks number
        if (!(s1.charAt(1) > '0' && s1.charAt(1) < '9') || !(s2.charAt(1) > '0' && s2.charAt(1) < '9')) {
            System.out.println("Error: Invalid number.");
            return true;
        }

        return false;
    }

    /**
     * converts the user input into something usable.
     * @param s1
     * @param s2
     * @return int array with proper values, so the input can be used in methods.
     */
    public int[] convertInput(String s1, String s2)
    {
        /**
         * fcol ABCDEFGH
         * rcol 01234567
         * take unicode value and subtract 34 to get literal int.
         *
         * frow 87654321
         * rrow 01234567
         */
        // user input a2 a4
        // col row || col row

        int[] returnVal = new int[4];
        s1.toLowerCase();
        s2.toLowerCase();

        // System.out.println("s1 test: " + s1);
        // System.out.println("s2 test: " + s2);

        returnVal[0] = Math.abs(8 - ((int) s1.charAt(1) - 48)); // original row
        returnVal[1] = (int) s1.charAt(0) - 97; // original col
        returnVal[2] = Math.abs(8 - ((int) s2.charAt(1) - 48)); // final row
        returnVal[3] = (int) s2.charAt(0) - 97; // final col

        return returnVal;

    }

    // Prints out the board.
    public String toString()
    {
        String str = "\t   _______________________________________\n\t";

        for (int x = 0, j = 8; x < 8; x++, j--)
        {
            str += "" + j;
            for (int y = 0; y < 8; y++)
            {
                str += (" | ");
                if (board[x][y] != null)
                {
                    str += board[x][y];
                }
                else
                {
                    str += "  ";
                }
            }
            if(j != 1)
                str += " |\n\t  |————|————|————|————|————|————|————|————|\n\t";
        }
        str += " |\n\t   ———————————————————————————————————————\n";
        str += "\t    A    B    C    D    E    F    G    H";
        return str;
    }
}