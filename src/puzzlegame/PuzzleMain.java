package puzzlegame;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import puzzlegame.language.Localize;
import puzzlegame.puzzlescreen.PuzzleScene;
import puzzlegame.startscreen.StartScreen;

/**
 * The main class of the puzzle game
 * Controls the Main window and the change of scenes
 */
public class PuzzleMain extends Application {


    /** The Scene where we actually do the puzzle*/
    private PuzzleScene puzzleScene;

    /** The Scene of the Main menu*/
    private StartScreen startScreen;

    /** The main window of the game*/
    private Stage primaryStage;

    /**
     * Initialize and shows the main window of the game
     * @param primaryStage The Main window of the game
     */
    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        Localize.load("francais");
     //   primaryStage.setTitle("Puzzler");
        primaryStage.titleProperty().bind(Localize.get(Localize.Target.GAME_TITLE));

        puzzleScene = new PuzzleScene(this);
        startScreen = new StartScreen(this);

        primaryStage.setScene(startScreen);
        primaryStage.sizeToScene();
        primaryStage.setMaximized(true);
        primaryStage.show();

    }

    /**
     * Entry point of the Application
     * @param args not used
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * A property that indicates if the current puzzle is finished
     * @return an unmodifiable property to link to
     */
    public ReadOnlyBooleanProperty finishedPuzzleProperty() {
        return puzzleScene.puzzleFinishedProperty();
    }

    /**
     * @return the main window of the game
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Permits transfer of informations to the puzzle scene
     * @param chosenImage The puzzle image that was chosen
     * @param nbPieces    The number of pieces that the puzzle will have
     */
    public void sendInfoToPuzzleTable(Image chosenImage, int nbPieces) {
        puzzleScene.setNewPuzzle(chosenImage, nbPieces);
    }

    /**
     * Switch the view to the puzzle scene
     */
    public void switchToPuzzleTable() {
        primaryStage.setScene(puzzleScene);

        // until i find a better way to have the scrollbars showing in maximized window
        if (primaryStage.isMaximized()){
            primaryStage.setMaximized(false);
            primaryStage.setMaximized(true);
        }
    }

    /**
     * switch the view to the start screen
     */
    public void switchToStartScreen() {
        primaryStage.setScene(startScreen);
    }

    /**
     * Brings up the puzzle chooser dialog
     */
    public void showPuzzleChooserDialog() {
        startScreen.showPuzzleChooserDialog();
    }
}
