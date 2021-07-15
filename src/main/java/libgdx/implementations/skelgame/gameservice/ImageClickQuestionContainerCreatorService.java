package libgdx.implementations.skelgame.gameservice;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.anatomy.AnatomySpecificResource;
import libgdx.implementations.screens.GameScreen;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.utils.ScreenDimensionsManager;
import libgdx.utils.model.FontColor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Map;

public class ImageClickQuestionContainerCreatorService extends QuestionContainerCreatorService<ImageClickGameService> {

    private ActorAnimation actorAnimation;

    public ImageClickQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
        actorAnimation = new ActorAnimation(getAbstractGameScreen());
    }

    @Override
    public Table createAnswerOptionsTable() {
        return new Table();
    }

    @Override
    public Table createQuestionTable() {
        Table questionTable = super.createQuestionTable();
        String questionTopTitle = getQuestionTopTitle();
        Table tableQuestionLabel = new Table();
        tableQuestionLabel.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        if (StringUtils.isNotBlank(questionTopTitle)) {
            MyWrappedLabel topTitle = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                    .setFontScale(FontManager.calculateMultiplierStandardFontSize(0.9f))
                    .setFontColor(FontColor.BLACK)
                    .setText(getQuestionTopTitle()).build());
            tableQuestionLabel.add(topTitle).row();
        }
        MyWrappedLabel questionLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontScale(FontManager.getNormalBigFontDim())
                .setFontColor(FontColor.BLACK)
                .setText(StringUtils.capitalize(gameService.getQuestionToBeDisplayed())).build());
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        tableQuestionLabel.add(questionLabel).width(ScreenDimensionsManager.getScreenWidth());
        questionContainer.add(tableQuestionLabel).padBottom(verticalGeneralMarginDimen).row();
        Image image = gameService.getQuestionImage();
        addQuestionImgTableToContainer(image);
        return questionTable;
    }

    protected String getQuestionTopTitle() {
        return "";
    }

    protected void addQuestionImgTableToContainer(Image image) {
        Table questionImgTable = createGroupWithImageAndAnswerOptionsForSideAnswers(image);
        questionContainer.add(questionImgTable)
                .height(questionImgTable.getHeight()).width(questionImgTable.getWidth()).padBottom(0).center();
    }

    @Override
    protected void setContainerBackground() {
    }


    protected Table createGroupWithImageAndAnswerOptionsForSideAnswers(Image image) {
        Question question = getGameQuestionInfo().getQuestion();
        Group grp = createInitialGroupAndAdjustImageForSideAnswers(image);
        Map<MyButton, ImageClickInfo> buttonWithCoordinates = gameService.getAnswerOptionsCoordinates(
                new ArrayList<>(getAllAnswerButtons().values()), question.getQuestionDifficultyLevel(), question.getQuestionCategory());
        ////////////////////
        ////////////////////
        ////////////////////
        for (Map.Entry<MyButton, ImageClickInfo> entry : buttonWithCoordinates.entrySet()) {
            grp.addActor(createRespArrow(entry.getValue(), image, grp, entry.getKey()));
        }
        ////////////////////
        ////////////////////
        ////////////////////
        image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.print((x / image.getWidth()) * 100 + "," + (y / image.getHeight()) * 100 + "\n");
            }
        });
        Table table = new Table();
        table.setWidth(grp.getWidth());
        table.setHeight(grp.getHeight());
        table.add(grp).width(grp.getWidth()).height(grp.getHeight());
        return table;
    }

    private Table createRespArrow(ImageClickInfo coord, Image image, Group group, MyButton respBtn) {
        float arrowWidthPercent = 30;
        if (coord.arrowWidth != null) {
            arrowWidthPercent = coord.arrowWidth;
        }
        float arrowWidth = ScreenDimensionsManager.getScreenWidth(arrowWidthPercent);
        Image arrowLeft = GraphicUtils.getImage(AnatomySpecificResource.arrow_left);
        arrowLeft.setWidth(arrowWidth);
        arrowLeft.setHeight(GameButtonSize.ANATOMY_RESPONSE_ARROW.getHeight());
        float pointerSideDimen = getSideAnswerResponsePointerDimen();
        Image arrowPointer = GraphicUtils.getImage(AnatomySpecificResource.arrow_left_pointer);
        arrowPointer.setWidth(pointerSideDimen);
        arrowPointer.setHeight(pointerSideDimen);
        actorAnimation.animateZoomInZoomOut(arrowPointer, 0.7f);
        Table arrowTable = new Table();
        arrowTable.setHeight(arrowLeft.getHeight());
        arrowTable.setWidth(arrowLeft.getWidth() + pointerSideDimen);
        if (showArrowPointer()) {
            arrowTable.add(arrowPointer).width(pointerSideDimen).height(pointerSideDimen);
        }
        arrowTable.add(arrowLeft).width(arrowLeft.getWidth()).height(arrowLeft.getHeight());
        arrowTable.add(respBtn).width(respBtn.getWidth()).height(respBtn.getHeight());
        setSideArrowPosition(coord, image, group, respBtn, arrowTable);
        return arrowTable;
    }

    protected boolean showArrowPointer() {
        return true;
    }

    protected float getSideAnswerResponsePointerDimen() {
        return MainDimen.horizontal_general_margin.getDimen();
    }

    protected void setSideArrowPosition(ImageClickInfo coord, Image image, Group group, MyButton respBtn, Table arrowTable) {
        arrowTable.setX(image.getWidth() / 100 * coord.x + respBtn.getWidth() / 2 - getSideAnswerResponsePointerDimen() / 2);
        arrowTable.setY(image.getHeight() / 100 * coord.y - respBtn.getHeight() / 2 + arrowTable.getHeight());
    }

    protected Table createGroupWithImageAndAnswerOptionsForExactAnswers(Image image) {
        Question question = getGameQuestionInfo().getQuestion();
        Group grp = createInitialGroupAndAdjustImageForExactAnswers(image);
        Map<MyButton, ImageClickInfo> buttonWithCoordinates = gameService.getAnswerOptionsCoordinates(new ArrayList<>(getAllAnswerButtons().values()), question.getQuestionDifficultyLevel(), question.getQuestionCategory());
        for (Map.Entry<MyButton, ImageClickInfo> entry : buttonWithCoordinates.entrySet()) {
            MyButton button = entry.getKey();
            grp.addActor(button);
            ImageClickInfo coord = entry.getValue();
            button.setTransform(true);
            setExactAnswerButtonPosition(image, grp, button, coord);
            new ActorAnimation(Game.getInstance().getAbstractScreen()).animateZoomInZoomOut(button);
        }
        image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.print((x / image.getWidth()) * 100 + "," + (y / image.getHeight()) * 100 + "\n");
            }
        });
        Table table = new Table();
        table.setWidth(grp.getWidth());
        table.setHeight(grp.getHeight());
        table.add(grp).width(grp.getWidth()).height(grp.getHeight());
        return table;
    }

    protected void setExactAnswerButtonPosition(Image image, Group group, MyButton button, ImageClickInfo coord) {
        button.setPosition(image.getWidth() / 100 * coord.x, image.getHeight() / 100 * coord.y, Align.center);
    }

    protected Group createInitialGroupAndAdjustImageForExactAnswers(Image image) {
        return createInitialGroupAndAdjustImageForAnswers(image);
    }

    protected Group createInitialGroupAndAdjustImageForSideAnswers(Image image) {
        return createInitialGroupAndAdjustImageForAnswers(image);
    }

    private Group createInitialGroupAndAdjustImageForAnswers(Image image) {
        Group grp = new Group();
        float imgHeight = image.getHeight();
        float imgWidth = image.getWidth();
        if (imgWidth > imgHeight) {
            image.setHeight(MainDimen.vertical_general_margin.getDimen() * 18);
            image.setWidth(ScreenDimensionsManager.getNewWidthForNewHeight(image.getHeight(), imgWidth, imgHeight));
        } else {
            image.setWidth(ScreenDimensionsManager.getScreenWidth(90));
            image.setHeight(ScreenDimensionsManager.getNewHeightForNewWidth(image.getWidth(), imgWidth, imgHeight));
        }
        grp.addActor(image);
        grp.setHeight(image.getHeight());
        grp.setWidth(image.getWidth());
        return grp;
    }

    @Override
    public ButtonSkin correctAnswerSkin() {
        return GameButtonSkin.ANSWER_IMAGE_CLICK_CORRECT;
    }

    @Override
    public ButtonSkin wrongAnswerSkin() {
        return GameButtonSkin.ANSWER_IMAGE_CLICK_WRONG;
    }

    @Override
    public int getNrOfAnswerRows() {
        return 2;
    }

    @Override
    public int getNrOfAnswersOnRow() {
        return 2;
    }

    protected GameButtonSkin getAnswerButtonSkin() {
        return GameButtonSkin.ANSWER_IMAGE_CLICK;
    }

    @Override
    protected MyButton createAnswerButton(final String answer) {
        final MyButton button = new ImageButtonBuilder(getAnswerButtonSkin(), Game.getInstance().getAbstractScreen())
                .setFontColor(FontColor.BLACK)
                .setText(StringUtils.capitalize(answer))
                .setFixedButtonSize(GameButtonSize.IMAGE_CLICK_ANSWER_OPTION)
                .build();
        ////////////////////
        ////////////////////
        ////////////////////
        button.getCenterRow().setVisible(false);
//        button.getCenterRow().setVisible(true);
        ////////////////////
        ////////////////////
        ////////////////////
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                button.toFront();
                button.getCenterRow().toFront();
                button.getParent().toFront();
                button.getParent().getParent().toFront();
                button.getCenterRow().setVisible(true);
            }
        });
        return button;
    }

}
