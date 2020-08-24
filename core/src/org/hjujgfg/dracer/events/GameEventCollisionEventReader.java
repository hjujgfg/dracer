package org.hjujgfg.dracer.events;

import com.badlogic.gdx.Gdx;

import org.hjujgfg.dracer.events.event.EventType;
import org.hjujgfg.dracer.gameplay.GameContext;

public class GameEventCollisionEventReader extends BaseEventReader {

    private GameContext context;

    public GameEventCollisionEventReader(GameContext context, Runnable... consumers) {
        super(consumers);
        this.context = context;
    }

    @Override
    protected EventType getType() {
        return EventType.GAME_EVENT_COLLISION;
    }

    @Override
    protected void handleEvent() {
        //Gdx.app.log("EVENT_INTERACTION", String.format("Handling event"));
        context.getGameEvent().randomizeEventPosition();
        context.enableTopView();
    }
}
