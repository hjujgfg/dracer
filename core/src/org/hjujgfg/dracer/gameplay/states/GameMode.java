package org.hjujgfg.dracer.gameplay.states;

public enum GameMode {

    PROBLEM_EVASION(null),
    TOP_VIEW_PROBLEM_EVASION(1000 * 10),
    BLURRED(500),
    LONG_BLURRED(5000);

    public Integer modeLength;

    GameMode(Integer modeLength) {
        this.modeLength = modeLength;
    }
}
