package libgdx.implementations.skelgame.gameservice;

import libgdx.campaign.QuestionCategory;

public interface QuizQuestionCategory extends QuestionCategory {

    Class<? extends CreatorDependencies> getCreatorDependencies();
}
