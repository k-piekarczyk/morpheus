package org.kmp.morpheus;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

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

    Edge currentHLine = new Edge();

    public void settings() {
        size(1600, 900);
    }

    public void setup() {
        fNear = 0.1f;
        fFar = 1000.0f;
        fFov = 75.0f;

//        scene.add(new Box(new PVector(2, 0, 2), 1, 2, .2f));
        scene.add(new Box(new PVector(-1, 0, 5.5f), 3, 3, .2f));
//        scene.add(new Box(new PVector(0, 0, 2), .5f, .5f, .2f));
        scene.add(new Box(new PVector(-10, 0, 4.5f), 5, 1, .2f));
        scene.add(new Box(new PVector(-5, 0, 5), 5, 2, .2f));
//        scene.add(new Box(new PVector(-5, 0, -20), 5, 2, .2f));

        currentHLine.start.x = 0;
        currentHLine.end.x = width;
        currentHLine.start.y = 0;
        currentHLine.end.y = 0;
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
//        noStroke();
        Matrix translationMatrix = Matrix.originTranslationMatrix(cameraPosition);
        Matrix rotationMatrix = Matrix.rotationMatrix(cameraRotation);
        Matrix matProj = Matrix.perspectiveProjectionMatrix(fFov, fNear, fFar, width, height);
        Matrix yReverseMatrix = Matrix.yReverseMatrix();

        Matrix camProjMat = yReverseMatrix
                .multiply(matProj)
                .multiply(rotationMatrix)
                .multiply(translationMatrix);

        ListIterator<Triangle> iterator = scene.triangles.listIterator();
        while (iterator.hasNext())
        {
            iterator.set(iterator.next().setDistanceByCopy(cameraPosition));
        }

        Collections.sort(scene.triangles);

        for (Triangle t : scene.triangles) {
            t.projectedPoints = camProjMat.multiply(t.points);

            if (t.projectedPoints == null) continue;

            scaleIntoView(t);

            switch (t.color) {
                case "red" -> fill(255, 0, 0);
                case "green" -> fill(0, 255, 0);
                case "blue" -> fill(0, 0, 255);
                default -> fill(0);
            }
            strokeWeight(1);
            triangle(
                    t.projectedPoints[0].x, t.projectedPoints[0].y,
                    t.projectedPoints[1].x, t.projectedPoints[1].y,
                    t.projectedPoints[2].x, t.projectedPoints[2].y
            );
        }

        scene.refreshEdges();

        stroke(255);
        strokeWeight(1);
        line(currentHLine.start.x, currentHLine.start.y, currentHLine.end.x, currentHLine.end.y);
        line(width/2,0,width/2, height);
        line(0,height/2,width, height/2);
        circle(width/2, height/2, 10);

        currentHLine.start.y = (currentHLine.start.y + 2) % height;
        currentHLine.end.y = (currentHLine.end.y + 2) % height;

        for (Edge e : scene.edges) {
            PVector intersection = currentHLine.findIntersection(e);

            if (intersection != null) {
                circle(intersection.x, intersection.y, 5);
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
                        case LEFT -> cameraRotation.y += rv;
                        case RIGHT -> cameraRotation.y -= rv;
                        case UP -> cameraRotation.x += rv;
                        case DOWN -> cameraRotation.x -= rv;
                        case SHIFT -> cameraPosition.add(invRotationMatrix.multiply(mvy));
                        case CONTROL -> cameraPosition.sub(invRotationMatrix.multiply(mvy));
                    }
                }
                case 'w' -> cameraPosition.add(invRotationMatrix.multiply(mvz));
                case 's' -> cameraPosition.sub(invRotationMatrix.multiply(mvz));
                case 'a' -> cameraPosition.sub(invRotationMatrix.multiply(mvx));
                case 'd' -> cameraPosition.add(invRotationMatrix.multiply(mvx));
                case 'q' -> cameraRotation.z -= rv;
                case 'e' -> cameraRotation.z += rv;
                case 'r' -> fFov -= fv;
                case 'f' -> fFov += fv;
                case 'o' -> cameraRotation = new PVector(0, 0, 0);
                case 'p' -> cameraPosition = new PVector(0, 0, 0);
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
        t.projectedPoints[0].x += 1f;
        t.projectedPoints[0].y += 1f;
        t.projectedPoints[1].x += 1f;
        t.projectedPoints[1].y += 1f;
        t.projectedPoints[2].x += 1f;
        t.projectedPoints[2].y += 1f;

        t.projectedPoints[0].x *= 0.5f * (float) width;
        t.projectedPoints[0].y *= 0.5f * (float) height;
        t.projectedPoints[1].x *= 0.5f * (float) width;
        t.projectedPoints[1].y *= 0.5f * (float) height;
        t.projectedPoints[2].x *= 0.5f * (float) width;
        t.projectedPoints[2].y *= 0.5f * (float) height;
    }
}
