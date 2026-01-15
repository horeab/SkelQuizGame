package libgdx.campaign;

import java.util.List;

import libgdx.game.SubGameDependencyManager;
import libgdx.resources.IncrementingRes;
import libgdx.resources.SpecificResource;
import libgdx.resources.gamelabel.GameLabel;
import libgdx.skelgameimpl.skelgame.SkelGameLabel;

public abstract class CampaignGameDependencyManager extends SubGameDependencyManager {

    public abstract <T extends Enum<T> & SpecificResource> Class<T> getSpecificResourceTypeEnum();

    public abstract List<? extends IncrementingRes> getIncrementResList();

    public abstract <T extends Enum<T>> Class<T> getCampaignLevelTypeEnum();

    public abstract <T extends Enum<T> & QuestionCategory> Class<T> getQuestionCategoryTypeEnum();

    public abstract <T extends Enum<T> & QuestionDifficulty> Class<T> getQuestionDifficultyTypeEnum();

    public QuestionConfigFileHandler getQuestionConfigFileHandler() {
        return new QuestionConfigFileHandler();
    }

    @Override
    public String getAllFontChars() {
        String allChars = super.getAllFontChars();
        StringBuilder labels = new StringBuilder();
        for (GameLabel label : SkelGameLabel.values()) {
            labels.append(label.getText());
        }
        return allChars + labels.toString();
    }
}
