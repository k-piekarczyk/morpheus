package org.kmp.morpheus;

import processing.core.PVector;
import java.awt.geom.Line2D;

public class Edge {
    public PVector start = new PVector();
    public PVector end = new PVector();
    public Triangle originTriangle = null;

    Edge() {
    }

    Edge(PVector start, PVector end, Triangle originTriangle) {
        this.start = start;
        this.end = end;
        this.originTriangle = originTriangle;
    }

    static public PVector findIntersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        if (x2 - x1 == 0) {
            float m2 = (y4 - y3) / (x4 - x3);
            float b2 = y3 - m2 * x3;

            PVector intersection = new PVector();

            intersection.x = x1;
            intersection.y = (m2 * intersection.x) + b2;

            return intersection;
        }

        if (x4 - x3 == 0) {
            float m1 = (y2 - y1) / (x2 - x1);
            float b1 = y1 - m1 * x1;

            PVector intersection = new PVector();

            intersection.x = x3;
            intersection.y = (m1 * intersection.x) + b1;

            return intersection;
        }

        float m1 = (y2 - y1) / (x2 - x1);
        float m2 = (y4 - y3) / (x4 - x3);

        float b1 = y1 - m1 * x1;
        float b2 = y3 - m2 * x3;

        PVector intersection = new PVector();

        intersection.x = -(b2 - b1) / (m2 - m1);
        intersection.y = (m1 * intersection.x) + b1;

        return intersection;
    }
}
