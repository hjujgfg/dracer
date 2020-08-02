package org.hjujgfg.dracer.world.interactions;

import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.ContextualizedInstance;
import org.hjujgfg.dracer.world.GameContext;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import static org.hjujgfg.dracer.world.BigStatic.COLLISION_EVENT_PRODUCER;
import static org.hjujgfg.dracer.world.BigStatic.RANDOM;
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
        if (dst < 2f) {
            if (context.isInUlt()) {
                getProblemTransform().setTranslation(
                        5 * RANDOM.nextFloat(),
                        40 + RANDOM.nextInt(10),
                        RANDOM.nextFloat() * 5f - 2.5f);
            } else {
                PROBLEM_SPEED.setMinimal(Math.max(PROBLEM_SPEED.getMinimal() - 0.05f, 0.05f));
                PROBLEM_SPEED.set(-1f * Math.max(1, PROBLEM_SPEED.get()));
                COLLISION_EVENT_PRODUCER.produceEvent();
            }
        }
    }
}
