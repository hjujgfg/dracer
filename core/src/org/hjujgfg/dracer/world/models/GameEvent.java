package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import java.util.ArrayList;
import java.util.Collection;

import static org.hjujgfg.dracer.gameplay.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.gameplay.BigStatic.RANDOM;
import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.models.Materials.createEmerald;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class GameEvent implements ModelSupplier, RenderAction {

    private final static Model model;
    private Collection<ModelInstance> instances;
    private ModelInstance instance;

    static {
        model = MODEL_BUILDER.createBox(1.5f, 1.5f, 1.5f,
                createEmerald(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
    }

    public GameEvent() {
        instances = new ArrayList<>(1);
        instance = new ModelInstance(model);
        instances.add(instance);
    }

    @Override
    public Collection<ModelInstance> getModels() {
        return instances;
    }

    public ModelInstance getModel() {
        return instance;
    }

    @Override
    public void dispose() {
        model.dispose();
    }

    @Override
    public void render() {
        moveGameEvent();
    }

    private void moveGameEvent() {
        Vector3 position;
        position = instance.transform.getTranslation(new Vector3());
        if (bigger(position.y, -5)) {
            instance.transform.translate(0, - PROBLEM_SPEED.get(), 0);
        } else {
            randomizeEventPosition();
        }
    }

    public void randomizeEventPosition() {
        float xPos = 1.5f;
        int val = RANDOM.nextInt(5);
        int zPos = 0;
        switch (val) {
            case 0: zPos = -4; break;
            case 1: zPos = -2; break;
            case 2: zPos = 0; break;
            case 3: zPos = 2; break;
            case 4: zPos = 4; break;
        }
        float yPos = 100 * Math.max(PROBLEM_SPEED.get(), 1) + RANDOM.nextInt(20);
        instance.transform.setTranslation(
                xPos,
                yPos,
                zPos);
        //pointLight.set(0.8f, 0.7f, 0.8f, xPos + 2, yPos, zPos, 5f);
    }
}
