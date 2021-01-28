package puzzlegame.puzzle;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PuzzleFragment {

    private final List<PuzzlePiece> pieces;
    private final Puzzle fromPuzzle;




    public PuzzleFragment(PuzzlePiece puzzlePiece, Puzzle puzzle) {
        super();
        pieces = new LinkedList<>();
        pieces.add(puzzlePiece);
        fromPuzzle = puzzle;
        puzzlePiece.setOwningFragment(this);
    }

    public void insertFragment(PuzzleFragment toInsert, Delta delta){
        pieces.addAll(toInsert.pieces);
        for (PuzzlePiece piece : toInsert.pieces){
            piece.setOwningFragment(this);
            piece.adjust(delta);
        }


    }

    public Optional<Delta> connectsWith(PuzzleFragment puzzleFragment) {
        for (PuzzlePiece piece1 : pieces){
            for (PuzzlePiece piece2 : puzzleFragment.pieces){
                Optional<Delta> delta = piece1.distance(piece2);
System.out.println(delta);
                if (delta.isPresent() && acceptable(delta.get())){
                    return delta;
                }
            }
        }
        return Optional.empty();
    }

    private boolean acceptable(Delta delta) {
        return (Math.abs(delta.x()) <= 10 && Math.abs(delta.y()) <= 10);
    }

    public List<PuzzlePiece> getPieces(){
        return Collections.unmodifiableList(pieces);
    }

    public void checkConnections() {
        fromPuzzle.checkConnections(this);
    }

    public Puzzle getPuzzle() {
        return fromPuzzle;
    }
}
