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
import libgdx.controls.button.builders.ButtonWithIconBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.hangman.HangmanSpecificResource;
import libgdx.implementations.hangmanarena.HangmanArenaGame;
import libgdx.implementations.hangmanarena.HangmanArenaSpecificResource;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Resource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.hangmanarena.spec.RoundButtonBuilder;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

public class HangmanArenaMainMenuScreen extends AbstractScreen<HangmanArenaScreenManager> {

    @Override
    public void buildStage() {
        addScreenBackground();
        createGameControls();
        executeOnFirstTimeMainMenuDisplayed();
    }


    private void createGameControls() {
        MyButton extraContentButton = null;
        Table table = new Table();
        table.setFillParent(true);
        table.add().padTop(MainDimen.vertical_general_margin.getDimen() * 2).row();
        addTitle(table);
        table.add().height(ScreenDimensionsManager.getScreenHeightValue(10)).row();
        addStartGameButtons(table);

        if (!Utils.isValidExtraContent()) {
//            table.row();
//            float dimen = MainDimen.horizontal_general_margin.getDimen();
//            extraContentButton = new ButtonWithIconBuilder("", MainResource.mug_color)
//                    .setFixedButtonSize(GameButtonSize.NORMAL_MENU_ROUND_IMAGE).build();
//            new ActorAnimation(extraContentButton, Game.getInstance().getAbstractScreen()).animateZoomInZoomOut();
//            table.add(extraContentButton).padTop(dimen * 5).width(GameButtonSize.NORMAL_MENU_ROUND_IMAGE.getWidth())
//                    .height(GameButtonSize.NORMAL_MENU_ROUND_IMAGE.getHeight());
//            extraContentButton.setTransform(true);
//            extraContentButton.addListener(new ClickListener() {
//                @Override
//                public void clicked(InputEvent event, float x, float y) {
//                    displayInAppPurchasesPopup(new Runnable() {
//                        @Override
//                        public void run() {
//                            screenManager.showMainScreen();
//                        }
//                    });
//                }
//            });
        }


        table.row();
        table.add().growY().row();
        Image image = GraphicUtils.getImage(HangmanArenaSpecificResource.forest_texture);
        table.add(image).width(ScreenDimensionsManager.getScreenWidth())
                .height(ScreenDimensionsManager.getNewHeightForNewWidth(ScreenDimensionsManager.getScreenWidth(), image));
        addActor(table);
        if (extraContentButton != null) {
            extraContentButton.toFront();
        }
    }

    public static void displayInAppPurchasesPopup(Runnable redirectAfterBoughtScreen) {
        Game.getInstance().getInAppPurchaseManager().displayInAppPurchasesPopup(MainGameLabel.l_extracontent.getText(), redirectAfterBoughtScreen);
    }

    private void addTitle(Table table) {
        Image titleRaysImage = GraphicUtils.getImage(Resource.title_rays);
        new ActorAnimation(titleRaysImage, getAbstractScreen()).animateFastFadeInFadeOut();
        float titleWidth = ScreenDimensionsManager.getScreenWidth();
        float titleHeight = ScreenDimensionsManager.getNewHeightForNewWidth(titleWidth,
                titleRaysImage.getWidth(), titleRaysImage.getHeight());
        titleRaysImage.setWidth(titleWidth);
        titleRaysImage.setHeight(titleHeight);
        titleRaysImage.setY(ScreenDimensionsManager.getScreenHeightValue(49));
        addActor(titleRaysImage);
        Stack titleLabel = createTitleLabel();
        table.add(titleLabel)
                .width(titleWidth)
                .height(ScreenDimensionsManager.getScreenHeightValue(30))
                .row();
    }

    private void addScreenBackground() {
        Image mainMenuScreenBackground = GraphicUtils.getImage(HangmanArenaSpecificResource.main_menu_screen_background);
        mainMenuScreenBackground.setFillParent(true);
        mainMenuScreenBackground.toBack();
        addActor(mainMenuScreenBackground);
    }

    private void addStartGameButtons(Table table) {
        MyButton button = createCampaignButton();
        table.add(button).height(button.getHeight()).width(button.getWidth());
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
        MyButton button = new RoundButtonBuilder(getAbstractScreen())
                .setCampaignButton().setFontDimen(FontManager.calculateMultiplierStandardFontSize(1.2f)).build();
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

    @Override
    public void render(float delta) {
        super.render(delta);
        Utils.createChangeLangPopup();
    }
}
