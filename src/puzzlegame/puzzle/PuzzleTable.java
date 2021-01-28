package puzzlegame.puzzle;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import puzzlegame.PuzzleMain;
import puzzlegame.puzzle.minimap.MiniMap;
import puzzlegame.puzzle.victorypane.VictoryPane;
import puzzlegame.util.Utilities;

import java.util.Random;

public class PuzzleTable extends Scene {

    private final StackPane table;
    private final ScrollPane pane;

    private final PuzzleMain puzzleGame;

    private final BooleanProperty puzzleFinished;

    private final ImageView imageHint;
    private final MiniMap miniMap;

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

        var tableSide = new AnchorPane();
        tableSide.getChildren().add(pane);

        var topBar = new HBox();
        topBar.setStyle("-fx-background-color: white");
        topBar.setAlignment(Pos.BASELINE_LEFT);
        topBar.setSpacing(3);


        // Start zoom control section

        Button zoomIn = new Button("+");
        zoomIn.setOnAction(event ->  zoomIn());

        Button zoomOut = new Button("-");
        zoomOut.setOnAction(event -> zoomOut());

        Label zoomValue = new Label();
        zoomValue.textProperty().bind(Bindings.format("Zoom: %.0f%%", table.scaleXProperty().multiply(100)));

        // End zoom control section



        // start show image section

        imageHint = new ImageView();
        imageHint.setPreserveRatio(true);
        imageHint.setFitWidth(400);
        imageHint.setFitHeight(400);

        tableSide.getChildren().add(imageHint);
        AnchorPane.setTopAnchor(imageHint, 50.0);
        AnchorPane.setLeftAnchor(imageHint, 50.0);

        ToggleButton showImage = new ToggleButton("Display Image");
        imageHint.visibleProperty().bind(showImage.selectedProperty());

        // end show image section

        // start mini map section

        miniMap = new MiniMap(table, 400, 400);

        tableSide.getChildren().add(miniMap);
        AnchorPane.setTopAnchor(miniMap, 50.0);
        AnchorPane.setRightAnchor(miniMap, 50.0);

        ToggleButton showMiniMap = new ToggleButton("Show Minimap");
        miniMap.visibleProperty().bind(showMiniMap.selectedProperty());


        // end minimap section

        topBar.getChildren().addAll(showMiniMap, showImage, zoomValue, zoomIn, zoomOut);


        borderPane.getChildren().addAll(topBar, tableSide);

        table.setOnScroll(event -> {
            double scrollValue = event.getDeltaY();
            if (scrollValue < 0){
                zoomOut();
            } else if (scrollValue > 0){
                zoomIn();
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

        Utilities.attach(borderPane, root.widthProperty(), root.heightProperty());
        Utilities.attach(tableSide, borderPane.widthProperty(), borderPane.heightProperty().subtract(topBar.heightProperty()));
        Utilities.attach(pane, tableSide.widthProperty(), tableSide.heightProperty());
        Utilities.attach(root, puzzleGame.getPrimaryStage().widthProperty(), puzzleGame.getPrimaryStage().heightProperty());
        Utilities.attach(victoryPane, root.widthProperty().multiply(0.75), root.heightProperty().multiply(0.75));

        setOnKeyPressed(event -> {
            var key = event.getCode();

            if (key == KeyCode.ESCAPE){
                puzzleGame.switchToStartScreen();
            }

        });

    }

    private void zoomOut() {
        table.setScaleX(table.getScaleX()-0.01);
        table.setScaleY(table.getScaleY()-0.01);
    }

    private void zoomIn() {
        table.setScaleX(table.getScaleX()+0.01);
        table.setScaleY(table.getScaleY()+0.01);
    }

    public PuzzleMain getPuzzleGame() {
        return puzzleGame;
    }

    public void resizeTable(Bounds position){
        System.out.println(position);

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

    public ReadOnlyBooleanProperty puzzleFinishedProperty() {
        return puzzleFinished;
    }

    public void setNewPuzzle(Image chosenImage, int nbPieces) {
        table.getChildren().clear();
        Puzzle puzzle = new Puzzle(chosenImage, nbPieces, this);

        table.setMinHeight(chosenImage.getHeight() * 4);
        table.setMinWidth(chosenImage.getWidth() * 4);

        Random rand = new Random();
        for (PuzzlePiece pf : puzzle.getPieces()){
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
        imageHint.setImage(chosenImage);
        miniMap.populate(puzzle.getPieces());
    }
}
