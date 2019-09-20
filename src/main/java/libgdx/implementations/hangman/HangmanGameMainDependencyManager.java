package libgdx.implementations.hangman;

import libgdx.controls.labelimage.InventoryTableBuilderCreator;
import libgdx.controls.popup.RatingService;
import libgdx.game.MainDependencyManager;
import libgdx.implementations.skelgame.QuizGameIdEnum;
import libgdx.implementations.skelgame.QuizGameLabel;
import libgdx.implementations.skelgame.QuizGameRatingService;
import libgdx.implementations.skelgame.QuizGameResourceService;
import libgdx.resources.Resource;
import libgdx.resources.ResourceService;
import libgdx.screen.AbstractScreen;
import libgdx.screens.implementations.geoquiz.QuizScreenManager;
import libgdx.screens.implementations.hangman.HangmanScreenManager;
import libgdx.transactions.TransactionsService;

public class HangmanGameMainDependencyManager extends MainDependencyManager<HangmanScreenManager, AbstractScreen, QuizGameLabel, Resource, QuizGameIdEnum> {

    @Override
    public Class<Resource> getMainResourcesClass() {
        return Resource.class;
    }

    @Override
    public Class<QuizGameIdEnum> getGameIdClass() {
        return QuizGameIdEnum.class;
    }

    @Override
    public ResourceService createResourceService() {
        return new QuizGameResourceService();
    }

    @Override
    public Class<QuizGameLabel> getGameLabelClass() {
        return QuizGameLabel.class;
    }

    @Override
    public RatingService createRatingService(AbstractScreen abstractScreen) {
        return new QuizGameRatingService(abstractScreen);
    }

    @Override
    public HangmanScreenManager createScreenManager() {
        return new HangmanScreenManager();
    }

    @Override
    public InventoryTableBuilderCreator createInventoryTableBuilderCreator() {
        throw new RuntimeException("Transactions not implemented for Game");
    }

    @Override
    public TransactionsService getTransactionsService() {
        throw new RuntimeException("Transactions not implemented for Game");
    }
}
