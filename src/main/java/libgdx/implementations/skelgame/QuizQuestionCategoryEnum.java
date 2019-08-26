package libgdx.implementations.skelgame;

import libgdx.campaign.QuestionCategory;
import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.DependentQuizGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;

public enum QuizQuestionCategoryEnum implements QuizQuestionCategory {

    CAT0(DependentQuizGameCreatorDependencies.class),
    CAT1(DependentQuizGameCreatorDependencies.class),
    CAT2(DependentQuizGameCreatorDependencies.class),
    CAT3(DependentQuizGameCreatorDependencies.class),
    CAT4(DependentQuizGameCreatorDependencies.class),
    ;

    private Class<? extends CreatorDependencies> questionCreator;

    QuizQuestionCategoryEnum(Class<? extends CreatorDependencies> questionCreator) {
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
