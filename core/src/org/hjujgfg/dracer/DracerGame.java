package org.hjujgfg.dracer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.GaussianBlurEffect;

import org.hjujgfg.dracer.events.CollisionEventReader;
import org.hjujgfg.dracer.events.GameEventCollisionEventReader;
import org.hjujgfg.dracer.events.ProblemPassedEventReader;
import org.hjujgfg.dracer.gameplay.GameContext;
import org.hjujgfg.dracer.shaders.TestShader;
import org.hjujgfg.dracer.world.camera.PerspectiveCameraSupplier;
import org.hjujgfg.dracer.world.interactions.CollisionInteraction;
import org.hjujgfg.dracer.world.interactions.EventInteraction;
import org.hjujgfg.dracer.world.interactions.NearMissInteraction;
import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;
import org.hjujgfg.dracer.world.light.DirectionalLightSupplier;
import org.hjujgfg.dracer.world.overlay.StatsOverlay;
import org.hjujgfg.dracer.world.params.ProblemSpeedCalculator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static org.hjujgfg.dracer.gameplay.BigStatic.TOUCH_ADAPTER;


public class DracerGame extends InputAdapter implements ApplicationListener {

	protected Stage stage;

	public Environment environment;

	List<ModelInstance> instances;
	ModelBatch modelBatch;
	ModelBatch floorModelBatch;
	ModelCache modelCache;
	ModelCache staticCache;

	ProblemSpeedCalculator problemSpeedCalculator;

	DirectionalLightSupplier directionalLightSupplier;

	CollisionInteraction collisionInteraction;
	NearMissInteraction nearMissInteraction;
	EventInteraction gameEventInteraction;

	PerspectiveCameraSupplier cameraSupplier;
	Collection<ModelSupplier> suppliers;
	List<RenderAction> renderActions;

	StatsOverlay statsOverlay;

	GameContext context;

	RenderContext renderContext;
	TestShader shader;

	DirectionalShadowLight shadowLight;
	ModelBatch shadowBatch;

	Thread renderActionsHandler;
	private final Object lock = new Object();

	private VfxManager vfxManager;
	private GaussianBlurEffect vfxEffect;

