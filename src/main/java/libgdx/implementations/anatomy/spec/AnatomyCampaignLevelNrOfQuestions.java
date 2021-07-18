package libgdx.implementations.anatomy.spec;

public class AnatomyCampaignLevelNrOfQuestions {

    public int nrOfQuestionsGeneralK;
    public int nrOfQuestionsIdentify;

    public int getNrOfQuestions(AnatomyGameType gameType) {
        return gameType == AnatomyGameType.GENERALK ? nrOfQuestionsGeneralK : nrOfQuestionsIdentify;
    }
}
