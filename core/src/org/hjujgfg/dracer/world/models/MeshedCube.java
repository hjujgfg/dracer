package org.hjujgfg.dracer.world.models;

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

import static org.hjujgfg.dracer.gameplay.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.models.Materials.createSilver;
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
    Map<Integer, Float> lastTiles = new HashMap<>();

    List<ModelInstance> instances = new ArrayList<>();

    public MeshedCube() {
        int width = 1;
        int height = 50;
        int length = 5;
        int numCubes = width*height*length;

        MODEL_BUILDER.begin();
        MeshPartBuilder mpb = MODEL_BUILDER.part("cubes", GL20.GL_TRIANGLES,
                (VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.ColorPacked),
                createSilver());
        for (int i = 0; i < numCubes; i++) {
            BoxShapeBuilder.build(mpb, 0.1f, SIDE, SIDE);
            //mpb.box(0.1f, SIDE, SIDE);
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
                    mBatchedCubes.add(c);
                    c.update(mBatchedCubesVertices);
                    if (y == height - 1) {
                        lastTiles.put( (int ) (z * BETWEEN), (float) y);
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
        for (Cube c : mBatchedCubes) {
            if (bigger(-10, c.position.y)) {
                Float last = lastTiles.getOrDefault((int) c.position.z, 100f);
                lastTiles.put((int) c.position.z, last);
                c.translateTo(c.position.x, last + BETWEEN, c.position.z);
            } else {
                c.translate(0, - PROBLEM_SPEED.get(), 0);
            }
            c.update(mBatchedCubesVertices);
        }
        mBatchedCubesMesh.updateVertices(0, mBatchedCubesVertices);
    }
}
