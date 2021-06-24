package libgdx.implementations.screens.implementations.anatomy;

import libgdx.implementations.skelgame.gameservice.GameService;
import libgdx.implementations.skelgame.gameservice.UniqueQuizGameCreatorDependencies;

public class AnatomyUniqueGameCreatorDependencies extends UniqueQuizGameCreatorDependencies {

    @Override
    public Class<? extends GameService> getGameServiceClass() {
        return AnatomyUniqueQuizGameService.class;
    }


}
