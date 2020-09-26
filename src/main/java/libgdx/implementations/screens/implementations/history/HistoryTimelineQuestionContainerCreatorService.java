package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.history.HistoryCampaignLevelEnum;
import libgdx.implementations.history.HistorySpecificResource;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;

public class HistoryTimelineQuestionContainerCreatorService extends HistoryQuestionContainerCreatorService {

    public HistoryTimelineQuestionContainerCreatorService(GameContext gameContext, HistoryGameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
    }

    protected Table createOptionsTable() {
        Table table = new Table();
        float optionHeight = optionHeight();
        float optionSideWidth = ScreenDimensionsManager.getScreenWidthValue(45);
        float timelineLineWidth = ScreenDimensionsManager.getScreenWidthValue(3);
        int i = 0;
        float historyTimelineArrowWidth = GameButtonSize.HISTORY_TIMELINE_ARROW.getWidth();
        for (GameQuestionInfo q : gameContext.getCurrentUserGameUser().getAllQuestionInfos()) {
            String questionString = q.getQuestion().getQuestionString();
            String optionText = HistoryGameService.getOptionText(questionString);
            Table timeLineTable = new Table();
            Table qTable = new Table();
            Table optionBtn = createOptionBtn(optionText, i);
            Table leftContainer = new Table();
            Cell leftCell = leftContainer.add().height(optionBtn.getHeight()).width(optionBtn.getWidth());
            Cell leftArrowCell = leftContainer.add().width(historyTimelineArrowWidth);
            leftContainer.row();
            if (i % 2 == 0) {
                leftCell.setActor(optionBtn);
                leftArrowCell.setActor(createTimelineArrow());
            } else {
                leftArrowCell.reset();
                leftCell.setActor(createAnswImg(i));
            }
            qTable.add(leftContainer).width(optionSideWidth);
            qTable.add(timeLineTable).height(optionHeight).width(timelineLineWidth);
            timeLineTable.setBackground(getBackgroundForTimeline(questionString));
            timeLineTable.setName(getTimelineItemName(i));
            Table rightContainer = new Table();
            Cell rightArrowCell = rightContainer.add().width(historyTimelineArrowWidth);
            Cell rightCell = rightContainer.add().height(optionBtn.getHeight()).width(optionBtn.getWidth());
            rightContainer.row();
            if (i % 2 != 0) {
                rightArrowCell.setActor(createTimelineArrow());
                rightCell.setActor(optionBtn);
            } else {
                rightArrowCell.reset();
                rightCell.setActor(createAnswImg(i));
            }
            qTable.add(rightContainer).width(optionSideWidth);
            qTable.setBackground(getTimelineItemBackgr(questionString, i));
            table.add(qTable).width(optionSideWidth * 2).height(optionHeight).row();
            i++;
        }
        return table;
    }

    private Drawable getBackgroundForTimeline(String questionString) {
        return GraphicUtils.getNinePatch(HistorySpecificResource.valueOf("timeline" + getTimelineForYear(HistoryGameService.getOptionRawText(questionString)) + "_line_background"));
    }

    @Override
    public HistoryCampaignLevelEnum getCampaignLevelEnum() {
        return HistoryCampaignLevelEnum.LEVEL_0_0;
    }

    private String getTimelineItemName(int i) {
        return "timeLineTable" + i;
    }

    @Override
    protected void initOptionHeight() {
        optionHeight = ScreenDimensionsManager.getScreenHeightValue(12);
    }

    private Table createAnswImg(int index) {
        Table table = new Table();
        Image img = createOptImg(index);
        table.add(img);
        table.add(img).width(img.getWidth()).height(img.getHeight());
        table.setName(getOptionImageName(index));
        return table;
    }

