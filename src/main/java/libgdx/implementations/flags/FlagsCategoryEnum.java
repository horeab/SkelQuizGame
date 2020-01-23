package libgdx.implementations.flags;

import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.DependentQuizGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;

public enum FlagsCategoryEnum implements QuizQuestionCategory {

    cat0(DependentQuizGameCreatorDependencies.class),//Europe
    cat1(DependentQuizGameCreatorDependencies.class),//North America
    cat2(DependentQuizGameCreatorDependencies.class),//Asia and Oceania
    cat3(DependentQuizGameCreatorDependencies.class),//South America
    cat4(DependentQuizGameCreatorDependencies.class),//Africa
    ;

    private Class<? extends CreatorDependencies> questionCreator;

    FlagsCategoryEnum(Class<? extends CreatorDependencies> questionCreator) {
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
