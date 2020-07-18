package org.hjujgfg.dracer.world.interfaces;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import java.util.Collection;

public interface ModelSupplier extends Disposable {

    Collection<ModelInstance> getModels();
}
