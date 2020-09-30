package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;

import org.hjujgfg.dracer.world.interfaces.ModelSupplier;

import java.util.Collection;
import java.util.HashSet;

import static org.hjujgfg.dracer.gameplay.BigStatic.OBJ_LOADER;

public class Whale implements ModelSupplier {

    private final Model model;
    private final ModelInstance instance;

    public Whale() {
        model = OBJ_LOADER.loadModel(Gdx.files.internal("whale/whale.obj"));
        instance = new ModelInstance(model, 2, 10, 0);
    }

    @Override
    public Collection<ModelInstance> getModels() {
        HashSet<ModelInstance> t = new HashSet<>();
        t.add(instance);
        return t;
    }

    @Override
    public void dispose() {
        model.dispose();
    }
}
