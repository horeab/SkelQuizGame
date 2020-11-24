package libgdx.implementations.screens.implementations.hangman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import libgdx.campaign.CampaignStoreService;
import libgdx.campaign.QuestionConfig;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.hangman.HangmanCampaignLevelEnum;
import libgdx.implementations.hangman.HangmanGame;
import libgdx.implementations.hangman.HangmanPreferencesService;
import libgdx.implementations.hangman.HangmanQuestionCategoryEnum;
import libgdx.implementations.hangman.HangmanQuestionDifficultyLevel;
import libgdx.implementations.hangman.HangmanSpecificResource;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.RandomQuestionCreatorService;
import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.FontManager;
import libgdx.resources.Resource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class HangmanMainMenuScreen extends AbstractScreen<HangmanScreenManager> {

    @Override
    public void buildStage() {
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        addButtons();
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
    }

    private void addButtons() {
        Table table = new Table();
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        table.setFillParent(true);
        addTitle(table);
        MyButton startGameBtn = createStartGameBtn();
        table.add(startGameBtn).height(ScreenDimensionsManager.getScreenHeightValue(13)).width(ScreenDimensionsManager.getScreenWidthValue(70)).padTop(verticalGeneralMarginDimen * 4).row();
        Table maxLevelTable = new Table();
        maxLevelTable.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setSingleLineLabel().setText(MainGameLabel.l_level_record.getText(new HangmanPreferencesService().getHighScore() + "")).build()));
        maxLevelTable.add(GraphicUtils.getImage(HangmanSpecificResource.star)).padLeft(horizontalGeneralMarginDimen).width(horizontalGeneralMarginDimen * 5).height(horizontalGeneralMarginDimen * 5);
        table.add(maxLevelTable).padTop(horizontalGeneralMarginDimen * 2);
        addActor(table);
        table.setBackground(GraphicUtils.getNinePatch(HangmanSpecificResource.main_menu_background_texture));
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
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        table.add(titleLabel)
                .width(titleWidth)
                .height(titleHeight)
                .padTop(-verticalGeneralMarginDimen * 14)
                .padBottom(verticalGeneralMarginDimen * 1)
                .row();
    }

    private Stack createTitleLabel() {
        String appName = Game.getInstance().getAppInfoService().getAppName();
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontConfig(new FontConfig(
                FontColor.WHITE.getColor(),
                FontColor.BLACK.getColor(),
                2f)).setText(appName).build());
        titleLabel.setFontScale(FontManager.calculateMultiplierStandardFontSize(getTitleMultiplier(appName)));
        titleLabel.setAlignment(Align.center);
        return createTitleStack(titleLabel);
    }

    private float getTitleMultiplier(String appName) {
        if (appName.length() > 16) {
            return 1.3f;
        } else if (appName.length() > 13) {
            return 1.7f;
        }
        {
            return 2f;
        }
    }

    protected Stack createTitleStack(MyWrappedLabel titleLabel) {
        Stack stack = new Stack();
        Image image = GraphicUtils.getImage(HangmanSpecificResource.title_background);
        stack.addActor(image);
        stack.addActor(titleLabel);
        return stack;
    }

    private MyButton createStartGameBtn() {
        MyButton button = new ButtonBuilder().setSingleLineText(MainGameLabel.l_new_game.getText(), FontManager.getBigFontDim()).setButtonSkin(GameButtonSkin.HANGMAN_MENU).build();
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new CampaignStoreService().reset();
                GameContext gameContext = new GameContextService().createGameContext(5, getQuestions(HangmanGameScreen.TOTAL_QUESTIONS));
                HangmanGame.getInstance().getScreenManager().showCampaignGameScreen(gameContext, HangmanCampaignLevelEnum.LEVEL_0_0);

            }
        });
        return button;
    }

    private Question[] getQuestions(int nr) {
        List<Question> res = new ArrayList<>();
        int diff0 = 4;
        int diff1 = 3;
        int diff2 = 3;
        int diff3 = 2;
        int diff4 = 2;
        int diff5 = 1;

        List<String> questionCategoryStringList = new ArrayList<>();
        for (HangmanQuestionCategoryEnum categ : HangmanQuestionCategoryEnum.values()) {
            if (categ != HangmanQuestionCategoryEnum.cat5) {
                questionCategoryStringList.add(categ.name());
            }
        }
        QuestionConfig questionConfig = new QuestionConfig(Collections.singletonList(HangmanQuestionDifficultyLevel._0.name()), questionCategoryStringList, diff0, 5);
        res.addAll(Arrays.asList(new RandomQuestionCreatorService().createRandomQuestions(questionConfig)));
        questionConfig = new QuestionConfig(Collections.singletonList(HangmanQuestionDifficultyLevel._1.name()), questionCategoryStringList, diff1, 5);
        res.addAll(Arrays.asList(new RandomQuestionCreatorService().createRandomQuestions(questionConfig)));
        questionConfig = new QuestionConfig(Collections.singletonList(HangmanQuestionDifficultyLevel._2.name()), questionCategoryStringList, diff2, 5);
        res.addAll(Arrays.asList(new RandomQuestionCreatorService().createRandomQuestions(questionConfig)));
        questionConfig = new QuestionConfig(Collections.singletonList(HangmanQuestionDifficultyLevel._3.name()), questionCategoryStringList, diff3, 5);
        res.addAll(Arrays.asList(new RandomQuestionCreatorService().createRandomQuestions(questionConfig)));
        questionConfig = new QuestionConfig(Collections.singletonList(HangmanQuestionDifficultyLevel._4.name()), questionCategoryStringList, diff4, 5);
        res.addAll(Arrays.asList(new RandomQuestionCreatorService().createRandomQuestions(questionConfig)));
        questionConfig = new QuestionConfig(Collections.singletonList(HangmanQuestionDifficultyLevel._5.name()), questionCategoryStringList, diff5, 5);
        res.addAll(Arrays.asList(new RandomQuestionCreatorService().createRandomQuestions(questionConfig)));
        return res.toArray(new Question[0]);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Utils.createChangeLangPopup();
    }

    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

}
