package libgdx.implementations.screens.implementations.astronomy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import libgdx.campaign.CampaignLevel;
import libgdx.campaign.CampaignService;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.astronomy.AstronomyGame;
import libgdx.implementations.astronomy.AstronomySpecificResource;
import libgdx.implementations.astronomy.spec.Planet;
import libgdx.implementations.astronomy.spec.PlanetsUtil;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.screens.implementations.geoquiz.CampaignLevelFinishedPopup;
import libgdx.implementations.skelgame.gameservice.LevelFinishedService;
import libgdx.implementations.skelgame.gameservice.QuizStarsService;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.Utils;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AstronomyDragDropPlanetsScreen extends GameScreen<AstronomyScreenManager> {


    private final static String IMG_NAME = "PlanetImgName";
    public static final String ANSWER_LABEL_TEXT_NAME = "answerLabelTextName";
    public static final String PLANET_STACK_NAME = "PLANET_STACK_NAME";
    private Table allTable;
    private int TOTAL_OPTIONS = 4;
    private CampaignLevel campaignLevel;
    private List<Planet> allPlanets;
    private int correctPlanetId;
    private List<Integer> allOptPlanetIndex;
    private PlanetsGameType planetsGameType;

    public AstronomyDragDropPlanetsScreen() {
        super(null);
        Random random = new Random();
        planetsGameType = PlanetsGameType.values()[random.nextInt(PlanetsGameType.values().length)];
//        planetsGameType = PlanetsGameType.GRAVITY;
        allPlanets = PlanetsUtil.getAllPlanets();
        correctPlanetId = getCorrectPlanetId(random);
        allOptPlanetIndex = getAllOptPlanetIndex();
    }

    private int getCorrectPlanetId(Random random) {
        int correctPlanetId = random.nextInt(allPlanets.size());
        while (!PlanetsUtil.isValidOption(correctPlanetId, planetsGameType, allPlanets)) {
            correctPlanetId = random.nextInt(allPlanets.size());
        }
        return correctPlanetId;
    }

    @Override
    public void buildStage() {
        createAllTable();
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createAllTable() {
        allTable = new Table();
        allTable.setFillParent(true);
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(MainResource.background_texture);
        float margin = ScreenDimensionsManager.getScreenWidthValue(10);
        allTable.add(createLevelTitle()).height(ScreenDimensionsManager.getScreenHeightValue(20)).row();
        allTable.add(createQuestionTable()).padBottom(margin / 2);
        addActor(allTable);
    }

    private MyWrappedLabel createLevelTitle() {
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(RGBColor.WHITE.toColor(),
                        Color.BLACK, Math.round(FontConfig.FONT_SIZE * 1.5f),
                        3f, 3, 3, RGBColor.BLACK.toColor(0.4f)))
                .setWrappedLineLabel(ScreenDimensionsManager.getScreenWidthValue(90))
                .setText(planetsGameType.levelName).build());
    }

    private List<Integer> getAllOptPlanetIndex() {
        List<Integer> res = new ArrayList<>();
        List<String> optTextList = new ArrayList<>();
        res.add(correctPlanetId);
        optTextList.add(getOptionsText(correctPlanetId));
        Random random = new Random();
        while (res.size() < TOTAL_OPTIONS) {
            int randI = random.nextInt(allPlanets.size());
            String optText = getOptionsText(randI);
            while (res.contains(randI)
                    || !PlanetsUtil.isValidOption(randI, planetsGameType, allPlanets)
                    || optTextList.contains(optText)) {
                randI = random.nextInt(allPlanets.size());
                optText = getOptionsText(randI);
            }
            optTextList.add(optText);
            res.add(randI);
        }
        Collections.shuffle(res);
        return res;
    }

    private Table createQuestionTable() {
        float optImgDimen = getOptImgDimen();
        float planetImgDimen = getPlanetImgDimen();
        Table imgTable = new Table();
        Res optRes = AstronomySpecificResource.planet_option_backgr;
        imgTable.add(creatAnswerImg(optRes, allOptPlanetIndex.get(0)))
                .height(optImgDimen)
                .width(optImgDimen);
        imgTable.add();
        imgTable.add(creatAnswerImg(optRes, allOptPlanetIndex.get(1)))
                .height(optImgDimen)
                .width(optImgDimen)
                .row();

        imgTable.add();
        imgTable.add(createQuestionImg(getQuestionPlanetRes(), correctPlanetId))
                .height(planetImgDimen)
                .width(planetImgDimen);
        imgTable.add().row();

        imgTable.add(creatAnswerImg(optRes, allOptPlanetIndex.get(2)))
                .height(optImgDimen)
                .width(optImgDimen);
        imgTable.add();
        imgTable.add(creatAnswerImg(optRes, allOptPlanetIndex.get(3)))
                .height(optImgDimen)
                .width(optImgDimen);

        return imgTable;
    }

    private float getPlanetImgDimen() {
        return ScreenDimensionsManager.getScreenWidthValue(25);
    }

    private float getOptImgDimen() {
        return ScreenDimensionsManager.getScreenWidthValue(30);
    }

    private Stack creatAnswerImg(Res background, int index) {
        float labelWidth = ScreenDimensionsManager.getScreenWidthValue(30);
        float labelHeight = ScreenDimensionsManager.getScreenHeightValue(7);
        String optionsText = getOptionsText(index);
        float fontFactor = optionsText.length() > 10 ? 1.1f : 1.3f;
        MyWrappedLabel answerText = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(RGBColor.WHITE.toColor(),
                        Color.BLACK, Math.round(FontConfig.FONT_SIZE * fontFactor),
                        3f, 3, 3, RGBColor.BLACK.toColor(0.4f)))
                /////////////////////////////////////////////////////////////////
                .setText(optionsText)
                /////////////////////////////////////////////////////////////////
                .setWrappedLineLabel(labelWidth)
                .setSingleLineLabel().build());
        answerText.setName(ANSWER_LABEL_TEXT_NAME + index);
        answerText.setWidth(labelWidth);
        answerText.setHeight(labelHeight);
        answerText.pad(labelHeight / 10);
        processAnswerTextTable(answerText);
        Table answer = new Table();
        answer.add(answerText);
        answerText.setBackground(GraphicUtils.getColorBackground(new RGBColor(0.8f, 230, 242, 255).toColor()));
        Stack stack = new Stack();
        stack.add(GraphicUtils.getImage(background));
        stack.add(answer);
        stack.setName(getImgName(index));
        return stack;
    }

    private void processAnswerTextTable(Table answerText) {
        float unitImgSideDimen = ScreenDimensionsManager.getScreenWidthValue(10);
        if (planetsGameType == PlanetsGameType.SUN_LIGHT_DURATION) {
            answerText.row();
            Image unit = GraphicUtils.getImage(AstronomySpecificResource.speed_light);
            answerText.add(unit).width(unitImgSideDimen * 2 * 1.2f).height(unitImgSideDimen * 1.2f);
        } else if (planetsGameType == PlanetsGameType.MASS || planetsGameType == PlanetsGameType.RADIUS) {
            Image unit = GraphicUtils.getImage(AstronomySpecificResource.earth_stroke);
            answerText.add(unit).width(unitImgSideDimen).height(unitImgSideDimen);
        } else if (planetsGameType == PlanetsGameType.MEAN_TEMPERATURE) {
            Image unit = GraphicUtils.getImage(AstronomySpecificResource.temperature);
            answerText.add(unit).width(unitImgSideDimen).height(unitImgSideDimen);
        } else if (planetsGameType == PlanetsGameType.ORBITAL_PERIOD) {
            Image unit = GraphicUtils.getImage(AstronomySpecificResource.orbital_period);
            answerText.row();
            answerText.add(unit).padTop(unitImgSideDimen / 5).width(unitImgSideDimen).height(unitImgSideDimen);
        }
    }

    private String getOptionsText(int index) {
        if (planetsGameType == PlanetsGameType.SUN_LIGHT_DURATION) {
            return PlanetsUtil.getLightFromSunInSec(index, allPlanets);
        } else if (planetsGameType == PlanetsGameType.MASS) {
            return PlanetsUtil.getMassInRelationToEarth(index, allPlanets);
        } else if (planetsGameType == PlanetsGameType.GRAVITY) {
            return PlanetsUtil.getGravityInRelationToEarth(index, allPlanets);
        } else if (planetsGameType == PlanetsGameType.MEAN_TEMPERATURE) {
            return PlanetsUtil.getMeanTemp(index, allPlanets);
        } else if (planetsGameType == PlanetsGameType.ORBITAL_PERIOD) {
            return PlanetsUtil.getOrbitalPeriod(index, allPlanets);
        } else if (planetsGameType == PlanetsGameType.RADIUS) {
            return PlanetsUtil.getRadius(index, allPlanets);
        }
        return "";
    }

    private Res getQuestionPlanetRes() {
        return AstronomySpecificResource.valueOf("planet_" + correctPlanetId);
    }

    private Stack createQuestionImg(Res res, int correctIndex) {
        Stack stack = new Stack();
        stack.setName(PLANET_STACK_NAME);
        stack.add(GraphicUtils.getImage(res));
        float labelWidth = ScreenDimensionsManager.getScreenWidthValue(30);
        float labelHeight = ScreenDimensionsManager.getScreenHeightValue(7);
        MyWrappedLabel planetName = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(RGBColor.WHITE.toColor(),
                        Color.BLACK, Math.round(FontConfig.FONT_SIZE * 1.5f),
                        3f, 3, 3, RGBColor.BLACK.toColor(0.4f)))
                .setText(PlanetsUtil.getName(correctPlanetId, allPlanets))
                .setWrappedLineLabel(labelWidth)
                .setSingleLineLabel().build());
        planetName.setWidth(labelWidth);
        planetName.setHeight(labelHeight);
        planetName.padTop(labelHeight / 1.5f);
        stack.add(planetName);
        stack.addListener(new DragListener() {
            float initialX;
            float initialY;

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                initialX = stack.getX();
                initialY = stack.getY();
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                stack.toFront();
                stack.moveBy(x - stack.getWidth() / 2, y - stack.getHeight() / 2);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                if (responseDraggedOverImage(stack, correctIndex)) {
                    stack.setTouchable(Touchable.disabled);
                    moveToResponseImg(correctIndex, stack);
                    displayImgOverAnswer(AstronomySpecificResource.correct_drag, correctIndex);
                    hidePlanetIcon();

                } else {

                    boolean isResponse = false;
                    for (Integer i : allOptPlanetIndex) {
                        if (responseDraggedOverImage(stack, i)) {
                            isResponse = true;
                            stack.setTouchable(Touchable.disabled);
                            moveToResponseImg(i, stack);
                            displayImgOverAnswer(AstronomySpecificResource.wrong_drag, i);
                            displayImgOverAnswer(AstronomySpecificResource.correct_drag, correctIndex);
                            hidePlanetIcon();
                            break;
                        }
                    }
                    if (!isResponse) {
                        stack.addAction(Actions.moveTo(initialX, initialY, 0.3f));
                    }
                }
            }
        });
        return stack;
    }

    private void hidePlanetIcon() {
        getRoot().findActor(PLANET_STACK_NAME).addAction(Actions.fadeOut(0.6f));
    }

    private void displayImgOverAnswer(Res res, int answerIndex) {
        Stack answerImg = getRoot().findActor(getImgName(answerIndex));
        Image image = GraphicUtils.getImage(res);
        image.setX(answerImg.getParent().getX() + answerImg.getX());
        image.setY(answerImg.getParent().getY() + answerImg.getY());
        float imgDimen = getOptImgDimen();
        image.setWidth(imgDimen);
        image.setHeight(imgDimen);
        new ActorAnimation(image, getAbstractScreen()).animateFadeIn(fadeInCorrectWrongImgDuration());
        addActor(image);
    }

    private float fadeInCorrectWrongImgDuration() {
        return 0.6f;
    }

    private void moveToResponseImg(int index, Stack planetImg) {
        Stack image = getRoot().findActor(getImgName(index));
        float diff = getOptImgDimen() - getPlanetImgDimen();
        planetImg.addAction(Actions.moveTo(image.getX() + diff / 2, image.getY() + diff / 2, 0.3f));
    }

    private boolean responseDraggedOverImage(Stack img, int index) {
        float imgDimen = getOptImgDimen();
        float acceptedDist = imgDimen / 3;
        Stack image = getRoot().findActor(getImgName(index));
        float imageX = image.getX();
        float imageY = image.getY();
        return (imageX - acceptedDist < img.getX() && imageX + imgDimen + acceptedDist > (img.getX() + imgDimen))
                &&
                (imageY - acceptedDist < img.getY() && imageY + imgDimen + acceptedDist > (img.getY() + imgDimen));
    }

    private String getImgName(int index) {
        return IMG_NAME + index;
    }


    @Override
    public void goToNextQuestionScreen() {
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameFailed(gameUser)) {
            new CampaignLevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
        }
        allTable.addAction(Actions.sequence(Actions.fadeOut(0.2f), Utils.createRunnableAction(new Runnable() {
            @Override
            public void run() {
                allTable.remove();
                createAllTable();
            }
        })));
    }

    @Override
    public void executeLevelFinished() {
        GameUser gameUser = gameContext.getCurrentUserGameUser();
        if (levelFinishedService.isGameWon(gameUser)) {
            QuizStarsService starsService = AstronomyGame.getInstance().getDependencyManager().getStarsService();
            int starsWon = starsService.getStarsWon(LevelFinishedService.getPercentageOfWonQuestions(gameUser));
            new CampaignService().levelFinished(starsWon, campaignLevel);
            screenManager.showMainScreen();
        } else if (levelFinishedService.isGameFailed(gameUser)) {
            new CampaignLevelFinishedPopup(this, campaignLevel, gameContext).addToPopupManager();
        }
//        screenManager.showMainScreen();
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showMainScreen();
    }

}
