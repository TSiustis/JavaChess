
public class Rating {
    private static Logic logic = new Logic();
    private static int pawnBoard[][]={//attribute to http://chessprogramming.wikispaces.com/Simplified+evaluation+function
            { 0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            { 5,  5, 10, 25, 25, 10,  5,  5},
            { 0,  0,  0, 20, 20,  0,  0,  0},
            { 5, -5,-10,  0,  0,-10, -5,  5},
            { 5, 10, 10,-20,-20, 10, 10,  5},
            { 0,  0,  0,  0,  0,  0,  0,  0}};
   private static int rookBoard[][]={
            { 0,  0,  0,  0,  0,  0,  0,  0},
            { 5, 10, 10, 10, 10, 10, 10,  5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            { 0,  0,  0,  5,  5,  0,  0,  0}};
   private static int knightBoard[][]={
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50}};
    private static int bishopBoard[][]={
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20}};
   private static int queenBoard[][]={
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            { -5,  0,  5,  5,  5,  5,  0, -5},
            {  0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}};
    private static int kingMidBoard[][]={
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            { 20, 20,  0,  0,  0,  0, 20, 20},
            { 20, 30, 10,  0,  0, 10, 30, 20}};
   private static int kingEndBoard[][]={
            {-50,-40,-30,-20,-20,-30,-40,-50},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50}};
    public static int rating(int moves, int depth, String[][] chessBoard, int kingPos) {
        int counter=0, material=rateMaterial(chessBoard);
        counter+=rateAttack(chessBoard, kingPos);
        counter+=material;
        counter+=rateMove(moves, depth, material,chessBoard);
        counter+=ratePosition(material,chessBoard,Main.kingPositionU);
        logic.flipBoard(chessBoard);
        material=rateMaterial(chessBoard);
        counter-=rateAttack(chessBoard, kingPos);
        counter-=material;
        counter-=rateMove(moves, depth, material,chessBoard);
        counter-=ratePosition(material,chessBoard,Main.kingPositionU);
        logic.flipBoard(chessBoard);
        return -(counter+depth*50);
    }
    public static int rateAttack(String[][] chessBoard, int kingPos) {
        int counter=0;
        int tempPositionC=kingPos;
        for (int i=0;i<64;i++) {
            switch (chessBoard[i/8][i%8]) {
                case "P": {kingPos=i; if (!logic.isCheck(chessBoard, tempPositionC)) {counter-=64;}}
                break;
                case "R": {kingPos=i; if (!logic.isCheck(chessBoard, tempPositionC)) {counter-=500;}}
                break;
                case "K": {kingPos=i; if (!logic.isCheck(chessBoard, tempPositionC)) {counter-=300;}}
                break;
                case "B": {kingPos=i; if (!logic.isCheck(chessBoard, tempPositionC)) {counter-=300;}}
                break;
                case "Q": {kingPos=i; if (!logic.isCheck(chessBoard, tempPositionC)) {counter-=900;}}
                break;
            }
        }
        kingPos=tempPositionC;
        if (!logic.isCheck(chessBoard, kingPos)) {counter-=200;}
        return counter/2;
    }
    public static int rateMaterial(String[][] chessBoard) {
        int counter=0, bishopCounter=0;
        for (int i=0;i<64;i++) {
            switch (chessBoard[i/8][i%8]) {
                case "P": counter+=100;
                    break;
                case "R": counter+=500;
                    break;
                case "K": counter+=300;
                    break;
                case "B": bishopCounter+=1;
                    break;
                case "Q": counter+=900;
                    break;
            }
        }
        if (bishopCounter>=2) {
            counter+=300*bishopCounter;
        } else {
            if (bishopCounter==1) {counter+=250;}
        }
        return counter;
    }
    public static int rateMove(int listLength, int depth, int material, String[][] chessBoard) {
        int counter=0;
        counter+=listLength;//5 pointer per valid move
        if (listLength==0) {//current side is in checkmate or stalemate
            if (!logic.isCheck(chessBoard, Main.kingPositionU)) {//if checkmate
                counter+=-200000*depth;
            } else {//if stalemate
                counter+=-150000*depth;
            }
        }
        return 0;
    }
    public static int ratePosition(int material,String[][] chessBoard,int kingPos) {
        int counter=0;
        for (int i=0;i<64;i++) {
            switch (chessBoard[i/8][i%8]) {
                case "P": counter+=pawnBoard[i/8][i%8];
                    break;
                case "R": counter+=rookBoard[i/8][i%8];
                    break;
                case "K": counter+=knightBoard[i/8][i%8];
                    break;
                case "B": counter+=bishopBoard[i/8][i%8];
                    break;
                case "Q": counter+=queenBoard[i/8][i%8];
                    break;
                case "A": if (material>=1750) {counter+=kingMidBoard[i/8][i%8]; counter+=logic.king(i,chessBoard,kingPos).length()*10;} else
                {counter+=kingEndBoard[i/8][i%8]; counter+=logic.king(i,chessBoard,kingPos).length()*30;}
                    break;
            }
        }
        return counter;
    }
}