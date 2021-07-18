package libgdx.implementations.screens.implementations.anatomy;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.QuestionConfig;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.anatomy.AnatomyCampaignLevelEnum;
import libgdx.implementations.anatomy.AnatomySpecificResource;
import libgdx.implementations.anatomy.spec.AnatomyGameType;
import libgdx.implementations.anatomy.spec.AnatomyPreferencesManager;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.GameContextService;
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

public class AnatomyLevelScreen extends AbstractScreen<AnatomyScreenManager> {

    private AnatomyCampaignLevelEnum campaignLevelEnum;
    public static final int TOTAL_STARS_TO_DISPLAY = 3;
    private MyButton backButton;
    private AnatomyPreferencesManager anatomyPreferencesManager = new AnatomyPreferencesManager();


    public AnatomyLevelScreen(AnatomyCampaignLevelEnum campaignLevelEnum) {
        this.campaignLevelEnum = campaignLevelEnum;

    }

    @Override
    public void buildStage() {
        addScreenBackground(campaignLevelEnum, this);
        Table table = new Table();
        table.setFillParent(true);
        table.add(createAllTable()).expand();
        addActor(table);
        backButton = new BackButtonBuilder().addHoverBackButton(this, ScreenDimensionsManager.getScreenWidth(0.5f),
                BackButtonBuilder.getY());
    }

