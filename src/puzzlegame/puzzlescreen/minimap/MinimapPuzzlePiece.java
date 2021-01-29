package puzzlegame.puzzlescreen.minimap;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import puzzlegame.puzzlescreen.puzzletable.PuzzlePiece;

public class MinimapPuzzlePiece extends Group {


    public MinimapPuzzlePiece(PuzzlePiece piece, RatioBinding ratio){
        super();



        ImageView original = (ImageView) piece.getChildren().get(0);

        ImageView me = new ImageView(original.getImage());
        me.setViewport(original.getViewport());

        getChildren().add(me);

        me.fitHeightProperty().bind(piece.heightProperty().divide(ratio));
        me.fitWidthProperty().bind(piece.widthProperty().divide(ratio));

        this.translateXProperty().bind(piece.translateXProperty().divide(ratio));
        this.translateYProperty().bind(piece.translateYProperty().divide(ratio));

    }

}
