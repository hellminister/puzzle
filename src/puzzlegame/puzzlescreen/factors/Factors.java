package puzzlegame.puzzlescreen.factors;

import java.util.*;

/**
 * Contains all the factor pairs for a given number
 */
public class Factors {

    /**
     * the factored number
     */
    private final int product;

    /**
     * the list of paired factors
     */
    private final List<Factor> factors;

    public Factors(int value){
        product = value;
        factors = List.copyOf(generateFactors(value));
    }

    /**
     * Finds all the factors pair for the given number
     * @param value the number to factor
     * @return the list of factors
     */
    private SortedSet<Factor> generateFactors(int value) {
        SortedSet<Factor> facts = new TreeSet<>();

        int second = value;
        for (int i = 1; i <= second; i++){
            second = value / i;
            if (value % i == 0){
                facts.add(new Factor(i, second));
                if (second != i){
                    facts.add(new Factor(second, i));
                }
            }
        }
        return facts;
    }

    /**
     * @return the list of paired factors
     */
    public List<Factor> getFactors() {
        return factors;
    }

    /**
     * @return The factored number for this object
     */
    public int getForProduct(){
        return product;
    }

    /**
     * Finds the paired factors that produces a ratio nearest to the given ration
     * @param ratio the wanted ratio
     * @return Factor pair giving the nearest ratio
     */
    public Factor nearestRatioTo(double ratio){

        double previousDeltaRatio = Double.MAX_VALUE;
        double deltaRatio;
        Factor nearest = null;

        for (Factor fact : factors){
            deltaRatio = Math.abs(ratio - fact.getRatio());
            if (deltaRatio < previousDeltaRatio){
                previousDeltaRatio = deltaRatio;
                nearest = fact;
            } else {
                break;
            }
        }

        return nearest;

    }

}