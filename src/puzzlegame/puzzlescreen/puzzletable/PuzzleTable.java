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

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import static puzzlegame.util.Utilities.getNextLine;

/**
 * The main game pane
 */
public class PuzzleTable extends AnchorPane {

    /**
     * the table where the puzzle pieces are
     */
    private final StackPane table;

    /**
     * the viewport on the table
     */
    private final ScrollPane pane;

    /**
     * the current puzzle
     */
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

    /**
     * Sets the viewport position on the table
     * @param vValue the viewport vertical position from 0 to 1
     * @param hValue the viewport horizontal position from 0 to 1
     */
    public void setTablePosition(double vValue, double hValue){
        pane.setHvalue(hValue);
        pane.setVvalue(vValue);
    }

    /**
     * @return the puzzle table
     */
    public StackPane getTable() {
        return table;
    }

    /**
     * @return the viewport on the table
     */
    public ScrollPane getPane() {
        return pane;
    }

    /**
     * @return the current puzzle
     */
    public Puzzle getPuzzle(){
        return puzzle;
    }

    /**
     * zooms out (see more of the table)
     */
    public void zoomOut() {
        table.setScaleX(table.getScaleX()-0.01);
        table.setScaleY(table.getScaleY()-0.01);
    }

    /**
     * zooms in (see less of the table)
     */
    public void zoomIn() {
        table.setScaleX(table.getScaleX()+0.01);
        table.setScaleY(table.getScaleY()+0.01);
    }

    /**
     * Creates and sets the new puzzle to do
     * @param chosenImage The image for the puzzle
     * @param nbPieces    The number of pieces the puzzle should have
     * @param imageFileName The file path of the image
     */
    public void setNewPuzzle(Image chosenImage, int nbPieces, String imageFileName) {
        table.getChildren().clear();
        puzzle = new Puzzle(chosenImage, nbPieces, imageFileName, this);

        // the table is at least 4 times the size of the image
        table.setMinHeight(chosenImage.getHeight() * 4);
        table.setMinWidth(chosenImage.getWidth() * 4);

        // disperse the pieces across the table
        Random rand = new Random();
        int dispersionX = (int) chosenImage.getWidth()*2;
        int dispersionY = (int) chosenImage.getHeight()*2;
        for (PuzzlePiece pf : puzzle.getPieces()){
            pf.setTranslateX(rand.nextInt(dispersionX) - dispersionX/2);
            pf.setTranslateY(rand.nextInt(dispersionY) - dispersionY/2);

            table.getChildren().add(pf);
        }

        // makes sure the size of the table is at least the size of the viewport
        if (table.getMinWidth() < pane.getWidth()){
            table.setMinWidth(pane.getWidth());
        }

        if (table.getMinHeight() < pane.getHeight()){
            table.setMinHeight(pane.getHeight());
        }

        // places the viewport at the center of the table
        pane.layout();
        setTablePosition(0.5, 0.5);
    }


    /**
     * Loads and sets the given puzzle saved file
     * @param filename the puzzle saved file path
     * @return The image of the loaded puzzle
     */
    public Image loadSavedPuzzle(Path filename){
        table.getChildren().clear();
        Image image = null;
        try (BufferedReader br = Files.newBufferedReader(filename)){

            reloadTable(br);
            String imagePath = getNextLine(br);
            Path path = Paths.get(imagePath);
            image = new Image(path.toUri().toURL().toExternalForm());

            puzzle = new Puzzle(image, imagePath, br, this);

            for (PuzzlePiece pf : puzzle.getPieces()){
                table.getChildren().add(pf);
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        return image;
    }

    /**
     * Sets the tables size and position as dictated by the saved file
     * @param br The saved file Reader
     * @throws IOException if something bad happens
     */
    private void reloadTable(BufferedReader br) throws IOException {
        String line = getNextLine(br);
        double value = Double.parseDouble(line.strip());
        table.setMinWidth(value);

        line = getNextLine(br);
        value = Double.parseDouble(line.strip());
        table.setMinHeight(value);

        line = getNextLine(br);
        value = Double.parseDouble(line.strip());
        table.setScaleX(value);

        line = getNextLine(br);
        value = Double.parseDouble(line.strip());
        table.setScaleY(value);

        line = getNextLine(br);
        value = Double.parseDouble(line.strip());
        pane.setHvalue(value);

        line = getNextLine(br);
        value = Double.parseDouble(line.strip());
        pane.setVvalue(value);

        pane.layout();
    }

    /**
     * resizes the table so a pieces that moves is always on the table
     * @param position the position of the piece on the table
     */
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

    /**
     *
     */
    public String save(){
        String save = "";

        save += /*"Width " +*/ table.getWidth() + "\n";
        save += /*"Height " +*/ table.getHeight() + "\n";
        save += /*"ScaleX " +*/ table.getScaleX() + "\n";
        save += /*"ScaleY " +*/ table.getScaleY() + "\n";
        save += /*"HValue " +*/ pane.getHvalue() + "\n";
        save += /*"VValue " +*/ pane.getVvalue() + "\n";

        save += /*"Puzzle" + "\n" +*/ puzzle.save();

        return save;
    }

    /**
     * @return the zoom value as a property
     */
    public DoubleProperty getZoom() {
        return table.scaleXProperty();
    }
}