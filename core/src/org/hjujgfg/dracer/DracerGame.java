package org.hjujgfg.dracer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelCache;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.utils.DefaultTextureBinder;
import com.badlogic.gdx.graphics.g3d.utils.DepthShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.bitfire.postprocessing.PostProcessor;
import com.bitfire.postprocessing.effects.Bloom;
import com.bitfire.postprocessing.effects.CrtMonitor;
import com.bitfire.postprocessing.effects.Curvature;
import com.bitfire.postprocessing.effects.Fxaa;
import com.bitfire.postprocessing.effects.LensFlare2;
import com.bitfire.postprocessing.effects.MotionBlur;
import com.bitfire.postprocessing.effects.Nfaa;
import com.bitfire.postprocessing.effects.Zoomer;
import com.bitfire.postprocessing.filters.CrtScreen;
import com.bitfire.postprocessing.filters.RadialBlur;
import com.bitfire.utils.ShaderLoader;
import com.crashinvaders.vfx.VfxManager;
import com.crashinvaders.vfx.effects.ChainVfxEffect;
import com.crashinvaders.vfx.effects.ChromaticAberrationEffect;
import com.crashinvaders.vfx.effects.RadialBlurEffect;

import org.hjujgfg.dracer.events.CollisionEventReader;
import org.hjujgfg.dracer.events.GameEventCollisionEventReader;
import org.hjujgfg.dracer.events.ProblemPassedEventReader;
import org.hjujgfg.dracer.gameplay.GameContext;
import org.hjujgfg.dracer.input.ContinuousTouchAdapter;
import org.hjujgfg.dracer.shaders.TestShader;
import org.hjujgfg.dracer.world.autopilot.Autopilot;
import org.hjujgfg.dracer.world.camera.PerspectiveCameraSupplier;
import org.hjujgfg.dracer.world.control.ContinuousTouchHandler;
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

import static org.hjujgfg.dracer.gameplay.states.GameMode.BLURRED;
import static org.hjujgfg.dracer.gameplay.states.GameMode.LONG_BLURRED;


public class DracerGame extends InputAdapter implements ApplicationListener {

	protected Stage stage;

	public Environment environment;

	List<ModelInstance> instances;

	ModelBatch modelBatch;
	List<ModelInstance> modelInstances;
	ModelBatch floorModelBatch;
	List<ModelInstance> floorModelInstances;
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
	private ChainVfxEffect vfxEffect;
	private ChainVfxEffect vfxEffect2;

	private FrameBuffer fb, sfb;

	private GLProfiler profiler;

	PostProcessor processor;
	PostProcessor sunProcessor;
	Bloom bloom;
	MotionBlur motionBlur;
	CrtMonitor crtMonitor;


