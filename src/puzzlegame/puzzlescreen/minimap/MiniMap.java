package puzzlegame.puzzlescreen.minimap;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import puzzlegame.puzzlescreen.puzzletable.PuzzlePiece;
import puzzlegame.puzzlescreen.puzzletable.PuzzleTable;
import puzzlegame.util.Utilities;

import java.util.List;

/**
 * A minimap to show the position of all the puzzle pieces on the table
 */
public class MiniMap extends StackPane {

    /**
     * the ratio between the table and the minimap space
     */
    private final RatioBinding ratio;

    /**
     * A rectangle representing the viewport on the table
     */
    private final Rectangle viewport;


    public MiniMap(PuzzleTable table, double maxWidth, double maxHeight){
        super();
        DoubleProperty maxHeight1 = new SimpleDoubleProperty(maxHeight);
        DoubleProperty maxWidth1 = new SimpleDoubleProperty(maxWidth);
        setStyle("-fx-background-color: black;" +
                " -fx-border-width: 3;" +
                " -fx-border-color: white");

        ratio = new RatioBinding(table.getTable(), maxWidth1, maxHeight1);

        viewport = makeViewport(table);

        Utilities.attach(this, table.getTable().widthProperty().divide(ratio), table.getTable().heightProperty().divide(ratio));

        setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double posX = event.getX();
                double posY = event.getY();

                table.setTablePosition(posY/getHeight(), posX/getWidth());
            }
        });

        setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double posX = event.getX();
                double posY = event.getY();

                table.setTablePosition(posY/getHeight(), posX/getWidth());
            }
        });

    }

    /**
     * Creates a rectangle representing the viewport on the table
     * (works more or less)
     * @param table the puzzle table
     * @return a rectangle representing the viewport on the table
     */
    private Rectangle makeViewport(PuzzleTable table) {
        final Rectangle viewport;
        viewport = new Rectangle();
        viewport.setStroke(Color.DARKSLATEGREY);

        ScrollPane tablePane = table.getPane();


        viewport.heightProperty().bind(tablePane.heightProperty().divide(ratio).divide(table.getTable().scaleXProperty()));
        viewport.widthProperty().bind(tablePane.widthProperty().divide(ratio).divide(table.getTable().scaleYProperty()));

        viewport.translateXProperty().bind(table.getTable().widthProperty().subtract(tablePane.widthProperty()).divide(ratio).multiply(tablePane.hvalueProperty()).multiply(table.getTable().scaleXProperty()));
        viewport.translateYProperty().bind(table.getTable().heightProperty().subtract(tablePane.heightProperty()).divide(ratio).multiply(tablePane.vvalueProperty()).multiply(table.getTable().scaleYProperty()));
        return viewport;
    }

    /**
     * Fills the minimap
     * @param pieces the pieces to add to the minimap
     */
    public void populate(List<PuzzlePiece> pieces){
        getChildren().clear();

        getChildren().add(viewport);
        StackPane.setAlignment(viewport, Pos.TOP_LEFT);

        for(PuzzlePiece piece : pieces) {
            MinimapPuzzlePiece minimapPiece = new MinimapPuzzlePiece(piece, ratio);
            getChildren().add(minimapPiece);
        }


    }


}