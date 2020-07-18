package org.hjujgfg.dracer.world.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.interfaces.LightSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.BigStatic.TOUCH_HANDLER;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class DirectionalLightSupplier implements LightSupplier<DirectionalLight>, RenderAction {

    DirectionalLight directionalLight;
    int lightDirection = 1;

    public DirectionalLightSupplier() {
        directionalLight = new DirectionalLight()
                .set(0.8f, 0.8f, 0.8f,
                        -6f, 5f, 5f);

    }

    @Override
    public DirectionalLight getLight() {
        return directionalLight;
    }

    @Override
    public void render() {
        move();
    }

    private void move() {
        if (TOUCH_HANDLER.isBoth()) {
            directionalLight.color.set(Color.RED);
        } else {
            directionalLight.color.set(Color.WHITE);
        }
		/*if (bigger(10, directionalLight.direction.x)) {
			directionalLight.direction.add(-0.5f, 0f, 0);
		} else {
			directionalLight.direction.add(0.5f, 0f, 0);
		}*/
        Vector3 direction = directionalLight.direction;
		/*Gdx.app.log("TRANSLATION", String.format("Light direction %f %f %f ",
				direction.x, direction.y, direction.z));*/

        if (bigger(-10, direction.x)) {
            lightDirection = 1;
        }
        if (bigger(direction.x, -5)) {
            lightDirection = -1;
        }
        direction.add( lightDirection * PROBLEM_SPEED.get() * 0.5f, 0, 0);
    }


}
