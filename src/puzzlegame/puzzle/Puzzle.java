package puzzlegame.puzzle;

import javafx.scene.image.Image;
import puzzlegame.puzzle.factors.Factor;
import puzzlegame.puzzle.factors.Factors;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class Puzzle {

    private final Image image;
    private final List<PuzzleFragment> pieces;
    private final PuzzleTable onTable;

    public Puzzle(Image image, int numberOfPieces, PuzzleTable puzzleTable){
        this.image = image;
        pieces = new LinkedList<>();
        onTable = puzzleTable;

        Factor fact = (new Factors(numberOfPieces)).nearestRatioTo(image.getWidth()/image.getHeight());

        Size size = new Size(image.getWidth()/fact.getX(), image.getHeight()/fact.getY());

        for (int i = 0; i < fact.getX(); i++){
            for (int j = 0; j < fact.getY(); j++){
                PuzzlePiece piece = new PuzzlePiece(image, size, new Position(i, j));
                if (!piece.isInvisible()) {
                    pieces.add(new PuzzleFragment(piece, this));
                }
            }
        }
    }


    public List<PuzzleFragment> getFragments() {
        return pieces;
    }

    public void checkConnections(PuzzleFragment puzzleFragment) {
        Optional<Delta> delta = Optional.empty();
        for (PuzzleFragment fragment : pieces){
            if (fragment != puzzleFragment){
                delta = fragment.connectsWith(puzzleFragment);
                if (delta.isPresent()){
                    fragment.insertFragment(puzzleFragment);
                    break;
                }
            }
        }
        if (delta.isPresent()){
            pieces.remove(puzzleFragment);
            onTable.remove(puzzleFragment);
        }

    }

    public PuzzleTable getTable() {
        return onTable;
    }
}
