package org.kmp.morpheus;

import java.util.ArrayList;

public class Mesh {
    public ArrayList<Triangle> triangles = new ArrayList<>();

    public void add(Triangle triangle) {
        triangles.add(triangle);
    }
}

