package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.gameplay.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.gameplay.BigStatic.RANDOM;
import static org.hjujgfg.dracer.world.models.Materials.createExtraSilver;
import static org.hjujgfg.dracer.world.models.Materials.createPolishedSilver;
import static org.hjujgfg.dracer.world.models.Materials.createSlightlyColoredSilver;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class TiledFlor implements RenderAction, ModelSupplier {

    private final static int ROWS = 30;
    private final static int COLS = 5;
    private final static int SIDE = 2;
    private final static float BETWEEN = SIDE + 0.1f;

    private final static Model floorPlane;

    private List<ModelInstance> planes;

    private ModelInstance[] lastPlanes = new ModelInstance[COLS];

    static {
        floorPlane = MODEL_BUILDER.createBox(0.1f, SIDE, SIDE,
                createSlightlyColoredSilver(),
                VertexAttributes.Usage.Normal |
                        VertexAttributes.Usage.Position |
                        VertexAttributes.Usage.TextureCoordinates
        );
    }

    public TiledFlor() {
        planes = new ArrayList<>();
        for (int i = 0; i < ROWS; i ++) {
            for (int j = -2; j < COLS - 2; j ++) {
                ModelInstance newOne = new ModelInstance(floorPlane);
                newOne.transform.setToTranslation(0, i * BETWEEN, j * BETWEEN);
                planes.add(newOne);
                if (i == ROWS - 1) {
                    lastPlanes[j + 2] = newOne;
                }
            }
        }
    }

    @Override
    public Collection<ModelInstance> getModels() {
        return planes;
    }

    @Override
    public void dispose() {
        floorPlane.dispose();
    }

    @Override
    public void render() {
        planes.forEach(this::moveOneFloorPlane);
    }

    private void moveOneFloorPlane(ModelInstance fl) {
        Vector3 position;
        position = fl.transform.getTranslation(new Vector3());
        /*if (x == 0 && y == 1) {
            Gdx.app.log("FLOOR", String.format("Floor now at %f %f %f ",
                    position.x, position.y, position.z));
        }*/
        if (bigger(-20f, position.y, 0.00001f)) {
            int index = (int) (position.z / BETWEEN) + 2;
            float last = lastPlanes[index].transform.getTranslation(new Vector3()).y;
            //fl.transform.setTranslation(11 + RANDOM.nextInt(50), last + 2.1f, position.z);
            fl.transform.setToTranslation( - RANDOM.nextInt(20), last + BETWEEN, position.z);
            lastPlanes[index] = fl;
        } else {
            fl.transform.translate(0, - PROBLEM_SPEED.get(), 0);
        }
        if (bigger(0, position.x, 0.00001f)) {
            fl.transform.translate(Math.min(Math.abs(PROBLEM_SPEED.get() * 2), Math.abs(position.x)),
                    0,
                    0);
        }
        if (bigger(position.x, 0, 0.00001f)) {
            fl.transform.translate( - Math.min(Math.abs(PROBLEM_SPEED.get() * 2), Math.abs(position.x)),
                    0,
                    0);
        }
    }

}
