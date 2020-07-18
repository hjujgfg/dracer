package org.hjujgfg.dracer.world;

public abstract class ContextualizedInstance {

    protected GameContext context;

    public ContextualizedInstance(GameContext context) {
        this.context = context;
    }
}