	@Override
	public void create () {
		context = new GameContext();
		renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
		renderContext.setCullFace(GL20.GL_CULL_FACE);
		//shader = new TestShader();
		//shader.init();


		String vert = Gdx.files.internal("shaders/test.vertex.glsl").readString();
		String frag = Gdx.files.internal("shaders/test.fragment.glsl").readString();

		modelBatch = new ModelBatch();
		//floorModelBatch = new ModelBatch(new TestShaderProvider(new DefaultShader.Config(vert, frag), context));
		floorModelBatch = new ModelBatch();
		modelCache = new ModelCache();
		Gdx.gl.glClearColor(9/255f, 36/255f, 51/255f, 0.3f);
		environment = new Environment();

		environment.set(new ColorAttribute(ColorAttribute.AmbientLight,
				0.4f, 0.4f, 0.4f, 1f));

		environment.add((shadowLight = new DirectionalShadowLight(
				1024,
				1024,
				30f,
				30f,
				1f, 100f))
				.set(0.8f, 0.8f, 0.8f, -1f, -.8f, -.2f));
		//environment.shadowMap = shadowLight;

		directionalLightSupplier = new DirectionalLightSupplier(context);
		directionalLightSupplier.getLights().forEach(environment::add);
		//environment.add(context.getProblem().getLight());
		//environment.add(context.getGround().getLight());
		cameraSupplier = new PerspectiveCameraSupplier(context);

		stage = new Stage();
		statsOverlay = new StatsOverlay(stage, context);
		CollisionEventReader collisionEventReader = new CollisionEventReader(statsOverlay, context);
		collisionEventReader.start();
		ProblemPassedEventReader problemPassedEventReader = new ProblemPassedEventReader(context);
		problemPassedEventReader.start();
		GameEventCollisionEventReader gameEventCollisionEventReader = new GameEventCollisionEventReader(context);
		gameEventCollisionEventReader.start();

		/*ProblemSpeedThread problemSpeedThread = new ProblemSpeedThread(context);
		problemSpeedThread.start();*/

		problemSpeedCalculator = new ProblemSpeedCalculator(context);

		collisionInteraction = new CollisionInteraction(context);
		nearMissInteraction = new NearMissInteraction(context);
		gameEventInteraction = new EventInteraction(context);


		suppliers = new ArrayList<>();

		suppliers.add(context.modelHolder.getMeshedCube());
		suppliers.add(context.getTerrain());
		suppliers.add(context.getProblem());
		suppliers.add(context.getGameEvent());
		suppliers.add(context.getVehicle());


		renderActions = new LinkedList<>();
		renderActions.add(problemSpeedCalculator);

		renderActions.add(context.getTiledFloor());
		renderActions.add(context.getVehicle());
		renderActions.add(context.getProblem());
		renderActions.add(context.getGround());
		renderActions.add(context.getGameEvent());
		renderActions.add(context.modelHolder.getMeshedCube());
		renderActions.add(directionalLightSupplier);
		renderActions.add(cameraSupplier);
		renderActions.add(collisionInteraction);
		renderActions.add(nearMissInteraction);
		renderActions.add(gameEventInteraction);
		renderActions.add(context.getTerrain());

		instances = new ArrayList<>();
		suppliers.forEach(supplier -> instances.addAll(supplier.getModels()));

		Gdx.input.setInputProcessor(new InputMultiplexer(TOUCH_ADAPTER));

		//CameraInputController camController = new CameraInputController(cameraSupplier.getCamera());
		//Gdx.input.setInputProcessor(camController);

		//Gdx.input.setInputProcessor(new SwipeGestureDetector(new SwipeDirectionListener()));

		shadowBatch = new ModelBatch(new DepthShaderProvider());

		/*staticCache = new ModelCache();
		staticCache.begin();
		staticCache.add(context.getGround().getModels());
		staticCache.end();*/
		renderActionsHandler = new Thread(() -> {
			while (true) {
				long cur = System.currentTimeMillis();
				renderActions.forEach(RenderAction::render);
				//Gdx.app.log("Actions cycle", "Pure time: " + (System.currentTimeMillis() - cur));
				synchronized (lock) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				//Gdx.app.log("Actions cycle", "With lock: " + (System.currentTimeMillis() - cur));
			}
		});
		renderActionsHandler.start();

		vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
		vfxEffect = new GaussianBlurEffect();
		vfxEffect.setType(GaussianBlurEffect.BlurType.Gaussian5x5b);
		vfxManager.addEffect(vfxEffect);
	}

	@Override
	public void render () {
		//renderActions.forEach(RenderAction::render);

		long cur = System.currentTimeMillis();
		synchronized (lock) {
			lock.notify();
		}
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		/*shadowLight.begin(Vector3.Zero, cameraSupplier.getCamera().direction);
		shadowBatch.begin(shadowLight.getCamera());

		shadowBatch.render(context.getGround().getModels());
		shadowBatch.end();
		shadowLight.end();*/



		modelCache.begin();
		//modelCache.add(context.getTiledFloor().getModels());
		//modelCache.add(context.modelHolder.getMeshedCube().getModels());
		//modelCache.add(context.getProblem().getModels());
		//modelCache.add(context.getGameEvent().getModels());
		//modelCache.add(context.getVehicle().getModels());
		modelCache.end();


		/*floorModelBatch.begin(cameraSupplier.getCamera());
		floorModelBatch.render(modelCache, environment);
		floorModelBatch.end();*/

		vfxManager.cleanUpBuffers();
		vfxManager.beginInputCapture();


		modelBatch.begin(cameraSupplier.getCamera());

		//modelBatch.render(staticCache, environment);
		//modelBatch.render(modelCache, environment);
		modelBatch.render(instances, environment);
		modelBatch.end();
		vfxManager.endInputCapture();
		vfxManager.applyEffects();
		vfxManager.renderToScreen();
		statsOverlay.render();

		//Gdx.app.log("Graphics render", "Time: " + (System.currentTimeMillis() - cur));
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
		modelCache.dispose();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

}
