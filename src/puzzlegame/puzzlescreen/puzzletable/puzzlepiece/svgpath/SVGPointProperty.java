package puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleDoubleProperty;

public class SVGPointProperty extends ObjectPropertyBase<SVGPointProperty> {

    private final DoubleProperty x;
    private final DoubleProperty y;

    public SVGPointProperty(double x, double y) {
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);

        this.x.addListener((observable, oldValue, newValue) -> this.fireValueChangedEvent());
        this.y.addListener((observable, oldValue, newValue) -> this.fireValueChangedEvent());
    }


    /**
     * Returns the {@code Object} that contains this property. If this property
     * is not contained in an {@code Object}, {@code null} is returned.
     *
     * @return the containing {@code Object} or {@code null}
     */
    @Override
    public Object getBean() {
        return null;
    }

    /**
     * Returns the name of this property. If the property does not have a name,
     * this method returns an empty {@code String}.
     *
     * @return the name or an empty {@code String}
     */
    @Override
    public String getName() {
        return "";
    }
}