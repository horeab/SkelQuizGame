package libgdx.implementations.periodictable;

import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.DependentQuizGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;

public enum PeriodicTableCategoryEnum implements QuizQuestionCategory {

    cat0(DependentQuizGameCreatorDependencies.class),//symbol
    cat1(DependentQuizGameCreatorDependencies.class),//discoveredBy
    cat2(DependentQuizGameCreatorDependencies.class),//yearOfDiscovery
    cat3(DependentQuizGameCreatorDependencies.class),//atomicWeight
    cat4(DependentQuizGameCreatorDependencies.class),//density
    ;

    private Class<? extends CreatorDependencies> questionCreator;

    PeriodicTableCategoryEnum(Class<? extends CreatorDependencies> questionCreator) {
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
