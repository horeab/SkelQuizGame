package libgdx.implementations.countries;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.game.Game;

public class CountriesQuestionCreator {

    private FileHandle getInternalFile(QuestionDifficulty difficultyLevelToCreate, QuestionCategory categoryEnumToCreate) {
        return Gdx.files.internal(getQuestionFilePath(difficultyLevelToCreate, categoryEnumToCreate));
    }

    private String getQuestionFilePath(QuestionDifficulty difficultyLevelToCreate, QuestionCategory categoryEnumToCreate) {
        return Game.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + "questions/aquestions/diff" + difficultyLevelToCreate.getIndex() + "/questions_diff" + difficultyLevelToCreate.getIndex() + "_cat" + categoryEnumToCreate.getIndex() + ".txt";
    }

    public String getFileText(QuestionDifficulty difficultyLevelToCreate, QuestionCategory categoryEnumToCreate) {
        return getInternalFile(difficultyLevelToCreate, categoryEnumToCreate).readString();
    }

    public String getCountriesFileText() {
        return Gdx.files.internal(Game.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + "questions/" + getLanguage() + "/countries.txt").readString();
    }

    public String getSynonymsFileText() {
        return Gdx.files.internal(Game.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + "questions/" + getLanguage() + "/synonyms.txt").readString();
    }

    protected String getLanguage() {
        return Game.getInstance().getAppInfoService().getLanguage();
    }
}
