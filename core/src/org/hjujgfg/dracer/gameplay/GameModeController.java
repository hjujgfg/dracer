package org.hjujgfg.dracer.gameplay;

import org.hjujgfg.dracer.gameplay.states.GameMode;

import static org.hjujgfg.dracer.gameplay.states.GameMode.PROBLEM_EVASION;
import static org.hjujgfg.dracer.gameplay.states.GameMode.TOP_VIEW_PROBLEM_EVASION;

public class GameModeController {

    private GameMode gameMode;
    private long lastSwitch;
    private Countdown countdown;
    private volatile long diff;

    public GameModeController() {
        this.gameMode = PROBLEM_EVASION;
        this.lastSwitch = System.currentTimeMillis();
        this.countdown = new Countdown();
        countdown.start();
    }

    public void enableTopView() {
        gameMode = TOP_VIEW_PROBLEM_EVASION;
        lastSwitch = System.currentTimeMillis();
    }

    public void enableProblemEvasion() {
        long diff = System.currentTimeMillis() - lastSwitch;
        if (diff > TOP_VIEW_PROBLEM_EVASION.modeLength) {
            gameMode = PROBLEM_EVASION;
        }
        lastSwitch = System.currentTimeMillis();
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public long getDiff() {
        return diff;
    }

    private class Countdown extends Thread {
        private boolean run = true;
        public void run() {
            while (run) {
                if (gameMode == TOP_VIEW_PROBLEM_EVASION) {
                    diff = System.currentTimeMillis() - lastSwitch;
                    if (diff > TOP_VIEW_PROBLEM_EVASION.modeLength) {
                        gameMode = PROBLEM_EVASION;
                    }
                }
            }
        }
    }
}
