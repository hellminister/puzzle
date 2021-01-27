package puzzlegame.chooserdialog;

import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import puzzlegame.PuzzleMain;

public class PuzzleChooserDialog extends Stage {

    private final PuzzleMain puzzleGame;


    public PuzzleChooserDialog(PuzzleMain mainWindow){
        puzzleGame = mainWindow;
        initOwner(mainWindow.getPrimaryStage());
        initModality(Modality.WINDOW_MODAL);
        setScene(new PuzzleChooserScene(this));

    }

    public void sendInfoToPuzzleTable(Image chosenImage, int nbPieces) {
        puzzleGame.sendInfoToPuzzleTable(chosenImage, nbPieces);
    }

    public void mainWindowSwitchToTable() {
        puzzleGame.switchToPuzzleTable();
    }
}
