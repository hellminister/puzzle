package puzzlegame.puzzlescreen.puzzletable;

import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.shape.SVGPath;
import puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath.PuzzleMaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static puzzlegame.util.Utilities.getNextLine;

/**
 * Represents the puzzle
 * contains informations like the its pieces, the formed fragments, its finished state and a link to the table its on
 */
public class Puzzle {

    /**
     * list of formed fragments
     */
    private final ObservableList<PuzzleFragment> fragments;

    /**
     * List of the puzzle's pieces
     * Should be unmodifiable
     */
    private final List<PuzzlePiece> pieces;

    /**
     * link to the table it is on
     */
    private final PuzzleTable onTable;

    /**
     * State of completion
     */
    private final BooleanBinding finished;

    /**
     * The file path to the chosen image
     */
    private final String imageURL;

    /**
     * Creates the puzzle from the given image
     * @param image The image of the puzzle
     * @param numberOfPieces The number of pieces of the puzzle
     * @param imageFileName The file path of the image
     * @param puzzleTable The table it will be on
     */
    public Puzzle(Image image, int numberOfPieces, String imageFileName, PuzzleTable puzzleTable){
        fragments = FXCollections.observableArrayList();
        var piecesTemp = new ArrayList<PuzzlePiece>();
        onTable = puzzleTable;

        finished = new FinishedPuzzle(fragments);

        imageURL = imageFileName;

        var cutPieces = PuzzleMaker.makePuzzle(image, numberOfPieces);

        for (PuzzlePiece piece : cutPieces){
            if (!piece.isInvisible()) {
                    piecesTemp.add(piece);
                    fragments.add(new PuzzleFragment(piece, this));
                }
        }
        pieces = Collections.unmodifiableList(piecesTemp);
    }

    /**
     * Constructor used when loading a puzzle from a file
     * @param image the puzzle image
     * @param imagePath the puzzle image file path
     * @param br The file reader
     * @param puzzleTable the puzzle table where the puzzle will be
     * @throws IOException if a problem occurs with the file
     * @throws IllegalStateException if there is a malformed problem in the file
     */
    public Puzzle(Image image, String imagePath, BufferedReader br, PuzzleTable puzzleTable) throws IOException, IllegalStateException {
        fragments = FXCollections.observableArrayList();
        var piecesTemp = new ArrayList<PuzzlePiece>();
        onTable = puzzleTable;
        finished = new FinishedPuzzle(fragments);

        imageURL = imagePath;

        String line = getNextLine(br);
        PuzzleFragment fragment = null;
        PuzzlePiece piece;
        while (line != null){
            switch (line) {
                case "Fragment" -> {
                    fragment = new PuzzleFragment(this);
                    fragments.add(fragment);
                }
                case "Piece" -> {
                    piece = readPiece(image, br);
                    // if it is null then theres a problem in the loaded file, so throw the exception...
                    fragment.addPuzzlePiece(piece);
                    piecesTemp.add(piece);

                }
                default -> throw new IllegalStateException("Unexpected value: " + line);
            }

            line = getNextLine(br);
        }


        pieces = Collections.unmodifiableList(piecesTemp);
    }

    /**
     * Reads and creates a puzzle piece
     * @param image the puzzle image
     * @param br the buffered reader loading the puzzle
     * @return The recreated puzzle piece
     * @throws IOException if theres a problem reading the file
     */
    private PuzzlePiece readPiece(Image image, BufferedReader br) throws IOException {
        String line = getNextLine(br).strip();
        String[] splitted = line.split(" ");
        Position pos = new Position(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]));

        line = getNextLine(br).strip();
        splitted = line.split(" ");
        Size size = new Size(Double.parseDouble(splitted[0]), Double.parseDouble(splitted[1]));

        line = getNextLine(br).strip();
        SVGPath svgpath = new SVGPath();
        svgpath.setContent(line);

        line = getNextLine(br).strip();
        splitted = line.split(" ");
        return new PuzzlePiece(image, size, pos, svgpath, Double.parseDouble(splitted[0]), Double.parseDouble(splitted[1]));
    }

    /**
     * @return the list of the puzzle pieces (should be unmodifiable)
     */
    public List<PuzzlePiece> getPieces() {
        return pieces;
    }

    /**
     * Verifies if the given fragment can be connected to a fragment in the puzzle
     * @param puzzleFragment The fragment to connect
     */
    public boolean checkConnections(PuzzleFragment puzzleFragment) {
        Optional<Delta> delta = Optional.empty();
        for (PuzzleFragment fragment : fragments){
            if (fragment != puzzleFragment){
                delta = fragment.connectsWith(puzzleFragment);
                if (delta.isPresent()){
                    fragment.insertFragment(puzzleFragment, delta.get());
                    break;
                }
            }
        }
        if (delta.isPresent()){
            fragments.remove(puzzleFragment);
        }
        return delta.isPresent();
    }

    /**
     * @return The table the puzzle is on
     */
    public PuzzleTable getTable() {
        return onTable;
    }

    /**
     * @return a property for the completion of the puzzle
     */
    public BooleanBinding finished() {
        return finished;
    }

    /**
     * Generates a string that can be used to recreate the current state of this puzzle
     * @return the description string of this puzzle
     */
    public String save() {
        StringBuilder save = new StringBuilder();

        save.append(imageURL).append("\n");

        for (PuzzleFragment fragment: fragments) {
            save.append(fragment.save()).append("\n");
        }

        return save.toString();
    }

    /**
     * A binding to give the completion state of the puzzle
     */
    private static class FinishedPuzzle extends BooleanBinding{

        private final ObservableList<PuzzleFragment> puzzle;

        public FinishedPuzzle(ObservableList<PuzzleFragment> puzzle){
            bind(puzzle);
            this.puzzle = puzzle;
        }


        @Override
        protected boolean computeValue() {
            // considers 0 as complete for an empty puzzle, should normally be 1 though
            return puzzle.size() <= 1;
        }
    }
}