package org.kmp.morpheus;

import processing.core.PVector;

public class Triangle {
    public PVector[] points;

    public Triangle () {
        points = new PVector[3];
    }

    public Triangle(PVector t1, PVector t2, PVector t3) {
        points = new PVector[]{t1, t2, t3};
    }

    public Triangle(Triangle old) {
        points = new PVector[]{old.points[0].copy(), old.points[1].copy(), old.points[2].copy()};
    }

    public Triangle(PVector[] p) {
        points = p;
    }


}
