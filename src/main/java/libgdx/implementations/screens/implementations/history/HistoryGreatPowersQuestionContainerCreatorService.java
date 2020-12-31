package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
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

public class HistoryGreatPowersQuestionContainerCreatorService extends HistoryQuestionContainerCreatorService<HistoryGreatPowersGameService> {

    private HistoryCampaignLevelEnum campaignLevelEnum;

    private Map<Integer, Integer> qNrMaxYear;

    public HistoryGreatPowersQuestionContainerCreatorService(GameContext gameContext, HistoryGameScreen abstractGameScreen, HistoryPreferencesService historyPreferencesService) {
        super(gameContext, abstractGameScreen,historyPreferencesService);
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
            Table answImg = createAnswImg(i, getPrefix());
            qTable.add(answImg).width(ScreenDimensionsManager.getScreenWidth() - optionWidth).center().height(optionBtnHeight);
            qTable.setName(getTimelineItemName(i));
            table.add(qTable).height(optionBtnHeight).row();
            i++;
        }
        return table;
    }

    @Override
    protected String getPrefix() {
        return "j";
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
        float yearWidth = btnWidth / 4f;
        FontColor fontColor = FontColor.BLACK;
        float minScale = 1.2f;
        float maxScale = 1.2f;
        int minLength = 9;
        MyButton btn = new ButtonBuilder()
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(minYear.length() >= minLength ? minScale : maxScale))
                .setWrappedText(minYear, yearWidth)
                .setFontColor(fontColor)
                .setButtonSkin(GameButtonSkin.HISTORY_GREAT_LEVEL_CORRECT)
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
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(maxYear.length() >= minLength ? minScale : maxScale)).build());
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
        new ActorAnimation(btn, getAbstractGameScreen()).animateZoomInZoomOut(0.01f);
        return btn;
    }

    @Override
    protected ButtonSkin getButtonSkin(Integer questionNr) {
        if (historyPreferencesService.getLevelsLost(getCampaignLevelEnum()).contains(questionNr)) {
            return GameButtonSkin.HISTORY_GREAT_LEVEL_WRONG;
        }
        return GameButtonSkin.HISTORY_GREAT_LEVEL_CORRECT;
    }

    @Override
    protected Drawable getTimelineItemBackgr(String questionString, Integer questionNr) {
        return GraphicUtils.getNinePatch(MainResource.transparent_background);
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