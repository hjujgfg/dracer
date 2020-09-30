package org.hjujgfg.dracer.events;

import com.badlogic.gdx.Gdx;

import org.hjujgfg.dracer.events.event.EventType;
import org.hjujgfg.dracer.gameplay.GameContext;
import org.hjujgfg.dracer.world.overlay.StatsOverlay;

public class CollisionEventReader extends BaseEventReader {

    StatsOverlay overlay;
    GameContext context;

    public CollisionEventReader(StatsOverlay overlay, GameContext context, Runnable... consumers) {
        super(consumers);
        this.overlay = overlay;
        this.context = context;
    }

    @Override
    protected EventType getType() {
        return EventType.PROBLEM_COLLISION;
    }

    @Override
    protected void handleEvent() {
        addHit();
    }

    private void addHit()  {
        overlay.addHit();
        context.passedProblems.collide();
        context.getTerrain().spike();
        context.enableBlurred();
        //Gdx.input.vibrate(50);
    }
}
