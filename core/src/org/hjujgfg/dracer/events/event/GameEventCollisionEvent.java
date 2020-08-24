package org.hjujgfg.dracer.events.event;

import static org.hjujgfg.dracer.events.event.EventType.GAME_EVENT_COLLISION;

public class GameEventCollisionEvent implements BaseEvent {

    @Override
    public EventType getType() {
        return GAME_EVENT_COLLISION;
    }
}
