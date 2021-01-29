package puzzlegame.puzzle.minimap;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import puzzlegame.puzzle.PuzzlePiece;
import puzzlegame.puzzle.PuzzleTable;
import puzzlegame.util.Utilities;

import java.util.List;

public class MiniMap extends StackPane {

    private final RatioBinding ratio;

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

    private Rectangle makeViewport(PuzzleTable table) {
        final Rectangle viewport;
        viewport = new Rectangle();
       // viewport.setFill(Color.BLUE);
        viewport.setStroke(Color.DARKSLATEGREY);

        ScrollPane tablePane = table.getPane();


        viewport.heightProperty().bind(tablePane.heightProperty().divide(ratio).divide(table.getTable().scaleXProperty()));
        viewport.widthProperty().bind(tablePane.widthProperty().divide(ratio).divide(table.getTable().scaleYProperty()));

        viewport.translateXProperty().bind(table.getTable().widthProperty().subtract(tablePane.widthProperty()).divide(ratio).multiply(tablePane.hvalueProperty()).multiply(table.getTable().scaleXProperty()));
        viewport.translateYProperty().bind(table.getTable().heightProperty().subtract(tablePane.heightProperty()).divide(ratio).multiply(tablePane.vvalueProperty()).multiply(table.getTable().scaleYProperty()));
        return viewport;
    }

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
