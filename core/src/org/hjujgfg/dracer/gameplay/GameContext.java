package org.hjujgfg.dracer.gameplay;

import com.badlogic.gdx.math.Matrix4;

import org.hjujgfg.dracer.gameplay.states.GameMode;
import org.hjujgfg.dracer.world.camera.PerspectiveCameraSupplier;
import org.hjujgfg.dracer.world.interfaces.CameraSupplier;
import org.hjujgfg.dracer.world.models.Floor;
import org.hjujgfg.dracer.world.models.GameEvent;
import org.hjujgfg.dracer.world.models.Ground;
import org.hjujgfg.dracer.world.models.Houses;
import org.hjujgfg.dracer.world.models.ModelHolder;
import org.hjujgfg.dracer.world.models.ModelType;
import org.hjujgfg.dracer.world.models.Problem;
import org.hjujgfg.dracer.world.models.Terrain;
import org.hjujgfg.dracer.world.models.TiledFlor;
import org.hjujgfg.dracer.world.models.Vehicle;
import org.hjujgfg.dracer.world.params.PassedProblems;

import static org.hjujgfg.dracer.gameplay.BigStatic.TOUCH_HANDLER;

public class GameContext extends GameModeController {

    public ModelHolder modelHolder;
    public PassedProblems passedProblems;
    public PerspectiveCameraSupplier cameraSupplier;

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

    public GameEvent getGameEvent() {
        return modelHolder.getGameEvent();
    }

    public Terrain getTerrain() {
        return modelHolder.getTerrain();
    }

    public PassedProblems getPassedProblems() {
        return passedProblems;
    }

    public boolean isInUlt() {
        return getGameMode() == GameMode.LONG_BLURRED;
    }
}
