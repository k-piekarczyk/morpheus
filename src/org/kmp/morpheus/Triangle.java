package org.kmp.morpheus;

import processing.core.PVector;

public class Triangle implements Comparable<Triangle> {
    public PVector[] points;
    public PVector[] projectedPoints;

    public PVector centroid = new PVector();
    public float distance;
    public String color = "black";

    public Triangle() {
        points = new PVector[3];
    }

    public Triangle(PVector t1, PVector t2, PVector t3) {
        points = new PVector[]{t1, t2, t3};
        setCentroid();
    }

    public Triangle(PVector t1, PVector t2, PVector t3, String color) {
        points = new PVector[]{t1, t2, t3};
        this.color = color;
        setCentroid();
    }

    public Triangle(Triangle old) {
        points = new PVector[]{old.points[0].copy(), old.points[1].copy(), old.points[2].copy()};
        centroid = old.centroid;
        distance = old.distance;
        color = old.color;
    }

    public Triangle(PVector[] p) {
        points = p;
    }

    public void setCentroid() {
        centroid.x = (points[0].x + points[1].x + points[2].x) / 3;
        centroid.y = (points[0].y + points[1].y + points[2].y) / 3;
        centroid.z = (points[0].z + points[1].z + points[2].z) / 3;
    }

    public void setDistance(PVector origin) {
        distance = (float) Math.sqrt(Math.pow(origin.x - centroid.x, 2) + Math.pow(origin.y - centroid.y, 2) + Math.pow(origin.z - centroid.z, 2));
    }

    public Triangle setDistanceByCopy(PVector origin) {
        Triangle t = new Triangle(this);
        t.setDistance(origin);

        return t;
    }

    @Override
    public int compareTo(Triangle t) {
        return Float.compare(t.distance, distance);
    }
}
