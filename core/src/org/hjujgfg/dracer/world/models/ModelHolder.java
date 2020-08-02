package org.hjujgfg.dracer.world.models;

import org.hjujgfg.dracer.world.interfaces.TransformSupplier;

import java.util.HashMap;
import java.util.Map;

public class ModelHolder {

    Floor floor;
    Vehicle vehicle;
    Problem problem;
    Houses houses;
    TiledFlor tiledFlor;
    Ground ground;
    public Map<ModelType, TransformSupplier> transformSupplierMap;

    public ModelHolder() {
        floor = new Floor();
        vehicle = new Vehicle();
        problem = new Problem();
        houses = new Houses();
        tiledFlor = new TiledFlor();
        ground = new Ground();
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

    public Houses getHouses() {
        return houses;
    }

    public Ground getGround() {
        return ground;
    }

    public TiledFlor getTiledFlor() {
        return tiledFlor;
    }

}
