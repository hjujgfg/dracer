package org.hjujgfg.dracer.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TouchAdapter extends InputAdapter {

    private List<Consumer<Integer>> leftPressedActions;
    private List<Consumer<Integer>> leftUpAction;
    private List<Consumer<Integer>> rightPressedActions;
    private List<Consumer<Integer>> rightUpAction;

    public TouchAdapter(Consumer<Integer> leftPressedAction,
                        Consumer<Integer> leftUpAction,
                        Consumer<Integer> rightPressedAction,
                        Consumer<Integer> rightUpAction) {
        List<Consumer<Integer>> lpa = new ArrayList<>(1);
        lpa.add(leftPressedAction);
        List<Consumer<Integer>> lua = new ArrayList<>(1);
        lua.add(leftUpAction);
        List<Consumer<Integer>> rpa = new ArrayList<>(1);
        rpa.add(rightPressedAction);
        List<Consumer<Integer>> rua = new ArrayList<>(1);
        rua.add(rightUpAction);
        this.leftPressedActions = lpa;
        this.leftUpAction = lua;
        this.rightPressedActions = rpa;
        this.rightUpAction = rua;
    }

    public TouchAdapter(List<Consumer<Integer>> leftPressedActions,
                        List<Consumer<Integer>> leftUpAction,
                        List<Consumer<Integer>> rightPressedActions,
                        List<Consumer<Integer>> rightUpAction) {
        this.leftPressedActions = leftPressedActions;
        this.leftUpAction = leftUpAction;
        this.rightPressedActions = rightPressedActions;
        this.rightUpAction = rightUpAction;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //Gdx.app.log("INFO", String.format("Touch down happened %d:%d with pointer %d, btn: %d",
        //        screenX, screenY, pointer, button));
        if (Gdx.graphics.getWidth() / 2 < screenX) {
            rightPressedActions.forEach(c -> c.accept(pointer));
        } else {
            leftPressedActions.forEach(c -> c.accept(pointer));
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //Gdx.app.log("INFO", String.format("Touch up happened %d:%d with pointer %d, btn: %d",
        //        screenX, screenY, pointer, button));
        if (Gdx.graphics.getWidth() / 2 < screenX) {
            rightUpAction.forEach(c -> c.accept(pointer));
        } else {
            leftUpAction.forEach(c -> c.accept(pointer));
        }
        return false;
    }
}
