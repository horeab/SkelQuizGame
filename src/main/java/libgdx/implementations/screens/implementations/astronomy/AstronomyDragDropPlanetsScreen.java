package libgdx.implementations.screens.implementations.astronomy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.builders.BackButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.astronomy.AstronomySpecificResource;
import libgdx.implementations.astronomy.spec.AstronomyPreferencesManager;
import libgdx.implementations.astronomy.spec.Planet;
import libgdx.implementations.astronomy.spec.PlanetsUtil;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.question.GameQuestionInfoStatus;
import libgdx.resources.MainResource;
import libgdx.resources.Res;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.SoundUtils;
import libgdx.utils.Utils;
import libgdx.utils.model.FontConfig;
import libgdx.utils.model.RGBColor;

import java.util.*;

public class AstronomyDragDropPlanetsScreen extends GameScreen<AstronomyScreenManager> {


    private final static String IMG_NAME = "PlanetImgName";
    private static final String ANSWER_LABEL_TEXT_NAME = "answerLabelTextName";
    private static final String PLANET_STACK_NAME = "PLANET_STACK_NAME";
    private Table allTable;
    private List<Planet> allPlanets;
    private int correctPlanetId;
    private List<Integer> allOptPlanetIndex;
    private Map<Integer, GameQuestionInfoStatus> allQuestionsValidToPlay;
    private AstronomyPlanetsGameType planetsGameType;
    private AstronomyPreferencesManager astronomyPreferencesManager = new AstronomyPreferencesManager();
    private List<Image> displayedWrongCorrectImages = new ArrayList<>();

    public AstronomyDragDropPlanetsScreen(AstronomyPlanetsGameType planetsGameType, List<Planet> allPlanets) {
        super(null);
        this.planetsGameType = planetsGameType;
        this.allPlanets = allPlanets;
        Collections.shuffle(allPlanets);
        allQuestionsValidToPlay = PlanetsUtil.getAllAvailableLevelsToPlay(allPlanets, planetsGameType);
    }

    private int getCorrectPlanetIdForQuestion() {
        int correctPlanetId = -1;
        for (Map.Entry<Integer, GameQuestionInfoStatus> e : allQuestionsValidToPlay.entrySet()) {
            if (e.getValue() == GameQuestionInfoStatus.OPEN) {
                return e.getKey();
            }
        }
        return correctPlanetId;
    }

    @Override
    public void buildStage() {
        new ActorAnimation(getAbstractScreen()).createScrollingBackground(MainResource.background_texture);
        createAllTable();
        new BackButtonBuilder().addHoverBackButton(this);
    }

    private void createAllTable() {
        correctPlanetId = getCorrectPlanetIdForQuestion();
        if (correctPlanetId == -1) {
            Game.getInstance().getAppInfoService().showPopupAd(new Runnable() {
                @Override
                public void run() {
                    onBackKeyPress();
                }
            });
            return;
        }
        allOptPlanetIndex = getAllOptPlanetIndex(correctPlanetId);

        allTable = new Table();
        allTable.setFillParent(true);

        Table headerTable = new HeaderCreator().createHeaderTable(allQuestionsValidToPlay);
        headerTable.setHeight(ScreenDimensionsManager.getScreenHeight(5));
        allTable.add(headerTable).height(headerTable.getHeight()).row();

        float margin = ScreenDimensionsManager.getScreenWidth(10);
        allTable.add(createLevelTitle()).height(ScreenDimensionsManager.getScreenHeight(20)).row();
        allTable.add(createQuestionTable()).padBottom(margin / 2);
        addActor(allTable);
    }

