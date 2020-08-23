package libgdx.implementations.skelgame.gameservice;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import libgdx.utils.model.FontColor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Map;

import libgdx.controls.animations.ActorAnimation;
import libgdx.controls.button.ButtonSkin;
import libgdx.controls.button.MyButton;
import libgdx.controls.button.builders.ImageButtonBuilder;
import libgdx.controls.label.MyWrappedLabel;
import libgdx.controls.label.MyWrappedLabelConfigBuilder;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.skelgame.GameButtonSize;
import libgdx.implementations.skelgame.GameButtonSkin;
import libgdx.implementations.skelgame.question.Question;
import libgdx.resources.FontManager;
import libgdx.resources.MainResource;
import libgdx.resources.dimen.MainDimen;
import libgdx.implementations.screens.GameScreen;
import libgdx.utils.ScreenDimensionsManager;

public class ImageClickQuestionContainerCreatorService extends QuestionContainerCreatorService<ImageClickGameService> {

    public ImageClickQuestionContainerCreatorService(GameContext gameContext, GameScreen abstractGameScreen) {
        super(gameContext, abstractGameScreen);
    }


    @Override
    public Table createAnswerOptionsTable() {
        return new Table();
    }

    @Override
    public Table createQuestionTable() {
        Table questionTable = super.createQuestionTable();
        MyWrappedLabel questionLabel = new MyWrappedLabel(new MyWrappedLabelConfigBuilder()
                .setFontScale(FontManager.getNormalBigFontDim())
                .setFontColor(FontColor.BLACK)
                .setText(StringUtils.capitalize(gameService.getQuestionToBeDisplayed())).build());
        Table table = new Table();
        table.setBackground(GraphicUtils.getNinePatch(MainResource.popup_background));
        float verticalGeneralMarginDimen = MainDimen.vertical_general_margin.getDimen();
        table.add(questionLabel);
        questionContainer.add(table).padBottom(verticalGeneralMarginDimen).row();
        Group group = createGroupWithImageAndAnswerOptions();
        questionContainer.add(group).height(group.getHeight()).width(group.getWidth()).padBottom(0).center();
        return questionTable;
    }

    @Override
    protected void setContainerBackground() {
    }

    private Group createGroupWithImageAndAnswerOptions() {
        Question question = getGameQuestionInfo().getQuestion();
        final Image image = gameService.getQuestionImage();
        Group grp = new Group();
        float imgHeight = image.getHeight();
        float imgWidth = image.getWidth();
        if (imgWidth > imgHeight) {
            image.setHeight(MainDimen.vertical_general_margin.getDimen() * 18);
            image.setWidth(ScreenDimensionsManager.getNewWidthForNewHeight(image.getHeight(), imgWidth, imgHeight));
        } else {
            image.setWidth(ScreenDimensionsManager.getScreenWidthValue(90));
            image.setHeight(ScreenDimensionsManager.getNewHeightForNewWidth(image.getWidth(), imgWidth, imgHeight));
        }
        grp.addActor(image);
        grp.setHeight(image.getHeight());
        grp.setWidth(image.getWidth());
        Map<MyButton, Pair<Integer, Integer>> buttonWithCoordinates = gameService.getAnswerOptionsCoordinates(new ArrayList<>(getAllAnswerButtons().values()), question.getQuestionDifficultyLevel(), question.getQuestionCategory());
        for (Map.Entry<MyButton, Pair<Integer, Integer>> entry : buttonWithCoordinates.entrySet()) {
            MyButton button = entry.getKey();
            grp.addActor(button);
            Pair<Integer, Integer> coord = entry.getValue();
            button.setTransform(true);
            button.setPosition(image.getWidth() / 100 * coord.getKey(), image.getHeight() / 100 * coord.getValue(), Align.center);
            new ActorAnimation(button, Game.getInstance().getAbstractScreen()).animateZoomInZoomOut();
        }
        image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.print((x / image.getWidth()) * 100 + "," + (y / image.getHeight()) * 100 + "\n");
            }
        });
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

    @Override
    protected MyButton createAnswerButton(final String answer) {
        final MyButton button = new ImageButtonBuilder(GameButtonSkin.ANSWER_IMAGE_CLICK, Game.getInstance().getAbstractScreen())
                .setFontColor(FontColor.BLACK)
                .setText(StringUtils.capitalize(answer))
                .setFixedButtonSize(GameButtonSize.IMAGE_CLICK_ANSWER_OPTION)
                .build();
        button.getCenterRow().setVisible(false);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                button.toFront();
                button.getCenterRow().setVisible(true);
            }
        });
        return button;
    }

}
