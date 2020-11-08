package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.history.HistoryCampaignLevelEnum;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;

public class HistoryGreatPowersQuestionContainerCreatorService extends HistoryQuestionContainerCreatorService<HistoryGreatPowersGameService> {

    private HistoryCampaignLevelEnum campaignLevelEnum;

    private Map<Integer, Integer> qNrMaxYear;

    public HistoryGreatPowersQuestionContainerCreatorService(GameContext gameContext, HistoryGameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
        this.campaignLevelEnum = HistoryCampaignLevelEnum.LEVEL_0_1;
        initQuestionNrMaxYear();
    }

    protected Table createOptionsTable() {
        Table table = new Table();
        float optionBtnHeight = getOptionHeight();
        float optionWidth = ScreenDimensionsManager.getScreenWidthValue(70);
        int i = 0;
        for (Integer qNr : qNrMaxYear.keySet()) {
            Pair<String, String> years = getGameService().getOptionText(gameContext.getCurrentUserGameUser().getGameQuestionInfo(i).getQuestion().getQuestionString());
            Table optionBtn = createOptionBtn(years.getLeft(), years.getRight(), i, optionWidth);
            Table qTable = new Table();
            qTable.add(optionBtn).width(optionWidth).height(optionBtnHeight);
            Table answImg = createAnswImg(i, "j");
            qTable.add(answImg).width(ScreenDimensionsManager.getScreenWidth() - optionWidth).center().height(optionBtnHeight);
            qTable.setName(getTimelineItemName(i));
            table.add(qTable).height(optionBtnHeight).row();
            i++;
        }
        return table;
    }

    protected List<Integer> getQuestionNrInOrder() {
        return new ArrayList<>(qNrMaxYear.keySet());
    }

    private void initQuestionNrMaxYear() {
        int i = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for (GameQuestionInfo q : gameContext.getCurrentUserGameUser().getAllQuestionInfos()) {
            map.put(i, getGameService().getOptionRawText(q.getQuestion().getQuestionString()).getRight());
            i++;
        }
        qNrMaxYear = map;
    }

    @Override
    protected HistoryGreatPowersGameService getGameService() {
        return new HistoryGreatPowersGameService(gameContext.getQuestion());
    }

    @Override
    public HistoryCampaignLevelEnum getCampaignLevelEnum() {
        return HistoryCampaignLevelEnum.LEVEL_0_1;
    }

    private Table createOptionBtn(String minYear, String maxYear, int index, float btnWidth) {
        Table table = new Table();
        float yearWidth = btnWidth / 2.5f;
        float fontScale = FontManager.calculateMultiplierStandardFontSize(1.2f);
        FontColor fontColor = FontColor.BLACK;
        MyButton btn = new ButtonBuilder()
                .setFontScale(fontScale)
                .setWrappedText(minYear, yearWidth)
                .setFontColor(fontColor)
                .setButtonSkin(MainButtonSkin.DEFAULT)
                .setDisabled(historyPreferencesService.getAllLevelsPlayed(campaignLevelEnum).contains(index))
                .setButtonName(getOptionBtnName(index))
                .build();
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });
        MyWrappedLabel questionLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(maxYear)
                .setWrappedLineLabel(yearWidth)
                .setFontColor(fontColor)
                .setFontScale(fontScale).build());
        ((Table) btn.getCenterRow().getChildren().get(0))
                .add(createTimelineArrow(true)).growX();
        ((Table) btn.getCenterRow().getChildren().get(0)).add(questionLabel).width(yearWidth);
        table.add(btn).width(btn.getWidth()).height(btn.getHeight());
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Integer currentQuestion = historyGameScreen.getCurrentQuestion();
                String questionString = historyGameScreen.getCurrentGameQuestionInfo().getQuestion().getQuestionString();
                Pair<String, String> optionText = getGameService().getOptionText(questionString);
                String cqMinY = optionText.getLeft();
                String cqMaxY = optionText.getRight();
                if (minYear.equals(cqMinY) && maxYear.equals(cqMaxY)) {
                    processWonQuestion(currentQuestion, "j");
                } else {
                    processLostQuestion(currentQuestion);
                }
                updateControlsAfterAnswPressed(questionString, currentQuestion);
            }
        });
        return btn;
    }

    @Override
    protected int getOptionYear(String qString) {
        return getGameService().getOptionRawText(qString).getRight();
    }

    @Override
    protected void initOptionHeight() {
        optionHeight = GameButtonSize.HISTORY_GREATPOWERS_ANSW_IMG.getHeight();
    }

    @Override
    protected float getAnswerImgSideDimen() {
        return GameButtonSize.HISTORY_GREATPOWERS_ANSW_IMG.getHeight();
    }

    public int getIncrementForYear(int year) {
        if (year > 1900) {
            return 5;
        } else if (year > 1800) {
            return 20;
        } else if (year > 1500) {
            return 30;
        } else if (year > 1000) {
            return 50;
        } else if (year > 0) {
            return 100;
        } else {
            return 250;
        }
    }


}