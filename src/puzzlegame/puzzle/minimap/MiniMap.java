package puzzlegame.puzzle.minimap;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.StackPane;
import puzzlegame.puzzle.PuzzleFragment;
import puzzlegame.puzzle.PuzzlePiece;
import puzzlegame.util.Utilities;

import java.util.List;

public class MiniMap extends StackPane {

    private final DoubleProperty maxWidth;
    private final DoubleProperty maxHeight;

    private final RatioBinding ratio;


    public MiniMap(StackPane table, double maxWidth, double maxHeight){
        super();
        this.maxHeight = new SimpleDoubleProperty(maxHeight);
        this.maxWidth = new SimpleDoubleProperty(maxWidth);
        setStyle("-fx-background-color: black;" +
                " -fx-border-width: 3;" +
                " -fx-border-color: white");

        ratio = new RatioBinding(table, this.maxWidth, this.maxHeight);

        Utilities.attach(this, table.widthProperty().divide(ratio), table.heightProperty().divide(ratio));

    }

    public void populate(List<PuzzleFragment> fragments){
        getChildren().clear();

        for (PuzzleFragment fragment : fragments){
            for(PuzzlePiece piece : fragment.getPieces()) {
                MinimapPuzzlePiece minimapPiece = new MinimapPuzzlePiece(piece, ratio);
                getChildren().add(minimapPiece);
            }
        }

    }


}
