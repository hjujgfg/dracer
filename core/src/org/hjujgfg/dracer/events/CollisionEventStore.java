package org.hjujgfg.dracer.events;

import org.hjujgfg.dracer.events.event.CollisionEvent;

import java.util.LinkedList;

public class CollisionEventStore implements EventStore<CollisionEvent> {

    private LinkedList<CollisionEvent> events = new LinkedList<>();

    @Override
    public void add(CollisionEvent event) {
        events.add(event);
    }

    @Override
    public CollisionEvent read() {
        return events.pop();
    }

    @Override
    public boolean hasEvent() {
        return !events.isEmpty();
    }


}
