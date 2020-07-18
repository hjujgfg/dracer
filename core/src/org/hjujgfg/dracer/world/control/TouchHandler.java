package org.hjujgfg.dracer.world.control;

import org.hjujgfg.dracer.world.params.control.Direction;

import java.time.Instant;

public class TouchHandler {

    private Instant prevLeftPressed, lastLeftPressed, prevRightPressed, lastRightPressed;
    boolean shouldMoveLeft, shouldMoveRight;
    public Direction activeDirection = Direction.NONE;

    public void handleLeftPressed(int pointer) {
        prevLeftPressed = lastLeftPressed;
        lastLeftPressed = Instant.now();
        shouldMoveLeft = true;
        updateActiveDirection();
    }

    public void handleRightPressed(int pointer) {
        prevRightPressed = lastRightPressed;
        lastRightPressed = Instant.now();
        shouldMoveRight = true;
        updateActiveDirection();
    }

    public void handleLeftUp(int pointer) {
        shouldMoveLeft = false;
        updateActiveDirection();
    }

    public void handleRightUp(int pointer) {
        shouldMoveRight = false;
        updateActiveDirection();
    }

    public boolean isBoth() {
        return activeDirection == Direction.BOTH;
    }

    public boolean isNone() {
        return activeDirection == Direction.NONE;
    }

    public boolean isLeftOrRight() {
        return activeDirection == Direction.RIGHT || activeDirection == Direction.LEFT;
    }

    public boolean isDoubleTap() {
        if (activeDirection == Direction.LEFT) {
            return isLeftDoubleTap();
        }
        if (activeDirection == Direction.RIGHT) {
            return isRightDoubleTap();
        }
        return false;
    }

    public boolean isLeftDoubleTap() {
        if (prevLeftPressed == null || lastLeftPressed == null) {
            return false;
        }
        long millisPassed = lastLeftPressed.toEpochMilli() - prevLeftPressed.toEpochMilli();
        return millisPassed < 300;
    }

    public boolean isRightDoubleTap() {
        if (prevRightPressed == null || lastRightPressed == null) {
            return false;
        }
        long millisPassed = lastRightPressed.toEpochMilli() - prevRightPressed.toEpochMilli();
        return millisPassed < 300;
    }

    private void updateActiveDirection() {
        if (shouldMoveLeft && shouldMoveRight) {
            activeDirection = Direction.BOTH;
        } else if (shouldMoveRight) {
            activeDirection = Direction.RIGHT;
        } else if (shouldMoveLeft) {
            activeDirection = Direction.LEFT;
        } else {
            activeDirection = Direction.NONE;
        }
    }
}