    private MyWrappedLabel createLevelTitle() {
        return new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontConfig(new FontConfig(RGBColor.WHITE.toColor(),
                        Color.BLACK, Math.round(FontConfig.FONT_SIZE * 1.5f),
                        3f, 3, 3, RGBColor.BLACK.toColor(0.4f)))
                .setWrappedLineLabel(ScreenDimensionsManager.getScreenWidth(90))
                .setText(SpecificPropertiesUtils.getText(planetsGameType.levelName)).build());
    }

    private List<Integer> getAllOptPlanetIndex(int correctPlanetId) {
        List<Integer> res = new ArrayList<>();
        List<String> optTextList = new ArrayList<>();
        res.add(correctPlanetId);
        optTextList.add(getOptionsText(correctPlanetId));
        Random random = new Random();
        int TOTAL_OPTIONS = 4;
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
        return ScreenDimensionsManager.getScreenWidth(25);
    }

    private float getOptImgDimen() {
        return ScreenDimensionsManager.getScreenWidth(30);
    }

    private Stack creatAnswerImg(Res background, int index) {
        float labelWidth = ScreenDimensionsManager.getScreenWidth(30);
        float labelHeight = ScreenDimensionsManager.getScreenHeight(7);
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
        float unitImgSideDimen = ScreenDimensionsManager.getScreenWidth(10);
        if (planetsGameType == AstronomyPlanetsGameType.SUN_LIGHT_DURATION) {
            answerText.row();
            Image unit = GraphicUtils.getImage(AstronomySpecificResource.speed_light);
            answerText.add(unit).width(unitImgSideDimen * 2 * 1.2f).height(unitImgSideDimen * 1.2f);
        } else if (planetsGameType == AstronomyPlanetsGameType.MASS || planetsGameType == AstronomyPlanetsGameType.RADIUS) {
            Image unit = GraphicUtils.getImage(AstronomySpecificResource.earth_stroke);
            answerText.add(unit).width(unitImgSideDimen).height(unitImgSideDimen);
        } else if (planetsGameType == AstronomyPlanetsGameType.MEAN_TEMPERATURE) {
            Image unit = GraphicUtils.getImage(AstronomySpecificResource.temperature);
            answerText.add(unit).width(unitImgSideDimen).height(unitImgSideDimen);
        } else if (planetsGameType == AstronomyPlanetsGameType.ORBITAL_PERIOD) {
            Image unit = GraphicUtils.getImage(AstronomySpecificResource.orbital_period);
            answerText.row();
            answerText.add(unit).padTop(unitImgSideDimen / 5).width(unitImgSideDimen).height(unitImgSideDimen);
        }
    }

    private String getOptionsText(int index) {
        if (planetsGameType == AstronomyPlanetsGameType.SUN_LIGHT_DURATION) {
            return PlanetsUtil.getLightFromSunInSec(index, allPlanets);
        } else if (planetsGameType == AstronomyPlanetsGameType.MASS) {
            return PlanetsUtil.getMassInRelationToEarth(index, allPlanets);
        } else if (planetsGameType == AstronomyPlanetsGameType.GRAVITY) {
            return PlanetsUtil.getGravityInRelationToEarth(index, allPlanets);
        } else if (planetsGameType == AstronomyPlanetsGameType.MEAN_TEMPERATURE) {
            return PlanetsUtil.getMeanTemp(index, allPlanets);
        } else if (planetsGameType == AstronomyPlanetsGameType.ORBITAL_PERIOD) {
            return PlanetsUtil.getOrbitalPeriod(index, allPlanets);
        } else if (planetsGameType == AstronomyPlanetsGameType.RADIUS) {
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
        float labelWidth = ScreenDimensionsManager.getScreenWidth(30);
        float labelHeight = ScreenDimensionsManager.getScreenHeight(7);
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

        float moveBy = ScreenDimensionsManager.getScreenWidth(2);
        float moveByDuration = 0.1f;
        stack.addAction(
                Actions.sequence(Actions.delay(0.2f),
                        Actions.moveBy(moveBy, 0, moveByDuration),
                        Actions.moveBy(-moveBy * 2, 0, moveByDuration),
                        Actions.moveBy(moveBy * 2, 0, moveByDuration)));

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
                    questionCorrectAnswered(stack, correctIndex);
                } else {
                    boolean isResponse = false;
                    for (Integer i : allOptPlanetIndex) {
                        if (responseDraggedOverImage(stack, i)) {
                            questionWrongAnswered(i, stack, correctIndex);
                            isResponse = true;
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

    private void questionWrongAnswered(Integer i, Stack stack, int correctIndex) {
        allQuestionsValidToPlay.put(correctPlanetId, GameQuestionInfoStatus.LOST);
        SoundUtils.playSound(AstronomySpecificResource.level_fail);
        stack.setTouchable(Touchable.disabled);
        moveToResponseImg(i, stack);
        displayImgOverAnswer(AstronomySpecificResource.wrong_drag, i);
        displayImgOverAnswer(AstronomySpecificResource.correct_drag, correctIndex);
        hidePlanetIcon();
    }

    private void questionCorrectAnswered(Stack stack, int correctIndex) {
        allQuestionsValidToPlay.put(correctPlanetId, GameQuestionInfoStatus.WON);
        astronomyPreferencesManager.putLevelScore(planetsGameType, getTotalWonQuestions());
        SoundUtils.playSound(AstronomySpecificResource.level_success);
        stack.setTouchable(Touchable.disabled);
        moveToResponseImg(correctIndex, stack);
        displayImgOverAnswer(AstronomySpecificResource.correct_drag, correctIndex);
        hidePlanetIcon();
    }

    private int getTotalWonQuestions() {
        int res = 0;
        for (Map.Entry<Integer, GameQuestionInfoStatus> e : allQuestionsValidToPlay.entrySet()) {
            if (e.getValue() == GameQuestionInfoStatus.WON) {
                res++;
            }
        }
        return res;
    }

    private void hidePlanetIcon() {
        getRoot().findActor(PLANET_STACK_NAME).addAction(Actions.sequence(Actions.fadeOut(0.6f),
                Utils.createRunnableAction(new Runnable() {
                    @Override
                    public void run() {
                        float fadeOutD = 0.2f;
                        float delayD = 2f;
                        for (Image image : new ArrayList<>(displayedWrongCorrectImages)) {
                            image.addAction(Actions.sequence(Actions.delay(delayD), Actions.fadeOut(fadeOutD), Utils.createRunnableAction(new Runnable() {
                                @Override
                                public void run() {
                                    displayedWrongCorrectImages.remove(image);
                                    image.remove();
                                }
                            })));
                        }
                        allTable.addAction(Actions.sequence(Actions.delay(delayD), Actions.fadeOut(fadeOutD),
                                Utils.createRunnableAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        allTable.remove();
                                        createAllTable();
                                    }
                                })));
                    }
                })));
    }

    private void displayImgOverAnswer(Res res, int answerIndex) {
        Stack answerImg = getRoot().findActor(getImgName(answerIndex));
        Image image = GraphicUtils.getImage(res);
        image.setX(answerImg.getParent().getX() + answerImg.getX());
        image.setY(answerImg.getParent().getY() + answerImg.getY());
        float imgDimen = getOptImgDimen();
        image.setWidth(imgDimen);
        image.setHeight(imgDimen);
        new ActorAnimation(getAbstractScreen()).animateFadeIn(image, fadeInCorrectWrongImgDuration());
        addActor(image);
        displayedWrongCorrectImages.add(image);
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
    }

    @Override
    public void executeLevelFinished() {
    }

    @Override
    public void onBackKeyPress() {
        screenManager.showDetailedCampaignScreen(AstronomyGameType.SOLAR_SYSTEM);
    }

}
