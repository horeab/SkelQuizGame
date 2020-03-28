package libgdx.implementations.hangmanarena;

import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;

public enum HangmanArenaQuestionCategoryEnum implements QuizQuestionCategory {

    cat0(HangmanArenaGameCreatorDependencies.class),
    cat1(HangmanArenaGameCreatorDependencies.class),
    cat2(HangmanArenaGameCreatorDependencies.class),
    cat3(HangmanArenaGameCreatorDependencies.class),
    cat4(HangmanArenaGameCreatorDependencies.class),
    ;

    private Class<? extends CreatorDependencies> questionCreator;

    HangmanArenaQuestionCategoryEnum(Class<? extends CreatorDependencies> questionCreator) {
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
