package libgdx.screens.implementations.judetelerom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import libgdx.campaign.*;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.labelimage.LabelImageConfigBuilder;
import libgdx.game.Game;
import libgdx.implementations.judetelerom.JudeteleRomCampaignLevelEnum;
import libgdx.implementations.judetelerom.JudeteleRomCategoryEnum;
import libgdx.implementations.judetelerom.JudeteleRomGame;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuestionCreator;
import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.hangman.HangmanScreenManager;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class JudeteleRomCampaignScreen extends AbstractScreen<HangmanScreenManager> {

    private int scrollPanePositionInit = 0;
    private CampaignService campaignService = new CampaignService();
    private List<CampaignStoreLevel> allCampaignLevelStores;
    private ScrollPane scrollPane;
    private Integer scrollToLevel;
    private float levelHeight;

    public JudeteleRomCampaignScreen() {
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
    }

    @Override
    public void buildStage() {
        final int maxOpenedLevel = allCampaignLevelStores.size();
        scrollToLevel = maxOpenedLevel < 3 ? 0 : maxOpenedLevel - 1;
        levelHeight = getLevelBtnHeight() + MainDimen.horizontal_general_margin.getDimen();

        scrollPane = new ScrollPane(createAllTable());
        scrollPane.setScrollingDisabled(true, false);
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
        Table table = new Table();
        table.setFillParent(true);
        table.add(scrollPane).expand();
        addActor(table);
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private Table createAllTable() {
        Table table = new Table();


        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontScale(FontManager.getBigFontDim()).setText(Game.getInstance().getAppInfoService().getAppName()).build())).pad(MainDimen.vertical_general_margin.getDimen()).colspan(2).row();
        for (final JudeteleRomCampaignLevelEnum campaignLevelEnum : JudeteleRomCampaignLevelEnum.values()) {
            float btnWidth = ScreenDimensionsManager.getScreenWidthValue(50);
            final Integer category = new CampaignLevelEnumService(campaignLevelEnum).getCategory();
            MyButton campaignBtn = new ButtonBuilder()
                    .setWrappedText(new LabelImageConfigBuilder()
                            .setText(new SpecificPropertiesUtils().getQuestionCampaignLabel(category))
                            .setFontScale(FontManager.getBigFontDim())
                            .setWrappedLineLabel(btnWidth / 1.1f))
                    .setButtonSkin(GameButtonSkin.HANGMAN_CATEG).build();
            final int maxOpenedLevel = allCampaignLevelStores.size();
            if (campaignLevelEnum.getIndex() >= maxOpenedLevel) {
                campaignBtn.setDisabled(true);
            }
            campaignBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    List<Question> questions = new QuestionCreator().getAllQuestionsOnLineNr(category);
                    GameContext gameContext = new GameContextService().createGameContext(questions.toArray(new Question[questions.size()]));
                    JudeteleRomGame.getInstance().getScreenManager().showCampaignGameScreen(gameContext, campaignLevelEnum);
                }
            });

            float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
            Table btnTable = new Table();

            btnTable.add(campaignBtn)
                    .pad(horizontalGeneralMarginDimen)
                    .height(getLevelBtnHeight())
                    .width(btnWidth);
            table.add(btnTable).expand().pad(horizontalGeneralMarginDimen);
            table.row();
        }

        if (campaignService.getFinishedCampaignLevels().size() == JudeteleRomCampaignLevelEnum.values().length) {
            new LevelFinishedPopup(this, SkelGameLabel.game_finished.getText()).addToPopupManager();
        }
        return table;
    }

    private float getLevelBtnHeight() {
        return ScreenDimensionsManager.getScreenHeightValue(43);
    }


    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        Utils.createChangeLangPopup();
//         scrollPanePositionInit needs to be used otherwise the scrollTo wont work
        if (scrollPane != null && scrollPanePositionInit < 2 && scrollToLevel != null) {
            scrollPane.setScrollY(scrollToLevel * levelHeight);
            scrollPanePositionInit++;
        }
    }
}
