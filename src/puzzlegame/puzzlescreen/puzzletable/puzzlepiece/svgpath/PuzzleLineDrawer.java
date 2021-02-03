package puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath;

import puzzlegame.puzzlescreen.puzzletable.Size;

import java.util.Random;

public class PuzzleLineDrawer extends LineDrawer{

    private static final Random rand = new Random();

    public PuzzleLineDrawer(Size size, Direction direction) {
        super(size, direction);

        commands.add(new Command(Operators.M, new SVGPoint(0,0)));

        double range = perpendicularSize(size, direction) * 0.2;
        double startPin = 0.2 + rand.nextDouble() * 0.6;

        // this is horizontal
       // M 0,0 C 10,0 10,10 100,-20 Q 150,-60 100,-100 T150,-150 T200,-100 Q150,-60 250,-20 C300,10 350,0 400,0
        // M 0,0 L100,-20 Q 150,-60 100,-100 T150,-150 T200,-100 Q150,-60 250,-20 L400,0


        // the line from the start of the piece to the start of the dent
        commands.add(new Command(Operators.L, markPoint(0, parallelSize(size, direction) * 0.3, direction)));

        // lower half of the left* half
        commands.add(new Command(Operators.Q, markPoint(perpendicularSize(size, direction) * 0.025, parallelSize(size, direction)* 0.4, direction),
                markPoint(perpendicularSize(size, direction) * 0.05, parallelSize(size, direction)* 0.25, direction)));

        //upper half of the left* half
        commands.add(new Command(Operators.T, markPoint(perpendicularSize(size, direction) * 0.15, parallelSize(size, direction)* 0.35, direction)));

        //upper half of the right* half
        commands.add(new Command(Operators.T, markPoint(perpendicularSize(size, direction) * 0.06, parallelSize(size, direction)* 0.30, direction)));

        // lower half of the right* half
        commands.add(new Command(Operators.Q, markPoint(perpendicularSize(size, direction) * 0.025, parallelSize(size, direction)* 0.3, direction),
                markPoint(0, parallelSize(size, direction)* 0.40, direction)));

        // the line from end of dent to the end of the piece
        commands.add((new Command(Operators.L, markPoint(0, parallelSize(size, direction),direction))));
    }
}