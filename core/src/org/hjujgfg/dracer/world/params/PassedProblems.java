package org.hjujgfg.dracer.world.params;

public class PassedProblems {

    private long totalPassedProblems = 0;
    private long passedProblemsWithoutCollision = 0;

    public void passProblem() {
        totalPassedProblems += 1;
        passedProblemsWithoutCollision += 1;
    }

    public void collide() {
        passedProblemsWithoutCollision = 0;
    }

    public long getTotalPassedProblems() {
        return totalPassedProblems;
    }

    public long getPassedProblemsWithoutCollision() {
        return passedProblemsWithoutCollision;
    }

    public boolean ultReady() {
        return passedProblemsWithoutCollision >= 20;
    }
}
