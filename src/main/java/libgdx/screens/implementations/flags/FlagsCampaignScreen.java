package libgdx.screens.implementations.flags;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.*;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.flags.FlagsCampaignLevelEnum;
import libgdx.implementations.flags.FlagsGame;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.SkelGameRatingService;
import libgdx.implementations.skelgame.gameservice.GameContextService;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;
import libgdx.implementations.skelgame.question.Question;
import libgdx.implementations.skelgame.question.QuestionParser;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontColor;

import java.util.List;

public class FlagsCampaignScreen extends AbstractScreen<FlagsScreenManager> {

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
        table.add(createAllTable()).expand();
        addActor(table);
    }

    private Table createAllTable() {
        Table table = new Table();
        table.setFillParent(true);
        addCategButtons(table);
        return table;
    }


    private void addCategButtons(Table table) {
        table.row();
        table.add(addDifficultyButtons()).padBottom(MainDimen.vertical_general_margin.getDimen() * 5);
        table.row();
        Table btnTable0 = new Table();
        addButtonToTable(btnTable0, FlagsCampaignLevelEnum.LEVEL_0_1);
        btnTable0.add().width(ScreenDimensionsManager.getScreenWidthValue(5));
        addButtonToTable(btnTable0, FlagsCampaignLevelEnum.LEVEL_0_0);
        btnTable0.add().width(ScreenDimensionsManager.getScreenWidthValue(5));
        addButtonToTable(btnTable0, FlagsCampaignLevelEnum.LEVEL_0_2);
        table.add(btnTable0).padBottom(MainDimen.vertical_general_margin.getDimen());
        table.row();
        Table btnTable1 = new Table();
        btnTable1.add().width(ScreenDimensionsManager.getScreenWidthValue(10));
        addButtonToTable(btnTable1, FlagsCampaignLevelEnum.LEVEL_0_3);
        btnTable1.add().width(ScreenDimensionsManager.getScreenWidthValue(10));
        addButtonToTable(btnTable1, FlagsCampaignLevelEnum.LEVEL_0_4);
        btnTable1.add().width(ScreenDimensionsManager.getScreenWidthValue(30));
        table.add(btnTable1);
    }

    private Table addDifficultyButtons() {
        Table table = new Table();
        for (int diff = 0; diff < 3; diff++) {
            MyButton difficultyButton = createDifficultyButton(diff);
            table.add(difficultyButton).width(difficultyButton.getWidth()).height(difficultyButton.getHeight());
        }
        return table;
    }

    private MyButton createDifficultyButton(final int diff) {
        String labelText = diff + " ";
        MyButton btn = new ImageButtonBuilder(GameButtonSkin.valueOf("FLAGS_CATEG" + diff), getAbstractScreen())
                .setFontScale(FontManager.getSmallFontDim())
                .setFontColor(FontColor.BLACK)
                .setFixedButtonSize(GameButtonSize.FLAGS_MENU_BUTTON)
                .setWrappedText(labelText, ScreenDimensionsManager.getScreenWidthValue(GameButtonSize.FLAGS_MENU_BUTTON.getWidth()))
                .build();
        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            }
        });
        return btn;
    }

    private void addButtonToTable(Table table, FlagsCampaignLevelEnum campaignLevel) {
        QuestionParser questionParser = new QuestionParser();
        CampaignLevelEnumService enumService = new CampaignLevelEnumService(campaignLevel);
        List<Question> allQuestions = questionParser.getAllQuestions(enumService.getDifficultyEnum(), (QuizQuestionCategory) enumService.getCategoryEnum());
        MyButton categButton = createCategButton(campaignLevel, allQuestions);

        Table containerTable = new Table();
        containerTable.add(categButton).width(categButton.getWidth()).height(categButton.getHeight()).row();
        Table allQuestionsTable = FlagsContainers.allQuestionsTable(allQuestions);
        containerTable.add(allQuestionsTable)
                .padTop(MainDimen.vertical_general_margin.getDimen() * 3)
                .width(categButton.getWidth()).height(categButton.getHeight() / 5);


        table.add(containerTable).padBottom(MainDimen.vertical_general_margin.getDimen() * 5f)
                .width(categButton.getWidth()).height(categButton.getHeight());
    }

    private MyButton createCategButton(final CampaignLevel campaignLevel, List<Question> allQuestions) {
        String labelText = new CampaignLevelEnumService(campaignLevel).getLabelText();
        MyButton categBtn = new ImageButtonBuilder(GameButtonSkin.valueOf("FLAGS_CATEG" + campaignLevel.getIndex()), getAbstractScreen())
                .padBetweenImageAndText(1.4f)
                .textBackground(null)
                .setFontScale(FontManager.calculateMultiplierStandardFontSize(0.9f))
                .setText(labelText)
                .setFontColor(FontColor.BLACK)
                .setFixedButtonSize(GameButtonSize.FLAGS_MENU_BUTTON)
                .build();
        categBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                CampaignLevelEnumService enumService = new CampaignLevelEnumService(campaignLevel);
                QuestionConfig questionConfig = enumService.getQuestionConfig(allQuestions.size());
                FlagsGame.getInstance().getScreenManager().showCampaignGameScreen(new GameContextService().createGameContext(questionConfig), campaignLevel);
            }
        });
        return categBtn;
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
