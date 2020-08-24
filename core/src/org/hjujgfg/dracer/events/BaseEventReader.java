package org.hjujgfg.dracer.events;

import org.hjujgfg.dracer.events.event.BaseEvent;
import org.hjujgfg.dracer.events.event.EventType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.hjujgfg.dracer.gameplay.BigStatic.BASE_EVENT_STORE;

public abstract class BaseEventReader extends Thread {

    private boolean run = true;
    private Collection<Runnable> externalConsumers;

    public BaseEventReader(Runnable... consumers) {
        externalConsumers = new ArrayList<>();
        if (consumers != null && consumers.length > 0) {
            externalConsumers.addAll(Arrays.asList(consumers));
        }
    }

    @Override
    public void run() {
        while (run) {
            while (BASE_EVENT_STORE.hasEventOfType(getType())) {
                BASE_EVENT_STORE.readOfType(getType());
                handleEvent();
                externalConsumers.forEach(Runnable::run);
            }
        }
    }

    protected abstract EventType getType();

    protected abstract void handleEvent();

    public void close() {
        run = false;
    }
}
