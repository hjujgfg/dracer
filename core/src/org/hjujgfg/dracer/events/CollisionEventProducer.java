package org.hjujgfg.dracer.events;

import org.hjujgfg.dracer.events.event.CollisionEvent;

import static org.hjujgfg.dracer.gameplay.BigStatic.BASE_EVENT_STORE;

public class CollisionEventProducer implements EventProducer<CollisionEvent> {

    @Override
    public void produceEvent() {
        BASE_EVENT_STORE.add(new CollisionEvent());
    }
}
