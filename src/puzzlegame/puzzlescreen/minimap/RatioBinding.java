package puzzlegame.puzzlescreen.minimap;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.StackPane;

class RatioBinding extends DoubleBinding {

    private final DoubleBinding heightRatio;
    private final DoubleBinding widthRatio;

    public RatioBinding(StackPane table, ReadOnlyDoubleProperty maxWidth, ReadOnlyDoubleProperty maxHeight) {
        bind(table.heightProperty(), table.widthProperty(), maxWidth, maxHeight);

        heightRatio = table.heightProperty().divide(maxHeight);
        widthRatio = table.widthProperty().divide(maxWidth);
    }

    /**
     * @return the current value
     */
    @Override
    protected double computeValue() {
        return Math.max(heightRatio.get(), widthRatio.get());
    }
}
