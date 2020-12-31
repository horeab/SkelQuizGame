package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.ArrayList;
import java.util.List;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.history.HistoryCampaignLevelEnum;
import libgdx.implementations.history.HistoryPreferencesService;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;

public class HistoryTimelineQuestionContainerCreatorService extends HistoryQuestionContainerCreatorService<HistoryTimelineGameService> {

    private List<Integer> questionNrInOrder;

    public HistoryTimelineQuestionContainerCreatorService(GameContext gameContext, HistoryGameScreen abstractGameScreen, HistoryPreferencesService historyPreferencesService) {
        super(gameContext, abstractGameScreen,historyPreferencesService);
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
//        float historyTimelineArrowWidth = GameButtonSize.HISTORY_TIMELINE_ARROW.getWidth();
        float historyTimelineArrowWidth = 0;
        for (GameQuestionInfo q : gameContext.getCurrentUserGameUser().getAllQuestionInfos()) {
            String questionString = q.getQuestion().getQuestionString();
            String optionText = getGameService().getOptionText(questionString);
            Table timeLineTable = new Table();
            Table qTable = new Table();
            Table optionBtn = createOptionBtn(optionText, i);
            Table leftContainer = new Table();
            Cell leftCell = leftContainer.add().height(optionBtn.getHeight()).width(optionBtn.getWidth());
            leftContainer.row();
            leftCell.setActor(optionBtn);
            qTable.add(leftContainer).width(optionSideWidth);
            qTable.add(timeLineTable).height(optionHeight).width(timelineLineWidth);
            timeLineTable.setBackground(getBackgroundForTimeline(questionString));
            timeLineTable.setName(getTimelineItemName(i));
            Table rightContainer = new Table();
            Cell rightArrowCell = rightContainer.add().width(historyTimelineArrowWidth);
            Cell rightCell = rightContainer.add().height(optionBtn.getHeight()).width(optionBtn.getWidth());
            rightContainer.row();
            rightArrowCell.reset();
            rightCell.setActor(createAnswImg(i, getPrefix()));
            qTable.add(rightContainer).width(optionSideWidth);
            qTable.setBackground(getTimelineItemBackgr(questionString, i));
            table.add(qTable).width(optionSideWidth * 2.2f).height(optionHeight).row();
            table.setBackground(GraphicUtils.getNinePatch(MainResource.transparent_background));
            i++;
        }
        return table;
    }


    @Override
    protected String getPrefix() {
        return "i";
    }
    @Override
    protected ButtonSkin getButtonSkin(Integer questionNr) {
        if (historyPreferencesService.getLevelsLost(getCampaignLevelEnum()).contains(questionNr)) {
            return GameButtonSkin.HISTORY_TIMELINE_LEVEL_WRONG;
        }
        return GameButtonSkin.HISTORY_TIMELINE_LEVEL_CORRECT;
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
        float fontScale = FontManager.calculateMultiplierStandardFontSize(optionText.length() >= 12 ? 0.9f : (optionText.length() >= 9 ? 1.1f : 1.2f));
        FontColor fontColor = FontColor.BLACK;
        MyButton btn = new ButtonBuilder()
                .setFontScale(fontScale)
                .setFontColor(fontColor)
                .setWrappedText(optionText, GameButtonSize.HISTORY_CLICK_ANSWER_OPTION.getWidth() / 1.4f)
                .setButtonSkin(GameButtonSkin.HISTORY_TIMELINE_LEVEL_CORRECT)
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
                    processWonQuestion(currentQuestion, getPrefix());
                } else {
                    processLostQuestion(currentQuestion);
                }
                showPopupAd(new Runnable() {
                    @Override
                    public void run() {
                        updateControlsAfterAnswPressed(questionString, currentQuestion);
                    }
                });
            }
        });
        btn.setTransform(true);
        new ActorAnimation(btn, getAbstractGameScreen()).animateZoomInZoomOut(0.05f);
        table.add(btn).width(btn.getWidth()).height(btn.getHeight());
        return btn;
    }

}