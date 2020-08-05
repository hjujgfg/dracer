package org.hjujgfg.dracer.world.light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.ContextualizedInstance;
import org.hjujgfg.dracer.world.GameContext;
import org.hjujgfg.dracer.world.interfaces.LightSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import java.util.ArrayList;
import java.util.Collection;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class DirectionalLightSupplier extends ContextualizedInstance implements LightSupplier<DirectionalLight>, RenderAction {

    private DirectionalLight directionalLight;
    private DirectionalLight staticLight;


    private DirectionalLight directionalLight2;


    public DirectionalLightSupplier(GameContext context) {
        super(context);
        directionalLight = new DirectionalLight()
                .set(28.f/255.f, 176.f/255.f, 255.f/255.f,
                //.set(Color.WHITE,
                        -6f, 5f, 0);
        staticLight = new DirectionalLight()
                .set(28.f/255.f, 176.f/255.f, 255.f/255.f,
                //.set(Color.WHITE,
                        -1f, 5f, 0);
        directionalLight2 = new DirectionalLight()
                //.set(0.4f, 0.4f, 0.4f,
                .set(Color.WHITE,
                        -10f, 2f, -1);

    }

    @Override
    public DirectionalLight getLight() {
        return directionalLight;
    }

    @Override
    public Collection<DirectionalLight> getLights() {
        ArrayList<DirectionalLight> lights = new ArrayList<>();
        lights.add(directionalLight);
        lights.add(directionalLight2);
        lights.add(staticLight);
        return lights;
    }

    @Override
    public void render() {
        move();
    }

    private void move() {
        moveFirstLight();
        moveSecondLight();
    }

    private void moveFirstLight() {
        if (context.isInUlt()) {
            reddenLight();
            //directionalLight.color.set(Color.RED);
        } else {
            directionalLight.color.set(28.f/255.f, 176.f/255.f, 255.f/255.f, 1f);
            //directionalLight.color.set(Color.WHITE);
        }
        Vector3 direction = directionalLight.direction;
        if (bigger(direction.y, 60)) {
            directionalLight.setDirection(-10, -50, 0);
        } else {
            directionalLight.direction.add(0, PROBLEM_SPEED.get(), 0);
        }
        /*if (bigger(direction.y, 15)) {
            directionalLight.direction.add(0, 0, 1);
        }*/
    }

    private void moveSecondLight() {
        Vector3 direction = directionalLight2.direction;
        if (bigger(direction.y, 50)) {
            directionalLight2.setDirection(-0.5f, -25, -1);
        } else {
            directionalLight2.direction.add(0, PROBLEM_SPEED.get() / 2, 0);
        }
    }

    private void reddenLight() {
        float r = directionalLight.color.r;
        if (bigger(1f, r)) {
            r += 0.1;
        }
        float g = directionalLight.color.g;
        if (bigger(g, 0f)) {
            g -= 0.1;
        }
        float b = directionalLight.color.b;
        if (bigger(b, 0f)) {
            b -= 0.1;
        }
        directionalLight.color.set(r, g, b, 1.f);
    }

}
