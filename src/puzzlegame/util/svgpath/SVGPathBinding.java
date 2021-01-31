package puzzlegame.util.svgpath;

import javafx.beans.binding.StringBinding;
import puzzlegame.puzzlescreen.minimap.RatioBinding;
import puzzlegame.puzzlescreen.puzzletable.puzzlepiece.LineDrawer;

import java.util.LinkedList;

public class SVGPathBinding extends StringBinding {

    private final SVGPathUtilities.Result path;
    private final RatioBinding ratio;

    public SVGPathBinding(SVGPathUtilities.Result parsed, RatioBinding ratio){
        super();
        bind(ratio);
        path = parsed;
        this.ratio = ratio;
    }

    /**
     * Calculates the current value of this binding.
     * <p>
     * Classes extending {@code StringBinding} have to provide an implementation
     * of {@code computeValue}.
     *
     * @return the current value
     */
    @Override
    protected String computeValue() {
        LinkedList<LineDrawer.Point> newPoints = new LinkedList<>();

        for (LineDrawer.Point point : path.points()){
            newPoints.add(new LineDrawer.Point(point.x() / ratio.get(), point.y() / ratio.get()));
        }

        return SVGPathUtilities.generate(new SVGPathUtilities.Result(path.operators(), newPoints));
    }
}
