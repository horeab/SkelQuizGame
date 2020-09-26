package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Random;
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
import libgdx.implementations.history.HistoryPreferencesService;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

public class HistoryGameScreen extends GameScreen<HistoryScreenManager> {

    private int scrollPanePositionInit = 0;
    private ScrollPane scrollPane;
    private Integer scrollToOption;
    private float optionHeight;
    private GameContext gameContext;
    private Integer firstOpenQuestion = 0;
    private Integer currentQuestion = 0;
    private Table questionTable;
    private HistoryPreferencesService historyPreferencesService = new HistoryPreferencesService();
    private HistoryQuestionContainerCreatorService questionContainerCreatorService;

    public HistoryGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        this.gameContext = gameContext;
        questionContainerCreatorService = new HistoryGreatPowersQuestionContainerCreatorService(gameContext, this);
        initNextQuestion();
    }

    private Integer getRandomNextQuestion() {
        int nr = new Random().nextInt(5) + firstOpenQuestion;
        int size = gameContext.getCurrentUserGameUser().getAllQuestionInfos().size();
        return Math.min(nr, size - 1);
    }

    public void goToNextQuestion() {
        initNextQuestion();
        if (questionTable.getChildren().size > 0) {
            Actor label = questionTable.getChildren().get(0);
            float qMoveDuration = 0.1f;
            float qFadeOutDuration = 0.1f;
            label.addAction(Actions.moveBy(ScreenDimensionsManager.getScreenWidth() / 2, 0, qMoveDuration));
            label.addAction(Actions.sequence(Actions.fadeOut(qFadeOutDuration), Utils.createRemoveActorAction(label)));
            addAction(Actions.sequence(Actions.delay(Math.max(qFadeOutDuration, qMoveDuration)), Utils.createRunnableAction(new Runnable() {
                @Override
                public void run() {
                    addLabelToQuestionTable();
                }
            })));
        } else {
            addLabelToQuestionTable();
        }
    }

    private void initNextQuestion() {
        Set<Integer> allQPlayed = historyPreferencesService.getAllLevelsPlayed(questionContainerCreatorService.getCampaignLevelEnum());
        for (int i = 0; i < gameContext.getCurrentUserGameUser().getAllQuestionInfos().size(); i++) {
            if (!allQPlayed.contains(i)) {
                firstOpenQuestion = i;
                currentQuestion = getRandomNextQuestion();
                while (allQPlayed.contains(currentQuestion)) {
                    currentQuestion = getRandomNextQuestion();
                }
                scrollToOption = firstOpenQuestion;
                scrollPanePositionInit = 0;
                break;
            }
        }
    }


    @Override
    public void buildStage() {
        scrollToOption = 0;
        scrollPane = new ScrollPane(createAllScroll());
        optionHeight = questionContainerCreatorService.optionHeight();
        Table table = new Table();
        table.setFillParent(true);
        scrollPane.setScrollingDisabled(true, false);
//        table.add(createAnswersTable()).width(ScreenDimensionsManager.getScreenWidth()).row();
        table.add(questionContainerCreatorService.createHeader()).width(ScreenDimensionsManager.getScreenWidth()).row();
        questionTable = new Table();
        table.add(questionTable).height(ScreenDimensionsManager.getScreenHeightValue(10)).padBottom(MainDimen.vertical_general_margin.getDimen()).row();
        goToNextQuestion();
        table.add(scrollPane).expand();
        addActor(table);
        new BackButtonBuilder().addHoverBackButton(this);
        table.setBackground(GraphicUtils.getNinePatch(MainResource.btn_menu_up));
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

    private void addLabelToQuestionTable() {
        MyWrappedLabel questionLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(getQuestionText())
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f)).build());
        questionTable.add(questionLabel);
        questionLabel.setVisible(false);
        Utils.fadeInActor(questionLabel, 0.1f);
    }

    private Table createAllScroll() {
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
        if (questionContainerCreatorService != null) {
//            questionContainerCreatorService.refreshTitleTable(scrollPane);
        }
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
