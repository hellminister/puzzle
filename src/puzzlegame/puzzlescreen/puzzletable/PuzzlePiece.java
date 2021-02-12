package puzzlegame.puzzlescreen.puzzletable;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.util.Optional;

/**
 * A puzzle piece
 */
public class PuzzlePiece extends StackPane {

    /**
     * the position of the piece in the puzzle
     */
    private final Position position;

    /**
     * The size of the piece
     */
    private final Size size;

    /**
     * whether the piece is fully transparent (ie non existing)
     */
    private final boolean invisible;

    /**
     * The group of attached pieces this piece is part of
     */
    private PuzzleFragment inFragment;

    /**
     * The shape of the piece
     */
    private final SVGPath pieceShape;

    /**
     * The X position correction of the clip (piece shape)
     * also the part where the shape can exceed to in size
     */
    private final DoubleProperty clipXCorrection;

    /**
     * The Y position correction of the clip (piece shape)
     * also the part where the shape can exceed to in size
     */
    private final DoubleProperty clipYCorrection;

    /**
     * last recorded x mouse position (for drag)
     */
    private double lastMouseX;

    /**
     * last recorded y mouse position (for drag)
     */
    private double lastMouseY;

    /**
     * Creates the puzzle piece
     *
     * for now a fifth of the piece size is used for the irregular shape
     *
     * @param image Image of the puzzle
     * @param position the position of the piece inside the puzzle
     * @param size The size of the piece
     * @param shape The shape of the piece
     */
    public PuzzlePiece(Image image, Size size, Position position, SVGPath shape) {
        super();
        pieceShape = shape;
        ImageView iv = new ImageView(image);
        this.size = size;
        this.position = position;

        clipXCorrection = new SimpleDoubleProperty(size.x()/5);
        clipYCorrection = new SimpleDoubleProperty(size.y()/5);

        Rectangle2D viewport = new Rectangle2D(position.x() * size.x() - clipXCorrection.get(), position.y() * size.y() - clipYCorrection.get(), size.x() + (clipXCorrection.get())*2,
                size.y() + (clipYCorrection.get())*2);
        iv.setViewport(viewport);

        shape.setStroke(Color.GREENYELLOW);

        shape.translateXProperty().bind(clipXCorrection);
        shape.translateYProperty().bind(clipYCorrection);

        iv.setClip(shape);

        getChildren().addAll(iv);

        setMinSize(size.x() + (size.x()/5)*2, size.y() + (size.y()/5)*2);
        setPrefSize(size.x() + (size.x()/5)*2, size.y() + (size.y()/5)*2);
        setMaxSize(size.x() + (size.x()/5)*2, size.y() + (size.y()/5)*2);

        // checks if the piece is invisible (completely transparent)
        PixelReader pr = image.getPixelReader();
        boolean inv = true;
        for (int i = (int) viewport.getMinX(); i < viewport.getMaxX(); i++){
            for (int j = (int) viewport.getMinY(); j < viewport.getMaxY(); j++){
                if (j >= 0 && i >= 0 && i < image.getWidth() && j < image.getHeight()) {
                    Color col = pr.getColor(i, j);
                    if (col.getOpacity() != 0) {
                        inv = false;
                        break;
                    }
                }
            }
        }
        invisible = inv;
        setPickOnBounds(false);

        setActions();

    }

