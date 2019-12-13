package libgdx.implementations.paintings;

import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.DependentQuizGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.ImageClickGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;

public enum PaintingsQuestionCategoryEnum implements QuizQuestionCategory {

    cat0(DependentQuizGameCreatorDependencies.class),
    cat1(DependentQuizGameCreatorDependencies.class),
    cat2(DependentQuizGameCreatorDependencies.class),
    cat3(DependentQuizGameCreatorDependencies.class),
    cat4(DependentQuizGameCreatorDependencies.class),
    ;

    private Class<? extends CreatorDependencies> questionCreator;

    PaintingsQuestionCategoryEnum(Class<? extends CreatorDependencies> questionCreator) {
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
