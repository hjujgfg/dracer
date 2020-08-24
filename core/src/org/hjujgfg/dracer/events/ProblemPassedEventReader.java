package org.hjujgfg.dracer.events;

import org.hjujgfg.dracer.events.event.EventType;
import org.hjujgfg.dracer.gameplay.GameContext;

public class ProblemPassedEventReader extends BaseEventReader {
    private GameContext context;

    public ProblemPassedEventReader(GameContext context) {
        this.context = context;
    }

    @Override
    protected EventType getType() {
        return EventType.PROBLEM_PASSED;
    }

    @Override
    protected void handleEvent() {
        context.getPassedProblems().passProblem();
    }

}
