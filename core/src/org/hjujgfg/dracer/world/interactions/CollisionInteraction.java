package org.hjujgfg.dracer.world.interactions;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.gameplay.GameContext;
import org.hjujgfg.dracer.world.ContextualizedInstance;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import java.util.Iterator;

import static org.hjujgfg.dracer.gameplay.BigStatic.COLLISION_EVENT_PRODUCER;
import static org.hjujgfg.dracer.world.models.ModelType.VEHICLE;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class CollisionInteraction extends ContextualizedInstance implements RenderAction {

    private final Vector3 TMP = new Vector3();

    public CollisionInteraction(GameContext context) {
        super(context);
    }

    @Override
    public void render() {
        checkCollision();
    }

    private void checkCollision() {
        Vector3 instancePosition = context.getTransform(VEHICLE).getTranslation(new Vector3());
        Iterator<ModelInstance> iterator = context.getProblem().getModels().iterator();
        while (iterator.hasNext()) {
            checkCollision(iterator.next(), instancePosition, iterator);
        }
        //context.getProblem().getModels().forEach(model -> checkCollision(model, instancePosition));
    }

    private void checkCollision(ModelInstance problem, Vector3 instancePosition, Iterator<ModelInstance> iterator) {
        float dst = problem.transform.getTranslation(TMP).dst(instancePosition);
        if (dst < 1.5f) {
            //context.getProblem().stopRendering(problem);
            iterator.remove();
            if (!context.isInUlt()) {
                PROBLEM_SPEED.setMinimal(Math.max(PROBLEM_SPEED.getMinimal() - 0.05f, 0.05f));
                PROBLEM_SPEED.set(-1f * Math.max(1, PROBLEM_SPEED.get()));
                COLLISION_EVENT_PRODUCER.produceEvent();
            }
        }
    }
}
