package puzzlegame.settingsdialog;

import javafx.stage.Modality;
import javafx.stage.Stage;
import puzzlegame.PuzzleMain;

public class SettingsDialog extends Stage {
    private final PuzzleMain puzzleGame;
    private final SettingsScene scene;


    public SettingsDialog(PuzzleMain mainWindow){
        super();
        puzzleGame = mainWindow;
        initOwner(mainWindow.getPrimaryStage());
        initModality(Modality.WINDOW_MODAL);
        scene = new SettingsScene(this);
        setScene(scene);
    }

    public void showMe(){
        scene.update();
        show();
    }
}