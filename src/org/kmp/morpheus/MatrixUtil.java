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
}
