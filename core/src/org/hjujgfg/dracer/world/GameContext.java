package org.hjujgfg.dracer.world;

import com.badlogic.gdx.math.Matrix4;

import org.hjujgfg.dracer.world.models.Floor;
import org.hjujgfg.dracer.world.models.Ground;
import org.hjujgfg.dracer.world.models.Houses;
import org.hjujgfg.dracer.world.models.ModelHolder;
import org.hjujgfg.dracer.world.models.ModelType;
import org.hjujgfg.dracer.world.models.Problem;
import org.hjujgfg.dracer.world.models.TiledFlor;
import org.hjujgfg.dracer.world.models.Vehicle;
import org.hjujgfg.dracer.world.params.PassedProblems;

import static org.hjujgfg.dracer.world.BigStatic.TOUCH_HANDLER;

public class GameContext {

    public ModelHolder modelHolder;
    public PassedProblems passedProblems;

    public GameContext() {
        this.modelHolder = new ModelHolder();
        this.passedProblems = new PassedProblems();
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

    public TiledFlor getTiledFloor() {
        return modelHolder.getTiledFlor();
    }

    public Houses getHouses() {
        return modelHolder.getHouses();
    }

    public Ground getGround() {
        return modelHolder.getGround();
    }

    public PassedProblems getPassedProblems() {
        return passedProblems;
    }

    public boolean isInUlt() {
        return passedProblems.ultReady() && TOUCH_HANDLER.isBoth();
    }
}
