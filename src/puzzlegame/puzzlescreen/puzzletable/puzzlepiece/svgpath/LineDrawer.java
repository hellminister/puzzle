package puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath;

import puzzlegame.puzzlescreen.puzzletable.Size;

import java.util.*;

public abstract class LineDrawer {

    protected List<Command> commands;

    public LineDrawer(Size size, Direction direction){
        commands = new ArrayList<>();
    }

    public final String getLineText(Size size, Side side){
        StringBuilder commandline = new StringBuilder();

        for(Command command : commands){
            commandline.append(command.adjustedCommand(size, side)).append(" ");
        }

        String result = commandline.toString().strip();

        if (side.isInvert()){
            result = SVGPathUtilities.inverse(result);
        }

        return stripM(result);
    }

    protected final SVGPoint markPoint(double xy, double yx, Direction dir){
        return switch (dir){
            case VERTICAL -> new SVGPoint(xy, yx);
            case HORIZONTAL -> new SVGPoint(yx, xy);
        };
    }

    protected final double parallelSize(Size size, Direction dir){
        return (switch (dir){
            case VERTICAL -> size.y();
            case HORIZONTAL -> size.x();
        });
    }

    protected final double perpendicularSize(Size size, Direction dir){
        return (switch (dir){
            case VERTICAL -> size.x();
            case HORIZONTAL -> size.y();
        });
    }

    private String stripM(String path){
        return path.replaceAll("^(M|m)( |[0-9]|\\.)*", "");
    }

    public enum Direction{
        VERTICAL,
        HORIZONTAL
    }

    public enum Side{
        TOP(0, 0, false),
        BOTTOM(0, 1, true),
        LEFT(0, 0, true),
        RIGHT(1, 0, false);

        private final int adjustX;
        private final int adjustY;
        private final boolean invert;

        Side(int x, int y, boolean inv){
            adjustX = x;
            adjustY = y;
            invert = inv;
        }

        public double adjustXValue(Size size){
            return size.x() * adjustX;
        }

        public double adjustYValue(Size size){
            return size.y() * adjustY;
        }

        public boolean isInvert() {
            return invert;
        }
    }

    protected static class Command{
        private final Operators command;
        private final List<SVGPoint> points;

        public Command(Operators command, SVGPoint... points){
            this.command = command;
            this.points = (Arrays.asList(points));
        }

        public String adjustedCommand(Size size, Side side){
            StringBuilder commandline = new StringBuilder(command.name());

            for (SVGPoint point : points){
                commandline.append(" ").append(point.x() + side.adjustXValue(size)).append(" ").append(point.y() + side.adjustYValue(size));
            }

            return commandline.toString();
        }

    }

}