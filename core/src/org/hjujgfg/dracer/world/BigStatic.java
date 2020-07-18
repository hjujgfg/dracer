package org.hjujgfg.dracer.world;

import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import org.hjujgfg.dracer.events.CollisionEventProducer;
import org.hjujgfg.dracer.events.CollisionEventStore;
import org.hjujgfg.dracer.input.TouchAdapter;
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


    public final static CollisionEventStore COLLISION_EVENT_STORE = new CollisionEventStore();
    public final static CollisionEventProducer COLLISION_EVENT_PRODUCER = new CollisionEventProducer();

    //public final static Vehicle VEHICLE = new Vehicle();
}
