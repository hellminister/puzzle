package puzzlegame.puzzle;

import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.Optional;

public class PuzzlePiece extends StackPane {

    private final Position position;
    private final Size size;
    private final boolean invisible;

    /**
     * Allocates a new ImageView object using the given image.
     *
     * @param image Image that this ImageView uses
     */
    public PuzzlePiece(Image image, Size size, Position position) {
        ImageView iv = new ImageView(image);
        this.size = size;
        this.position = position;
        Rectangle2D viewport = new Rectangle2D(position.x() * size.x(), position.y() * size.y(), size.x(), size.y());
        iv.setViewport(viewport);
        getChildren().add(iv);

        PixelReader pr = image.getPixelReader();
        boolean inv = true;
        for (int i = (int) viewport.getMinX(); i < viewport.getMaxX(); i++){
            for (int j = (int) viewport.getMinY(); j < viewport.getMaxY(); j++){
                Color col = pr.getColor(i, j);
                if (col.getOpacity() != 0){
                    inv = false;
                    break;
                }
            }
        }
        invisible = inv;
        setPickOnBounds(false);


    }

    public boolean isInvisible() {
        return invisible;
    }

    public Optional<Delta> distance(PuzzlePiece piece2) {
        Optional<Delta> distance = Optional.empty();

        Bounds boundsInScene = localToScene(getBoundsInLocal());
        Bounds boundsInScene2 = piece2.localToScene(piece2.getBoundsInLocal());

        if (position.x() == piece2.position.x()) {
            double scale = getParent().getParent().getScaleY();
            if (position.y() + 1 == piece2.position.y()){
                distance = Optional.of(new Delta(boundsInScene.getCenterX() - boundsInScene2.getCenterX(), boundsInScene.getCenterY() - (boundsInScene2.getCenterY() - (size.y() * scale))));
            } else if (position.y() - 1 == piece2.position.y()){
                distance = Optional.of(new Delta(boundsInScene.getCenterX() - boundsInScene2.getCenterX(), boundsInScene.getCenterY() - (boundsInScene2.getCenterY() + (size.y() * scale))));
            }
        } else if (position.y() == piece2.position.y()){
            double scale = getParent().getParent().getScaleX();
            if (position.x() + 1 == piece2.position.x()){
                distance = Optional.of(new Delta(boundsInScene.getCenterX() - (boundsInScene2.getCenterX() - (size.x() * scale)), boundsInScene.getCenterY() - boundsInScene2.getCenterY()));
            } else if (position.x() - 1 == piece2.position.x()){
                distance = Optional.of(new Delta(boundsInScene.getCenterX() - (boundsInScene2.getCenterX() + (size.x() * scale)), boundsInScene.getCenterY() - boundsInScene2.getCenterY()));
            }
        }

        return distance;
    }

    public double getRelativeX(PuzzlePiece piece2) {
        return ((position.x() - piece2.position.x()) * size.x());
    }

    public double getRelativeY(PuzzlePiece piece2) {
        return ((position.y() - piece2.position.y()) * size.y());
    }
}
