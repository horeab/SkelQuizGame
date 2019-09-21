package libgdx.implementations.hangman;

import libgdx.campaign.CampaignLevel;

public enum HangmanCampaignLevelEnum implements CampaignLevel {

    //This order is displayed on the campaign screen
    LEVEL_0_0,
    LEVEL_0_1,
    LEVEL_0_2,
    LEVEL_0_3,
    LEVEL_0_4,
    LEVEL_0_5,
    ////
    LEVEL_1_0,
    LEVEL_1_1,
    LEVEL_1_2,
    LEVEL_1_3,
    LEVEL_1_4,
    LEVEL_1_5,
    ////
    LEVEL_2_0,
    LEVEL_2_1,
    LEVEL_2_2,
    LEVEL_2_3,
    LEVEL_2_4,
    LEVEL_2_5,
    ////
    LEVEL_3_0,
    LEVEL_3_1,
    LEVEL_3_2,
    LEVEL_3_3,
    LEVEL_3_4,
    LEVEL_3_5,
    ////
    LEVEL_4_0,
    LEVEL_4_1,
    LEVEL_4_2,
    LEVEL_4_3,
    LEVEL_4_4,
    LEVEL_4_5,
    ////
    LEVEL_5_0,
    LEVEL_5_1,
    LEVEL_5_2,
    LEVEL_5_3,
    LEVEL_5_4,
    LEVEL_5_5,
    ;

    @Override
    public int getIndex() {
        return ordinal();
    }

    @Override
    public String getName() {
        return name();
    }

}
