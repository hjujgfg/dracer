package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;

public class Materials {

    public static Material createSilver() {
        return new Material(
                ColorAttribute.createAmbient(0.19225f, 0.19225f, 0.19225f, 1),
                ColorAttribute.createDiffuse(0.50754f, 0.50754f, 0.50754f, 1),
                ColorAttribute.createSpecular(0.508273f, 0.508273f, 0.508273f, 1),
                FloatAttribute.createShininess(51.2f)
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

    public static Material createMattDiffuse() {
        return new Material(
                ColorAttribute.createDiffuse(76, 181, 151, 1)
                //FloatAttribute.createShininess(76.8f)
        );
    }
}
