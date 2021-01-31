package puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath;

import javafx.scene.image.Image;
import puzzlegame.puzzlescreen.factors.Factor;
import puzzlegame.puzzlescreen.factors.Factors;
import puzzlegame.puzzlescreen.puzzletable.Position;
import puzzlegame.puzzlescreen.puzzletable.PuzzlePiece;
import puzzlegame.puzzlescreen.puzzletable.Size;

import java.util.ArrayList;
import java.util.List;

public final class PuzzleMaker {


    private PuzzleMaker(){

    }

    public static List<PuzzlePiece> makePuzzle(Image image, int nbPieces){


        Factor fact = (new Factors(nbPieces)).nearestRatioTo(image.getWidth()/image.getHeight());

        PuzzleShape[][] pieceShapes = new PuzzleShape[fact.getX()][fact.getY()];

        Size size = new Size(image.getWidth()/fact.getX(), image.getHeight()/fact.getY());

        List<PuzzlePiece> pieces = new ArrayList<>();

        for (int i = 0; i < fact.getX(); i++){
            for (int j = 0; j < fact.getY(); j++){
                pieceShapes[i][j] = new PuzzleShape(i, j, fact, size, pieceShapes);
                pieces.add(new PuzzlePiece(image, size, new Position(i, j), pieceShapes[i][j].generatePath()));
            }
        }

        return pieces;
    }


}
