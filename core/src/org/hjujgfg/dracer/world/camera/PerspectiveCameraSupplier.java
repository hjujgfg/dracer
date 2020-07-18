package org.hjujgfg.dracer.world.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.ContextualizedInstance;
import org.hjujgfg.dracer.world.GameContext;
import org.hjujgfg.dracer.world.interfaces.CameraSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;
import org.hjujgfg.dracer.world.models.ModelType;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.BigStatic.TOUCH_HANDLER;

public class PerspectiveCameraSupplier extends ContextualizedInstance implements CameraSupplier, RenderAction {

    private final static Vector3 TMP = new Vector3();

    PerspectiveCamera cam;

    final float cameraX = 7f, cameraY = -5f, cameraZ = 0f;
    final float lookX = 0, lookY = 5, lookZ = 0;

    private boolean reachedFow = false;

    public PerspectiveCameraSupplier(GameContext context) {
        super(context);
        cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.up.set(0, 1, 0);
        cam.position.set(7f, -6f, 0f);
        cam.lookAt(0, 0, 0);
        cam.near = 1f;
        cam.far = 300f;
        cam.update();
    }

    @Override
    public void render() {
        moveCamera();
    }

    @Override
    public Camera getCamera() {
        return cam;
    }

    private void moveCamera() {
        if (TOUCH_HANDLER.isBoth()) {
            handleBoth();
        } else {
            Vector3 pos = cam.position;
            if (bigger(cameraY, pos.y)) {
                cam.position.set(pos.x, pos.y + 0.1f, pos.z);
            } else if (bigger(pos.y, cameraY)) {
                cam.position.set(pos.x, pos.y - 0.1f, pos.z);
            }
            if (bigger(cameraX, pos.x)) {
                cam.position.set(pos.x + 0.1f, pos.y, pos.z);
            } else if (bigger(pos.x, cameraX)) {
                cam.position.set(pos.x - 0.1f, pos.y, pos.z);
            }

            if (bigger(cameraZ, pos.z)) {
                cam.position.set(pos.x, pos.y, pos.z + 0.1f);
            } else if (bigger(pos.z, cameraZ)) {
                cam.position.set(pos.x, pos.y, pos.z - 0.1f);
            }
            cam.fieldOfView = 67;
            cam.lookAt(lookX, lookY, lookZ);
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
            if (cam.fieldOfView < 120)
            cam.fieldOfView += 5;
        }
        cam.lookAt(context.getTransform(ModelType.VEHICLE).getTranslation(TMP));
    }
}
