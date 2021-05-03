package libgdx.implementations.astronomy;

import libgdx.implementations.skelgame.gameservice.*;

public enum AstronomyCategoryEnum implements QuizQuestionCategory {

    cat0(ImageClickGameCreatorDependencies.class),
    cat1(UniqueQuizGameCreatorDependencies.class),
    cat2(UniqueQuizGameCreatorDependencies.class),
    cat3(DependentQuizGameCreatorDependencies.class),
    cat4(DependentQuizGameCreatorDependencies.class),
    cat5(UniqueQuizGameCreatorDependencies.class),
    cat6(DependentQuizGameCreatorDependencies.class),
    cat7(DependentQuizGameCreatorDependencies.class),
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
