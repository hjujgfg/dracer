package org.hjujgfg.dracer.events;

import org.hjujgfg.dracer.events.event.BaseEvent;
import org.hjujgfg.dracer.events.event.EventType;

import java.util.LinkedList;

public class BaseEventStore<T extends BaseEvent> implements EventStore<T> {

    private LinkedList<T> events = new LinkedList<>();

    @Override
    public synchronized void add(T event) {
        events.add(event);
    }

    @Override
    public synchronized T read() {
        return events.pop();
    }

    public synchronized void readOfType(EventType eventType) {
        BaseEvent event = events.stream().filter(e -> e.getType() == eventType).findFirst().get();
        events.remove(event);
    }

    @Override
    public boolean hasEvent() {
        return !events.isEmpty();
    }

    public synchronized boolean hasEventOfType(EventType type) {
        return events.stream().anyMatch(e -> e.getType() == type);
    }
}
