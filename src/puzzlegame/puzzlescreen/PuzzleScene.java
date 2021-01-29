package puzzlegame.puzzlescreen;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import puzzlegame.PuzzleMain;
import puzzlegame.language.Localize;
import puzzlegame.puzzlescreen.minimap.MiniMap;
import puzzlegame.puzzlescreen.puzzletable.PuzzleTable;
import puzzlegame.puzzlescreen.victorypane.VictoryPane;
import puzzlegame.util.Utilities;

public class PuzzleScene extends Scene {

    private final PuzzleMain puzzleGame;

    private final BooleanProperty puzzleFinished;

    private final PuzzleTable puzzleTable;
    private final ImageView imageHint;
    private final MiniMap miniMap;

    /**
     * Creates a Scene for a specific root Node.
     */
    public PuzzleScene(PuzzleMain puzzleGame) {
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

        puzzleTable = new PuzzleTable();

        var topBar = new HBox();
        topBar.setStyle("-fx-background-color: white");
        topBar.setAlignment(Pos.BASELINE_LEFT);
        topBar.setSpacing(3);


        // Start zoom control section

        Button zoomIn = new Button("+");
        zoomIn.setOnAction(event ->  puzzleTable.zoomIn());

        Button zoomOut = new Button("-");
        zoomOut.setOnAction(event -> puzzleTable.zoomOut());

        Label zoomValue = new Label();
        zoomValue.textProperty().bind(Bindings.format("%s %.0f%%", Localize.get(Localize.Target.PUZZLE_SCENE_ZOOM), puzzleTable.getZoom().multiply(100)));

        // End zoom control section



        // start show image section

        imageHint = new ImageView();
        imageHint.setPreserveRatio(true);
        imageHint.setFitWidth(400);
        imageHint.setFitHeight(400);

        puzzleTable.getChildren().add(imageHint);
        AnchorPane.setTopAnchor(imageHint, 50.0);
        AnchorPane.setLeftAnchor(imageHint, 50.0);

        ToggleButton showImage = new ToggleButton();
        showImage.textProperty().bind(Localize.get(Localize.Target.PUZZLE_SCENE_SHOW_IMAGE));
        imageHint.visibleProperty().bind(showImage.selectedProperty());

        // end show image section

        // start mini map section

        miniMap = new MiniMap(puzzleTable, 400, 400);

        puzzleTable.getChildren().add(miniMap);
        AnchorPane.setTopAnchor(miniMap, 50.0);
        AnchorPane.setRightAnchor(miniMap, 50.0);

        ToggleButton showMiniMap = new ToggleButton();
        showMiniMap.textProperty().bind(Localize.get(Localize.Target.PUZZLE_SCENE_SHOW_MINIMAP));
        miniMap.visibleProperty().bind(showMiniMap.selectedProperty());


        // end minimap section

        topBar.getChildren().addAll(showMiniMap, showImage, zoomValue, zoomIn, zoomOut);


        borderPane.getChildren().addAll(topBar, puzzleTable);

        Utilities.attach(borderPane, root.widthProperty(), root.heightProperty());
        Utilities.attach(puzzleTable, borderPane.widthProperty(), borderPane.heightProperty().subtract(topBar.heightProperty()));
        Utilities.attach(root, puzzleGame.getPrimaryStage().widthProperty(), puzzleGame.getPrimaryStage().heightProperty());
        Utilities.attach(victoryPane, root.widthProperty().multiply(0.75), root.heightProperty().multiply(0.75));

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

    public ReadOnlyBooleanProperty puzzleFinishedProperty() {
        return puzzleFinished;
    }

    public void setNewPuzzle(Image chosenImage, int nbPieces) {
        puzzleTable.setNewPuzzle(chosenImage, nbPieces);

        puzzleFinished.bind(puzzleTable.getPuzzle().finished());
        imageHint.setImage(chosenImage);
        miniMap.populate(puzzleTable.getPuzzle().getPieces());
    }

    public PuzzleTable getPuzzleTable(){
        return puzzleTable;
    }
}
