package puzzlegame.puzzlescreen.puzzletable;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath.PuzzleMaker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Represents the puzzle
 * contains informations like the its pieces, the formed fragments, its finished state and a link to the table its on
 */
public class Puzzle {

    /**
     * list of formed fragments
     */
    private final ObservableList<PuzzleFragment> fragments;

    /**
     * List of the puzzle's pieces
     * Should be unmodifiable
     */
    private final List<PuzzlePiece> pieces;

    /**
     * link to the table it is on
     */
    private final PuzzleTable onTable;

    /**
     * State of completion
     */
    private final BooleanBinding finished;

    /**
     * Creates the puzzle from the given image
     * @param image The image of the puzzle
     * @param numberOfPieces The number of pieces of the puzzle
     * @param puzzleScene The table it will be on
     */
    public Puzzle(Image image, int numberOfPieces, PuzzleTable puzzleScene){
        fragments = FXCollections.observableArrayList();
        var piecesTemp = new ArrayList<PuzzlePiece>();
        onTable = puzzleScene;

        finished = new FinishedPuzzle(fragments);

        var cutPieces = PuzzleMaker.makePuzzle(image, numberOfPieces);

        for (PuzzlePiece piece : cutPieces){
            if (!piece.isInvisible()) {
                    piecesTemp.add(piece);
                    fragments.add(new PuzzleFragment(piece, this));
                }
        }
        pieces = Collections.unmodifiableList(piecesTemp);

    }

    /**
     * @return the list of the puzzle pieces (should be unmodifiable)
     */
    public List<PuzzlePiece> getPieces() {
        return pieces;
    }

    /**
     * Verifies if the given fragment can be connected to a fragment in the puzzle
     * @param puzzleFragment The fragment to connect
     */
    public boolean checkConnections(PuzzleFragment puzzleFragment) {
        Optional<Delta> delta = Optional.empty();
        for (PuzzleFragment fragment : fragments){
            if (fragment != puzzleFragment){
                delta = fragment.connectsWith(puzzleFragment);
                if (delta.isPresent()){
                    fragment.insertFragment(puzzleFragment, delta.get());
                    break;
                }
            }
        }
        if (delta.isPresent()){
            fragments.remove(puzzleFragment);
        }
        return delta.isPresent();
    }

    /**
     * @return The table the puzzle is on
     */
    public PuzzleTable getTable() {
        return onTable;
    }

    /**
     * @return a property for the completion of the puzzle
     */
    public BooleanBinding finished() {
        return finished;
    }

    /**
     * A binding to give the completion state of the puzzle
     */
    private static class FinishedPuzzle extends BooleanBinding{

        private final ObservableList<PuzzleFragment> puzzle;

        public FinishedPuzzle(ObservableList<PuzzleFragment> puzzle){
            bind(puzzle);
            this.puzzle = puzzle;
        }


        @Override
        protected boolean computeValue() {
            // considers 0 as complete for an empty puzzle, should normally be 1 though
            return puzzle.size() <= 1;
        }
    }
}