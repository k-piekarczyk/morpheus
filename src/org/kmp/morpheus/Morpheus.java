package org.kmp.morpheus;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.*;
import java.util.stream.Collectors;

public class Morpheus extends PApplet {
    Scene scene = new Scene();

//    PVector cameraPosition = new PVector(0, 5.11f, -3f);
//    PVector cameraRotation = new PVector(-0.44f, 0.28f, 0);

    PVector cameraPosition = new PVector(0, 0, 0);
    PVector cameraRotation = new PVector(0, 0, 0);

    float fFov;

    float mv = 0.5f;
    float rv = 0.05f;
    float fv = 0.5f;

    PVector mvx = new PVector(mv, 0, 0);
    PVector mvy = new PVector(0, mv, 0);
    PVector mvz = new PVector(0, 0, mv);

    float originalX;
    float originalY;

    List<Sphere> sphereList = new ArrayList<>();

    public void settings() {
        size(1000, 500);
    }


    PVector cameraOrigin = new PVector(0, 0, 0);


    float aspectRatio;

    public void setup() {
        fFov = 120.0f;
        aspectRatio = (float) width / height;
        sphereList.add(new Sphere(0, 0, -10, 4, new Color(255, 0, 0)));
        sphereList.add(new Sphere(1, 5, -14, 3, new Color(0, 255, 0)));
        sphereList.add(new Sphere(-3, -2, -5, 1, new Color(0, 0, 255)));
    }

    public void draw() {
        keyBoardRoutine();
//        oldCameraRoutine();
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

        Matrix translationMatrix = Matrix.originTranslationMatrix(cameraPosition);
        Matrix rotationMatrix = Matrix.rotationMatrix(cameraRotation);
        Matrix yReverseMatrix = Matrix.yReverseMatrix();

        Matrix cameraToWorld = yReverseMatrix
                .multiply(rotationMatrix)
                .multiply(translationMatrix);

        PVector cco = cameraToWorld.multiply(cameraOrigin);
        loadPixels();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                PVector direction = cameraToWorld.multiply(generateCameraSpacePixel(x, y));
                Color currentColor = new Color();
                float currentSolution = Float.POSITIVE_INFINITY;
                boolean solutionFound = false;
                for (Sphere sphere : sphereList) {
                    float x1 = cco.x;
                    float y1 = cco.y;
                    float z1 = cco.z;

                    float x2 = direction.x;
                    float y2 = direction.y;
                    float z2 = direction.z;

                    float x3 = sphere.origin.x;
                    float y3 = sphere.origin.y;
                    float z3 = sphere.origin.z;

                    float r = sphere.radius;

                    float a = (float) (Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2 - z1, 2));

                    float b = 2 * (((x2 - x1) * (x1 - x3)) + ((y2 - y1) * (y1 - y3)) + ((z2 - z1) * (z1 - z3)));

                    float c = (float) (Math.pow(x3, 2) + Math.pow(y3, 2) + Math.pow(z3, 2) + Math.pow(x1, 2) + Math.pow(y1, 2) + Math.pow(z1, 2) - (2 * ((x3 * x1) + (y3 * y1) + (z3 * z1))) - Math.pow(r, 2));

                    float part = (float) (Math.pow(b, 2) - (4 * a * c));

                    float solution = Float.POSITIVE_INFINITY;
                    if (part < 0f) {
                        continue;
                    } else if (part == 0f) {
                        solution = (-b) / (2 * a);
                    } else if (part > 0f) {
                        float s1 = (float) ((-b) + Math.sqrt(part));
                        float s2 = (float) ((-b) - Math.sqrt(part));

                        if (s1 >= 0 && s1 < s2) solution = s1;
                        else if (s2 >= 0 && s2 < s1) solution = s2;
                        else continue;
                    }

                    if (solution < currentSolution) {
                        currentSolution = solution;
                        currentColor = sphere.color;
                        solutionFound = true;
                    }
                }

                int idx = x + (width * y);

                if(solutionFound) pixels[idx] = color(currentColor.r, currentColor.g, currentColor.b);
            }
        }
        updatePixels();
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
                        case SHIFT -> cameraPosition.sub(mvy);
                        case CONTROL -> cameraPosition.add(mvy);
                    }
                }
                case 'w' -> cameraPosition.add(mvz);
                case 's' -> cameraPosition.sub(mvz);
                case 'a' -> cameraPosition.add(mvx);
                case 'd' -> cameraPosition.sub(mvx);
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

    private float pixelNDCx(float x) {
        return (x + 0.5f) / width;
    }

    private float pixelNDCy(float y) {
        return (y + 0.5f) / height;
    }



    private float pixelScreenX(int x) {
        return ((2 * pixelNDCx(x)) - 1) * aspectRatio * (float) Math.tan(Math.toRadians(fFov) / 2);
    }

    private float pixelScreenY(int y) {
        return (1 - (2 * pixelNDCy(y))) * (float) Math.tan(Math.toRadians(fFov) / 2);
    }

    private PVector generateCameraSpacePixel(int x, int y) {
        return new PVector(pixelScreenX(x), pixelScreenY(y), -1);
    }
}
