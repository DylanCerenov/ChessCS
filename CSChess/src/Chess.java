/*
 * Authors: Dylan and Ari
 * Chess.java
 * Chess Program
 * Started on January 23, 2020.
 */
import java.io.FileNotFoundException;
import java.util.*;
import java.io.File;

public class Chess
{
    private static ChessBoard screen = new ChessBoard();
    public static int count;
    public static int turnsSincePawnMovedOrCaptureMade = 0;
    public static boolean useFullMoveSet = false;
    /*
     * Uses Scanner to ask the user what type of game/test they would like to run.
     * Required:
     * 	- playChess(): runs a normal game of Chess without special movement from the beginning
     * 	- testChess(ChessBoard b): runs a normal game of Chess starting with the given ChessBoard
     * 		- Program should prompt user for the name of the text file of the ChessBoard
     * Either runs playChess() or testChess(ChessBoard b) based on user input.
     *
     * Required Game Features:
     * 	- Initial pawn movement option (the ability to move a pawn 2 spaces forward for its first turn)
     * 	- Pawn Promotion:
     * 		- When pawn reaches opposing side, ask user what to promote to and they should enter B, N, R, Q
     * 		- Update board
     * 		- Game continues as normal
     * 	- You cannot make a move that puts your King in check or checkmate
     * 	- Ask if the user wants to return to the main menu to select a game or end the program
     * Optional:
     * 	- All special move features
     *
     * @param args
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("===========================================");
        System.out.println("Chess Project by Dylan & Ari");
        System.out.println("Would you like to PLAY TEST or EXIT?");
        String input = scan.nextLine();
        // Checks for invalid input.
        while (!input.equalsIgnoreCase("PLAY") && !input.equalsIgnoreCase("TEST") && !input.equalsIgnoreCase("EXIT"))
        {
            System.out.println("[ERROR]: Wrong input. Enter PLAY TEST or EXIT");
            input = scan.nextLine();
        }
        // Continuously runs the main method until QUIT is entered.
        while (!input.equalsIgnoreCase("EXIT"))
        {
            // Cheesey one liner fix problem i thonk
            screen = new ChessBoard();

            if (input.equalsIgnoreCase("PLAY"))
            {
                //****************************************************
                // For Ms. Chiu:
                // Swap between the two depending on full game or not.
                Chess.playChess();
                //Chess.runFullChess();
                //****************************************************
            }
            else if (input.equalsIgnoreCase("TEST"))
            {
                System.out.println("Enter file name: (ex: file.txt)");
                File file = new File(new File(scan.nextLine()).getAbsolutePath());
                String resp = "";

                boolean validInput = false;
                while (!file.exists() || resp.equalsIgnoreCase("exit"))
                {
                    System.out.println("[ERROR]: File not found. Retype the name or enter \"exit\".");
                    resp = scan.nextLine();
                    if (resp.equalsIgnoreCase("exit"))
                        break;

                    file = new File(new File(resp).getAbsolutePath());
                }
                if (!resp.equalsIgnoreCase("exit"))
                {
                    ChessBoard board = new ChessBoard(file);
                    //****************************************************
                    // For Ms. Chiu:
                    // Swap between the two depending on full game or not.
                    Chess.testChess(board);
                    //Chess.testFullChess(board);
                    //****************************************************
                }
            }

//            System.out.println("[Back in Main Menu]");
            System.out.println("Would you like to continue using the program? (PLAY TEST or EXIT)");
            input = scan.nextLine();
            while (!input.equalsIgnoreCase("PLAY") && !input.equalsIgnoreCase("TEST") && !input.equalsIgnoreCase("EXIT")) {
                System.out.println("[ERROR]: Wrong input. Enter PLAY TEST or EXIT");
                input = scan.nextLine();
            }
        }
        System.out.println("Program exited.\nThank you for playing! :)");

        //*************************************
        // Testing reading moves from a file...
        //*************************************
//        File file = new File(new File("moveset1.txt").getAbsolutePath());
//        playChess(file);

    }

    /**
     * Runs a normal game of Chess from the given ChessBoard
     *
     * @param b the initial ChessBoard
     */
    public static void testChess(ChessBoard b)
    {
        screen = b;
        playChess();
    }

