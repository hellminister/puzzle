package puzzlegame.puzzle.minimap;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Bounds;

public class ViewportBoundsProperties {

    private final ViewportMinXProperty minXProperty;
    private final ViewportMinYProperty minYProperty;
    private final ViewportHeightProperty heightProperty;
    private final ViewportWidthProperty widthProperty;

    public ViewportBoundsProperties(ObjectProperty<Bounds> paneViewport) {
        minXProperty = new ViewportMinXProperty(paneViewport);
        minYProperty = new ViewportMinYProperty(paneViewport);
        heightProperty = new ViewportHeightProperty(paneViewport);
        widthProperty = new ViewportWidthProperty(paneViewport);
    }

    public ViewportMinXProperty minXProperty() {
        return minXProperty;
    }

    public ViewportMinYProperty minYProperty() {
        return minYProperty;
    }

    public ViewportHeightProperty heightProperty() {
        return heightProperty;
    }

    public ViewportWidthProperty widthProperty() {
        return widthProperty;
    }


    private abstract static class ViewportProperty extends DoubleBinding {

        protected final ObjectProperty<Bounds> viewport;

        public ViewportProperty(ObjectProperty<Bounds> viewportBounds){
            super();
            bind(viewportBounds);
            viewport = viewportBounds;
        }

        protected abstract double getWantedValue();


        @Override
        protected double computeValue() {
            System.out.println(getClass() + " " + getWantedValue());
            System.out.println(viewport.get());
            return getWantedValue();
        }
    }

    public static class ViewportMinXProperty extends ViewportProperty{

        public ViewportMinXProperty(ObjectProperty<Bounds> viewportBounds) {
            super(viewportBounds);
        }

        @Override
        protected double getWantedValue() {
            return viewport.getValue().getMinX();
        }
    }

    public static class ViewportMinYProperty extends ViewportProperty{

        public ViewportMinYProperty(ObjectProperty<Bounds> viewportBounds) {
            super(viewportBounds);
        }

        @Override
        protected double getWantedValue() {
            return viewport.getValue().getMinY();
        }
    }

    public static class ViewportHeightProperty extends ViewportProperty{

        public ViewportHeightProperty(ObjectProperty<Bounds> viewportBounds) {
            super(viewportBounds);
        }

        @Override
        protected double getWantedValue() {
            return viewport.getValue().getHeight();
        }
    }

    public static class ViewportWidthProperty extends ViewportProperty{

        public ViewportWidthProperty(ObjectProperty<Bounds> viewportBounds) {
            super(viewportBounds);
        }

        @Override
        protected double getWantedValue() {
            return viewport.getValue().getWidth();
        }
    }


}
