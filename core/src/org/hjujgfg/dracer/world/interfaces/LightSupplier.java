package org.hjujgfg.dracer.world.interfaces;

import com.badlogic.gdx.graphics.g3d.environment.BaseLight;

public interface LightSupplier<T extends BaseLight<T>> {

    T getLight();

}