    /**
     * Sets the supported actions on the piece
     */
    private void setActions() {
        // when primary mouse button is pressed, brings all the pieces from the containing fragment to the front
        setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                for (PuzzlePiece piece : inFragment.getPieces()) {
                    piece.toFront();
                }
                lastMouseX = event.getScreenX();
                lastMouseY = event.getScreenY();
            }
        });

        // when primary mouse button is released, connects the fragment if possible with another fragment
        setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                boolean connected = true;
                while (connected) {
                    connected = inFragment.checkConnections();
                }
            }
        });

        // when secondary mouse button is clicked, pushed all the pieces from the containing fragment to the back
        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY){
                for (PuzzlePiece piece : inFragment.getPieces()) {
                    piece.toBack();
                }
            }
        });

        // moves all the pieces from the containing fragment as the mouse drags
        setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                // delta / getParent().getScale() -> so the mouse stays with the group in all zooms
                double deltaX = (event.getScreenX() - lastMouseX) / getParent().getScaleX();
                double deltaY = (event.getScreenY() - lastMouseY) / getParent().getScaleY();

                for (PuzzlePiece piece : inFragment.getPieces()) {
                    piece.moveDragged(deltaX, deltaY);
                }

                lastMouseX = event.getScreenX();
                lastMouseY = event.getScreenY();
                event.consume();
            }
        });
    }

    /**
     * move the piece by the deltas
     * @param deltaX The X translation
     * @param deltaY The Y translation
     */
    public void moveDragged(double deltaX, double deltaY){
        double initialTranslateX = getTranslateX();
        double initialTranslateY = getTranslateY();

        setTranslateX(initialTranslateX + deltaX);
        setTranslateY(initialTranslateY + deltaY);

        inFragment.getPuzzle().getTable().resizeTable(getBoundsInParent());
    }

    /**
     * @return whether this piece is fully transparent
     */
    public boolean isInvisible() {
        return invisible;
    }

    /**
     * calculates the distance between this piece and the given one if they are both adjacent
     * @param piece2 the second piece
     * @return the distance between the pieces if adjacent
     */
    public Optional<Delta> distance(PuzzlePiece piece2) {
        Optional<Delta> distance = Optional.empty();

        Bounds boundsInScene = localToScene(getBoundsInLocal());
        Bounds boundsInScene2 = piece2.localToScene(piece2.getBoundsInLocal());

        if (position.x() == piece2.position.x()) {
            double scale = getParent().getScaleY();
            if (position.y() + 1 == piece2.position.y()){
                distance = Optional.of(new Delta(boundsInScene.getCenterX() - boundsInScene2.getCenterX(), boundsInScene.getCenterY() - (boundsInScene2.getCenterY() - (size.y() * scale))));
            } else if (position.y() - 1 == piece2.position.y()){
                distance = Optional.of(new Delta(boundsInScene.getCenterX() - boundsInScene2.getCenterX(), boundsInScene.getCenterY() - (boundsInScene2.getCenterY() + (size.y() * scale))));
            }
        } else if (position.y() == piece2.position.y()){
            double scale = getParent().getScaleX();
            if (position.x() + 1 == piece2.position.x()){
                distance = Optional.of(new Delta(boundsInScene.getCenterX() - (boundsInScene2.getCenterX() - (size.x() * scale)), boundsInScene.getCenterY() - boundsInScene2.getCenterY()));
            } else if (position.x() - 1 == piece2.position.x()){
                distance = Optional.of(new Delta(boundsInScene.getCenterX() - (boundsInScene2.getCenterX() + (size.x() * scale)), boundsInScene.getCenterY() - boundsInScene2.getCenterY()));
            }
        }

        return distance;
    }

    /**
     * @param puzzleFragment the new owning fragment
     */
    public void setOwningFragment(PuzzleFragment puzzleFragment) {
        inFragment = puzzleFragment;
    }

    /**
     * adjusts the piece's position
     * @param delta The values by which to adust the piece's position
     */
    public void adjust(Delta delta) {
        setTranslateX(getTranslateX() + delta.x());
        setTranslateY(getTranslateY() + delta.y());
    }

    /**
     * @return the shape of the piece
     */
    public SVGPath getPieceShape() {
        return pieceShape;
    }

    /**
     * @return The piece adjustment for its shape width wise
     */
    public ReadOnlyDoubleProperty clipXCorrectionProperty() {
        return clipXCorrection;
    }

    /**
     * @return The piece adjustment for its shape height wise
     */
    public ReadOnlyDoubleProperty clipYCorrectionProperty() {
        return clipYCorrection;
    }
}