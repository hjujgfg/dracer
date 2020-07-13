package org.hjujgfg.dracer.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.params.ProblemSpeed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class Floor implements ModelSupplier, RenderAction {

    private final static Model floorPlane, houseModel;
    private final static List<ModelInstance> floorPlanes = new LinkedList<>();
    private final static List<ModelInstance> houses = new LinkedList<>();
    private final static Random r = new Random();

    private final static Vector3 buffer = new Vector3();

    static {
        floorPlane = MODEL_BUILDER.createBox(0.2f, 10f, 7,
                new Material(
                        ColorAttribute.createDiffuse(Color.LIGHT_GRAY),
                        ColorAttribute.createSpecular(Color.WHITE),
                        FloatAttribute.createShininess(128f)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        houseModel = MODEL_BUILDER.createBox(50, 30, 20,
                new Material(ColorAttribute.createDiffuse(Color.FIREBRICK),
                        ColorAttribute.createReflection(Color.RED),
                        ColorAttribute.createSpecular(Color.WHITE),
                        FloatAttribute.createShininess(64f)),
                VertexAttributes.Usage.Position);
        for (int i = 0; i < 5; i ++) {
            ModelInstance instance = new ModelInstance(floorPlane);
            instance.transform.setTranslation(0, 10 * i + 1, 0);
            floorPlanes.add(instance);
        }

        for (int i = 0; i < 5; i ++) {
            ModelInstance house = new ModelInstance(houseModel);
            boolean is_left = r.nextInt(2) % 2 == 0;
            int modifier;
            if (is_left) {
                modifier = 1;
            } else {
                modifier = -1;
            }
            house.transform.setToTranslation(0, r.nextInt(200), modifier * (20 + r.nextInt(50)));
            houses.add(house);
        }

    }


    @Override
    public Collection<ModelInstance> getModels() {
        ArrayList<ModelInstance> all = new ArrayList<>();
        all.addAll(floorPlanes);
        all.addAll(houses);
        return all;
    }

    @Override
    public void render() {
        for (int i = 0; i < floorPlanes.size(); i ++) {
            moveOneFloorPlane(floorPlanes.get(i), i, 1, i);
        }
        for (int i = 0; i < houses.size(); i ++) {
            moveOneFloorPlane(houses.get(i), 0, 4, i);
        }
    }

    private void moveOneFloorPlane(ModelInstance fl, int x, int y, int index) {
        Vector3 position;
        position = fl.transform.getTranslation(new Vector3());
        /*if (x == 0 && y == 1) {
            Gdx.app.log("FLOOR", String.format("Floor now at %f %f %f ",
                    position.x, position.y, position.z));
        }*/
        if (bigger(-10, position.y, 0.00001f)) {
            float last = findLastFloorPlane(index);
            fl.transform.setTranslation(0, last + 12, position.z);
        } else {
            fl.transform.translate(0, - PROBLEM_SPEED.get(), 0);
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


    private void moveHouse(ModelInstance houseInstance) {
        Vector3 position;
        position = houseInstance.transform.getTranslation(new Vector3());
		/*if (x == 3) {
			Gdx.app.log("FLOOR", String.format("Floor now at %f %f %f ",
					position.x, position.y, position.z));
		}*/
        if (position.y < - 10) {
            houseInstance.transform.setTranslation(0, 23 - position.y, 0);
        } else {
            houseInstance.transform.translate(0, 0, - PROBLEM_SPEED.get());
        }
    }
}
