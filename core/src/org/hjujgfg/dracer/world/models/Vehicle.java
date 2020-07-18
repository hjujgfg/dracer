package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;
import org.hjujgfg.dracer.world.interfaces.TransformSupplier;
import org.hjujgfg.dracer.world.interfaces.TypedModel;
import org.hjujgfg.dracer.world.params.control.Direction;

import java.util.ArrayList;
import java.util.Collection;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.BigStatic.OBJ_LOADER;
import static org.hjujgfg.dracer.world.BigStatic.RANDOM;
import static org.hjujgfg.dracer.world.BigStatic.TOUCH_HANDLER;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class Vehicle implements ModelSupplier, RenderAction, TransformSupplier, TypedModel {

    private final static Model model;
    private final ModelInstance instance;
    private final Collection<ModelInstance> instances;

    boolean inBarrelRoll;
    int barrelRolCounter = 0;
    int barrelRollDirection;

    int fluctCounter = 10;
    float fluct = 0;


    static {
        model = OBJ_LOADER.loadModel(Gdx.files.internal("ship.obj"));

    }

    public Vehicle() {
        instance = new ModelInstance(model);
        instance.transform
                .setToRotation(0, 0, 1, -90)
                .rotate(0, 1, 0, -90)
                .translate(0, 2, 0);
        instances = new ArrayList<>(1);
        instances.add(instance);
    }

    @Override
    public Collection<ModelInstance> getModels() {
        return instances;
    }

    @Override
    public void dispose() {
        model.dispose();
    }

    @Override
    public void render() {
        moveVehicleTouch();
    }

    @Override
    public Matrix4 getTransform() {
        return instance.transform;
    }

    public void doABarrelRoll() {
        Direction direction = TOUCH_HANDLER.activeDirection;
        if (!TOUCH_HANDLER.isLeftOrRight()) {
            direction = Direction.LEFT;
        }
        barrelRollDirection = direction.multiplier;
        inBarrelRoll = true;
        barrelRolCounter = 18;
    }

    private void moveVehicleTouch() {
        if (inBarrelRoll) {
            int part = barrelRolCounter / 6;
            float mult;
            if (part == 1) {
                mult = 0.1f;
            } else if (part == 2) {
                mult = 0.2f;
            } else {
                mult = - 0.3f;
            }
            instance.transform
                    .rotate(0, 0, 1, barrelRollDirection * 20f)
                    .trn(mult, 0, - barrelRollDirection * 0.3f * Math.max(PROBLEM_SPEED.get(), 1));
            barrelRolCounter --;
			/*Gdx.app.log("BARREL", String.format("Counter is %d",
					barrelRolCounter));*/
            if (barrelRolCounter == 1) {
                inBarrelRoll = false;
            }
        } else if (TOUCH_HANDLER.isBoth()) {
            //
        } else {
            if (TOUCH_HANDLER.isDoubleTap()) {
                doABarrelRoll();
            } else {
                Direction direction = TOUCH_HANDLER.activeDirection;
                instance.transform
                        .rotate(0, 0, 1, direction.multiplier * 0.8f)
                        .trn(0, 0, (- direction.multiplier) * 0.2f * Math.max(PROBLEM_SPEED.get(), 1));
            }
        }

        if (!inBarrelRoll && TOUCH_HANDLER.isNone()) {
            stabilize(instance);
        }
    }

    private void stabilize(ModelInstance instance) {
        Quaternion rotation = instance.transform.getRotation(new Quaternion());
        float angleAround = rotation.getAngleAround(0, 0, 1);
        if (bigger(angleAround, -90)) {
            instance.transform.rotate(0, 0, 1, (-90 - angleAround));
        } else if (bigger(-90, angleAround)) {
            instance.transform.rotate(0, 0, 1, (-90 + angleAround));
        }
        Vector3 translation = instance.transform.getTranslation(new Vector3());
		/*Gdx.app.log("TRANSLATION", String.format("Instance translation %f %f %f ",
				translation.x, translation.y, translation.z));*/
        if (fluctCounter-- == 0) {
            fluct = 2 + RANDOM.nextFloat() - 0.5f;
            fluctCounter = 50;
        }
        if (bigger(translation.x, fluct)) {
            instance.transform.trn( -0.01f, 0, 0);
        } else if (bigger(fluct, translation.x)) {
            instance.transform.trn(0.01f, 0, 0);
        }
    }

    @Override
    public ModelType getType() {
        return ModelType.VEHICLE;
    }
}
