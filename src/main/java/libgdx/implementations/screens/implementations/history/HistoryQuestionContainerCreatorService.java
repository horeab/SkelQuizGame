package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.history.HistoryCampaignLevelEnum;
import libgdx.implementations.history.HistoryPreferencesService;
import libgdx.implementations.history.HistorySpecificResource;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.utils.ScreenDimensionsManager;

public abstract class HistoryQuestionContainerCreatorService extends QuestionContainerCreatorService<HistoryGameService> {

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

    public float optionHeight() {
        return optionHeight;
    }

    protected abstract void initOptionHeight();

    public abstract HistoryCampaignLevelEnum getCampaignLevelEnum();


    public void refreshTitleTable(ScrollPane scrollPane) {
        if (scrollPane != null && epochNameLabel != null) {
            int option = getOptionDisplayed(scrollPane);
            int answYear = HistoryGameService.getOptionRawText(gameContext.getCurrentUserGameUser().getAllQuestionInfos().get(option).getQuestion().getQuestionString());
            int timelineForYear = getTimelineForYear(answYear);
            if (timelineDisplayed == null || timelineForYear != timelineDisplayed) {
                timelineDisplayed = timelineForYear;
                String text = SpecificPropertiesUtils.getText(Game.getInstance().getAppInfoService().getLanguage() + "_" + Game.getInstance().getGameIdPrefix() + "_timeline_" + timelineForYear);
                epochNameLabel.setText(text);
            }
        }
    }

    private int getOptionDisplayed(ScrollPane scrollPane) {
        int option = Math.round(scrollPane.getScrollY() / optionHeight());
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
        scoreTable.add(image).width(scoreIconDimen).padRight(MainDimen.horizontal_general_margin.getDimen()).height(scoreIconDimen);
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