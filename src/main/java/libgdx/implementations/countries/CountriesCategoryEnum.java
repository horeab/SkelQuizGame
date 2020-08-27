package libgdx.implementations.countries;

import libgdx.implementations.countries.hangman.CountriesAtoZCreatorDependencies;
import libgdx.implementations.countries.hangman.CountriesHangmanCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.DependentQuizGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;
import libgdx.implementations.skelgame.gameservice.UniqueQuizGameCreatorDependencies;

public enum CountriesCategoryEnum implements QuizQuestionCategory {

    cat0(CountriesHangmanCreatorDependencies.class),
    cat1(CountriesAtoZCreatorDependencies.class),
    cat2(UniqueQuizGameCreatorDependencies.class),
    cat3(DependentQuizGameCreatorDependencies.class),
    cat4(DependentQuizGameCreatorDependencies.class),
    cat5(UniqueQuizGameCreatorDependencies.class),
    ;

    private Class<? extends CreatorDependencies> questionCreator;

    CountriesCategoryEnum(Class<? extends CreatorDependencies> questionCreator) {
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
