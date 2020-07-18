package org.hjujgfg.dracer.world.interactions;

import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.interfaces.RenderAction;

import java.util.Random;

import static org.hjujgfg.dracer.world.BigStatic.COLLISION_EVENT_PRODUCER;
import static org.hjujgfg.dracer.world.BigStatic.TOUCH_HANDLER;
import static org.hjujgfg.dracer.world.models.Problem.getProblemTransform;
import static org.hjujgfg.dracer.world.models.Vehicle.getVehicleTransform;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class CollisionInteraction implements RenderAction {

    private final static Random r = new Random();

    @Override
    public void render() {
        checkCollision();
    }

    private void checkCollision() {
        Vector3 problemPosition = getProblemTransform().getTranslation(new Vector3());
        Vector3 instancePosition = getVehicleTransform().getTranslation(new Vector3());
        float dst = problemPosition.dst(instancePosition);
        if (dst < 2) {
            if (TOUCH_HANDLER.isBoth()) {
                getProblemTransform().setTranslation(
                        5 * r.nextFloat(),
                        20 + r.nextInt(10),
                        r.nextFloat() * 5f - 2.5f);
            } else {
                PROBLEM_SPEED.setMinimal(Math.max(PROBLEM_SPEED.getMinimal() - 0.1f, 0.01f));
                PROBLEM_SPEED.set(-1f * Math.max(1, PROBLEM_SPEED.get()));
                COLLISION_EVENT_PRODUCER.produceEvent();
            }
        }
    }
}
