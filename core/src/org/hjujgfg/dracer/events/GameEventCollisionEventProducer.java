package org.hjujgfg.dracer.events;

import org.hjujgfg.dracer.events.event.GameEventCollisionEvent;

import static org.hjujgfg.dracer.gameplay.BigStatic.BASE_EVENT_STORE;

public class GameEventCollisionEventProducer implements EventProducer<GameEventCollisionEvent> {

    @Override
    public void produceEvent() {
        BASE_EVENT_STORE.add(new GameEventCollisionEvent());
    }
}
