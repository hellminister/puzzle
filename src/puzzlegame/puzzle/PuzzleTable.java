package puzzlegame.puzzle;

import javafx.beans.binding.DoubleExpression;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class PuzzleTable extends Scene {

    private StackPane table;
    private ScrollPane pane;

    /**
     * Creates a Scene for a specific root Node.
     */
    public PuzzleTable(Stage stage) {
        super(new BorderPane());
        var root = (BorderPane)getRoot();
        root.setStyle("-fx-background-color: black");


        var fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.gif", "*.png")
        );

        var db = new TextInputDialog();
        db.setContentText("How many pieces?");

        db.getEditor().setTextFormatter(
                new TextFormatter<>(new IntegerStringConverter(), 1));


        fc.setSelectedExtensionFilter(fc.getExtensionFilters().get(0));

        MenuBar menubar = new MenuBar();
        Menu menu = new Menu("file");
        MenuItem loadImage = new MenuItem("load Image");

        loadImage.setOnAction(event -> {
            File file = fc.showOpenDialog(stage);
            if (file != null){
                var nbPieceSt = db.showAndWait();
                System.out.println(nbPieceSt);
                nbPieceSt.ifPresent(s -> loadPuzzle(file, Integer.parseInt(s)));
                pane.layout();
                pane.setHvalue(0.5);
                pane.setVvalue(0.5);

            }
        });

        menu.getItems().add(loadImage);

        menubar.getMenus().add(menu);

        root.setTop(menubar);

        table = new StackPane();
        table.setAlignment(Pos.CENTER);

        table.setStyle("-fx-background-color: black");


        pane = new ScrollPane();
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        pane.setContent(table);

        pane.setStyle("-fx-background-color: black");
        pane.setPannable(true);

        root.setCenter(pane);

        table.setOnScroll(event -> {
            double scrollValue = event.getDeltaY();
            if (scrollValue < 0){
                table.setScaleX(table.getScaleX()-0.01);
                table.setScaleY(table.getScaleY()-0.01);
            } else if (scrollValue > 0){
                table.setScaleX(table.getScaleX()+0.01);
                table.setScaleY(table.getScaleY()+0.01);
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

        attach(pane, root.widthProperty(), root.heightProperty().subtract(menubar.heightProperty()));

    }


    private void loadPuzzle(File file, int nbPieces) {
        table.getChildren().clear();
        try (InputStream is = new FileInputStream(file)) {
            Image image = new Image(is);
            Puzzle puzzle = new Puzzle(image, nbPieces, this);

            table.setMinHeight(image.getHeight() * 4);
            table.setMinWidth(image.getWidth() * 4);

            Random rand = new Random();
            for (PuzzleFragment pf : puzzle.getFragments()){
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



        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void resizeTable(Bounds position){
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

    public void remove(PuzzleFragment puzzleFragment) {
        table.getChildren().remove(puzzleFragment);
    }

    public static void attach(Region pane, DoubleExpression width, DoubleExpression height){
        pane.maxWidthProperty().bind(width);
        pane.minWidthProperty().bind(width);
        pane.prefWidthProperty().bind(width);

        pane.maxHeightProperty().bind(height);
        pane.minHeightProperty().bind(height);
        pane.prefHeightProperty().bind(height);
    }
}
