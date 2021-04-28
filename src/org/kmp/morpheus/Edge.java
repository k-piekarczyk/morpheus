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

    public PVector findIntersection(Edge e) {
        if (!Line2D.linesIntersect(start.x, start.y, end.x, end.y, e.start.x, e.start.y, e.end.x, e.end.y)) {
            return null;
        }

        if (start.x == end.x && start.y == end.y) return null;
        if (e.start.x == e.end.x && e.start.y == e.end.y) return null;

        if (end.x - start.x == 0) {
            float m2 = (e.end.y - e.start.y) / (e.end.x - e.start.x);
            float b2 = e.start.y - m2 * e.start.x;

            PVector intersection = new PVector();

            intersection.x = start.x;
            intersection.y = (m2 * intersection.x) + b2;

            return intersection;
        }

        if (e.end.x - e.start.x == 0) {
            float m1 = (end.y - start.y) / (end.x - start.x);
            float b1 = start.y - m1 * start.x;

            PVector intersection = new PVector();

            intersection.x = e.start.x;
            intersection.y = (m1 * intersection.x) + b1;

            return intersection;
        }

        float m1 = (end.y - start.y) / (end.x - start.x);
        float m2 = (e.end.y - e.start.y) / (e.end.x - e.start.x);

        float b1 = start.y - m1 * start.x;
        float b2 = e.start.y - m2 * e.start.x;

        PVector intersection = new PVector();

        intersection.x = -(b2 - b1) / (m2 - m1);
        intersection.y = (m1 * intersection.x) + b1;

        return intersection;
    }
}
