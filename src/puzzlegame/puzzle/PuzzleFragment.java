package puzzlegame.puzzle;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class PuzzleFragment extends Group {

    private final List<PuzzlePiece> pieces;
    private final Puzzle fromPuzzle;

    private double lastMouseX;
    private double lastMouseY;


    public PuzzleFragment(PuzzlePiece puzzlePiece, Puzzle puzzle) {
        pieces = new LinkedList<>();
        pieces.add(puzzlePiece);
        fromPuzzle = puzzle;
        getChildren().add(puzzlePiece);

        setPickOnBounds(false);

        setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                toFront();
                lastMouseX = event.getScreenX();
                lastMouseY = event.getScreenY();
            }
        });

        setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double deltaX = event.getScreenX() - lastMouseX;
                double deltaY = event.getScreenY() - lastMouseY;

                double initialTranslateX = getTranslateX();
                double initialTranslateY = getTranslateY();

                // delta / getParent().getScale() -> so the mouse stays with the group in all zooms
                setTranslateX(initialTranslateX + (deltaX / getParent().getScaleX()));
                setTranslateY(initialTranslateY + (deltaY / getParent().getScaleY()));

                lastMouseX = event.getScreenX();
                lastMouseY = event.getScreenY();


                puzzle.getTable().resizeTable(getBoundsInParent());

                event.consume();
            }
        });

        setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                fromPuzzle.checkConnections(this);
            }
        });

        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY){
                toBack();
            }
        });

    }

    public void insertFragment(PuzzleFragment toInsert){
        PuzzlePiece piece = pieces.get(0);

        Bounds boundsInScene = localToScene(getBoundsInLocal());

        pieces.addAll(toInsert.pieces);
        getChildren().addAll(toInsert.pieces);

        // inserts the pieces in the group in the right positions
        for (PuzzlePiece piece2 : toInsert.pieces){
            double relativeX = piece.getRelativeX(piece2);
            double relativeY = piece.getRelativeY(piece2);
            piece2.setLayoutX(piece.getLayoutX() - relativeX);
            piece2.setLayoutY(piece.getLayoutY() - relativeY);
        }



        // prevents the group from moving in the scene
        Bounds boundsInScene2 = localToScene(getBoundsInLocal());
        setTranslateX(getTranslateX() - ((boundsInScene.getCenterX() - boundsInScene2.getCenterX()) / getParent().getScaleX()));
        setTranslateY(getTranslateY() - ((boundsInScene.getCenterY() - boundsInScene2.getCenterY()) / getParent().getScaleY()));

    }

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

    private boolean acceptable(Delta delta) {
        return (Math.abs(delta.x()) <= 10 && Math.abs(delta.y()) <= 10);
    }
}
