package libgdx.implementations.periodictable;

import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;

public enum PeriodicTableCategoryEnum implements QuizQuestionCategory {

    cat0(PeriodicTableCreatorDependencies.class),//symbol
    cat1(PeriodicTableCreatorDependencies.class),//discoveredBy
    cat2(PeriodicTableCreatorDependencies.class),//yearOfDiscovery
    cat3(PeriodicTableCreatorDependencies.class),//atomicNumber
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
