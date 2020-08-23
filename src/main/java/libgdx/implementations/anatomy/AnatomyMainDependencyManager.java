package libgdx.implementations.anatomy;

import libgdx.controls.labelimage.InventoryTableBuilderCreator;
import libgdx.controls.popup.RatingService;
import libgdx.game.MainDependencyManager;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.implementations.skelgame.GameRatingService;
import libgdx.skelgameimpl.skelgame.SkelGameLabel;
import libgdx.resources.Resource;
import libgdx.resources.ResourceService;
import libgdx.screen.AbstractScreen;
import libgdx.implementations.screens.implementations.anatomy.AnatomyScreenManager;
import libgdx.transactions.TransactionsService;

public class AnatomyMainDependencyManager extends MainDependencyManager<AnatomyScreenManager, AbstractScreen, SkelGameLabel, Resource, GameIdEnum> {

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
        return new AnatomyGameResourceService();
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
    public AnatomyScreenManager createScreenManager() {
        return new AnatomyScreenManager();
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
