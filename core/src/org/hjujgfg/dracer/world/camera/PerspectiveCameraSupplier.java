package org.hjujgfg.dracer.world.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.ContextualizedInstance;
import org.hjujgfg.dracer.gameplay.GameContext;
import org.hjujgfg.dracer.world.interfaces.CameraSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import static org.hjujgfg.dracer.gameplay.states.GameMode.PROBLEM_EVASION;
import static org.hjujgfg.dracer.gameplay.states.GameMode.TOP_VIEW_PROBLEM_EVASION;
import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.models.ModelType.VEHICLE;

public class PerspectiveCameraSupplier extends ContextualizedInstance implements CameraSupplier, RenderAction {

    private final static Vector3 TMP = new Vector3();
    private final static Vector3 UP = new Vector3(1, 0, 0);
    private final static Vector3 LEFT = new Vector3(0, 0, 1);
    private final static Vector3 FORWARD = new Vector3(0, 1, 0);

    private final static float SWITCH_SPEED = 0.2f;
    private final static int DEFAUlT_FOW = 100;
    private final static int DEFAULT_TOP_FOW = 120;

    PerspectiveCamera cam;

    final float cameraX = 7f, cameraY = -7f, cameraZ = 0f;
    final float lookX = 0, lookY = 5, lookZ = 0;
    final Vector3 defaultLookAt = new Vector3(lookX, lookY, lookZ);
    final Vector3 lookAt = new Vector3(defaultLookAt);
    private boolean reachedAngle = false;
    private CameraMode cameraMode = CameraMode.DEFAULT;


    private boolean reachedFow = false;

