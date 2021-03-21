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

    public Matrix multiply(Matrix m) {
        Matrix result = new Matrix();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int i = 0; i < 4; i++) {
                    result.inner[col][row] += inner[i][row] * m.inner[col][i];
                }
            }
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

    public static Matrix yReverseMatrix() {
        Matrix yReverseMatrix = new Matrix();

        yReverseMatrix.inner[0][0] = 1;
        yReverseMatrix.inner[1][1] = -1;
        yReverseMatrix.inner[2][2] = 1;
        yReverseMatrix.inner[3][3] = 1;

        return yReverseMatrix;
    }


    public static Matrix translationMatrix(PVector cameraPosition) {
        Matrix translationMatrix = new Matrix();

        translationMatrix.inner[0][0] = 1;
        translationMatrix.inner[1][1] = 1;
        translationMatrix.inner[2][2] = 1;
        translationMatrix.inner[3][0] = -cameraPosition.x;
        translationMatrix.inner[3][1] = -cameraPosition.y;
        translationMatrix.inner[3][2] = -cameraPosition.z;
        translationMatrix.inner[3][3] = 1;

        return translationMatrix;
    }

    public static Matrix rotationMatrix(PVector cameraRotation) {
        Matrix zRotationMatrix = zRotationMatrix(cameraRotation);
        Matrix yRotationMatrix = yRotationMatrix(cameraRotation);
        Matrix xRotationMatrix = xRotationMatrix(cameraRotation);

        return xRotationMatrix.multiply(yRotationMatrix).multiply(zRotationMatrix);
    }

    public static Matrix invRotationMatrix(PVector cameraRotation) {
        Matrix zRotationMatrix = zRotationMatrix(cameraRotation);
        Matrix yRotationMatrix = yRotationMatrix(cameraRotation);
        Matrix xRotationMatrix = xRotationMatrix(cameraRotation);

        return zRotationMatrix.multiply(yRotationMatrix).multiply(xRotationMatrix);
    }

    public static Matrix xRotationMatrix(PVector cameraRotation) {
        Matrix xRotationMatrix = new Matrix();

        xRotationMatrix.inner[0][0] = 1;
        xRotationMatrix.inner[1][1] = (float) Math.cos(cameraRotation.x);
        xRotationMatrix.inner[1][2] = (float) Math.sin(cameraRotation.x);
        xRotationMatrix.inner[2][1] = -(float) Math.sin(cameraRotation.x);
        xRotationMatrix.inner[2][2] = (float) Math.cos(cameraRotation.x);
        xRotationMatrix.inner[3][3] = 1;

        return xRotationMatrix;
    }

    public static Matrix yRotationMatrix(PVector cameraRotation) {
        Matrix yRotationMatrix = new Matrix();

        yRotationMatrix.inner[0][0] = (float) Math.cos(cameraRotation.y);
        yRotationMatrix.inner[0][2] = - (float) Math.sin(cameraRotation.y);
        yRotationMatrix.inner[1][1] = 1;
        yRotationMatrix.inner[2][0] = (float) Math.sin(cameraRotation.y);
        yRotationMatrix.inner[2][2] = (float) Math.cos(cameraRotation.y);

        return yRotationMatrix;
    }

    public static Matrix zRotationMatrix(PVector cameraRotation) {
        Matrix zRotationMatrix = new Matrix();

        zRotationMatrix.inner[0][0] = (float) Math.cos(cameraRotation.z);
        zRotationMatrix.inner[0][1] = (float) Math.sin(cameraRotation.z);
        zRotationMatrix.inner[1][0] = -(float) Math.sin(cameraRotation.z);
        zRotationMatrix.inner[1][1] = (float) Math.cos(cameraRotation.z);
        zRotationMatrix.inner[2][2] = 1;
        zRotationMatrix.inner[3][3] = 1;

        return zRotationMatrix;
    }



}
