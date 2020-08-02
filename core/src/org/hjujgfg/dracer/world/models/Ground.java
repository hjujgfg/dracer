package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.interfaces.LightSupplier;
import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class Ground implements RenderAction, ModelSupplier, LightSupplier<PointLight> {

    private final static Model groundModel;

    private List<ModelInstance> ground;

    private PointLight leftLight;

    static {
        Texture texture = new Texture("brick.png");
        texture.setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        TextureRegion imgTextureRegion = new TextureRegion(texture);
        imgTextureRegion.setRegion(0,0,texture.getWidth() * 3,texture.getHeight() * 3);
        groundModel = MODEL_BUILDER.createBox(2, 8, 10,
                new Material(
                        /*ColorAttribute.createDiffuse(new Color(
                        173.f/255.f, 245.f/255.f, 255.f/255.f, 1f
                        )),*/
                        ColorAttribute.createDiffuse(Color.GRAY),
                        ColorAttribute.createReflection(Color.WHITE),
                        ColorAttribute.createSpecular(Color.WHITE),
                        FloatAttribute.createShininess(1024)

                        //,
                        //TextureAttribute.createDiffuse(imgTextureRegion)
                ),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
    }

    public Ground() {
        ground = new ArrayList<>();

        ModelInstance right = new ModelInstance(groundModel);
        right.transform.setToTranslation(0, 28, -12);
        //ground.add(left);
        for (int i = 0; i < 10; i ++) {
            ModelInstance left = new ModelInstance(groundModel);
            left.transform.setToTranslation(0, i * 8.1f, 12);
            ground.add(left);
        }
        ground.add(right);

        leftLight = new PointLight();
        leftLight.set(new Color(117 / 255.f, 255/255.f, 239/255.f, 1f),
                2, 5, 9, 20f);
    }

    @Override
    public Collection<ModelInstance> getModels() {
        return ground;
    }

    @Override
    public void dispose() {
        groundModel.dispose();
    }

    @Override
    public void render() {
        //rotate();
        moveLight();
    }

    @Override
    public PointLight getLight() {
        return leftLight;
    }

    private void rotate() {
        float accelY = Gdx.input.getAccelerometerY();
        for (ModelInstance i : ground) {
            i.transform.rotate(new Vector3(0, 1,0), accelY * 0.5f);
        }
    }

    private void moveLight() {
        Vector3 position = leftLight.position;
        if (bigger(-20, position.y)) {
            position.set(6, 25, 15);
        } else {
            position.add(0, - PROBLEM_SPEED.get() / 2, 0);
        }
    }
}
