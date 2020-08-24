package org.hjujgfg.dracer.events.event;

public class ProblemPassedEvent implements BaseEvent {

    @Override
    public EventType getType() {
        return EventType.PROBLEM_PASSED;
    }
}
