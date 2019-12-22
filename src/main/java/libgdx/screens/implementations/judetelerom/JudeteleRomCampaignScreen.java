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
import libgdx.implementations.judetelerom.JudeteleRomDifficultyLevel;
import libgdx.implementations.judetelerom.JudeteleRomGame;
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
import libgdx.resources.gamelabel.MainGameLabel;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

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
        final int maxOpenedLevel = allCampaignLevelStores.size();
        levelHeight = getLevelBtnHeight() + MainDimen.horizontal_general_margin.getDimen();

        scrollPane = new ScrollPane(createAllTable());
        scrollPane.setScrollingDisabled(true, false);
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
        Table table = new Table();
        table.setFillParent(true);
        MyWrappedLabel titleLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontScale(FontManager.getBigFontDim()).setText(Game.getInstance().getAppInfoService().getAppName()).build());

        table.add(titleLabel).pad(MainDimen.vertical_general_margin.getDimen()).colspan(2).row();
        table.add(judeteContainers.createAllJudeteFound()).pad(MainDimen.vertical_general_margin.getDimen()).colspan(2).row();
        MyButton startGameBtn = createStartGameBtn();
        table.add(startGameBtn).pad(MainDimen.vertical_general_margin.getDimen()).width(ScreenDimensionsManager.getScreenWidthValue(50)).height(ScreenDimensionsManager.getScreenHeightValue(10)).row();
        table.add(scrollPane).expand();
        addActor(table);
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private MyButton createStartGameBtn() {
        MyButton button = new ButtonBuilder().setSingleLineText(MainGameLabel.l_new_game.getText(), FontManager.getNormalFontDim()).setButtonSkin(MainButtonSkin.DEFAULT).build();
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                List<Question> questions = new ArrayList<>();
                questions.addAll(new QuestionCreator().getAllQuestions());
                Collections.shuffle(questions);
                GameContext gameContext = new GameContextService().createGameContext(questions.toArray(new Question[questions.size()]));
                JudeteleRomGame.getInstance().getScreenManager().showCampaignGameScreen(gameContext, null);
            }
        });
        return button;
    }

    private Table createAllTable() {
        Table table = new Table();
        int i = 1;
        JudeteleRomCampaignLevelEnum[] allJudete = JudeteleRomCampaignLevelEnum.values();
        for (final JudeteleRomCampaignLevelEnum campaignLevelEnum : allJudete) {
            float btnWidth = ScreenDimensionsManager.getScreenWidthValue(40);
            final Integer category = new CampaignLevelEnumService(campaignLevelEnum).getCategory();
            float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
            Table judTable = new Table();
            judTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
            MyWrappedLabel judLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setWrappedLineLabel(btnWidth / 1.1f).setFontScale(FontManager.getNormalFontDim()).setText(new SpecificPropertiesUtils().getQuestionCampaignLabel(category)).build());
            judTable.add(judLabel)
                    .colspan(5)
                    .height(getLevelBtnHeight())
                    .width(btnWidth).row();
            for (int j = 0; j < 5; j++) {
                Table q = new Table();
                q.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
                judTable.add(q).expand();
            }
            table.add(judTable).expand().pad(horizontalGeneralMarginDimen);
            if (i % 2 == 0) {
                table.row();
            }
            i++;
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
