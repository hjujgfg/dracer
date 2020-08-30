package org.hjujgfg.dracer.input;

import static org.hjujgfg.dracer.gameplay.BigStatic.SWIPE_HANDLER;

public class SwipeDirectionListener implements SwipeGestureDetector.DirectionListener {
    @Override
    public void onLeft() {
        SWIPE_HANDLER.moveLeft();
    }

    @Override
    public void onRight() {
        SWIPE_HANDLER.moveRight();
    }

    @Override
    public void onUp() {

    }

    @Override
    public void onDown() {

    }
}
