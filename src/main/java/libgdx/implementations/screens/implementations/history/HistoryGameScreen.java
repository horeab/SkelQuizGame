package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.List;
import java.util.Random;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
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
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
import libgdx.implementations.skelgame.question.GameQuestionInfo;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.EnumUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

public class HistoryGameScreen extends GameScreen<HistoryScreenManager> {

    private int scrollPanePositionInit = 0;
    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;
    private ScrollPane scrollPane;
    private Integer scrollToOption;
    private int nrOfLevels;
    private float optionHeight;
    private GameContext gameContext;
    private MyWrappedLabel epochNameLabel;
    private Integer currentQuestion = 0;

    public HistoryGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        allCampaignLevelStores = campaignService.getFinishedCampaignLevels();
        this.gameContext = gameContext;
    }

    @Override
    public void buildStage() {
        nrOfLevels = ((CampaignLevel[]) EnumUtils.getValues(HistoryGame.getInstance().getSubGameDependencyManager().getCampaignLevelTypeEnum())).length;
        scrollToOption = 0;
        optionHeight = HistoryQuestionContainerCreatorService.optionHeight();
        scrollPane = new ScrollPane(createAllScroll());
        Table table = new Table();
        table.setFillParent(true);
        scrollPane.setScrollingDisabled(true, false);
        table.add(createQuestionTable()).height(ScreenDimensionsManager.getScreenHeightValue(10)).padBottom(MainDimen.vertical_general_margin.getDimen()).row();
        table.add(createAnswersTable()).width(ScreenDimensionsManager.getScreenWidth()).row();
        table.add(epcohTable()).height(ScreenDimensionsManager.getScreenHeightValue(5)).row();
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
            Drawable colorBackground = new Random().nextInt(100) > 80 ? GraphicUtils.getColorBackground(Color.ORANGE) : GraphicUtils.getColorBackground(Color.BLUE);
            answ.setBackground(colorBackground);
            table.add(answ).padLeft(padLeft).height(height).width(width);
            i++;
        }
        return table;
    }


    private Table epcohTable() {
        Table table = new Table();
        epochNameLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText("Pre-History")
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f)).build());
        table.add(epochNameLabel);
        addQuestion(table);
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
        Table table = new Table();
        MyWrappedLabel questionLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText(getQuestionText())
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f)).build());
        table.add(questionLabel);
        addQuestion(table);
        return table;
    }

    private void setEpochName() {
        if (scrollPane != null && epochNameLabel != null) {
            int option = getOptionDisplayed();
            epochNameLabel.setText(option + "");
        }
    }

    private int getOptionDisplayed() {
        int option = Math.round(scrollPane.getScrollY() / optionHeight);
        option = Math.max(option, 0);
        return option;
    }

    private void addQuestion(Table table) {
        table.setVisible(false);
        Utils.fadeInActor(table, 0.6f);
    }

    private void removeQuestion(Table table) {
        table.addAction(Actions.moveBy(ScreenDimensionsManager.getScreenWidth() / 2, 0, 1f));
        table.addAction(Actions.sequence(Actions.fadeOut(0.8f), Utils.createRemoveActorAction(table)));
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
