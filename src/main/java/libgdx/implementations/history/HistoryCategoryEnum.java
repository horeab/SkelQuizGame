package libgdx.implementations.history;

import libgdx.implementations.screens.implementations.history.HistoryGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.ImageClickGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;
import libgdx.implementations.skelgame.gameservice.UniqueQuizGameCreatorDependencies;

public enum HistoryCategoryEnum implements QuizQuestionCategory {

    cat0(HistoryGameCreatorDependencies.class),
    cat1(HistoryGameCreatorDependencies.class),
    ;

    private Class<? extends CreatorDependencies> questionCreator;

    HistoryCategoryEnum(Class<? extends CreatorDependencies> questionCreator) {
        this.questionCreator = questionCreator;
    }

    @Override
    public int getIndex() {
        return ordinal();
    }

    @Override
    public Class<? extends CreatorDependencies> getCreatorDependencies() {
        return questionCreator;
    }

}
