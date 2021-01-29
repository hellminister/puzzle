package puzzlegame;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import puzzlegame.puzzlescreen.PuzzleScene;
import puzzlegame.startscreen.StartScreen;

public class PuzzleMain extends Application {


    private PuzzleScene puzzleScene;
    private StartScreen startScreen;

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Hello World");
        puzzleScene = new PuzzleScene(this);
        startScreen = new StartScreen(this);

        primaryStage.setScene(startScreen);
        primaryStage.sizeToScene();
        primaryStage.setMaximized(true);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }


    public ObservableValue<Boolean> finishedPuzzleProperty() {
        return puzzleScene.puzzleFinishedProperty();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void sendInfoToPuzzleTable(Image chosenImage, int nbPieces) {
        puzzleScene.setNewPuzzle(chosenImage, nbPieces);
    }

    public void switchToPuzzleTable() {
        primaryStage.setScene(puzzleScene);

        // until i find a better way to have the scrollbars showing in maximized window
        if (primaryStage.isMaximized()){
            primaryStage.setMaximized(false);
            primaryStage.setMaximized(true);
        }
    }

    public void switchToStartScreen() {
        primaryStage.setScene(startScreen);
    }

    public void showPuzzleChooserDialog() {
        startScreen.showPuzzleChooserDialog();
    }
}
