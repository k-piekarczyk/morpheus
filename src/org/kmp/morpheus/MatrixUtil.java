package org.kmp.morpheus;

public class MatrixUtil {
    public static float[][] multiply(float[][] left, float[][] right) {
        if (left[0].length != right.length) {
            System.err.println("Can't multiply those matrices!");
            System.exit(1);
        }

        float[][] result = new float[left.length][right[0].length];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[0].length; col++) {
                result[row][col] = 0;

                for (int i = 0; i < left[0].length; i++) {
                    result[row][col] += left[row][i] * right[i][col];
                }
            }
        }

        return result;
    }

    public static float[] multiply(float[][] left, float[] right) {
        if (left[0].length != right.length) {
            System.err.println("Can't multiply those matrices!");
            System.exit(1);
        }

        float[] result = new float[left.length];

        for (int row = 0; row < result.length; row++) {
            result[row] = 0;

            for (int i = 0; i < left[0].length; i++) {
                result[row] += left[row][i] * right[i];
            }
        }

        return result;
    }

    public static float[] add(float[] a, float[] b) {
        if (a.length != b.length) {
            System.err.println("Vectors need to be the same length to add them!");
            System.exit(1);
        }

        float[] result = new float[a.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = a[i] + b[i];
        }

        return result;
    }

    public static float[][] add(float[][] a, float[][] b) {
        if (a.length != b.length || a[0].length != b[0].length) {
            System.err.println("Matrices need to be the same size!");
            System.exit(1);
        }

        float[][] result = new float[a.length][a[0].length];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[0].length; col++) {
                result[row][col] = a[row][col] + b[row][col];
            }
        }

        return result;
    }

    public static void print(float[][] mat) {
        for (int row = 0; row < mat.length; row++) {
            System.out.print("|");
            for (int col = 0; col < mat[0].length; col++) {
                System.out.printf("%10.4f", mat[row][col]);
            }
            System.out.println("|");
        }
        System.out.println();
    }

    public static void print(float[] mat) {
        for (int row = 0; row < mat.length; row++) {
            System.out.print("|");
            System.out.printf("%10.4f", mat[row]);
            System.out.println("|");
        }
        System.out.println();
    }

    public static float[][] translationMatrix(float[] position) {
        return new float[][]{
                new float[]{1, 0, 0, -position[0]},
                new float[]{0, 1, 0, -position[1]},
                new float[]{0, 0, 1, -position[2]},
                new float[]{0, 0, 0, 1},
        };
    }

    public static float[][] xAxisRotationMatrix(float[] rotation) {
        return new float[][]{
                new float[]{1, 0, 0, 0},
                new float[]{0, (float) Math.cos(rotation[0]), -(float) Math.sin(rotation[0]), 0},
                new float[]{0, (float) Math.sin(rotation[0]), (float) Math.cos(rotation[0]), 0},
                new float[]{0, 0, 0, 1},
        };
    }

    public static float[][] yAxisRotationMatrix(float[] rotation) {
        return new float[][]{
                new float[]{(float) Math.cos(rotation[1]), 0, (float) Math.sin(rotation[1]), 0},
                new float[]{0, 1, 0, 0},
                new float[]{-(float) Math.sin(rotation[1]), 0, (float) Math.cos(rotation[1]), 0},
                new float[]{0, 0, 0, 1},
        };
    }

    public static float[][] zAxisRotationMatrix(float[] rotation) {
        return new float[][]{
                new float[]{(float) Math.cos(rotation[2]), -(float) Math.sin(rotation[2]), 0, 0},
                new float[]{(float) Math.sin(rotation[2]), (float) Math.cos(rotation[2]), 0, 0},
                new float[]{0, 0, 1, 0},
                new float[]{0, 0, 0, 1},
        };
    }

    public static float[][] compoundRotationMatrix(float[] rotation){
        float[][] x = xAxisRotationMatrix(rotation);
        float[][] y = yAxisRotationMatrix(rotation);
        float[][] z = zAxisRotationMatrix(rotation);

        return multiply(x, multiply(y, z));
    }
}
