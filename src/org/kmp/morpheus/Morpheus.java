package org.kmp.morpheus;

import processing.core.PApplet;
import processing.core.PVector;

public class Morpheus extends PApplet {

    Mesh meshCube = new Mesh();

    PVector cameraPosition = new PVector(0, 0f, -3f);
    PVector cameraRotation = new PVector(0, 0, 0);

    float fNear;
    float fFar;
    float fFov;
    float fAspectRatio;
    float fFovRad;

    float mv = 0.02f;
    float rv = 0.01f;

    PVector mvx = new PVector(mv, 0, 0);
    PVector mvy = new PVector(0, mv, 0);
    PVector mvz = new PVector(0, 0, mv);

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

        fNear = 0.1f;
        fFar = 1000.0f;
        fFov = 90.0f;
        fAspectRatio = (float) height / (float) width;
        fFovRad = 1.0f / (float) Math.tan(fFov * 0.5f / 180.0f * Math.PI);
    }

    public void draw() {
        keyBoardRoutine();
        cameraRoutine();
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

        Matrix translationMatrix = Matrix.translationMatrix(cameraPosition);
        Matrix rotationMatrix = Matrix.rotationMatrix(cameraRotation);
        Matrix yRecerseMatrix = Matrix.yReverseMatrix();
        Matrix matProj = new Matrix();

//        matProj.inner[0][0] = fAspectRatio * fFovRad;
//        matProj.inner[1][1] = fFovRad;
//        matProj.inner[2][2] = fFar / (fFar - fNear);
//        matProj.inner[3][2] = (-fFar * fNear) / (fFar - fNear);
//        matProj.inner[2][3] = 1.0f;
//        matProj.inner[3][3] = 0.0f;

        float aspectRatio = (float) width / height;
        float FOV = 90f;
        float FOVRadTan = (float) Math.tan(Math.toRadians(FOV / 2));

        matProj.inner[0][0] = 1 / (aspectRatio * FOVRadTan);
        matProj.inner[1][1] = 1 / FOVRadTan;
        matProj.inner[2][2] = (-fNear - fFar) / (fNear - fFar);
        matProj.inner[2][3] = 1;
        matProj.inner[3][2] = (2 * fNear * fFar) / (fNear - fFar);

        Matrix camProjMat = yRecerseMatrix
                .multiply(matProj)
                .multiply(rotationMatrix)
                .multiply(translationMatrix);

        for (Triangle t : meshCube.triangles) {
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

    private void keyBoardRoutine() {
        if (keyPressed) {
            PVector invCameraRotation = new PVector(0, 0, 0).sub(cameraRotation);
            Matrix invRotationMatrix = Matrix.invRotationMatrix(invCameraRotation);

            switch (key) {
                case CODED: {
                    switch (keyCode) {
                        case LEFT: {
                            cameraPosition.sub(
                                    invRotationMatrix.multiply(mvx)
                            );
                            break;
                        }
                        case RIGHT: {
                            cameraPosition.add(
                                    invRotationMatrix.multiply(mvx)
                            );
                            break;
                        }
                        case UP: {
                            cameraPosition.add(
                                    invRotationMatrix.multiply(mvz)
                            );
                            break;
                        }
                        case DOWN: {
                            cameraPosition.sub(
                                    invRotationMatrix.multiply(mvz)
                            );
                            break;
                        }
                        case SHIFT: {
                            cameraPosition.add(
                                    invRotationMatrix.multiply(mvy)
                            );
                            break;
                        }
                        case CONTROL: {
                            cameraPosition.sub(
                                    invRotationMatrix.multiply(mvy)
                            );
                            break;
                        }
                    }
                    break;
                }
                case 'w': {
                    cameraRotation.x += rv;
                    break;
                }
                case 's': {
                    cameraRotation.x -= rv;
                    break;
                }
                case 'a': {
                    cameraRotation.y += rv;
                    break;
                }
                case 'd': {
                    cameraRotation.y -= rv;
                    break;
                }
                case 'q': {
                    cameraRotation.z -= rv;
                    break;
                }
                case 'e': {
                    cameraRotation.z += rv;
                    break;
                }
            }
        }
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