    private Table createAllTable() {
        Table table = new Table();

        float categFontScale = 1.25f;
        MyWrappedLabel title = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(
                        FontColor.BLACK.getColor(),
                        FontConfig.FONT_SIZE * categFontScale))
                .setText(new SpecificPropertiesUtils().getQuestionCategoryLabel(campaignLevelEnum.getIndex()))
                .build());
        Table titleTable = new Table();
        titleTable.add(title);
        titleTable.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        table.add(titleTable).width(ScreenDimensionsManager.getScreenWidth(110))
                .height(ScreenDimensionsManager.getScreenHeight(8))
                .row();

        Table levelBtn1 = createLevelBtn(AnatomyGameType.GENERALK);
        Table levelBtn2 = createLevelBtn(AnatomyGameType.IDENTIFY);

        Table allBtnTable = new Table();
        allBtnTable.add(levelBtn1).width(levelBtn1.getWidth()).height(levelBtn1.getHeight())
                .pad(MainDimen.vertical_general_margin.getDimen() * 5).row();
        allBtnTable.add(levelBtn2).width(levelBtn1.getWidth()).height(levelBtn1.getHeight())
                .pad(MainDimen.vertical_general_margin.getDimen() * 5).row();
        table.add(allBtnTable).height(ScreenDimensionsManager.getScreenHeight(84));
        if (backButton != null) {
            backButton.toFront();
        }

        return table;
    }

    public static void addScreenBackground(CampaignLevel campaignLevelEnum, AbstractScreen abstractScreen) {
        if (campaignLevelEnum != null) {
            int index = campaignLevelEnum.getIndex();
            Image image = GraphicUtils.getImage(AnatomySpecificResource.valueOf("img_cat" + index + "_" + index + "t"));
            float imgHeight = image.getHeight();
            float imgWidth = image.getWidth();
            if (imgWidth > imgHeight) {
                image.setWidth(ScreenDimensionsManager.getScreenWidth(100));
                image.setHeight(ScreenDimensionsManager.getNewHeightForNewWidth(image.getWidth(), imgWidth, imgHeight));
                image.setPosition(ScreenDimensionsManager.getScreenWidth(95) - image.getWidth(),
                        ScreenDimensionsManager.getScreenHeight(25));
            } else {
                image.setHeight(ScreenDimensionsManager.getScreenHeight(75));
                image.setWidth(ScreenDimensionsManager.getNewWidthForNewHeight(image.getHeight(), imgWidth, imgHeight));
                image.setPosition(ScreenDimensionsManager.getScreenWidth(95) - image.getWidth(),
                        ScreenDimensionsManager.getScreenHeight(3));
            }
            image.toBack();
            abstractScreen.addActor(image);
        }
    }

    private Table createLevelBtn(AnatomyGameType anatomyGameType) {
        int totalNrOfQuestions = AnatomyCampaignScreen.nrOfQuestionsMap.get(campaignLevelEnum).getNrOfQuestions(anatomyGameType);
        MyButton btn = createCategButton(anatomyGameType, totalNrOfQuestions);
        Table btnTable = new Table();
        btnTable.add(createStarsTable(anatomyGameType, totalNrOfQuestions)).row();
        btnTable.add(btn).width(btn.getWidth()).height(btn.getHeight());
        btnTable.setHeight(ScreenDimensionsManager.getScreenHeight(20));
        return btnTable;
    }

    public static int getStarsBasedOnTotalLevels(int currentMaxLevels, int totalLevels) {
        if (currentMaxLevels >= totalLevels) {
            return TOTAL_STARS_TO_DISPLAY;
        } else if (currentMaxLevels * 2 >= totalLevels) {
            return TOTAL_STARS_TO_DISPLAY - 1;
        } else if (currentMaxLevels > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    private Table createStarsTable(AnatomyGameType anatomyGameType, int totalNrOfQuestions) {
        int levelStars = getLevelNrOfStars(anatomyGameType, totalNrOfQuestions, campaignLevelEnum);
        Table table = new Table();
        float sideDimen = ScreenDimensionsManager.getScreenWidth(7);
        if (levelStars == TOTAL_STARS_TO_DISPLAY) {
            addStarToTable(TOTAL_STARS_TO_DISPLAY, table, sideDimen, AnatomySpecificResource.star_green);
        } else {
            addStarToTable(levelStars, table, sideDimen, AnatomySpecificResource.star_level);
            addStarToTable(TOTAL_STARS_TO_DISPLAY - levelStars, table, sideDimen, AnatomySpecificResource.star_disabled);
        }
        return table;
    }

    private int getLevelNrOfStars(AnatomyGameType anatomyGameType, int totalNrOfQuestions, AnatomyCampaignLevelEnum campaignLevelEnum) {
        return getStarsBasedOnTotalLevels(anatomyPreferencesManager
                .getLevelScore((AnatomyCampaignLevelEnum) AnatomyCampaignScreen.getActualCampaignLevel(anatomyGameType, campaignLevelEnum)), totalNrOfQuestions);
    }

    private void addStarToTable(int to, Table table, float sideDimen, Res res) {
        float horiz = MainDimen.horizontal_general_margin.getDimen();
        float vert = MainDimen.vertical_general_margin.getDimen() / 2;
        for (int i = 0; i < to; i++) {
            table.add(GraphicUtils.getImage(res)).width(sideDimen).height(sideDimen)
                    .padRight(horiz)
                    .padBottom(vert);
        }
    }


    private MyButton createCategButton(AnatomyGameType anatomyGameType, final int totalNrOfQuestions) {
        String text = anatomyGameType.getLevelName().getText();
        float fontDim = Utils.containsWordWithLength(text, 13) ? FontManager.getSmallFontDim() : FontManager.getNormalFontDim();
        fontDim = Utils.containsWordWithLength(text, 15) ? FontManager.calculateMultiplierStandardFontSize(0.85f) : fontDim;

        MyButton categBtn = new ImageButtonBuilder(anatomyGameType.getButtonSkin(), getAbstractScreen())
                .textButtonWidth(GameButtonSize.ASTRONOMY_MENU_BUTTON.getWidth() * 1.4f)
                .padBetweenImageAndText(1.3f)
                .setFontScale(fontDim)
                .setFontColor(FontColor.BLACK)
                .setFixedButtonSize(GameButtonSize.ASTRONOMY_MENU_BUTTON)
                .setText(text)
                .build();
        categBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                CampaignLevel campaignLevel = AnatomyCampaignScreen.getActualCampaignLevel(anatomyGameType, campaignLevelEnum);
                CampaignLevelEnumService enumService = new CampaignLevelEnumService(campaignLevel);
                QuestionConfig questionConfig = enumService.getQuestionConfig(totalNrOfQuestions);
                GameContext gameContext = new GameContextService().createGameContext(questionConfig);
                screenManager.showCampaignGameScreen(gameContext, campaignLevel, anatomyGameType);
            }
        });

        return categBtn;
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

    @Override
    public void render(float dt) {
        super.render(dt);
        Utils.createChangeLangPopup();
    }

}
