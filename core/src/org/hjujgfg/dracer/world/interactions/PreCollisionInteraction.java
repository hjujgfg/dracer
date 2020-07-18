package org.hjujgfg.dracer.world.interactions;

import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.ContextualizedInstance;
import org.hjujgfg.dracer.world.GameContext;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.models.ModelType.VEHICLE;
import static org.hjujgfg.dracer.world.models.Problem.getProblemTransform;

public class PreCollisionInteraction extends ContextualizedInstance implements RenderAction {

    public PreCollisionInteraction(GameContext context) {
        super(context);
    }

    @Override
    public void render() {
        check();
    }

    private void check() {
        Vector3 problemPosition = getProblemTransform().getTranslation(new Vector3());
        Vector3 instancePosition = context.getTransform(VEHICLE).getTranslation(new Vector3());
        float dst = problemPosition.dst(instancePosition);
        if (bigger(3, dst) && bigger(dst, 2)) {
            context.getVehicle().doABarrelRoll();
        }
    }
}
