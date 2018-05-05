public class Logic {

    private static int alphaBetaDepth=4;

    public static int getAlphaBetaDepth() {
        return alphaBetaDepth;
    }

    public static String alphaBeta(int depth, int beta, int alpha, String move, int player) {
         String chessBoard[][] = Main.getChessBoard();
        String list=possibleMoves(chessBoard, Main.kingPositionU);
        if (depth==0 || list.length()==0) {return move+(Rating.rating(list.length(), depth, chessBoard, Main.kingPositionU)*(player*2-1));}
        list=sortMoves(list,chessBoard, Main.kingPositionU);
        player=1-player;
        for (int i=0;i<list.length();i+=5) {
            makeMove(list.substring(i,i+5), chessBoard);
            flipBoard(chessBoard);
            String returnString=alphaBeta(depth-1, beta, alpha, list.substring(i,i+5), player);
            int value=Integer.valueOf(returnString.substring(5));
            flipBoard(chessBoard);
            undoMove(list.substring(i,i+5), chessBoard);
            if (player==0) {
                if (value<=beta) {
                    beta = value;
                    if (depth == alphaBetaDepth)
                        move = returnString.substring(0, 5);

                }
            } else {
                if (value>alpha) {alpha=value; if (depth==alphaBetaDepth)
                    move=returnString.substring(0,5);}

            }
            if (alpha>=beta)
                if (player==0) return move+beta; else return move+alpha;

        }
        if (player==0) return move+beta; else return move+alpha;
    }
    public static void flipBoard(String chessBoard[][]) {
        String temp;
        for (int i=0;i<32;i++) {
            int r=i/8, c=i%8;
            if (Character.isUpperCase(chessBoard[r][c].charAt(0))) {
                temp=chessBoard[r][c].toLowerCase();
            } else {
                temp=chessBoard[r][c].toUpperCase();
            }
            if (Character.isUpperCase(chessBoard[7-r][7-c].charAt(0))) {
                chessBoard[r][c]=chessBoard[7-r][7-c].toLowerCase();
            } else {
                chessBoard[r][c]=chessBoard[7-r][7-c].toUpperCase();
            }
            chessBoard[7-r][7-c]=temp;
        }
        int kingTemp= Main.kingPositionU;
        Main.kingPositionU=63- Main.kingPositionL;
        Main.kingPositionL=63-kingTemp;
    }
    public static void makeMove(String move, String chessBoard[][]) {
        int x1,x2,y1,y2;
        x1 = Character.getNumericValue(move.charAt(2));
        y1 = Character.getNumericValue(move.charAt(3));
        x2 = Character.getNumericValue(move.charAt(0));
        y2 = Character.getNumericValue(move.charAt(1));
        if (move.charAt(4)!='P') {
            chessBoard[x1][y1] = chessBoard[x2][y2];
            chessBoard[x2][y2] = " ";
            if (chessBoard[x1][y1].equals("A")) {
                Main.kingPositionU=8*x1+y1;
            }
        } else {
            chessBoard[1][x2] = " ";
            chessBoard[0][y2] = String.valueOf(move.charAt(3));
        }
    }
    public  static void undoMove(String move, String chessBoard[][]) {
        int x1,x2,y1,y2;
        x1 = Character.getNumericValue(move.charAt(2));
        y1 = Character.getNumericValue(move.charAt(3));
        x2 = Character.getNumericValue(move.charAt(0));
        y2 = Character.getNumericValue(move.charAt(1));
        if (move.charAt(4)!='P') {
            chessBoard[x2][y2]=chessBoard[x1][y1];
            chessBoard[x1][y1]=String.valueOf(move.charAt(4));
            if (chessBoard[x2][y2].equals("A")) {
                Main.kingPositionU=8*x2+y2;
            }
        } else {
            //if pawn promotion
            chessBoard[1][x2]="P";
            chessBoard[0][y2]=String.valueOf(move.charAt(2));
        }
    }
    public static String possibleMoves(String chessBoard[][], int kingPos) {
        String list="";
        for (int i=0; i<64; i++) {
            switch (chessBoard[i/8][i%8]) {
                case "P": list+=pawn(i, chessBoard, kingPos);
                    break;
                case "R": list+=rook(i, chessBoard, kingPos);
                    break;
                case "K": list+=knight(i,  chessBoard, kingPos);
                    break;
                case "B": list+=bishop(i,  chessBoard, kingPos);
                    break;
                case "Q": list+=queen(i,  chessBoard, kingPos);
                    break;
                case "A": list+=king(i,  chessBoard, kingPos);
                    break;
            }
        }
        return list;//x1,y1,x2,y2,captured piece
    }
    public String win(String chessBoard[][]) {
        int isKing = 0, isKing2=0;
        for(int i = 0;i<8;i++) {
            for(int j = 0; j<8; j++) {
                if(chessBoard[i][j].equals("A"))
                    isKing++;
                if(chessBoard[i][j].equals("a"))
                    isKing2++;

            }
        }
        if(isKing == 0 && isKing2 != 0)
            return "won";
        else if(isKing != 0 && isKing2  ==0)
            return "loss";
        return "";
    }
    public static String pawn(int i, String chessBoard[][],int  kingPositionU) {
        String list="", oldPiece;
        int r=i/8, c=i%8;
        for (int j=-1; j<=1; j+=2) {
            try {//capture
                if (Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i>=16) {
                    oldPiece=chessBoard[r-1][c+j];
                    chessBoard[r][c]=" ";
                    chessBoard[r-1][c+j]="P";
                    if (isCheck(chessBoard, kingPositionU)) {
                        list=list+r+c+(r-1)+(c+j)+oldPiece;
                    }
                    chessBoard[r][c]="P";
                    chessBoard[r-1][c+j]=oldPiece;
                }
            } catch (Exception e) {}
            try {
                if (Character.isLowerCase(chessBoard[r-1][c+j].charAt(0)) && i<16) {
                    String[] temp={"Q","R","B","K"};
                    for (int k=0; k<4; k++) {
                        oldPiece=chessBoard[r-1][c+j];
                        chessBoard[r][c]=" ";
                        chessBoard[r-1][c+j]=temp[k];
                        if (isCheck(chessBoard, kingPositionU)) {
                            list=list+c+(c+j)+oldPiece+temp[k]+"P";
                        }
                        chessBoard[r][c]="P";
                        chessBoard[r-1][c+j]=oldPiece;
                    }
                }
            } catch (Exception e) {}
        }
        try {
            if (chessBoard[r-1][c].equals("") && i>=16) {
                oldPiece=chessBoard[r-1][c];
                chessBoard[r][c]=" ";
                chessBoard[r-1][c]="P";
                if (isCheck(chessBoard, kingPositionU)) {
                    list=list+r+c+(r-1)+c+oldPiece;
                }
                chessBoard[r][c]="P";
                chessBoard[r-1][c]=oldPiece;
            }
        } catch (Exception e) {}
        try {
            if (" ".equals(chessBoard[r-1][c]) && i<16) {
                String[] temp={"Q","R","B","K"};
                for (int k=0; k<4; k++) {
                    oldPiece=chessBoard[r-1][c];
                    chessBoard[r][c]=" ";
                    chessBoard[r-1][c]=temp[k];
                    if (isCheck(chessBoard, kingPositionU)) {
                        //column1,column2,captured-piece,new-piece,P
                        list=list+c+c+oldPiece+temp[k]+"P";
                    }
                    chessBoard[r][c]="P";
                    chessBoard[r-1][c]=oldPiece;
                }
            }
        } catch (Exception e) {}
        try {
            if (chessBoard[r-1][c].equals(" ") && chessBoard[r-2][c].equals(" ") && i>=48) {
                oldPiece=chessBoard[r-2][c];
                chessBoard[r][c]=" ";
                chessBoard[r-2][c]="P";
                if (isCheck(chessBoard, kingPositionU)) {
                    list=list+r+c+(r-2)+c+oldPiece;
                }
                chessBoard[r][c]="P";
                chessBoard[r-2][c]=oldPiece;
            }
        } catch (Exception e) {}
        return list;
    }
    public static String rook(int i, String chessBoard[][], int kingPositionU) {
        String list="", oldPiece;
        int r=i/8, c=i%8;
        int temp=1;
        for (int j=-1; j<=1; j+=2) {
            try {
                while(chessBoard[r][c+temp*j].equals(" "))
                {
                    oldPiece=chessBoard[r][c+temp*j];
                    chessBoard[r][c]=" ";
                    chessBoard[r][c+temp*j]="R";
                    if (isCheck(chessBoard, kingPositionU)) {
                        list=list+r+c+r+(c+temp*j)+oldPiece;
                    }
                    chessBoard[r][c]="R";
                    chessBoard[r][c+temp*j]=oldPiece;
                    temp++;
                }
                if (Character.isLowerCase(chessBoard[r][c+temp*j].charAt(0))) {
                    oldPiece=chessBoard[r][c+temp*j];
                    chessBoard[r][c]=" ";
                    chessBoard[r][c+temp*j]="R";
                    if (isCheck(chessBoard, kingPositionU)) {
                        list=list+r+c+r+(c+temp*j)+oldPiece;
                    }
                    chessBoard[r][c]="R";
                    chessBoard[r][c+temp*j]=oldPiece;
                }
            } catch (Exception e) {}
            temp=1;
            try {
                while(chessBoard[r+temp*j][c].equals(" "))
                {
                    oldPiece=chessBoard[r+temp*j][c];
                    chessBoard[r][c]=" ";
                    chessBoard[r+temp*j][c]="R";
                    if (isCheck(chessBoard, kingPositionU)) {
                        list=list+r+c+(r+temp*j)+c+oldPiece;
                    }
                    chessBoard[r][c]="R";
                    chessBoard[r+temp*j][c]=oldPiece;
                    temp++;
                }
                if (Character.isLowerCase(chessBoard[r+temp*j][c].charAt(0))) {
                    oldPiece=chessBoard[r+temp*j][c];
                    chessBoard[r][c]=" ";
                    chessBoard[r+temp*j][c]="R";
                    if (isCheck(chessBoard, kingPositionU)) {
                        list=list+r+c+(r+temp*j)+c+oldPiece;
                    }
                    chessBoard[r][c]="R";
                    chessBoard[r+temp*j][c]=oldPiece;
                }
            } catch (Exception e) {}
            temp=1;
        }
        return list;
    }
    public static String knight(int i, String chessBoard[][],int  kingPositionU) {
        String list="", oldPiece;
        int r=i/8, c=i%8;
        for (int j=-1; j<=1; j+=2) {
            for (int k=-1; k<=1; k+=2) {
                try {
                    if (Character.isLowerCase(chessBoard[r+j][c+k*2].charAt(0)) || chessBoard[r+j][c+k*2].equals(" ")) {
                        oldPiece=chessBoard[r+j][c+k*2];
                        chessBoard[r][c]=" ";
                        if (isCheck(chessBoard, kingPositionU)) {
                            list=list+r+c+(r+j)+(c+k*2)+oldPiece;
                        }
                        chessBoard[r][c]="K";
                        chessBoard[r+j][c+k*2]=oldPiece;
                    }
                } catch (Exception e) {}
                try {
                    if (Character.isLowerCase(chessBoard[r+j*2][c+k].charAt(0)) || chessBoard[r+j*2][c+k].equals(" ")) {
                        oldPiece=chessBoard[r+j*2][c+k];
                        chessBoard[r][c]=" ";
                        if (isCheck(chessBoard, kingPositionU)) {
                            list=list+r+c+(r+j*2)+(c+k)+oldPiece;
                        }
                        chessBoard[r][c]="K";
                        chessBoard[r+j*2][c+k]=oldPiece;
                    }
                } catch (Exception e) {}
            }
        }
        return list;
    }
    public static String bishop(int i, String chessBoard[][], int kingPositionU) {
        String list="", oldPiece;
        int r=i/8, c=i%8;
        int temp=1;
        for (int j=-1; j<=1; j+=2) {
            for (int k=-1; k<=1; k+=2) {
                try {
                    while(chessBoard[r+temp*j][c+temp*k].equals(" "))
                    {
                        oldPiece=chessBoard[r+temp*j][c+temp*k];
                        chessBoard[r][c]=" ";
                        chessBoard[r+temp*j][c+temp*k]="B";
                        if (isCheck(chessBoard, kingPositionU)) {
                            list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                        }
                        chessBoard[r][c]="B";
                        chessBoard[r+temp*j][c+temp*k]=oldPiece;
                        temp++;
                    }
                    if (Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))) {
                        oldPiece=chessBoard[r+temp*j][c+temp*k];
                        chessBoard[r][c]=" ";
                        chessBoard[r+temp*j][c+temp*k]="B";
                        if (isCheck(chessBoard, kingPositionU)) {
                            list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                        }
                        chessBoard[r][c]="B";
                        chessBoard[r+temp*j][c+temp*k]=oldPiece;
                    }
                } catch (Exception e) {}
                temp=1;
            }
        }
        return list;
    }
    public static String queen(int i, String chessBoard[][], int kingPositionU) {
        String list="", oldPiece;
        int r=i/8, c=i%8;
        int temp=1;
        for (int j=-1; j<=1; j++) {
            for (int k=-1; k<=1; k++) {
                if (j!=0 || k!=0) {
                    try {
                        while(chessBoard[r+temp*j][c+temp*k].equals(" "))
                        {
                            oldPiece=chessBoard[r+temp*j][c+temp*k];
                            chessBoard[r][c]=" ";
                            chessBoard[r+temp*j][c+temp*k]="Q";
                            if (isCheck(chessBoard, kingPositionU)) {
                                list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                            }
                            chessBoard[r][c]="Q";
                            chessBoard[r+temp*j][c+temp*k]=oldPiece;
                            temp++;
                        }
                        if (Character.isLowerCase(chessBoard[r+temp*j][c+temp*k].charAt(0))) {
                            oldPiece=chessBoard[r+temp*j][c+temp*k];
                            chessBoard[r][c]=" ";
                            chessBoard[r+temp*j][c+temp*k]="Q";
                            if (isCheck(chessBoard, kingPositionU)) {
                                list=list+r+c+(r+temp*j)+(c+temp*k)+oldPiece;
                            }
                            chessBoard[r][c]="Q";
                            chessBoard[r+temp*j][c+temp*k]=oldPiece;
                        }
                    } catch (Exception e) {}
                    temp=1;
                }
            }
        }
        return list;
    }
    public static String king(int i,String chessBoard[][], int kingPositionU) {
        String list="", oldPiece;
        int r=i/8, c=i%8;
        for (int j=0;j<9;j++) {
            if (j!=4) {
                try {
                    if (Character.isLowerCase(chessBoard[r-1+j/3][c-1+j%3].charAt(0)) || " ".equals(chessBoard[r-1+j/3][c-1+j%3])) {
                        oldPiece=chessBoard[r-1+j/3][c-1+j%3];
                        chessBoard[r][c]=" ";
                        chessBoard[r-1+j/3][c-1+j%3]="A";
                        int kingTemp=kingPositionU;
                        kingPositionU=i+(j/3)*8+j%3-9;
                        if (isCheck(chessBoard, kingPositionU)) {
                            list=list+r+c+(r-1+j/3)+(c-1+j%3)+oldPiece;
                        }
                        chessBoard[r][c]="A";
                        chessBoard[r-1+j/3][c-1+j%3]=oldPiece;
                        kingPositionU=kingTemp;
                    }
                } catch (Exception e) {}
            }
        }
        //need to add casting later
        return list;
    }
    public static String sortMoves(String list, String chessBoard[][], int kingPos) {
        int[] score=new int [list.length()/5];
        for (int i=0;i<list.length();i+=5) {
            makeMove(list.substring(i, i+5), chessBoard);
            score[i/5]=-Rating.rating(-1, 0, chessBoard, kingPos);
            undoMove(list.substring(i, i+5), chessBoard);
        }
        String newListA="", newListB=list;
        for (int i=0;i<Math.min(6, list.length()/5);i++) {//first few moves only
            int max=-1000000, maxLocation=0;
            for (int j=0;j<list.length()/5;j++) {
                if (score[j]>max) {max=score[j]; maxLocation=j;}
            }
            score[maxLocation]=-1000000;
            newListA+=list.substring(maxLocation*5,maxLocation*5+5);
            newListB=newListB.replace(list.substring(maxLocation*5,maxLocation*5+5), "");
        }
        return newListA+newListB;
    }
    public static boolean isCheck(String chessBoard[][], int kingPositionU) {
        //bishop/queen
        int temp=1;
        for (int i=-1; i<=1; i+=2) {
            for (int j=-1; j<=1; j+=2) {
                try {
                    while(chessBoard[kingPositionU/8+temp*i][kingPositionU%8+temp*j].equals(" ")) temp++;
                    if (chessBoard[kingPositionU/8+temp*i][kingPositionU%8+temp*j].equals("b") ||
                            chessBoard[kingPositionU/8+temp*i][kingPositionU%8+temp*j].equals("q")) {
                        return false;
                    }
                } catch (Exception e) {}
                temp=1;
            }
        }
        //rook/queen
        for (int i=-1; i<=1; i+=2) {
            try {
                while(chessBoard[kingPositionU/8][kingPositionU%8+temp*i].equals(" ")) {temp++;}
                if (chessBoard[kingPositionU/8][kingPositionU%8+temp*i].equals("r") ||
                        chessBoard[kingPositionU/8][kingPositionU%8+temp*i].equals("q")) {
                    return false;
                }
            } catch (Exception e) {}
            temp=1;
            try {
                while(chessBoard[kingPositionU/8+temp*i][kingPositionU%8].equals(" ")) {temp++;}
                if (chessBoard[kingPositionU/8+temp*i][kingPositionU%8].equals("r") ||
                        chessBoard[kingPositionU/8+temp*i][kingPositionU%8].equals("q")) {
                    return false;
                }
            } catch (Exception e) {}
            temp=1;
        }
        //knight
        for (int i=-1; i<=1; i+=2) {
            for (int j=-1; j<=1; j+=2) {
                try {
                    if (chessBoard[kingPositionU/8+i][kingPositionU%8+j*2].equals("k")) {
                        return false;
                    }
                } catch (Exception e) {}
                try {
                    if (chessBoard[kingPositionU/8+i*2][kingPositionU%8+j].equals("k")) {
                        return false;
                    }
                } catch (Exception e) {}
            }
        }
        //pawn
        if (kingPositionU>=16) {
            try {
                if (chessBoard[kingPositionU/8-1][kingPositionU%8-1].equals("p")) {
                    return false;
                }
            } catch (Exception e) {}
            try {
                if (chessBoard[kingPositionU/8-1][kingPositionU%8+1].equals("p")) {
                    return false;
                }
            } catch (Exception e) {}
            //king
            for (int i=-1; i<=1; i++) {
                for (int j=-1; j<=1; j++) {
                    if (i!=0 || j!=0) {
                        try {
                            if (chessBoard[kingPositionU/8+i][kingPositionU%8+j].equals("a")) {
                                return false;
                            }
                        } catch (Exception e) {}
                    }
                }
            }
        }
        return true;
    }

}
