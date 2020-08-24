package org.hjujgfg.dracer.world.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import org.hjujgfg.dracer.world.ContextualizedInstance;
import org.hjujgfg.dracer.gameplay.GameContext;
import org.hjujgfg.dracer.world.interfaces.RenderAction;

import static org.hjujgfg.dracer.gameplay.states.GameMode.TOP_VIEW_PROBLEM_EVASION;
import static org.hjujgfg.dracer.world.params.ParamsSupplierFactory.PROBLEM_SPEED;

public class StatsOverlay extends ContextualizedInstance implements RenderAction {

    protected long startTime = System.currentTimeMillis();
    protected long hits;
    protected Label label;
    protected Label onDemandLabel;
    protected Label speedLabel;
    protected BitmapFont font;
    protected Stage stage;
    StringBuilder builder = new StringBuilder();

    public StatsOverlay(Stage stage, GameContext context) {
        super(context);
        font = new BitmapFont();
        font.getData().setScale(4f, 4f);
        label = new Label(" ", new Label.LabelStyle(font, Color.GREEN));
        onDemandLabel = new Label(" ", new Label.LabelStyle(font, Color.GREEN));
        int height = Gdx.graphics.getHeight();
        onDemandLabel.setPosition(60, height - 200);
        speedLabel = new Label(" ", new Label.LabelStyle(font, Color.GREEN));
        speedLabel.setPosition(60, height - 300);
        this.stage = stage;
        this.stage = new Stage();
        this.stage.addActor(label);
        this.stage.addActor(onDemandLabel);
        this.stage.addActor(speedLabel);
    }

    public void addHit() {
        hits += 1;
    }

    @Override
    public void render() {
        builder.setLength(0);
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
        builder.append(String.format(" Speed: %.2f", PROBLEM_SPEED.get())).append("|");
        label.setText(builder);
        builder.setLength(0);
        if (context.getGameMode() == TOP_VIEW_PROBLEM_EVASION) {
            builder.append(" Top view: |");
            for (int i = TOP_VIEW_PROBLEM_EVASION.modeLength / 1000; i >= 0; i--) {
                if (i < context.getDiff() / 1000) {
                    builder.append('-');
                } else {
                    builder.append('|');
                }
            }
            builder.append('|');
        }
        onDemandLabel.setText(builder);
        builder.setLength(0);
        builder.append(" Speed: |");
        for (int i = 0; i < 20; i ++) {
            if (PROBLEM_SPEED.get() * 10 < i) {
                builder.append("-");
            } else {
                builder.append("|");
            }
        }
        builder.append("|");
        speedLabel.setText(builder);
        stage.draw();
        stage.act();
    }
}
