package puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath;

import puzzlegame.puzzlescreen.puzzletable.Size;

import java.util.Random;

public class VLineDrawer extends LineDrawer{

    private static final Random rand = new Random();


    public VLineDrawer(Size size, Direction direction) {
        super(size, direction);
        commands.add(new Command(Operators.M, new SVGPoint(0,0)));

        double range = perpendicularSize(size, direction) * 0.2;
        double vPoint = rand.nextDouble() * (range * 2) - range;
        double position = 0.1 + rand.nextDouble() * 0.8;

        commands.add(new Command(Operators.L, markPoint(vPoint, parallelSize(size, direction) * position, direction)));
        commands.add(new Command(Operators.L, markPoint(0, parallelSize(size, direction), direction)));
    }


}