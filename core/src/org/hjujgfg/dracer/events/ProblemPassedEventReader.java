package org.hjujgfg.dracer.events;

import org.hjujgfg.dracer.events.event.ProblemPassedEvent;
import org.hjujgfg.dracer.world.GameContext;

import static org.hjujgfg.dracer.world.BigStatic.PROBLEM_PASSED_EVENT_STORE;

public class ProblemPassedEventReader extends Thread {
    private boolean run = true;
    private GameContext context;

    public ProblemPassedEventReader(GameContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        while (run) {
            while (PROBLEM_PASSED_EVENT_STORE.hasEvent()) {
                ProblemPassedEvent event = PROBLEM_PASSED_EVENT_STORE.read();
                context.getPassedProblems().passProblem();
            }
        }
    }

    public void close() {
        run = false;
    }
}
