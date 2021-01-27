package puzzlegame;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import puzzlegame.puzzle.PuzzleTable;
import puzzlegame.startscreen.StartScreen;

public class PuzzleMain extends Application {


    private PuzzleTable puzzleTable;
    private StartScreen startScreen;

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Hello World");
        puzzleTable = new PuzzleTable(this);
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
        return puzzleTable.puzzleFinishedProperty();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void sendInfoToPuzzleTable(Image chosenImage, int nbPieces) {
        puzzleTable.setNewPuzzle(chosenImage, nbPieces);
    }

    public void switchToPuzzleTable() {
        primaryStage.setScene(puzzleTable);
    }

    public void switchToStartScreen() {
        primaryStage.setScene(startScreen);
    }

    public void showPuzzleChooserDialog() {
        startScreen.showPuzzleChooserDialog();
    }
}
