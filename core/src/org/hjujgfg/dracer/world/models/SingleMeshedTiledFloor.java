package org.hjujgfg.dracer.world.models;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.model.Node;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.BoxShapeBuilder;

import static org.hjujgfg.dracer.gameplay.BigStatic.MODEL_BUILDER;
import static org.hjujgfg.dracer.world.models.Materials.createSilver;

public class SingleMeshedTiledFloor {

    private void buildMeshedStuff() {
        MODEL_BUILDER.begin();
        Node node = MODEL_BUILDER.node();
        MeshPartBuilder mpb = MODEL_BUILDER.part("box", GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Normal |
                        VertexAttributes.Usage.Position |
                        VertexAttributes.Usage.TextureCoordinates,
                createSilver());
        BoxShapeBuilder.build(mpb, 1, 1,1);


    }
}
