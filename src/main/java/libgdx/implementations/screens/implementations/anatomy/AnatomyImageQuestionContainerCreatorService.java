package libgdx.implementations.screens.implementations.anatomy;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import libgdx.controls.button.MyButton;
import libgdx.game.Game;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.gameservice.GameContext;
import libgdx.implementations.skelgame.gameservice.ImageClickInfo;
import libgdx.implementations.skelgame.gameservice.ImageClickQuestionContainerCreatorService;
import libgdx.resources.gamelabel.GameLabelUtils;
import libgdx.resources.gamelabel.SpecificPropertiesUtils;
import libgdx.utils.ScreenDimensionsManager;

public class AnatomyImageQuestionContainerCreatorService extends ImageClickQuestionContainerCreatorService {

    public AnatomyImageQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
    }

    @Override
    protected Group createInitialGroupAndAdjustImageForSideAnswers(Image image) {
        Group grp = new Group();
        float imgHeight = image.getHeight();
        float imgWidth = image.getWidth();
        float groupHeight = getGroupHeightValue();
        float groupWidth = getGroupWidthValue();
        image.setWidth(ScreenDimensionsManager.getScreenWidth(60));
        image.setHeight(ScreenDimensionsManager.getNewHeightForNewWidth(image.getWidth(), imgWidth, imgHeight));
        image.setPosition(getQuestionImageLeftMarginWithSideAnswers(image, groupWidth), getQuestionImageTopMargin(image, groupHeight));
        grp.addActor(image);
        grp.setHeight(groupHeight);
        grp.setWidth(groupWidth);
        return grp;
    }

    @Override
    protected Group createInitialGroupAndAdjustImageForExactAnswers(Image image) {
        Group grp = new Group();
        float imgHeight = image.getHeight();
        float imgWidth = image.getWidth();
        float groupHeight = getGroupHeightValue();
        float groupWidth = getGroupWidthValue();
        image.setWidth(ScreenDimensionsManager.getScreenWidth(85));
        image.setHeight(ScreenDimensionsManager.getNewHeightForNewWidth(image.getWidth(), imgWidth, imgHeight));
        image.setPosition(getQuestionImageLeftMarginWithExactAnswers(image, groupWidth), getQuestionImageTopMargin(image, groupHeight));
        grp.addActor(image);
        grp.setHeight(groupHeight);
        grp.setWidth(groupWidth);
        return grp;
    }

    @Override
    protected String getQuestionTopTitle() {
        int catNr = gameContext.getQuestion().getQuestionCategory().getIndex();
        String language = Game.getInstance().getAppInfoService().getLanguage();
        String key = language + "_" + Game.getInstance().getAppInfoService().getGameIdPrefix() + "_question_category_" + catNr + "_sub";
        return GameLabelUtils.getText(key, language, SpecificPropertiesUtils.getLabelFilePath());
    }

    private float getQuestionImageLeftMarginWithExactAnswers(Image image, float groupWidth) {
        return (groupWidth - image.getWidth()) / 2;
    }

    private float getGroupWidthValue() {
        return ScreenDimensionsManager.getScreenWidth(98);
    }

    private float getGroupHeightValue() {
        return ScreenDimensionsManager.getScreenHeight(82);
    }

    @Override
    protected void setSideArrowPosition(ImageClickInfo coord, Image image, Group group, MyButton respBtn, Table arrowTable) {
        float leftMargin = getQuestionImageLeftMarginWithSideAnswers(image, group.getWidth());
        float topMargin = getQuestionImageTopMargin(image, group.getHeight());
        arrowTable.setX(leftMargin + ((image.getWidth() / 100) * coord.x) + respBtn.getWidth() / 2 - getSideAnswerResponsePointerDimen() / 2);
        arrowTable.setY(topMargin + (image.getHeight() / 100 * coord.y - respBtn.getHeight() / 2 + arrowTable.getHeight()));
//        arrowTable.setVisible(false);
    }

    @Override
    protected void setExactAnswerButtonPosition(Image image, Group group, MyButton button, ImageClickInfo coord) {
        float leftMargin = getQuestionImageLeftMarginWithExactAnswers(image, group.getWidth());
        float topMargin = getQuestionImageTopMargin(image, group.getHeight());
        button.setPosition(
                leftMargin + (image.getWidth() / 100 * coord.x),
                topMargin + (image.getHeight() / 100 * coord.y),
                Align.center);
    }

    private float getQuestionImageTopMargin(Image image, float groupHeight) {
        return (groupHeight - image.getHeight()) / 2;
    }

    private float getQuestionImageLeftMarginWithSideAnswers(Image image, float groupWidth) {
        return ((groupWidth / 1.3f) - image.getWidth()) / 2;
    }

    @Override
    protected GameButtonSkin getAnswerButtonSkin() {
        if (isSideAnswers(gameService.getQuestionImage())) {
            return GameButtonSkin.ANSWER_IMAGE_CLICK;
        } else {
            return GameButtonSkin.ANSWER_IMAGE_EXACT_ANSWER_CLICK;
        }
    }

    @Override
    protected void addQuestionImgTableToContainer(Image image) {
        Table questionImgTable;
        if (isSideAnswers(image)) {
            questionImgTable = createGroupWithImageAndAnswerOptionsForSideAnswers(image);
        } else {
            questionImgTable = createGroupWithImageAndAnswerOptionsForExactAnswers(image);
        }
        questionContainer.add(questionImgTable)
                .height(questionImgTable.getHeight()).width(questionImgTable.getWidth()).padBottom(0).center();
    }

    private boolean isSideAnswers(Image image) {
        return image.getWidth() != image.getHeight();
    }
}