package libgdx.implementations.anatomy;

import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.ImageClickGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;

public enum AnatomyQuestionCategoryEnum implements QuizQuestionCategory {

    cat0(ImageClickGameCreatorDependencies.class),
    cat1(ImageClickGameCreatorDependencies.class),
    cat2(ImageClickGameCreatorDependencies.class),
    cat3(ImageClickGameCreatorDependencies.class),
    cat4(ImageClickGameCreatorDependencies.class),
    cat5(ImageClickGameCreatorDependencies.class),
    cat6(ImageClickGameCreatorDependencies.class),
    cat7(ImageClickGameCreatorDependencies.class),
    cat8(ImageClickGameCreatorDependencies.class),
    cat9(ImageClickGameCreatorDependencies.class),
    ;

    private Class<? extends CreatorDependencies> questionCreator;

    AnatomyQuestionCategoryEnum(Class<? extends CreatorDependencies> questionCreator) {
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
