package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.SphereShapeBuilder;

import org.hjujgfg.dracer.world.interfaces.LightSupplier;
import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import java.util.ArrayList;
import java.util.Collection;

import static com.badlogic.gdx.graphics.GL20.GL_TRIANGLES;
import static org.hjujgfg.dracer.gameplay.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.world.models.Materials.createClouds;
import static org.hjujgfg.dracer.world.models.Materials.createSilver;
import static org.hjujgfg.dracer.world.models.Materials.createSun;

public class Sun implements ModelSupplier, RenderAction, LightSupplier<DirectionalLight> {

    private Model model, cloudModel;
    private ModelInstance modelInstance;
    private ModelInstance second;
    private Collection<ModelInstance> instances;
    private DirectionalLight directionalLight;

    public Sun() {
        model = createModel(createSun());
        modelInstance = new ModelInstance(model, 0, 150, 0);
        cloudModel = createModel(createClouds());
        second = new ModelInstance(cloudModel, 0, 150, 0);
        second.transform.scale(1.01f, 1.01f, 1.01f);
        directionalLight = new DirectionalLight();
        directionalLight.set(Color.WHITE, -1f, -0.8f, -0.2f);
        /*model = MODEL_BUILDER.createSphere(80, 80, 80, 2, 2, GL_TRIANGLES, createSilver(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        modelInstance = new ModelInstance(model, 0, 0, 0);*/
        instances = new ArrayList<>();
        instances.add(modelInstance);
    }

    @Override
    public Collection<ModelInstance> getModels() {
        return instances;
    }

    public ModelInstance getAtmosphere() {
        return second;
    }

    @Override
    public void dispose() {
        model.dispose();
    }

    @Override
    public DirectionalLight getLight() {
        return directionalLight;
    }

    @Override
    public void render() {
        rotate();
    }

    private Model createModel(Material material) {
        MODEL_BUILDER.begin();
        MeshPartBuilder mpb = MODEL_BUILDER.part("parts", GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Position
                        | VertexAttributes.Usage.Normal
                        //| VertexAttributes.Usage.ColorUnpacked
                        | VertexAttributes.Usage.TextureCoordinates,
                material);
        //mpb.setColor(1f, 0f, 1f, 1f);
        SphereShapeBuilder.build(mpb, 120, 120, 120, 50, 50);
        //mpb.sphere(2f, 2f, 2f, 10, 10);
        return MODEL_BUILDER.end();
    }

    private void rotate() {
        modelInstance.transform.rotate(1, 1, 0, 0.05f);
        second.transform.rotate(1, 0, 1, 0.07f);
    }
}
