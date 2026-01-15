package libgdx.skelgameimpl.screens.mainmenu;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.game.ScreenManager;
import libgdx.graphics.GraphicUtils;
import libgdx.resources.FontManager;
import libgdx.resources.Resource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
import libgdx.utils.ScreenDimensionsManager;

public class MainMenuScreen extends AbstractScreen<ScreenManager> {

    @Override
    public void buildStage() {
        new SkelGameRatingService(this).appLaunched();
        addButtons();
    }

    private void addButtons() {
        Table table = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        if (Gdx.app.getType() == Application.ApplicationType.iOS) {
            MyButton backBtn = new BackButtonBuilder().createScreenBackButton(this);
            table.add(backBtn).padLeft(-MainDimen.horizontal_general_margin.getDimen() * 40).padTop(-verticalGeneralMarginDimen * 3).width(backBtn.getWidth()).height(backBtn.getHeight()).row();
        }
        table.setFillParent(true);
        addTitle(table);
        MyButton startGameBtn = createStartGameBtn();
        table.add(startGameBtn).height(ScreenDimensionsManager.getScreenHeight(13)).width(ScreenDimensionsManager.getScreenWidth(70)).padTop(verticalGeneralMarginDimen * 4).row();
        table.add().padTop(verticalGeneralMarginDimen * 17);
        addActor(table);
    }

    private void addTitle(Table table) {
        Image titleRaysImage = GraphicUtils.getImage(Resource.title_rays);
        new ActorAnimation(this).animateFastFadeInFadeOut(titleRaysImage);
        float titleWidth = ScreenDimensionsManager.getScreenWidth();
        float titleHeight = ScreenDimensionsManager.getNewHeightForNewWidth(titleWidth, titleRaysImage.getWidth(), titleRaysImage.getHeight());
        titleRaysImage.setWidth(titleWidth);
        titleRaysImage.setHeight(titleHeight);
        titleRaysImage.setY(ScreenDimensionsManager.getScreenHeight(49));
        addActor(titleRaysImage);
        Stack titleLabel = createTitleLabel();
        table.add(titleLabel)
                .width(titleWidth)
                .height(titleHeight)
                .padBottom(MainDimen.vertical_general_margin.getDimen() * 1)
                .row();
    }

    private Stack createTitleLabel() {
        String appName = Game.getInstance().getAppInfoService().getAppName();
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(appName).build());
        titleLabel.setFontScale(FontManager.calculateMultiplierStandardFontSize(appName.length() > 14 ? 1.5f : 2f));
        titleLabel.setAlignment(Align.center);
        return createTitleStack(titleLabel);
    }

    protected Stack createTitleStack(MyWrappedLabel titleLabel) {
        Stack stack = new Stack();
        Image image = GraphicUtils.getImage(Resource.title_background);
        stack.addActor(image);
        stack.addActor(titleLabel);
        return stack;
    }

    private MyButton createStartGameBtn() {
        return new BackButtonBuilder().createScreenBackButton(this);
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

}
