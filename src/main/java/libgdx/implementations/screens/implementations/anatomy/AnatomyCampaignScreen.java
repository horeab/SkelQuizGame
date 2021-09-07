package libgdx.implementations.screens.implementations.anatomy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignLevelEnumService;
import libgdx.campaign.CampaignService;
import libgdx.campaign.QuestionCategory;
import libgdx.constants.Language;
import libgdx.controls.button.ButtonBuilder;
import libgdx.controls.button.MyButton;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.controls.labelimage.InAppPurchaseTable;
import libgdx.controls.labelimage.LabelImageConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.anatomy.AnatomyCampaignLevelEnum;
import libgdx.implementations.anatomy.AnatomyQuestionCategoryEnum;
import libgdx.implementations.anatomy.AnatomyQuestionDifficultyLevel;
import libgdx.implementations.anatomy.AnatomySpecificResource;
import libgdx.implementations.anatomy.spec.AnatomyCampaignLevelNrOfQuestions;
import libgdx.implementations.anatomy.spec.AnatomyGameType;
import libgdx.implementations.anatomy.spec.AnatomyPreferencesManager;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.LevelFinishedPopup;
import libgdx.implementations.skelgame.gameservice.QuestionCreator;
import libgdx.resources.FontManager;
import libgdx.resources.dimen.MainDimen;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameLabel;
import libgdx.skelgameimpl.skelgame.SkelGameRatingService;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;

import java.util.*;

import static libgdx.implementations.anatomy.AnatomyCampaignLevelEnum.*;

public class AnatomyCampaignScreen extends AbstractScreen<AnatomyScreenManager> {

    private int scrollPanePositionInit = 0;
    private CampaignService campaignService = new CampaignService();
    private ScrollPane scrollPane;
    private Integer scrollToLevel = -1;
    private float levelHeight;
    public static Map<AnatomyCampaignLevelEnum, List<CampaignLevel>> campaign0AllLevels;
    private AnatomyPreferencesManager anatomyPreferencesManager = new AnatomyPreferencesManager();

    static {
        Map<AnatomyCampaignLevelEnum, List<CampaignLevel>> aMap = new LinkedHashMap<>();
        aMap.put(LEVEL_0_0, Arrays.asList(LEVEL_0_0, LEVEL_0_12));
        aMap.put(LEVEL_0_1, Arrays.asList(LEVEL_0_1, LEVEL_0_13));
        aMap.put(LEVEL_0_2, Arrays.asList(LEVEL_0_2, LEVEL_0_14));
        aMap.put(LEVEL_0_3, Arrays.asList(LEVEL_0_3, LEVEL_0_15));
        aMap.put(LEVEL_0_4, Arrays.asList(LEVEL_0_4, LEVEL_0_16));
        aMap.put(LEVEL_0_5, Arrays.asList(LEVEL_0_5, LEVEL_0_17));
        aMap.put(LEVEL_0_6, Arrays.asList(LEVEL_0_6, LEVEL_0_18));
        aMap.put(LEVEL_0_7, Arrays.asList(LEVEL_0_7, LEVEL_0_19));
        aMap.put(LEVEL_0_8, Arrays.asList(LEVEL_0_8, LEVEL_0_20));
        aMap.put(LEVEL_0_9, Arrays.asList(LEVEL_0_9, LEVEL_0_21));
        aMap.put(LEVEL_0_10, Arrays.asList(LEVEL_0_10, LEVEL_0_22));
        aMap.put(LEVEL_0_11, Arrays.asList(LEVEL_0_11, LEVEL_0_23));
        campaign0AllLevels = Collections.unmodifiableMap(aMap);
    }

    public static Map<AnatomyCampaignLevelEnum, AnatomyCampaignLevelNrOfQuestions> nrOfQuestionsMap = getNrOfQuestionsForLevel();

    public AnatomyCampaignScreen() {
    }

    @Override
    public void buildStage() {
        levelHeight = getLevelBtnHeight() + MainDimen.horizontal_general_margin.getDimen();

        scrollPane = new ScrollPane(createAllTable());

        if (scrollToLevel == -1) {
            scrollToLevel = 0;
        }

        scrollPane.setScrollingDisabled(true, false);
        if (Game.getInstance().isFirstTimeMainMenuDisplayed()) {
            new SkelGameRatingService(this).appLaunched();
        }
        Game.getInstance().setFirstTimeMainMenuDisplayed(false);
        Table table = new Table();
        table.setFillParent(true);
        table.add(scrollPane).expand();
        addActor(table);
    }

