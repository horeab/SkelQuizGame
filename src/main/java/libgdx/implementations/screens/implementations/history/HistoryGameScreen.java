package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import libgdx.campaign.CampaignLevel;
import libgdx.constants.Contrast;
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
import libgdx.implementations.history.HistoryCampaignLevelEnum;
import libgdx.implementations.history.HistoryCategoryEnum;
import libgdx.implementations.history.HistoryPreferencesService;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameLabel;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;

public class HistoryGameScreen extends GameScreen<HistoryScreenManager> {

    private int scrollPanePositionInit = 0;
    private ScrollPane scrollPane;
    private Integer scrollToOption;
    private float optionHeight;
    private GameContext gameContext;
    private Integer firstOpenQuestionIndex = 0;
    private Integer currentQuestion = 0;
    private Table questionTable;
    private HistoryPreferencesService historyPreferencesService = new HistoryPreferencesService();
    private HistoryQuestionContainerCreatorService questionContainerCreatorService;

    public HistoryGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        this.gameContext = gameContext;
        if (gameContext.getQuestion().getQuestionCategory() == HistoryCategoryEnum.cat0) {
            questionContainerCreatorService = new HistoryTimelineQuestionContainerCreatorService(gameContext, this);
        } else {
            questionContainerCreatorService = new HistoryGreatPowersQuestionContainerCreatorService(gameContext, this);
        }
        Collections.reverse(gameContext.getCurrentUserGameUser().getAllQuestionInfos());
        initNextQuestion();
    }

    private Integer getRandomNextQuestion() {
        int nr = new Random().nextInt(5) + firstOpenQuestionIndex;
        int size = questionContainerCreatorService.getQuestionNrInOrder().size();
        return ((List<Integer>) questionContainerCreatorService.getQuestionNrInOrder()).get(Math.min(nr, size - 1));
    }

    public void goToNextQuestion() {
        if (isGameOver()) {
            processGameOver();
        }
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
                    addQuestionText();
                }
            })));
        } else {
            addQuestionText();
        }
    }

    private boolean isGameOver() {
        return historyPreferencesService.getAllLevelsPlayed(questionContainerCreatorService.getCampaignLevelEnum()).size() ==
                questionContainerCreatorService.getQuestionNrInOrder().size();
    }

    private void processGameOver() {
        new MyPopup<AbstractScreen, HistoryScreenManager>(getAbstractScreen()) {
            @Override
            protected String getLabelText() {
                String text = "";
                HistoryCampaignLevelEnum campaignLevelEnum = questionContainerCreatorService.getCampaignLevelEnum();
                if (historyPreferencesService.isHighScore(campaignLevelEnum)) {
                    text = MainGameLabel.l_congratulations.getText();
                    text = text + "\n";
                    text = text + MainGameLabel.l_highscore_record.getText();
                    text = text + "\n";
                    text = text + "\n";
                }
                text = text + MainGameLabel.l_score.getText(
                        historyPreferencesService.getLevelsWon(campaignLevelEnum).size() + ""
                );
                return text;
            }

            @Override
            protected void addButtons() {
                MyButton playAgain = new ButtonBuilder()
                        .setFontColor(FontColor.BLACK)
                        .setContrast(Contrast.LIGHT)
                        .setDefaultButton().setText(SkelGameLabel.play_again.getText()).build();
                playAgain.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        HistoryCampaignScreen.goToLevel(questionContainerCreatorService.getCampaignLevelEnum(), historyPreferencesService, screenManager);
                    }
                });
                addButton(playAgain);
                MyButton campaignScreenBtn = new ButtonBuilder().setDefaultButton()
                        .setContrast(Contrast.LIGHT)
                        .setFontColor(FontColor.BLACK)
                        .setText(SkelGameLabel.go_back.getText()).build();
                addButton(campaignScreenBtn);
                campaignScreenBtn.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        screenManager.showMainScreen();
                    }
                });
            }

            @Override
            public void hide() {
                super.hide();
                screenManager.showMainScreen();
            }
        }.addToPopupManager();
    }

    private void initNextQuestion() {
        Set<Integer> allQPlayed = historyPreferencesService.getAllLevelsPlayed(questionContainerCreatorService.getCampaignLevelEnum());
        for (Integer i : (List<Integer>) questionContainerCreatorService.getQuestionNrInOrder()) {
            if (!allQPlayed.contains(i)) {
                firstOpenQuestionIndex = questionContainerCreatorService.getQuestionNrInOrder().indexOf(i);
                currentQuestion = getRandomNextQuestion();
                while (allQPlayed.contains(currentQuestion)) {
                    currentQuestion = getRandomNextQuestion();
                }
                addAction(Actions.sequence(Actions.delay(1f), Utils.createRunnableAction(new Runnable() {
                    @Override
                    public void run() {
                        scrollToOption = firstOpenQuestionIndex;
                        scrollPanePositionInit = 0;
                    }
                })));
                break;
            }
        }
    }

    @Override
    public void buildStage() {
        scrollToOption = 0;
        scrollPane = new ScrollPane(questionContainerCreatorService.createQuestionTable());
        optionHeight = questionContainerCreatorService.getOptionHeight();
        Table table = new Table();
        table.setFillParent(true);
        scrollPane.setScrollingDisabled(true, false);
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
        return gameContext.getCurrentUserGameUser().getAllQuestionInfos().get(questionContainerCreatorService.getQuestionNrInOrder().indexOf(currentQuestion));
    }

    private void addQuestionText() {
        MyWrappedLabel questionLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(questionContainerCreatorService.getGameService().getQuestionText(getCurrentGameQuestionInfo().getQuestion().getQuestionString()))
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f)).build());
        questionTable.add(questionLabel);
        questionLabel.setVisible(false);
        Utils.fadeInActor(questionLabel, 0.1f);
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
            questionContainerCreatorService.refreshTitleTable(scrollPane);
        }
        createScrollTo();
//         scrollPanePositionInit needs to be used otherwise the scrollTo wont work
        if (scrollPane != null && scrollPanePositionInit < 2) {
            scrollPane.setScrollY((scrollToOption) * optionHeight);
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
