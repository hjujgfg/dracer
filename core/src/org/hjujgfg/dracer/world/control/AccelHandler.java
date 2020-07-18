package org.hjujgfg.dracer.world.control;

import com.badlogic.gdx.Gdx;

public class AccelHandler {

    float accelX, accelY, accelZ;
    public AccelHandler() {
        accelX = Gdx.input.getAccelerometerX();
        accelY = Gdx.input.getAccelerometerY();
        accelZ = Gdx.input.getAccelerometerZ();
    }

    public void check() {
        accelX = Gdx.input.getAccelerometerX();
        accelY = Gdx.input.getAccelerometerY();
        accelZ = Gdx.input.getAccelerometerZ();
    }
}
