package libgdx.controls.animations;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.resources.FontManager;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;

public class WordAnimation {

    private AbstractScreen abstractScreen;

    public WordAnimation() {
        this.abstractScreen = Game.getInstance().getAbstractScreen();
    }

    public void animateShowFadeOut(String text, FontColor fontColor) {
        MyWrappedLabel infoLabel = createLabelToAnimate(text, fontColor);
        abstractScreen.addActor(infoLabel);
    }

    public MyWrappedLabel createLabelToAnimate(String text, FontColor fontColor) {
        MyWrappedLabel infoLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontScale(FontManager.getBigFontDim()).setText(text).setFontColor(fontColor).build());
        addWordInfoAnimation(infoLabel);
        infoLabel.setX(ScreenDimensionsManager.getScreenWidth(50));
        infoLabel.setY(ScreenDimensionsManager.getScreenHeight(60));
        infoLabel.toFront();
        return infoLabel;
    }

    private void addWordInfoAnimation(Table actor) {
        actor.setTransform(true);
        actor.setOrigin(Align.center);
        float duration = 1f;
        actor.addAction(
                Actions.sequence(
                        Actions.fadeOut(duration),
                        Utils.createRemoveActorAction(actor)
                ));
        actor.addAction(Actions.scaleBy(-0.1f, -0.1f, duration));
    }

}
