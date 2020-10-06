package org.hjujgfg.dracer.world.autopilot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.gameplay.GameContext;
import org.hjujgfg.dracer.world.ContextualizedInstance;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;

public class Autopilot extends ContextualizedInstance implements RenderAction {

    private final static Vector3 TMP = new Vector3(), TMP2 = new Vector3();

    public Autopilot(GameContext context) {
        super(context);
    }

    @Override
    public void render() {
        if (context.isInManualSteering()) {
            return;
        }
        Vector3 pos = context.getVehicle().getTransform().getTranslation(TMP);
        for (ModelInstance i : context.getProblem().getModels()) {
            Vector3 translation = i.transform.getTranslation(TMP2);
            float dst = translation.dst(pos);
            float zDiff = Math.abs(translation.z - pos.z);
            if (dst < 20 && bigger(translation.y, 0) && zDiff < 1.5) {
                Gdx.app.log("Autopilot", String.format("Dst: %.2f, zDiff: %.2f, diff: %.2f", dst, zDiff, translation.z - pos.z));
                if (bigger(pos.z, 3.5f)) {
                    context.getVehicle().changeSteeringVelocity( Math.abs(translation.z - pos.z));
                } else if (bigger(- 3.5f, pos.z)) {
                    context.getVehicle().changeSteeringVelocity(-Math.abs(translation.z - pos.z));
                } else {
                    context.getVehicle().changeSteeringVelocity(translation.z - pos.z);
                }
            } else {
                context.getVehicle().changeSteeringVelocity(MathUtils.random(0.1f) * MathUtils.randomSign());
            }
        }
    }
}
