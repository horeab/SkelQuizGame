package libgdx.implementations.screens.implementations.history;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import java.util.List;

import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.history.HistoryGame;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.QuestionContainerCreatorService;
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
    private Integer scrollToLevel;
    private int nrOfLevels;
    private float levelHeight;
    private GameContext gameContext;

    public HistoryGameScreen(GameContext gameContext, CampaignLevel campaignLevel) {
        super(gameContext);
        allCampaignLevelStores = campaignService.getFinishedCampaignLevels();
        this.gameContext = gameContext;
    }

    @Override
    public void buildStage() {
        nrOfLevels = ((CampaignLevel[]) EnumUtils.getValues(HistoryGame.getInstance().getSubGameDependencyManager().getCampaignLevelTypeEnum())).length;
        scrollToLevel = allCampaignLevelStores.size();
        levelHeight = MainDimen.vertical_general_margin.getDimen() * 18;
        scrollPane = new ScrollPane(createAllScroll());
        Table table = new Table();
        table.setFillParent(true);
        scrollPane.setScrollingDisabled(true, false);
        float qLabelHeight = ScreenDimensionsManager.getScreenHeightValue(20);
        table.add(createQuestionTable()).height(qLabelHeight).row();
        table.add(scrollPane).expand();
        addActor(table);
        new BackButtonBuilder().addHoverBackButton(this);
        table.setBackground(GraphicUtils.getNinePatch(MainResource.btn_menu_up));
    }

    private Table createQuestionTable() {
        Table table = new Table();
        MyWrappedLabel questionLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setText("The first dinosaur was found in Jsaosn asefpkm asdfma sdkmfaoksmfokawef kamsfk asmd wapmfpasdf sadfm?")
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(1.2f)).build());
        table.add(questionLabel);
        addQuestion(table);
        return table;
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
//         scrollPanePositionInit needs to be used otherwise the scrollTo wont work
        if (scrollPane != null && scrollPanePositionInit < 2) {
            scrollPane.setScrollY((scrollToLevel / 2) * levelHeight);
            scrollPanePositionInit++;
        }
    }

}