    private Table createAllTable() {
        Table table = new Table();


        int totalCat = campaign0AllLevels.size();
        table.add(new MyWrappedLabel(new MyWrappedLabelConfigBuilder().setFontScale(FontManager.getBigFontDim()).setText(Game.getInstance().getAppInfoService().getAppName()).build())).pad(MainDimen.vertical_general_margin.getDimen()).colspan(2).row();
        InAppPurchaseTable inAppPurchaseTable = new InAppPurchaseTable();
        for (int i = 0; i < totalCat; i++) {
            final int finalIndex = i;
            AnatomyQuestionCategoryEnum categoryEnum = (AnatomyQuestionCategoryEnum) CampaignLevelEnumService.getCategoryEnum(new ArrayList<>(campaign0AllLevels.keySet()).get(i).getName());
            float btnWidth = ScreenDimensionsManager.getScreenWidth(50);
            String btnText = new SpecificPropertiesUtils()
                    .getQuestionCategoryLabel(categoryEnum.getIndex());

            float btnFontDimen = Utils.containsWordWithLength(btnText, 11) ? FontManager.getNormalBigFontDim() : FontManager.getBigFontDim();
            btnFontDimen = Utils.containsWordWithLength(btnText, 15) ? FontManager.getNormalFontDim() : btnFontDimen;


            MyButton categBtn = new ButtonBuilder()
                    .setWrappedText(
                            new LabelImageConfigBuilder().setText(btnText)
                                    .setFontScale(btnFontDimen).setWrappedLineLabel(btnWidth / 1.1f))
                    .setButtonSkin(GameButtonSkin.HANGMAN_CATEG).build();
            final AnatomyCampaignLevelEnum campaignLevelEnum = valueOf("LEVEL_0_" + finalIndex);
            categBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    screenManager.showLevelScreen(campaignLevelEnum);
                }
            });

            float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
            Table btnTable = new Table();
            Image image = GraphicUtils.getImage(AnatomySpecificResource.valueOf("img_cat" + i + "_" + i + "s"));
            float imgHeight = image.getHeight();
            float imgWidth = image.getWidth();
            float levelBtnHeight = getLevelBtnHeight();
            if (imgWidth > imgHeight) {
                image.setWidth(ScreenDimensionsManager.getScreenWidth(55));
                image.setHeight(ScreenDimensionsManager.getNewHeightForNewWidth(image.getWidth(), imgWidth, imgHeight));
            } else {
                image.setHeight(levelBtnHeight);
                image.setWidth(ScreenDimensionsManager.getNewWidthForNewHeight(image.getHeight(), imgWidth, imgHeight));
            }
            Table imgTable = new Table();
            imgTable.add(image).width(image.getWidth()).height(image.getHeight());
            btnTable.add(imgTable).width(btnWidth);
            Table categBtnTable = new Table();
            categBtn.getCenterRow().row();
            categBtn.getCenterRow().add(createAchTable(campaignLevelEnum)).padTop(MainDimen.vertical_general_margin.getDimen() * 2);
