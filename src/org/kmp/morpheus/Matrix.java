package org.kmp.morpheus;

import processing.core.PVector;

public class Matrix {
    public float[][] inner;

    public Matrix() {
        inner = new float[4][4];
    }

    public Matrix(float[][] mat) {
        inner = mat;
    }

    public PVector multiply(PVector vector) {
        PVector result = new PVector();

        result.x = vector.x * inner[0][0] + vector.y * inner[1][0] + vector.z * inner[2][0] + inner[3][0];
        result.y = vector.x * inner[0][1] + vector.y * inner[1][1] + vector.z * inner[2][1] + inner[3][1];
        result.z = vector.x * inner[0][2] + vector.y * inner[1][2] + vector.z * inner[2][2] + inner[3][2];
        float w = vector.x * inner[0][3] + vector.y * inner[1][3] + vector.z * inner[2][3] + inner[3][3];

        if (w != 0.0f)
        {
            result.x /= w; result.y /= w; result.z /= w;
        }

        if (w < 0f) {
            return null;
        }

        return result;
    }

    public Triangle multiply(Triangle triangle) {
        PVector t1 = this.multiply(triangle.points[0]);
        PVector t2 = this.multiply(triangle.points[1]);
        PVector t3 = this.multiply(triangle.points[2]);

        if (t1 == null || t2 == null || t3 == null) {
            return null;
        }

        return new Triangle(t1, t2, t3);
    }
}
