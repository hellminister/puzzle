package puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath;

import puzzlegame.puzzlescreen.puzzletable.Size;

import java.util.Random;

public class VLineDrawer extends LineDrawer{

    private static final Random rand = new Random();


    public VLineDrawer(Size size, Direction direction) {
        super(size, direction);
        commands.add(new Command(Operators.M, new SVGPoint(0,0)));


        switch (direction){
            case VERTICAL -> {
                double range = size.x() / 5;
                double x = rand.nextDouble() * (range * 2) - range;
                double position = 0.1 + rand.nextDouble() * 0.8;

                commands.add(new Command(Operators.L, new SVGPoint(x, size.y() * position)));
                commands.add(new Command(Operators.L, new SVGPoint(0, size.y())));
            }
            case HORIZONTAL -> {
                double range = size.y() / 5;
                double y = rand.nextDouble() * (range * 2) - range;
                double position = 0.1 + rand.nextDouble() * 0.8;

                commands.add(new Command(Operators.L, new SVGPoint(size.x() * position, y)));
                commands.add(new Command(Operators.L, new SVGPoint(size.x(), 0)));
            }
        }
    }
}
