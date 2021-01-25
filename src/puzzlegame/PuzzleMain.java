package puzzlegame;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import puzzlegame.puzzle.PuzzleTable;

public class PuzzleMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = new StackPane();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new PuzzleTable(primaryStage));
        primaryStage.sizeToScene();
        primaryStage.setMaximized(true);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
