package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

public class Materials {

    public static Material createSilver() {
        return new Material(
                ColorAttribute.createAmbient(0.19225f, 0.19225f, 0.19225f, 1),
                ColorAttribute.createDiffuse(0.50754f, 0.50754f, 0.50754f, 1),
                ColorAttribute.createSpecular(0.508273f, 0.508273f, 0.508273f, 1),
                FloatAttribute.createShininess(51.2f)
        );
    }

    public static Material createNeonSilver() {
        return new Material(
                ColorAttribute.createAmbient(117 / 255.f, 21 / 255.f, 104 / 255.f, 1),
                ColorAttribute.createDiffuse(199.f / 255.f,36.f / 255.f,177.f / 255.f, 1),
                ColorAttribute.createSpecular(255 / 255.f, 181 / 255.f, 245 / 255.f, 1),
                //ColorAttribute.createReflection(Color.FOREST),
                FloatAttribute.createShininess(12100000f)
        );
    }

    public static Material createSlightlyColoredSilver() {
        return new Material(
                ColorAttribute.createAmbient(0.19225f, 0.19225f, 0.19225f, 1),
                ColorAttribute.createDiffuse(0.50754f, 0.60754f, 0.60754f, 1),
                ColorAttribute.createSpecular(0.508273f, 0.508273f, 0.508273f, 1),
                FloatAttribute.createShininess(51.2f)
        );
    }

    public static Material createExtraSilver() {
        return new Material(
                ColorAttribute.createAmbient(0.89225f, 0.89225f, 0.89225f, 1),
                ColorAttribute.createDiffuse(0.90754f, 0.90754f, 0.90754f, 1),
                ColorAttribute.createSpecular(0.808273f, 0.808273f, 0.808273f, 1),
                FloatAttribute.createShininess(120)
        );
    }

    public static Material createPolishedSilver() {
        return new Material(
                ColorAttribute.createAmbient(0.23125f, 0.23125f, 0.23125f, 1),
                ColorAttribute.createDiffuse(0.2775f, 0.2775f, 0.2775f, 1),
                ColorAttribute.createSpecular(0.773911f, 0.773911f, 0.773911f, 1),
                FloatAttribute.createShininess(89.6f)
        );
    }

    public static Material createEmerald() {
        return new Material(
                ColorAttribute.createAmbient(0.0215f, 0.1745f, 0.0215f, 0.55f),
                ColorAttribute.createDiffuse(0.07568f, 0.61424f, 0.07568f, 0.55f),
                ColorAttribute.createSpecular(0.633f, 0.727811f, 0.633f, 0.55f),
                FloatAttribute.createShininess(76.8f)
        );
    }

    public static Material createSun() {
        Texture texture = new Texture(Gdx.files.internal("neutron_inverted_low_brightness.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        //texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        //texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return new Material(
                //ColorAttribute.createAmbient(0.4f, 1f, 1f, 1),
                //ColorAttribute.createDiffuse(0.3f, 1f, 1f, 1),
                //ColorAttribute.createSpecular(0.5f, 1f, 1f, 1),
                ColorAttribute.createDiffuse(1f, 1f, 1f, 1),
                ColorAttribute.createAmbient(1f, 1f, 1f, 1),
                FloatAttribute.createShininess(151.2f),
                TextureAttribute.createAmbient(texture),
                TextureAttribute.createDiffuse(texture),
                TextureAttribute.createSpecular(texture),
                TextureAttribute.createBump(texture),
                TextureAttribute.createEmissive(texture),
                new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        );
    }

    public static Material createClouds() {
        Texture texture = new Texture(Gdx.files.internal("clouds.png"));
        TextureRegion textureRegion = new TextureRegion(texture);
        //texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        //texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return new Material(
                //ColorAttribute.createAmbient(0.4f, 1f, 1f, 1),
                //ColorAttribute.createDiffuse(0.3f, 1f, 1f, 1),
                //ColorAttribute.createSpecular(0.5f, 1f, 1f, 1),
                ColorAttribute.createDiffuse(1f, 1f, 1f, 1),
                ColorAttribute.createAmbient(1f, 1f, 1f, 1),
                FloatAttribute.createShininess(151.2f),
                TextureAttribute.createAmbient(texture),
                TextureAttribute.createDiffuse(texture),
                TextureAttribute.createSpecular(texture),
                TextureAttribute.createBump(texture),
                TextureAttribute.createEmissive(texture),
                new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        );
    }

    public static Material createChip() {
        Texture texture = new Texture(Gdx.files.internal("chip1.png"));
        texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion textureRegion = new TextureRegion(texture);
        //texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        //texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return new Material(
                //ColorAttribute.createAmbient(0.4f, 1f, 1f, 1),
                //ColorAttribute.createDiffuse(0.3f, 1f, 1f, 1),
                //ColorAttribute.createSpecular(0.5f, 1f, 1f, 1),
                //ColorAttribute.createDiffuse(1f, 1f, 1f, 1),
                //ColorAttribute.createAmbient(1f, 1f, 1f, 1),
                //FloatAttribute.createShininess(10f),
                TextureAttribute.createAmbient(texture),
                TextureAttribute.createDiffuse(texture),
                TextureAttribute.createSpecular(texture),
                TextureAttribute.createBump(texture),
                TextureAttribute.createEmissive(texture)
        );
    }

    public static Material createNeonGrid() {
        Texture texture = new Texture(Gdx.files.internal("neon_grid1.png"));
        texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        TextureRegion textureRegion = new TextureRegion(texture);
        //texture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        //texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        return new Material(
                //ColorAttribute.createAmbient(0.4f, 1f, 1f, 1),
                //ColorAttribute.createDiffuse(0.3f, 1f, 1f, 1),
                //ColorAttribute.createSpecular(0.5f, 1f, 1f, 1),
                //ColorAttribute.createDiffuse(1f, 1f, 1f, 1),
                //ColorAttribute.createAmbient(1f, 1f, 1f, 1),
                //FloatAttribute.createShininess(10f),
                TextureAttribute.createAmbient(texture),
                TextureAttribute.createDiffuse(texture),
                TextureAttribute.createSpecular(texture),
                TextureAttribute.createBump(texture),
                TextureAttribute.createEmissive(texture)
        );
    }

    public static Material createMattDiffuse() {
        return new Material(
                ColorAttribute.createDiffuse(76, 181, 151, 1)
                //FloatAttribute.createShininess(76.8f)
        );
    }
}
