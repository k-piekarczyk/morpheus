package org.kmp.morpheus;

import java.util.ArrayList;

public class Scene {
    public ArrayList<Mesh> meshes = new ArrayList<>();
    public ArrayList<Triangle> triangles = new ArrayList<>();

    public void add(Mesh mesh) {
        meshes.add(mesh);
        triangles.addAll(mesh.triangles);
    }
}
