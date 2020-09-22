package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.List;
import java.util.Set;

import libgdx.campaign.CampaignLevel;
import libgdx.controls.MyTextField;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.popup.MyPopup;
import libgdx.controls.textfield.MyTextFieldBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.history.HistoryGame;
import libgdx.implementations.history.HistoryPreferencesService;
import libgdx.implementations.history.HistorySpecificResource;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.utils.EnumUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

public class HistoryGameScreen extends GameScreen<HistoryScreenManager> {

    private int scrollPanePositionInit = 0;
    private ScrollPane scrollPane;
    private Integer scrollToOption;
    private float optionHeight;
    private GameContext gameContext;
    private MyWrappedLabel epochNameLabel;
    private Integer currentQuestion = 0;
    private Integer timelineDisplayed = null;
    private MyWrappedLabel questionLabel;
    private HistoryPreferencesService historyPreferencesService = new HistoryPreferencesService();

    public HistoryGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        this.gameContext = gameContext;
    }

    private void goToNextQuestion() {
        removeQuestion(questionLabel);
        Set<Integer> allQPlayed = historyPreferencesService.getAllLevelsPlayed();
        for (int i = 0; i < gameContext.getCurrentUserGameUser().getAllQuestionInfos().size(); i++) {
            if (!allQPlayed.contains(i)) {
                currentQuestion = i;
                addQuestion(questionLabel);
                break;
            }
        }
    }

    private Table createHeader() {
        Table table = new Table();
        MyWrappedLabel score = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText("12")
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f)).build());
        float scoreIconDimen = ScreenDimensionsManager.getScreenWidthValue(10);
        float sideDimen = scoreIconDimen * 2;
        table.add().width(sideDimen).height(scoreIconDimen).padLeft(MainDimen.horizontal_general_margin.getDimen());
        table.add(epcohTable()).width(ScreenDimensionsManager.getScreenWidth() - sideDimen * 2).height(ScreenDimensionsManager.getScreenHeightValue(5));
        Table scoreTable = new Table();
        scoreTable.add(score).width(scoreIconDimen);
        Image image = GraphicUtils.getImage(HistorySpecificResource.score_icon);
        scoreTable.add(image).width(scoreIconDimen).padRight(MainDimen.horizontal_general_margin.getDimen()).height(scoreIconDimen);
        table.add(scoreTable).width(sideDimen);
        table.setBackground(GraphicUtils.getNinePatch(MainResource.btn_menu_up));
        return table;
    }

    @Override
    public void buildStage() {
        scrollToOption = 0;
        optionHeight = HistoryQuestionContainerCreatorService.optionHeight();
        scrollPane = new ScrollPane(createAllScroll());
        Table table = new Table();
        table.setFillParent(true);
        scrollPane.setScrollingDisabled(true, false);
        table.add(createAnswersTable()).width(ScreenDimensionsManager.getScreenWidth()).row();
        table.add(createHeader()).width(ScreenDimensionsManager.getScreenWidth()).row();
        table.add(createQuestionTable()).height(ScreenDimensionsManager.getScreenHeightValue(10)).padBottom(MainDimen.vertical_general_margin.getDimen()).row();
        goToNextQuestion();
        table.add(scrollPane).expand();
        addActor(table);
        new BackButtonBuilder().addHoverBackButton(this);
        table.setBackground(GraphicUtils.getNinePatch(MainResource.btn_menu_up));
    }

    private Table createAnswersTable() {
        Table table = new Table();
        List<GameQuestionInfo> allQuestionInfos = gameContext.getCurrentUserGameUser().getAllQuestionInfos();
        float height = ScreenDimensionsManager.getScreenHeightValue(2);
        float width = ScreenDimensionsManager.getScreenWidthValue(100f / allQuestionInfos.size());
        float padLeft = -MainDimen.horizontal_general_margin.getDimen() / 20;
        int i = 0;
        for (GameQuestionInfo q : allQuestionInfos) {
            Table answ = new Table();
            answ.setBackground(getBackgroundForTimeline(q.getQuestion().getQuestionString()));
            table.add(answ).padLeft(padLeft).height(height).width(width);
            i++;
        }
        return table;
    }

    public Drawable getBackgroundForTimeline(String questionString) {
        return GraphicUtils.getNinePatch(HistorySpecificResource.valueOf("timeline" + getTimelineForYear(HistoryGameService.getOptionRawText(questionString)) + "_line_background"));
    }

    private Table epcohTable() {
        Table table = new Table();
        epochNameLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText("0")
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f)).build());
        table.add(epochNameLabel);
        return table;
    }

    public Integer getCurrentQuestion() {
        return currentQuestion;
    }

    public GameQuestionInfo getCurrentGameQuestionInfo() {
        return gameContext.getCurrentUserGameUser().getAllQuestionInfos().get(currentQuestion);
    }

    private String getQuestionText() {
        return HistoryGameService.getQuestionText(getCurrentGameQuestionInfo().getQuestion().getQuestionString());
    }

    private Table createQuestionTable() {
       Table questionTable = new Table();
         questionLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText("0")
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f)).build());
        questionTable.add(questionLabel);
        return questionTable;
    }

    private void setEpochName() {
        if (scrollPane != null && epochNameLabel != null) {
            int option = getOptionDisplayed();
            int answYear = HistoryGameService.getOptionRawText(gameContext.getCurrentUserGameUser().getAllQuestionInfos().get(option).getQuestion().getQuestionString());
            int timelineForYear = getTimelineForYear(answYear);
            if (timelineDisplayed == null || timelineForYear != timelineDisplayed) {
                timelineDisplayed = timelineForYear;
                String text = SpecificPropertiesUtils.getText(Game.getInstance().getAppInfoService().getLanguage() + "_" + Game.getInstance().getGameIdPrefix() + "_timeline_" + timelineForYear);
                epochNameLabel.setText(text);
            }
        }
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

    private int getOptionDisplayed() {
        int option = Math.round(scrollPane.getScrollY() / optionHeight);
        option = Math.max(option, 0);
        option = Math.min(option, gameContext.getCurrentUserGameUser().getAllQuestionInfos().size());
        return option;
    }

    private void addQuestion(MyWrappedLabel label) {
        label.setText(getQuestionText());
        label.setVisible(false);
        Utils.fadeInActor(label, 0.6f);
    }

    private void removeQuestion(MyWrappedLabel label) {
        label.addAction(Actions.moveBy(ScreenDimensionsManager.getScreenWidth() / 2, 0, 1f));
        label.addAction(Actions.sequence(Actions.fadeOut(0.8f), Utils.createRemoveActorAction(label)));
    }

    private Table createAllScroll() {
        QuestionContainerCreatorService questionContainerCreatorService = new HistoryQuestionContainerCreatorService(gameContext, this);
        Table questionTable = questionContainerCreatorService.createQuestionTable();
        return questionTable;
    }

    @Override
    public void executeLevelFinished() {
    }

    @Override
    public void goToNextQuestionScreen() {
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        setEpochName();
//        createScrollTo();
//         scrollPanePositionInit needs to be used otherwise the scrollTo wont work
        if (scrollPane != null && scrollPanePositionInit < 2) {
            scrollPane.setScrollY((scrollToOption - 1) * optionHeight);
            scrollPanePositionInit++;
        }
    }

    private void createScrollTo() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ALT_LEFT)) {
            MyPopup popup = new MyPopup(Game.getInstance().getAbstractScreen()) {
                @Override
                protected void addButtons() {
                    final MyTextField myTextField = new MyTextFieldBuilder().build();
                    getButtonTable().add(myTextField).row();
                    MyButton changeLangBtn = new ButtonBuilder().setWrappedText("OK", MainDimen.horizontal_general_margin.getDimen() * 10)
                            .setDefaultButton()
                            .build();
                    changeLangBtn.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            scrollToOption = Integer.valueOf(myTextField.getTextField().getText());
                            scrollPanePositionInit = 0;
                        }
                    });
                    addButton(changeLangBtn);
                }

                @Override
                protected String getLabelText() {
                    return "";
                }
            };
            popup.addToPopupManager();
        }
    }

}
