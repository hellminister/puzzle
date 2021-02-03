package puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath;

import java.util.Arrays;
import java.util.Set;

@SuppressWarnings("unused")
enum Operators {
    M(1, points -> "M " + points[0].x() + " " + points[0].y()),
    m(1, points -> "m " + points[0].x() + " " + points[0].y()),
    L(1, points -> "L " + points[0].x() + " " + points[0].y()),
    l(1, points -> "l " + points[0].x() + " " + points[0].y()),
    H(1, points -> "H " + points[0].x()),
    h(1, points -> "h " + points[0].x()),
    V(1, points -> "V " + points[0].x()),
    v(1, points -> "v " + points[0].x()),
    C(3, points -> "C " + points[0].x() + " " + points[0].y() + " " + points[1].x() + " " + points[1].y() + " " + points[2].x() + " " + points[2].y()),
    c(3, points -> "c " + points[0].x() + " " + points[0].y() + " " + points[1].x() + " " + points[1].y() + " " + points[2].x() + " " + points[2].y()),
    S(2, points -> "S " + points[0].x() + " " + points[0].y() + " " + points[1].x() + " " + points[1].y()),
    s(2, points -> "s " + points[0].x() + " " + points[0].y() + " " + points[1].x() + " " + points[1].y()),
    Q(2, points -> "Q " + points[0].x() + " " + points[0].y() + " " + points[1].x() + " " + points[1].y()),
    q(2, points -> "q " + points[0].x() + " " + points[0].y() + " " + points[1].x() + " " + points[1].y()),
    T(1, points -> "T " + points[0].x() + " " + points[0].y()),
    t(1, points -> "t " + points[0].x() + " " + points[0].y()),
    Z(0, points -> "Z"),
    z(0, points -> "z"),
    ;

    private final Transform asString;
    private final int nbPoints;

    Operators(int nbPoints, Transform transform){
        this.nbPoints = nbPoints;
        asString = transform;
    }

    public String toString(SVGPoint... points){
        return asString.toString(points);
    }

    public int getNbPoints(){
        return nbPoints;
    }

    private static final Set<String> availableOperators = Set.of(Arrays.stream(Operators.values()).map(Enum::name).toArray(String[]::new));

    public static boolean isOperator(String operator){
        return availableOperators.contains(operator);
    }

    private interface Transform {
        String toString(SVGPoint... points);
    }
}