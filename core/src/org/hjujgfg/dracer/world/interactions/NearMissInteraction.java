package org.hjujgfg.dracer.world.interactions;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.ContextualizedInstance;
import org.hjujgfg.dracer.gameplay.GameContext;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.models.ModelType.VEHICLE;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class NearMissInteraction extends ContextualizedInstance implements RenderAction {

    private final Vector3 TMP = new Vector3();
    private long prevHappened;
    private boolean inNearMiss;


    public NearMissInteraction(GameContext context) {
        super(context);
        prevHappened = System.currentTimeMillis();
    }

    @Override
    public void render() {
        if (bigger(2f, PROBLEM_SPEED.get(), 0.01f) && !inNearMiss) {
            return;
        }
        long current = System.currentTimeMillis();
        if (current - prevHappened < 3_000) {
            inNearMiss = false;
            return;
        }
        Vector3 instancePosition = context.getTransform(VEHICLE).getTranslation(new Vector3());
        if (context.getProblem().getModels().stream().anyMatch(model -> checkNearMiss(model, instancePosition))) {
            inNearMiss = true;
            PROBLEM_SPEED.set(0.1f);
            PROBLEM_SPEED.setMinimal(0.1f);
        } else {
            PROBLEM_SPEED.set(2f);
            PROBLEM_SPEED.setMinimal(2f);
            if (inNearMiss) {
                prevHappened = System.currentTimeMillis();
            }
            inNearMiss = false;
        }
    }

    private boolean checkNearMiss(ModelInstance problem, Vector3 instancePosition) {
        Vector3 translation = problem.transform.getTranslation(TMP);
        float dst = translation.dst(instancePosition);
        float zDiff = Math.abs(translation.z - instancePosition.z);
        return dst < 10 && bigger(translation.y, 0) && zDiff < 1.5;
    }
}
