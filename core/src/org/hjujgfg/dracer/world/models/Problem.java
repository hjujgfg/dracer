package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.interfaces.LightSupplier;
import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;
import org.hjujgfg.dracer.world.interfaces.TransformSupplier;
import org.hjujgfg.dracer.world.interfaces.TypedModel;

import java.util.ArrayList;
import java.util.Collection;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.world.BigStatic.PROBLEM_PASSED_EVENT_PRODUCER;
import static org.hjujgfg.dracer.world.BigStatic.RANDOM;
import static org.hjujgfg.dracer.world.BigStatic.TOUCH_HANDLER;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class Problem implements ModelSupplier, RenderAction, TransformSupplier, TypedModel, LightSupplier<PointLight> {

    private final static Model problem;
    private final static ModelInstance problemInstance;
    private final static Collection<ModelInstance> instances;

    private PointLight pointLight;

    static {
        problem = MODEL_BUILDER.createBox(1.5f, 1.5f, 1.5f,
                new Material(ColorAttribute.createDiffuse(new Color(0f, 0.7f, 0.8f, 1f))),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        problemInstance = new ModelInstance(problem);
        problemInstance.transform.setToTranslation(
                5 * RANDOM.nextFloat(),
                20 + RANDOM.nextInt(10),
                RANDOM.nextFloat() * 5f - 2.5f);
        instances = new ArrayList<>(1);
        instances.add(problemInstance);
    }


    public Problem() {
        pointLight = new PointLight();
        randomizeProblemPosition();
    }

    @Override
    public Collection<ModelInstance> getModels() {
        return instances;
    }

    @Override
    public void dispose() {
        problem.dispose();
    }

    @Override
    public void render() {
        moveProblem();
    }

    @Override
    public Matrix4 getTransform() {
        return problemInstance.transform;
    }

    public static Matrix4 getProblemTransform() {
        return problemInstance.transform;
    }

    private void moveProblem() {
        Vector3 position;
        position = problemInstance.transform.getTranslation(new Vector3());
        if (bigger(position.y, -5)) {
            problemInstance.transform.translate(0, - PROBLEM_SPEED.get(), 0);
            pointLight.position.y -= PROBLEM_SPEED.get();
        } else {
            PROBLEM_SPEED.changeMinimal(0.01f);
            PROBLEM_SPEED.change(0.01f);
            randomizeProblemPosition();
            PROBLEM_PASSED_EVENT_PRODUCER.produceEvent();
        }
        if (bigger(position.x, 1.5f, 0.00001f)) {
            problemInstance.transform.translate(- Math.min(Math.abs(PROBLEM_SPEED.get()), position.x),
                    0,
                    0);
        }
    }

    private void randomizeProblemPosition() {
        int val = RANDOM.nextInt(2);
        float xPos = 1.5f;
        if (val % 2 == 0) {
            xPos = 25f;
        }
        val = RANDOM.nextInt(4);
        float zPos = val * 2 - 4;
        float yPos = 60 + RANDOM.nextInt(10);
        problemInstance.transform.setTranslation(
                xPos,
                yPos,
                zPos);
        pointLight.set(0.8f, 0.7f, 0.8f, xPos + 2, yPos, zPos, 5f);
    }

    @Override
    public ModelType getType() {
        return ModelType.PROBLEM;
    }

    @Override
    public PointLight getLight() {
        return pointLight;
    }
}
