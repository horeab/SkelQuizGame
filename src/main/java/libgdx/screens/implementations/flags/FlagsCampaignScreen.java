package libgdx.screens.implementations.flags;

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
import libgdx.implementations.flags.FlagsCampaignLevelEnum;
import libgdx.implementations.flags.FlagsSpecificResource;
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
import libgdx.resources.Res;
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

public class FlagsCampaignScreen extends AbstractScreen<FlagsScreenManager> {

    private FlagsContainers flagsContainers = new FlagsContainers();
    private CampaignService campaignService = new CampaignService();
    private CampaignStoreService campaignStoreService = new CampaignStoreService();
    private List<CampaignStoreLevel> allCampaignLevelStores;

    public FlagsCampaignScreen() {
        allCampaignLevelStores = campaignService.processAndGetAllLevels();
    }

    @Override
    public void buildStage() {
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
//        table.add(flagsContainers.createAllJudeteFound()).pad(MainDimen.vertical_general_margin.getDimen()).colspan(2).row();
        MyButton startGameBtn = createStartGameBtn();
//        table.add(startGameBtn).pad(MainDimen.vertical_general_margin.getDimen()).width(ScreenDimensionsManager.getScreenWidthValue(50)).height(ScreenDimensionsManager.getScreenHeightValue(10)).row();
        table.add(createAllTable()).expand();
        addActor(table);
    }

    private MyButton createStartGameBtn() {
        MyButton button = new ButtonBuilder().setSingleLineText(SpecificPropertiesUtils.getText("ro_judetelerom_start_game"), FontManager.getNormalFontDim()).setButtonSkin(MainButtonSkin.DEFAULT).build();
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                List<Question> questions = new ArrayList<>(new QuestionCreator().getAllQuestions());
                List<Question> notPlayedQuestions = new ArrayList<>();
                for (Question question : questions) {
                    if (!campaignStoreService.isQuestionAlreadyPlayed(FlagsContainers.getQuestionId(question.getQuestionLineInQuestionFile(), question.getQuestionCategory(), question.getQuestionDifficultyLevel()))) {
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
        table.setFillParent(true);
        FlagsCampaignLevelEnum[] allCategs = FlagsCampaignLevelEnum.values();
        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        int i = 0;
        float iconDimen = horizontalGeneralMarginDimen * 5;
        for (final FlagsCampaignLevelEnum campaignLevelEnum : allCategs) {
            if ((i + 1) % 2 == 0) {
                table.row();
            }
            table.add(GraphicUtils.getImage(getCampaignNrIcon(campaignLevelEnum))).width(iconDimen).height(iconDimen);
            i++;
        }

        if (campaignService.getFinishedCampaignLevels().size() == allCategs.length) {
            new LevelFinishedPopup(this, SkelGameLabel.game_finished.getText()).addToPopupManager();
        }
        return table;
    }

    private Res getCampaignNrIcon(FlagsCampaignLevelEnum levelEnum) {
        Res icon = FlagsSpecificResource.campaign_level_0_0;
        if (levelEnum.getIndex() == 1) {
            icon = FlagsSpecificResource.campaign_level_0_1;
        } else if (levelEnum.getIndex() == 2) {
            icon = FlagsSpecificResource.campaign_level_0_2;
        } else if (levelEnum.getIndex() == 3) {
            icon = FlagsSpecificResource.campaign_level_0_3;
        } else if (levelEnum.getIndex() == 4) {
            icon = FlagsSpecificResource.campaign_level_0_4;
        }
        return icon;
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
