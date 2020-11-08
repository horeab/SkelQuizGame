package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import libgdx.controls.animations.ActorAnimation;
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
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

public abstract class HistoryQuestionContainerCreatorService<TGameService extends HistoryGameService> extends QuestionContainerCreatorService<TGameService> {

    float optionHeight;
    HistoryGameScreen historyGameScreen;
    HistoryPreferencesService historyPreferencesService = new HistoryPreferencesService();
    MyWrappedLabel scoreLabel;
    private Integer timelineDisplayed = null;
    private MyWrappedLabel epochNameLabel;

    public HistoryQuestionContainerCreatorService(GameContext gameContext, HistoryGameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
        this.historyGameScreen = abstractGameScreen;
        initOptionHeight();
    }

    @Override
    public Table createAnswerOptionsTable() {
        return new Table();
    }

    @Override
    public Table createQuestionTable() {
        Table questionTable = createOptionsTable();
        questionTable.setBackground(GraphicUtils.getNinePatch(MainResource.btn_lowcolor_up));
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
//            if (historyPreferencesService.getAllLevelsPlayed(getCampaignLevelEnum()).contains(index)) {
            res = HistorySpecificResource.valueOf(prefix + index);
//            }
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
            res = HistorySpecificResource.valueOf("timeline" + getTimelineForYear(getOptionYear(questionString)) + "_opt_background");
        }
        return GraphicUtils.getNinePatch(res);
    }

    protected void processWonQuestion(Integer currentQuestion, String prefix) {
        int index = getQuestionNrInOrder().indexOf(currentQuestion);
        Table imgTable = historyGameScreen.getRoot().findActor(getOptionImageName(index));
        Actor oldImg = imgTable.getChildren().get(0);
        oldImg.addAction(Actions.sequence(Actions.fadeOut(0.2f), Utils.createRemoveActorAction(oldImg)));
        historyPreferencesService.setLevelWon(currentQuestion, getCampaignLevelEnum());
        Image img = createOptImg(index, prefix);
        imgTable.clear();
        img.setVisible(false);
        imgTable.add(img).width(img.getWidth()).height(img.getHeight());
        new ActorAnimation(img, historyGameScreen).animateFastFadeIn();
    }

    protected Drawable getBackgroundForTimeline(String questionString) {
        return GraphicUtils.getNinePatch(HistorySpecificResource.valueOf("timeline" + getTimelineForYear(getOptionYear(questionString)) + "_line_background"));
    }

    protected void updateControlsAfterAnswPressed(String questionString, Integer currentQuestion) {
        int index = getQuestionNrInOrder().indexOf(currentQuestion);
        Table item = historyGameScreen.getRoot().findActor(getTimelineItemName(index));
        MyButton optBtn = historyGameScreen.getRoot().findActor(getOptionBtnName(index));
        optBtn.setDisabled(true);
        item.setBackground(getTimelineItemBackgr(questionString, index));
        historyGameScreen.goToNextQuestion();
        if (scoreLabel != null) {
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
        historyPreferencesService.setLevelLost(currentQuestion, getCampaignLevelEnum());
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

    protected Table createTitleTable() {
        Table table = new Table();
        epochNameLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText("0")
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f)).build());
        table.add(epochNameLabel);
        return table;
    }

    public Table createHeader() {
        Table table = new Table();
        scoreLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(historyPreferencesService.getLevelsWon(getCampaignLevelEnum()).size() + "")
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f)).build());
        float scoreIconDimen = ScreenDimensionsManager.getScreenWidthValue(10);
        float sideDimen = scoreIconDimen * 2;
        table.add().width(sideDimen).height(scoreIconDimen).padLeft(MainDimen.horizontal_general_margin.getDimen());
        table.add(createTitleTable()).width(ScreenDimensionsManager.getScreenWidth() - sideDimen * 2).height(ScreenDimensionsManager.getScreenHeightValue(5));
        Table scoreTable = new Table();
        scoreTable.add(scoreLabel).width(scoreIconDimen);
        Image image = GraphicUtils.getImage(HistorySpecificResource.score_icon);
        scoreTable.add(image).width(scoreIconDimen).padRight(MainDimen.horizontal_general_margin.getDimen() * 2).height(scoreIconDimen);
        table.add(scoreTable).width(sideDimen);
        table.setBackground(GraphicUtils.getNinePatch(MainResource.btn_menu_up));
        return table;
    }

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