	@Override
	public void create () {
		context = new GameContext();
		renderContext = new RenderContext(new DefaultTextureBinder(DefaultTextureBinder.WEIGHTED, 1));
		renderContext.setCullFace(GL20.GL_CULL_FACE);
		//shader = new TestShader();
		//shader.init();
		//String vert = Gdx.files.internal("shaders/test.vertex.glsl").readString();
		//String frag = Gdx.files.internal("shaders/test.fragment.glsl").readString();

		modelBatch = new ModelBatch();
		floorModelBatch = new ModelBatch();
		modelCache = new ModelCache();
		Gdx.gl.glClearColor(9/255f, 36/255f, 51/255f, 0.3f);
		environment = new Environment();

		environment.set(new ColorAttribute(ColorAttribute.AmbientLight,
				0.7f, 0.7f, 0.7f, 1f)
		);

		//environment.set(new ColorAttribute(ColorAttribute.Fog, Color.FOREST));


		/*environment.add((shadowLight = new DirectionalShadowLight(
				1024,
				1024,
				30f,
				30f,
				1f, 100f))
				.set(0.8f, 0.8f, 0.8f, -1f, -.8f, -.2f));*/

		directionalLightSupplier = new DirectionalLightSupplier(context);
		directionalLightSupplier.getLights().forEach(environment::add);
		cameraSupplier = new PerspectiveCameraSupplier(context);
		//environment.add(context.modelHolder.getSun().getLight());
		PointLight pl = new PointLight();
		pl.set(59, 255, 245,
				10, 80, 0, 900);

		environment.add(pl);

		stage = new Stage();
		statsOverlay = new StatsOverlay(stage, context);

		setUpEvents();

		fillModelSuppliers();

		fillRenderActions();

		instances = new ArrayList<>();
		suppliers.forEach(supplier -> instances.addAll(supplier.getModels()));

		modelInstances = new ArrayList<>();
		modelInstances.addAll(context.getProblem().getModels());
		modelInstances.addAll(context.getGameEvent().getModels());
		modelInstances.addAll(context.getVehicle().getModels());

		floorModelInstances = new ArrayList<>();
		floorModelInstances.addAll(context.modelHolder.getSun().getModels());
		floorModelInstances.addAll(context.getTerrain().getModels());
		floorModelInstances.addAll(context.modelHolder.getMeshedCube().getModels());



		//Gdx.input.setInputProcessor(new InputMultiplexer(TOUCH_ADAPTER));
		ContinuousTouchHandler continuousTouchHandler = new ContinuousTouchHandler(context);
		Gdx.input.setInputProcessor(new InputMultiplexer(new ContinuousTouchAdapter(
				continuousTouchHandler::handleDown,
				continuousTouchHandler::handleDrag,
				continuousTouchHandler::handleUp
		)));

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
		//renderActionsHandler.start();

		//setUpVfx();
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		profiler = new GLProfiler(Gdx.graphics);
		profiler.enable();

		ShaderLoader.BasePath = "shaders/";
		processor = new PostProcessor( true, true, false);
		processor.setClearDepth(900);
		sunProcessor = new PostProcessor( true, true, false);
		bloom = new Bloom( (int)(Gdx.graphics.getWidth() * 0.1f), (int)(Gdx.graphics.getHeight() * 0.1f) );
		sunProcessor.addEffect(bloom);
		motionBlur = new MotionBlur();
		motionBlur.setBlurOpacity(0.5f);
		//bloom.setBlurPasses(3);

		processor.addEffect(new CrtMonitor(
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight(),
				false,
				true,
				CrtScreen.RgbMode.RgbShift,
				CrtScreen.Effect.Scanlines.v | CrtScreen.Effect.PhosphorVibrance.v
		));
		bloom.setBloomIntesity(0.1f);
		//processor.addEffect(bloom);
		processor.addEffect(motionBlur);

		//processor.addEffect(new Zoomer(3, 3, RadialBlur.Quality.High));

		fb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		sfb = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
	}

	@Override
	public void render () {
		//renderActions.forEach(RenderAction::render);

		long cur = System.currentTimeMillis();
		long cur2 = cur;
		synchronized (lock) {
			lock.notify();
		}
		renderActions.forEach(RenderAction::render);
		//cur2 = logTime("notify: ", cur2);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT
				| GL20.GL_DEPTH_BUFFER_BIT
		);
		//cur2 = logTime("clear: ", cur2);
		/*shadowLight.begin(Vector3.Zero, cameraSupplier.getCamera().direction);
		shadowBatch.begin(shadowLight.getCamera());

		shadowBatch.render(context.getGround().getModels());
		shadowBatch.end();
		shadowLight.end();*/



		/*modelCache.begin();
		//modelCache.add(context.getTiledFloor().getModels());
		//modelCache.add(context.modelHolder.getMeshedCube().getModels());
		modelCache.add(context.getProblem().getModels());
		modelCache.add(context.getGameEvent().getModels());
		modelCache.add(context.getVehicle().getModels());
		modelCache.end();*/


		/*floorModelBatch.begin(cameraSupplier.getCamera());
		floorModelBatch.render(modelCache, environment);
		floorModelBatch.end();*/

		//vfxManager.cleanUpBuffers();
		//vfxManager.removeAllEffects();
		//vfxManager.beginInputCapture();
		//cur2 = logTime("vfx clear: ", cur2);
		processor.capture();
		floorModelBatch.begin(cameraSupplier.getCamera());
		floorModelBatch.render(context.modelHolder.getSun().getModels());
		floorModelBatch.end();
		//vfxManager.endInputCapture();
		//vfxManager.applyEffects();
		//vfxManager.renderToScreen();
		//cur2 = logTime("sun + vfx: ", cur2);
		//vfxManager.removeAllEffects();
		//vfxManager.addEffect(vfxEffect2);
		//vfxManager.beginInputCapture();

		floorModelBatch.begin(cameraSupplier.getCamera());
		floorModelBatch.render(context.getTerrain().getModels(), environment);
		floorModelBatch.render(context.modelHolder.getMeshedCube().getModels(), environment);

		//floorModelBatch.render(context.getFloor().getModels(), environment);
		//floorModelBatch.render(floorModelInstances, environment);
		floorModelBatch.end();

		modelBatch.begin(cameraSupplier.getCamera());
		modelBatch.render(context.getProblem().getModels(), environment);
		//modelBatch.render(context.getGameEvent().getModels(), environment);
		modelBatch.render(context.getVehicle().getModels(), environment);
		//modelBatch.render(modelInstances, environment);

