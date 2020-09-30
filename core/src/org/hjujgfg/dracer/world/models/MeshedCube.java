package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.hjujgfg.dracer.gameplay.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.models.Materials.createChip;
import static org.hjujgfg.dracer.world.models.Materials.createClouds;
import static org.hjujgfg.dracer.world.models.Materials.createExtraSilver;
import static org.hjujgfg.dracer.world.models.Materials.createNeonGrid;
import static org.hjujgfg.dracer.world.models.Materials.createNeonSilver;
import static org.hjujgfg.dracer.world.models.Materials.createSilver;
import static org.hjujgfg.dracer.world.models.Materials.createSun;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class MeshedCube implements RenderAction, ModelSupplier {

    private final static int ROWS = 30;
    private final static int COLS = 5;
    private final static int SIDE = 2;
    private final static float BETWEEN = SIDE + 0.1f;

    ModelInstance mBatchedCubesModelInstance;
    Mesh mBatchedCubesMesh;
    float[] mBatchedCubesVertices;
    Array<Cube> mBatchedCubes;
    Model model;

    List<ModelInstance> instances = new ArrayList<>();
    Map<Cube, Integer> cubeToLine;
    Map<Integer, Cube> lineToLastCube;

    public MeshedCube() {
        int width = 1;
        int height = 50;
        int length = 5;
        int numCubes = width*height*length;
        cubeToLine = new HashMap<>();
        lineToLastCube = new HashMap<>();

        MODEL_BUILDER.begin();
        MeshPartBuilder mpb = MODEL_BUILDER.part("cubes", GL20.GL_TRIANGLES,
                (VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates),
                createNeonSilver());
        for (int i = 0; i < numCubes; i++) {
            BoxShapeBuilder.build(mpb, 0.1f, SIDE, SIDE);
        }
        model = MODEL_BUILDER.end();
        mBatchedCubesModelInstance = new ModelInstance(model);
        instances.add(mBatchedCubesModelInstance);
        mBatchedCubesMesh = model.meshes.get(0);
        VertexAttributes vertexAttributes = mBatchedCubesMesh.getVertexAttributes();
        int vertexFloatSize = vertexAttributes.vertexSize / 4; //4 bytes per float
        mBatchedCubesVertices = new float[numCubes * 24 * vertexFloatSize]; //24 unique vertices per cube

        mBatchedCubes = new Array<>(numCubes);
        int cubeNum = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = - length / 2; z <= length / 2; z++) {
                    Cube c = new Cube(
                            //(x - (width / 2f)) * 1.5f, ((y - (height / 2f)) * 1.5f), -(z - (length / 2f)) * 1.5f,
                            0, y * BETWEEN, z * BETWEEN,
                            0.1f, SIDE, SIDE, cubeNum++,
                            vertexAttributes);
                    c.setColor(Color.WHITE);
                    mBatchedCubes.add(c);
                    c.update(mBatchedCubesVertices);
                    cubeToLine.put(c, z);
                    if (y == height - 1) {
                        lineToLastCube.put(z, c);
                    }
                }
            }
        }
        mBatchedCubesMesh.setVertices(mBatchedCubesVertices); //apply changes to mesh
    }


    @Override
    public Collection<ModelInstance> getModels() {
        return instances;
    }

    @Override
    public void dispose() {
        model.dispose();
    }

    @Override
    public void render() {
        /*for (Cube c : mBatchedCubes) {
            c.translate(MathUtils.random() * MathUtils.randomSign(), 0, 0);
            c.update(mBatchedCubesVertices);
        }
        mBatchedCubesMesh.updateVertices(0, mBatchedCubesVertices);*/
        move();
    }

    private void move() {
        float speed = PROBLEM_SPEED.get();
        for (int i = mBatchedCubes.size - 1; i >= 0; i --) {
            Cube c = mBatchedCubes.get(i);
            if (bigger(-20, c.position.y)) {
                int targetLine = cubeToLine.get(c);
                Cube lastCube = lineToLastCube.get(targetLine);
                //float last = Math.max(lastCube.position.y, 75f);
                float last = lastCube.position.y;
                lineToLastCube.put(targetLine, c);
                //Gdx.app.log("Tile:", "Last y: " + last);
                c.translateTo(c.position.x, Math.round((last + BETWEEN) * 1000f) / 1000f, c.position.z);
            }
            c.translate(0, - speed, 0);
            c.update(mBatchedCubesVertices);
        }
        mBatchedCubesMesh.updateVertices(0, mBatchedCubesVertices);
    }
}
