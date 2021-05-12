package org.kmp.morpheus;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.*;
import java.util.stream.Collectors;

public class Morpheus extends PApplet {
    Scene scene = new Scene();

    PVector cameraPosition = new PVector(0, 5.11f, -3f);
    PVector cameraRotation = new PVector(-0.44f, 0.28f, 0);

//    PVector cameraPosition = new PVector(0, 0, -3f);
//    PVector cameraRotation = new PVector(0, 0, 0);

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

        List<Triangle> filtered = scene.triangles.stream().filter(t -> {
            PVector pC = cameraPosition.copy();
            PVector p0 = t.points[0].copy();
            PVector vPQ = new PVector(t.points[1].x - t.points[0].x, t.points[1].y - t.points[0].y, t.points[1].z - t.points[0].z);
            PVector vPR = new PVector(t.points[2].x - t.points[0].x, t.points[2].y - t.points[0].y, t.points[2].z - t.points[0].z);

            PVector normal = new PVector();
            PVector.cross(vPQ, vPR, normal);

            return PVector.dot(normal, PVector.sub(pC, p0)) > 0;
        })
                .peek(t -> t.projectedPoints = camProjMat.multiply(t.points)).filter(t -> t.projectedPoints != null)
                .map(this::scaleIntoViewWithReturn)
                .collect(Collectors.toList());

        try {
            scene.triangles.sort((t1, t2) -> {
                PVector pC = cameraPosition.copy();
                int count0 = 0;
                int count1 = 0;

                // First Run
                PVector p0 = t2.points[0].copy();
                PVector vPQ = new PVector(t2.points[1].x - t2.points[0].x, t2.points[1].y - t2.points[0].y, t2.points[1].z - t2.points[0].z);
                PVector vPR = new PVector(t2.points[2].x - t2.points[0].x, t2.points[2].y - t2.points[0].y, t2.points[2].z - t2.points[0].z);

                PVector normal = new PVector();
                PVector.cross(vPQ, vPR, normal);

                for (PVector point : t1.points) {
                    PVector l0 = point.copy();
                    PVector l = PVector.sub(pC, l0);

                    float topPart = PVector.dot(PVector.sub(p0, l0), normal);
                    float bottomPart = PVector.dot(l, normal);

                    if (bottomPart == 0) {
                        continue;
                    }

                    float d = topPart / bottomPart;

                    PVector pI = PVector.add(l0, PVector.mult(l, d));

                    PVector v0 = PVector.sub(l0, pC);
                    PVector v1 = PVector.sub(pI, pC);

                    if (PVector.dot(v0, v1) < 0) continue;

                    float pC_l0 = pC.dist(l0);
                    float pC_pI = pC.dist(pI);

                    if (pC_l0 < pC_pI) count0++;
                }

                // Second Run
                p0 = t1.points[0].copy();
                vPQ = new PVector(t1.points[1].x - t1.points[0].x, t1.points[1].y - t1.points[0].y, t1.points[1].z - t1.points[0].z);
                vPR = new PVector(t1.points[2].x - t1.points[0].x, t1.points[2].y - t1.points[0].y, t1.points[2].z - t1.points[0].z);

                normal = new PVector();
                PVector.cross(vPQ, vPR, normal);

                for (PVector point : t2.points) {
                    PVector l0 = point.copy();
                    PVector l = PVector.sub(pC, l0);

                    float topPart = PVector.dot(PVector.sub(p0, l0), normal);
                    float bottomPart = PVector.dot(l, normal);

                    if (bottomPart == 0) {
                        continue;
                    }

                    float d = topPart / bottomPart;

                    PVector pI = PVector.add(l0, PVector.mult(l, d));

                    PVector v0 = PVector.sub(l0, pC);
                    PVector v1 = PVector.sub(pI, pC);

                    if (PVector.dot(v0, v1) < 0) continue;

                    float pC_l0 = pC.dist(l0);
                    float pC_pI = pC.dist(pI);

                    if (pC_l0 < pC_pI) count1++;
                }

                return Integer.compare(count0, count1);
            });
        } catch (Exception ignored) {
            System.out.println("PANIC!!!");
        }

        int tr_count = 0;
        for (Triangle t : filtered) {


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

            tr_count++;
        }
        strokeWeight(2);
        fill(255);
        text("Triangles on screen: " + tr_count, 10, 90);
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

    private Triangle scaleIntoViewWithReturn(Triangle t) {
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

        return t;
    }
}
