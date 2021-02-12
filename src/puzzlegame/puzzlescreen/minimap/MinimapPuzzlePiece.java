package puzzlegame.puzzlescreen.minimap;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import puzzlegame.puzzlescreen.puzzletable.PuzzlePiece;
import puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath.SVGPathUtilities;

/**
 * Represents a puzzle piece, just smaller to place in the minimap
 */
public class MinimapPuzzlePiece extends Group {

    public MinimapPuzzlePiece(PuzzlePiece piece, RatioBinding ratio){
        super();
        ImageView original = (ImageView) piece.getChildren().get(0);

        ImageView me = new ImageView(original.getImage());
        me.setViewport(original.getViewport());

        SVGPath clip = new SVGPath();
        clip.contentProperty().bind(SVGPathUtilities.adjustableSize(piece.getPieceShape().getContent(), ratio));
        clip.setFill(Color.BLUE);
        clip.setStroke(Color.GREENYELLOW);
        clip.translateXProperty().bind(piece.clipXCorrectionProperty().divide(ratio));
        clip.translateYProperty().bind(piece.clipYCorrectionProperty().divide(ratio));


        me.setClip(clip);

        getChildren().add(me);

        me.fitHeightProperty().bind(piece.heightProperty().divide(ratio));
        me.fitWidthProperty().bind(piece.widthProperty().divide(ratio));

        this.translateXProperty().bind(piece.translateXProperty().divide(ratio));
        this.translateYProperty().bind(piece.translateYProperty().divide(ratio));

    }

}