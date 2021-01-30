package puzzlegame.puzzlescreen.puzzletable.puzzlepiece;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReverseSVGPathTest {

    @Test
    public void testReverse(){

        ReverseSVGPath rp = new ReverseSVGPath();

        String result = rp.inverse("M 50 300 L 50 250 C 50 150 75 150 100 250 C 150 450 200 450 200 250 Q 200 100 400 100");
        String expected = "M 400 100 Q 200 100 200 250 C 200 450 150 450 100 250 C 75 150 50 150 50 250 L 50 300";
        assertEquals(expected, result);

    }

}