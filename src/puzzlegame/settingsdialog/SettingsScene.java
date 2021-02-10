package puzzlegame.settingsdialog;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import puzzlegame.language.Localize;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.logging.Logger.getLogger;

public class SettingsScene extends Scene {

    private static final  Path LANGUAGE_DIRECTORY = Paths.get("resources/language");
    private static final Logger LOG = getLogger(SettingsScene.class.getName());

    private final ChoiceBox<String> languageChoices;


    public SettingsScene(SettingsDialog settingsDialog) {
        super(new StackPane());

        var root = (StackPane) getRoot();

        var settingsPane = new VBox();
        root.getChildren().add(settingsPane);

        var languageSetting = new HBox();
        languageSetting.setAlignment(Pos.CENTER);
        Label languageLabel = new Label();
        languageLabel.textProperty().bind(Localize.get(Localize.Target.SETTINGS_SCENE_LANGUAGE));
        languageChoices = new ChoiceBox<>();
        loadLanguageChoices();

        languageSetting.getChildren().addAll(languageLabel, languageChoices);

        var buttons = new HBox();

        Button accept = new Button();
        accept.textProperty().bind(Localize.get(Localize.Target.SETTINGS_SCENE_ACCEPT_BUTTON));
        accept.setOnAction(event -> {
            apply();
            settingsDialog.close();
        });

        Button cancel = new Button();
        cancel.textProperty().bind(Localize.get(Localize.Target.SETTINGS_SCENE_CANCEL_BUTTON));
        cancel.setOnAction(event -> settingsDialog.close());

        Button apply = new Button();
        apply.textProperty().bind(Localize.get(Localize.Target.SETTINGS_SCENE_APPLY_BUTTON));
        apply.setOnAction(event -> apply());

        buttons.getChildren().addAll(accept, cancel, apply);

        settingsPane.getChildren().addAll(languageSetting, buttons);

    }

    private void apply() {
        String language = languageChoices.getValue();
        Localize.load(language);
    }

    private void loadLanguageChoices(){
        List<String> languages = new LinkedList<>();
        try (Stream<Path> walk = Files.walk(LANGUAGE_DIRECTORY, 1)){
            languages = walk.filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .map(s -> s.replace(".txt", "").strip())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            LOG.severe(e::toString);
        }
        languageChoices.setItems(FXCollections.observableList(languages));
    }

    public void update(){
        languageChoices.setValue(Localize.getCurrentLanguage());
    }
}