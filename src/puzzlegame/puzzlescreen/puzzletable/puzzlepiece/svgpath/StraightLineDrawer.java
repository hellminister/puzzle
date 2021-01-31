package puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath;

import puzzlegame.puzzlescreen.puzzletable.Size;

public class StraightLineDrawer extends LineDrawer{
    public StraightLineDrawer(Size size, Direction direction) {
        super(size, direction);
        commands.add(new Command(Operators.M, new SVGPoint(0,0)));
        switch (direction){
            case VERTICAL -> commands.add(new Command(Operators.L, new SVGPoint(0, size.y())));
            case HORIZONTAL -> commands.add(new Command(Operators.L, new SVGPoint(size.x(), 0)));
        }
    }
}
