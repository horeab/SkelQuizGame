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
    cat8(DependentQuizGameCreatorDependencies.class),
    cat9(DependentQuizGameCreatorDependencies.class),
    cat10(DependentQuizGameCreatorDependencies.class),
    cat11(DependentQuizGameCreatorDependencies.class),
    cat12(DependentQuizGameCreatorDependencies.class),
    cat13(DependentQuizGameCreatorDependencies.class),
    cat14(DependentQuizGameCreatorDependencies.class),
    cat15(DependentQuizGameCreatorDependencies.class),
    cat16(DependentQuizGameCreatorDependencies.class),
    cat17(DependentQuizGameCreatorDependencies.class),
    cat18(DependentQuizGameCreatorDependencies.class),
    cat19(DependentQuizGameCreatorDependencies.class),
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
