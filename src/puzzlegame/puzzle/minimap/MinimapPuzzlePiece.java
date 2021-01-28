package puzzlegame.puzzle.minimap;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import puzzlegame.puzzle.PuzzlePiece;
import puzzlegame.util.Utilities;

public class MinimapPuzzlePiece extends Group {


    public MinimapPuzzlePiece(PuzzlePiece piece, RatioBinding ratio){
        super();



        ImageView original = (ImageView) piece.getChildren().get(0);

        ImageView me = new ImageView(original.getImage());
        me.setViewport(original.getViewport());

        getChildren().add(me);

        me.fitHeightProperty().bind(piece.heightProperty().divide(ratio));
        me.fitWidthProperty().bind(piece.widthProperty().divide(ratio));


        this.translateXProperty().bind(piece.parentTranslateXProperty().add(piece.layoutXProperty()).divide(ratio));
        this.translateYProperty().bind(piece.parentTranslateYProperty().add(piece.layoutYProperty()).divide(ratio));

    }

}
