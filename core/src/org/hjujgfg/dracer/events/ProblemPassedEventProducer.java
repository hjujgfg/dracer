package org.hjujgfg.dracer.events;

import org.hjujgfg.dracer.events.event.ProblemPassedEvent;

import static org.hjujgfg.dracer.world.BigStatic.PROBLEM_PASSED_EVENT_STORE;

public class ProblemPassedEventProducer implements EventProducer<ProblemPassedEvent> {


    @Override
    public void produceEvent() {
        PROBLEM_PASSED_EVENT_STORE.add(new ProblemPassedEvent());
    }
}
