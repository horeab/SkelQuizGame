package libgdx.implementations.animals;

import libgdx.controls.labelimage.InventoryTableBuilderCreator;
import libgdx.controls.popup.RatingService;
import libgdx.game.MainDependencyManager;
import libgdx.implementations.skelgame.GameIdEnum;
import libgdx.implementations.skelgame.GameRatingService;
import libgdx.implementations.skelgame.QuizGameResourceService;
import libgdx.skelgameimpl.skelgame.SkelGameLabel;
import libgdx.resources.Resource;
import libgdx.resources.ResourceService;
import libgdx.screen.AbstractScreen;
import libgdx.implementations.screens.implementations.animals.AnimalsScreenManager;
import libgdx.transactions.TransactionsService;

public class AnimalsGameMainDependencyManager extends MainDependencyManager<AnimalsScreenManager, AbstractScreen, SkelGameLabel, Resource, GameIdEnum> {

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
    public AnimalsScreenManager createScreenManager() {
        return new AnimalsScreenManager();
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
