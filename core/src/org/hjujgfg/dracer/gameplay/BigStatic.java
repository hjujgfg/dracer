package org.hjujgfg.dracer.gameplay;

import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import org.hjujgfg.dracer.events.BaseEventStore;
import org.hjujgfg.dracer.events.CollisionEventProducer;
import org.hjujgfg.dracer.events.GameEventCollisionEventProducer;
import org.hjujgfg.dracer.events.ProblemPassedEventProducer;
import org.hjujgfg.dracer.events.event.BaseEvent;
import org.hjujgfg.dracer.input.TouchAdapter;
import org.hjujgfg.dracer.world.control.SwipeHandler;
import org.hjujgfg.dracer.world.control.TouchHandler;

import java.util.Random;

public class BigStatic {


    public final static Random RANDOM = new Random();

    public final static ModelBuilder MODEL_BUILDER = new ModelBuilder();
    public final static ObjLoader OBJ_LOADER = new ObjLoader();

    public final static TouchHandler TOUCH_HANDLER = new TouchHandler();
    public final static TouchAdapter TOUCH_ADAPTER = new TouchAdapter(
            TOUCH_HANDLER::handleLeftPressed,
            TOUCH_HANDLER::handleLeftUp,
            TOUCH_HANDLER::handleRightPressed,
            TOUCH_HANDLER::handleRightUp
    );

    public final static SwipeHandler SWIPE_HANDLER = new SwipeHandler();

    public final static BaseEventStore<BaseEvent> BASE_EVENT_STORE = new BaseEventStore<>();

    public final static CollisionEventProducer COLLISION_EVENT_PRODUCER = new CollisionEventProducer();

    public final static ProblemPassedEventProducer PROBLEM_PASSED_EVENT_PRODUCER = new ProblemPassedEventProducer();

    public final static GameEventCollisionEventProducer GAME_EVENT_COLLISION_EVENT_PRODUCER = new GameEventCollisionEventProducer();

    //public final static Vehicle VEHICLE = new Vehicle();
}
