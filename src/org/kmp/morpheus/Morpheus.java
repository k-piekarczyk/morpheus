package org.kmp.morpheus;

import processing.core.PApplet;
import processing.core.PVector;

public class Morpheus extends PApplet {

    PVector[] box = new PVector[8];
    float mv = 0;

    public void settings() {
        size(500, 500);
    }

    public void draw() {
        background(0);
        translate(width / 2, height / 2);
        stroke(255);
        strokeWeight(4);
        noFill();

        box[0] = new PVector(50 + mv, 50, 50);
        box[1] = new PVector(-50 + mv, 50, 50);
        box[2] = new PVector(50 + mv, -50, 50);
        box[3] = new PVector(-50 + mv, -50, 50);
        box[4] = new PVector(50 + mv, 50, -50);
        box[5] = new PVector(-50 + mv, 50, -50);
        box[6] = new PVector(50 + mv, -50, -50);
        box[7] = new PVector(-50 + mv, -50, -50);

        for (PVector p : box) {
            point(p.x + (p.x * 5 / (1 - p.z)), p.y + (p.y * 5 / (1 - p.z)));
        }

        mv += 0.5;
    }

    public static void main(String[] args) {
//        String[] processingArgs = {"Morpheus"};
//        Morpheus morpheus = new Morpheus();
//        PApplet.runSketch(processingArgs, morpheus);

        float[][] mat1 = new float[][]{
                new float[]{1, 2, 3},
                new float[]{4, 5, 6},
                new float[]{7, 8, 9},
        };

        float[][] mat2 = new float[][]{
                new float[]{1, 2, 3},
                new float[]{4, 5, 6},
                new float[]{7, 8, 9},
        };

        float[] vec1 = new float[]{1, 2, 3};

        float[][] mat3 = MatrixUtil.multiply(mat1, mat2);
        float[] vec2 = MatrixUtil.multiply(mat1, vec1);

        MatrixUtil.print(mat1);
        MatrixUtil.print(mat2);
        MatrixUtil.print(mat3);
        MatrixUtil.print(vec1);
        MatrixUtil.print(vec2);
    }
}
