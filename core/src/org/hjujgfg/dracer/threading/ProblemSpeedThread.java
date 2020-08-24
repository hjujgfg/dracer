package org.hjujgfg.dracer.threading;

import com.badlogic.gdx.Gdx;

import org.hjujgfg.dracer.gameplay.GameContext;

import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class ProblemSpeedThread extends Thread {
    private boolean run = true;
    private GameContext context;

    public ProblemSpeedThread(GameContext context) {
        this.context = context;
    }

    @Override
    public void run() {
        while (run) {
            correctSpeed();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Gdx.app.log("SPEED_THREAD", "sleep failed", e);
            }
        }
    }

    private void correctSpeed() {
        if (context.isInUlt() || PROBLEM_SPEED.isSlow()) {
            PROBLEM_SPEED.set(Math.min(PROBLEM_SPEED.get() + 0.1f, 2));
        } else if (PROBLEM_SPEED.isFast()) {
            PROBLEM_SPEED.change(-0.1f);
        }
    }

    public void close() {
        run = false;
    }
}
