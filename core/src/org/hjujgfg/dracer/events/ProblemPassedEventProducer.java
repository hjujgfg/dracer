package org.hjujgfg.dracer.events;

import org.hjujgfg.dracer.events.event.ProblemPassedEvent;

import static org.hjujgfg.dracer.gameplay.BigStatic.BASE_EVENT_STORE;

public class ProblemPassedEventProducer implements EventProducer<ProblemPassedEvent> {

    @Override
    public void produceEvent() {
        BASE_EVENT_STORE.add(new ProblemPassedEvent());
    }
}
