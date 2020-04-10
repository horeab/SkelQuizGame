package libgdx.implementations.periodictable;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.implementations.periodictable.spec.ChemicalElement;
import libgdx.implementations.periodictable.spec.ChemicalElementsUtil;
import libgdx.implementations.skelgame.gameservice.*;
import libgdx.implementations.skelgame.question.Question;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PeriodicTableCreatorDependencies extends DependentQuizGameCreatorDependencies {

    private List<ChemicalElement> elements;
    private List<Question> allQuestions = new ArrayList<>();

    public PeriodicTableCreatorDependencies() {
        elements = ChemicalElementsUtil.processTextForChemicalElements();
    }

    @Override
    public QuestionCreator getQuestionCreator(QuestionDifficulty questionDifficulty, QuestionCategory questionCategory) {
        return new QuestionCreator() {
            @Override
            public List<Question> getAllQuestions() {
                if (allQuestions != null && !allQuestions.isEmpty()) {
                    return allQuestions;
                } else {
                    for (ChemicalElement e : elements) {
                        for (PeriodicTableCategoryEnum categoryEnum : PeriodicTableCategoryEnum.values()) {
                            String qString = "";
                            if (categoryEnum == PeriodicTableCategoryEnum.cat0) {
                                qString = formQuestionString(e.getSymbol(), e.getAtomicNumber(), "symbol", "Symbol");
                            } else if (categoryEnum == PeriodicTableCategoryEnum.cat1) {
                                qString = formQuestionString(e.getDiscoveredBy(), e.getAtomicNumber(), "discoveredBy", "Discovered by");
                            } else if (categoryEnum == PeriodicTableCategoryEnum.cat2) {
                                qString = formQuestionString(String.valueOf(e.getYearOfDiscovery()), e.getAtomicNumber(), "yearOfDiscovery", "Year of discovery");
                            } else if (categoryEnum == PeriodicTableCategoryEnum.cat3) {
                                qString = formQuestionString(e.getAtomicWeight(), e.getAtomicNumber(), "atomicWeight", "Atomic weight");
                            } else if (categoryEnum == PeriodicTableCategoryEnum.cat4) {
                                qString = formQuestionString(e.getDensity(), e.getAtomicNumber(), "density", "Density");
                            }
                            Question question = new Question(e.getAtomicNumber(), PeriodicTableDifficultyLevel._0, categoryEnum, qString);
                            allQuestions.add(question);
                        }
                    }
                    return allQuestions;
                }
            }

            @Override
            public Question createRandomQuestion() {
                List<Question> shuffledQ = new ArrayList<>(getAllQuestions());
                Collections.shuffle(shuffledQ);
                return shuffledQ.get(0);
            }

            @Override
            public List<Question> getAllQuestions(List<QuestionDifficulty> difficultyLevels, QuestionCategory categoryEnumToCreate) {
                List<Question> allQuestions = getAllQuestions();
                List<Question> result = new ArrayList<>();
                for (Question q : allQuestions) {
                    if (difficultyLevels.contains(q.getQuestionDifficultyLevel())
                            && q.getQuestionCategory() == categoryEnumToCreate) {
                        result.add(q);
                    }
                }
                return result;
            }
        };
    }

    private String formQuestionString(String answer, int atomicNumber, String fieldName, final String questionLabel) {
        return atomicNumber +
                ":" + questionLabel + ":" +
                answer +
                ":" +
                StringUtils.join(getAnswerOptions(3, fieldName, answer), ",");
    }

    private List<Integer> getAnswerOptions(int nrOfOptions, String fieldName, String answer) {
        List<Integer> options = new ArrayList<>();
        List<ChemicalElement> shuffledEls = new ArrayList<>(elements);
        Collections.shuffle(shuffledEls);
        for (ChemicalElement e : shuffledEls) {
            boolean isOptionValid = true;
            switch (fieldName) {
                case "symbol":
                    isOptionValid = isOptionValid(answer, e.getSymbol());
                    break;
                case "discoveredBy":
                    isOptionValid = isOptionValid(answer, e.getDiscoveredBy());
                    break;
                case "yearOfDiscovery":
                    isOptionValid = isOptionValid(answer, String.valueOf(e.getYearOfDiscovery()));
                    break;
                case "atomicWeight":
                    isOptionValid = isOptionValid(answer, e.getAtomicWeight());
                    break;
                case "density":
                    isOptionValid = isOptionValid(answer, e.getDensity());
                    break;
            }
            if (isOptionValid) {
                options.add(e.getAtomicNumber());
            }else {
                int i=0;
            }
            if (options.size() == nrOfOptions) {
                break;
            }
        }
        return options;
    }

    private boolean isOptionValid(String answer, String option) {
        String[] toTestAnswer = cleanString(answer).split(" ");
        String[] toTestOption = cleanString(option).split(" ");
        for (String a : toTestAnswer) {
            for (String o : toTestOption) {
                if (a.equalsIgnoreCase(o)) {
                    return false;
                }
            }
        }
        return true;
    }

    private String cleanString(String s) {
        return s.replace("et al.", "").replace(",", "");
    }
}
