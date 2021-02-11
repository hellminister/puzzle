package puzzlegame.settingsdialog;

import javafx.stage.Modality;
import javafx.stage.Stage;
import puzzlegame.PuzzleMain;

/**
 * The dialog window giving access to the game's settings
 */
public class SettingsDialog extends Stage {

    /**
     * The scene of this dialog window
     */
    private final SettingsScene scene;

    public SettingsDialog(PuzzleMain mainWindow){
        super();
        initOwner(mainWindow.getPrimaryStage());
        initModality(Modality.WINDOW_MODAL);
        scene = new SettingsScene(this);
        setScene(scene);
    }

    /**
     * updates the scenes data and makes this window visible
     */
    public void showMe(){
        scene.update();
        show();
    }
}