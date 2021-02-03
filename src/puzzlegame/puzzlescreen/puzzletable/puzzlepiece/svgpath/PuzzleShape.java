package puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath;

import javafx.scene.shape.SVGPath;
import puzzlegame.puzzlescreen.factors.Factor;
import puzzlegame.puzzlescreen.puzzletable.Size;

class PuzzleShape {
    private final LineDrawer topPath;
    private final LineDrawer bottomPath;
    private final LineDrawer rightPath;
    private final LineDrawer leftPath;

    private final Size size;

    public PuzzleShape(int x, int y, Factor fact, Size size, PuzzleShape[][] pieceShapes) {
        this.size = size;
        // top
        if (y == 0) {
            topPath = new StraightLineDrawer(size, LineDrawer.Direction.HORIZONTAL);  //draw a straight line;
        } else {
            topPath = pieceShapes[x][y - 1].getBottomPath(); // take the bottom path of the piece above
        }

        // bottom
        if (y < fact.getY() - 1) {
            bottomPath = new VLineDrawer(size, LineDrawer.Direction.HORIZONTAL); // TODO draw the bottom line
        } else {
            bottomPath = new StraightLineDrawer(size, LineDrawer.Direction.HORIZONTAL); // draw a straight line
        }

        // left
        if (x == 0) {
            leftPath = new StraightLineDrawer(size, LineDrawer.Direction.VERTICAL); // draw a straight line
        } else {
            leftPath = pieceShapes[x - 1][y].getRightPath(); // take the right path of the piece to the left
        }

        // right
        if (x < fact.getX() - 1) {
            rightPath = new VLineDrawer(size, LineDrawer.Direction.VERTICAL); // TODO draw the right line
        } else {
            rightPath = new StraightLineDrawer(size, LineDrawer.Direction.VERTICAL); // draw a straight line
        }

    }

    public LineDrawer getTopPath() {
        return topPath;
    }

    public LineDrawer getBottomPath() {
        return bottomPath;
    }

    public LineDrawer getRightPath() {
        return rightPath;
    }

    public LineDrawer getLeftPath() {
        return leftPath;
    }

    public SVGPath generatePath() {
        SVGPath path = new SVGPath();
        String svgPath = "M 0 0 " +
                topPath.getLineText(size, LineDrawer.Side.TOP) + " " +
                rightPath.getLineText(size, LineDrawer.Side.RIGHT) + " " +
                bottomPath.getLineText(size, LineDrawer.Side.BOTTOM) + " " +
                leftPath.getLineText(size, LineDrawer.Side.LEFT) + " " +
                "z";
        path.setContent(svgPath);
        return path;
    }
}