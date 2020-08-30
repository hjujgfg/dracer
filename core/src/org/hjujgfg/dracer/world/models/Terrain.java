package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hjujgfg.dracer.gameplay.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.world.models.Materials.createSilver;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class Terrain implements ModelSupplier, RenderAction {

    private final Vector3 TMP = new Vector3();

    private final List<Model> models;
    private final Map<ModelInstance, HeightField> instanceToField;
    private final Map<ModelInstance, ModelInstance> toPrev;

    public Terrain() {
        instanceToField = new HashMap<>(6);
        models = new ArrayList<>(6);
        toPrev = new HashMap<>();
        int onSide = 3;
        ModelInstance prevl = null, firstl = null, prevr = null, firstr = null;
        for (int i = 0; i < onSide; i ++) {
            HeightField fl = createHeightMap();
            MODEL_BUILDER.begin();
            MODEL_BUILDER.part("1",
                    fl.mesh,
                    GL20.GL_TRIANGLES,
                    0,
                    fl.mesh.getNumIndices(),
                    createSilver()
            );
            Model m = MODEL_BUILDER.end();
            models.add(m);
            ModelInstance instance = new ModelInstance(m, 0,-5 + (55) * i ,0);
            instanceToField.put(instance, fl);
            if (prevl == null) {
                firstl = instance;
            } else {
                toPrev.put(instance, prevl);
            }
            prevl = instance;

            HeightField fl2 = createHeightMap();
            MODEL_BUILDER.begin();
            MODEL_BUILDER.part("1",
                    fl2.mesh,
                    GL20.GL_TRIANGLES,
                    0,
                    fl2.mesh.getNumIndices(),
                    createSilver()
            );
            Model m2 = MODEL_BUILDER.end();
            models.add(m2);
            instance = new ModelInstance(m, 0,-5 + (55) * i ,65.3f);
            instanceToField.put(instance, fl2);
            if (prevr == null) {
                firstr = instance;
            } else {
                toPrev.put(instance, prevr);
            }
            prevr = instance;
        }
        toPrev.put(firstl, prevl);
        toPrev.put(firstr, prevr);
    }

    @Override
    public Collection<ModelInstance> getModels() {
        return instanceToField.keySet();
    }

    @Override
    public void dispose() {
        models.forEach(Model::dispose);
    }

    @Override
    public void render() {
        move();
    }

    private void move() {
        instanceToField.forEach((m, f) -> {
            //randomizeField(f, 0.05f * Math.max(PROBLEM_SPEED.get(), 1f));
            m.transform.translate(0, - PROBLEM_SPEED.get(), 0);
            Vector3 translation = m.transform.getTranslation(TMP);
            if (translation.y < -55) {
                ModelInstance prev = toPrev.get(m);
                randomizeField(f, 2);
                float prevY = prev.transform.getTranslation(TMP).y;
                //Gdx.app.log("PREV_TERRAIN", "Pre terrain y: " + prevY);
                m.transform.setToTranslation(translation.x, prevY + 50, translation.z);
            }
        });
    }

    private HeightField createHeightMap() {
        int width = 8;
        int height = 16;
        HeightField field = new HeightField(false, width, height, false,
                VertexAttributes.Usage.Position
                        | VertexAttributes.Usage.Normal
                        | VertexAttributes.Usage.ColorUnpacked
                        | VertexAttributes.Usage.TextureCoordinates);
        field.corner00.set(0, -5f, -60f);
        field.corner10.set(0f, -5f, -5.3f);
        field.corner01.set(0, 50f, -60f);
        field.corner11.set(0, 50f, -5.3f);
        field.color00.set(0, 0, 1, 1);
        field.color01.set(0, 0, 1, 1);
        field.color10.set(0, 1, 1, 1);
        field.color11.set(0, 1, 1, 1);
        field.magnitude.set(1, 0, 0);
        randomizeField(field, 1f);
        /*field.data[0] = 10f;
        field.data[2] = -5f;
        field.data[4] = 10f;
        field.data[6] = -5f;*/
        return field;
    }

    private void randomizeField(HeightField field, float factor) {
        int lastRow = field.width * field.height - field.width;
        for (int i = 0; i < field.data.length; i++) {
            if ((i + 1) % field.width == 0 ||
                    i % field.width == 0 ||
                    i < field.width ||
                    i > lastRow
            ) {
                continue;
            }
            field.data[i] = MathUtils.random(1f * factor) * MathUtils.randomSign();
        }
        field.update();
    }

    private static Model buildTerrain() {
        int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates;
        MODEL_BUILDER.begin();
        MODEL_BUILDER.part("face1", GL20.GL_TRIANGLES, attr, createSilver())
                .triangle(
                        new Vector3(0, 0, -2),
                        new Vector3(0, 10, 0),
                        new Vector3(0, 0, 2)
                );
        return MODEL_BUILDER.end();
    }
}
