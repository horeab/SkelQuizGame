package libgdx.implementations.hangman;

import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;

public enum HangmanQuestionCategoryEnum implements QuizQuestionCategory {

    cat0(HangmanGameCreatorDependencies.class),
    cat1(HangmanGameCreatorDependencies.class),
    cat2(HangmanGameCreatorDependencies.class),
    cat3(HangmanGameCreatorDependencies.class),
    cat4(HangmanGameCreatorDependencies.class),
    cat5(HangmanGameCreatorDependencies.class),
    ;

    private Class<? extends CreatorDependencies> questionCreator;

    HangmanQuestionCategoryEnum(Class<? extends CreatorDependencies> questionCreator) {
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
