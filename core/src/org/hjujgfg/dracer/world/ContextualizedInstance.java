package org.hjujgfg.dracer.world;

import org.hjujgfg.dracer.gameplay.GameContext;

public abstract class ContextualizedInstance {

    protected org.hjujgfg.dracer.gameplay.GameContext context;

    public ContextualizedInstance(GameContext context) {
        this.context = context;
    }
}
