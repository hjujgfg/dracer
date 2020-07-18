package org.hjujgfg.dracer.events;

import org.hjujgfg.dracer.events.event.CollisionEvent;

import static org.hjujgfg.dracer.world.BigStatic.COLLISION_EVENT_STORE;

public class CollisionEventProducer implements EventProducer<CollisionEvent> {



    @Override
    public void produceEvent() {
        COLLISION_EVENT_STORE.add(new CollisionEvent());
    }
}
