package puzzlegame.chooserdialog;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;
import puzzlegame.PuzzleMain;

import java.io.*;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;

public class PuzzleChooserScene extends Scene {


    private Image choosenImage = null;
    private IntegerProperty nbPieces;


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

        ImageView preview = new ImageView();
        preview.setPreserveRatio(true);
        preview.setFitHeight(400);
        preview.setFitWidth(400);

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
                    choosenImage = image;
                    preview.setImage(image);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        leftSide.getChildren().addAll(preview, imageChooser);

        TextField nbPieceGetter = new TextField();
        nbPieceGetter.setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), 4, change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([1-9][0-9]*)?")) {
                return change;
            }
            return null;
        }));

        nbPieces.bind((ObservableValue<? extends Number>) nbPieceGetter.getTextFormatter().valueProperty());

        Label text = new Label();
        text.textProperty().bind(nbPieces.asString());


rightSide.getChildren().addAll(nbPieceGetter, text);



        Button start = new Button("Start");
        start.setOnAction(event -> {
            if (choosenImage != null && nbPieces.get() > 1){
                pcd.sendInfoToPuzzleTable(choosenImage, nbPieces.get());
                pcd.mainWindowSwitchToTable();
                pcd.close();
            }
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(event -> pcd.close());
        lowerBox.getChildren().addAll(start, cancel);
    }
}
