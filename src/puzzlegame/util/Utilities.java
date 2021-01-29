package puzzlegame.util;

import javafx.beans.binding.DoubleExpression;
import javafx.scene.layout.Region;

/**
 * Simple utilities class containing some utilities methods
 */
public final class Utilities {

    /**
     * to prevent initialization of the class
     */
    private Utilities(){}

    /**
     * Binds the given region's size to the given values
     * @param pane   The region to bind
     * @param width  The region's wanted width
     * @param height The region's wanted height
     */
    public static void attach(Region pane, DoubleExpression width, DoubleExpression height){
        pane.maxWidthProperty().bind(width);
        pane.minWidthProperty().bind(width);
        pane.prefWidthProperty().bind(width);

        pane.maxHeightProperty().bind(height);
        pane.minHeightProperty().bind(height);
        pane.prefHeightProperty().bind(height);
    }
}
