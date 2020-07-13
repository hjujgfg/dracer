package org.hjujgfg.dracer;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalShadowLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import org.hjujgfg.dracer.world.Floor;
import org.hjujgfg.dracer.world.ModelSupplier;
import org.hjujgfg.dracer.world.RenderAction;
import org.hjujgfg.dracer.world.World;
import org.hjujgfg.dracer.world.params.ProblemSpeed;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;


public class DracerGame extends InputAdapter implements ApplicationListener {

	SpriteBatch batch;
	Texture img;

	public PerspectiveCamera cam;
	final float cameraX = 7f, cameraY = -5f, cameraZ = 0f;
	final float lookX = 0, lookY = 5, lookZ = 0;

	final float[] startPos = {7f, -6f, 0f};

	public Environment environment;

	Model model;
	ModelInstance instance;
	ModelInstance problemInstance;

	List<ModelInstance> instances;
	ModelBatch modelBatch;

	final float speed = 2f;

	protected Label label;
	protected BitmapFont font;
	protected Stage stage;

	protected long startTime;
	protected long hits;

	boolean shouldRotate = false;
	boolean shouldMoveLeft, shouldMoveRight;
	Instant prevLeftPressed, lastLeftPressed, prevRightPressed, lastRightPressed;
	boolean inBarrelRoll;
	int barrelRolCounter = 0;
	int barrelRollDirection;
	float rotation = 0;

	Random r = new Random();


	float accelX, accelY, accelZ;

	Set<Integer> appliedPointers = new HashSet<>();

	DirectionalShadowLight directionalShadowLight;
	DirectionalLight directionalLight;


	Floor floor;
	Collection<ModelSupplier> suppliers = new ArrayList<>();
	List<RenderAction> renderActions = new LinkedList<>();

	World world;

