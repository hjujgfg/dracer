package org.hjujgfg.dracer.gameplay;

import org.hjujgfg.dracer.gameplay.states.GameMode;

import static org.hjujgfg.dracer.gameplay.states.GameMode.BLURRED;
import static org.hjujgfg.dracer.gameplay.states.GameMode.LONG_BLURRED;
import static org.hjujgfg.dracer.gameplay.states.GameMode.PROBLEM_EVASION;
import static org.hjujgfg.dracer.gameplay.states.GameMode.TOP_VIEW_PROBLEM_EVASION;

public class GameModeController {

    private GameMode gameMode;
    private long lastSwitch;
    private Countdown countdown;
    private volatile long diff;
    private boolean isInManualSteering;

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

    public void enableBlurred() {
        gameMode = BLURRED;
        lastSwitch = System.currentTimeMillis();
    }

    public void enableLongBlurred() {
        gameMode = LONG_BLURRED;
        lastSwitch = System.currentTimeMillis();
    }

    public void enableProblemEvasion() {
        long diff = System.currentTimeMillis() - lastSwitch;
        if (diff > TOP_VIEW_PROBLEM_EVASION.modeLength) {
            gameMode = PROBLEM_EVASION;
        }
        lastSwitch = System.currentTimeMillis();
    }

    public void startManualSteering() {
        isInManualSteering = true;
    }

    public void stopManualSteering() {
        isInManualSteering = false;
    }

    public boolean isInManualSteering() {
        return isInManualSteering;
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
                if (gameMode != PROBLEM_EVASION) {
                    diff = System.currentTimeMillis() - lastSwitch;
                    if (diff > gameMode.modeLength) {
                        gameMode = PROBLEM_EVASION;
                    }
                }
            }
        }
    }
}
