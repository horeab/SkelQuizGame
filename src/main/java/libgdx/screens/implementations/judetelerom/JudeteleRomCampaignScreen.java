package libgdx.screens.implementations.judetelerom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignService;
import libgdx.campaign.CampaignStoreLevel;
import libgdx.campaign.CampaignStoreService;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MainButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.judetelerom.JudeteleRomCampaignLevelEnum;
import libgdx.implementations.judetelerom.JudeteleRomCategoryEnum;
import libgdx.implementations.judetelerom.JudeteleRomGame;
import libgdx.implementations.judetelerom.JudeteleRomSpecificResource;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.SkelGameLabel;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuestionCreator;
import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;
import libgdx.utils.model.FontConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JudeteleRomCampaignScreen extends AbstractScreen<JudeteleRomScreenManager> {

    private JudeteContainers judeteContainers = new JudeteContainers();
    private CampaignService campaignService = new CampaignService();
    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private List<CampaignStoreLevel> allCampaignLevelStores;
    private ScrollPane scrollPane;
    private float levelHeight;

    public JudeteleRomCampaignScreen() {
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
    }

    @Override
    public void buildStage() {
        levelHeight = getLevelBtnHeight() + MainDimen.horizontal_general_margin.getDimen();

        scrollPane = new ScrollPane(createAllTable());
        scrollPane.setScrollingDisabled(true, false);
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
        Table table = new Table();
        table.setFillParent(true);
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        FontColor.WHITE.getColor(),
                        FontColor.BLACK.getColor(),
                        2f))
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(2.2f))
                .setText(Game.getInstance().getAppInfoService().getAppName()).build());

        table.add(titleLabel).padTop(MainDimen.vertical_general_margin.getDimen() * 2).colspan(2).row();
        table.add(judeteContainers.createAllJudeteFound()).pad(MainDimen.vertical_general_margin.getDimen()).colspan(2).row();
        MyButton startGameBtn = createStartGameBtn();
        table.add(startGameBtn).pad(MainDimen.vertical_general_margin.getDimen()).width(ScreenDimensionsManager.getScreenWidthValue(50)).height(ScreenDimensionsManager.getScreenHeightValue(10)).row();
        table.add(scrollPane).expand();
        addActor(table);
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private MyButton createStartGameBtn() {
        MyButton button = new ButtonBuilder().setSingleLineText(SpecificPropertiesUtils.getText("ro_judetelerom_start_game"), FontManager.getNormalFontDim()).setButtonSkin(MainButtonSkin.DEFAULT).build();
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                List<Question> questions = new ArrayList<>(new QuestionCreator().getAllQuestions());
                List<Question> notPlayedQuestions = new ArrayList<>();
                for (Question question : questions) {
                    if (!campaignStoreService.isQuestionAlreadyPlayed(JudeteContainers.getQuestionId(question.getQuestionLineInQuestionFile(), question.getQuestionCategory(), question.getQuestionDifficultyLevel()))) {
                        notPlayedQuestions.add(question);
                    }
                }
                Collections.shuffle(notPlayedQuestions);
                GameContext gameContext = new GameContextService().createGameContext(notPlayedQuestions.toArray(new Question[notPlayedQuestions.size()]));
                JudeteleRomGame.getInstance().getScreenManager().showCampaignGameScreen(gameContext, null);
            }
        });
        return button;
    }

    private Table createAllTable() {
        Table table = new Table();
        int rowIndex = 1;
        JudeteleRomCampaignLevelEnum[] allJudete = JudeteleRomCampaignLevelEnum.values();
        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        for (final JudeteleRomCampaignLevelEnum campaignLevelEnum : allJudete) {
            float btnWidth = ScreenDimensionsManager.getScreenWidthValue(40);
            final Integer category = new CampaignLevelEnumService(campaignLevelEnum).getCategory();
            Table judTable = new Table();
            MyWrappedLabel judLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setWrappedLineLabel(btnWidth / 1.1f).setFontScale(FontManager.getNormalFontDim()).setText(new SpecificPropertiesUtils().getQuestionCampaignLabel(category)).build());
            judTable.add(judLabel)
                    .colspan(5)
                    .height(getLevelBtnHeight())
                    .width(btnWidth).row();
            int correctAnswersForJudet = JudeteContainers.getTotalCorrectAnswersForJudet(category);

            if (correctAnswersForJudet == JudeteleRomCategoryEnum.values().length) {
                judTable.setBackground(GraphicUtils.getNinePatch(JudeteleRomSpecificResource.allfound));
            } else {
                judTable.setBackground(GraphicUtils.getNinePatch(JudeteleRomSpecificResource.notansw_background));
                for (int j = 0; j < JudeteleRomCategoryEnum.values().length; j++) {
                    Table q = new Table();
                    if (j <= correctAnswersForJudet - 1) {
                        q.setBackground(GraphicUtils.getNinePatch(JudeteleRomSpecificResource.correctansw_background));
                    } else {
                        q.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
                    }
                    float qSideDimen = horizontalGeneralMarginDimen * 2.2f;
                    judTable.add(q).width(qSideDimen).height(qSideDimen).pad(horizontalGeneralMarginDimen / 1.5f).expand();
                }
            }
            table.add(judTable).expand().pad(horizontalGeneralMarginDimen);
            if (rowIndex % 2 == 0) {
                table.row();
            }
            rowIndex++;
        }

        if (campaignService.getFinishedCampaignLevels().size() == allJudete.length) {
            new LevelFinishedPopup(this, SkelGameLabel.game_finished.getText()).addToPopupManager();
        }
        return table;
    }

    private float getLevelBtnHeight() {
        return ScreenDimensionsManager.getScreenHeightValue(10);
    }


    @Override
    public void onBackKeyPress() {
        Gdx.app.exit();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        Utils.createChangeLangPopup();
    }
}
