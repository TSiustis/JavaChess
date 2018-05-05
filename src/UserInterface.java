
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class UserInterface extends JPanel implements MouseListener, MouseMotionListener {
    private static int mouseX, mouseY, newMouseX, newMouseY;
    static int squareSize=32;
    private static Logic logic = new Logic();

   static String[][] chessBoard = Main.getChessBoard();
  @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
		this.setBackground(Color.yellow);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		for (int i=0;i<64;i+=2) {
        g.setColor(new Color(245, 255, 225));
        g.fillRect((i%8+(i/8)%2)*squareSize, (i/8)*squareSize, squareSize, squareSize);
        g.setColor(new Color(143, 153, 161));
        g.fillRect(((i+1)%8-((i+1)/8)%2)*squareSize, ((i+1)/8)*squareSize, squareSize, squareSize);
    }

    Image chessPiecesImage;
    chessPiecesImage=new ImageIcon("ChessPieces.png").getImage();
		for (int i=0;i<64;i++) {
        int j=-1,k=-1;
        switch (chessBoard[i/8][i%8]) {
            case "P": j=5; k=0;
                break;
            case "p": j=5; k=1;
                break;
            case "R": j=2; k=0;
                break;
            case "r": j=2; k=1;
                break;
            case "K": j=4; k=0;
                break;
            case "k": j=4; k=1;
                break;
            case "B": j=3; k=0;
                break;
            case "b": j=3; k=1;
                break;
            case "Q": j=1; k=0;
                break;
            case "q": j=1; k=1;
                break;
            case "A": j=0; k=0;
                break;
            case "a": j=0; k=1;
                break;
        }
        if (j!=-1 && k!=-1) {
            g.drawImage(chessPiecesImage, (i%8)*squareSize, (i/8)*squareSize, (i%8+1)*squareSize, (i/8+1)*squareSize, j*64, k*64, (j+1)*64, (k+1)*64, this);
        }
           if(logic.win(chessBoard).equals("won")) {
                JOptionPane.showMessageDialog(this,
                        "You won!",
                        "Warning",
                        JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }

    }

}
    @Override
    public void mouseMoved(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX()<8*squareSize &&e.getY()<8*squareSize) {
            mouseX=e.getX();
            mouseY=e.getY();
            repaint();
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        String[][] chessBoard =Main.getChessBoard();
        if (e.getX()<8*squareSize &&e.getY()<8*squareSize) {
            newMouseX=e.getX();
            newMouseY=e.getY();
            if (e.getButton()==MouseEvent.BUTTON1) {
                String dragMove;
                if (newMouseY/squareSize==0 && mouseY/squareSize==1 && "P".equals(chessBoard[mouseY/squareSize][mouseX/squareSize])) {
                    dragMove=""+mouseX/squareSize+newMouseX/squareSize+chessBoard[newMouseY/squareSize][newMouseX/squareSize]+"QP";
                } else {
                    dragMove=""+mouseY/squareSize+mouseX/squareSize+newMouseY/squareSize+newMouseX/squareSize+chessBoard[newMouseY/squareSize][newMouseX/squareSize];
                }
                String userPossibilities=logic.possibleMoves(chessBoard, Main.kingPositionU);
                if (userPossibilities.replaceAll(dragMove, "").length()<userPossibilities.length()) {
                    //if valid move
                    logic.makeMove(dragMove, chessBoard);
                    logic.flipBoard(chessBoard);
                    logic.makeMove(logic.alphaBeta(logic.getAlphaBetaDepth(), 1000000, -1000000, "", 0),chessBoard );
                    logic.flipBoard(chessBoard);
                    repaint();
                }
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseDragged(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}