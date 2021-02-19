package puzzlegame.chooserdialog;

import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import puzzlegame.PuzzleMain;

/**
 * The dialog window to set up the new puzzle
 */
public class PuzzleChooserDialog extends Stage {

    /**
     * The main window
     */
    private final PuzzleMain puzzleGame;

    public PuzzleChooserDialog(PuzzleMain mainWindow){
        super();
        puzzleGame = mainWindow;
        initOwner(mainWindow.getPrimaryStage());
        initModality(Modality.WINDOW_MODAL);
        setScene(new PuzzleChooserScene(this));

    }

    /**
     * transfers the obtained information to the puzzle table
     * @param chosenImage The image to make as a puzzle
     * @param nbPieces    The number of pieces for the puzzle
     * @param imageFileName The file path of the image
     */
    public void sendInfoToPuzzleTable(Image chosenImage, int nbPieces, String imageFileName) {
        puzzleGame.sendInfoToPuzzleTable(chosenImage, nbPieces, imageFileName);
    }

    /**
     * Tells the main window to switch to the puzzle table
     */
    public void mainWindowSwitchToTable() {
        puzzleGame.switchToPuzzleTable();
    }
}