package org.hjujgfg.dracer.events.event;

public class CollisionEvent implements BaseEvent {

    @Override
    public EventType getType() {
        return EventType.PROBLEM_COLLISION;
    }
}
