package libgdx.implementations.countries;

import libgdx.implementations.countries.hangman.CountriesAtoZCreatorDependencies;
import libgdx.implementations.countries.hangman.CountriesPopulationAreaNeighbCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.CreatorDependencies;
import libgdx.implementations.skelgame.gameservice.DependentQuizGameCreatorDependencies;
import libgdx.implementations.skelgame.gameservice.QuizQuestionCategory;
import libgdx.implementations.skelgame.gameservice.UniqueQuizGameCreatorDependencies;

public enum CountriesCategoryEnum implements QuizQuestionCategory {

    cat0(CountriesPopulationAreaNeighbCreatorDependencies.class),
    cat1(CountriesPopulationAreaNeighbCreatorDependencies.class),
    cat2(CountriesAtoZCreatorDependencies.class),
    cat3(CountriesPopulationAreaNeighbCreatorDependencies.class),
    cat4(CountriesPopulationAreaNeighbCreatorDependencies.class),
    cat5(CountriesPopulationAreaNeighbCreatorDependencies.class),
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