//            categBtnTable.add().row();
            categBtnTable.add(categBtn).height(levelBtnHeight).width(btnWidth);
            if (i >= (totalCat / 2) && !Utils.isValidExtraContent()) {
                categBtnTable = inAppPurchaseTable.create(categBtnTable,
                        Language.en.name(), "Unlock extra categories and remove Ads", btnWidth, levelBtnHeight);
                categBtn.setDisabled(true);
            }
            btnTable.add(categBtnTable)
                    .pad(horizontalGeneralMarginDimen)
                    .height(levelBtnHeight)
                    .width(btnWidth);

            table.add(btnTable).expand().pad(horizontalGeneralMarginDimen);
            table.row();
        }

        if (campaignService.getFinishedCampaignLevels().size() == AnatomyCampaignLevelEnum.values().length) {
            new LevelFinishedPopup(this, SkelGameLabel.game_finished.getText()).addToPopupManager();
        }
        return table;
    }

    private Table createAchTable(AnatomyCampaignLevelEnum campaignLevelEnum) {
        int g0Stars = getLevelNrOfStars(AnatomyGameType.GENERALK, campaignLevelEnum);
        int g1Stars = getLevelNrOfStars(AnatomyGameType.IDENTIFY, campaignLevelEnum);


        Table table = new Table();
        AnatomySpecificResource g0Res = AnatomySpecificResource.star_disabled;
        AnatomySpecificResource g1Res = AnatomySpecificResource.star_disabled;
        if (g0Stars == AnatomyLevelScreen.TOTAL_STARS_TO_DISPLAY) {
            g0Res = AnatomySpecificResource.star_green;
        } else if (g0Stars > 0) {
            g0Res = AnatomySpecificResource.star_half;
        } else if (scrollToLevel == -1) {
            scrollToLevel = campaignLevelEnum.getIndex();
        }
        if (g1Stars == AnatomyLevelScreen.TOTAL_STARS_TO_DISPLAY) {
            g1Res = AnatomySpecificResource.star_green;
        } else if (g1Stars > 0) {
            g1Res = AnatomySpecificResource.star_half;
        } else if (scrollToLevel == -1) {
            scrollToLevel = campaignLevelEnum.getIndex();
        }

        Image g0Img = GraphicUtils.getImage(g0Res);
        Image g1Img = GraphicUtils.getImage(g1Res);
        float horizontalGeneralMarginDimen = MainDimen.horizontal_general_margin.getDimen();
        float achImgDimen = horizontalGeneralMarginDimen * 6;
        table.add(g0Img).width(achImgDimen).height(achImgDimen);
        table.add(g1Img).padLeft(horizontalGeneralMarginDimen).width(achImgDimen).height(achImgDimen);

        return table;
    }

    private int getLevelNrOfStars(AnatomyGameType anatomyGameType, AnatomyCampaignLevelEnum campaignLevelEnum) {
        return AnatomyLevelScreen.getStarsBasedOnTotalLevels(anatomyPreferencesManager
                        .getLevelScore((AnatomyCampaignLevelEnum) AnatomyCampaignScreen.getActualCampaignLevel(anatomyGameType, campaignLevelEnum)),
                getTotalNrOfQuestions(anatomyGameType, campaignLevelEnum));
    }

    private float getLevelBtnHeight() {
        return ScreenDimensionsManager.getScreenHeight(43);
    }

    private static Map<AnatomyCampaignLevelEnum, AnatomyCampaignLevelNrOfQuestions> getNrOfQuestionsForLevel() {
        Map<AnatomyCampaignLevelEnum, AnatomyCampaignLevelNrOfQuestions> map = new HashMap<>();
        for (AnatomyCampaignLevelEnum campaignLevelEnum : campaign0AllLevels.keySet()) {
            AnatomyCampaignLevelNrOfQuestions nrOfQuestions = new AnatomyCampaignLevelNrOfQuestions();
            nrOfQuestions.nrOfQuestionsGeneralK = getTotalNrOfQuestions(AnatomyGameType.GENERALK, campaignLevelEnum);
            nrOfQuestions.nrOfQuestionsIdentify = getTotalNrOfQuestions(AnatomyGameType.IDENTIFY, campaignLevelEnum);
            map.put(campaignLevelEnum, nrOfQuestions);
        }
        return map;
    }

    private static int getTotalNrOfQuestions(AnatomyGameType anatomyGameType, AnatomyCampaignLevelEnum campaignLevelEnum) {
        QuestionCategory categoryEnum = CampaignLevelEnumService.getCategoryEnum(getActualCampaignLevel(anatomyGameType, campaignLevelEnum).getName());
        QuestionCreator questionCreator = new QuestionCreator(AnatomyQuestionDifficultyLevel._0,
                categoryEnum);
        return questionCreator.getAllQuestions(Collections.singletonList(AnatomyQuestionDifficultyLevel._0),
                categoryEnum).size();
    }

    public static CampaignLevel getActualCampaignLevel(AnatomyGameType anatomyGameType, AnatomyCampaignLevelEnum campaignLevelEnum) {
        int levelPosition = 0;
        if (anatomyGameType == AnatomyGameType.GENERALK) {
            levelPosition = 1;
        }
        return campaign0AllLevels.get(campaignLevelEnum).get(levelPosition);
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
