package puzzlegame.puzzlescreen.puzzletable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * groups different pieces that where attached together
 */
public class PuzzleFragment {

    /**
     * The list of pieces contained by this fragment
     */
    private final List<PuzzlePiece> pieces;

    /**
     * the puzzle this fragment is part of
     */
    private final Puzzle fromPuzzle;


    public PuzzleFragment(PuzzlePiece puzzlePiece, Puzzle puzzle) {
        super();
        pieces = new LinkedList<>();
        pieces.add(puzzlePiece);
        fromPuzzle = puzzle;
        puzzlePiece.setOwningFragment(this);
    }

    /**
     * Inserts the given fragment into this fragment
     * @param toInsert the fragment to insert
     * @param delta    the difference of distance between the perfect position and the real position
     */
    public void insertFragment(PuzzleFragment toInsert, Delta delta){
        pieces.addAll(toInsert.pieces);
        for (PuzzlePiece piece : toInsert.pieces){
            piece.setOwningFragment(this);
            piece.adjust(delta);
        }
    }

    /**
     * Returns a delta if the fragments can be connected together and that they are close enough
     * @param puzzleFragment
     * @return
     */
    public Optional<Delta> connectsWith(PuzzleFragment puzzleFragment) {
        for (PuzzlePiece piece1 : pieces){
            for (PuzzlePiece piece2 : puzzleFragment.pieces){
                Optional<Delta> delta = piece1.distance(piece2);
                if (delta.isPresent() && acceptable(delta.get())){
                    return delta;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * whether the given delta is small enough to consider connecting
     * @param delta the delta to verify
     * @return if the gap is acceptable
     */
    private boolean acceptable(Delta delta) {
        return (Math.abs(delta.x()) <= 10 && Math.abs(delta.y()) <= 10);
    }

    /**
     * @return the list of pieces in this fragment (unmodifiable list)
     */
    public List<PuzzlePiece> getPieces(){
        return Collections.unmodifiableList(pieces);
    }

    /**
     * Checks possible connection between this fragment and the others
     */
    public void checkConnections() {
        fromPuzzle.checkConnections(this);
    }

    /**
     * @return The full puzzle
     */
    public Puzzle getPuzzle() {
        return fromPuzzle;
    }
}