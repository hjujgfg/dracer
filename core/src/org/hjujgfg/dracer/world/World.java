package org.hjujgfg.dracer.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.hjujgfg.dracer.world.BigStatic.MODEL_BUILDER;

public class World implements ModelSupplier {
    private final static Model floorPlane, houseModel;
    private final static List<ModelInstance> floorPlanes = new LinkedList<>();
    private final static List<ModelInstance> houses = new LinkedList<>();
    private final static Random r = new Random();

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
        for (int i = 0; i < 100; i ++) {
            ModelInstance instance = new ModelInstance(floorPlane);
            instance.transform.setTranslation(0, 10 * i + 1, 0);
            floorPlanes.add(instance);
        }

        for (int i = 0; i < 100; i += 5) {
            ModelInstance house = new ModelInstance(houseModel);
            boolean is_left = r.nextInt(2) % 2 == 0;
            int modifier;
            if (is_left) {
                modifier = 1;
            } else {
                modifier = -1;
            }
            house.transform.setToTranslation(0, r.nextInt(5) * i, modifier * (20 + r.nextInt(50)));
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
}
