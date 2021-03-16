package org.kmp.morpheus;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

public class Morpheus extends PApplet {

    float[][] box = new float[][]{
            {0, 0, 200, 1},
//            {-50, 50, 50, 1},
//            {50, -50, 50, 1},
//            {-50, -50, 50, 1},
//            {50, 50, -50, 1},
//            {-50, 50, -50, 1},
//            {50, -50, -50, 1},
//            {-50, -50, -50, 1},
    };

    float[] position = new float[]{0.0f, 0.0f, 0.0f}; // x, y, z
    float[] rotation = new float[]{0.0f, 0.0f, 0.0f}; // x, y, x

    public void settings() {
        size(500, 500);
    }

    public void setup() {
        background(0);
    }

    public void draw() {

        translate(width / 2f, height / 2f);
        stroke(255);
        strokeWeight(4);
        noFill();
//        if (keyPressed) {
//            if (key == CODED) {
//                if (keyCode == UP) {
//                    position = MatrixUtil.add(position, new float[]{0, 0, 0.2f});
//                }
//                if (keyCode == DOWN) {
//                    position = MatrixUtil.add(position, new float[]{0, 0, -0.2f});
//                }
//                if (keyCode == LEFT) {
//                    position = MatrixUtil.add(position, new float[]{0.2f, 0, 0});
//                }
//                if (keyCode == RIGHT) {
//                    position = MatrixUtil.add(position, new float[]{-0.2f, 0, 0});
//                }
//            }
//        }

        float[][] translationMatrix = MatrixUtil.translationMatrix(position);
        float[][] compoundRotationMatrix = MatrixUtil.compoundRotationMatrix(rotation);

//        float[][] cameraTransformMatrix = MatrixUtil.multiply(compoundRotationMatrix, translationMatrix);

        float[][] projectedBox = new float[box.length][box[0].length];

        for (int i = 0; i < projectedBox.length; i++) {
//            projectedBox[i] = MatrixUtil.multiply(translationMatrix, box[i]);
            projectedBox[i] = MatrixUtil.multiply(compoundRotationMatrix, box[i]);
        }

        for (float[] p : projectedBox) {
            point(p[0], p[1]);
        }

//        System.out.println(Arrays.toString(rotation));
        rotation[0] += .005;
//        rotation[1] += .005;
//        rotation[2] += .005;
    }

    public static void main(String[] args) {
        String[] processingArgs = {"Morpheus"};
        Morpheus morpheus = new Morpheus();
        PApplet.runSketch(processingArgs, morpheus);
    }
}
