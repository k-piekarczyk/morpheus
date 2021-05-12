package org.kmp.morpheus;

import processing.core.PVector;

public class Box extends Mesh {
    public Box(PVector origin, float w, float h, float d) {
        initUnitCube();
        scale(new PVector(w, h, d));
        translate(origin);
    }

    private void initUnitCube() {
        add(new Triangle(new PVector(0, 0, 0), new PVector(1, 1, 0), new PVector(1, 0, 0), "red")); // 1
        add(new Triangle(new PVector(0, 1, 0), new PVector(1, 1, 0), new PVector(0, 0, 0), "red")); // 2

        add(new Triangle(new PVector(1, 0, 1), new PVector(1, 0, 0), new PVector(1, 1, 1), "green")); // 3
        add(new Triangle(new PVector(1, 1, 0), new PVector(1, 1, 1), new PVector(1, 0, 0), "green")); // 4

        add(new Triangle(new PVector(0, 0, 1), new PVector(1, 0, 1), new PVector(0, 1, 1), "red")); // 5
        add(new Triangle(new PVector(1, 1, 1), new PVector(0, 1, 1), new PVector(1, 0, 1), "red")); // 6

        add(new Triangle(new PVector(0, 0, 0), new PVector(0, 0, 1), new PVector(0, 1, 0), "green")); // 7
        add(new Triangle(new PVector(0, 1, 1), new PVector(0, 1, 0), new PVector(0, 0, 1), "green")); // 8

        add(new Triangle(new PVector(0, 1, 0), new PVector(0, 1, 1), new PVector(1, 1, 1), "blue"));
        add(new Triangle(new PVector(0, 1, 0), new PVector(1, 1, 1), new PVector(1, 1, 0), "blue"));

        add(new Triangle(new PVector(1, 0, 1), new PVector(0, 0, 1), new PVector(0, 0, 0), "blue"));
        add(new Triangle(new PVector(1, 0, 1), new PVector(0, 0, 0), new PVector(1, 0, 0), "blue"));
    }
}
