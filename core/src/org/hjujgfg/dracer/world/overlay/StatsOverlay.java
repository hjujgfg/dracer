package org.hjujgfg.dracer.world.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import org.hjujgfg.dracer.world.ContextualizedInstance;
import org.hjujgfg.dracer.world.GameContext;
import org.hjujgfg.dracer.world.interfaces.RenderAction;
import org.hjujgfg.dracer.world.params.ProblemSpeed;

import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class StatsOverlay extends ContextualizedInstance implements RenderAction {

    protected long startTime = System.currentTimeMillis();
    protected long hits;
    protected Label label;
    protected BitmapFont font;
    protected Stage stage;

    public StatsOverlay(Stage stage, GameContext context) {
        super(context);
        font = new BitmapFont();
        font.getData().setScale(4f, 4f);
        label = new Label(" ", new Label.LabelStyle(font, Color.BLACK));
        this.stage = stage;
        this.stage = new Stage();
        this.stage.addActor(label);
    }

    public void addHit() {
        hits += 1;
    }

    @Override
    public void render() {
        StringBuilder builder = new StringBuilder();
        builder.append(" FPS: ").append(Gdx.graphics.getFramesPerSecond());
        long time = System.currentTimeMillis() - startTime;
        builder.append("| Game time: ").append(time);
        builder.append("| Hits: ").append(hits).append("/").append(context.passedProblems.getTotalPassedProblems());
        //builder.append(String"| Rating: ").append((float) hits/(float) time);
        //builder.append("| Position: ").append(context.getTransform(VEHICLE).getTranslation(new Vector3()));
        builder.append("| Ult: ");
        for (int i = 0; i < 20; i ++) {
            if (i < context.getPassedProblems().getPassedProblemsWithoutCollision()) {
                builder.append('|');
            } else {
                builder.append('-');
            }
        }
        builder.append('|');
        builder.append(" Speed: ").append(PROBLEM_SPEED.get()).append("|");
        label.setText(builder);
        stage.draw();
        stage.act();
    }
}
