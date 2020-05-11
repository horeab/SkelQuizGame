package libgdx.implementations.animals;

import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;

public enum AnimalsQuestionCategoryEnum implements QuizQuestionCategory {

    cat0(AnimalsGameCreatorDependencies.class),
    cat1(AnimalsGameCreatorDependencies.class),
    cat2(AnimalsGameCreatorDependencies.class),
    cat3(AnimalsGameCreatorDependencies.class),
    cat4(AnimalsGameCreatorDependencies.class),
    ;

    private Class<? extends CreatorDependencies> questionCreator;

    AnimalsQuestionCategoryEnum(Class<? extends CreatorDependencies> questionCreator) {
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
