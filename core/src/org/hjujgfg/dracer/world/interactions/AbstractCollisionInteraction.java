package org.hjujgfg.dracer.world.interactions;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.gameplay.GameContext;
import org.hjujgfg.dracer.world.ContextualizedInstance;

import static org.hjujgfg.dracer.gameplay.BigStatic.COLLISION_EVENT_PRODUCER;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class AbstractCollisionInteraction extends ContextualizedInstance {

    private final Vector3 TMP = new Vector3();

    public AbstractCollisionInteraction(GameContext context) {
        super(context);
    }
}
