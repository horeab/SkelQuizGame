package libgdx.implementations.screens.implementations.hangmanarena.spec;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import libgdx.controls.ScreenRunnable;
import libgdx.game.Game;
import libgdx.graphics.GraphicUtils;
import libgdx.implementations.hangmanarena.HangmanArenaSpecificResource;
import libgdx.implementations.skelgame.GameDimen;
import libgdx.implementations.skelgame.gameservice.GameServiceContainer;
import libgdx.implementations.skelgame.gameservice.HangmanGameService;
import libgdx.implementations.skelgame.gameservice.HangmanQuestionContainerCreatorService;
import libgdx.implementations.skelgame.gameservice.HangmanRefreshQuestionDisplayService;
import libgdx.implementations.skelgame.question.GameUser;
import libgdx.resources.Res;
import libgdx.screen.AbstractScreen;
import libgdx.utils.ActorPositionManager;
import libgdx.utils.ScreenDimensionsManager;
import org.apache.commons.lang3.StringUtils;

public class HangmanScreenBackgroundCreator extends AbstractGameScreenBackgroundCreator {

    private static final String ACTOR_NAME_SKY_BACKGROUND = "actorNameSkyBackground";

    private HangmanGameService gameService;

    public HangmanScreenBackgroundCreator(AbstractScreen abstractGameScreen, GameUser gameUser) {
        super(abstractGameScreen, gameUser);
        gameService = (HangmanGameService) GameServiceContainer.getGameService(gameUser.getGameQuestionInfo());
    }

    @Override
    public void createBackground() {
        if (gameService.getQuestionImage() == null) {
            addSkyImages();
            createGrassTexture();
            createForestTexture();
            animateSmokeImage(getAbstractGameScreen(), 66);
            refreshHangManImg(0);
        }
    }

    @Override
    public void refreshBackground(int nrOfWrongAnswersPressed) {
        if (gameService.getQuestionImage() == null) {
            refreshSkyImages(nrOfWrongAnswersPressed, true);
            refreshHangManImg(nrOfWrongAnswersPressed);
        } else {
            refreshAvailableTriesTableForQuestionWithImage(nrOfWrongAnswersPressed);
        }
    }

    private void refreshAvailableTriesTableForQuestionWithImage(int nrOfWrongLettersPressed) {
        for (int i = nrOfWrongLettersPressed - 1; i >= 0; i--) {
            Image image = getAbstractGameScreen().getRoot().findActor(HangmanQuestionContainerCreatorService.AVAILABLE_TRIES_IMAGE_CELL_NAME + i);
            if (image != null) {
                image.addAction(Actions.fadeOut(0.5f));
            }
        }
    }

    public void refreshSkyImages(int nrOfWrongLettersPressed, boolean fadeOut) {
        for (int i = nrOfWrongLettersPressed - 1; i >= 0; i--) {
            Image image = getAbstractGameScreen().getRoot().findActor(ACTOR_NAME_SKY_BACKGROUND + i);
            if (image != null) {
                AlphaAction action = fadeOut ? Actions.fadeOut(0.5f) : Actions.fadeIn(1.5f);
                image.addAction(action);
            }
        }
    }

    private void addSkyImages() {
        for (int i = 6; i >= 0; i--) {
            addSkyImage(i);
        }
    }

    private void addSkyImage(int imgNr) {
        Res imgName = Game.getInstance().getMainDependencyManager().createResourceService().getByName("skyb" + imgNr);
        Image image = GraphicUtils.addTiledImage(imgName, 0, Texture.TextureWrap.Repeat);
        image.setName(ACTOR_NAME_SKY_BACKGROUND + imgNr);
        getAbstractGameScreen().addActor(image);
    }

    private void refreshHangManImg(int nrOfWrongLettersPressed) {
        Res imgName = Game.getInstance().getMainDependencyManager().createResourceService().getByName("h" + nrOfWrongLettersPressed);
        Image image = GraphicUtils.getImage(imgName);
        Image hangmanImage = getAbstractGameScreen().getRoot().findActor(HangmanRefreshQuestionDisplayService.ACTOR_NAME_HANGMAN_IMAGE);
        if (hangmanImage != null) {
            hangmanImage.setDrawable(image.getDrawable());
        } else {
            float hangmanImageDimenDivider = StringUtils.isNotBlank(new HangmanGameService(getGameQuestionInfo().getQuestion()).getQuestionToBeDisplayed()) ? 1.5f : 1;
            float hangmanImageDimen = GameDimen.side_hangman_image.getDimen() / hangmanImageDimenDivider;
            image.setHeight(image.getHeight() / Float.valueOf(image.getWidth()) * hangmanImageDimen);
            image.setWidth(hangmanImageDimen);
            image.setPosition(0, getHangmanImageYPosition());
            ActorPositionManager.setActorCenterHorizontalOnScreen(image);
            image.setName(HangmanRefreshQuestionDisplayService.ACTOR_NAME_HANGMAN_IMAGE);
            getAbstractGameScreen().addActor(image);
        }
    }

    private void createGrassTexture() {
        getAbstractGameScreen().addActor(GraphicUtils.addTiledImage(HangmanArenaSpecificResource.grass_texture, -getHangmanImageYPosition() + ScreenDimensionsManager.getScreenHeight(4), Texture.TextureWrap.MirroredRepeat));
    }

    private static void animateSmokeImage(final AbstractScreen abstractScreen, final int y) {
        try {
            Image image = GraphicUtils.getImage(HangmanArenaSpecificResource.smoke);
            image.setPosition(ScreenDimensionsManager.getScreenWidth(87), ScreenDimensionsManager.getScreenHeight(y));
            image.setHeight(ScreenDimensionsManager.getScreenWidth(2f));
            image.setWidth(ScreenDimensionsManager.getScreenWidth(2f));
            RunnableAction run = new RunnableAction();
            run.setRunnable(new ScreenRunnable(abstractScreen) {
                @Override
                public void executeOperations() {
                    animateSmokeImage(abstractScreen, y);
                }
            });
            image.addAction(Actions.sequence(Actions.delay(1), Actions.scaleBy(10f, 10f, 10), run));
            image.addAction(Actions.sequence(Actions.delay(0), Actions.fadeOut(12)));
            image.addAction(Actions.sequence(Actions.delay(0), Actions.moveTo(image.getX(), ScreenDimensionsManager.getScreenHeight(), 15)));
            abstractScreen.addActor(image);
        } catch (Exception e) {
            //Ignore animation if exception happens
        }
    }

    private void createForestTexture() {
        Image image = GraphicUtils.getImage(HangmanArenaSpecificResource.forest_texture);
        image.setPosition(0, getHangmanImageYPosition());
        image.setHeight(ScreenDimensionsManager.getNewHeightForNewWidth(ScreenDimensionsManager.getScreenWidth(), image.getWidth(), image.getHeight()));
        image.setWidth(ScreenDimensionsManager.getScreenWidth());
        getAbstractGameScreen().addActor(image);
    }

    private float getHangmanImageYPosition() {
        return ScreenDimensionsManager.getScreenHeight(52);
    }
}
