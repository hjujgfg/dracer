package org.hjujgfg.dracer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class ContinuousTouchAdapter extends InputAdapter {

    private int activePointer;
    private BiConsumer<Integer, Integer> down, drag, up;

    public ContinuousTouchAdapter(BiConsumer<Integer, Integer> down,
                                  BiConsumer<Integer, Integer> drag,
                                  BiConsumer<Integer, Integer> up) {
        this.down = down;
        this.drag = drag;
        this.up = up;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //Gdx.app.log("Continuous touch", String.format(" Down x: %d, y: %d, pointer: %d", screenX, screenY, pointer));
        if (activePointer == -1) {
            activePointer = pointer;
        }
        if (pointer == 0) {
            down.accept(screenX, screenY);
        }
        return true;
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        //Gdx.app.log("Continuous touch", String.format("Drag x: %d, y: %d, pointer: %d", screenX, screenY, pointer));
        if (pointer == 0) {
            drag.accept(screenX, screenY);
        }
        return true;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //Gdx.app.log("Continuous touch", String.format("Up x: %d, y: %d, pointer: %d", screenX, screenY, pointer));
        if (pointer == 0) {
            up.accept(screenX, screenY);
            activePointer = -1;
        }
        return true;
    }
}
