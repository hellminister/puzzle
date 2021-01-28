package puzzlegame.startscreen;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import puzzlegame.PuzzleMain;
import puzzlegame.chooserdialog.PuzzleChooserDialog;
import puzzlegame.util.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


public class StartScreen extends Scene {

    private final PuzzleChooserDialog pcd;

    /**
     * Creates a Scene for a specific root Node.
     *
     */
    public StartScreen(PuzzleMain mainWindow) {
        super(new StackPane());
        pcd = new PuzzleChooserDialog(mainWindow);

        var root = (StackPane) getRoot();
        root.setStyle("-fx-background-color: black");

        Image image = null;
        // Background image
        try (InputStream is = Files.newInputStream(Paths.get("src/resources/faces-550786_1920.jpg"))) {
            image = new Image(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create background Pane
        var backgroundPlate = new ImageView(image);
        backgroundPlate.setPreserveRatio(true);

        // this pane is to permit resizing of the window
        var backgroundImagePane = new StackPane();
        backgroundImagePane.getChildren().add(backgroundPlate);

        root.setPrefSize(backgroundPlate.getLayoutBounds().getWidth(), backgroundPlate.getLayoutBounds().getHeight());

        backgroundPlate.fitHeightProperty().bind(root.heightProperty());
        backgroundPlate.fitWidthProperty().bind(root.widthProperty());

        root.getChildren().add(backgroundImagePane);

        var buttonBar = new VBox();

        Button continueButton = new Button("Continue");
        continueButton.setOnAction(event -> mainWindow.switchToPuzzleTable());
        continueButton.disableProperty().bind(mainWindow.finishedPuzzleProperty());

        Button startNew = new Button("Start new puzzle");
        startNew.setOnAction(event -> showPuzzleChooserDialog());

        Button quit = new Button("Quit");
        quit.setOnAction(event -> Platform.exit());

        buttonBar.getChildren().addAll(continueButton, startNew, quit);
        buttonBar.setAlignment(Pos.CENTER);

        buttonBar.setSpacing(10);

        backgroundImagePane.getChildren().add(buttonBar);

        Utilities.attach(backgroundImagePane, mainWindow.getPrimaryStage().widthProperty(), mainWindow.getPrimaryStage().heightProperty());
    }

    public void showPuzzleChooserDialog(){
        pcd.show();
    }
}
