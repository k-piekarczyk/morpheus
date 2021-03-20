package org.kmp.morpheus;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.Arrays;

public class Morpheus extends PApplet {

    PVector[] box = new PVector[]{
            new PVector(50f, 50f, 50f),
            new PVector(-50f, 50f, 50f),
            new PVector(50f, -50f, 50f),
            new PVector(-50f, -50f, 50f),
            new PVector(50f, 50f, -50f),
            new PVector(-50f, 50f, -50f),
            new PVector(50f, -50f, -50f),
            new PVector(-50f, -50f, -50f),
    };

    PVector position = new PVector(0f, 0f, -100f);
    PVector rotation = new PVector(0, 0, 0);

    float f = 50;

    public void settings() {
        size(500, 500);
    }

    public void setup() {
        background(0);
    }

    public void draw() {
        background(0);
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

        float fNear = 0.1f;
        float fFar = 1000f;
        float fFov = 90f;
        float fAspectRatio = (float) height/ (float) width;
        float fS = 1f / (float) Math.tan((fFov / 2f) / (Math.PI * 180f));

        Matrix perspectiveMatrix = new Matrix(new float[][]{
                new float[]{fS * fAspectRatio, 0, 0, 0},
                new float[]{0, fS, 0 ,0},
                new float[]{0, 0, -((fFar * fNear) / (fFar - fNear)), -(fFar / (fFar - fNear))},
                new float[]{0, 0, -1, 0},
        });

        Matrix translationMatrix = Matrix.translationMatrix(position);
        Matrix compoundRotationMatrix = Matrix.compoundRotationMatrix(rotation);

        Matrix cameraTransformMatrix = perspectiveMatrix.multiply(translationMatrix);

        PVector[] projectedBox = new PVector[box.length];

        for (int i = 0; i < projectedBox.length; i++) {
            projectedBox[i] = cameraTransformMatrix.multiply(box[i]);
        }

        for (PVector p : projectedBox) {
            System.out.println(p);
            point(p.x, p.y);
        }
        System.out.println("Stop");

//        System.out.println(Arrays.toString(rotation));
//        rotation.x += .005;
//        rotation.y += .001;
//        rotation[2] += .005;
    }

    public static void main(String[] args) {
        String[] processingArgs = {"Morpheus"};
        Morpheus morpheus = new Morpheus();
        PApplet.runSketch(processingArgs, morpheus);
    }
}