    /*
     * Starts a default game of Chess from the beginning
     */
    public static void playChess()
    {
        //  fill this in
        //  MAKE SURE THAT ANY TIME WE TAKE ANY INPUT, IF THEY TYPE "QUIT" THEN THE PROGRAM ENDS
        Scanner scan = new Scanner(System.in);
        String whitePrompt = "White move (ex: a2 a4)";
        String blackPrompt = "Black move (ex: a7 a5)";
        String prompt;

        boolean playing = true;
        count = 0; // 0 white 1 black

        System.out.println("===========================================");
        System.out.println("Welcome to Chess!");
        System.out.println("Do you need instructions? (Y/N)");

        String response = scan.nextLine();
        while (!(response.equalsIgnoreCase("Y") || response.equalsIgnoreCase("N") || response.equalsIgnoreCase("QUIT")))
        {
            System.out.println("[ERROR]: Input precondition not met. Enter (Y/N).");
            response = scan.nextLine();
        }
        if (response.equalsIgnoreCase("Y")) // instructions go here
        {
            printInstructions();
        }
        if (response.equalsIgnoreCase("QUIT"))
            playing = false;
        else
        {
            System.out.println();
            System.out.println("Starting game: ");
            System.out.println("Enter QUIT to exit the program at any time.");
            System.out.println("===========================================");
        }

        //System.out.println(screen);
        while (playing)
        {
            System.out.println(screen);
            if (count % 2 == 0)
                prompt = whitePrompt;
            else
                prompt = blackPrompt;

            //********************************
            // resets per turn
            //********************************
            /*
             * id tag 1 21MAR2020 10:36AM
             * Im trying something new here
             * Im going to update everyone's available moves in here all at once.
             * DIDNT WORK 11:29
             */

            String input1, input2;
            int[] convertedInput = new int[4]; // this exists because we have to convert user input for it to work.

            //********************************
            // Get original input from user.
            //********************************
            System.out.println(prompt);
            input1 = scan.next();
            if (input1.equalsIgnoreCase("quit"))
                break;
            input2 = scan.next();
            if (input2.equalsIgnoreCase("quit"))
                break;
            // while incorrect input and input is not quit
            while (screen.incorrectSyntax(input1, input2) && !input1.equalsIgnoreCase("quit"))
            {
                System.out.println("[ERROR]: Input precondition not met.");
                System.out.println(prompt);
                input1 = scan.next();
                if (input1.equalsIgnoreCase("quit"))
                    break;
                input2 = scan.next();
                if (input2.equalsIgnoreCase("quit"))
                    break;
            }
            if (input1.equalsIgnoreCase("quit"))
                break;
            if (input2.equalsIgnoreCase("quit"))
                break;

            //********************************
            // Convert the user's input.
            //********************************
            convertedInput = screen.convertInput(input1, input2);

            /*if (count % 2 == 0) // even turn
                System.out.print("\nwhite move: " + convertedInput[0]);
            else // odd turn
                System.out.print("\nblack move: " + convertedInput[0]);
            System.out.print(convertedInput[1]);
            System.out.print(convertedInput[2]);
            System.out.print(convertedInput[3]);
            System.out.println();*/

            //*************************************************
            // While input is wrong and the move doesn't work
            //*************************************************
            while (!screen.move(convertedInput[0], convertedInput[1], convertedInput[2], convertedInput[3]))
            {
                System.out.println("[ERROR]: Move doesn't work.");
                System.out.println(prompt);
                input1 = scan.next();
                if (input1.equalsIgnoreCase("quit"))
                    break;
                input2 = scan.next();
                if (input2.equalsIgnoreCase("quit"))
                    break;
                while (screen.incorrectSyntax(input1, input2) && !input1.equalsIgnoreCase("quit")) {
                    System.out.println("[ERROR]: Input precondition not met.");
                    System.out.println(prompt);
                    input1 = scan.next();
                    input2 = scan.next();
                    convertedInput = screen.convertInput(input1, input2);
                }
                convertedInput = screen.convertInput(input1, input2);
                if (input1.equalsIgnoreCase("quit"))
                    break;
            }
            if (input1.equalsIgnoreCase("quit"))
                break;
            if (input2.equalsIgnoreCase("quit"))
                break;

            //*****************************************************************
            // handle game requirements after Piece moves in the previous step.
            //*****************************************************************
            /*
             * Checks for stalemates.
             */
            // Checks for two kings left draw.
            int numOfPieces = 0;
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    if (screen.pieceAt(i,j) != null)
                        numOfPieces++;
                }
            }
            if (numOfPieces == 2)
            {
                System.out.println(screen);
                System.out.println("Stalemate. Two kings left.");
                playing = false;
                break;
            }
            // 50 move rule
            if (turnsSincePawnMovedOrCaptureMade >= 50)
            {
                System.out.println(screen);
                System.out.println("Stalemate. 50 move rule.");
                playing = false;
                break;
            }

