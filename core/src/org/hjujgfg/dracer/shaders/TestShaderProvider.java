package org.hjujgfg.dracer.shaders;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;

import org.hjujgfg.dracer.world.GameContext;

public class TestShaderProvider extends DefaultShaderProvider {

    TestShader shader;
    GameContext context;

    public TestShaderProvider(DefaultShader.Config config, GameContext context) {
        super(config);
        shader = new TestShader(context);
        shader.init();
    }

    @Override
    protected Shader createShader (final Renderable renderable) {
        //return new TestShader(renderable, config);
        //renderable.shader = shader;
        return shader;
    }
}
