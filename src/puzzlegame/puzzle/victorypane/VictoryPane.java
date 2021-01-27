package puzzlegame.puzzle.victorypane;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import puzzlegame.puzzle.PuzzleTable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class VictoryPane extends StackPane {
    /**
     * Creates a Scene for a specific root Node.
     *
     */
    public VictoryPane(PuzzleTable window) {
        Image image = null;
        // Background image
        try (InputStream is = Files.newInputStream(Paths.get("src/resources/teamwork-3275565_1920.jpg"))) {
            image = new Image(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setAlignment(Pos.CENTER);

        // Create background Pane
        var backgroundPlate = new ImageView(image);
        backgroundPlate.setPreserveRatio(true);

        var splitA = new VBox();
        var buttonBar = new HBox();

        splitA.setAlignment(Pos.CENTER);

        getChildren().addAll(splitA);

        splitA.getChildren().addAll(backgroundPlate, buttonBar);

        Button newPuzzle = new Button("Start New Puzzle");
        newPuzzle.setOnAction(event -> {
            window.getPuzzleGame().showPuzzleChooserDialog();
        });

        Button backToMain = new Button ("Back to Main menu");
        backToMain.setOnAction(event -> {
            window.getPuzzleGame().switchToStartScreen();
        });

        buttonBar.getChildren().addAll(newPuzzle, backToMain);
        buttonBar.setAlignment(Pos.CENTER);



    }
}