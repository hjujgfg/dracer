package org.hjujgfg.dracer.world.params.control;

public enum Direction {

    LEFT(-1),
    RIGHT(1),
    BOTH(0),
    NONE(0);

    public int multiplier;

    Direction(int multiplier) {
        this.multiplier = multiplier;
    }
}
