package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.interfaces.LightSupplier;
import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;
import org.hjujgfg.dracer.world.interfaces.TypedModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.gameplay.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.gameplay.BigStatic.PROBLEM_PASSED_EVENT_PRODUCER;
import static org.hjujgfg.dracer.gameplay.BigStatic.RANDOM;
import static org.hjujgfg.dracer.world.models.Materials.createChip;
import static org.hjujgfg.dracer.world.models.Materials.createEmerald;
import static org.hjujgfg.dracer.world.models.Materials.createMattDiffuse;
import static org.hjujgfg.dracer.world.models.Materials.createPolishedSilver;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class Problem implements ModelSupplier, RenderAction, TypedModel, LightSupplier<PointLight> {

    private final static Model problem;
    private final static int LINES_COUNT = 5;
    private final Collection<ModelInstance> instances;
    private final Set<ModelInstance> renderableInstances;
    private final Set<Integer> takenLines;
    private final Map<ModelInstance, Integer> takenLineByProblem;
    private int problemsCount = 10;

    private PointLight pointLight;

    static {
        problem = MODEL_BUILDER.createBox(1.5f, 1.5f, 1.5f,
                createChip(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
    }


    public Problem() {
        pointLight = new PointLight();
        instances = new ArrayList<>(problemsCount);
        takenLines = new HashSet<>();
        takenLineByProblem = new HashMap<>();
        renderableInstances = new HashSet<>();
        for (int i = 0; i < problemsCount; i ++) {
            ModelInstance problemInstance = new ModelInstance(problem);
            problemInstance.transform.setToTranslation(
                    5 * RANDOM.nextFloat(),
                    20 + RANDOM.nextInt(10),
                    RANDOM.nextFloat() * 5f - 2.5f);
            randomizeProblemPosition(problemInstance);
            instances.add(problemInstance);
            renderableInstances.add(problemInstance);
        }

    }

    @Override
    public Collection<ModelInstance> getModels() {
        return renderableInstances;
    }

    @Override
    public void dispose() {
        problem.dispose();
    }

    @Override
    public void render() {
        instances.forEach(this::moveProblem);
    }

    private void moveProblem(ModelInstance problemInstance) {
        Vector3 position;
        position = problemInstance.transform.getTranslation(new Vector3());
        if (bigger(position.y, -5)) {
            problemInstance.transform.trn(0, - PROBLEM_SPEED.get(), 0);
            problemInstance.transform.rotate(
                    MathUtils.random(1f),
                    MathUtils.random(1f),
                    MathUtils.random(1f),
                    MathUtils.random(5f) * PROBLEM_SPEED.get()
            );
            //pointLight.position.y -= PROBLEM_SPEED.get();
        } else {
            PROBLEM_SPEED.changeMinimal(0.01f);
            PROBLEM_SPEED.change(0.01f);
            randomizeProblemPosition(problemInstance);
            PROBLEM_PASSED_EVENT_PRODUCER.produceEvent();
            problemInstance.materials.clear();
            Material m = MathUtils.randomSign() > 0 ? createMattDiffuse() : createEmerald();
            problemInstance.materials.add(m);
        }
        if (bigger(position.x, 0.75f, 0.00001f)) {
            problemInstance.transform.translate(- Math.min(Math.abs(PROBLEM_SPEED.get()), position.x - 0.75f),
                    0,
                    0);
        }
    }

    public void stopRendering(ModelInstance problemInstance) {
        renderableInstances.remove(problemInstance);
    }

    public void randomizeProblemPosition(ModelInstance problemInstance) {
        renderableInstances.add(problemInstance);
        int val = RANDOM.nextInt(2);
        float xPos = 1.5f;
        if (val % 2 == 0) {
            xPos = 25f;
        }
        if (takenLineByProblem.containsKey(problemInstance)) {
            takenLines.remove(takenLineByProblem.get(problemInstance));
        }
        /*val = RANDOM.nextInt(LINES_COUNT - takenLines.size());
        int zPos = 0;
        int indexAmongFree = 0;
        for (int i = 0; i < LINES_COUNT; i ++) {
            if (takenLines.contains(i)) {
                continue;
            }
            if (indexAmongFree == val) {
                val = i;
                break;
            }
            indexAmongFree ++;
        }
        switch (val) {
            case 0: zPos = -4; break;
            case 1: zPos = -2; break;
            case 2: zPos = 0; break;
            case 3: zPos = 2; break;
            case 4: zPos = 4; break;
        }*/
        float zPos = MathUtils.random(-2f, 2f);
        takenLines.add(val);
        takenLineByProblem.put(problemInstance, val);
        //float yPos = 50 * Math.max(PROBLEM_SPEED.get(), 1);//+ RANDOM.nextInt(20);
        float yPos = 100;
        problemInstance.transform.setTranslation(
                xPos,
                yPos,
                zPos);
        //pointLight.set(0.8f, 0.7f, 0.8f, xPos + 2, yPos, zPos, 5f);
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
