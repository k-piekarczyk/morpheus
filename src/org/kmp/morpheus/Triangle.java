package org.kmp.morpheus;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

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
        float a = (float) Math.sqrt(Math.pow(points[1].x - points[2].x, 2) + Math.pow(points[1].y - points[2].y, 2) + Math.pow(points[1].z - points[2].z, 2));
        float b = (float) Math.sqrt(Math.pow(points[2].x - points[0].x, 2) + Math.pow(points[2].y - points[0].y, 2) + Math.pow(points[2].z - points[0].z, 2));
        float c = (float) Math.sqrt(Math.pow(points[0].x - points[1].x, 2) + Math.pow(points[0].y - points[1].y, 2) + Math.pow(points[0].z - points[1].z, 2));


        if (a >= b && a >= c) {
            centroid.x = (points[1].x + points[2].x) / 2;
            centroid.y = (points[1].y + points[2].y) / 2;
            centroid.z = (points[1].z + points[2].z) / 2;
        } else if (b >= a && b >= c) {
            centroid.x = (points[2].x + points[0].x) / 2;
            centroid.y = (points[2].y + points[0].y) / 2;
            centroid.z = (points[2].z + points[0].z) / 2;
        } else if (c >= b && c >= a) {
            centroid.x = (points[0].x + points[1].x) / 2;
            centroid.y = (points[0].y + points[1].y) / 2;
            centroid.z = (points[0].z + points[1].z) / 2;
        }
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
//        return Float.compare(t.distance, distance);
        PVector vPQ = new PVector(t.points[1].x - t.points[0].x, t.points[1].y - t.points[0].y, t.points[1].z - t.points[0].z);
        PVector vPR = new PVector(t.points[2].x - t.points[0].x, t.points[2].y - t.points[0].y, t.points[2].z - t.points[0].z);

        PVector perpV = vPQ.cross(vPR);
        float a = perpV.x;
        float b = perpV.y;
        float c = perpV.z;
        float x0 = vPQ.x;
        float y0 = vPQ.y;
        float z0 = vPQ.z;

        // a * (x - x0) + b * (y - y0) + c * (z - z0) = 0


    }

    public List<Edge> edgeList() {
        List<Edge> edgeList = new ArrayList<>();

        if (projectedPoints == null) return edgeList;

        edgeList.add(new Edge(projectedPoints[0], projectedPoints[1], this));
        edgeList.add(new Edge(projectedPoints[1], projectedPoints[2], this));
        edgeList.add(new Edge(projectedPoints[2], projectedPoints[0], this));

        return edgeList;
    }
}