    public PerspectiveCameraSupplier(GameContext context) {
        super(context);
        cam = new PerspectiveCamera(DEFAUlT_FOW, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.up.set(UP);
        cam.position.set(7f, -6f, 0f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
        context.cameraSupplier = this;
    }

    @Override
    public void render() {
        moveCamera();
    }

    @Override
    public Camera getCamera() {
        return cam;
    }

    public void switchMode() {
        if (cameraMode == CameraMode.DEFAULT) {
            cameraMode = CameraMode.FROM_TOP;
        } else {
            cameraMode = CameraMode.DEFAULT;
        }
    }

    public void switchToDefault() {
        cameraMode = CameraMode.DEFAULT;
    }

    public void switchToFromTop() {
        cameraMode = CameraMode.FROM_TOP;
    }

    public void moveSideways(float velocity) {
        Vector3 pos = cam.position;
        cam.position.set(pos.x, pos.y, pos.z + velocity);
        cam.up.set(UP);
    }

    private void moveCamera() {
        if (context.isInUlt()) {
            handleBoth();
        } else if (context.getGameMode() == PROBLEM_EVASION) {
            Vector3 pos = cam.position;
            if (bigger(cameraY, pos.y)) {
                cam.position.set(pos.x, pos.y + SWITCH_SPEED, pos.z);
            } else if (bigger(pos.y, cameraY)) {
                cam.position.set(pos.x, pos.y - SWITCH_SPEED, pos.z);
            }
            if (bigger(cameraX, pos.x)) {
                cam.position.set(pos.x + SWITCH_SPEED, pos.y, pos.z);
            } else if (bigger(pos.x, cameraX)) {
                cam.position.set(pos.x - SWITCH_SPEED, pos.y, pos.z);
            }

            if (bigger(cameraZ, pos.z)) {
                cam.position.set(pos.x, pos.y, pos.z + SWITCH_SPEED);
            } else if (bigger(pos.z, cameraZ)) {
                cam.position.set(pos.x, pos.y, pos.z - SWITCH_SPEED);
            }
            if (cam.fieldOfView > DEFAUlT_FOW) {
                cam.fieldOfView --;
            }
            cam.lookAt(defaultLookAt);
            cam.up.set(0, 1, 0);
            reachedFow = false;
        } else if (context.getGameMode() == TOP_VIEW_PROBLEM_EVASION) {
            Vector3 pos = cam.position;
            if (bigger(30, pos.y)) {
                cam.position.set(pos.x, pos.y + SWITCH_SPEED, pos.z);
            } else if (bigger(pos.y, 30)) {
                cam.position.set(pos.x, pos.y - SWITCH_SPEED, pos.z);
            }
            if (bigger(30, pos.x)) {
                cam.position.set(pos.x + SWITCH_SPEED, pos.y, pos.z);
            } else if (bigger(pos.x, 30)) {
                cam.position.set(pos.x - SWITCH_SPEED, pos.y, pos.z);
            }

            if (bigger(cameraZ, pos.z)) {
                cam.position.set(pos.x, pos.y, pos.z + SWITCH_SPEED);
            } else if (bigger(pos.z, cameraZ)) {
                cam.position.set(pos.x, pos.y, pos.z - SWITCH_SPEED);
            }
            if (cam.fieldOfView < DEFAULT_TOP_FOW) {
                cam.fieldOfView ++;
            }
            cam.lookAt(0, 30, 0);
            cam.up.set(0, 1, 0);
            reachedFow = false;
        }

        //cam.normalizeUp();
        cam.update();
    }

    private void handleBoth() {
        Vector3 pos = cam.position;
        if (pos.x > 1.5) {
            cam.position.add(-0.1f, 0, 0);
        }
        if (pos.y > -9) {
            cam.position.add(0, -0.1f, 0);

        }
        if (cam.fieldOfView > 30 && !reachedFow) {
            cam.fieldOfView -= 1;
            if (bigger(31, cam.fieldOfView)) {
                reachedFow = true;
            }
        } else {
            if (cam.fieldOfView < 120) {
                cam.fieldOfView += 5;
            }
        }
        rotateByHorizontal();
        //rotateByVertical();
        //cam.lookAt(context.getTransform(ModelType.VEHICLE).getTranslation(TMP));
    }

    private void rotateByHorizontal() {
        float dot = FORWARD.dot(cam.direction.nor());
        float angle = (float) (Math.acos(dot) * 180 / Math.PI);
        Gdx.app.log("ANGLE", String.format("dot %f Angle is %f", dot, angle));
        if (bigger(angle, 5)) { // todo it sometimes goes around
            cam.rotate(LEFT, -0.5f);
        }
    }

    private void rotateByVertical() {
        Vector3 vPos = context.getTransform(VEHICLE).getTranslation(new Vector3());
        Vector2 target = new Vector2(vPos.y, vPos.z);
        Vector2 cur = new Vector2(cam.direction.y, cam.direction.z);
        float dot = cur.dot(target);
        float angle = (float) (Math.acos(dot) * 180 / Math.PI);
        Gdx.app.log("ANGLe", String.format("cur %s, target %s, dot %f Angle is %f v pos: %s Axis is %s",
                cur.toString(), target.toString(), dot, angle, vPos.toString(), UP.toString()));
        if (bigger(Math.abs(angle), 1) ) {
            float multiplier = vPos.z > 0 ? 1 : -1;
            cam.rotate(UP, multiplier * 0.01f);
            if (bigger(1, Math.abs(angle))) {
                reachedAngle = true;
            }
        } else {
            cam.lookAt(vPos);
        }
    }

    private void rotateByAngleBetween() {
        Vector3 vPos = context.getTransform(VEHICLE).getTranslation(new Vector3());
        Vector3 cDir = cam.direction;
        Vector3 axis = new Vector3(vPos).crs(cDir);
        float dot = cDir.dot(vPos);
        float cos = dot / Math.abs(vPos.len() * cDir.len());
        double angle = Math.acos(dot);
        Gdx.app.log("ANGLe", String.format("dot is %f cos is %f Angle is %f Direction is %s, v pos: %s Axis is %s",
                dot, cos, angle, cDir.toString(), vPos.toString(), axis.toString()));
        if (bigger(1, (float) Math.abs(angle))) {
            cam.rotate(axis, (float) angle / 10);
        } else {
            cam.lookAt(vPos);
        }
    }

    public enum CameraMode {
        DEFAULT,
        FROM_TOP
    }
}
