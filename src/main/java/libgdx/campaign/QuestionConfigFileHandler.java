package libgdx.campaign;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.List;

import libgdx.game.Game;
import libgdx.utils.EnumUtils;

public class QuestionConfigFileHandler {


    public String getFileText(QuestionDifficulty difficultyLevelToCreate, QuestionCategory categoryEnumToCreate) {
        return getInternalFile(difficultyLevelToCreate, categoryEnumToCreate).readString();
    }

    /**
     * tournament_resources/implementations/gamid/{path param}
     */
    public String getFileText(String path) {
        return Gdx.files.internal(Game.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + path).readString();
    }

    public List<QuestionDifficulty> getQuestionDifficultiesForCategory(QuestionCategory questionCategory) {
        List<QuestionDifficulty> questionDifficulties = new ArrayList<>();
        for (QuestionDifficulty questionDifficulty : (QuestionDifficulty[]) EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionDifficultyTypeEnum())) {
            if (questionConfigExists(questionDifficulty, questionCategory)) {
                questionDifficulties.add(questionDifficulty);
            }
        }
        return questionDifficulties;
    }

    public List<QuestionCategory> getQuestionCategoriesForDifficulty(QuestionDifficulty questionDifficulty) {
        List<QuestionCategory> questionCategories = new ArrayList<>();
        for (QuestionCategory questionCategory : (QuestionCategory[]) EnumUtils.getValues(CampaignGame.getInstance().getSubGameDependencyManager().getQuestionCategoryTypeEnum())) {
            if (questionConfigExists(questionDifficulty, questionCategory)) {
                questionCategories.add(questionCategory);
            }
        }
        return questionCategories;
    }

    public boolean questionConfigExists(QuestionDifficulty difficultyLevelToCreate, QuestionCategory categoryEnumToCreate) {
        return getInternalFile(difficultyLevelToCreate, categoryEnumToCreate).exists();
    }

    private FileHandle getInternalFile(QuestionDifficulty difficultyLevelToCreate, QuestionCategory categoryEnumToCreate) {
        return Gdx.files.internal(getQuestionFilePath(difficultyLevelToCreate, categoryEnumToCreate));
    }

    private String getQuestionFilePath(QuestionDifficulty difficultyLevelToCreate, QuestionCategory categoryEnumToCreate) {
        return Game.getInstance().getAppInfoService().getImplementationGameResourcesFolder() + "questions/" + getLanguage() + "/diff" + difficultyLevelToCreate.getIndex() + "/questions_diff" + difficultyLevelToCreate.getIndex() + "_cat" + categoryEnumToCreate.getIndex() + ".txt";
    }

    protected String getLanguage() {
        return Game.getInstance().getAppInfoService().getLanguage();
    }
}
