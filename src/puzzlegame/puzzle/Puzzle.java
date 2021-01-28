package puzzlegame.puzzle;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import puzzlegame.puzzle.factors.Factor;
import puzzlegame.puzzle.factors.Factors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Puzzle {

    private final ObservableList<PuzzleFragment> fragments;
    private final List<PuzzlePiece> pieces;
    private final PuzzleTable onTable;
    private final BooleanBinding finished;

    public Puzzle(Image image, int numberOfPieces, PuzzleTable puzzleTable){
        fragments = FXCollections.observableArrayList();
        pieces = new ArrayList<>();
        onTable = puzzleTable;

        finished = new FinishedPuzzle(fragments);

        Factor fact = (new Factors(numberOfPieces)).nearestRatioTo(image.getWidth()/image.getHeight());

        Size size = new Size(image.getWidth()/fact.getX(), image.getHeight()/fact.getY());

        for (int i = 0; i < fact.getX(); i++){
            for (int j = 0; j < fact.getY(); j++){
                PuzzlePiece piece = new PuzzlePiece(image, size, new Position(i, j));
                if (!piece.isInvisible()) {
                    pieces.add(piece);
                    fragments.add(new PuzzleFragment(piece, this));
                }
            }
        }
    }


    public List<PuzzlePiece> getPieces() {
        return pieces;
    }

    public void checkConnections(PuzzleFragment puzzleFragment) {
        Optional<Delta> delta = Optional.empty();
        for (PuzzleFragment fragment : fragments){
            if (fragment != puzzleFragment){
                delta = fragment.connectsWith(puzzleFragment);
                System.out.println(delta);
                if (delta.isPresent()){
                    fragment.insertFragment(puzzleFragment, delta.get());
                    break;
                }
            }
        }
        if (delta.isPresent()){
            fragments.remove(puzzleFragment);
        }

    }

    public PuzzleTable getTable() {
        return onTable;
    }

    public BooleanBinding finished() {
        return finished;
    }

    private static class FinishedPuzzle extends BooleanBinding{

        private final ObservableList<PuzzleFragment> puzzle;

        public FinishedPuzzle(ObservableList<PuzzleFragment> puzzle){
            bind(puzzle);
            this.puzzle = puzzle;
        }


        @Override
        protected boolean computeValue() {
            return puzzle.size() <= 1;
        }
    }
}
