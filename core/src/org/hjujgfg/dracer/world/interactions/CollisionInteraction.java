package org.hjujgfg.dracer.world.interactions;

import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.ContextualizedInstance;
import org.hjujgfg.dracer.world.GameContext;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import static org.hjujgfg.dracer.world.BigStatic.COLLISION_EVENT_PRODUCER;
import static org.hjujgfg.dracer.world.BigStatic.RANDOM;
import static org.hjujgfg.dracer.world.BigStatic.TOUCH_HANDLER;
import static org.hjujgfg.dracer.world.models.ModelType.VEHICLE;
import static org.hjujgfg.dracer.world.models.Problem.getProblemTransform;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class CollisionInteraction extends ContextualizedInstance implements RenderAction {

    public CollisionInteraction(GameContext context) {
        super(context);
    }

    @Override
    public void render() {
        checkCollision();
    }

    private void checkCollision() {
        Vector3 problemPosition = getProblemTransform().getTranslation(new Vector3());
        Vector3 instancePosition = context.getTransform(VEHICLE).getTranslation(new Vector3());
        float dst = problemPosition.dst(instancePosition);
        if (dst < 2) {
            if (TOUCH_HANDLER.isBoth()) {
                getProblemTransform().setTranslation(
                        5 * RANDOM.nextFloat(),
                        20 + RANDOM.nextInt(10),
                        RANDOM.nextFloat() * 5f - 2.5f);
            } else {
                PROBLEM_SPEED.setMinimal(Math.max(PROBLEM_SPEED.getMinimal() - 0.1f, 0.01f));
                PROBLEM_SPEED.set(-1f * Math.max(1, PROBLEM_SPEED.get()));
                COLLISION_EVENT_PRODUCER.produceEvent();
            }
        }
    }
}
