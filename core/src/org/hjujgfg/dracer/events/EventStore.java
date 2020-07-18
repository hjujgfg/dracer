package org.hjujgfg.dracer.events;

public interface EventStore <T> {

    void add(T event);

    T read();

    boolean hasEvent();
}
