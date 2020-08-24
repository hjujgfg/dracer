package org.hjujgfg.dracer.world.params;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;

public class ProblemSpeed {

    public static float MINIMAL_MINIMAL_SPEED = 0.5f;

    private float problemSpeed = 0.5f;
    private float minimalProblemSpeed = problemSpeed;

    public float get() {
        return problemSpeed;
    }

    public float getMinimal() {
        return minimalProblemSpeed;
    }

    public void set(float speed) {
        problemSpeed = speed;
    }

    public void setMinimal(float minimal) {
        minimalProblemSpeed = minimal;
    }

    public void change(float delta) {
        problemSpeed += delta;
    }

    public boolean minimalThresholdNotPassed() {
        return bigger(minimalProblemSpeed, MINIMAL_MINIMAL_SPEED);
    }

    public void changeMinimal(float delta) {
        minimalProblemSpeed += delta;
    }

    public boolean isSlow() {
        return bigger(minimalProblemSpeed, problemSpeed);
    }

    public boolean isFast() {
        return bigger(problemSpeed, minimalProblemSpeed);
    }

}
