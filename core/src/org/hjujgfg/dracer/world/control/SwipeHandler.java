package org.hjujgfg.dracer.world.control;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Map;

public class SwipeHandler {

    private int currentLine = 0;
    private final static int MAX_LINE = 2;
    private final static int MIN_LINE = -2;
    private final static Map<Integer, Float> lineToCoord = new HashMap<>();

    static {
        for (int i = MIN_LINE; i <= MAX_LINE; i++) {
            lineToCoord.put(i, 2.1f * i);
        }
    }

    public float currentLineCoord() {
        return lineToCoord.get(currentLine);
    }

    public void moveRight() {
        Gdx.app.log("SWIPE", "Move right called");
        if (currentLine > MIN_LINE) {
            currentLine --;
        }
    }

    public void moveLeft() {
        Gdx.app.log("SWIPE", "Move left called");
        if (currentLine < MAX_LINE) {
            currentLine ++;
        }
    }
}
