package libgdx.implementations.history;

import libgdx.controls.labelimage.InventoryTableBuilderCreator;
import libgdx.controls.popup.RatingService;
import libgdx.game.MainDependencyManager;
import libgdx.implementations.screens.implementations.conthistory.ConthistoryScreenManager;
import libgdx.implementations.screens.implementations.history.HistoryScreenManager;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.implementations.skelgame.GameRatingService;
import libgdx.implementations.skelgame.QuizGameResourceService;
import libgdx.resources.Resource;
import libgdx.resources.ResourceService;
import libgdx.screen.AbstractScreen;
import libgdx.skelgameimpl.skelgame.SkelGameLabel;
import libgdx.transactions.TransactionsService;

public class HistoryMainDependencyManager extends MainDependencyManager<HistoryScreenManager, AbstractScreen, SkelGameLabel, Resource, GameIdEnum> {

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
    public Class<SkelGameLabel> getGameLabelClass() {
        return SkelGameLabel.class;
    }

    @Override
    public RatingService createRatingService(AbstractScreen abstractScreen) {
        return new GameRatingService(abstractScreen);
    }

    @Override
    public HistoryScreenManager createScreenManager() {
        return new HistoryScreenManager();
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
