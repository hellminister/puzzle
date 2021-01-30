package puzzlegame.puzzlescreen.puzzletable.puzzlepiece;

import puzzlegame.puzzlescreen.puzzletable.Size;

public class VLineDrawer extends LineDrawer{
    public VLineDrawer(Size size, Direction direction) {
        super(size, direction);
        commands.add(new Command("M", new Point(0,0)));
        switch (direction){
            case VERTICAL -> {
                commands.add(new Command("L", new Point(-100, size.y() * 0.3)));
                commands.add(new Command("L", new Point(0, size.y())));
            }
            case HORIZONTAL -> {
                commands.add(new Command("L", new Point(size.x() * 0.7, -50)));
                commands.add(new Command("L", new Point(size.x(), 0)));

            }
        }
    }
}
