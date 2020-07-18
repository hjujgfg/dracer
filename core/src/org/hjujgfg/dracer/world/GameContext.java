package org.hjujgfg.dracer.world;

import com.badlogic.gdx.math.Matrix4;

import org.hjujgfg.dracer.world.models.Floor;
import org.hjujgfg.dracer.world.models.ModelHolder;
import org.hjujgfg.dracer.world.models.ModelType;
import org.hjujgfg.dracer.world.models.Problem;
import org.hjujgfg.dracer.world.models.Vehicle;

public class GameContext {

    public ModelHolder modelHolder;

    public GameContext() {
        this.modelHolder = new ModelHolder();
    }

    public Matrix4 getTransform(ModelType type) {
        return modelHolder.transformSupplierMap.get(type).getTransform();
    }

    public Vehicle getVehicle() {
        return modelHolder.getVehicle();
    }

    public Floor getFloor() {
        return modelHolder.getFloor();
    }

    public Problem getProblem() {
        return modelHolder.getProblem();
    }

}
