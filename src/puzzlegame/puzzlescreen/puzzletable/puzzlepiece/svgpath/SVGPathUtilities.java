package puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import puzzlegame.puzzlescreen.minimap.RatioBinding;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

public class SVGPathUtilities {

    public static void main(String[] args) {

        String toTest = "M 50.0 300.0 L 50.0 250.0 C 50.0 150.0 75.0 150.0 100.0 250.0 C 150.0 450.0 200.0 450.0 200.0 250.0 Q 200.0 100.0 400.0 100.0";
        String expected = "M 400.0 100.0 Q 200.0 100.0 200.0 250.0 C 200.0 450.0 150.0 450.0 100.0 250.0 C 75.0 150.0 50.0 150.0 50.0 250.0 L 50.0 300.0";

        SVGPathDescription res = parse(toTest);

        System.out.println(res.operators().toString());
        System.out.println(res.points().toString());

        System.out.println(generate(res));
        System.out.println(toTest);
        System.out.println(inverse(toTest));
        System.out.println(expected);

    }

    public static String inverse(String path){
        SVGPathDescription parsed = parse(path);

        inverse(parsed);

        return generate(parsed);
    }

    public static void inverse(SVGPathDescription path) {
        Collections.reverse(path.operators());
        Collections.reverse(path.points());

        Operators first = path.operators().remove(path.operators().size() - 1);

        Operators last = path.operators().get(0);
        if (last == Operators.Z || last == Operators.z){
            path.operators().remove(0);
            path.operators().add(last);
        }
        path.operators().add(0, first);
    }

    public static SVGPathBinding adjustableSize(String path, RatioBinding ratio){
        SVGPathDescription parsed = parse(path);

        return new SVGPathBinding(parsed, ratio);
    }

    private static final Set<String> acceptedOperators = Set.of("M", "m", "L", "l", "H", "h", "V", "v", "C", "c", "S", "s", "Q", "q", "T", "t", "Z", "z");
    private static SVGPathDescription parse(String toParse){
        ObservableList<Operators> operators = FXCollections.observableArrayList();
        ObservableList<SVGPoint> points = FXCollections.observableArrayList();

        String[] splitted = toParse.split(" ");

        Double x = null;
        for (String s : splitted){
            if (acceptedOperators.contains(s)){
                operators.add(Operators.valueOf(s));
                if (x != null){
                    points.add(new SVGPoint(x,0));
                }
            } else {
                if (x == null){
                    x = Double.valueOf(s);
                } else {
                    points.add(new SVGPoint(x, Double.parseDouble(s)));
                    x = null;
                }
            }
        }

        return new SVGPathDescription(operators, points);
    }

    static String generate(SVGPathDescription data){
        StringBuilder build = new StringBuilder();

        // this copy is to prevent consumption of the SVGPath
        LinkedList<SVGPoint> points = new LinkedList<>(data.points());

        for (Operators operator : data.operators()){
            build.append(operator.toString(pop(points, operator.getNbPoints()))).append(" ");
        }

        return build.toString().strip();
    }

    private static SVGPoint[] pop(LinkedList<SVGPoint> points, int nbPoints){
        SVGPoint[] ps = new SVGPoint[nbPoints];

        for (int i = 0; i < nbPoints; i++) {
            ps[i] = points.pop();
        }

        return ps;
    }

}
