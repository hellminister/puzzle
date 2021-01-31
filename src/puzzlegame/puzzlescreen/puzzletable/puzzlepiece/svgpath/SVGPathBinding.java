package puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath;

import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import puzzlegame.puzzlescreen.minimap.RatioBinding;

public class SVGPathBinding extends StringBinding {

    private final SVGPathDescription path;
    private final RatioBinding ratio;

    public SVGPathBinding(SVGPathDescription parsed, RatioBinding ratio){
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
        ObservableList<SVGPoint> newPoints = FXCollections.observableArrayList();

        for (SVGPoint point : path.points()){
            newPoints.add(new SVGPoint(point.x() / ratio.get(), point.y() / ratio.get()));
        }

        return SVGPathUtilities.generate(new SVGPathDescription(path.operators(), newPoints));
    }
}
