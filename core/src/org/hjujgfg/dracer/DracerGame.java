package org.hjujgfg.dracer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.scenes.scene2d.Stage;

import org.hjujgfg.dracer.events.CollisionEventReader;
import org.hjujgfg.dracer.world.GameContext;
import org.hjujgfg.dracer.world.camera.PerspectiveCameraSupplier;
import org.hjujgfg.dracer.world.interactions.CollisionInteraction;
import org.hjujgfg.dracer.world.interactions.PreCollisionInteraction;
import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;
import org.hjujgfg.dracer.world.light.DirectionalLightSupplier;
import org.hjujgfg.dracer.world.models.ModelHolder;
import org.hjujgfg.dracer.world.overlay.StatsOverlay;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.hjujgfg.dracer.world.BigStatic.TOUCH_ADAPTER;


public class DracerGame extends InputAdapter implements ApplicationListener {

	protected Stage stage;

	public Environment environment;

	List<ModelInstance> instances;
	ModelBatch modelBatch;

	DirectionalLightSupplier directionalLightSupplier;

	CollisionInteraction collisionInteraction;
	PreCollisionInteraction preCollisionInteraction;

	PerspectiveCameraSupplier cameraSupplier;
	Collection<ModelSupplier> suppliers;
	List<RenderAction> renderActions;

	StatsOverlay statsOverlay;

	@Override
	public void create () {
		GameContext context = new GameContext();
		modelBatch = new ModelBatch();
		Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight,
				0.4f, 0.4f, 0.4f, 1f));


		directionalLightSupplier = new DirectionalLightSupplier();
		environment.add(directionalLightSupplier.getLight());
		cameraSupplier = new PerspectiveCameraSupplier(context);

		stage = new Stage();
		statsOverlay = new StatsOverlay(stage, context);
		CollisionEventReader collisionEventReader = new CollisionEventReader(statsOverlay);
		collisionEventReader.start();

		collisionInteraction = new CollisionInteraction(context);
		preCollisionInteraction = new PreCollisionInteraction(context);

		suppliers = new ArrayList<>();
		suppliers.add(context.getFloor());
		suppliers.add(context.getVehicle());
		suppliers.add(context.getProblem());

		renderActions = new LinkedList<>();
		renderActions.add(context.getFloor());
		renderActions.add(context.getVehicle());
		renderActions.add(context.getProblem());
		renderActions.add(directionalLightSupplier);
		renderActions.add(cameraSupplier);
		renderActions.add(collisionInteraction);
		//renderActions.add(preCollisionInteraction);

		instances = new ArrayList<>();
		suppliers.forEach(supplier -> instances.addAll(supplier.getModels()));

		Gdx.input.setInputProcessor(new InputMultiplexer(TOUCH_ADAPTER));
		//CameraInputController camController = new CameraInputController(cam);
		//Gdx.input.setInputProcessor(camController);
	}

	@Override
	public void render () {
		renderActions.forEach(RenderAction::render);

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cameraSupplier.getCamera());
		modelBatch.render(instances, environment);
		modelBatch.end();

		statsOverlay.render();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {
		stage.dispose();
		suppliers.forEach(ModelSupplier::dispose);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

}
