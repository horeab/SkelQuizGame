package libgdx.implementations.astronomy;

import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;
import libgdx.implementations.skelgame.gameservice.UniqueQuizGameCreatorDependencies;

public enum AstronomyCategoryEnum implements QuizQuestionCategory {

    cat0(UniqueQuizGameCreatorDependencies.class),
    cat1(UniqueQuizGameCreatorDependencies.class),
    cat2(UniqueQuizGameCreatorDependencies.class),
    cat3(UniqueQuizGameCreatorDependencies.class),
    cat4(UniqueQuizGameCreatorDependencies.class),
    ;

    private Class<? extends CreatorDependencies> questionCreator;

    AstronomyCategoryEnum(Class<? extends CreatorDependencies> questionCreator) {
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
