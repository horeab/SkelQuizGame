package libgdx.implementations.judetelerom;

import libgdx.implementations.hangman.HangmanGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.DependentQuizGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.ImageClickGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;

public enum JudeteleRomCategoryEnum implements QuizQuestionCategory {

    cat0(ImageClickGameCreatorDependencies.class),//Find on map
    cat1(DependentQuizGameCreatorDependencies.class),//Car ID
    cat2(DependentQuizGameCreatorDependencies.class),//Tourist attractions
    cat3(DependentQuizGameCreatorDependencies.class),//Trivia
    cat4(HangmanGameCreatorDependencies.class),//Capital city
    ;

    private Class<? extends CreatorDependencies> questionCreator;

    JudeteleRomCategoryEnum(Class<? extends CreatorDependencies> questionCreator) {
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
