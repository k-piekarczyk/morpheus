package org.kmp.morpheus;

import processing.core.PVector;
import processing.core.PApplet;

public class Sphere {
    PVector origin = new PVector();
    float radius;
    Color color;

    Sphere(PVector origin, float radius, Color color) {
        this.origin = origin;
        this.radius = radius;
        this.color = color;
    }

    Sphere(float x, float y, float z, float radius, Color color) {
        this.origin = new PVector(x, y, z);
        this.radius = radius;
        this.color = color;
    }

    Sphere(Sphere sphere) {
        this.origin = sphere.origin;
        this.radius = sphere.radius;
        this.color = sphere.color;
    }
}
