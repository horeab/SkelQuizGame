package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.List;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.implementations.history.HistoryCampaignLevelEnum;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

public class HistoryTimelineQuestionContainerCreatorService extends HistoryQuestionContainerCreatorService<HistoryTimelineGameService> {

    private List<Integer> questionNrInOrder;

    public HistoryTimelineQuestionContainerCreatorService(GameContext gameContext, HistoryGameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
        initQuestionNrInOrder();
    }

    private void initQuestionNrInOrder() {
        List<Integer> qNr = new ArrayList<>();
        for (int i = 0; i < gameContext.getCurrentUserGameUser().getAllQuestionInfos().size(); i++) {
            qNr.add(i);
        }
        questionNrInOrder = qNr;
    }

    protected Table createOptionsTable() {
        Table table = new Table();
        float optionHeight = getOptionHeight();
        float optionSideWidth = ScreenDimensionsManager.getScreenWidthValue(45);
        float timelineLineWidth = ScreenDimensionsManager.getScreenWidthValue(3);
        int i = 0;
        float historyTimelineArrowWidth = GameButtonSize.HISTORY_TIMELINE_ARROW.getWidth();
        for (GameQuestionInfo q : gameContext.getCurrentUserGameUser().getAllQuestionInfos()) {
            String questionString = q.getQuestion().getQuestionString();
            String optionText = getGameService().getOptionText(questionString);
            Table timeLineTable = new Table();
            Table qTable = new Table();
            Table optionBtn = createOptionBtn(optionText, i);
            Table leftContainer = new Table();
            Cell leftCell = leftContainer.add().height(optionBtn.getHeight()).width(optionBtn.getWidth());
            Cell leftArrowCell = leftContainer.add().width(historyTimelineArrowWidth);
            leftContainer.row();
            if (i % 2 == 0) {
                leftCell.setActor(optionBtn);
                leftArrowCell.setActor(createTimelineArrow(true));
            } else {
                leftArrowCell.reset();
                leftCell.setActor(createAnswImg(i, "i"));
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
                rightArrowCell.setActor(createTimelineArrow(false));
                rightCell.setActor(optionBtn);
            } else {
                rightArrowCell.reset();
                rightCell.setActor(createAnswImg(i, "i"));
            }
            qTable.add(rightContainer).width(optionSideWidth);
            qTable.setBackground(getTimelineItemBackgr(questionString, i));
            table.add(qTable).width(optionSideWidth * 2).height(optionHeight).row();
            i++;
        }
        return table;
    }

    protected List<Integer> getQuestionNrInOrder() {
        return questionNrInOrder;
    }

    @Override
    protected float getAnswerImgSideDimen() {
        return GameButtonSize.HISTORY_TIMELINE_ANSW_IMG.getHeight();
    }

    @Override
    protected int getOptionYear(String qString) {
        return getGameService().getOptionRawText(qString);
    }

    @Override
    protected HistoryTimelineGameService getGameService() {
        return new HistoryTimelineGameService(gameContext.getQuestion());
    }

    @Override
    public HistoryCampaignLevelEnum getCampaignLevelEnum() {
        return HistoryCampaignLevelEnum.LEVEL_0_0;
    }

    @Override
    protected void initOptionHeight() {
        optionHeight = ScreenDimensionsManager.getScreenHeightValue(15);
    }

    private Table createOptionBtn(String optionText, int index) {
        Table table = new Table();
        float mult = optionText.length() > 13 ? 1.0f : 1.2f;
        MyButton btn = new ButtonBuilder()
                .setFontConfig(new FontConfig(FontColor.BLACK.getColor(), FontColor.GREEN.getColor(),
                        FontConfig.FONT_SIZE * mult, 3f))
                .setWrappedText(optionText, GameButtonSize.HISTORY_CLICK_ANSWER_OPTION.getWidth())
                .setButtonSkin(GameButtonSkin.HISTORY_TIMELINE_LEVEL)
                .setDisabled(historyPreferencesService.getAllLevelsPlayed(getCampaignLevelEnum()).contains(index))
                .setFixedButtonSize(GameButtonSize.HISTORY_CLICK_ANSWER_OPTION)
                .setButtonName(getOptionBtnName(index))
                .build();
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String questionString = historyGameScreen.getCurrentGameQuestionInfo().getQuestion().getQuestionString();
                String currentQuestionCorrectAnswer = getGameService().getOptionText(questionString);
                Integer currentQuestion = historyGameScreen.getCurrentQuestion();
                if (currentQuestionCorrectAnswer.equals(optionText)) {
                    processWonQuestion(currentQuestion, "i");
                } else {
                    processLostQuestion(currentQuestion);
                }
                updateControlsAfterAnswPressed(questionString, currentQuestion);
            }
        });
        table.add(btn).width(btn.getWidth()).height(btn.getHeight());
        return btn;
    }


}