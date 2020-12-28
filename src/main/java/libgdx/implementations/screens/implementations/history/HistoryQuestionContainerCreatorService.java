package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.history.HistoryCampaignLevelEnum;
import libgdx.implementations.history.HistoryPreferencesService;
import libgdx.implementations.history.HistorySpecificResource;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public abstract class HistoryQuestionContainerCreatorService<TGameService extends HistoryGameService> extends QuestionContainerCreatorService<TGameService> {

    float optionHeight;
    HistoryGameScreen historyGameScreen;
    HistoryPreferencesService historyPreferencesService;
    MyWrappedLabel scoreLabel;
    MyWrappedLabel totalHintsLabel;
    private Integer timelineDisplayed = null;
    private MyWrappedLabel epochNameLabel;

    public HistoryQuestionContainerCreatorService(GameContext gameContext, HistoryGameScreen abstractGameScreen, HistoryPreferencesService historyPreferencesService) {
        super(gameContext, abstractGameScreen);
        this.historyGameScreen = abstractGameScreen;
        this.historyPreferencesService = historyPreferencesService;
        initOptionHeight();
    }

    @Override
    public Table createAnswerOptionsTable() {
        return new Table();
    }

    @Override
    public Table createQuestionTable() {
        Table questionTable = createOptionsTable();
        questionTable.setBackground(GraphicUtils.getNinePatch(MainResource.transparent_background));
        return questionTable;
    }

    @Override
    protected void setContainerBackground() {
    }

    protected abstract Table createOptionsTable();

    protected abstract List<Integer> getQuestionNrInOrder();

    public float getOptionHeight() {
        return optionHeight;
    }

    protected Table createAnswImg(int index, String prefix) {
        Table table = new Table();
        Image img = createOptImg(index, prefix);
        table.add(img).width(img.getWidth()).height(img.getHeight());
        table.setName(getOptionImageName(index));
        return table;
    }

    protected Image createOptImg(int index, String prefix) {
        Res res = HistorySpecificResource.timeline_opt_unknown;
        try {
            if (historyPreferencesService.getAllLevelsPlayed(getCampaignLevelEnum()).contains(index)
                    || historyPreferencesService.getLevelsImgShown(getCampaignLevelEnum()).contains(index)) {
                res = HistorySpecificResource.valueOf(prefix + index);
            }
        } catch (Exception ignore) {
        }
        Image image = GraphicUtils.getImage(res);
        float answerImgSideDimen = getAnswerImgSideDimen();
        Pair<Float, Float> size = ScreenDimensionsManager.resize(answerImgSideDimen, answerImgSideDimen, image);
        image.setWidth(size.getLeft());
        image.setHeight(size.getRight());
        return image;
    }

    protected Drawable getTimelineItemBackgr(String questionString, Integer questionNr) {
        Res res;
        if (historyPreferencesService.getLevelsLost(getCampaignLevelEnum()).contains(questionNr)) {
            res = HistorySpecificResource.timeline_opt_wrong;
        } else if (historyPreferencesService.getLevelsWon(getCampaignLevelEnum()).contains(questionNr)) {
            res = HistorySpecificResource.timeline_opt_correct;
        } else {
            res = HistorySpecificResource.valueOf("timeline" + getTimelineForYear(getOptionYear(questionString)) + "_opt_background"
                    + (questionNr % 2 == 0 ? "_odd" : ""));
        }
        return GraphicUtils.getNinePatch(res);
    }

    protected void processWonQuestion(Integer currentQuestion, String prefix) {
        historyPreferencesService.setLevelWon(currentQuestion, getCampaignLevelEnum());
        int index = getQuestionNrInOrder().indexOf(currentQuestion);
        displayAnswerImage(prefix, index);
    }

    private void displayAnswerImage(String prefix, int index) {
        Table imgTable = historyGameScreen.getRoot().findActor(getOptionImageName(index));
        if (imgTable != null) {
            Actor oldImg = imgTable.getChildren().get(0);
            oldImg.addAction(Actions.sequence(Actions.fadeOut(0.2f), Utils.createRemoveActorAction(oldImg)));
            Image img = createOptImg(index, prefix);
            imgTable.clear();
            img.setVisible(false);
            imgTable.add(img).width(img.getWidth()).height(img.getHeight());
            new ActorAnimation(img, historyGameScreen).animateFastFadeIn();
        }
    }

    protected Drawable getBackgroundForTimeline(String questionString) {
        return GraphicUtils.getNinePatch(HistorySpecificResource.valueOf("timeline3_line_background"));
    }

    protected void updateControlsAfterAnswPressed(String questionString, Integer currentQuestion) {
        int index = getQuestionNrInOrder().indexOf(currentQuestion);
        Table item = historyGameScreen.getRoot().findActor(getTimelineItemName(index));
        MyButton optBtn = historyGameScreen.getRoot().findActor(getOptionBtnName(index));
        optBtn.setButtonSkin(getButtonSkin(currentQuestion));
        optBtn.setTransform(false);
        optBtn.setDisabled(true);
        item.setBackground(getTimelineItemBackgr(questionString, index));
        historyGameScreen.goToNextQuestion();
        if (scoreLabel != null) {
            float scaleFactor = 0.3f;
            float duration = 0.2f;
            scoreLabel.addAction(Actions.sequence(Actions.scaleBy(scaleFactor, scaleFactor, duration),
                    Actions.scaleBy(-scaleFactor, -scaleFactor, duration)));
            scoreLabel.setText(historyPreferencesService.getLevelsWon(getCampaignLevelEnum()).size() + "");
        }
    }

    protected String getOptionBtnName(Integer index) {
        return "optBtn" + index;
    }

    protected String getTimelineItemName(int i) {
        return "timeLineTable" + i;
    }


    protected void processLostQuestion(Integer currentQuestion) {
        int index = getQuestionNrInOrder().indexOf(currentQuestion);
        Table imgTable = historyGameScreen.getRoot().findActor(getOptionImageName(index));
        historyPreferencesService.setLevelLost(currentQuestion, getCampaignLevelEnum());
        Image img = GraphicUtils.getImage(HistorySpecificResource.hist_answ_wrong);
        imgTable.clear();
        img.setVisible(false);
        float imgDimen = getAnswerImgSideDimen() / 2;
        imgTable.add(img).width(imgDimen).height(imgDimen);
        new ActorAnimation(img, historyGameScreen).animateFastFadeIn();
    }

    protected void showPopupAd(Runnable runnable) {
        int size = historyPreferencesService.getAllLevelsPlayed(getCampaignLevelEnum()).size();
        if (size > 0 && size % 15 == 0) {
            Game.getInstance().getAppInfoService().showPopupAd(runnable);
        } else {
            runnable.run();
        }
    }

    protected abstract int getOptionYear(String qString);

    protected String getOptionImageName(Integer index) {
        return "optImage" + index;
    }

    protected Image createTimelineArrow(boolean left) {
        Image image = GraphicUtils.getImage(left ? HistorySpecificResource.arrow_right : HistorySpecificResource.arrow_left);
        image.setWidth(GameButtonSize.HISTORY_TIMELINE_ARROW.getWidth());
        image.setHeight(GameButtonSize.HISTORY_TIMELINE_ARROW.getHeight());
        return image;
    }

    protected abstract ButtonSkin getButtonSkin(Integer questionNr);

    protected abstract void initOptionHeight();

    protected abstract float getAnswerImgSideDimen();

    protected abstract TGameService getGameService();

    public abstract HistoryCampaignLevelEnum getCampaignLevelEnum();


    public void refreshTitleTable(ScrollPane scrollPane) {
        if (scrollPane != null && epochNameLabel != null) {
            int option = getOptionDisplayed(scrollPane);
            if (gameContext.getCurrentUserGameUser().getAllQuestionInfos().size() > option) {
                int answYear = getGameService().getSortYear(gameContext.getCurrentUserGameUser().getAllQuestionInfos().get(option).getQuestion().getQuestionString());
                int timelineForYear = getTimelineForYear(answYear);
                if (timelineDisplayed == null || timelineForYear != timelineDisplayed) {
                    timelineDisplayed = timelineForYear;
                    String text = SpecificPropertiesUtils.getText(Game.getInstance().getAppInfoService().getLanguage() + "_" + Game.getInstance().getGameIdPrefix() + "_timeline_" + timelineForYear);
                    epochNameLabel.setText(text);
                }
            }
        }
    }

    private int getOptionDisplayed(ScrollPane scrollPane) {
        int option = Math.round(scrollPane.getScrollY() / getOptionHeight());
        option = Math.max(option, 0);
        option = Math.min(option, gameContext.getCurrentUserGameUser().getAllQuestionInfos().size());
        return option;
    }

    public int getTimelineForYear(int year) {
        if (year < -3200) {
            return 0;
        } else if (year < 499) {
            return 1;
        } else if (year < 1499) {
            return 2;
        } else {
            return 3;
        }
    }

    protected Table createTitleTable(float width) {
        Table table = new Table();
        epochNameLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setWrappedLineLabel(width)
                .setText("0")
                .setFontConfig(new FontConfig(Color.FOREST, FontConfig.FONT_SIZE * 1.1f)).build());
        table.add(epochNameLabel).width(width);
        return table;
    }

    public Table createHeader() {
        Table table = new Table();
        scoreLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(historyPreferencesService.getLevelsWon(getCampaignLevelEnum()).size() + "")
                .setFontConfig(new FontConfig(FontColor.YELLOW.getColor(), Color.BLACK, FontConfig.FONT_SIZE * 1.3f, 3f)).build());
        scoreLabel.setTransform(true);
        float hintIconDimen = ScreenDimensionsManager.getScreenWidthValue(12);
        totalHintsLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setWrappedLineLabel(hintIconDimen)
                .setText(historyGameScreen.totalHints + "")
                .setFontConfig(new FontConfig(FontColor.LIGHT_GREEN.getColor(), Color.BLACK, FontConfig.FONT_SIZE * 1.5f, 3f)).build());
        float scoreIconDimen = ScreenDimensionsManager.getScreenWidthValue(6);
        Table hintTable = new Table();
        Stack stack = createHintBtn(hintIconDimen);
        hintTable.add(stack).width(stack.getWidth()).height(stack.getHeight());
        float sideDimen = scoreIconDimen * 2;
        float titleWidth = ScreenDimensionsManager.getScreenWidthValue(45);
        table.add(createScoreTable(scoreIconDimen)).padLeft(MainDimen.horizontal_general_margin.getDimen() * 5).width(sideDimen);
        table.add(createTitleTable(titleWidth)).width(titleWidth).height(ScreenDimensionsManager.getScreenHeightValue(5));
        table.add(hintTable).padLeft(MainDimen.horizontal_general_margin.getDimen() * 2).width(hintIconDimen).height(hintIconDimen);
        table.setBackground(GraphicUtils.getNinePatch(HistorySpecificResource.timeline2_opt_background));
        return table;
    }

    private Stack createHintBtn(float hintIconDimen) {
        final Stack stack = new Stack();
        final MyButton hintBtn = new ButtonBuilder()
                .setFontColor(FontColor.BLACK)
                .setButtonSkin(GameButtonSkin.HISTORY_HINT).setText("").build();
        stack.setTransform(true);
        hintBtn.setWidth(hintIconDimen);
        hintBtn.setHeight(hintIconDimen);
        stack.setWidth(hintIconDimen);
        stack.setHeight(hintIconDimen);
        ClickListener clickListener = new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                historyGameScreen.totalHints = historyGameScreen.totalHints - 1;
                if (historyGameScreen.totalHints == 0) {
                    stack.setTouchable(Touchable.disabled);
                    stack.addAction(Actions.fadeOut(0.2f));
                }
                totalHintsLabel.setText(historyGameScreen.totalHints + "");
                for (int i = historyGameScreen.firstOpenQuestionIndex; i <= historyGameScreen.firstOpenQuestionIndex + 4; i++) {
                    historyPreferencesService.setLeveImgShown(i, getCampaignLevelEnum());
                    displayAnswerImage(getPrefix(), i);
                }
            }
        };
        hintBtn.addListener(clickListener);
        totalHintsLabel.addListener(clickListener);
        new ActorAnimation(stack, getAbstractGameScreen()).animateZoomInZoomOut(0.3f);
        stack.add(hintBtn);
        float pad = MainDimen.vertical_general_margin.getDimen() * 2;
        totalHintsLabel.padTop(pad);
        totalHintsLabel.padLeft(pad);
        stack.add(totalHintsLabel);
        return stack;
    }

    private Table createScoreTable(float scoreIconDimen) {
        Table scoreTable = new Table();
        float dimen = MainDimen.horizontal_general_margin.getDimen();
        scoreTable.add(scoreLabel).width(scoreIconDimen).padRight(dimen);
        Image image = GraphicUtils.getImage(HistorySpecificResource.score_icon);
        scoreTable.add(image).width(scoreIconDimen).padRight(dimen * 2).height(scoreIconDimen);
        return scoreTable;
    }

    protected abstract String getPrefix();

    @Override
    public ButtonSkin correctAnswerSkin() {
        return GameButtonSkin.ANSWER_IMAGE_CLICK_CORRECT;
    }

    @Override
    public ButtonSkin wrongAnswerSkin() {
        return GameButtonSkin.ANSWER_IMAGE_CLICK_WRONG;
    }

    @Override
    public int getNrOfAnswerRows() {
        return 2;
    }

    @Override
    public int getNrOfAnswersOnRow() {
        return 2;
    }

    @Override
    protected MyButton createAnswerButton(final String answer) {
        return null;
    }

}