            // Checks to see if King is now in check/checkmate.
            if (Chess.count % 2 == 0) // if white just moved. Check if black king is in check.
            {
                screen.getKing(1).setCheck(screen);
                //screen.getKing(0).setCheck(screen); // dunno if this is needed or not

                // check most drastic option first.
                // check if King is in checkmate.
                if (screen.getKing(1).checkCheckmate(screen))
                {
                    // white wins the game.
                    System.out.println(screen);
                    System.out.println("Checkmate! White player wins the game!");
                    playing = false;
                    break;
                }
                else if (screen.getKing(1).returnCheck())
                {
                    System.out.println("White puts Black in Check!");
                }
            }
            else // black just moved. Check if white king is in check/checkmate.
            {
                screen.getKing(0).setCheck(screen);
                //screen.getKing(1).setCheck(screen); // dunno if this is needed or not

                if (screen.getKing(0).checkCheckmate(screen))
                {
                    //black wins the game
                    System.out.println(screen);
                    System.out.println("Checkmate! Black player wins the game!");
                    playing = false;
                    break;
                }
                else if (screen.getKing(0).returnCheck())
                {
                    System.out.println("Black puts White in Check!");
                }
            }
            count++;
        }
    }

    /**
     * Runs a normal game of Chess after executing the moves in File moves.
     *
     * @param moves a text file with moves on each line (e.g. a2 a4)
     */
    public static void playChess(File moves) throws FileNotFoundException
    {
        Scanner scan = new Scanner(System.in);
        Scanner scanf = new Scanner(moves);
        String whitePrompt = "White move (ex: a2 a4)";
        String blackPrompt = "Black move (ex: a7 a5)";
        String prompt;
        boolean playing = true;
        count = 0;

        /*
         * Before the user takes control, we put in all the moves.
         */
        while (scanf.hasNext())
        {
            // resets per "turn"
            String read1 = scanf.next();
            String read2 = scanf.next();
            int[] convertedInput = screen.convertInput(read1, read2);

            if (count % 2 == 0) // if white
                System.out.println("Entering in white move [" + read1 + "] to [" + read2 + "].");
            else // then black
                System.out.println("Entering in black move [" + read1 + "] to [" + read2 + "].");

            /*
             * have to flip flop between white and black turns.
             * start with white (0) and go to black (1).
             */
            if (count % 2 == 0) // if white
                screen.move(convertedInput[0], convertedInput[1], convertedInput[2], convertedInput[3]);
            else // then black
                screen.move(convertedInput[0], convertedInput[1], convertedInput[2], convertedInput[3]);

            //*************************************
            // check for checkmates just in case...
            // Checks for stalemates too!
            //*************************************
            // Checks for two kings left draw.
            int numOfPieces = 0;
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    if (screen.pieceAt(i,j) != null)
                        numOfPieces++;
                }
            }
            if (numOfPieces == 2)
            {
                System.out.println(screen);
                System.out.println("Stalemate. Two kings left.");
                playing = false;
                break;
            }
            // 50 move rule
            if (turnsSincePawnMovedOrCaptureMade >= 50)
            {
                System.out.println(screen);
                System.out.println("Stalemate. 50 move rule.");
                playing = false;
                break;
            }

            // checkmates below:
            if (Chess.count % 2 == 0) // if white just moved. Check if black king is in check.
            {
                screen.getKing(1).setCheck(screen);
                if (screen.getKing(1).checkCheckmate(screen))
                {
                    System.out.println(screen);
                    System.out.println("Checkmate! White player wins the game!");
                    playing = false;
                    break;
                }
                else if (screen.getKing(1).returnCheck())
                {
                    System.out.println("White puts Black in Check!");
                }
            }
            else // black just moved. Check if white king is in check/checkmate.
            {
                screen.getKing(0).setCheck(screen);
                if (screen.getKing(0).checkCheckmate(screen))
                {
                    System.out.println(screen);
                    System.out.println("Checkmate! Black player wins the game!");
                    playing = false;
                    break;
                }
                else if (screen.getKing(0).returnCheck())
                {
                    System.out.println("Black puts White in Check!");
                }
            }
            count++;
        }

        /*
         * Actual game begins...
         */
        while (playing)
        {
            System.out.println(screen);
            if (count % 2 == 0)
                prompt = whitePrompt;
            else
                prompt = blackPrompt;

            //********************************
            // resets per turn
            //********************************
            String input1, input2;
            int[] convertedInput = new int[4]; // this exists because we have to convert user input for it to work.

            //********************************
            // Get original input from user.
            //********************************
            System.out.println(prompt);
            input1 = scan.next();
            if (input1.equalsIgnoreCase("quit"))
                break;
            input2 = scan.next();
            if (input2.equalsIgnoreCase("quit"))
                break;
            // while incorrect input and input is not quit
            while (screen.incorrectSyntax(input1, input2) && !input1.equalsIgnoreCase("quit"))
            {
                System.out.println("[ERROR]: Input precondition not met.");
                System.out.println(prompt);
                input1 = scan.next();
                if (input1.equalsIgnoreCase("quit"))
                    break;
                input2 = scan.next();
                if (input2.equalsIgnoreCase("quit"))
                    break;
            }
            if (input1.equalsIgnoreCase("quit"))
                break;
            if (input2.equalsIgnoreCase("quit"))
                break;

            //********************************
            // Convert the user's input.
            //********************************
            convertedInput = screen.convertInput(input1, input2);

            //*************************************************
            // While input is wrong and the move doesn't work
            //*************************************************
            while (!screen.move(convertedInput[0], convertedInput[1], convertedInput[2], convertedInput[3]))
            {
                System.out.println("[ERROR]: Move doesn't work.");
                System.out.println(prompt);
                input1 = scan.next();
                if (input1.equalsIgnoreCase("quit"))
                    break;
                input2 = scan.next();
                if (input2.equalsIgnoreCase("quit"))
                    break;
                while (screen.incorrectSyntax(input1, input2) && !input1.equalsIgnoreCase("quit")) {
                    System.out.println("[ERROR]: Input precondition not met.");
                    System.out.println(prompt);
                    input1 = scan.next();
                    input2 = scan.next();
                    convertedInput = screen.convertInput(input1, input2);
                }
                convertedInput = screen.convertInput(input1, input2);
                if (input1.equalsIgnoreCase("quit"))
                    break;
            }
            if (input1.equalsIgnoreCase("quit"))
                break;
            if (input2.equalsIgnoreCase("quit"))
                break;

            //*****************************************************************
            // handle game requirements after Piece moves in the previous step.
            //*****************************************************************
            /*
             * Checks for stalemates.
             */
            // Checks for two kings left draw.
            int numOfPieces = 0;
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    if (screen.pieceAt(i,j) != null)
                        numOfPieces++;
                }
            }
            if (numOfPieces == 2)
            {
                System.out.println(screen);
                System.out.println("Stalemate. Two kings left.");
                playing = false;
                break;
            }
            // 50 move rule
            if (turnsSincePawnMovedOrCaptureMade >= 50)
            {
                System.out.println(screen);
                System.out.println("Stalemate. 50 move rule.");
                playing = false;
                break;
            }

            // Checks to see if King is now in check/checkmate.
            if (Chess.count % 2 == 0) // if white just moved. Check if black king is in check.
            {
                screen.getKing(1).setCheck(screen);
                //screen.getKing(0).setCheck(screen); // dunno if this is needed or not

                // check most drastic option first.
                // check if King is in checkmate.
                if (screen.getKing(1).checkCheckmate(screen))
                {
                    // white wins the game.
                    System.out.println(screen);
                    System.out.println("Checkmate! White player wins the game!");
                    playing = false;
                    break;
                }
                else if (screen.getKing(1).returnCheck())
                {
                    System.out.println("White puts Black in Check!");
                }
            }
            else // black just moved. Check if white king is in check/checkmate.
            {
                screen.getKing(0).setCheck(screen);
                //screen.getKing(1).setCheck(screen); // dunno if this is needed or not

                if (screen.getKing(0).checkCheckmate(screen))
                {
                    //black wins the game
                    System.out.println(screen);
                    System.out.println("Checkmate! Black player wins the game!");
                    playing = false;
                    break;
                }
                else if (screen.getKing(0).returnCheck())
                {
                    System.out.println("Black puts White in Check!");
                }
            }
            count++;
        }
    }

    /**
     * OPTIONAL: Runs a normal game of Chess with all the special move features from the given ChessBoard
     *
     * @param b the initial ChessBoard
     */
    public static void testFullChess(ChessBoard b)
    {
        screen = b;
        runFullChess();
    }

    /**
     * OPTIONAL: Runs a game of Chess with all the special move features
     */
    public static void runFullChess()
    {
        useFullMoveSet = true;
        playChess();
        useFullMoveSet = false;
    }

    /**
     * This is for printing the instructions
     */
    private static void printInstructions()
    {                       
        System.out.println("\nCHESS RULES:\n------------");
        System.out.println("- Chess is a two-player board game.");
        System.out.println("- There is a white player and black player.");
        System.out.println("- White moves first.");
        System.out.println("- Each player has 16 pieces");
        System.out.println("- There are 6 types of pieces, each moving differently.");
        System.out.println("\t - Pawn: moves exactly one square horizontally, vertically, or diagonally.");
        System.out.println("\t - Knight: moves to the nearest square not on the same rank, file, or diagonal.");
        System.out.println("\t - Rook: moves any number of vacant squares horizontally or vertically.");
        System.out.println("\t - Bishop: moves any number of vacant squares diagonally.");
        System.out.println("\t - Queen: moves any number of vacant squares horizontally, vertically, or diagonally.");
        System.out.println("\t - King: moves exactly one square horizontally, vertically, or diagonally.");
        System.out.println("- Each player can capture the other player's pieces.");
        System.out.println("- The game ends when the other King is in checkmate.");
        System.out.println("- The game can also end with a draw under certain conditions.");
        System.out.println("- For any gaps in knowledge, consult:");
        System.out.println("\t - https://en.wikipedia.org/wiki/Rules_of_chess\n");
                            
        System.out.println("PROGRAM INSTRUCTIONS:\n---------------------");
        System.out.println("- The turns automatically move from white to black and vice versa.");
        System.out.println("- To move pieces, the proper input is:");
        System.out.println("\t - rowOrigin+colOrigin rowDestination+colDestination");
        System.out.println("\t - e.g. a2 a4");
    }
}