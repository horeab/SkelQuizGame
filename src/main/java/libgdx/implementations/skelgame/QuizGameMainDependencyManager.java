package libgdx.implementations.skelgame;

import libgdx.controls.labelimage.InventoryTableBuilderCreator;
import libgdx.controls.popup.RatingService;
import libgdx.game.MainDependencyManager;
import libgdx.game.ScreenManager;
import libgdx.resources.Resource;
import libgdx.resources.ResourceService;
import libgdx.screens.AbstractScreen;
import libgdx.transactions.TransactionsService;

public class QuizGameMainDependencyManager extends MainDependencyManager<ScreenManager, AbstractScreen, QuizGameLabel, Resource, GameIdEnum> {

    @Override
    public Class<Resource> getMainResourcesClass() {
        return Resource.class;
    }

    @Override
    public Class<GameIdEnum> getGameIdClass() {
        return GameIdEnum.class;
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
    public ScreenManager createScreenManager() {
        return new ScreenManager();
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
