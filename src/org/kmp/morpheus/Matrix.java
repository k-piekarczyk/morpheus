package org.kmp.morpheus;

import processing.core.PVector;

public class Matrix {
    public float[][] mat = new float[4][4];

    public Matrix() {
    }

    public Matrix(float[][] mat) {
        this.mat = mat;
    }

    public Matrix multiply(Matrix right) {

        float[][] result = new float[4][4];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[0].length; col++) {
                result[row][col] = 0;

                for (int i = 0; i < mat[0].length; i++) {
                    result[row][col] += mat[row][i] * right.mat[i][col];
                }
            }
        }

        return new Matrix(result);
    }

    public PVector multiply(PVector v) {

        PVector result = new PVector();

        result.x = v.x * mat[0][0] + v.y * mat[0][1] + v.z * mat[0][2] + mat[0][3];
        result.y = v.x * mat[1][0] + v.y * mat[1][1] + v.z * mat[1][2] + mat[1][3];
        result.z = v.x * mat[2][0] + v.y * mat[2][1] + v.z * mat[2][2] + mat[2][3];
        float w = v.x * mat[3][0] + v.y * mat[3][1] + v.z * mat[3][2] + mat[3][3];

        if (w != 0.0f)
        {
            System.out.println(w);
            result.x /= w; result.y /= w; result.z /= w;
        }

        return result;
    }

    public static Matrix translationMatrix(PVector position) {
        return new Matrix(new float[][]{
                new float[]{1, 0, 0, -position.x},
                new float[]{0, 1, 0, -position.y},
                new float[]{0, 0, 1, -position.z},
                new float[]{0, 0, 0, 1},
        });
    }

    public static Matrix xAxisRotationMatrix(PVector rotation) {
        return new Matrix(new float[][]{
                new float[]{1, 0, 0, 0},
                new float[]{0, (float) Math.cos(rotation.x), -(float) Math.sin(rotation.x), 0},
                new float[]{0, (float) Math.sin(rotation.x), (float) Math.cos(rotation.x), 0},
                new float[]{0, 0, 0, 1},
        });
    }

    public static Matrix yAxisRotationMatrix(PVector rotation) {
        return new Matrix(new float[][]{
                new float[]{(float) Math.cos(rotation.y), 0, (float) Math.sin(rotation.y), 0},
                new float[]{0, 1, 0, 0},
                new float[]{-(float) Math.sin(rotation.y), 0, (float) Math.cos(rotation.y), 0},
                new float[]{0, 0, 0, 1},
        });
    }

    public static Matrix zAxisRotationMatrix(PVector rotation) {
        return new Matrix(new float[][]{
                new float[]{(float) Math.cos(rotation.z), -(float) Math.sin(rotation.z), 0, 0},
                new float[]{(float) Math.sin(rotation.z), (float) Math.cos(rotation.z), 0, 0},
                new float[]{0, 0, 1, 0},
                new float[]{0, 0, 0, 1},
        });
    }

    public static Matrix compoundRotationMatrix(PVector rotation) {
        Matrix x = xAxisRotationMatrix(rotation);
        Matrix y = yAxisRotationMatrix(rotation);
        Matrix z = zAxisRotationMatrix(rotation);

        return x.multiply(y).multiply(z);
    }
}