	@Override
	public void create () {
		modelBatch = new ModelBatch();
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.up.set(0, 1, 0);
		cam.position.set(startPos[0], startPos[1], startPos[2]);
		cam.lookAt(0, 0, 0);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		Gdx.gl.glClearColor(135/255f, 206/255f, 235/255f, 1);
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight,
				0.4f, 0.4f, 0.4f, 1f));
		/*directionalShadowLight = new DirectionalShadowLight(
				1024, 1024, 60f,
				60f, .1f, 50f);*/
		directionalLight = new DirectionalLight()
				.set(0.8f, 0.8f, 0.8f,
				-6f, 5f, 5f);
		environment.add(directionalLight);
		/*environment.add(directionalShadowLight
				.set(1f, 1f, 1f, -10.0f, 5f, 0f));*/
		//environment.shadowMap = directionalShadowLight;

		ModelBuilder builder = new ModelBuilder();
		ModelLoader loader = new ObjLoader();
		model = loader.loadModel(Gdx.files.internal("ship.obj"));

		//Model houseModel = loader.loadModel(Gdx.files.internal("house/Futuristic ghetto building_one_Mesh_obj.obj"));
		Model coordinatesModel = builder.createXYZCoordinates(10,
				new Material(ColorAttribute.createDiffuse(Color.BLACK)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		ModelInstance coordsInstance = new ModelInstance(coordinatesModel);


		Model houseModel = builder.createBox(10, 50, 20,
				new Material(ColorAttribute.createDiffuse(Color.FIREBRICK),
						ColorAttribute.createReflection(Color.RED),
						ColorAttribute.createSpecular(Color.WHITE),
						FloatAttribute.createShininess(64f)),
				VertexAttributes.Usage.Position);

		instance = new ModelInstance(model);

		// instance.transform.setToRotation(Vector3.Z, 120);
		instance.transform
				.setToRotation(0, 0, 1, -90)
				.rotate(0, 1, 0, -90)
				.translate(0, 2, 0);

		Model arrow1 = builder.createArrow(0, 0, 0, 10, 0, 0,
				0.2f, 0.02f, 10, GL20.GL_TRIANGLES,
				new Material(ColorAttribute.createDiffuse(Color.CYAN)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		Model arrow2 = builder.createArrow(0, 0, 0, 0, 10, 0,
				0.2f, 0.02f, 10, GL20.GL_TRIANGLES,
				new Material(ColorAttribute.createDiffuse(Color.RED)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		Model arrow3 = builder.createArrow(0, 0, 0, 0, 0, 10,
				0.2f, 0.02f, 10, GL20.GL_TRIANGLES,
				new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

		ModelInstance arrowX = new ModelInstance(arrow1);
		ModelInstance arrowY = new ModelInstance(arrow2);
		ModelInstance arrowZ = new ModelInstance(arrow3);

		Model problem = builder.createBox(2, 2, 2,
				new Material(ColorAttribute.createDiffuse(Color.CHARTREUSE)),
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);
		problemInstance = new ModelInstance(problem);
		problemInstance.transform.setToTranslation(r.nextFloat(), 15, r.nextFloat());

		instances = new ArrayList<>();
		instances.add(instance);

		floor = new Floor();
		suppliers.add(floor);
		renderActions.add(floor);
		/*world = new World();
		suppliers.add(world);*/

		suppliers.forEach(supplier -> instances.addAll(supplier.getModels()));


		//instances.add(arrowX);
		//instances.add(arrowY);
		//instances.add(arrowZ);

		instances.add(problemInstance);

		//instances.add(coordsInstance);


		font = new BitmapFont();
		label = new Label(" ", new Label.LabelStyle(font, Color.WHITE));

		stage = new Stage();
		stage.addActor(label);

		startTime = System.currentTimeMillis();

		accelX = Gdx.input.getAccelerometerX();
		accelY = Gdx.input.getAccelerometerY();
		accelZ = Gdx.input.getAccelerometerZ();

		Gdx.input.setInputProcessor(new InputMultiplexer(this));
		//CameraInputController camController = new CameraInputController(cam);
		//Gdx.input.setInputProcessor(camController);
	}

	@Override
	public void render () {
		//directionalShadowLight.begin();

		moveProblem();
		moveVehicleTouch();
		moveCamera();
		checkCollision();
		moveDirectionalLight();
		renderActions.forEach(RenderAction::render);

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		modelBatch.begin(cam);
		modelBatch.render(instances, environment);
		modelBatch.end();
		//directionalShadowLight.end();


		StringBuilder builder = new StringBuilder();
		builder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
		long time = System.currentTimeMillis() - startTime;
		builder.append("| Game time: ").append(time);
		builder.append("| Hits: ").append(hits);
		builder.append("| Rating: ").append((float) hits/(float) time);
		label.setText(builder);
		stage.draw();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		model.dispose();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Gdx.app.log("INFO", String.format("Touch down happened %d:%d with pointer %d, btn: %d",
				screenX, screenY, pointer, button));
		if (Gdx.graphics.getWidth() / 2 < screenX) {
			prevRightPressed = lastRightPressed;
			lastRightPressed = Instant.now();
			shouldMoveRight = true;
		} else {
			prevLeftPressed = lastLeftPressed;
			lastLeftPressed = Instant.now();
			shouldMoveLeft = true;
		}
		appliedPointers.add(pointer);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		Gdx.app.log("INFO", String.format("Touch up happened %d:%d with pointer %d, btn: %d",
				screenX, screenY, pointer, button));

		shouldMoveLeft = false;
		shouldMoveRight = false;
		shouldRotate = false;
		appliedPointers.remove(pointer);
		return false;
	}


	private float getSpeed() {
		return speed * Math.signum((float) Math.random() - 0.5f) * Math.max((float) Math.random(), 0.5f);
	}

	private void moveVehicleAccel() {
		float accelXCur = Gdx.input.getAccelerometerX();
		float accelYCur = Gdx.input.getAccelerometerY();
		float accelZCur = Gdx.input.getAccelerometerZ();

		float dx = accelX - accelXCur;
		float dy = accelY - accelYCur;
		float dz = accelZ - accelZCur;

		accelX = accelXCur;
		accelY = accelYCur;
		accelZ = accelZCur;

		instance.transform.translate(dx * 0.5f, dy * 0.5f, dz * 0.5f);

	}

	private void moveVehicleTouch() {
		if (inBarrelRoll) {
			int part = barrelRolCounter / 6;
			float mult = 0.0f;
			if (part == 1) {
				mult = 0.1f;
			} else if (part == 2) {
				mult = 0.2f;
			} else {
				mult = - 0.3f;
			}
			instance.transform
					.rotate(0, 0, 1, barrelRollDirection * 20f)
					.trn(mult, 0, - barrelRollDirection * 0.3f * Math.max(PROBLEM_SPEED.get(), 1));
			barrelRolCounter --;
			/*Gdx.app.log("BARREL", String.format("Counter is %d",
					barrelRolCounter));*/
			if (barrelRolCounter == 1) {
				inBarrelRoll = false;
			}
		} else if (appliedPointers.size() > 1) {
			//
		} else {
			if (shouldMoveLeft) {
				if (prevLeftPressed != null) {
					long millisPassed = lastLeftPressed.toEpochMilli() - prevLeftPressed.toEpochMilli();
					if (millisPassed < 300) {
						inBarrelRoll = true;
						barrelRollDirection = -1;
						barrelRolCounter = 18;
					}
				}
				if (!inBarrelRoll) {
					instance.transform
							.rotate(0, 0, 1, -0.8f)
							.trn(0, 0, 0.2f * Math.max(PROBLEM_SPEED.get(), 1));
				}
			}
			if (shouldMoveRight) {
				if (prevRightPressed != null) {
					long millisPassed = lastRightPressed.toEpochMilli() - prevRightPressed.toEpochMilli();
					if (millisPassed < 300) {
						inBarrelRoll = true;
						barrelRollDirection = 1;
						barrelRolCounter = 18;
					}
				}
				instance.transform
						.rotate(0, 0, 1, 0.8f)
						.trn(0, 0, -0.2f * Math.max(PROBLEM_SPEED.get(), 1));
			}
		}

		if (!inBarrelRoll && !shouldMoveRight && !shouldMoveLeft) {
			stabilize(instance);
		}
	}

	int lightDirection = 1;
	private void moveDirectionalLight() {
		if (appliedPointers.size() > 1) {
			directionalLight.color.set(Color.RED);
		} else {
			directionalLight.color.set(Color.WHITE);
		}
		/*if (bigger(10, directionalLight.direction.x)) {
			directionalLight.direction.add(-0.5f, 0f, 0);
		} else {
			directionalLight.direction.add(0.5f, 0f, 0);
		}*/
		Vector3 direction = directionalLight.direction;
		/*Gdx.app.log("TRANSLATION", String.format("Light direction %f %f %f ",
				direction.x, direction.y, direction.z));*/

		if (bigger(-10, direction.x)) {
			lightDirection = 1;
		}
		if (bigger(direction.x, -5)) {
			lightDirection = -1;
		}
		direction.add( lightDirection * PROBLEM_SPEED.get() * 0.5f, 0, 0);
	}

	private void moveCamera() {
		if (appliedPointers.size() > 1) {
			Vector3 pos = cam.position;
			if (pos.x > 1.5) {
				cam.position.set(pos.x - 0.1f, pos.y - 0.1f, pos.z);
			}
			cam.lookAt(instance.transform.getTranslation(new Vector3()));
		} else {
			Vector3 pos = cam.position;
			if (bigger(cameraY, pos.y)) {
				cam.position.set(pos.x, pos.y + 0.1f, pos.z);
			} else if (bigger(pos.y, cameraY)) {
				cam.position.set(pos.x, pos.y - 0.1f, pos.z);
			}
			if (bigger(cameraX, pos.x)) {
				cam.position.set(pos.x + 0.1f, pos.y, pos.z);
			} else if (bigger(pos.x, cameraX)) {
				cam.position.set(pos.x - 0.1f, pos.y, pos.z);
			}

			if (bigger(cameraZ, pos.z)) {
				cam.position.set(pos.x, pos.y, pos.z + 0.1f);
			} else if (bigger(pos.z, cameraZ)) {
				cam.position.set(pos.x, pos.y, pos.z - 0.1f);
			}
			cam.lookAt(lookX, lookY, lookZ);
			cam.up.set(0, 1, 0);
		}

		//cam.normalizeUp();
		cam.update();
	}

	private void moveProblem() {
		if (appliedPointers.size() > 1 || PROBLEM_SPEED.isSlow()) {
			PROBLEM_SPEED.set(Math.min(PROBLEM_SPEED.get() + 0.1f, 2));
		} else if (PROBLEM_SPEED.isFast()) {
			PROBLEM_SPEED.change(-0.1f);
		}
		Vector3 position;
		position = problemInstance.transform.getTranslation(new Vector3());
		if (bigger(position.y, -5)) {
			problemInstance.transform.translate(0, - PROBLEM_SPEED.get(), 0);
		} else {
			PROBLEM_SPEED.changeMinimal(0.01f);
			PROBLEM_SPEED.change(0.01f);
			problemInstance.transform.setTranslation(
					5 * r.nextFloat(),
					20 + r.nextInt(10),
					r.nextFloat() * 5f - 2.5f);
		}
	}

	private void checkCollision() {
		Vector3 problemPosition = problemInstance.transform.getTranslation(new Vector3());
		Vector3 instancePosition = instance.transform.getTranslation(new Vector3());
		float dst = problemPosition.dst(instancePosition);
		if (dst < 2) {
			if (appliedPointers.size() > 1) {
				problemInstance.transform.setTranslation(
						5 * r.nextFloat(),
						20 + r.nextInt(10),
						r.nextFloat() * 5f - 2.5f);
			} else {
				PROBLEM_SPEED.setMinimal(Math.max(PROBLEM_SPEED.getMinimal() - 0.1f, 0.01f));
				PROBLEM_SPEED.set(-1f * Math.max(1, PROBLEM_SPEED.get()));
			}
		}
	}

	int fluctCounter = 10;
	float fluct = 0;
	private void stabilize(ModelInstance instance) {
		Quaternion rotation = instance.transform.getRotation(new Quaternion());
		float angleAround = rotation.getAngleAround(0, 0, 1);
		if (bigger(angleAround, -90)) {
			instance.transform.rotate(0, 0, 1, (-90 - angleAround));
		} else if (bigger(-90, angleAround)) {
			instance.transform.rotate(0, 0, 1, (-90 + angleAround));
		}
		Vector3 translation = instance.transform.getTranslation(new Vector3());
		/*Gdx.app.log("TRANSLATION", String.format("Instance translation %f %f %f ",
				translation.x, translation.y, translation.z));*/
		if (fluctCounter-- == 0) {
			fluct = 2 + r.nextFloat() - 0.5f;
			fluctCounter = 50;
		}
		if (bigger(translation.x, fluct)) {
			instance.transform.trn( -0.01f, 0, 0);
		} else if (bigger(fluct, translation.x)) {
			instance.transform.trn(0.01f, 0, 0);
		}
	}
}
