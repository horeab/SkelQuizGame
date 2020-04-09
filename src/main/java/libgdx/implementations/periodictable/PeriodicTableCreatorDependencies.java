package libgdx.implementations.periodictable;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.controls.button.MyButton;
import libgdx.implementations.periodictable.spec.ChemicalElement;
import libgdx.implementations.periodictable.spec.ChemicalElementsUtil;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.Question;
import libgdx.screen.AbstractScreen;
import libgdx.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PeriodicTableCreatorDependencies extends DependentQuizGameCreatorDependencies {

    private List<ChemicalElement> elements;

    public PeriodicTableCreatorDependencies() {
        elements = ChemicalElementsUtil.processTextForChemicalElements();
    }

    @Override
    public QuestionCreator getQuestionCreator(QuestionDifficulty questionDifficulty, QuestionCategory questionCategory) {
        return new QuestionCreator() {
            @Override
            public List<Question> getAllQuestions() {
                List<Question> questions = new ArrayList<>();
                int i = 0;
                for (ChemicalElement e : elements) {
                    for (PeriodicTableCategoryEnum categoryEnum : PeriodicTableCategoryEnum.values()) {
                        String qString = "";
                        if (categoryEnum == PeriodicTableCategoryEnum.cat0) {
                            //1:Acest judeţ se află în provincia istorică Crişana:Arad:4,3,7:
                            qString = "xxx";
                        }
                        Question question = new Question(i, PeriodicTableDifficultyLevel._0, categoryEnum, qString);
                        questions.add(question);
                    }
                }
                return questions;
            }

        };
    }
}
