package libgdx.screens.implementations.hangmanarena;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.hangmanarena.HangmanArenaGame;
import libgdx.implementations.hangmanarena.HangmanArenaSpecificResource;
import libgdx.resources.FontManager;
import libgdx.resources.Resource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.hangmanarena.spec.RoundButtonBuilder;
import libgdx.utils.ScreenDimensionsManager;

public class HangmanArenaMainMenuScreen extends AbstractScreen<HangmanArenaScreenManager> {

    @Override
    public void buildStage() {
        addScreenBackground();
        createGameControls();
        executeOnFirstTimeMainMenuDisplayed();
    }


    private void createGameControls() {
        Table table = new Table();
        table.add().padTop(MainDimen.vertical_general_margin.getDimen() * 2).row();
        addTitle(table);
        addStartGameButtons(table);
        table.setWidth(ScreenDimensionsManager.getScreenWidth());
        table.setHeight(ScreenDimensionsManager.getScreenHeight());
        addActor(table);
    }

    private void addTitle(Table table) {
        Image titleRaysImage = GraphicUtils.getImage(Resource.title_rays);
        new ActorAnimation(titleRaysImage, getAbstractScreen()).animateFastFadeInFadeOut();
        float titleWidth = ScreenDimensionsManager.getScreenWidth();
        float titleHeight = ScreenDimensionsManager.getNewHeightForNewWidth(titleWidth, titleRaysImage.getWidth(), titleRaysImage.getHeight());
        titleRaysImage.setWidth(titleWidth);
        titleRaysImage.setHeight(titleHeight);
        titleRaysImage.setY(ScreenDimensionsManager.getScreenHeightValue(49));
        addActor(titleRaysImage);
        Stack titleLabel = createTitleLabel();
        table.add(titleLabel)
                .width(titleWidth)
                .height(titleHeight)
                .padBottom(MainDimen.vertical_general_margin.getDimen() * 1)
                .row();
    }

    private void addScreenBackground() {
        Image mainMenuScreenBackground = GraphicUtils.getImage(HangmanArenaSpecificResource.main_menu_screen_background);
        mainMenuScreenBackground.setFillParent(true);
        mainMenuScreenBackground.toBack();
        addActor(mainMenuScreenBackground);
    }

    private void addStartGameButtons(Table table) {
        Table firstRowStartGame = new Table();
        addStartGameButton(firstRowStartGame, createCampaignButton());
        table.add(firstRowStartGame).row();
        Table secondRowStartGame = new Table();
        table.add(secondRowStartGame).padBottom(MainDimen.vertical_general_margin.getDimen() * 3).row();
    }

    private void addExtraButton(Table extraBtnTable, MyButton btn) {
        extraBtnTable.add(btn).height(btn.getHeight()).width(btn.getWidth());
    }

    private void addStartGameButton(Table table, MyButton button) {
        table.add(button).height(button.getHeight()).width(button.getWidth()).padRight(MainDimen.horizontal_general_margin.getDimen() * 2);
    }

    private Stack createTitleLabel() {
        String appName = HangmanArenaGame.getInstance().getAppInfoService().getAppName();
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setText(appName).build());
        titleLabel.setFontScale(FontManager.calculateMultiplierStandardFontSize(appName.length() > 14 ? 1.3f : 1.7f));
        titleLabel.setAlignment(Align.center);
        return createTitleStack(titleLabel);
    }

    protected Stack createTitleStack(MyWrappedLabel titleLabel) {
        Stack stack = new Stack();
        stack.addActor(GraphicUtils.getImage(HangmanArenaSpecificResource.title_clouds_background));
        stack.addActor(titleLabel);
        return stack;
    }

    private MyButton createCampaignButton() {
        MyButton button = new RoundButtonBuilder(getAbstractScreen()).setCampaignButton().setFontDimen(FontManager.calculateMultiplierStandardFontSize(1.2f)).build();
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                screenManager.showCampaignScreen();
            }
        });
        return button;
    }


    private void executeOnFirstTimeMainMenuDisplayed() {
        HangmanArenaGame instance = HangmanArenaGame.getInstance();
        if (instance.isFirstTimeMainMenuDisplayed()) {
            instance.getMainDependencyManager().createRatingService(this).appLaunched();
            instance.setFirstTimeMainMenuDisplayed(false);
        }
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

}