		//modelBatch.render(context.modelHolder.getWhale().getModels(), environment);
		modelBatch.end();

		//FrameBuffer result = processor.captureEnd();
		//fb.begin();
		modelBatch.render(context.modelHolder.getSun().getAtmosphere(), environment);
		//fb.end();


		//bloom.setBlurAmount(1000);
		//bloom.enableBlending(1, 10);
		//bloom.render(fb, result);
		processor.render();

		//cur2 = logTime("main: ", cur2);

		//vfxManager.endInputCapture();
		//if (context.getGameMode() == BLURRED || context.getGameMode() == LONG_BLURRED || context.isInUlt()) {
		//	vfxManager.addEffect(vfxEffect2);
		//	vfxManager.applyEffects();
		//}
		//vfxManager.renderToScreen();
		//vfxManager.removeEffect(vfxEffect2);
		statsOverlay.render();
		//cur2 = logTime("vfx main: ", cur2);

		/*Gdx.app.log("PROFILER",
				"  Drawcalls: " + profiler.getDrawCalls() +
						", Calls: " + profiler.getCalls() +
						", TextureBindings: " + profiler.getTextureBindings() +
						", ShaderSwitches:  " + profiler.getShaderSwitches() +
						", vertexCount: " + profiler.getVertexCount().average +
						", FPS:" + Gdx.graphics.getFramesPerSecond()
		);*/
		profiler.reset();



		//Gdx.app.log("Graphics render", "Time: " + (System.currentTimeMillis() - cur));
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
		processor.rebind();
	}

	@Override
	public void dispose () {
		stage.dispose();
		suppliers.forEach(ModelSupplier::dispose);
		modelCache.dispose();
		processor.dispose();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	private void fillModelSuppliers() {
		suppliers = new ArrayList<>();

		suppliers.add(context.modelHolder.getMeshedCube());
		suppliers.add(context.getTerrain());
		suppliers.add(context.modelHolder.getSun());
		//suppliers.add(context.getProblem());
		//suppliers.add(context.getGameEvent());
		//suppliers.add(context.getVehicle());
	}

	private void fillRenderActions() {
		renderActions = new LinkedList<>();
		renderActions.add(problemSpeedCalculator);
		renderActions.add(context.getVehicle());
		renderActions.add(context.getProblem());
		renderActions.add(context.getGround());
		//renderActions.add(context.getGameEvent());
		renderActions.add(context.modelHolder.getMeshedCube());
		renderActions.add(context.getFloor());
		renderActions.add(directionalLightSupplier);
		renderActions.add(cameraSupplier);
		renderActions.add(collisionInteraction);
		renderActions.add(nearMissInteraction);
		renderActions.add(gameEventInteraction);
		renderActions.add(context.getTerrain());
		renderActions.add(context.modelHolder.getSun());
		//renderActions.add(new Autopilot(context));
	}

	private void setUpEvents() {
		problemSpeedCalculator = new ProblemSpeedCalculator(context);
		collisionInteraction = new CollisionInteraction(context);
		nearMissInteraction = new NearMissInteraction(context);
		gameEventInteraction = new EventInteraction(context);

		CollisionEventReader collisionEventReader = new CollisionEventReader(statsOverlay, context);
		collisionEventReader.start();
		ProblemPassedEventReader problemPassedEventReader = new ProblemPassedEventReader(context);
		problemPassedEventReader.start();
		GameEventCollisionEventReader gameEventCollisionEventReader = new GameEventCollisionEventReader(context);
		gameEventCollisionEventReader.start();
	}

	private void setUpVfx() {
		vfxManager = new VfxManager(Pixmap.Format.RGBA8888);
		vfxEffect = new RadialBlurEffect(12);
		vfxEffect2 = new ChromaticAberrationEffect(3);
		//vfxManager.addEffect(vfxEffect);
		//vfxManager.addEffect(new CrtEffect(CrtEffect.LineStyle.CROSSLINE_HARD, 0.5f, 1f));
		//vfxManager.addEffect(new ChromaticAberrationEffect(3));
		//vfxManager.addEffect(new MotionBlurEffect(Pixmap.Format.RGBA8888, MixEffect.Method.MIX, 0.5f));
		//vfxManager.addEffect(new RadialBlurEffect(12));
		vfxManager.addEffect(vfxEffect);
	}

	private long logTime(String msg, long prev) {
		long cur = System.currentTimeMillis();
		Gdx.app.log("Graphics render", msg + (cur - prev));
		return cur;
	}
}
