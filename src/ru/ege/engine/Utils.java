package ru.ege.engine;

import java.awt.*;
import java.awt.geom.Area;

public class Utils {
    public static boolean testIntersection(Shape shapeA, Shape shapeB) {
        Area areaA = new Area(shapeA);
        areaA.intersect(new Area(shapeB));
        return !areaA.isEmpty();
    }
}
