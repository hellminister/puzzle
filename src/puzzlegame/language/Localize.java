package puzzlegame.language;

import javafx.beans.property.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Singleton class containing the texts to show
 * This is to reduce the need to pass objects through object that otherwise wouldn't need to see the parent calls
 */
public final class Localize {
    private static final Logger LOG = Logger.getLogger(Localize.class.getName());

    private static final Localize localize = new Localize();

    public static ReadOnlyStringProperty get(Target target){
        return localize.getString(target);
    }

    public static void load(String language){
        localize.loadLanguage(language);
    }

    public static String getCurrentLanguage(){
        return localize.getLanguage();
    }

    private final Map<Target, StringProperty> localizedStrings;
    private String language;

    private Localize() {
        EnumMap<Target, StringProperty> temp = new EnumMap<>(Target.class);
        for (Target t : Target.values()){
            temp.put(t, new SimpleStringProperty(""));
        }

        localizedStrings = Collections.unmodifiableMap(temp);
    }

    private ReadOnlyStringProperty getString(Target target){
        return  localizedStrings.get(target);
    }

    private void loadLanguage(String language){
        this.language = language;
        EnumSet<Target> notLoaded = EnumSet.allOf(Target.class);
        try (BufferedReader br = Files.newBufferedReader(Paths.get("resources/language/" + language + ".txt"))) {
            String line = br.readLine();

            while(line != null){
                if (!line.isBlank() && !line.startsWith("#")){
                    String[] text = line.split(" ", 2);

                    Target key = Target.valueOf(text[0]);
                    localizedStrings.get(key).setValue(text[1]);
                    notLoaded.remove(key);
                }

                line = br.readLine();
            }

            if (!notLoaded.isEmpty()){
                LOG.severe(() -> "Missing localization of these entries : " + notLoaded.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getLanguage(){
        return language;
    }

    public enum Target{
        GAME_TITLE,
        START_SCREEN_CONTINUE,
        START_SCREEN_START_NEW,
        START_SCREEN_SETTINGS,
        START_SCREEN_QUIT,
        PUZZLE_SCENE_ZOOM,
        PUZZLE_SCENE_SHOW_IMAGE,
        VICTORY_PANE_NEW_PUZZLE,
        VICTORY_PANE_BACK_TO_MAIN,
        PUZZLE_CHOOSER_SCENE_CHOOSE_IMAGE,
        PUZZLE_CHOOSER_SCENE_NB_PIECE_TEXT,
        PUZZLE_CHOOSER_SCENE_START,
        PUZZLE_CHOOSER_SCENE_CANCEL,
        SETTINGS_SCENE_LANGUAGE,
        SETTINGS_SCENE_ACCEPT_BUTTON,
        SETTINGS_SCENE_CANCEL_BUTTON,
        SETTINGS_SCENE_APPLY_BUTTON,
        PUZZLE_SCENE_SHOW_MINIMAP

    }
}