package puzzlegame;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import puzzlegame.chooserdialog.PuzzleChooserDialog;
import puzzlegame.chooserdialog.PuzzleChooserScene;
import puzzlegame.puzzle.PuzzleTable;
import puzzlegame.startscreen.StartScreen;

public class PuzzleMain extends Application {


    private PuzzleTable puzzleTable;
    private StartScreen startScreen;

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = new StackPane();
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Hello World");
        puzzleTable = new PuzzleTable(this);
        startScreen = new StartScreen(this);

      //  primaryStage.setScene(puzzleTable);
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

    public void sendInfoToPuzzleTable(Image choosenImage, int nbPieces) {
        puzzleTable.setNewPuzzle(choosenImage, nbPieces);
    }

    public void switchToPuzzleTable() {
        primaryStage.setScene(puzzleTable);
    }

    public void switchToStartScreen() {
        primaryStage.setScene(startScreen);
    }
}
