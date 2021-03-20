package org.kmp.morpheus;

import processing.core.PApplet;
import processing.core.PVector;

public class Morpheus extends PApplet {

    Mesh meshCube = new Mesh();
    Matrix matProj = new Matrix();

    PVector cameraPosition = new PVector(2f, 0.6f, -3f);
    PVector cameraRotation = new PVector(0, 0, 0);

    public void settings() {
        size(500, 500);
    }

    public void setup() {
        meshCube.add(new Triangle(new PVector(0, 0, 0), new PVector(0, 1, 0), new PVector(1, 1, 0)));
        meshCube.add(new Triangle(new PVector(0, 0, 0), new PVector(0, 1, 0), new PVector(1, 1, 0)));

        meshCube.add(new Triangle(new PVector(1, 0, 0), new PVector(1, 1, 0), new PVector(1, 1, 1)));
        meshCube.add(new Triangle(new PVector(1, 0, 0), new PVector(1, 1, 1), new PVector(1, 0, 1)));

        meshCube.add(new Triangle(new PVector(1, 0, 1), new PVector(1, 1, 1), new PVector(0, 1, 1)));
        meshCube.add(new Triangle(new PVector(1, 0, 1), new PVector(0, 1, 1), new PVector(0, 0, 1)));

        meshCube.add(new Triangle(new PVector(0, 0, 1), new PVector(0, 1, 1), new PVector(0, 1, 0)));
        meshCube.add(new Triangle(new PVector(0, 0, 1), new PVector(0, 1, 0), new PVector(0, 0, 0)));

        meshCube.add(new Triangle(new PVector(0, 1, 0), new PVector(0, 1, 1), new PVector(1, 1, 1)));
        meshCube.add(new Triangle(new PVector(0, 1, 0), new PVector(1, 1, 1), new PVector(1, 1, 0)));

        meshCube.add(new Triangle(new PVector(1, 0, 1), new PVector(0, 0, 1), new PVector(0, 0, 0)));
        meshCube.add(new Triangle(new PVector(1, 0, 1), new PVector(0, 0, 0), new PVector(1, 0, 0)));

        float fNear = 0.1f;
        float fFar = 1000.0f;
        float fFov = 90.0f;
        float fAspectRatio = (float) height / (float) width;
        float fFovRad = 1.0f / (float) Math.tan(fFov * 0.5f / 180.0f * Math.PI);

        matProj.inner[0][0] = fAspectRatio * fFovRad;
        matProj.inner[1][1] = fFovRad;
        matProj.inner[2][2] = fFar / (fFar - fNear);
        matProj.inner[3][2] = (-fFar * fNear) / (fFar - fNear);
        matProj.inner[2][3] = 1.0f;
        matProj.inner[3][3] = 0.0f;
    }

    public void draw() {
        background(0);
//        translate(width / 2f, height / 2f);
        stroke(255);
        noFill();

        println(cameraRotation);
        cameraRotation.z += 0.01;
        cameraRotation.x += 0.002;

        Matrix zRotationMatrix = new Matrix();
        Matrix xRotationMatrix = new Matrix();

        zRotationMatrix.inner[0][0] = (float) Math.cos(cameraRotation.z);
        zRotationMatrix.inner[0][1] = (float) Math.sin(cameraRotation.z);
        zRotationMatrix.inner[1][0] = -(float) Math.sin(cameraRotation.z);
        zRotationMatrix.inner[1][1] = (float) Math.cos(cameraRotation.z);
        zRotationMatrix.inner[2][2] = 1;
        zRotationMatrix.inner[3][3] = 1;

        xRotationMatrix.inner[0][0] = 1;
        xRotationMatrix.inner[1][1] = (float) Math.cos(cameraRotation.x);
        xRotationMatrix.inner[1][2] = (float) Math.sin(cameraRotation.x);
        xRotationMatrix.inner[2][1] = -(float) Math.sin(cameraRotation.x);
        xRotationMatrix.inner[2][2] = (float) Math.cos(cameraRotation.x);
        xRotationMatrix.inner[3][3] = 1;

        for (Triangle t : meshCube.triangles) {
            Triangle tTrans = new Triangle(t);
            tTrans.points[0] = tTrans.points[0].sub(cameraPosition);
            tTrans.points[1] = tTrans.points[1].sub(cameraPosition);
            tTrans.points[2] = tTrans.points[2].sub(cameraPosition);

            Triangle tRotZ = zRotationMatrix.multiply(tTrans);
            Triangle tRotZX = xRotationMatrix.multiply(tRotZ);


            Triangle tProj = matProj.multiply(tRotZX);
//            Triangle tProj = tTrans;

            tProj.points[0].x += 1f;
            tProj.points[0].y += 1f;
            tProj.points[1].x += 1f;
            tProj.points[1].y += 1f;
            tProj.points[2].x += 1f;
            tProj.points[2].y += 1f;

            tProj.points[0].x *= 0.5f * (float) width;
            tProj.points[0].y *= 0.5f * (float) height;
            tProj.points[1].x *= 0.5f * (float) width;
            tProj.points[1].y *= 0.5f * (float) height;
            tProj.points[2].x *= 0.5f * (float) width;
            tProj.points[2].y *= 0.5f * (float) height;

////            strokeWeight(10);
//            for (PVector p : tProj.points) {
//                point(p.x, p.y);
//            }

            strokeWeight(1);
            line(tProj.points[0].x, tProj.points[0].y, tProj.points[1].x, tProj.points[1].y);
            line(tProj.points[0].x, tProj.points[0].y, tProj.points[1].x, tProj.points[1].y);
            line(tProj.points[1].x, tProj.points[1].y, tProj.points[2].x, tProj.points[2].y);
            line(tProj.points[2].x, tProj.points[2].y, tProj.points[0].x, tProj.points[0].y);
        }
    }

    public static void main(String[] args) {
        String[] processingArgs = {"Morpheus"};
        Morpheus morpheus = new Morpheus();
        PApplet.runSketch(processingArgs, morpheus);
    }
}
