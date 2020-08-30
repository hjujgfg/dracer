package org.hjujgfg.dracer.events;

import org.hjujgfg.dracer.events.event.BaseEvent;
import org.hjujgfg.dracer.events.event.EventType;

import java.util.ArrayList;
import java.util.LinkedList;

public class BaseEventStore<T extends BaseEvent> implements EventStore<T> {

    private ArrayList<T> events = new ArrayList<>();

    @Override
    public synchronized void add(T event) {
        events.add(event);
    }

    @Override
    public synchronized T read() {
        //return events.pop();
        return null;
    }

    public synchronized void readOfType(EventType eventType) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getType() == eventType) {
                events.remove(i);
                break;
            }
        }
    }

    @Override
    public boolean hasEvent() {
        return !events.isEmpty();
    }

    public synchronized boolean hasEventOfType(EventType type) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getType() == type) {
                return true;
            }
        }
        return false;
    }
}
