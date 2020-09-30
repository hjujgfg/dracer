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

import static java.lang.Math.abs;
import static java.lang.Math.signum;
import static org.hjujgfg.dracer.gameplay.BigStatic.SWIPE_HANDLER;
import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.gameplay.BigStatic.OBJ_LOADER;
import static org.hjujgfg.dracer.gameplay.BigStatic.RANDOM;
import static org.hjujgfg.dracer.gameplay.BigStatic.TOUCH_HANDLER;
import static org.hjujgfg.dracer.util.FloatUtils.someWhatSimilar;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;
import static org.hjujgfg.dracer.world.params.control.Direction.LEFT;
import static org.hjujgfg.dracer.world.params.control.Direction.RIGHT;

public class Vehicle implements ModelSupplier, RenderAction, TransformSupplier, TypedModel {

    private final static Vector3 TMP = new Vector3();
    private final static float DEFAULT_ANGULAR_SPEED = 0.4f;
    private final static float MIN_DEFAULT_ANGULAR_SPEED = 0.2f;
    private final static Model model;
    private final ModelInstance instance;
    private final Collection<ModelInstance> instances;

    boolean inBarrelRoll;
    int barrelRolCounter = 0;
    int barrelRollDirection;

    int fluctCounter = 10;
    float fluct = 0;

    float angularSpeed = DEFAULT_ANGULAR_SPEED;

    private float jumpHeight = -1;
    private float steeringVelocity;

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
        //moveVehicleTouch();
        stabilize(instance);
    }

    @Override
    public Matrix4 getTransform() {
        return instance.transform;
    }

    public void doABarrelRoll() {
        Direction direction = TOUCH_HANDLER.activeDirection;
        if (!TOUCH_HANDLER.isLeftOrRight()) {
            direction = LEFT;
        }
        barrelRollDirection = direction.multiplier;
        inBarrelRoll = true;
        barrelRolCounter = 18;
    }

    public void steerContinuousStop() {
        steeringVelocity = 0;
    }



    public void steerContinuousAlt() {

    }

    public void steerContinuous(float velocity) {
        steeringVelocity = velocity;
        instance.transform.rotate(0, 0, 1, - (steeringVelocity * steeringVelocity) * 2 * signum(steeringVelocity));
        instance.transform.trn(0, 0, (-steeringVelocity) * 0.4f);

        Vector3 pos = instance.transform.getTranslation(TMP);
        if (PROBLEM_SPEED.minimalThresholdNotPassed() && (bigger(pos.z, 4.2f) || bigger(-4.2f, pos.z))) {
            PROBLEM_SPEED.change(-0.01f);
            PROBLEM_SPEED.changeMinimal(-0.01f);
        }
        if (bigger(pos.z, 4.2f)) {
            instance.transform.trn(0, 0, 4.2f - pos.z);
        }
        if (bigger(-4.2f, pos.z)) {
            instance.transform.trn(0, 0, -4.2f - pos.z);
        }
    }

    public void jump() {
        jumpHeight = 5f;
    }

    private void moveVehicleSwipe() {
        Vector3 pos = instance.transform.getTranslation(TMP);
        Gdx.app.log("SWIPE MOVING", String.format("Line coord: %.2f, veh coord: %.2f", SWIPE_HANDLER.currentLineCoord(), pos.z));
        /*if (someWhatSimilar(pos.z, SWIPE_HANDLER.currentLineCoord(), 0.1f)) {
            instance.transform
                    .setToRotation(0, 0, 1, -90)
                    .rotate(0, 1, 0, -90)
                    .setToTranslation(pos.x, pos.y, SWIPE_HANDLER.currentLineCoord());
        }*/
        if (bigger(pos.z, SWIPE_HANDLER.currentLineCoord())) {
            instance.transform
                    .rotate(0, 0, 1, RIGHT.multiplier * 0.8f)
                    .setTranslation(pos.x, pos.y, SWIPE_HANDLER.currentLineCoord());
        } else if (bigger(SWIPE_HANDLER.currentLineCoord(), pos.z)) {
            instance.transform
                    .rotate(0, 0, 1, LEFT.multiplier * 0.8f)
                    .setTranslation(pos.x, pos.y, SWIPE_HANDLER.currentLineCoord());
        }
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
                mult = -0.3f;
            }
            instance.transform
                    .rotate(0, 0, 1, barrelRollDirection * 20f)
                    .trn(mult, 0, -barrelRollDirection * 0.5f * Math.max(PROBLEM_SPEED.get(), 1));
            barrelRolCounter--;
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
                        .trn(0, 0, (-direction.multiplier) * angularSpeed);
                if (bigger(angularSpeed, MIN_DEFAULT_ANGULAR_SPEED)) {
                    angularSpeed -= 0.05f;
                }
            }
        }
        if (!inBarrelRoll && TOUCH_HANDLER.isNone()) {
            stabilize(instance);
        }
        Vector3 pos = instance.transform.getTranslation(TMP);
        if (PROBLEM_SPEED.minimalThresholdNotPassed() && (bigger(pos.z, 4.2f) || bigger(-4.2f, pos.z))) {
            PROBLEM_SPEED.change(-0.01f);
            PROBLEM_SPEED.changeMinimal(-0.01f);
        }
        if (bigger(pos.z, 4.2f)) {
            instance.transform.trn(0, 0, 4.2f - pos.z);
        }
        if (bigger(-4.2f, pos.z)) {
            instance.transform.trn(0, 0, -4.2f - pos.z);
        }
    }

    private void stabilize(ModelInstance instance) {
        angularSpeed = DEFAULT_ANGULAR_SPEED;
        Quaternion rotation = instance.transform.getRotation(new Quaternion());
        float targetAngle = 270f;
        float angleAround = rotation.getAngleAround(0, 0, 1);
        Gdx.app.log("angle around", "angl: " + angleAround);
        float diff = Math.abs(targetAngle - angleAround);

        if (bigger(0.05f, abs(steeringVelocity)) && diff > 0) {
            float rotate = 2;
            if (diff <= 2) {
                rotate = 0.5f;
            }
            if (bigger(angleAround, targetAngle)) {
                //Gdx.app.log("angle around", "around: " + (-90 - angleAround));
                instance.transform.rotate(0, 0, 1, - rotate);
            } else if (bigger(targetAngle, angleAround)) {
                //Gdx.app.log("angle around", " - around: " + (-90 + angleAround));
                instance.transform.rotate(0, 0, 1, rotate);
            }
        }
        Vector3 translation = instance.transform.getTranslation(new Vector3());
		/*Gdx.app.log("TRANSLATION", String.format("Instance translation %f %f %f ",
				translation.x, translation.y, translation.z));*/
        if (fluctCounter-- == 0) {
            fluct = 1 + RANDOM.nextFloat() - 0.5f;
            fluctCounter = 50;
        }

        if (bigger(translation.x, fluct, 0.3f)) {
            instance.transform.trn(-0.05f, 0, 0);
        } else if (bigger(fluct, translation.x, 0.3f)) {
            instance.transform.trn(0.05f, 0, 0);
        } else {
            if (bigger(translation.x, fluct, 0.01f)) {
                instance.transform.trn(-0.01f, 0, 0);
            } else if (bigger(fluct, translation.x, 0.01f)) {
                instance.transform.trn(0.01f, 0, 0);
            }
        }
        if (bigger(jumpHeight, translation.x)) {
            instance.transform.trn(0.5f, 0, 0);
            if (bigger(translation.x + 0.5f, jumpHeight)) {
                jumpHeight = -1;
            }
        }
    }

    @Override
    public ModelType getType() {
        return ModelType.VEHICLE;
    }
}
