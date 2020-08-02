package org.hjujgfg.dracer.events;

import org.hjujgfg.dracer.events.event.CollisionEvent;
import org.hjujgfg.dracer.world.GameContext;
import org.hjujgfg.dracer.world.overlay.StatsOverlay;

import static org.hjujgfg.dracer.world.BigStatic.COLLISION_EVENT_STORE;

public class CollisionEventReader extends Thread {

    private boolean run = true;
    StatsOverlay overlay;
    GameContext context;

    public CollisionEventReader(StatsOverlay overlay, GameContext context) {
        this.overlay = overlay;
        this.context = context;
    }

    @Override
    public void run() {
        while (run) {
            while (COLLISION_EVENT_STORE.hasEvent()) {
                CollisionEvent event = COLLISION_EVENT_STORE.read();
                overlay.addHit();
                context.passedProblems.collide();
            }
        }
    }

    public void close() {
        run = false;
    }
}
