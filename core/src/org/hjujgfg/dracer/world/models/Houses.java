package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;

import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.gameplay.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.gameplay.BigStatic.RANDOM;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class Houses implements ModelSupplier, RenderAction {

    private final static Model houseModel;
    private final List<ModelInstance> houses = new LinkedList<>();

    static {
        houseModel = MODEL_BUILDER.createBox(50, 30, 20,
                new Material(ColorAttribute.createDiffuse(new Color(
                173.f/255.f, 245.f/255.f, 255.f/255.f, 1f
                )),
                        ColorAttribute.createReflection(Color.RED),
                        ColorAttribute.createSpecular(Color.WHITE),
                        FloatAttribute.createShininess(128f)
                         ,
                        PBRTextureAttribute.createDiffuse(new Texture("brick.png"))
                ),
                VertexAttributes.Usage.Position);
    }

    public Houses() {

        for (int i = 0; i < 5; i ++) {
            ModelInstance house = new ModelInstance(houseModel);
            boolean is_left = RANDOM.nextInt(2) % 2 == 0;
            int modifier;
            if (is_left) {
                modifier = 1;
            } else {
                modifier = -1;
            }
            house.transform.setToTranslation(0, RANDOM.nextInt(200), modifier * (20 + RANDOM.nextInt(50)));
            houses.add(house);
        }
    }

    @Override
    public Collection<ModelInstance> getModels() {
        return houses;
    }

    @Override
    public void dispose() {
        houseModel.dispose();
    }

    @Override
    public void render() {
        for (int i = 0; i < houses.size(); i ++) {
            moveHouse(houses.get(i));
        }
    }

    private void moveHouse(ModelInstance fl) {
        Vector3 position;
        position = fl.transform.getTranslation(new Vector3());
        /*if (x == 0 && y == 1) {
            Gdx.app.log("FLOOR", String.format("Floor now at %f %f %f ",
                    position.x, position.y, position.z));
        }*/
        boolean is_left = RANDOM.nextInt(2) % 2 == 0;
        int mult = is_left ? 1 : -1;
        if (bigger(-50, position.y, 0.00001f)) {
            fl.transform.setTranslation(50, 100 + RANDOM.nextInt(10), mult * position.z);
        } else {
            fl.transform.translate(0, - PROBLEM_SPEED.get(), 0);
        }
        fl.transform.translate(- 0.9f,
                0,
                0);

        /*float x = Gdx.input.getAccelerometerX();
        float y = Gdx.input.getAccelerometerY();
        float z = Gdx.input.getAccelerometerZ();
        Gdx.app.log("ACCEL", String.format("ACCEL is %f %f %f ",
                x, y, z));*/

    }

}
