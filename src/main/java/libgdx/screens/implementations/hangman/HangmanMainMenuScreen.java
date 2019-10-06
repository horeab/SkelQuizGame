package libgdx.screens.implementations.hangman;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.campaign.CampaignStoreService;
import libgdx.campaign.LettersCampaignLevelEnum;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.hangman.HangmanCampaignLevelEnum;
import libgdx.implementations.hangman.HangmanGame;
import libgdx.implementations.hangman.HangmanQuestionCategoryEnum;
import libgdx.implementations.hangman.HangmanSpecificResource;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Resource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.geoquiz.GeoQuizCampaignScreen;
import libgdx.screens.implementations.geoquiz.QuizScreenManager;
import libgdx.utils.ScreenDimensionsManager;

public class HangmanMainMenuScreen extends AbstractScreen<HangmanScreenManager> {

    @Override
    public void buildStage() {
        addButtons();
    }

    private void addButtons() {
        Table table = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        if (Gdx.app.getType() == Application.ApplicationType.iOS) {
            new BackButtonBuilder().addHoverBackButton(this);
        }
        table.setFillParent(true);
        addTitle(table);
        MyButton startGameBtn = createStartGameBtn();
        table.add(startGameBtn).height(ScreenDimensionsManager.getScreenHeightValue(13)).width(ScreenDimensionsManager.getScreenWidthValue(70)).padTop(verticalGeneralMarginDimen * 4).row();
        Table maxLevelTable = new Table();
        maxLevelTable.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setSingleLineLabel().setText(MainGameLabel.l_level_record.getText(new CampaignStoreService().getAllStarsWon())).build()));
        maxLevelTable.add(GraphicUtils.getImage(HangmanSpecificResource.star)).padLeft(horizontalGeneralMarginDimen).width(horizontalGeneralMarginDimen * 5).height(horizontalGeneralMarginDimen * 5);
        table.add(maxLevelTable).padTop(horizontalGeneralMarginDimen);
        addActor(table);
    }

    private void addTitle(Table table) {
        Image titleRaysImage = GraphicUtils.getImage(Resource.title_rays);
        new ActorAnimation(titleRaysImage, this).animateFastFadeInFadeOut();
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
        MyButton button = new ButtonBuilder().setButtonSkin(GameButtonSkin.HANGMAN_MENU).setText(MainGameLabel.l_new_game.getText()).build();
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new CampaignStoreService().reset();
                screenManager.showCampaignScreen();
            }
        });
        return button;
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

}
