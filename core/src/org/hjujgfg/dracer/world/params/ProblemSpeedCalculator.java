package org.hjujgfg.dracer.world.params;

import org.hjujgfg.dracer.world.GameContext;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class ProblemSpeedCalculator implements RenderAction {

    private GameContext context;

    public ProblemSpeedCalculator(GameContext context) {
        this.context = context;
    }

    @Override
    public void render() {
        if (context.isInUlt() || PROBLEM_SPEED.isSlow()) {
            PROBLEM_SPEED.set(Math.min(PROBLEM_SPEED.get() + 0.1f, 2));
        } else if (PROBLEM_SPEED.isFast()) {
            PROBLEM_SPEED.change(-0.1f);
        }
    }
}
