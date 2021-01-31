package puzzlegame.puzzlescreen.puzzletable.puzzlepiece.svgpath;

import javafx.collections.ObservableList;

record SVGPathDescription(ObservableList<Operators> operators, ObservableList<SVGPoint> points){}
