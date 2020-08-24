package org.hjujgfg.dracer.world.interactions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.gameplay.GameContext;
import org.hjujgfg.dracer.world.ContextualizedInstance;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import static org.hjujgfg.dracer.gameplay.BigStatic.GAME_EVENT_COLLISION_EVENT_PRODUCER;
import static org.hjujgfg.dracer.world.models.ModelType.VEHICLE;

public class EventInteraction extends ContextualizedInstance implements RenderAction {
    private final Vector3 TMP = new Vector3();

    public EventInteraction(GameContext context) {
        super(context);
    }

    @Override
    public void render() {
        Vector3 instancePosition = context.getTransform(VEHICLE).getTranslation(new Vector3());
        float dst = context.getGameEvent().getModel().transform.getTranslation(TMP).dst(instancePosition);
        if (dst < 1.5f) {
            GAME_EVENT_COLLISION_EVENT_PRODUCER.produceEvent();
        }
    }
}
