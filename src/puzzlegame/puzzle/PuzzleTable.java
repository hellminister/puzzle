package puzzlegame.puzzle;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import puzzlegame.PuzzleMain;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class PuzzleTable extends Scene {

    private final StackPane table;
    private final ScrollPane pane;

    private final PuzzleMain puzzleGame;

    private final BooleanProperty puzzleFinished;

    /**
     * Creates a Scene for a specific root Node.
     */
    public PuzzleTable(PuzzleMain puzzleGame) {
        super(new BorderPane());
        this.puzzleGame = puzzleGame;

        var root = (BorderPane)getRoot();
        root.setStyle("-fx-background-color: black");

        puzzleFinished = new SimpleBooleanProperty(true);

        table = new StackPane();
        table.setAlignment(Pos.CENTER);

        table.setStyle("-fx-background-color: black");


        pane = new ScrollPane();
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        pane.setContent(table);

        pane.setStyle("-fx-background-color: black");
        pane.setPannable(true);

        root.setCenter(pane);

        table.setOnScroll(event -> {
            double scrollValue = event.getDeltaY();
            if (scrollValue < 0){
                table.setScaleX(table.getScaleX()-0.01);
                table.setScaleY(table.getScaleY()-0.01);
            } else if (scrollValue > 0){
                table.setScaleX(table.getScaleX()+0.01);
                table.setScaleY(table.getScaleY()+0.01);
            }
            event.consume();
        });

        table.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.MIDDLE){
                table.setScaleX(1);
                table.setScaleY(1);
                event.consume();
            }
        });

        attach(pane, root.widthProperty(), root.heightProperty());

        setOnKeyPressed(event -> {
            var key = event.getCode();

            if (key == KeyCode.ESCAPE){
                puzzleGame.switchToStartScreen();
            }

        });

    }

    public void resizeTable(Bounds position){
        if (position.getMinX() < 0){
            table.setMinWidth(table.getWidth() - position.getMinX());
        }

        if (position.getMaxX() > table.getWidth()){
            table.setMinWidth(position.getMaxX());
            pane.setHvalue(pane.getHmax());
        }

        if (position.getMinY() < 0){
            table.setMinHeight(table.getHeight() - position.getMinY());
        }

        if (position.getMaxY() > table.getHeight()){
            table.setMinHeight(position.getMaxY());
            pane.setVvalue(pane.getVmax());
        }
    }

    public void remove(PuzzleFragment puzzleFragment) {
        table.getChildren().remove(puzzleFragment);
    }

    public static void attach(Region pane, DoubleExpression width, DoubleExpression height){
        pane.maxWidthProperty().bind(width);
        pane.minWidthProperty().bind(width);
        pane.prefWidthProperty().bind(width);

        pane.maxHeightProperty().bind(height);
        pane.minHeightProperty().bind(height);
        pane.prefHeightProperty().bind(height);
    }

    public ReadOnlyBooleanProperty puzzleFinishedProperty() {
        return puzzleFinished;
    }

    public void setNewPuzzle(Image choosenImage, int nbPieces) {
        table.getChildren().clear();
        Puzzle puzzle = new Puzzle(choosenImage, nbPieces, this);

        table.setMinHeight(choosenImage.getHeight() * 4);
        table.setMinWidth(choosenImage.getWidth() * 4);

        Random rand = new Random();
        for (PuzzleFragment pf : puzzle.getFragments()){
            pf.setTranslateX(rand.nextInt(1200)-600);

            pf.setTranslateY(rand.nextInt(1200)-600);
            table.getChildren().add(pf);
        }

        if (table.getMinWidth() < pane.getWidth()){
            table.setMinWidth(pane.getWidth());
        }

        if (table.getMinHeight() < pane.getHeight()){
            table.setMinHeight(pane.getHeight());
        }

        pane.layout();

        pane.setVvalue(0.5);
        pane.setHvalue(0.5);

        puzzleFinished.bind(puzzle.finished());
    }
}
