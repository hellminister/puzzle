package puzzlegame.puzzlescreen.puzzletable;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import puzzlegame.util.Utilities;

import java.util.Random;

public class PuzzleTable extends AnchorPane {

    private final StackPane table;
    private final ScrollPane pane;
    private Puzzle puzzle;

    public PuzzleTable(){

        table = new StackPane();
        table.setAlignment(Pos.CENTER);

        table.setStyle("-fx-background-color: black");


        pane = new ScrollPane();
        pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        //table put inside a group first to help with zoom and panning
        pane.setContent(new Group(table));

        pane.setStyle("-fx-background-color: black");
        pane.setPannable(true);

        getChildren().add(pane);

        table.setOnScroll(event -> {
            double scrollValue = event.getDeltaY();
            if (scrollValue < 0){
                zoomOut();
            } else if (scrollValue > 0){
                zoomIn();
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

        Utilities.attach(pane, widthProperty(), heightProperty());


    }

    public void setTablePosition(double vValue, double hValue){
        pane.setHvalue(hValue);
        pane.setVvalue(vValue);
    }

    public StackPane getTable() {
        return table;
    }

    public ScrollPane getPane() {
        return pane;
    }

    public Puzzle getPuzzle(){
        return puzzle;
    }

    public void zoomOut() {
        table.setScaleX(table.getScaleX()-0.01);
        table.setScaleY(table.getScaleY()-0.01);
    }

    public void zoomIn() {
        table.setScaleX(table.getScaleX()+0.01);
        table.setScaleY(table.getScaleY()+0.01);
    }

    public void setNewPuzzle(Image chosenImage, int nbPieces) {
        table.getChildren().clear();
        puzzle = new Puzzle(chosenImage, nbPieces, this);

        table.setMinHeight(chosenImage.getHeight() * 4);
        table.setMinWidth(chosenImage.getWidth() * 4);

        Random rand = new Random();
        for (PuzzlePiece pf : puzzle.getPieces()){
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

        pane.layout();

        setTablePosition(0.5, 0.5);
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

    public DoubleProperty getZoom() {
        return table.scaleXProperty();
    }
}
