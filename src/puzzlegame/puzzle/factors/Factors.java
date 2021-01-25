package puzzlegame.puzzle.factors;

import java.util.*;

public class Factors {

    private final int product;
    private final List<Factor> factors;

    public Factors(int value){
        product = value;
        factors = List.copyOf(generateFactors(value));
    }

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

    public List<Factor> getFactors() {
        return factors;
    }

    public int getForProduct(){
        return product;
    }

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
