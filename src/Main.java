import java.util.*;
import javax.swing.*;
  /*
         * PIECE=WHITE/black
         * pawn=P/p
         * kinght (horse)=K/k
         * bishop=B/b
         * rook (castle)=R/r
         * Queen=Q/q
         * King=A/a
         *
         */
public class Main {
    private static Logic logic = new Logic();
    private static String[][] chessBoard={
            {"r","k","b","q","a","b","k","r"},
            {"p","p","p","p","p","p","p","p"},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {"P","P","P","P","P","P","P","P"},
            {"R","K","B","Q","A","B","K","R"}};
    static int kingPositionU, kingPositionL;

    public static void main(String[] args) {
        while (!(chessBoard[kingPositionU/8][kingPositionU%8].equals("A"))) kingPositionU++;
        while (!(chessBoard[kingPositionL/8][kingPositionL%8].equals("a"))) kingPositionL++;


        JFrame frame=new JFrame("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UserInterface ui=new UserInterface();

        frame.add(ui);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setSize(273, 295);
        frame.setVisible(true);


    }

    public static String[][] getChessBoard() {
        return chessBoard;
    }
}