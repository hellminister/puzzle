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
import puzzlegame.language.Localize;
import puzzlegame.settingsdialog.SettingsDialog;
import puzzlegame.util.Utilities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The main menu of the game
 */
public class StartScreen extends Scene {

    /** The puzzle chooser dialog window*/
    private final PuzzleChooserDialog pcd;

    private final SettingsDialog settingsDialog;

    /**
     * Creates the start screen scene
     * @param mainWindow The application object creating this
     */
    public StartScreen(PuzzleMain mainWindow) {
        super(new StackPane());
        pcd = new PuzzleChooserDialog(mainWindow);
        settingsDialog = new SettingsDialog(mainWindow);

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

        // this button is available only when there's an unfinished puzzle
        Button continueButton = new Button();
        continueButton.textProperty().bind(Localize.get(Localize.Target.START_SCREEN_CONTINUE));
        continueButton.setOnAction(event -> mainWindow.switchToPuzzleTable());
        continueButton.disableProperty().bind(mainWindow.finishedPuzzleProperty());

        Button startNew = new Button();
        startNew.textProperty().bind(Localize.get(Localize.Target.START_SCREEN_START_NEW));
        startNew.setOnAction(event -> showPuzzleChooserDialog());

        Button settings = new Button();
        settings.textProperty().bind(Localize.get(Localize.Target.START_SCREEN_SETTINGS));
        settings.setOnAction(event -> showOptionDialogBox());

        Button quit = new Button();
        quit.textProperty().bind(Localize.get(Localize.Target.START_SCREEN_QUIT));
        quit.setOnAction(event -> Platform.exit());

        buttonBar.getChildren().addAll(continueButton, startNew, settings, quit);
        buttonBar.setAlignment(Pos.CENTER);

        buttonBar.setSpacing(10);

        backgroundImagePane.getChildren().add(buttonBar);

        Utilities.attach(backgroundImagePane, mainWindow.getPrimaryStage().widthProperty(), mainWindow.getPrimaryStage().heightProperty());
    }

    private void showOptionDialogBox() {
        settingsDialog.showMe();
    }

    /**
     * Calls up the puzzle chooser dialog
     */
    public void showPuzzleChooserDialog(){
        pcd.show();
    }
}