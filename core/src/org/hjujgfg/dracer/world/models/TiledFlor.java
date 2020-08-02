package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import net.mgsx.gltf.scene3d.attributes.PBRColorAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRFloatAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRTextureAttribute;
import net.mgsx.gltf.scene3d.attributes.PBRVertexAttributes;

import org.hjujgfg.dracer.world.interfaces.ModelSupplier;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.hjujgfg.dracer.util.FloatUtils.bigger;
import static org.hjujgfg.dracer.world.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.world.BigStatic.RANDOM;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class TiledFlor implements RenderAction, ModelSupplier {

    private final static int ROWS = 25;
    private final static int COLS = 5;

    private final static Model floorPlane;

    private List<ModelInstance> planes;

    private ModelInstance[] lastPlanes = new ModelInstance[COLS];

    static {
        floorPlane = MODEL_BUILDER.createBox(0.2f, 2f, 2f,
                new Material(
                        ColorAttribute.createDiffuse(Color.FOREST),
                        ColorAttribute.createDiffuse(Color.BLACK),
                        ColorAttribute.createSpecular(Color.WHITE),
                        TextureAttribute.createNormal(new Texture("metall.jpg")),
                        FloatAttribute.createShininess(256)
                ),
                VertexAttributes.Usage.Normal |
                        VertexAttributes.Usage.Position |
                        VertexAttributes.Usage.TextureCoordinates |
                        256 |
                        PBRVertexAttributes.Usage.PositionTarget |
                        PBRVertexAttributes.Usage.NormalTarget
        );
    }

    public TiledFlor() {
        planes = new ArrayList<>();
        for (int i = 0; i < ROWS; i ++) {
            for (int j = -2; j < COLS - 2; j ++) {
                ModelInstance newOne = new ModelInstance(floorPlane);
                newOne.transform.setToTranslation(0, i * 2.1f, j * 2.1f);
                planes.add(newOne);
                if (i == ROWS - 2) {
                    lastPlanes[j + 2] = newOne;
                }
            }
        }
    }

    @Override
    public Collection<ModelInstance> getModels() {
        return planes;
    }

    @Override
    public void dispose() {
        floorPlane.dispose();
    }

    @Override
    public void render() {
        planes.forEach(this::moveOneFloorPlane);
    }

    private void moveOneFloorPlane(ModelInstance fl) {
        Vector3 position;
        position = fl.transform.getTranslation(new Vector3());
        /*if (x == 0 && y == 1) {
            Gdx.app.log("FLOOR", String.format("Floor now at %f %f %f ",
                    position.x, position.y, position.z));
        }*/
        if (bigger(-4.2f, position.y, 0.00001f)) {
            int index = (int) (position.z / 2.1f) + 2;
            float last = lastPlanes[index].transform.getTranslation(new Vector3()).y;
            //fl.transform.setTranslation(11 + RANDOM.nextInt(50), last + 2.1f, position.z);
            fl.transform.setTranslation(MathUtils.randomSign() * 40 + RANDOM.nextInt(5), last + 2.1f, position.z);
            lastPlanes[index] = fl;
        } else {
            fl.transform.translate(0, - PROBLEM_SPEED.get(), 0);
        }
        if (bigger(0, position.x, 0.00001f)) {
            fl.transform.translate(Math.min(Math.abs(PROBLEM_SPEED.get() * 2), Math.abs(position.x)),
                    0,
                    0);
        }
        if (bigger(position.x, 0, 0.00001f)) {
            fl.transform.translate( - Math.min(Math.abs(PROBLEM_SPEED.get() * 2), Math.abs(position.x)),
                    0,
                    0);
        }
    }
}
