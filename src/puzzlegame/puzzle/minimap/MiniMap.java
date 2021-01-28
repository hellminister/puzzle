package puzzlegame.puzzle.minimap;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.StackPane;
import puzzlegame.puzzle.PuzzlePiece;
import puzzlegame.util.Utilities;

import java.util.List;

public class MiniMap extends StackPane {

    private final RatioBinding ratio;


    public MiniMap(StackPane table, double maxWidth, double maxHeight){
        super();
        DoubleProperty maxHeight1 = new SimpleDoubleProperty(maxHeight);
        DoubleProperty maxWidth1 = new SimpleDoubleProperty(maxWidth);
        setStyle("-fx-background-color: black;" +
                " -fx-border-width: 3;" +
                " -fx-border-color: white");

        ratio = new RatioBinding(table, maxWidth1, maxHeight1);

        Utilities.attach(this, table.widthProperty().divide(ratio), table.heightProperty().divide(ratio));

    }

    public void populate(List<PuzzlePiece> pieces){
        getChildren().clear();

        for(PuzzlePiece piece : pieces) {
            MinimapPuzzlePiece minimapPiece = new MinimapPuzzlePiece(piece, ratio);
            getChildren().add(minimapPiece);
        }


    }


}
