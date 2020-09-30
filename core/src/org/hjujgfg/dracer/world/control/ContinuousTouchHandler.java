package org.hjujgfg.dracer.world.control;

import com.badlogic.gdx.Gdx;

import org.hjujgfg.dracer.gameplay.GameContext;
import org.hjujgfg.dracer.world.ContextualizedInstance;

public class ContinuousTouchHandler extends ContextualizedInstance {

    private boolean isPressed;
    private int prevX = Gdx.graphics.getWidth() / 2, prevY;
    private float prevVelocity;
    private float prevDelta;
    private float avgVelocity;
    private long counter;

    public ContinuousTouchHandler(GameContext context) {
        super(context);
    }

    public void handleDown(int x, int y) {
        isPressed = true;
        prevX = x;
        prevY = y;
        counter = 1;
        avgVelocity = 0;
    }

    public void handleDrag(int x, int y) {
        int diff = x - prevX;
        float velocity = diff / 50f;
        float deltaVelocity = velocity - prevVelocity;
        if (velocity > 0.2) {
            float diffx = (velocity - avgVelocity) / counter++;
            //Gdx.app.log("Continuous control", "Velocity: " + velocity + " delta: " + diffx + " avg velocity: " + avgVelocity);
            avgVelocity += diffx;
        }

        context.getVehicle().steerContinuous(prevVelocity);
        prevVelocity = velocity;
        if (prevY - y > 100) {
            context.getVehicle().jump();
            context.enableLongBlurred();
        }
        prevX = x;
        prevY = y;
    }

    public void handleUp(int x, int y) {
        isPressed = false;
        prevX = x;
        prevY = y;
        context.getVehicle().steerContinuousStop();
    }
}
