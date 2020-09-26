package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.implementations.history.HistoryCampaignLevelEnum;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;

public class HistoryGreatPowersQuestionContainerCreatorService extends HistoryQuestionContainerCreatorService {

    private HistoryCampaignLevelEnum campaignLevelEnum;

    public HistoryGreatPowersQuestionContainerCreatorService(GameContext gameContext, HistoryGameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
        this.campaignLevelEnum = HistoryCampaignLevelEnum.LEVEL_0_1;
    }

    protected Table createOptionsTable() {
        Table table = new Table();
        float optionHeight = optionHeight(1920, 1934);
        float optionSideWidth = ScreenDimensionsManager.getScreenWidthValue(45);
        int i = 0;
        int optionsPerRow = 4;
        float optionWidth = ScreenDimensionsManager.getScreenWidthValue(100 / optionsPerRow - 2);
        for (GameQuestionInfo q : gameContext.getCurrentUserGameUser().getAllQuestionInfos()) {
            Table optionBtn = createOptionBtn("1923\n-\n1935", i);
            table.add(optionBtn).width(optionWidth).height(optionHeight);
            i++;
            if (i % optionsPerRow == 0) {
                table.row();
            }
        }
        return table;
    }

    @Override
    public HistoryCampaignLevelEnum getCampaignLevelEnum() {
        return HistoryCampaignLevelEnum.LEVEL_0_1;
    }

    private Table createOptionBtn(String optionText, int index) {
        Table table = new Table();
        MyButton btn = new ButtonBuilder()
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f))
                .setWrappedText(optionText, GameButtonSize.HISTORY_CLICK_ANSWER_OPTION.getWidth())
                .setFontColor(FontColor.BLACK)
                .setButtonSkin(MainButtonSkin.DEFAULT)
                .setDisabled(historyPreferencesService.getAllLevelsPlayed(campaignLevelEnum).contains(index))
                .setButtonName(getOptionBtnName(index))
                .build();
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });
        btn.getCenterRow().setFillParent(true);
        ((Table) btn.getCenterRow().getChildren().get(0)).getCells().get(0).padTop(MainDimen.vertical_general_margin.getDimen());
        table.add(btn).width(btn.getWidth()).height(btn.getHeight());
        return btn;
    }


    private String getOptionBtnName(Integer currentQuestion) {
        return "optBtn" + currentQuestion;
    }

    private String getTimelineItemName(int i) {
        return "timeLineTable" + i;
    }

    @Override
    protected void initOptionHeight() {
        optionHeight = ScreenDimensionsManager.getScreenHeightValue(1);
    }

    public float optionHeight(int minYear, int maxYear) {
        int i1 = getIncrementForYear(minYear);
        int i2 = getIncrementForYear(maxYear);

        return ScreenDimensionsManager.getScreenHeightValue(40);
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