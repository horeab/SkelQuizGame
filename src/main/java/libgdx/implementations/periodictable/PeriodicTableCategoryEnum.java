package libgdx.implementations.periodictable;

import libgdx.implementations.skelgame.gameservice.*;

public enum PeriodicTableCategoryEnum implements QuizQuestionCategory {

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
