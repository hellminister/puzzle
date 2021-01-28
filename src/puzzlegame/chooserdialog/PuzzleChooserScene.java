package puzzlegame.chooserdialog;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;

import java.io.*;

public class PuzzleChooserScene extends Scene {


    private Image chosenImage = null;
    private final IntegerProperty nbPieces;


    public PuzzleChooserScene(PuzzleChooserDialog pcd) {
        super(new VBox());
        nbPieces = new SimpleIntegerProperty();

        var root = (VBox) getRoot();

        var upperBox = new HBox();
        var lowerBox = new HBox();

        root.getChildren().addAll(upperBox, lowerBox);


        var rightSide = new VBox();
        var leftSide = new VBox();
        upperBox.getChildren().addAll(leftSide, rightSide);
        upperBox.setAlignment(Pos.CENTER);

        ImageView preview = new ImageView();
        preview.setPreserveRatio(true);
        preview.setFitHeight(400);
        preview.setFitWidth(400);
        StackPane previewPane = new StackPane();

        previewPane.setMinSize(406,406);
        previewPane.setMaxSize(406, 406);

        previewPane.getChildren().addAll(preview);

        previewPane.setStyle("-fx-background-color: black;" +
                " -fx-border-color: darkslategrey;" +
                " -fx-border-width: 3");


        var fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.gif", "*.png")
        );
        fc.setSelectedExtensionFilter(fc.getExtensionFilters().get(0));

        Button imageChooser = new Button("Choose Image");
        imageChooser.setOnAction(event -> {
            File file = fc.showOpenDialog(pcd);
            if (file != null){
                try (InputStream is = new FileInputStream(file)) {
                    Image image = new Image(is);
                    chosenImage = image;
                    preview.setImage(image);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        leftSide.getChildren().addAll(previewPane);
        leftSide.setAlignment(Pos.CENTER);

        TextField nbPieceGetter = new TextField();
        nbPieceGetter.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 4, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        }));

        bindingNbPieces(nbPieceGetter);

        Label text = new Label();
        text.textProperty().bind(nbPieces.asString());


        rightSide.getChildren().addAll(imageChooser, new Label("Number of pieces"), nbPieceGetter);
        rightSide.setAlignment(Pos.CENTER);



        Button start = new Button("Start");
        start.setOnAction(event -> {
            if (chosenImage != null && nbPieces.get() > 1){
                pcd.sendInfoToPuzzleTable(chosenImage, nbPieces.get());
                pcd.mainWindowSwitchToTable();
                pcd.close();
            }
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(event -> pcd.close());
        lowerBox.getChildren().addAll(start, cancel);
        lowerBox.setAlignment(Pos.CENTER);
    }

    /**
     * this method is to suppress the unchecked warning we get from the casting
     * @param nbPieceGetter the field to bind
     */
    @SuppressWarnings("unchecked")
    private void bindingNbPieces(TextField nbPieceGetter) {
        nbPieceGetter.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 4, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        }));

        nbPieces.bind((ObservableValue<? extends Number>) nbPieceGetter.getTextFormatter().valueProperty());
    }
}
