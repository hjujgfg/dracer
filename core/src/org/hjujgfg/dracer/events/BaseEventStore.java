package org.hjujgfg.dracer.events;

import java.util.LinkedList;

public abstract class BaseEventStore<T> implements EventStore<T> {

    private LinkedList<T> events = new LinkedList<>();

    @Override
    public synchronized void add(T event) {
        events.add(event);
    }

    @Override
    public synchronized T read() {
        return events.pop();
    }

    @Override
    public boolean hasEvent() {
        return !events.isEmpty();
    }

}
