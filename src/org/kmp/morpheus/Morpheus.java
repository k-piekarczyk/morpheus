package org.kmp.morpheus;

import processing.core.PApplet;
import processing.core.PVector;

public class Morpheus extends PApplet {

    Mesh meshCube = new Mesh();
    Matrix matProj = new Matrix();

    PVector cameraPosition = new PVector(0, 0.6f, -3f);
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
        stroke(255);
        noFill();

        Matrix translationMatrix = new Matrix();
        Matrix zRotationMatrix = new Matrix();
        Matrix yRotationMatrix = new Matrix();
        Matrix xRotationMatrix = new Matrix();

        translationMatrix.inner[0][0] = 1;
        translationMatrix.inner[1][1] = 1;
        translationMatrix.inner[2][2] = 1;
        translationMatrix.inner[3][0] = -cameraPosition.x;
        translationMatrix.inner[3][1] = -cameraPosition.y;
        translationMatrix.inner[3][2] = -cameraPosition.z;
        translationMatrix.inner[3][3] = 1;

        zRotationMatrix.inner[0][0] = (float) Math.cos(cameraRotation.z);
        zRotationMatrix.inner[0][1] = (float) Math.sin(cameraRotation.z);
        zRotationMatrix.inner[1][0] = -(float) Math.sin(cameraRotation.z);
        zRotationMatrix.inner[1][1] = (float) Math.cos(cameraRotation.z);
        zRotationMatrix.inner[2][2] = 1;
        zRotationMatrix.inner[3][3] = 1;

        yRotationMatrix.inner[0][0] = (float) Math.cos(cameraRotation.y);
        yRotationMatrix.inner[0][2] = - (float) Math.sin(cameraRotation.y);
        yRotationMatrix.inner[1][1] = 1;
        yRotationMatrix.inner[2][0] = (float) Math.sin(cameraRotation.y);
        yRotationMatrix.inner[2][2] = (float) Math.cos(cameraRotation.y);

        xRotationMatrix.inner[0][0] = 1;
        xRotationMatrix.inner[1][1] = (float) Math.cos(cameraRotation.x);
        xRotationMatrix.inner[1][2] = (float) Math.sin(cameraRotation.x);
        xRotationMatrix.inner[2][1] = -(float) Math.sin(cameraRotation.x);
        xRotationMatrix.inner[2][2] = (float) Math.cos(cameraRotation.x);
        xRotationMatrix.inner[3][3] = 1;

        for (Triangle t : meshCube.triangles) {
            Triangle tTrans = translationMatrix.multiply(t);

            Triangle tRotZ = zRotationMatrix.multiply(tTrans);
            Triangle tRotZY = yRotationMatrix.multiply(tRotZ);
            Triangle tRotZYX = xRotationMatrix.multiply(tRotZY);

            Triangle tProj = matProj.multiply(tRotZYX);

            if (tProj == null) continue;

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
