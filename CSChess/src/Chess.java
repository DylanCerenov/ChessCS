/*
 * authors: Dylan & Ari
 * Chess Program
 * Jan 23 2020
 * IMPORTANT: IMPORTANT: IMPORTANT: CHANGE THE MAIN CLASS NAME TO CHESS.JAVA.
 */
import java.util.*;
import java.io.File;

public class Chess
{
    private static ChessBoard screen = new ChessBoard();

    public static int count;
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
    public static void main(String[] args)
    {
        /*
         * This is good code
         * Commented out for convenience.
         */
        /*Scanner scan = new Scanner(System.in);
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
            if (input.equalsIgnoreCase("PLAY"))
            {
                Chess.playChess();
            }
            else if (input.equalsIgnoreCase("TEST"))
            {
                //Chess.testChess(customBoard);

                System.out.println("Enter file name: ");
                String fileName = scan.nextLine();
                System.out.println(">"+fileName);

                ChessBoard board = new ChessBoard(new File(fileName));
                testChess(board);
            }

            System.out.println("[Back in Main Menu]");
            System.out.println("Would you like to PLAY TEST or EXIT?");
            input = scan.nextLine();
            while (!input.equalsIgnoreCase("PLAY") && !input.equalsIgnoreCase("TEST") && !input.equalsIgnoreCase("EXIT")) {
                System.out.println("[ERROR]: Wrong input. Enter PLAY TEST or EXIT");
                input = scan.nextLine();
            }
        }
        System.out.println("Program exited.\nThank you for playing!");*/


        Chess.playChess(); // remove this and test the code above -DYL 20MAR2020 5:23PM
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
        String whitePrompt = "White move (ex: [a2 a4])";
        String blackPrompt = "Black move (ex: [a7 a5])";
        String prompt;

        boolean playing = true;
        count = 0; // 0 white 1 black

        /*
         * This is good code.
         * Commented out for convenience.
         */
        /*System.out.println("===========================================");
        System.out.println("Welcome to Chess!");
        System.out.println("Do you need instructions? (Y/N)");

        String response = scan.nextLine();
        while (!(response.equalsIgnoreCase("Y") || response.equalsIgnoreCase("N") || response.equalsIgnoreCase("QUIT")))
        {
            System.out.println("[error]: input precondition not met.");
            response = scan.nextLine();
        }
        if (response.equalsIgnoreCase("Y"))
        {
            System.out.println("https://en.wikipedia.org/wiki/Chess");
            System.out.println("Proper input is:");
            System.out.println("\trowOrigin+colOrigin rowDestination+colDestination");
            System.out.println("\te.g. a2 a4");
        }
        if (response.equalsIgnoreCase("QUIT"))
            playing = false;
        else
        {
            System.out.println();
            System.out.println("Starting game: ");
            System.out.println("Enter QUIT to exit the program at any time.");
            System.out.println("===========================================");
        }*/

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
            String input1, input2;
            int[] convertedInput = new int[4]; // this exists because we have to convert user input for it to work.

            //********************************
            // Get original input from user.
            //********************************
            System.out.println(prompt);
            input1 = scan.next(); input2 = scan.next();
            // while incorrect input and input is not quit
            while (screen.incorrectSyntax(input1, input2) && !input1.equalsIgnoreCase("quit"))
            {
                System.out.println("[error]: input precondition not met.");
                System.out.println(prompt);
                input1 = scan.next(); input2 = scan.next();
            }
            if (input1.equalsIgnoreCase("quit"))
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
                System.out.println("[error]: move doesn't work.");
                System.out.println(prompt);
                input1 = scan.next(); input2 = scan.next();
                while (screen.incorrectSyntax(input1, input2) && !input1.equalsIgnoreCase("quit")) {
                    System.out.println("[error]: input precondition not met.");
                    System.out.println(prompt);
                    input1 = scan.next();
                    input2 = scan.next();
                    convertedInput = screen.convertInput(input1, input2);
                }
                convertedInput = screen.convertInput(input1, input2);
                if (input1.equalsIgnoreCase("quit"))
                    break;
            }

            //*****************************************************************
            // handle game requirements after Piece moves in the previous step.
            //*****************************************************************
            // Checks to see if King is now in check/checkmate.
            if (Chess.count % 2 == 0) // if white just moved. Check if black king is in check.
            {
                screen.getKing(1).setCheck(screen);
                screen.getKing(0).setCheck(screen); // dunno if this is needed or not

                // check most drastic option first.
                // check if King is in checkmate.
                if (screen.getKing(1).checkCheckmate(screen))
                {
                    // white wins the game.
                    System.out.println(screen);
                    System.out.println("White player wins the game!");
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
                screen.getKing(1).setCheck(screen); // dunno if this is needed or not

                if (screen.getKing(0).checkCheckmate(screen))
                {
                    //black wins the game
                    System.out.println(screen);
                    System.out.println("Black player wins the game!");
                    playing = false;
                    break;
                }
                else if (screen.getKing(0).returnCheck())
                {
                    System.out.println("Black puts White in Check!");
                }
            }



            count++;
            //System.out.println(screen);

            /*
            //********************************
            // get black input
            //********************************
            System.out.println(blackPrompt);
            input1 = scan.next(); input2 = scan.next();
            while (screen.incorrectSyntax(input1, input2))
            {
                System.out.println("[error]: input precondition not met.");
                System.out.println(blackPrompt);
                input1 = scan.next(); input2 = scan.next();
            }

            //********************************
            // do something with the blacks
            //********************************
            convertedInput = screen.convertInput(input1, input2);
            System.out.println("black move: " + convertedInput[0]);
            System.out.print(convertedInput[1]);
            System.out.print(convertedInput[2]);
            System.out.print(convertedInput[3]);

            //*************************************************
            // while input is wrong and the move doesnt work...
            //*************************************************
            while (!screen.move(convertedInput[0], convertedInput[1], convertedInput[2], convertedInput[3]))
            {
                System.out.println("[error]: move doesn't work.");
                System.out.println(blackPrompt);
                input1 = scan.next(); input2 = scan.next();
                while (screen.incorrectSyntax(input1, input2))
                {
                    System.out.println("[error]: input precondition not met.");
                    System.out.println(blackPrompt);
                    input1 = scan.next(); input2 = scan.next();
                    convertedInput = screen.convertInput(input1, input2);
                }
                convertedInput = screen.convertInput(input1, input2);
            }

            //*************************************
            // handle game requirements after black
            //*************************************
            System.out.println(screen);
            */




            // Figure out how to pass a board object in another class, from another class.



            // playing = false;
        }
    }

    /**
     * Runs a normal game of Chess after executing the moves in File moves.
     *
     * @param moves a text file with moves on each line (e.g. a2 a4)
     */
    public static void playChess(File moves)
    {
        //your code
    }

    /**
     * OPTIONAL: Runs a normal game of Chess with all the special move features from the given ChessBoard
     *
     * @param b the initial ChessBoard
     */
    public static void testFullChess(ChessBoard b)
    {

    }

    /**
     * OPTIONAL: Runs a game of Chess with all the special move features
     */
    public static void runFullChess()
    {

    }
}