    private Image createOptImg(int index) {
        Res res = HistorySpecificResource.timeline_opt_unknown;
        try {
            if (historyPreferencesService.getAllLevelsPlayed(getCampaignLevelEnum()).contains(index)) {
                res = HistorySpecificResource.valueOf("i" + index);
            }
        } catch (Exception ignore) {
        }
        Image image = GraphicUtils.getImage(res);
        image.setWidth(GameButtonSize.HISTORY_ANSW_IMG.getWidth());
        image.setHeight(GameButtonSize.HISTORY_ANSW_IMG.getHeight());
        return image;
    }

    private Image createTimelineArrow() {
        Image image = GraphicUtils.getImage(HistorySpecificResource.arrow_left);
        image.setWidth(GameButtonSize.HISTORY_TIMELINE_ARROW.getWidth());
        image.setHeight(GameButtonSize.HISTORY_TIMELINE_ARROW.getHeight());
        return image;
    }

    private Table createOptionBtn(String optionText, int index) {
        Table table = new Table();
        MyButton btn = new ButtonBuilder()
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f))
                .setWrappedText(optionText, GameButtonSize.HISTORY_CLICK_ANSWER_OPTION.getWidth())
                .setFontColor(FontColor.BLACK)
                .setButtonSkin(MainButtonSkin.DEFAULT)
                .setDisabled(historyPreferencesService.getAllLevelsPlayed(getCampaignLevelEnum()).contains(index))
                .setFixedButtonSize(GameButtonSize.HISTORY_CLICK_ANSWER_OPTION)
                .setButtonName(getOptionBtnName(index))
                .build();
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String questionString = historyGameScreen.getCurrentGameQuestionInfo().getQuestion().getQuestionString();
                String currentQuestionCorrectAnswer = HistoryGameService.getOptionText(questionString);
                Integer currentQuestion = historyGameScreen.getCurrentQuestion();
                if (currentQuestionCorrectAnswer.equals(optionText)) {
                    Table imgTable = historyGameScreen.getRoot().findActor(getOptionImageName(currentQuestion));
                    Actor oldImg = imgTable.getChildren().get(0);
                    oldImg.addAction(Actions.sequence(Actions.fadeOut(0.2f), Utils.createRemoveActorAction(oldImg)));
                    Image img = createOptImg(currentQuestion);
                    imgTable.clear();
                    img.setVisible(false);
                    imgTable.add(img).width(img.getWidth()).height(img.getHeight());
                    new ActorAnimation(img, historyGameScreen).animateFastFadeIn();
                    historyPreferencesService.setLevelWon(currentQuestion, getCampaignLevelEnum());
                } else {
                    historyPreferencesService.setLevelLost(currentQuestion, getCampaignLevelEnum());
                }
                Table item = historyGameScreen.getRoot().findActor(getTimelineItemName(currentQuestion));
                MyButton optBtn = historyGameScreen.getRoot().findActor(getOptionBtnName(currentQuestion));
                optBtn.setDisabled(true);
                item.setBackground(getTimelineItemBackgr(questionString, currentQuestion));
                historyGameScreen.goToNextQuestion();
                if (scoreLabel != null) {
                    scoreLabel.setText(historyPreferencesService.getLevelsWon(getCampaignLevelEnum()).size() + "");
                }
            }
        });
        table.add(btn).width(btn.getWidth()).height(btn.getHeight());
        return btn;
    }

    private String getOptionImageName(Integer currentQuestion) {
        return "optImage" + currentQuestion;
    }

    private String getOptionBtnName(Integer currentQuestion) {
        return "optBtn" + currentQuestion;
    }

    private Drawable getTimelineItemBackgr(String questionString, Integer questionNr) {
        Res res;
        if (historyPreferencesService.getLevelsLost(getCampaignLevelEnum()).contains(questionNr)) {
            res = HistorySpecificResource.timeline_opt_wrong;
        } else if (historyPreferencesService.getLevelsWon(getCampaignLevelEnum()).contains(questionNr)) {
            res = HistorySpecificResource.timeline_opt_correct;
        } else {
            res = HistorySpecificResource.valueOf("timeline" + getTimelineForYear(HistoryGameService.getOptionRawText(questionString)) + "_opt_background");
        }
        return GraphicUtils.getNinePatch(res);
    }


}