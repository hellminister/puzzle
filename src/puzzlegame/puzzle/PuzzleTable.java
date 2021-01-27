package puzzlegame.puzzle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import puzzlegame.PuzzleMain;
import puzzlegame.puzzle.victorypane.VictoryPane;
import puzzlegame.util.Utilities;

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
        super(new StackPane());
        this.puzzleGame = puzzleGame;


        var root = (StackPane) getRoot();

        var borderPane = new VBox();
        borderPane.setStyle("-fx-background-color: black");

        puzzleFinished = new SimpleBooleanProperty(true);

        VictoryPane victoryPane = new VictoryPane(this);
        victoryPane.visibleProperty().bind(puzzleFinished);

        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(borderPane, victoryPane);

        table = new StackPane();
        table.setAlignment(Pos.CENTER);

        table.setStyle("-fx-background-color: black");


        pane = new ScrollPane();
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        pane.setContent(table);

        pane.setStyle("-fx-background-color: black");
        pane.setPannable(true);

        borderPane.getChildren().addAll(pane);

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

        Utilities.attach(pane, borderPane.widthProperty(), borderPane.heightProperty());
        Utilities.attach(root, puzzleGame.getPrimaryStage().widthProperty(), puzzleGame.getPrimaryStage().heightProperty());
        Utilities.attach(victoryPane, root.widthProperty(), root.heightProperty());
        Utilities.attach(borderPane, root.widthProperty(), root.heightProperty());

        setOnKeyPressed(event -> {
            var key = event.getCode();

            if (key == KeyCode.ESCAPE){
                puzzleGame.switchToStartScreen();
            }

        });

    }

    public PuzzleMain getPuzzleGame() {
        return puzzleGame;
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



    public ReadOnlyBooleanProperty puzzleFinishedProperty() {
        return puzzleFinished;
    }

    public void setNewPuzzle(Image chosenImage, int nbPieces) {
        table.getChildren().clear();
        Puzzle puzzle = new Puzzle(chosenImage, nbPieces, this);

        table.setMinHeight(chosenImage.getHeight() * 4);
        table.setMinWidth(chosenImage.getWidth() * 4);

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
