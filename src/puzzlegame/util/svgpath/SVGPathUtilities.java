package puzzlegame.util.svgpath;

import puzzlegame.puzzlescreen.minimap.RatioBinding;
import puzzlegame.puzzlescreen.puzzletable.puzzlepiece.LineDrawer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

public class SVGPathUtilities {

    public static void main(String[] args) {

        String toTest = "M 50.0 300.0 L 50.0 250.0 C 50.0 150.0 75.0 150.0 100.0 250.0 C 150.0 450.0 200.0 450.0 200.0 250.0 Q 200.0 100.0 400.0 100.0";
        String expected = "M 400.0 100.0 Q 200.0 100.0 200.0 250.0 C 200.0 450.0 150.0 450.0 100.0 250.0 C 75.0 150.0 50.0 150.0 50.0 250.0 L 50.0 300.0";

        Result res = parse(toTest);

        System.out.println(res.operators().toString());
        System.out.println(res.points().toString());

        System.out.println(generate(res));
        System.out.println(toTest);
        System.out.println(inverse(toTest));
        System.out.println(expected);

    }

    public static String inverse(String path){
        Result parsed = parse(path);

        Collections.reverse(parsed.operators());
        Collections.reverse(parsed.points());

        Operators first = parsed.operators().removeLast();

        Operators last = parsed.operators().peek();
        if (last == Operators.Z || last == Operators.z){
            parsed.operators().removeFirst();
            parsed.operators().addLast(last);
        }
        parsed.operators().addFirst(first);

        return generate(parsed);
    }

    public static SVGPathBinding adjustableSize(String path, RatioBinding ratio){
        Result parsed = parse(path);

        return new SVGPathBinding(parsed, ratio);
    }

    private static final Set<String> acceptedOperators = Set.of("M", "m", "L", "l", "H", "h", "V", "v", "C", "c", "S", "s", "Q", "q", "T", "t", "Z", "z");
    private static Result parse(String toParse){
        LinkedList<Operators> operators = new LinkedList<>();
        LinkedList<LineDrawer.Point> points = new LinkedList<>();

        String[] splitted = toParse.split(" ");

        Double x = null;
        for (String s : splitted){
            if (acceptedOperators.contains(s)){
                operators.add(Operators.valueOf(s));
                if (x != null){
                    points.add(new LineDrawer.Point(x,0));
                }
            } else {
                if (x == null){
                    x = Double.valueOf(s);
                } else {
                    points.add(new LineDrawer.Point(x, Double.parseDouble(s)));
                    x = null;
                }
            }
        }

        return new Result(operators, points);
    }

    static String generate(Result data){
        StringBuilder build = new StringBuilder();

        for (Operators operator : data.operators()){
            build.append(operator.toString(pop(data.points(), operator.getNbPoints()))).append(" ");
        }

        return build.toString().strip();
    }

    private static LineDrawer.Point[] pop(LinkedList<LineDrawer.Point> points, int nbPoints){
        LineDrawer.Point[] ps = new LineDrawer.Point[nbPoints];

        for (int i = 0; i < nbPoints; i++) {
            ps[i] = points.pop();
        }

        return ps;
    }

    record Result(LinkedList<Operators> operators, LinkedList<LineDrawer.Point> points){}

    @SuppressWarnings("unused")
    private enum Operators{
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
        T(2, points -> "T " + points[0].x() + " " + points[0].y() + " " + points[1].x() + " " + points[1].y()),
        t(2, points -> "t " + points[0].x() + " " + points[0].y() + " " + points[1].x() + " " + points[1].y()),
        Z(0, points -> "Z"),
        z(0, points -> "z"),
        ;

        private final Transform asString;
        private final int nbPoints;

        Operators(int nbPoints, Transform transform){
            this.nbPoints = nbPoints;
            asString = transform;
        }

        public String toString(LineDrawer.Point... points){
            return asString.toString(points);
        }

        public int getNbPoints(){
            return nbPoints;
        }
    }

    private interface Transform{
        String toString(LineDrawer.Point... points);
    }
}
