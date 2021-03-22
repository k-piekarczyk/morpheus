package org.kmp.morpheus;

import processing.core.PVector;

import java.util.ArrayList;

public class Mesh {
    public ArrayList<Triangle> triangles = new ArrayList<>();

    public void add(Triangle triangle) {
        triangles.add(triangle);
    }

    public void translate(PVector v) {
        Matrix translationMatrix = Matrix.translationMatrix(v);

        ArrayList<Triangle> movedTriangles = new ArrayList<>();

        for(Triangle t: triangles) movedTriangles.add(translationMatrix.multiply(t));

        triangles = movedTriangles;
    }

    public void scale(PVector s) {
        Matrix scalingMatrix = Matrix.scalingMatrix(s);

        ArrayList<Triangle> scaledTriangles = new ArrayList<>();

        for(Triangle t: triangles) scaledTriangles.add(scalingMatrix.multiply(t));

        triangles = scaledTriangles;
    }
}

