package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;
import org.hjujgfg.dracer.world.interfaces.TypedModel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.gameplay.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.world.models.Materials.createExtraSilver;
import static org.hjujgfg.dracer.world.models.Materials.createSilver;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class Floor implements ModelSupplier, RenderAction, TypedModel {

    private final static Model floorPlane;
    private final List<ModelInstance> floorPlanes = new LinkedList<>();

    private final static Vector3 buffer = new Vector3();

    static {
        floorPlane = MODEL_BUILDER.createBox(0.2f, 10f, 10.5f,
                /*new Material(
                        ColorAttribute.createDiffuse(Color.WHITE),
                        ColorAttribute.createSpecular(Color.WHITE),
                        FloatAttribute.createShininess(128f)),*/
                createExtraSilver(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
    }

    public Floor() {
        for (int i = 0; i < 10; i ++) {
            ModelInstance instance = new ModelInstance(floorPlane);
            instance.transform.setTranslation(0, 10 * i + 1, 0);
            floorPlanes.add(instance);
        }
    }

    @Override
    public Collection<ModelInstance> getModels() {
        return floorPlanes;
    }

    @Override
    public void dispose() {
        floorPlane.dispose();
    }

    @Override
    public void render() {
        for (int i = 0; i < floorPlanes.size(); i ++) {
            moveOneFloorPlane(floorPlanes.get(i), i);
        }
    }

    private void moveOneFloorPlane(ModelInstance fl, int index) {
        Vector3 position;
        position = fl.transform.getTranslation(new Vector3());
        /*if (x == 0 && y == 1) {
            Gdx.app.log("FLOOR", String.format("Floor now at %f %f %f ",
                    position.x, position.y, position.z));
        }*/
        if (bigger(-10, position.y, 0.00001f)) {
            float last = findLastFloorPlane(index);
            fl.transform.setTranslation(-50, last + 10.1f, position.z);
        } else {
            fl.transform.translate(0, - PROBLEM_SPEED.get(), 0);
        }
        if (bigger(position.x, 0, 0.00001f)) {
            fl.transform.translate(- Math.min(Math.abs(PROBLEM_SPEED.get()), position.x),
                    0,
                    0);
        }
        if (bigger(0, position.x, 0.00001f)) {
            fl.transform.translate(-Math.min(Math.abs(PROBLEM_SPEED.get()), position.x),
                    0,
                    0);
        }
    }

    private float findLastFloorPlane(int currentIndex) {
        float max = Float.MIN_VALUE;

        for (int i = 0; i < floorPlanes.size(); i ++) {
            if (i == currentIndex) {
                continue;
            }
            float tmp = floorPlanes.get(i).transform.getTranslation(buffer).y;
            if (bigger(tmp, max, 0.00001f)) {
                max = tmp;
            }
        }
        return max;
    }

    @Override
    public ModelType getType() {
        return ModelType.FLOOR;
    }
}
