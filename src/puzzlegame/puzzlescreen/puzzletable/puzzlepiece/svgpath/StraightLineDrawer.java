package puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath;

import puzzlegame.puzzlescreen.puzzletable.Size;

public class StraightLineDrawer extends LineDrawer{
    public StraightLineDrawer(Size size, Direction direction) {
        super(size, direction);
        commands.add(new Command(Operators.M, new SVGPoint(0,0)));

        commands.add(new Command(Operators.L, markPoint(0, parallelSize(size, direction), direction)));
    }
}