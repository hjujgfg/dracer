package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;
import org.hjujgfg.dracer.world.interfaces.TransformSupplier;
import org.hjujgfg.dracer.world.interfaces.TypedModel;

import java.util.ArrayList;
import java.util.Collection;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.world.BigStatic.RANDOM;
import static org.hjujgfg.dracer.world.BigStatic.TOUCH_HANDLER;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class Problem implements ModelSupplier, RenderAction, TransformSupplier, TypedModel {

    private final static Model problem;
    private final static ModelInstance problemInstance;
    private final static Collection<ModelInstance> instances;

    static {
        problem = MODEL_BUILDER.createBox(2, 2, 2,
                new Material(ColorAttribute.createDiffuse(Color.CHARTREUSE)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
        problemInstance = new ModelInstance(problem);
        problemInstance.transform.setToTranslation(
                5 * RANDOM.nextFloat(),
                20 + RANDOM.nextInt(10),
                RANDOM.nextFloat() * 5f - 2.5f);
        instances = new ArrayList<>(1);
        instances.add(problemInstance);
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
        if (TOUCH_HANDLER.isBoth() || PROBLEM_SPEED.isSlow()) {
            PROBLEM_SPEED.set(Math.min(PROBLEM_SPEED.get() + 0.1f, 2));
        } else if (PROBLEM_SPEED.isFast()) {
            PROBLEM_SPEED.change(-0.1f);
        }
        Vector3 position;
        position = problemInstance.transform.getTranslation(new Vector3());
        if (bigger(position.y, -5)) {
            problemInstance.transform.translate(0, - PROBLEM_SPEED.get(), 0);
        } else {
            PROBLEM_SPEED.changeMinimal(0.01f);
            PROBLEM_SPEED.change(0.01f);

            randomizeProblemPosition();
        }
    }

    private void randomizeProblemPosition() {
        int val = RANDOM.nextInt(2);
        float xPos = 0;
        if (val % 2 == 0) {
            xPos = 2;
        }
        val = RANDOM.nextInt(5);
        float zPos = val * 2 - 5;
        problemInstance.transform.setTranslation(
                xPos,
                20 + RANDOM.nextInt(10),
                zPos);

    }

    @Override
    public ModelType getType() {
        return ModelType.PROBLEM;
    }
}
