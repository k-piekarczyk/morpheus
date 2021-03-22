package org.kmp.morpheus;

import processing.core.PApplet;
import processing.core.PVector;

public class Morpheus extends PApplet {
    Scene scene = new Scene();

    PVector cameraPosition = new PVector(0, 5.11f, -3f);
    PVector cameraRotation = new PVector(-0.44f, 0.28f, 0);

    float fNear;
    float fFar;
    float fFov;

    float mv = 0.02f;
    float rv = 0.01f;
    float fv = 0.5f;

    PVector mvx = new PVector(mv, 0, 0);
    PVector mvy = new PVector(0, mv, 0);
    PVector mvz = new PVector(0, 0, mv);

    float originalX;
    float originalY;

    public void settings() {
        size(1600, 900);
    }

    public void setup() {
        fNear = 0.1f;
        fFar = 1000.0f;
        fFov = 75.0f;

        scene.add(new Box(new PVector(2, 0, 2), 1, 2, 1));
        scene.add(new Box(new PVector(-1, 0, 10), 3, 3, 3));
        scene.add(new Box(new PVector(0, 0, 2), .5f, .5f, .5f));
        scene.add(new Box(new PVector(-10, 0, 2), 5, 1, 1));
        scene.add(new Box(new PVector(-5, 0, 5), 5, 2, 3));
        scene.add(new Box(new PVector(-5, 0, -20), 5, 2, 3));
    }

    public void draw() {
        keyBoardRoutine();
        cameraRoutine();

        textSize(14);
        fill(255);
        text("Camera rotation: " + cameraRotation.toString(), 10, 30);
        text("Camera position: " + cameraPosition.toString(), 10, 50);
        text("Field of view: " + fFov, 10, 70);

        originalX = mouseX;
        originalY = mouseY;
    }

    public static void main(String[] args) {
        String[] processingArgs = {"Morpheus"};
        Morpheus morpheus = new Morpheus();
        PApplet.runSketch(processingArgs, morpheus);
    }

    private void cameraRoutine() {
        background(0);
        stroke(255);
        noFill();

        Matrix translationMatrix = Matrix.originTranslationMatrix(cameraPosition);
        Matrix rotationMatrix = Matrix.rotationMatrix(cameraRotation);
        Matrix matProj = Matrix.perspectiveProjectionMatrix(fFov, fNear, fFar, width, height);
        Matrix yReverseMatrix = Matrix.yReverseMatrix();

        Matrix camProjMat = yReverseMatrix
                .multiply(matProj)
                .multiply(rotationMatrix)
                .multiply(translationMatrix);

        for (Mesh m : scene.meshes) {
            for (Triangle t : m.triangles) {
                Triangle tProj = camProjMat.multiply(t);

                if (tProj == null) continue;

                scaleIntoView(tProj);

                strokeWeight(1);
                line(tProj.points[0].x, tProj.points[0].y, tProj.points[1].x, tProj.points[1].y);
                line(tProj.points[0].x, tProj.points[0].y, tProj.points[1].x, tProj.points[1].y);
                line(tProj.points[1].x, tProj.points[1].y, tProj.points[2].x, tProj.points[2].y);
                line(tProj.points[2].x, tProj.points[2].y, tProj.points[0].x, tProj.points[0].y);
            }
        }
    }

    private void keyBoardRoutine() {
        if (keyPressed) {
            PVector invCameraRotation = new PVector(0, 0, 0).sub(cameraRotation);
            Matrix invRotationMatrix = Matrix.invRotationMatrix(invCameraRotation);

            switch (key) {
                case CODED -> {
                    switch (keyCode) {
                        case LEFT -> cameraPosition.sub(invRotationMatrix.multiply(mvx));
                        case RIGHT -> cameraPosition.add(invRotationMatrix.multiply(mvx));
                        case UP -> cameraPosition.add(invRotationMatrix.multiply(mvz));
                        case DOWN -> cameraPosition.sub(invRotationMatrix.multiply(mvz));
                        case SHIFT -> cameraPosition.add(invRotationMatrix.multiply(mvy));
                        case CONTROL -> cameraPosition.sub(invRotationMatrix.multiply(mvy));
                    }
                }
                case 'w' -> cameraRotation.x += rv;
                case 's' -> cameraRotation.x -= rv;
                case 'a' -> cameraRotation.y += rv;
                case 'd' -> cameraRotation.y -= rv;
                case 'q' -> cameraRotation.z -= rv;
                case 'e' -> cameraRotation.z += rv;
                case 'r' -> fFov -= fv;
                case 'f' -> fFov += fv;
                case 'o' -> cameraRotation = new PVector(0, 0,0);
                case 'p' -> cameraPosition = new PVector(0,0,0);
            }

            if (fFov <= 0f) fFov = fv;
            if (fFov >= 180f) fFov = 180f - fv;
        }
    }

    public void mouseDragged() {
        float dx = (originalX - mouseX) * 30 / width;
        float dy = (originalY - mouseY) * 30 / height;

        cameraRotation.y += dx * rv;
        cameraRotation.x += dy * rv;
    }

    private void scaleIntoView(Triangle t) {
        t.points[0].x += 1f;
        t.points[0].y += 1f;
        t.points[1].x += 1f;
        t.points[1].y += 1f;
        t.points[2].x += 1f;
        t.points[2].y += 1f;

        t.points[0].x *= 0.5f * (float) width;
        t.points[0].y *= 0.5f * (float) height;
        t.points[1].x *= 0.5f * (float) width;
        t.points[1].y *= 0.5f * (float) height;
        t.points[2].x *= 0.5f * (float) width;
        t.points[2].y *= 0.5f * (float) height;
    }
}
