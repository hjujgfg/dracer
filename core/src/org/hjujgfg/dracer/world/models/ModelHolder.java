package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.graphics.g3d.ModelInstance;

import org.hjujgfg.dracer.world.interfaces.TransformSupplier;

import java.util.HashMap;
import java.util.Map;

public class ModelHolder {

    Floor floor;
    Vehicle vehicle;
    Problem problem;
    public Map<ModelType, TransformSupplier> transformSupplierMap;

    public ModelHolder() {
        floor = new Floor();
        vehicle = new Vehicle();
        problem = new Problem();
        transformSupplierMap = new HashMap<>();
        transformSupplierMap.put(ModelType.VEHICLE, vehicle);
        transformSupplierMap.put(ModelType.PROBLEM, problem);
    }

    public Floor getFloor() {
        return floor;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Problem getProblem() {
        return problem;
    }

}
