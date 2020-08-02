package org.hjujgfg.dracer.world.interfaces;

import com.badlogic.gdx.graphics.g3d.environment.BaseLight;

import java.util.ArrayList;
import java.util.Collection;

public interface LightSupplier<T extends BaseLight<T>> {

    T getLight();

    default Collection<T> getLights() {
        ArrayList<T> list = new ArrayList<>();
        list.add(getLight());
        return list;
    }

}
