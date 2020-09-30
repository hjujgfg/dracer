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
        float res = problemSpeed + delta;
        if (bigger(res, minimalProblemSpeed)) {
            return;
        }
        problemSpeed = res;
    }

    public boolean minimalThresholdNotPassed() {
        return bigger(minimalProblemSpeed, MINIMAL_MINIMAL_SPEED);
    }

    public void changeMinimal(float delta) {
        float res = minimalProblemSpeed + delta;
        if (bigger(res, 2.f)) {
            return;
        }
        minimalProblemSpeed = res;
    }

    public boolean isSlow() {
        return bigger(minimalProblemSpeed, problemSpeed);
    }

    public boolean isFast() {
        return bigger(problemSpeed, minimalProblemSpeed);
    }

}
