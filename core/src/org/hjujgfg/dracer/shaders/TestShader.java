package org.hjujgfg.dracer.shaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;

import org.hjujgfg.dracer.world.GameContext;

public class TestShader implements Shader {
    private final Vector3 tmp = new Vector3();

    GameContext gameContext;

    ShaderProgram program;
    Camera camera;
    RenderContext context;

    int u_projTrans;
    int u_worldTrans;
    int u_color;

    int lightPosition;
    int v_lightIntensity;

    float r, g, b;

    public TestShader(GameContext gameContext) {
        this.gameContext = gameContext;
    }

    @Override
    public void init() {
        String vert = Gdx.files.internal("shaders/test.vertex.glsl").readString();
        String frag = Gdx.files.internal("shaders/test.fragment.glsl").readString();
        program = new ShaderProgram(vert, frag);
        if (!program.isCompiled())
            throw new GdxRuntimeException(program.getLog());
        u_projTrans = program.getUniformLocation("u_projTrans");
        u_worldTrans = program.getUniformLocation("u_worldTrans");
        u_color = program.getUniformLocation("u_color");
        lightPosition = program.getUniformLocation("lightPosition");
        r = MathUtils.random();
        g = MathUtils.random();
        b = MathUtils.random();
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        return true;
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        this.camera = camera;
        this.context = context;
        program.begin();
        program.setUniformMatrix(u_projTrans, camera.combined);
        this.context.setDepthTest(GL20.GL_LEQUAL);
        this.context.setCullFace(GL20.GL_BACK);
    }

    @Override
    public void render(Renderable renderable) {
        program.setUniformMatrix(u_worldTrans, renderable.worldTransform);
        r = r + MathUtils.random(0.01f) * MathUtils.randomSign();
        g = g + MathUtils.random(0.01f) * MathUtils.randomSign();
        b = b + MathUtils.random(0.01f) * MathUtils.randomSign();
        program.setUniformf(u_color, r, g, b);
        gameContext.getVehicle().getTransform().getTranslation(tmp);
        program.setUniformf(lightPosition, tmp.x, tmp.y, tmp.z);
        //program.setUniformi("u_texture", 0);
        renderable.meshPart.render(program);
    }

    @Override
    public void end() {
        program.end();
    }

    @Override
    public void dispose() {
        program.dispose();
    }
}
