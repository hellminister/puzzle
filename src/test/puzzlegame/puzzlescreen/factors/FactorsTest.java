package puzzlegame.puzzlescreen.factors;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.List;


class FactorsTest {

    @Test
    void generateFactors() {
        Factors factorsOf1 = new Factors(1);
        var factors = factorsOf1.getFactors();
        var expected = List.of(new Factor(1, 1));

        assertEquals(expected.toString(), factors.toString());

        Factors factorsOf0 = new Factors(0);
        factors = factorsOf0.getFactors();
        expected = List.of();

        assertEquals(expected.toString(), factors.toString());

        Factors factorsOf10 = new Factors(10);
        factors = factorsOf10.getFactors();
        expected = List.of(new Factor(1, 10), new Factor(2, 5), new Factor(5, 2), new Factor(10, 1));

        assertEquals(expected.toString(), factors.toString());

        Factors factorsOf100 = new Factors(100);
        factors = factorsOf100.getFactors();
        expected = List.of(new Factor(1, 100), new Factor(2, 50), new Factor(4, 25), new Factor(5, 20), new Factor(10, 10), new Factor(20, 5), new Factor(25, 4), new Factor(50,
                2), new Factor(100, 1));

        assertEquals(expected.toString(), factors.toString());

    }

    @Test
    void nearestRatioTo(){

    }
}