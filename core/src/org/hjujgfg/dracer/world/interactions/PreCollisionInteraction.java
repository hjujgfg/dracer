package org.hjujgfg.dracer.world.interactions;

import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.interfaces.RenderAction;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.models.Problem.getProblemTransform;
import static org.hjujgfg.dracer.world.models.Vehicle.getVehicleTransform;

public class PreCollisionInteraction implements RenderAction {

    @Override
    public void render() {
        check();
    }

    private void check() {
        Vector3 problemPosition = getProblemTransform().getTranslation(new Vector3());
        Vector3 instancePosition = getVehicleTransform().getTranslation(new Vector3());
        float dst = problemPosition.dst(instancePosition);
        if (bigger(3, dst) && bigger(dst, 2)) {
            //  VEHICLE.doABarrelRoll();
        }
    }
}
