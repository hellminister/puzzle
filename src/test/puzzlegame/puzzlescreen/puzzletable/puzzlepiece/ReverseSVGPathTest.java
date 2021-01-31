package puzzlegame.puzzlescreen.puzzletable.puzzlepiece;

import org.junit.jupiter.api.Test;
import puzzlegame.util.svgpath.SVGPathUtilities;

import static org.junit.jupiter.api.Assertions.*;

class ReverseSVGPathTest {

    @Test
    public void testReverse(){

        String result = SVGPathUtilities.inverse("M 50 300 L 50 250 C 50 150 75 150 100 250 C 150 450 200 450 200 250 Q 200 100 400 100");
        String expected = "M 400.0 100.0 Q 200.0 100.0 200.0 250.0 C 200.0 450.0 150.0 450.0 100.0 250.0 C 75.0 150.0 50.0 150.0 50.0 250.0 L 50.0 300.0";
        assertEquals(expected, result);

    }

}